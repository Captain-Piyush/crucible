package com.crucible.escrow.service;

import com.crucible.escrow.contract.EscrowFactory;
import com.crucible.escrow.contract.EscrowRecord;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

@Service
public class EscrowService {

    private final Web3j web3j;
    private final Credentials credentials;
    private final DefaultGasProvider gasProvider = new DefaultGasProvider();

    // Cache the deployed factory address
    private String factoryAddress;

    public EscrowService(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public String deployEscrowFactory() {
        try {
            System.out.println("Deploying EscrowFactory to the blockchain...");
            EscrowFactory factory = EscrowFactory.deploy(
                    web3j, credentials, gasProvider
            ).send();

            this.factoryAddress = factory.getContractAddress();
            System.out.println("Successfully deployed at address: " + this.factoryAddress);
            return this.factoryAddress;
        } catch (Exception e) {
            throw new RuntimeException("Failed to deploy EscrowFactory contract", e);
        }
    }

    public String createEscrowRecord(Long gigId, BigInteger fiatAmountCents) {
        try {
            if (this.factoryAddress == null) {
                throw new IllegalStateException("EscrowFactory address is not set. Deploy factory first.");
            }

            EscrowFactory factory = EscrowFactory.load(this.factoryAddress, web3j, credentials, gasProvider);
            BigInteger contractGigId = BigInteger.valueOf(gigId);

            // 1. Submit on-chain transaction
            TransactionReceipt receipt = factory.createEscrow(contractGigId, fiatAmountCents).send();
            System.out.println("Escrow created in Tx: " + receipt.getTransactionHash());

            // 2. Query the mapping getter to find the newly spawned contract address
            String recordAddress = factory.gigEscrows(contractGigId).send();
            System.out.println("EscrowRecord contract spawned at: " + recordAddress);
            return recordAddress;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create EscrowRecord", e);
        }
    }

    public String fundEscrow(String escrowRecordAddress) {
        try {
            EscrowRecord record = EscrowRecord.load(escrowRecordAddress, web3j, credentials, gasProvider);
            TransactionReceipt receipt = record.fund().send();
            return receipt.getTransactionHash();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fund EscrowRecord", e);
        }
    }

    public String releaseEscrow(String escrowRecordAddress) {
        try {
            EscrowRecord record = EscrowRecord.load(escrowRecordAddress, web3j, credentials, gasProvider);
            TransactionReceipt receipt = record.release().send();
            return receipt.getTransactionHash();
        } catch (Exception e) {
            throw new RuntimeException("Failed to release EscrowRecord", e);
        }
    }

    public String refundEscrow(String escrowRecordAddress) {
        try {
            EscrowRecord record = EscrowRecord.load(escrowRecordAddress, web3j, credentials, gasProvider);
            TransactionReceipt receipt = record.refund().send();
            return receipt.getTransactionHash();
        } catch (Exception e) {
            throw new RuntimeException("Failed to refund EscrowRecord", e);
        }
    }

    public String disputeEscrow(String escrowRecordAddress) {
        try {
            EscrowRecord record = EscrowRecord.load(escrowRecordAddress, web3j, credentials, gasProvider);
            TransactionReceipt receipt = record.dispute().send();
            return receipt.getTransactionHash();
        } catch (Exception e) {
            throw new RuntimeException("Failed to dispute EscrowRecord", e);
        }
    }
}