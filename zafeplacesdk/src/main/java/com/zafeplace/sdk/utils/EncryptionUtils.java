package com.zafeplace.sdk.utils;

import android.util.Base64;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class EncryptionUtils {

    public static String encryption(String password, String text) {
        String encryptedMsg = "";
        try {
            encryptedMsg = AESCrypt.encrypt(password, text);
        }catch (GeneralSecurityException e){
            e.printStackTrace();
        }
        return encryptedMsg;
    }

    public static String decryption(String password, String text) {
        String messageAfterDecrypt = "";
        try {
            messageAfterDecrypt = AESCrypt.decrypt(password, text);
        }catch (GeneralSecurityException e){
            e.printStackTrace();
        }
        return messageAfterDecrypt;
    }
}
