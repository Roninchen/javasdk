package cn.hyperchain.sdk.service;

import cn.hyperchain.sdk.account.Account;
import cn.hyperchain.sdk.account.Algo;
import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Decoder;
import cn.hyperchain.sdk.common.utils.FileUtil;
import cn.hyperchain.sdk.crypto.SignerUtil;
import cn.hyperchain.sdk.exception.RequestException;
import cn.hyperchain.sdk.provider.ProviderManager;
import cn.hyperchain.sdk.request.Request;
import cn.hyperchain.sdk.response.ReceiptResponse;
import cn.hyperchain.sdk.transaction.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class ContractServiceTest {
    ProviderManager providerManager = Common.soloProviderManager;
    ContractService contractService = ServiceManager.getContractService(providerManager);
    AccountService accountService = ServiceManager.getAccountService(providerManager);

    @Before
    @Ignore
    public void deploy() throws RequestException, IOException {
        // 3. build transaction
        Account account = accountService.genAccount(Algo.SMRAW);
        InputStream payload = FileUtil.readFileAsStream("hvm-jar/hvmbasic-1.0.0-student.jar");
        Transaction transaction = new Transaction.HVMBuilder(account.getAddress()).deploy(payload).build();
        transaction.sign(accountService.fromAccountJson(account.toJson()));
        Assert.assertTrue(account.verify(transaction.getNeedHashString().getBytes(), ByteUtil.fromHex(transaction.getSignature())));
        Assert.assertTrue(SignerUtil.verifySign(transaction.getNeedHashString(), transaction.getSignature(), account.getPublicKey()));
        // 4. get request
        ReceiptResponse receiptResponse = contractService.deploy(transaction).send().polling();
        String contractAddress = receiptResponse.getContractAddress();
        System.out.println("合约地址: " + contractAddress);
        System.out.println("部署返回(未解码): " + receiptResponse.getRet());
        System.out.println("部署返回(解码)：" + Decoder.decodeHVM(receiptResponse.getRet(), String.class));
    }

    @Test
    @Ignore
    public void testReceipt() throws RequestException {
        String txHash = "0x8186076256e9ca935c2cea065777fbdc9bcb095e4edd38826d820d289f97b905";
        Request<ReceiptResponse> receipt = contractService.getReceipt(txHash);
        ReceiptResponse response = receipt.send();
        System.out.println(response);
    }
}
