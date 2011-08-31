/**
 * CryptoUtils
 * 
 */

package info.mattweppler.sharedcomponents;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class CryptoUtils
{

	public static void main(String[] args)
	{
		String message = messageCryptography("This is a test  ", "ENCRYPT");
		System.out.println(message);
		System.out.println(messageCryptography(message, "DECRYPT"));
	}

	public static String messageCryptography(String message, String type)
	{
		String keyString = "uzlY0fIjAHDLmy3C";
		byte[] keyBytes = keyString.getBytes();
		SecretKeySpec secret = new SecretKeySpec(keyBytes, "AES");
		Cipher cipher = null;
		Base64 base64 = new Base64();
		byte[] cryptBytes = null;
		try {
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			if (type.equals("ENCRYPT")) {
				cipher.init(Cipher.ENCRYPT_MODE, secret);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, secret);
			}
			if (type.equals("ENCRYPT")) {
				cryptBytes = cipher.doFinal(message.getBytes());
			} else {
				cryptBytes = cipher.doFinal(base64.decode(message.getBytes()));
			}
		} catch (NoSuchAlgorithmException nsae) {
			nsae.printStackTrace();
		} catch (NoSuchPaddingException nspe) {
			nspe.printStackTrace();
		} catch (InvalidKeyException ike) {
			ike.printStackTrace();
		} catch (IllegalBlockSizeException ibse) {
			ibse.printStackTrace();
		} catch (BadPaddingException bpee) {
			bpee.printStackTrace();
		}
		if (type.equals("ENCRYPT")) {
			return new String(base64.encode(cryptBytes));
		} else {
			return new String(cryptBytes);
		}
	}
}
