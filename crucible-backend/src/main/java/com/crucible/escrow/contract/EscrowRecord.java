package com.crucible.escrow.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/LFDT-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.8.0.
 */
@SuppressWarnings("rawtypes")
@Generated("org.web3j.codegen.SolidityFunctionWrapperGenerator")
public class EscrowRecord extends Contract {
    public static final String BINARY = "608060405234801561000f575f80fd5b5060405161051838038061051883398101604081905261002e91610068565b5f805460019390935560029190915560ff196001600160a01b039390931661010002929092166001600160a81b03199091161790556100a7565b5f805f6060848603121561007a575f80fd5b83516001600160a01b0381168114610090575f80fd5b602085015160409095015190969495509392505050565b610464806100b45f395ff3fe608060405234801561000f575f80fd5b5060043610610085575f3560e01c806386d1a69f1161005857806386d1a69f146100fb578063aa8c217c14610103578063b60d42881461010c578063f240f7c314610114575f80fd5b80630c3f6acf14610089578063590e1ae3146100ab5780635d62d5df146100b557806365d65e86146100cc575b5f80fd5b5f546100959060ff1681565b6040516100a29190610391565b60405180910390f35b6100b361011c565b005b6100be60015481565b6040519081526020016100a2565b5f546100e39061010090046001600160a01b031681565b6040516001600160a01b0390911681526020016100a2565b6100b36101fd565b6100be60025481565b6100b3610273565b6100b36102e7565b5f5461010090046001600160a01b031633146101535760405162461bcd60e51b815260040161014a906103a5565b60405180910390fd5b60015f5460ff16600481111561016b5761016b61035d565b148061018b57505f805460ff1660048111156101895761018961035d565b145b6101a75760405162461bcd60e51b815260040161014a906103dc565b5f80546003919060ff19166001835b02179055505f546040517f0b6b340830d52b5dd44a416ee7a14618083b381ba65f16a9c90930abba8de85a916101f39160ff909116904290610413565b60405180910390a1565b5f5461010090046001600160a01b0316331461022b5760405162461bcd60e51b815260040161014a906103a5565b60015f5460ff1660048111156102435761024361035d565b146102605760405162461bcd60e51b815260040161014a906103dc565b5f80546002919060ff19166001836101b6565b5f5461010090046001600160a01b031633146102a15760405162461bcd60e51b815260040161014a906103a5565b5f805460ff1660048111156102b8576102b861035d565b146102d55760405162461bcd60e51b815260040161014a906103dc565b5f80546001919060ff191682806101b6565b5f5461010090046001600160a01b031633146103155760405162461bcd60e51b815260040161014a906103a5565b60015f5460ff16600481111561032d5761032d61035d565b1461034a5760405162461bcd60e51b815260040161014a906103dc565b5f80546004919060ff19166001836101b6565b634e487b7160e01b5f52602160045260245ffd5b6005811061038d57634e487b7160e01b5f52602160045260245ffd5b9052565b6020810161039f8284610371565b92915050565b6020808252601d908201527f4f6e6c79206261636b656e642063616e20757064617465207374617465000000604082015260600190565b60208082526018908201527f496e76616c6964207374617465207472616e736974696f6e0000000000000000604082015260600190565b604081016104218285610371565b826020830152939250505056fea2646970667358221220a1b3d6152854497ef012ad0f04ad0587a2b6030fcb48f34d00563890e325529c64736f6c63430008180033\r\n";

    private static String librariesLinkedBinary;

    public static final String FUNC_AMOUNT = "amount";

    public static final String FUNC_BACKENDSIGNER = "backendSigner";

    public static final String FUNC_CURRENTSTATE = "currentState";

    public static final String FUNC_DISPUTE = "dispute";

    public static final String FUNC_FUND = "fund";

    public static final String FUNC_GIGID = "gigId";

    public static final String FUNC_REFUND = "refund";

    public static final String FUNC_RELEASE = "release";

    public static final Event STATECHANGED_EVENT = new Event("StateChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected EscrowRecord(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected EscrowRecord(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected EscrowRecord(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected EscrowRecord(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<StateChangedEventResponse> getStateChangedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(STATECHANGED_EVENT, transactionReceipt);
        ArrayList<StateChangedEventResponse> responses = new ArrayList<StateChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            StateChangedEventResponse typedResponse = new StateChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newState = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static StateChangedEventResponse getStateChangedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(STATECHANGED_EVENT, log);
        StateChangedEventResponse typedResponse = new StateChangedEventResponse();
        typedResponse.log = log;
        typedResponse.newState = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
        return typedResponse;
    }

    public Flowable<StateChangedEventResponse> stateChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getStateChangedEventFromLog(log));
    }

    public Flowable<StateChangedEventResponse> stateChangedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(STATECHANGED_EVENT));
        return stateChangedEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> amount() {
        final Function function = new Function(FUNC_AMOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<String> backendSigner() {
        final Function function = new Function(FUNC_BACKENDSIGNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> currentState() {
        final Function function = new Function(FUNC_CURRENTSTATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> dispute() {
        final Function function = new Function(
                FUNC_DISPUTE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> fund() {
        final Function function = new Function(
                FUNC_FUND, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> gigId() {
        final Function function = new Function(FUNC_GIGID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> refund() {
        final Function function = new Function(
                FUNC_REFUND, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> release() {
        final Function function = new Function(
                FUNC_RELEASE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static EscrowRecord load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new EscrowRecord(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static EscrowRecord load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new EscrowRecord(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static EscrowRecord load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new EscrowRecord(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static EscrowRecord load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new EscrowRecord(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<EscrowRecord> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider, String _backendSigner, BigInteger _gigId,
            BigInteger _amount) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _backendSigner), 
                new org.web3j.abi.datatypes.generated.Uint256(_gigId), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)));
        return deployRemoteCall(EscrowRecord.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    public static RemoteCall<EscrowRecord> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider,
            String _backendSigner, BigInteger _gigId, BigInteger _amount) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _backendSigner), 
                new org.web3j.abi.datatypes.generated.Uint256(_gigId), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)));
        return deployRemoteCall(EscrowRecord.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<EscrowRecord> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit, String _backendSigner, BigInteger _gigId,
            BigInteger _amount) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _backendSigner), 
                new org.web3j.abi.datatypes.generated.Uint256(_gigId), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)));
        return deployRemoteCall(EscrowRecord.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<EscrowRecord> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit,
            String _backendSigner, BigInteger _gigId, BigInteger _amount) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _backendSigner), 
                new org.web3j.abi.datatypes.generated.Uint256(_gigId), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)));
        return deployRemoteCall(EscrowRecord.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), encodedConstructor);
    }

    public static void linkLibraries(List<Contract.LinkReference> references) {
        librariesLinkedBinary = linkBinaryWithReferences(BINARY, references);
    }

    private static String getDeploymentBinary() {
        if (librariesLinkedBinary != null) {
            return librariesLinkedBinary;
        } else {
            return BINARY;
        }
    }

    public static class StateChangedEventResponse extends BaseEventResponse {
        public BigInteger newState;

        public BigInteger timestamp;
    }
}
