package manager;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class AEScodingManager {

	private static String secretKeyString = "cebacafcbbbfbcfc";
	private static String msgToSend = "";
	private static String msgGot = "";
	
	
	public AEScodingManager(String inputMsgToSend) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		msgToSend = inputMsgToSend;
		//generateSkey();

		SecretKeySpec secretKey = new SecretKeySpec(secretKeyString.getBytes(),"AES");
		encodeByAES(secretKey);
	}
	
	public void generateSkey() throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		
		kgen.init(128);
		
		SecretKey skey = kgen.generateKey();
		secretKeyString = Hex.encodeHexString(skey.getEncoded());

		//System.out.println("sKey : "+secretKeyString);
		encodeByAES(skey);
		
	}
	
	//암호화
	public void encodeByAES(SecretKey inputSkey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		SecretKeySpec sKeySpec = new SecretKeySpec(inputSkey.getEncoded(),"AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE,sKeySpec);
		byte[] encrypted = cipher.doFinal(msgToSend.getBytes());
		//System.out.println("msg to send : "+msgToSend);
		//System.out.println("encrypted string : " + Hex.encodeHexString(encrypted));
		
		
		
		
	}
	
	//복호
	public static String decodeByAES(SecretKey inputSkey )throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
		SecretKeySpec sKeySpec = new SecretKeySpec(inputSkey.getEncoded(),"AES");
		Cipher cipher = Cipher.getInstance("AES");
		byte[] encrypted = cipher.doFinal(msgGot.getBytes());
		
		cipher.init(Cipher.DECRYPT_MODE,sKeySpec);
		byte[] original = cipher.doFinal(encrypted);
		String originalString = new String(original);
		//System.out.println("original string : " + originalString + " "+ Hex.encodeHexString(original));
		return originalString;
	}
	
}
