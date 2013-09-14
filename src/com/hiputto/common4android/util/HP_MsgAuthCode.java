package com.hiputto.common4android.util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class HP_MsgAuthCode {

	static byte[] mac(byte[] key, byte[] data) throws NoSuchAlgorithmException,
			NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, IllegalStateException {

		return mac(key, data, 0, data.length);
	}

	static byte[] mac(byte[] key, byte[] data, int offset, int len)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, IllegalStateException {
		final String Algorithm = "DES";

		SecretKey deskey = new SecretKeySpec(key, Algorithm);

		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE, deskey);

		byte buf[] = { 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
		for (int i = 0; i < len;) {
			for (int j = 0; j < 8 && i < len; i++, j++) {
				buf[j] ^= data[offset + i];
			}
			buf = c1.update(buf);
		}
		c1.doFinal();
		return buf;
	}

	static byte[] desEncryption(byte[] key, byte[] data)
			throws NoSuchAlgorithmException, Exception {
		final String Algorithm = "DES/ECB/NoPadding";

		if (key.length != DESKeySpec.DES_KEY_LEN || data.length != 8)
			throw new IllegalArgumentException("key or data's length != 8");

		DESKeySpec desKS = new DESKeySpec(key);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey deskey = skf.generateSecret(desKS);

		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE, deskey);

		byte buf[];
		buf = c1.doFinal(data);

		byte[] enc_data = new byte[8];
		System.arraycopy(buf, 0, enc_data, 0, 8);
		return enc_data;
	}

	static byte[] desDecryption(byte[] key, byte[] data)
			throws NoSuchAlgorithmException, Exception {
		final String Algorithm = "DES/ECB/NoPadding";

		if (key.length != DESKeySpec.DES_KEY_LEN || data.length != 8)
			throw new IllegalArgumentException(
					"key's len != 8 or data's length != 8");

		SecretKey deskey = new SecretKeySpec(key, "DES");

		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.DECRYPT_MODE, deskey);

		byte decrypted[];

		decrypted = c1.doFinal(data);

		return decrypted;
	}

	byte[] encryptByDES(byte[] bytP, byte[] bytKey) throws Exception {
		DESKeySpec desKS = new DESKeySpec(bytKey);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey sk = skf.generateSecret(desKS);
		Cipher cip = Cipher.getInstance("DES");
		cip.init(Cipher.ENCRYPT_MODE, sk);
		return cip.doFinal(bytP);
	}

	byte[] decryptByDES(byte[] bytE, byte[] bytKey) throws Exception {
		DESKeySpec desKS = new DESKeySpec(bytKey);
		SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
		SecretKey sk = skf.generateSecret(desKS);
		Cipher cip = Cipher.getInstance("DES");
		cip.init(Cipher.DECRYPT_MODE, sk);
		return cip.doFinal(bytE);
	}

	static byte[] des3Encryption(byte[] key, byte[] data)
			throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, IllegalStateException {
		final String Algorithm = "DESede";

		SecretKey deskey = new SecretKeySpec(key, Algorithm);

		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE, deskey);
		return c1.doFinal(data);
	}

	static byte[] des3Decryption(byte[] key, byte[] data)
			throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, IllegalStateException {
		final String Algorithm = "DESede";

		SecretKey deskey = new SecretKeySpec(key, Algorithm);

		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.DECRYPT_MODE, deskey);
		return c1.doFinal(data);
	}

	static byte[] des3Encryption(byte[] key, byte[] iv, byte[] data)
			throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, IllegalStateException,
			InvalidAlgorithmParameterException, InvalidKeySpecException {
		final String Algorithm = "DESede/CBC/PKCS5Padding";
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKey deskey = keyFactory.generateSecret(spec);
		IvParameterSpec tempIv = new IvParameterSpec(iv);
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.ENCRYPT_MODE, deskey, tempIv);
		return c1.doFinal(data);

	}

	static byte[] des3Decryption(byte[] key, byte[] iv, byte[] data)
			throws NoSuchPaddingException, NoSuchAlgorithmException,
			InvalidKeyException, BadPaddingException,
			IllegalBlockSizeException, IllegalStateException,
			InvalidAlgorithmParameterException, InvalidKeySpecException {
		final String Algorithm = "DESede/CBC/PKCS5Padding";
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
		DESedeKeySpec spec = new DESedeKeySpec(key);
		SecretKey deskey = keyFactory.generateSecret(spec);
		IvParameterSpec tempIv = new IvParameterSpec(iv);
		Cipher c1 = Cipher.getInstance(Algorithm);
		c1.init(Cipher.DECRYPT_MODE, deskey, tempIv);
		return c1.doFinal(data);

	}

}