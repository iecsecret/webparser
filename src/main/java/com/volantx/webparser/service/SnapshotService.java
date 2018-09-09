package com.volantx.webparser.service;

import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SnapshotService {

    private static final String TOPIC_NAME = "snapshotTopic";

    private KafkaEmbedded kafkaEmbedded;

    @Value("${selenium.webdriver.chrome}")
    private String chromeWebDriver;

    @Value("${snap.save.path}")
    private String savePath;

    @Value("${snap.save.url}")
    private String saveUrl;

    public WebDriver driver;
    private File savePathDir;

    Logger logger = LoggerFactory.getLogger(SnapshotService.class);

    @PostConstruct
    void init() {

        kafkaEmbedded = new KafkaEmbedded(1, true, TOPIC_NAME);

        System.setProperty("webdriver.chrome.driver", chromeWebDriver);

        savePathDir = new File(savePath);
        setupTest();
        try {
            screenshotGetScreenShotAs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Setup Driver
    public void setupTest() {
        ChromeOptions ops = new ChromeOptions();
        ops.addArguments("--whitelisted-ips=''");
        ops.addArguments("--headless");
        driver = new ChromeDriver(ops);
        //Navigate to URL
        driver.navigate().to(saveUrl);
        driver.manage().window().maximize();
    }

    public void screenshotGetScreenShotAs() throws IOException {
        //Take Screenshot of viewable area
        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        //Write Screenshot to a file
        FileUtils.copyFile(scrFile, File.createTempFile("snap", ".png", savePathDir));
    }

    public int produceSnapshotTasks(List<String> urls) {
        Map<String, Object> senderProps = KafkaTestUtils.producerProps(kafkaEmbedded);
        KafkaProducer<Integer, String> producer = new KafkaProducer<>(senderProps);

        AtomicInteger accepted=new AtomicInteger();
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

}
