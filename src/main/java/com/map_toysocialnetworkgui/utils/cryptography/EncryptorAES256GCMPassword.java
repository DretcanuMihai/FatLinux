package com.map_toysocialnetworkgui.utils.cryptography;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES-256 encryptor class
 * AES-256-GCM inputs - 12 bytes IV, needs the same IV and secret keys for encryption and decryption.
 * The output consist of IV, password's salt, encrypted content and auth tag in the following format:
 *     output = byte[] { i i i s s s c c c c c c ... }
 *     i = IV bytes
 *     s = Salt bytes
 *     c = content bytes (encrypted content)
 */
public class EncryptorAES256GCMPassword {
    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";
    private static final int TAG_LENGTH_BIT = 128;      // must be one of { 128, 120, 112, 104, 96 }
    private static final int IV_LENGTH_BYTE = 12;       // 12 or 16 bytes fo IV
    private static final int SALT_LENGTH_BYTE = 16;     // 16 bytes for salt
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     * generates a base64 encoded AES-256-GCM encrypted text
     *
     * @param text - text to encrypt and encode
     * @param password - password used for encryption
     * @return - said text
     * @throws Exception - if encryption fails
     */
    public static String encrypt(byte[] text, String password) throws Exception {
        byte[] salt = CryptoUtils.getRandomNonce(SALT_LENGTH_BYTE);
        byte[] iv = CryptoUtils.getRandomNonce(IV_LENGTH_BYTE);

        // Secret key from password
        SecretKey AESKeyFromPassword = CryptoUtils.getAESKeyFromPassword(password.toCharArray(), salt);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

        // AES-256-GCM needs GCMParameterSpec
        cipher.init(Cipher.ENCRYPT_MODE, AESKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        byte[] cipherText = cipher.doFinal(text);

        // Prefix IV and salt to cipher text
        byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array();

        // String representation, base64
        return Base64.getEncoder().encodeToString(cipherTextWithIvSalt);
    }

    /**
     * decrypts a base64 encoded AES-256-GCM encrypted text
     * needs the same password, salt and IV to decrypt it
     *
     * @param encryptedText - text to decrypt
     * @param password - password used for encryption
     * @return - said decrypted text
     * @throws Exception - if decryption fails
     */
    private static String decrypt(String encryptedText, String password) throws Exception {
        byte[] decode = Base64.getDecoder().decode(encryptedText.getBytes(UTF_8));

        // Get back the IV and salt from the cipher text
        ByteBuffer bb = ByteBuffer.wrap(decode);
        byte[] iv = new byte[IV_LENGTH_BYTE];
        bb.get(iv);
        byte[] salt = new byte[SALT_LENGTH_BYTE];
        bb.get(salt);
        byte[] cipherText = new byte[bb.remaining()];
        bb.get(cipherText);

        // Get back the AES key from the same password and salt
        SecretKey AESKeyFromPassword = CryptoUtils.getAESKeyFromPassword(password.toCharArray(), salt);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, AESKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        byte[] text = cipher.doFinal(cipherText);
        return new String(text, UTF_8);
    }
}
