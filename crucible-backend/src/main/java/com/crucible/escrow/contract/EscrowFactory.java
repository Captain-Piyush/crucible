package com.crucible.escrow.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
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
public class EscrowFactory extends Contract {
    public static final String BINARY = "608060405234801561000f575f80fd5b505f80546001600160a01b031916331790556107ca8061002e5f395ff3fe608060405234801561000f575f80fd5b506004361061003f575f3560e01c806365d65e8614610043578063c0a21d1f14610071578063e858bcd414610086575b5f80fd5b5f54610055906001600160a01b031681565b6040516001600160a01b03909116815260200160405180910390f35b61008461007f366004610245565b6100ae565b005b610055610094366004610265565b60016020525f90815260409020546001600160a01b031681565b5f546001600160a01b0316331461010c5760405162461bcd60e51b815260206004820152601f60248201527f4f6e6c79206261636b656e642063616e2063726561746520657363726f77730060448201526064015b60405180910390fd5b5f828152600160205260409020546001600160a01b03161561017b5760405162461bcd60e51b815260206004820152602260248201527f457363726f7720616c72656164792065786973747320666f7220746869732067604482015261696760f01b6064820152608401610103565b5f80546040516001600160a01b03909116908490849061019a90610238565b6001600160a01b03909316835260208301919091526040820152606001604051809103905ff0801580156101d0573d5f803e3d5ffd5b505f8481526001602090815260409182902080546001600160a01b0319166001600160a01b038516908117909155915191825291925084917f10cef5ec72e57e7983d937eb064757a668131fb3c724a8a7b1ee3fbd334fa3d6910160405180910390a2505050565b6105188061027d83390190565b5f8060408385031215610256575f80fd5b50508035926020909101359150565b5f60208284031215610275575f80fd5b503591905056fe608060405234801561000f575f80fd5b5060405161051838038061051883398101604081905261002e91610068565b5f805460019390935560029190915560ff196001600160a01b039390931661010002929092166001600160a81b03199091161790556100a7565b5f805f6060848603121561007a575f80fd5b83516001600160a01b0381168114610090575f80fd5b602085015160409095015190969495509392505050565b610464806100b45f395ff3fe608060405234801561000f575f80fd5b5060043610610085575f3560e01c806386d1a69f1161005857806386d1a69f146100fb578063aa8c217c14610103578063b60d42881461010c578063f240f7c314610114575f80fd5b80630c3f6acf14610089578063590e1ae3146100ab5780635d62d5df146100b557806365d65e86146100cc575b5f80fd5b5f546100959060ff1681565b6040516100a29190610391565b60405180910390f35b6100b361011c565b005b6100be60015481565b6040519081526020016100a2565b5f546100e39061010090046001600160a01b031681565b6040516001600160a01b0390911681526020016100a2565b6100b36101fd565b6100be60025481565b6100b3610273565b6100b36102e7565b5f5461010090046001600160a01b031633146101535760405162461bcd60e51b815260040161014a906103a5565b60405180910390fd5b60015f5460ff16600481111561016b5761016b61035d565b148061018b57505f805460ff1660048111156101895761018961035d565b145b6101a75760405162461bcd60e51b815260040161014a906103dc565b5f80546003919060ff19166001835b02179055505f546040517f0b6b340830d52b5dd44a416ee7a14618083b381ba65f16a9c90930abba8de85a916101f39160ff909116904290610413565b60405180910390a1565b5f5461010090046001600160a01b0316331461022b5760405162461bcd60e51b815260040161014a906103a5565b60015f5460ff1660048111156102435761024361035d565b146102605760405162461bcd60e51b815260040161014a906103dc565b5f80546002919060ff19166001836101b6565b5f5461010090046001600160a01b031633146102a15760405162461bcd60e51b815260040161014a906103a5565b5f805460ff1660048111156102b8576102b861035d565b146102d55760405162461bcd60e51b815260040161014a906103dc565b5f80546001919060ff191682806101b6565b5f5461010090046001600160a01b031633146103155760405162461bcd60e51b815260040161014a906103a5565b60015f5460ff16600481111561032d5761032d61035d565b1461034a5760405162461bcd60e51b815260040161014a906103dc565b5f80546004919060ff19166001836101b6565b634e487b7160e01b5f52602160045260245ffd5b6005811061038d57634e487b7160e01b5f52602160045260245ffd5b9052565b6020810161039f8284610371565b92915050565b6020808252601d908201527f4f6e6c79206261636b656e642063616e20757064617465207374617465000000604082015260600190565b60208082526018908201527f496e76616c6964207374617465207472616e736974696f6e0000000000000000604082015260600190565b604081016104218285610371565b826020830152939250505056fea2646970667358221220a1b3d6152854497ef012ad0f04ad0587a2b6030fcb48f34d00563890e325529c64736f6c63430008180033a2646970667358221220fbbcfbafea2f8b8fc606131de2989ae7d4bd201628f3d873095c7b8261cc284664736f6c63430008180033\r\n";

