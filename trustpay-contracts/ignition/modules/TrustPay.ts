import { buildModule } from "@nomicfoundation/hardhat-ignition/modules";

const TrustPayModule = buildModule("TrustPayModule", (m) => {
  // We deploy the Factory, which will generate individual EscrowRecords later
  const escrowFactory = m.contract("EscrowFactory");

  return { escrowFactory };
});

export default TrustPayModule;