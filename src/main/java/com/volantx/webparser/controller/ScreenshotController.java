package com.volantx.webparser.controller;


import com.volantx.webparser.service.SnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ScreenshotController {

    @Autowired
    private SnapshotService snapshotService;

    @RequestMapping(value = "/api/takeSnapshot", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody int takeSnapshot(@RequestBody List<String> urls) {
        return snapshotService.produceSnapshotTasks(urls);
    }

    @RequestMapping(value = "/api/takeSnapshotSync", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody int takeSnapshotSync(@RequestBody List<String> urls) {
        return snapshotService.snapshotSync(urls);
    }

}
