package com.leonardo.springwebservice.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.leonardo.springwebservice.domain.InvoicePayment;

@Service
public class InvoiceService {
    
    public void addInvoicePayment(InvoicePayment payment, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 7);
        payment.setDueDate(cal.getTime());
    }
}
