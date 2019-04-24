package com.dentok.parserxml.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(
            generator = "UUID"
    )
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(
            name = "id",
            updatable = false,
            nullable = false
    )
    private UUID id;

    private Float amount;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(
            name = "client_id",
            referencedColumnName = "id"
    )
    private Client client;

    private String currency;

    private String place;

    private String card;

    public Transaction() {
    }

    public Transaction(Float amount, Client client, String currency, String place, String card) {
        this.amount = amount;
        this.client = client;
        this.currency = currency;
        this.place = place;
        this.card = card;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    @Override
    public String toString() {
        return "ClassPojo [amount = " + amount + ", client = " + client + ", currency = " + currency + ", place = " + place + ", card = " + card + "]";
    }
}

