package com.leonardo.springwebservice.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.leonardo.springwebservice.domain.Client;
import com.leonardo.springwebservice.domain.dto.ClientNewDTO;
import com.leonardo.springwebservice.domain.enums.ClientType;
import com.leonardo.springwebservice.repositories.ClientRepository;
import com.leonardo.springwebservice.resources.exceptions.FieldMessage;
import com.leonardo.springwebservice.services.validation.utils.BR;

public class ClientInsertValidator implements ConstraintValidator<ClientInsert, ClientNewDTO> {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void initialize(ClientInsert ann) {
    }

    @Override
    public boolean isValid(ClientNewDTO clientNewDTO, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();
        
        if (clientNewDTO.getType().equals(ClientType.PESSOAFISICA.getCod()) && !BR.isValidCPF(clientNewDTO.getIdNumber())) {
            list.add(new FieldMessage("idNumber", "CPF Inválido"));
        }

        if (clientNewDTO.getType().equals(ClientType.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(clientNewDTO.getIdNumber())) {
            list.add(new FieldMessage("idNumber", "CNPJ Inválido"));
        }

        Client client = clientRepository.findByEmail(clientNewDTO.getEmail());
        if (client != null) {
            list.add(new FieldMessage("email", "Email já existente"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addPropertyNode(e.getFieldName()).addConstraintViolation();
        }
        return list.isEmpty();
    }
}
