package com.leonardo.springwebservice.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.leonardo.springwebservice.domain.Client;
import com.leonardo.springwebservice.domain.InvoicePayment;
import com.leonardo.springwebservice.domain.Order;
import com.leonardo.springwebservice.domain.OrderItem;
import com.leonardo.springwebservice.domain.enums.PaymentStatus;
import com.leonardo.springwebservice.repositories.OrderItemRepository;
import com.leonardo.springwebservice.repositories.OrderRepository;
import com.leonardo.springwebservice.repositories.PaymentRepository;
import com.leonardo.springwebservice.security.UserSS;
import com.leonardo.springwebservice.services.exceptions.AuthorizationException;
import com.leonardo.springwebservice.services.exceptions.ObjectNotFoundException;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmailService emailService;

    public Order findById(Integer id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Order.class.getName()));
    }

    @Transactional
    public Order insert(Order order) {
        order.setId(null);
        order.setDate(new Date());
        order.setClient(clientService.findById(order.getClient().getId()));
        order.getPayment().setPaymentStatus(PaymentStatus.PENDENTE);
        order.getPayment().setOrder(order);
        if (order.getPayment() instanceof InvoicePayment) {
             InvoicePayment payment = (InvoicePayment) order.getPayment();
             invoiceService.addInvoicePayment(payment, order.getDate());   
        }
        order = orderRepository.save(order);
        paymentRepository.save(order.getPayment());
        for (OrderItem item : order.getOrderItems()) {
            item.setDiscount(0.0);
            item.setProduct(productService.findById(item.getProduct().getId()));
            item.setPrice(item.getProduct().getPrice());
            item.setOrder(order);
        }
        orderItemRepository.saveAll(order.getOrderItems());
        emailService.sendOrderConfirmationHtmlEmail(order);
        return order;
    }

    public Page<Order> findByPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        UserSS userSS = UserService.authenticated();
        if (userSS == null) {
            throw new AuthorizationException("Acesso Negado");
        }
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        Client client = clientService.findById(userSS.getId());
        return orderRepository.findByClient(client, pageRequest);
    }
}
