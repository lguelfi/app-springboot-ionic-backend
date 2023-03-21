package com.leonardo.springwebservice.domain.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.leonardo.springwebservice.domain.Client;
import com.leonardo.springwebservice.services.validation.ClientUpdate;

@ClientUpdate
public class ClientDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    @NotEmpty(message = "Preenchimento Obrigatório")
    @Length(min = 5, max = 120, message = "O nome deve ter entre 5 a 120 caracteres")
    private String name;

    @NotEmpty(message = "Preenchimento Obrigatório")
    @Email(message = "Email Inválido")
    private String email;

    public ClientDTO() {
    }
    
    public ClientDTO(Client client) {
        id = client.getId();
        name = client.getName();
        email = client.getEmail();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }   
}
