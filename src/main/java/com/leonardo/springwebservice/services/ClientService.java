package com.leonardo.springwebservice.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.leonardo.springwebservice.domain.Adress;
import com.leonardo.springwebservice.domain.City;
import com.leonardo.springwebservice.domain.Client;
import com.leonardo.springwebservice.domain.dto.ClientDTO;
import com.leonardo.springwebservice.domain.dto.ClientNewDTO;
import com.leonardo.springwebservice.domain.enums.ClientType;
import com.leonardo.springwebservice.domain.enums.Profile;
import com.leonardo.springwebservice.repositories.AdressRepository;
import com.leonardo.springwebservice.repositories.ClientRepository;
import com.leonardo.springwebservice.security.UserSS;
import com.leonardo.springwebservice.services.exceptions.AuthorizationException;
import com.leonardo.springwebservice.services.exceptions.DataIntegrityException;
import com.leonardo.springwebservice.services.exceptions.ObjectNotFoundException;

@Service
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdressRepository adressRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ImageService imageService;

    @Value("${img.prefix.client.profile}")
    private String prefix;

    @Value("${img.profile.size}")
    private Integer size;

    public List<Client> findAll() {
        return  clientRepository.findAll();
    }

    public Client findById(Integer id) {
        UserSS userSS = UserService.authenticated();
        if (userSS == null || !userSS.hasRole(Profile.ADMIN) && !id.equals(userSS.getId())) {
            throw new AuthorizationException("Acesso Negado");
        }
        Optional<Client> client = clientRepository.findById(id);
        return client.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Client.class.getName()));
    }

    public Client findByEmail(String email) {
        UserSS userSS = UserService.authenticated();
        if (userSS == null || !userSS.hasRole(Profile.ADMIN) && !email.equals(userSS.getUsername())) {
            throw new AuthorizationException("Acesso Negado");
        }
        Client client = clientRepository.findByEmail(email);
        if (client == null) {
            throw new ObjectNotFoundException("Cliente: " + userSS.getId() + " não encontrado!");
        }
        return client;
    }

    public Client insert(Client client) {
        client.setId(null);
        client = clientRepository.save(client);
        adressRepository.saveAll(client.getAdresses());
        return client;
    }

    public Client update(Client client) {
        Client newClient = findById(client.getId());
        updateData(newClient, client);
        return clientRepository.save(newClient);
    }

    private void updateData(Client newClient, Client client) {
        newClient.setName(client.getName());
        newClient.setEmail(client.getEmail());
    }

    public void deleteById(Integer id) {
        try {
            clientRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("O cliente possui pedidos relacionados");
        }
    }

    public Page<Client> findByPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        return clientRepository.findAll(pageRequest);
    }

    public Client fromDto(ClientDTO clientDto) {
        return new Client(clientDto.getId(), clientDto.getName(), clientDto.getEmail(), null, null, null);
    }

    public Client fromDto(ClientNewDTO clientNewDTO) {
        Client client = new Client(null, clientNewDTO.getName(), clientNewDTO.getEmail(), clientNewDTO.getIdNumber(), ClientType.toEnum(clientNewDTO.getType()), passwordEncoder.encode(clientNewDTO.getPassword()));
        City city = new City(clientNewDTO.getCityId(), null, null);
        Adress adress = new Adress(null, clientNewDTO.getStreetName(), clientNewDTO.getNumber(), clientNewDTO.getAdditionalInfo(), clientNewDTO.getDistrict(), clientNewDTO.getZipcode(), client, city);
        client.getAdresses().add(adress);
        client.getPhoneList().add(clientNewDTO.getPhone1());
        if (clientNewDTO.getPhone2() != null) {
            client.getPhoneList().add(clientNewDTO.getPhone2());
        }
        if (clientNewDTO.getPhone3() != null) {
            client.getPhoneList().add(clientNewDTO.getPhone3());
        }
        return client;
    }

    public URI uploadProfilePicture(MultipartFile multipartFile) {
        UserSS userSS = UserService.authenticated();
        if (userSS == null) {
            throw new AuthorizationException("Acesso Negado");
        }
        BufferedImage bufferedImage = imageService.getJpgImageFromFile(multipartFile);
        bufferedImage = imageService.cropSquare(bufferedImage);
        bufferedImage = imageService.resize(bufferedImage, size);
        String fileName = prefix + userSS.getId() + ".jpg";
        return s3Service.uploadFile(imageService.getInputStream(bufferedImage, "jpg"), fileName, "image");
    }
}