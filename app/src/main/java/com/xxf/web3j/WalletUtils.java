package com.xxf.web3j;

import android.support.annotation.WorkerThread;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.ObjectMapperFactory;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class WalletUtils {
    /**
     * 创建钱包
     *
     * @param password
     * @param file
     * @return
     * @throws InvalidAlgorithmParameterException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchProviderException
     * @throws CipherException
     * @throws IOException
     */
    @WorkerThread
    public static WalletResult _createWallet(final String password, File file)
            throws InvalidAlgorithmParameterException,
            NoSuchAlgorithmException,
            NoSuchProviderException,
            CipherException,
            IOException {
        // key pair --> ec key pair
        ECKeyPair ecKeyPair = Keys.createEcKeyPair();
        // wallet
        WalletFile wallet = Wallet.create(password, ecKeyPair, 1024, 1);
        // write local file
        FileUtils.createFile(file);
        ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
        mapper.writeValue(file, wallet);
        // result wrapper
        return new WalletResult(ecKeyPair, wallet, file);
    }

    /**
     * 打开钱包
     *
     * @param password
     * @param file
     * @return
     * @throws IOException
     * @throws CipherException
     */
    @WorkerThread
    public static WalletResult _openWallet(final String password, File file) throws IOException, CipherException {
        ObjectMapper mapper = ObjectMapperFactory.getObjectMapper();
        // read wallet from local file
        WalletFile wallet = mapper.readValue(file, WalletFile.class);
        // get old EC key pair
        ECKeyPair ecKeyPair = Wallet.decrypt(password, wallet);
        return new WalletResult(ecKeyPair, wallet, file);
    }


}
