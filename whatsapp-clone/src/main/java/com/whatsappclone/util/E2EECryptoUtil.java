package com.whatsappclone.util;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Arrays;

public class E2EECryptoUtil {

    private static final String DH_ALGORITHM = "DH";
    private static final String AES_ALGORITHM = "AES";

    public static KeyPair generateDHKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(DH_ALGORITHM);
        kpg.initialize(2048, new SecureRandom());
        return kpg.generateKeyPair();
    }

    public static byte[] computeSharedSecret(PrivateKey myPrivateKey, PublicKey peerPublicKey) throws Exception {
        KeyAgreement keyAgreement = KeyAgreement.getInstance(DH_ALGORITHM);
        keyAgreement.init(myPrivateKey);
        keyAgreement.doPhase(peerPublicKey, true);
        return keyAgreement.generateSecret();
    }

    public static SecretKeySpec deriveAESKey(byte[] sharedSecret) {
        byte[] keyBytes = Arrays.copyOfRange(sharedSecret, 0, 32);
        return new SecretKeySpec(keyBytes, AES_ALGORITHM);
    }

    public static byte[] encryptAES(byte[] plaintext, SecretKeySpec aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return cipher.doFinal(plaintext);
    }

    public static byte[] decryptAES(byte[] ciphertext, SecretKeySpec aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return cipher.doFinal(ciphertext);
    }
}
