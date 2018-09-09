package com.volantx.webparser.service;

import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.mobile.NetworkConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SnapshotService {

    public static ArrayList<ChromeDriver> chromeDrivers = new ArrayList<>();

    public static final String TOPIC_NAME = "snapshotTopic";

    private KafkaEmbedded kafkaEmbedded;

    @Value("${kafka.consumer.count:1}")
    private int kafkaConsumerCount;

    @Value("${selenium.webdriver.chrome}")
    private String chromeWebDriver;

    @Value("${snap.save.path}")
    private String savePath;

    Logger logger = LoggerFactory.getLogger(SnapshotService.class);


    @PostConstruct
    void init() {

        System.setProperty("webdriver.chrome.driver", chromeWebDriver);

        for (int i = 1; i <= kafkaConsumerCount; i++) {

            ChromeOptions ops = new ChromeOptions();
            ops.addArguments("--headless","--disabled-gpu");
            ChromeDriverService chromeDriverService = new ChromeDriverService.Builder().withVerbose(true).withWhitelistedIps("").build();
            ChromeDriver chromeDriver = new ChromeDriver(chromeDriverService, ops);
            chromeDrivers.add(chromeDriver);
        }


        kafkaEmbedded = new KafkaEmbedded(1, true, TOPIC_NAME);

        Map<String, Object> props = KafkaTestUtils.producerProps(kafkaEmbedded);

        ExecutorService executor = Executors.newFixedThreadPool(kafkaConsumerCount);

        for (int i = 1; i <= kafkaConsumerCount; i++) {
            executor.submit(new SnapshotConsumer(props, savePath, i));
        }

    }

    public int produceSnapshotTasks(List<String> urls) {
        Map<String, Object> senderProps = KafkaTestUtils.producerProps(kafkaEmbedded);
        KafkaProducer<Integer, String> producer = new KafkaProducer<>(senderProps);

        AtomicInteger accepted = new AtomicInteger();
        urls.stream().forEach(url -> {
            try {
                producer.send(new ProducerRecord<>(TOPIC_NAME, 0, 0, url)).get();
                accepted.incrementAndGet();
            } catch (Exception e) {
                logger.error("Cannot add url:" + url, e);
            }
        });
        return accepted.get();
    }


    public int snapshotSync(List<String> urls) {
        urls.stream().forEach(url->new SnapshotConsumer().takeSnap(url, 0, new File(savePath) ));
        return 0;
    }

}
