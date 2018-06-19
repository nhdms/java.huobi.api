/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author X
 */
public class HMAC {
    public static final String HMAC_SHA256 = "HmacSHA256";
    public static byte[] generateHMAC(String data, String secret, String HMACType) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), HMACType);
        Mac mac = Mac.getInstance(HMACType);
        mac.init(secretKeySpec);
        return mac.doFinal(data.getBytes());
    }
}
