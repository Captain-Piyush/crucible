package com.crucible.escrow.service;

import com.crucible.crucible_backend.service.StipendCalculationService;
import com.crucible.escrow.contract.EscrowFactory;
import com.crucible.escrow.contract.EscrowRecord;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
public class EscrowService {

    private final Web3j web3j;
    private final Credentials credentials;
    private final DefaultGasProvider gasProvider = new DefaultGasProvider();

    private String factoryAddress;

    public EscrowService(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public String deployEscrowFactory() {
        try {
            System.out.println("Deploying EscrowFactory to the blockchain...");
            EscrowFactory factory = EscrowFactory.deploy(web3j, credentials, gasProvider).send();
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

            TransactionReceipt receipt = factory.createEscrow(contractGigId, fiatAmountCents).send();
            return factory.gigEscrows(contractGigId).send();
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

    public String releaseEscrow(String escrowRecordAddress, StipendCalculationService.FinancialLedger ledger) {
        try {
            EscrowRecord record = EscrowRecord.load(escrowRecordAddress, web3j, credentials, gasProvider);

            // Using your built-in Web3j Bridge Methods!
            BigInteger winnerCents = ledger.getWinnerPayoutInCents();
            BigInteger platformRevenueCents = ledger.getPlatformRevenueInCents();
            BigInteger stipendPoolCents = ledger.totalStipendPool().multiply(new BigDecimal("100")).toBigInteger();

            // Line 76 WILL STAY RED until we update the Solidity contract wrapper
            TransactionReceipt receipt = record.release(winnerCents, platformRevenueCents, stipendPoolCents).send();
            return receipt.getTransactionHash();
        } catch (Exception e) {
            throw new RuntimeException("Failed to release EscrowRecord with stipend ledger", e);
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