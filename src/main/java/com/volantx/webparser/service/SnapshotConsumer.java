package com.volantx.webparser.service;

import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class SnapshotConsumer implements Runnable {

    private KafkaConsumer<String, String> kafkaConsumer;

    private File savePathDir;

    Logger logger = LoggerFactory.getLogger(SnapshotConsumer.class);

    private int index;

    public SnapshotConsumer() {

    }

    public SnapshotConsumer(Map<String, Object> props, String savePath, int index) {

        this.index = index;

        savePathDir = new File(savePath);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        kafkaConsumer = new KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Arrays.asList(new String[]{SnapshotService.TOPIC_NAME}));

    }

    @Override
    public void run() {
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Integer.MAX_VALUE);
        consumerRecords.records(SnapshotService.TOPIC_NAME).forEach(record -> {
            String url = record.value();
            takeSnap(url, index, savePathDir);
        });
    }

    public void takeSnap(String url, int idx, File savePathDir) {
        WebDriver chromeDriver = SnapshotService.chromeDrivers.get(idx);
        chromeDriver.navigate().to(url);
        chromeDriver.manage().window().maximize();
        File scrFile = ((TakesScreenshot) chromeDriver).getScreenshotAs(OutputType.FILE);

        try {
            FileUtils.copyFile(scrFile, File.createTempFile("snap", ".png", savePathDir));
        } catch (IOException e) {
            logger.error("Createsnapshot failed for url:" + url);
        }
    }
}
