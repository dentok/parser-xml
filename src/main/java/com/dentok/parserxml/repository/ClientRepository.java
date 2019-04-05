package com.dentok.parserxml.repository;

import com.dentok.parserxml.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {
}
