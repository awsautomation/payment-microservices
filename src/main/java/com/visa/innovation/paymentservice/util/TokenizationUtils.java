package com.visa.innovation.paymentservice.util;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.stereotype.Component;

import com.visa.innovation.paymentservice.vdp.model.tokenization.KeyResponseVDP;
/**
 * TODO: This class is for Tokenization , it is a temporary code that needs to removed/modified 
 * 
 * @author akakade
 *
 */
@Component
public class TokenizationUtils {

	private Base64.Decoder DECODER = Base64.getDecoder();

	public void encryptPAN(String pan, KeyResponseVDP keyResponseVDP) {

		 PublicKey flexPublicKey = decodePublicKey(keyResponseVDP.getDer().getPublicKey());
		
	}

	public PublicKey decodePublicKey(String encodedKey) {

		try {
			byte[] keyBytes = DECODER.decode(encodedKey);
			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyBytes));
			return publicKey;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean verify(final PublicKey publicKey, final String dataToVerify, final String base64Signature) {
        try {
            final Signature signInstance = Signature.getInstance("SHA512withRSA");
            signInstance.initVerify(publicKey);
            signInstance.update(dataToVerify.getBytes());
            byte[] signature = DECODER.decode(base64Signature);
            return signInstance.verify(signature);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException ex) {
            throw new RuntimeException(ex);
        }
    }
}
