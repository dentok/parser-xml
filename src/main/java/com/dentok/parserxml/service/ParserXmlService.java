package com.dentok.parserxml.service;

import com.dentok.parserxml.entity.Client;
import com.dentok.parserxml.entity.Transaction;
import com.dentok.parserxml.exception.InputFileException;
import com.dentok.parserxml.repository.ClientRepository;
import com.dentok.parserxml.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ParserXmlService {

    private TransactionRepository transactionsRepository;

    private ClientRepository clientRepository;

    /**
     * method parse file and save in Database h2
     *
     * @param file The stream of file
     * @throws XMLStreamException throws if has errors
     */
    public void parseXMLAndSave(InputStream file) throws XMLStreamException {

        List<Transaction> transactions = parseXML(file);
        if (transactions.isEmpty()) {
            throw new InputFileException("Input file exception, please check file!!!");
        }

        transactionsRepository.saveAll(transactions);
    }

    /**
     * method parse file
     *
     * @param file The stream of file
     * @return List object Transaction
     * @throws XMLStreamException
     */
    private List<Transaction> parseXML(InputStream file) throws XMLStreamException {
        List<Transaction> transactions = new ArrayList<>();
        Client client = null;
        Transaction transaction = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(file);
        while (xmlEventReader.hasNext()) {
            XMLEvent xmlEvent = xmlEventReader.nextEvent();
            if (xmlEvent.isStartElement()) {
                String startElement = xmlEvent.asStartElement().getName().getLocalPart();

                if (startElement.equals("transaction")) {
                    transaction = new Transaction();
                } else if (startElement.equals("place")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    if (transaction != null)
                        transaction.setPlace(xmlEvent.asCharacters().getData());
                } else if (startElement.equals("amount")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    if (transaction != null)
                        transaction.setAmount(Float.valueOf(xmlEvent.asCharacters().getData()));
                } else if (startElement.equals("currency")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    if (transaction != null)
                        transaction.setCurrency(xmlEvent.asCharacters().getData());
                } else if (startElement.equals("card")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    if (transaction != null)
                        transaction.setCard(xmlEvent.asCharacters().getData());
                } else if (startElement.equals("client")) {
                    client = new Client();
                } else if (startElement.equals("firstName")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    if (client != null) client.setFirstName(xmlEvent.asCharacters().getData());
                } else if (startElement.equals("lastName")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    if (client != null) client.setLastName(xmlEvent.asCharacters().getData());
                } else if (startElement.equals("middleName")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    if (client != null) client.setMiddleName(xmlEvent.asCharacters().getData());
                } else if (startElement.equals("inn")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    String inn = xmlEvent.asCharacters().getData();
                    client = getClientProcess(client, inn);
                }
            }
            endProcessParse(transactions, client, transaction, xmlEvent);
        }

        return transactions;
    }

    /**
     * method get current client, if not exist create new Client object
     *
     * @param client The client of transaction
     * @param inn    The INN of client
     * @return Client object
     */
    private Client getClientProcess(Client client, String inn) {
        Client currentClient = clientRepository.findClientByInn(inn).orElse(null);

        if (currentClient != null) {
            client = currentClient;
        } else {
            if (client != null) client.setInn(inn);
        }
        return client;
    }

    /**
     * end process parse file for end element of file
     *
     * @param transactions the list of Transaction object
     * @param client       The client of transaction
     * @param transaction  The transaction
     * @param xmlEvent     The xmlEvent
     */
    private void endProcessParse(List<Transaction> transactions, Client client, Transaction transaction, XMLEvent xmlEvent) {
        if (xmlEvent.isEndElement()) {
            EndElement endElement = xmlEvent.asEndElement();

            if (endElement.getName().getLocalPart().equals("transaction")) {
                if (client != null) {
                    client.addTransactions(transaction);
                    transactions.add(transaction);
                }
            } else if (endElement.getName().getLocalPart().equals("client")) {
                clientRepository.saveAndFlush(client);
            }
        }
    }

    @Autowired
    public void setTransactionsRepository(TransactionRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
}