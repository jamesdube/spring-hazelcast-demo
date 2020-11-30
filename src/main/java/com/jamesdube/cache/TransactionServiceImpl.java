package com.jamesdube.cache;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;

    private final HazelcastInstance hazelcastInstance;

    private final CacheManager cacheManager;

    public TransactionServiceImpl(TransactionRepository transactionRepository, HazelcastInstance hazelcastInstance, CacheManager cacheManager) {
        this.transactionRepository = transactionRepository;
        this.hazelcastInstance = hazelcastInstance;
        this.cacheManager = cacheManager;
    }

    @Override
    @CachePut(value = "transactions")
    public void save(Transaction transaction) {
        logger.info("saving transaction: {}",transaction.getId());

        saveToHazelcast(transaction);
        transactionRepository.save(transaction);
    }

    @Override
    public void save(List<Transaction> transactionList) {
        for (Transaction transaction: transactionList){
            save(transaction);
        }
    }

    @CachePut(value = "transactions", key = "#transactions.id")
    public Transaction update(Transaction transaction) {
        if (transactionRepository.existsById(transaction.getId())) {
            return transactionRepository.save(transaction);
        }
        throw new IllegalArgumentException("A transaction must have an id to be updated");
    }

    @Cacheable(value = "transactions")
    public Transaction get(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("transaction with id " + id + " was not found"));
    }

    @Override
    @Cacheable(value = "transactions")
    public Transaction findById(Long id) {

        logger.info("retrieving transaction with id: {}",id);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("transaction with id " + id + " was not found"));
    }

    @Override
    public void deleteAll() {
        transactionRepository.deleteAll();
    }

    private void saveToHazelcast(Transaction transaction) {

        Cache cache = cacheManager.getCache("transactions");
        if (cache != null) {
            cache.put(transaction.getId(),transaction);
        }
    }
}
