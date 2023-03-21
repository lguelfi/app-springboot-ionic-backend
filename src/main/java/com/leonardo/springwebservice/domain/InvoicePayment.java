package com.leonardo.springwebservice.domain;

import java.util.Date;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.leonardo.springwebservice.domain.enums.PaymentStatus;

@Entity
@JsonTypeName("pagamentoComBoleto")
public class InvoicePayment extends Payment {
    private static final long serialVersionUID = 1L;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dueDate;
    
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date paymentDate;

    public InvoicePayment(){
    }

    public InvoicePayment(Integer id, PaymentStatus paymentStatus, Order order, Date dueDate, Date paymentDate) {
        super(id, paymentStatus, order);
        this.dueDate = dueDate;
        this.paymentDate = paymentDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
