import { defineConfig } from "hardhat/config";
import hardhatToolboxMochaEthers from "@nomicfoundation/hardhat-toolbox-mocha-ethers";
import * as dotenv from "dotenv";

// Loads the variables from your .env file into process.env
dotenv.config();

export default defineConfig({
  plugins: [hardhatToolboxMochaEthers],
  solidity: "0.8.24",
  networks: {
    amoy: {
      type: "http",
      url: "https://rpc-amoy.polygon.technology",
      chainId: 80002,
      // Injects your private key securely at runtime for deployments
      accounts: process.env.PRIVATE_KEY ? [process.env.PRIVATE_KEY] : [],
    }
  }
});