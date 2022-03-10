package com.techelevator.tenmo.service;

import com.techelevator.tenmo.dao.TransferDao;
import org.springframework.stereotype.Component;

@Component
public class TransferService {
    private TransferDao transferDao;

    public TransferService(TransferDao transferDao) {
        this.transferDao = transferDao;
    }

}
