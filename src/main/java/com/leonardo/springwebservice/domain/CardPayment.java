package com.leonardo.springwebservice.domain;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.leonardo.springwebservice.domain.enums.PaymentStatus;

@Entity
@JsonTypeName("pagamentoComCartao")
public class CardPayment extends Payment {
    private static final long serialVersionUID = 1L;
    
    private Integer paymentNumber;

    public CardPayment() {
    }

    public CardPayment(Integer id, PaymentStatus paymentStatus, Order order, Integer paymentNumber) {
        super(id, paymentStatus, order);
        this.paymentNumber = paymentNumber;
    }

    public Integer getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(Integer paymentNumber) {
        this.paymentNumber = paymentNumber;
    }
}
