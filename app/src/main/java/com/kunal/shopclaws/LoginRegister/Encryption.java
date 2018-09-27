package com.kunal.shopclaws.LoginRegister;

import android.util.Base64;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
public class Encryption
{
    //Advanced Encryption Standard algorithm used
    private static final String ALGORITHM = "AES";
    //Key for encryption fed to Base64
    private static final String KEY = "1Hbfh667adfDEJ78";

    //Encyption function
    public static String encrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(Encryption.ALGORITHM);
        //Initialising Encryption mode
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;

    }
    //Decyption Function
    public static String decrypt(String value) throws Exception
    {
        Key key = generateKey();
        Cipher cipher = Cipher.getInstance(Encryption.ALGORITHM);
        //Initialising Decryption mode
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;

    }
    // Generating a key using given algorithm(AES) and feeding to encryption and decryption function
    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(Encryption.KEY.getBytes(), Encryption.ALGORITHM);
        return key;
    }
}