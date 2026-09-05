// SPDX-License-Identifier: MIT
pragma solidity ^0.8.24;

/**
 * @title EscrowRecord
 * @dev Represents a single gig's escrow state.
 * This is an AUDIT TRAIL contract, not a fiat custodian.
 */
contract EscrowRecord {
    enum State { Created, Funded, Released, Refunded, Disputed }

    State public currentState;
    address public backendSigner;
    uint256 public gigId;
    uint256 public amount; // Represents fiat value for audit purposes

    event StateChanged(State newState, uint256 timestamp);

    modifier onlyBackend() {
        require(msg.sender == backendSigner, "Only backend can update state");
        _;
    }

    constructor(address _backendSigner, uint256 _gigId, uint256 _amount) {
        backendSigner = _backendSigner;
        gigId = _gigId;
        amount = _amount;
        currentState = State.Created;
    }

    function fund() external onlyBackend {
        require(currentState == State.Created, "Invalid state transition");
        currentState = State.Funded;
        emit StateChanged(currentState, block.timestamp);
    }

    function release() external onlyBackend {
        require(currentState == State.Funded, "Invalid state transition");
        currentState = State.Released;
        emit StateChanged(currentState, block.timestamp);
    }

    function refund() external onlyBackend {
        require(currentState == State.Funded || currentState == State.Created, "Invalid state transition");
        currentState = State.Refunded;
        emit StateChanged(currentState, block.timestamp);
    }

    function dispute() external onlyBackend {
        require(currentState == State.Funded, "Invalid state transition");
        currentState = State.Disputed;
        emit StateChanged(currentState, block.timestamp);
    }
}

/**
 * @title EscrowFactory
 * @dev Deploys individual EscrowRecords to isolate per-gig lifecycle and state.
 */
contract EscrowFactory {
    address public backendSigner;
    mapping(uint256 => address) public gigEscrows;

    event EscrowCreated(uint256 indexed gigId, address escrowAddress);

    constructor() {
        // The wallet that deploys this contract (your Spring Boot backend wallet)
        // becomes the authorized signer.
        backendSigner = msg.sender;
    }

    function createEscrow(uint256 gigId, uint256 amount) external {
        require(msg.sender == backendSigner, "Only backend can create escrows");
        require(gigEscrows[gigId] == address(0), "Escrow already exists for this gig");

        // Deploy a new independent EscrowRecord contract
        EscrowRecord newEscrow = new EscrowRecord(backendSigner, gigId, amount);
        gigEscrows[gigId] = address(newEscrow);

        emit EscrowCreated(gigId, address(newEscrow));
    }
}