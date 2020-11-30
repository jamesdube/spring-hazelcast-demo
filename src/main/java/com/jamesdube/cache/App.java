package com.jamesdube.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class App implements CommandLineRunner {

    private final TransactionService transactionService;

    private final Logger logger = LoggerFactory.getLogger(App.class);

    public App(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public void run(String... args) throws Exception {

        logger.info("loading data...");
        transactionService.deleteAll();

        List<Transaction> transactionList = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            transactionList.add(new Transaction(i,"boj"));
        }

        transactionService.save(transactionList);

        logger.info("...finished loading data");

        transactionService.findById(1L);

    }
}
