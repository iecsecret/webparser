package com.volantx.webparser.service;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class SnapshotService {


    @Value("${selenium.webdriver.chrome}")
    private String chromeWebDriver;

    @Value("${snap.save.path}")
    private String savePath;

    @Value("${snap.save.url}")
    private String saveUrl;

    public WebDriver driver;
    private File savePathDir;


    @PostConstruct
    void init()  {

        System.setProperty("webdriver.chrome.driver",chromeWebDriver);
        savePathDir  = new File(savePath);
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
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        //Write Screenshot to a file
        FileUtils.copyFile(scrFile, File.createTempFile("snap",".png", savePathDir));
    }

}
