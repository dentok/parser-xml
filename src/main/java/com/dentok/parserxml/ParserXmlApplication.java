package com.dentok.parserxml;

import com.dentok.parserxml.service.ParserXmlService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@EnableJpaRepositories
public class ParserXmlApplication extends SpringBootServletInitializer {

    /**
     * App entry point
     *
     * @param args incoming arguments
     */
    public static void main(String... args) throws XMLStreamException {

        ApplicationContext ctx = SpringApplication.run(
                ParserXmlApplication.class, args);

        // load transactions from file xml
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("test.xml");

        //start parse xml and save to DB
        //check load data from xml, please open link http://localhost:8089/parser/h2-console and select from tables TRANSACTION , client
        ParserXmlService parserXmlService = ctx.getBean(ParserXmlService.class);
        System.out.println("Starting parse and save clients....");
        parserXmlService.parseXMLAndSave(is);
        System.out.println("Save is complete");


    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ParserXmlApplication.class);
    }
}