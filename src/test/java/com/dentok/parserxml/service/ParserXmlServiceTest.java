package com.dentok.parserxml.service;

import com.dentok.parserxml.entity.Client;
import com.dentok.parserxml.entity.Transaction;
import com.dentok.parserxml.repository.ClientRepository;
import com.dentok.parserxml.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ParserXmlService.class})
public class ParserXmlServiceTest {

    @Spy
    private ParserXmlService parserXmlService = new ParserXmlService();

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TransactionRepository transactionRepository;


    String dataSuccess = "<?xml version='1.0' encoding='UTF-8'?>\n" +
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "\t<soap:Body>\n" +
            "\t\t<ns2:GetTransactionsResponse xmlns:ns2=\"http://dbo.qulix.com/ukrsibdbo\">\n" +
            "\t\t  <transactions>\n" +
            "\t\t    <transaction>\n" +
            "\t\t      <place>A PLACE 1</place>\n" +
            "\t\t      <amount>10.01</amount>\n" +
            "\t\t      <currency>UAH</currency>\n" +
            "\t\t      <card>123456****1234</card>\n" +
            "\t\t      <client>\n" +
            "\t\t\t<firstName>Ivan</firstName>\n" +
            "\t\t\t<lastName>Ivanoff</lastName>\n" +
            "\t\t\t<middleName>Ivanoff</middleName>\n" +
            "\t\t\t<inn>1234567890</inn>\n" +
            "\t\t      </client>\n" +
            "\t\t    </transaction>\n"+
            "\t\t  </transactions>\n" +
            "\t\t</ns2:GetTransactionsResponse>\n" +
            "\t</soap:Body>\n" +
            "</soap:Envelope>";
    String dataFailure = "<Client<clientNumber>9979</clientNumber><name>Vitaliy9979</name><surName>Petrovich9979</surName><age>18</age><company>Big Company9979</company></Client>";
    String dataEmpty = "";
    String dataClientEmpty = "<Clients></Clients>";

    List<Transaction> transactions = new ArrayList<>();

    List<Client> clients = new ArrayList<>();
    Client client = new Client();


    @Before
    public void setUp() {

        client.setId(UUID.randomUUID());
        client.setFirstName("name");
        client.setInn("223233323");
        client.setLastName("sdsdsdd");
        client.setMiddleName("Midle name");

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setAmount((float)10.01);
        transaction.setCard("123456****1234");
        transaction.setCurrency("UAH");
        transaction.setPlace("A PLACE 1");
        transaction.setClient(client);
        transactions.add(transaction);


        when(clientRepository.findClientByInn(anyString())).thenReturn(Optional.of(client));
        parserXmlService.setClientRepository(clientRepository);

        parserXmlService.setTransactionsRepository(transactionRepository);
        clients.add(client);
    }

    //check is invoke all methods in parserXmlService class
    @Test
    public void parseXMLAndSave_checkInvoke_parseXMLAndSaveAll() throws Exception {
        InputStream stream = new ByteArrayInputStream(dataSuccess.getBytes(StandardCharsets.UTF_8));
        when(clientRepository.findClientByInn(anyString())).thenReturn(Optional.of(client));

        parserXmlService.parseXMLAndSave(stream);

        PowerMockito.verifyPrivate(parserXmlService, times(1)).invoke("parseXML", eq(stream));
        PowerMockito.verifyPrivate(clientRepository, times(1)).invoke("saveAndFlush", client);
        PowerMockito.verifyPrivate(transactionRepository, times(1)).invoke("saveAll", anyList());
    }

    //check is throw XMLStreamException
    @Test
    public void parseXMLAndSave_throwException_dataFailerTag() throws Exception {
        InputStream stream = new ByteArrayInputStream(dataFailure.getBytes(StandardCharsets.UTF_8));
        XMLStreamException testEx = new XMLStreamException("Message: Element type \"Client\" must be followed by either attribute specifications, \">\" or \"/>\".");

        try {
            parserXmlService.parseXMLAndSave(stream);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString(testEx.getMessage()));
        }

    }

    //check is throw XMLStreamException
    @Test
    public void parseXMLAndSave_throwException_dataEmpty() throws Exception {
        InputStream stream = new ByteArrayInputStream(dataEmpty.getBytes(StandardCharsets.UTF_8));
        XMLStreamException testEx = new XMLStreamException("Message: Premature end of file.");

        try {
            parserXmlService.parseXMLAndSave(stream);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString(testEx.getMessage()));
        }

    }

    //check is throw XMLStreamException
    @Test
    public void parseXMLAndSave_throwException_dataClientEmpty() throws Exception {
        InputStream stream = new ByteArrayInputStream(dataClientEmpty.getBytes(StandardCharsets.UTF_8));
        XMLStreamException testEx = new XMLStreamException("Input file exception, please check file!!!");

        try {
            parserXmlService.parseXMLAndSave(stream);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString(testEx.getMessage()));
        }

    }
}