package com.whatsappclone.util;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Arrays;

public class E2EECryptoUtil {

    private static final String DH_ALGORITHM = "DH";
    private static final String AES_ALGORITHM = "AES";
    private static final int DH_KEY_SIZE = 2048;  // Use 2048-bit keys for strong security

    /**
     * Generate a Diffie–Hellman key pair.
     */
    public static KeyPair generateDHKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(DH_ALGORITHM);
        kpg.initialize(DH_KEY_SIZE, new SecureRandom());
        return kpg.generateKeyPair();
    }

    /**
     * Compute the shared secret using the private key and peer's public key.
     */
    public static byte[] computeSharedSecret(PrivateKey myPrivateKey, PublicKey peerPublicKey) throws Exception {
        KeyAgreement keyAgreement = KeyAgreement.getInstance(DH_ALGORITHM);
        keyAgreement.init(myPrivateKey);
        keyAgreement.doPhase(peerPublicKey, true);
        return keyAgreement.generateSecret();
    }

    /**
     * Derive a 256-bit AES key from the shared secret bytes.
     * (In production, consider using a proper KDF like HKDF.)
     */
    public static SecretKeySpec deriveAESKey(byte[] sharedSecret) {
        // Use first 32 bytes for a 256-bit AES key.
        byte[] keyBytes = Arrays.copyOfRange(sharedSecret, 0, 32);
        return new SecretKeySpec(keyBytes, AES_ALGORITHM);
    }

    /**
     * Encrypt plaintext using AES/ECB/PKCS5Padding.
     * Note: For production use, consider AES/GCM or AES/CBC with an IV.
     */
    public static byte[] encryptAES(byte[] plaintext, SecretKeySpec aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        return cipher.doFinal(plaintext);
    }

    /**
     * Decrypt ciphertext using AES/ECB/PKCS5Padding.
     */
    public static byte[] decryptAES(byte[] ciphertext, SecretKeySpec aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        return cipher.doFinal(ciphertext);
    }
}
