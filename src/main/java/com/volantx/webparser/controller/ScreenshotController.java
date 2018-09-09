package com.volantx.webparser.controller;


import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
public class ScreenshotController {

    private static final String TOPIC_NAME = "smapshotTopic";

    private KafkaEmbedded kafkaEmbedded;

    @RequestMapping("/api/takeSnapshot")
    @ResponseBody String takeSnapshot(@RequestBody List<String> urls) {
        kafkaEmbedded = new KafkaEmbedded(1, true, TOPIC_NAME);
        return null;
    }

}
