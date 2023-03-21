package com.leonardo.springwebservice.domain.enums;

public enum Profile {
    
    ADMIN(1, "ROLE_ADMIN"),
    CLIENTE(2, "ROLE_CLIENTE");

    private int cod;
    private String description;

    private Profile(int cod, String description) {
        this.cod = cod;
        this.description = description;
    }

    public int getCod() {
        return cod;
    }

    public String getDescription() {
        return description;
    }

    public static Profile toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }
        for (Profile x : Profile.values()) {
            if (cod == x.getCod()) {
                return x;
            }
        }
        throw new IllegalArgumentException("Id invalido: " + cod);
    }
}
