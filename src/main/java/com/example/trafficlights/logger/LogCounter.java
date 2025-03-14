package com.example.trafficlights.logger;

import java.util.concurrent.atomic.AtomicInteger;

public class LogCounter {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static int getNextNumber() {
        return counter.incrementAndGet();
    }

}