    private static String librariesLinkedBinary;

    public static final String FUNC_BACKENDSIGNER = "backendSigner";

    public static final String FUNC_CREATEESCROW = "createEscrow";

    public static final String FUNC_GIGESCROWS = "gigEscrows";

    public static final Event ESCROWCREATED_EVENT = new Event("EscrowCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected EscrowFactory(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected EscrowFactory(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected EscrowFactory(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected EscrowFactory(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static List<EscrowCreatedEventResponse> getEscrowCreatedEvents(
            TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = staticExtractEventParametersWithLog(ESCROWCREATED_EVENT, transactionReceipt);
        ArrayList<EscrowCreatedEventResponse> responses = new ArrayList<EscrowCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            EscrowCreatedEventResponse typedResponse = new EscrowCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.gigId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.escrowAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public static EscrowCreatedEventResponse getEscrowCreatedEventFromLog(Log log) {
        Contract.EventValuesWithLog eventValues = staticExtractEventParametersWithLog(ESCROWCREATED_EVENT, log);
        EscrowCreatedEventResponse typedResponse = new EscrowCreatedEventResponse();
        typedResponse.log = log;
        typedResponse.gigId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
        typedResponse.escrowAddress = (String) eventValues.getNonIndexedValues().get(0).getValue();
        return typedResponse;
    }

    public Flowable<EscrowCreatedEventResponse> escrowCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(log -> getEscrowCreatedEventFromLog(log));
    }

    public Flowable<EscrowCreatedEventResponse> escrowCreatedEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ESCROWCREATED_EVENT));
        return escrowCreatedEventFlowable(filter);
    }

    public RemoteFunctionCall<String> backendSigner() {
        final Function function = new Function(FUNC_BACKENDSIGNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> createEscrow(BigInteger gigId,
            BigInteger amount) {
        final Function function = new Function(
                FUNC_CREATEESCROW, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(gigId), 
                new org.web3j.abi.datatypes.generated.Uint256(amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> gigEscrows(BigInteger param0) {
        final Function function = new Function(FUNC_GIGESCROWS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static EscrowFactory load(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return new EscrowFactory(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static EscrowFactory load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new EscrowFactory(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static EscrowFactory load(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return new EscrowFactory(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static EscrowFactory load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new EscrowFactory(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<EscrowFactory> deploy(Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        return deployRemoteCall(EscrowFactory.class, web3j, credentials, contractGasProvider, getDeploymentBinary(), "");
    }

    public static RemoteCall<EscrowFactory> deploy(Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(EscrowFactory.class, web3j, transactionManager, contractGasProvider, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<EscrowFactory> deploy(Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(EscrowFactory.class, web3j, credentials, gasPrice, gasLimit, getDeploymentBinary(), "");
    }

    @Deprecated
    public static RemoteCall<EscrowFactory> deploy(Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(EscrowFactory.class, web3j, transactionManager, gasPrice, gasLimit, getDeploymentBinary(), "");
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

    public static class EscrowCreatedEventResponse extends BaseEventResponse {
        public BigInteger gigId;

        public String escrowAddress;
    }
}
