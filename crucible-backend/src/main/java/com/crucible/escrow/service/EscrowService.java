package com.crucible.escrow.service;

import com.crucible.escrow.contract.EscrowFactory;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;

@Service
public class EscrowService {

    private final Web3j web3j;
    private final Credentials credentials;

    public EscrowService(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public String deployEscrowFactory() {
        try {
            System.out.println("Deploying EscrowFactory to the blockchain...");

            EscrowFactory factory = EscrowFactory.deploy(
                    web3j,
                    credentials,
                    new DefaultGasProvider()
            ).send();

            String contractAddress = factory.getContractAddress();
            System.out.println("Successfully deployed at address: " + contractAddress);

            return contractAddress;
        } catch (Exception e) {
            throw new RuntimeException("Failed to deploy EscrowFactory contract", e);
        }
    }
}