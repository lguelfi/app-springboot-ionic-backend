package com.leonardo.springwebservice.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.leonardo.springwebservice.domain.Client;
import com.leonardo.springwebservice.repositories.ClientRepository;
import com.leonardo.springwebservice.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {
    
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BCryptPasswordEncoder encoder; 

    @Autowired
    private EmailService emailService;

    private Random random = new Random();

    public void sendNewPassword(String email) {
        Client client = clientRepository.findByEmail(email);
        if (client == null) {
            throw new ObjectNotFoundException("Email não encontrado");
        }
        String newPassword = newPassword();
        client.setPassword(encoder.encode(newPassword));
        clientRepository.save(client);
        emailService.sendNewPasswordEmail(client, newPassword);
    }

    private String newPassword() {
        char[] vet = new char[10];
        for (int i = 0; i < vet.length; i++) {
            vet[i] = randomChar();
        }
        return new String(vet);
    }

    private char randomChar() {
        int opt = random.nextInt(3);
        if (opt == 0) {   //gera um dígito
            return (char) (random.nextInt(10) + 48);
        } 
        else if (opt == 1) { // gera uma letra maiuscula
            return (char) (random.nextInt(26) + 65);
        } 
        else { // gera uma letra minuscula
            return (char) (random.nextInt(26) + 97);
        }
    }
}
