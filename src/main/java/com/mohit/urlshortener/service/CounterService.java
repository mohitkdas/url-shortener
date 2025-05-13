package com.mohit.urlshortener.service;

import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CounterService {

    @Autowired
    private RedisCommands<String, String> counterCommands;

    private long currentCounter = 0;
    private long maxCounter = 0;

    private static final int RANGE_SIZE = 1000;

    public synchronized long getNextId() {
        // If the current range is exhausted, fetch a new range
        if (currentCounter >= maxCounter) {
            long newCounter = counterCommands.incrby("counter", RANGE_SIZE);
            currentCounter = newCounter - RANGE_SIZE + 1;
            maxCounter = newCounter;
        }
        return currentCounter++;
    }

}
