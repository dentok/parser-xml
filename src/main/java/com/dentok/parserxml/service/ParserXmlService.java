package com.dentok.parserxml.service;

import com.dentok.parserxml.entity.Client;
import com.dentok.parserxml.exception.InputFileException;
import com.dentok.parserxml.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class ParserXmlService {

    private ClientRepository clientRepository;

    public void parseXMLAndSave(InputStream file) throws XMLStreamException {

        List<Client> clients = parseXML(file);
        if (clients.isEmpty()) {
            throw new InputFileException("Input file exception, please check file!!!");
        }

        clientRepository.saveAll(clients);
    }

    private List<Client> parseXML(InputStream file) throws XMLStreamException {
        List<Client> clients = new ArrayList<>();
        Client client = null;
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

        XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(file);
        while (xmlEventReader.hasNext()) {
            XMLEvent xmlEvent = xmlEventReader.nextEvent();
            if (xmlEvent.isStartElement()) {
                StartElement startElement = xmlEvent.asStartElement();
                if (startElement.getName().getLocalPart().equals("Client")) {
                    client = new Client();
                } else if (startElement.getName().getLocalPart().equals("clientNumber")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    client.setClientNumber(Integer.parseInt(xmlEvent.asCharacters().getData()));
                } else if (startElement.getName().getLocalPart().equals("name")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    client.setName(xmlEvent.asCharacters().getData());
                } else if (startElement.getName().getLocalPart().equals("surName")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    client.setSurName(xmlEvent.asCharacters().getData());
                } else if (startElement.getName().getLocalPart().equals("age")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    client.setAge(Integer.parseInt(xmlEvent.asCharacters().getData()));
                } else if (startElement.getName().getLocalPart().equals("company")) {
                    xmlEvent = xmlEventReader.nextEvent();
                    client.setCompany(xmlEvent.asCharacters().getData());
                }
            }
            if (xmlEvent.isEndElement()) {
                EndElement endElement = xmlEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals("Client")) {
                    clients.add(client);
                }
            }
        }

        return clients;
    }

    @Autowired
    public void setClientRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
}
