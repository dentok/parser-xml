package com.dentok.parserxml.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "client")
public class Client implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private Integer clientNumber;
    private String name;
    private String surName;
    private Integer age;
    private String company;

    /**
     * Instantiate newly created {@code Client} class
     */
    public Client() {
    }

    /**
     * @param clientNumber is a clientNumber of client
     * @param name         is a name of client
     * @param surName      is a sur Name of client
     * @param age          is a age of client
     * @param company      is a name company of client
     */
    public Client(Integer clientNumber, String name, String surName, Integer age, String company) {
        this.clientNumber = clientNumber;
        this.name = name;
        this.surName = surName;
        this.age = age;
        this.company = company;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getClientNumber() {
        return clientNumber;
    }

    public void setClientNumber(Integer clientNumber) {
        this.clientNumber = clientNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", clientNumber=" + clientNumber +
                ", name='" + name + '\'' +
                ", surName='" + surName + '\'' +
                ", age=" + age +
                ", company='" + company + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (id != null ? !id.equals(client.id): client.id != null) return false;
        if (clientNumber != null ? !clientNumber.equals(client.clientNumber) : client.clientNumber != null)
            return false;
        if (name != null ? !name.equals(client.name) : client.name != null) return false;
        if (surName != null ? !surName.equals(client.surName) : client.surName != null) return false;
        if (age != null ? !age.equals(client.age) : client.age != null) return false;
        return company != null ? company.equals(client.company) : client.company == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (clientNumber != null ? clientNumber.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surName != null ? surName.hashCode() : 0);
        result = 31 * result + (age != null ? age.hashCode() : 0);
        result = 31 * result + (company != null ? company.hashCode() : 0);
        return result;
    }
}
