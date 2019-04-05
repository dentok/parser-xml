package com.dentok.parserxml.service;

import com.dentok.parserxml.entity.Client;
import com.dentok.parserxml.repository.ClientRepository;
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
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ParserXmlService.class})
public class ParserXmlServiceTest {

    @Spy
    private ParserXmlService parserXmlService = new ParserXmlService();

    @Mock
    private ClientRepository clientRepository;


    String dataSuccess = "<Client><clientNumber>9979</clientNumber><name>Vitaliy9979</name><surName>Petrovich9979</surName><age>18</age><company>Big Company9979</company></Client>";
    String dataFailure = "<Client<clientNumber>9979</clientNumber><name>Vitaliy9979</name><surName>Petrovich9979</surName><age>18</age><company>Big Company9979</company></Client>";
    String dataEmpty = "";
    String dataClientEmpty = "<Clients></Clients>";
    Client client = new Client(9979, "Vitaliy9979", "Petrovich9979", 18, "Big Company9979");
    List<Client> clients = new ArrayList<>();

    @Before
    public void setUp() {
        parserXmlService.setClientRepository(clientRepository);
        clients.add(client);
    }

    //check is invoke all methods in parserXmlService class
    @Test
    public void parseXMLAndSave_checkInvoke_parseXMLAndSaveAll() throws Exception {
        InputStream stream = new ByteArrayInputStream(dataSuccess.getBytes(StandardCharsets.UTF_8));

        parserXmlService.parseXMLAndSave(stream);

        PowerMockito.verifyPrivate(parserXmlService, times(1)).invoke("parseXML", eq(stream));
        PowerMockito.verifyPrivate(clientRepository, times(1)).invoke("saveAll", eq(clients));
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