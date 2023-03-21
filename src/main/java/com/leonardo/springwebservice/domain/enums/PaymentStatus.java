package com.leonardo.springwebservice.domain.enums;

public enum PaymentStatus {

    PENDENTE(1, "Pendente"),
    QUITADO(2, "Quitado"),
    CANCELADO(3, "Cancelado");
    
    private int cod;
    private String description;

    private PaymentStatus(int cod, String description) {
        this.cod = cod;
        this.description = description;
    }

    public int getCod() {
        return cod;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentStatus toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }
        for (PaymentStatus x : PaymentStatus.values()) {
            if (cod == x.getCod()) {
                return x;
            }
        }
        throw new IllegalArgumentException("Id invalido: " + cod);
    }
}
