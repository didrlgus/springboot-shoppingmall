package com.shoppingmall.util;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class StepShareMap <T> {

    private Map<String, T> stepShareMap;

    public StepShareMap() {
        this.stepShareMap = Maps.newConcurrentMap();
    }

    public void put(String key, T data) {
        if (stepShareMap ==  null) {
            log.error("Map is not initialize");
            return;
        }
        stepShareMap.put(key, data);
    }

    public T get (String key) {
        if (stepShareMap == null) {
            return null;
        }
        return stepShareMap.get(key);
    }
}
