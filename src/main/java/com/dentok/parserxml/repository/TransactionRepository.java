package com.dentok.parserxml.repository;

import com.dentok.parserxml.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
