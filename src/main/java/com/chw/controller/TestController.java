package com.chw.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/test")
public class TestController {

    private int getCount = 0;
    private int postCount = 0;

    @GetMapping("/getData")
    public Map getData() {
        log.info("GET DATA request....");
        Map map = new HashMap();
        map.put("code", 0);
        map.put("msg", "success");
        map.put("data", "http:getData count:" + getCount);
        getCount += 1;
        return map;
    }

    @PostMapping("/postData")
    public Map postData(@RequestBody Map<String, Object> request) {
        log.info("PSOT DATA request....");
        Map<String, String> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : request.entrySet()) {
            result.put(entry.getKey(), entry.getValue() + "-result count:" + postCount);
        }
        postCount += 1;
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 0);
        resultMap.put("msg", "success");
        resultMap.put("data", result);
        return resultMap;
    }
}
