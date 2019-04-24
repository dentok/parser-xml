package com.dentok.parserxml.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
public class Client {

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

    @OneToMany(
            mappedBy = "client",
            cascade = {CascadeType.ALL},
            orphanRemoval = true
    )
    private Set<Transaction> transactions = new HashSet<>();

    private String firstName;

    private String lastName;

    private String inn;

    private String middleName;

    public Client() {
    }

    public Client(Set<Transaction> transactions, String firstName, String lastName, String inn, String middleName) {
        this.transactions = transactions;
        this.firstName = firstName;
        this.lastName = lastName;
        this.inn = inn;
        this.middleName = middleName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Set<Transaction> getTransactions() {
        return this.transactions;
    }

    /**
     * @param transactions Set transaction entities
     */
    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
        this.transactions.stream().filter(transaction -> transaction.getClient() != this).forEach(transaction -> transaction.setClient(this));
    }

    /**
     * @param transaction add Bot entity
     */
    public void addTransactions(Transaction transaction) {
        this.transactions.add(transaction);
        if (transaction.getClient() != this) {
            transaction.setClient(this);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (id != null ? !id.equals(client.id) : client.id != null) return false;
        if (transactions != null ? !transactions.equals(client.transactions) : client.transactions != null)
            return false;
        if (firstName != null ? !firstName.equals(client.firstName) : client.firstName != null) return false;
        if (lastName != null ? !lastName.equals(client.lastName) : client.lastName != null) return false;
        if (inn != null ? !inn.equals(client.inn) : client.inn != null) return false;
        return middleName != null ? middleName.equals(client.middleName) : client.middleName == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (transactions != null ? transactions.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (inn != null ? inn.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", transactions=" + Arrays.toString(((Set) this.transactions.stream().map(Transaction::getId).collect(Collectors.toSet())).toArray(new String[0])) +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", inn='" + inn + '\'' +
                ", middleName='" + middleName + '\'' +
                '}';
    }
}
