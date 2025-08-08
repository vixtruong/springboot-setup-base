package com.example.springbootservice.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Log {
    @Scheduled(fixedRate = 5000)
    public void taskOne() {
        System.out.println("Task One - " + Thread.currentThread().getName());
    }

    @Scheduled(fixedRate = 5000)
    public void taskTwo() {
        System.out.println("Task Two - " + Thread.currentThread().getName());
    }

    @Scheduled(fixedRate = 5000)
    public void taskThree() {
        System.out.println("Task Three - " + Thread.currentThread().getName());
    }
}
