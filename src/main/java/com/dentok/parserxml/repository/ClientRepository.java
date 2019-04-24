package com.dentok.parserxml.repository;

import com.dentok.parserxml.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, String> {

    /**
     * find client by INN
     *
     * @param inn The Inn of client, unique filed
     * @return Client Object
     */
    Optional<Client> findClientByInn(String inn);
}
