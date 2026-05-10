package com.devsu.service;

import com.devsu.model.Client;
import com.devsu.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public Optional<Client> getById(Long id) {
        return clientRepository.findById(id);
    }

    public Client create(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> update(Long id, Client updatedClient) {
        return clientRepository.findById(id).map(existing -> {
            existing.setName(updatedClient.getName());
            existing.setGender(updatedClient.getGender());
            existing.setAge(updatedClient.getAge());
            existing.setIdentification(updatedClient.getIdentification());
            existing.setAddress(updatedClient.getAddress());
            existing.setPhoneNumber(updatedClient.getPhoneNumber());
            existing.setPassword(updatedClient.getPassword());
            existing.setStatus(updatedClient.getStatus());
            return clientRepository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
