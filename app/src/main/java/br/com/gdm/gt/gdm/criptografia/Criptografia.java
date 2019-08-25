package br.com.gdm.gt.gdm.criptografia;

import android.util.Log;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by estac on 17/04/2018.
 */

public class Criptografia {
    private static final String TAG_ERRO = "ERRO!";
    private static final String TAG_INFORMACAO = "INFORMAÇÃO!";

    public static String criptografar(String s){
        String strCriptografada = "";
        try {
            KeyGenerator chave = KeyGenerator.getInstance("AES");
            SecretKey chaveSecreta = chave.generateKey();
            Cipher cifra = Cipher.getInstance("AES");
            cifra.init(Cipher.ENCRYPT_MODE,chaveSecreta);
            byte[] auxCripto = cifra.doFinal(s.getBytes());
            strCriptografada = auxCripto.toString();
            Log.i(TAG_INFORMACAO,strCriptografada);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG_ERRO,e.getMessage());
        } catch (NoSuchPaddingException e) {
            Log.e(TAG_ERRO,e.getMessage());
        } catch (InvalidKeyException e) {
            Log.e(TAG_ERRO,e.getMessage());
        } catch (BadPaddingException e) {
            Log.e(TAG_ERRO,e.getMessage());
        } catch (IllegalBlockSizeException e) {
            Log.e(TAG_ERRO,e.getMessage());
        }
        return strCriptografada;
    }

    public static String descriptografar(String s){
        String strDescriptografada = "";
        try {
            KeyGenerator chave = KeyGenerator.getInstance("AES");
            SecretKey chaveSecreta = chave.generateKey();
            Cipher cifra = Cipher.getInstance("AES");
            cifra.init(Cipher.DECRYPT_MODE,chaveSecreta);
            byte[] auxCripto = cifra.doFinal(s.getBytes());
            strDescriptografada = auxCripto.toString();
            Log.i(TAG_INFORMACAO,strDescriptografada);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG_ERRO,e.getMessage());
        } catch (NoSuchPaddingException e) {
            Log.e(TAG_ERRO,e.getMessage());
        } catch (InvalidKeyException e) {
            Log.e(TAG_ERRO,e.getMessage());
        } catch (BadPaddingException e) {
            Log.e(TAG_ERRO,e.getMessage());
        } catch (IllegalBlockSizeException e) {
            Log.e(TAG_ERRO,e.getMessage());
        }
        return strDescriptografada;
    }
}
