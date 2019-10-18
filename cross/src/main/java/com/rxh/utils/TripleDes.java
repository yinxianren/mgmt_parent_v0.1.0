package com.rxh.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;


/**
 *
 * @author xie
 *
 */
public class TripleDes {

	private static Logger logger = Logger.getLogger(TripleDes.class.getName());

	private static final String Algorithm = "DESede"; //���� �����㷨,���� DES,DESede,Blowfish
	private static final String ENCODING = "UTF-8";

	/**
	 * ���ܣ�ʹ��CreditBase64����
	 * @param keybyte
	 * @param src
	 * @return
	 */
	public static String encrypt(String keybyte, String src) {

		try {

			SecretKey deskey = new SecretKeySpec(CreditBase64.decode(keybyte), Algorithm);

			Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			byte[] bts = c1.doFinal(src.getBytes(ENCODING));

			return CreditBase64.encode(bts);

		} catch (Exception e) {
			logger.error(Strings.toString(e));
		}
		return "";
	}

	/**
	 *
	 * @param keybyte
	 * @param src
	 * @return
	 */
	public static String decrypt(String keybyte, String src) {

		try {

			SecretKey deskey = new SecretKeySpec(CreditBase64.decode(keybyte), Algorithm); //

			Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c1.init(Cipher.DECRYPT_MODE, deskey);

			byte[] bts = c1.doFinal(CreditBase64.decode(src));

			return new String(bts,ENCODING);
		} catch (Exception e) {
			logger.error(Strings.toString(e));
		}
		return null;
	}

	/**
	 *
	 * @param keybyte
	 * @param src
	 * @return
	 */
	public static byte[] encrypt(byte[] keybyte, byte[] src) {

		try {

			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			byte[] bts = c1.doFinal(src);

			return bts;

		} catch (Exception e) {
			logger.error(Strings.toString(e));
		}
		return null;
	}

	/**
	 *
	 * @param keybyte
	 * @param src
	 * @return
	 */
	public static byte[] decrypt(byte[] keybyte, byte[] src) {

		try {

			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
			c1.init(Cipher.DECRYPT_MODE, deskey);

			byte[] bts = c1.doFinal(src);

			return bts;
		} catch (Exception e) {
			logger.error(Strings.toString(e));
		}
		return null;
	}

	/**
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String keybyte = CreditBase64.encode("67d10dc16d8901600d57fd83".toUpperCase().getBytes());

		String enc = "hz7l9lpO3j1cTdxJ+S4L3wWHb+CXheG4brgnchO0kxA7AkNdeyNUsLA82tfc4Ds6WvOzNufh4JD1dXbu5m68/Q==";

		System.out.println(TripleDes.decrypt(keybyte, enc));

		//String src = "abc:[0200 190011 111 2016110657248621 06635595 0.1 156]";
		//System.out.println(TripleDes.encrypt(keybyte, src));

		/*        System.out.println(CreditBase64.encode(Rsa.getPublicKey("c:\\gdyilian_merchant_signature.pfx", "80115864").getEncoded()));
        if(1==1) return;

		//String easy_pub_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCqWSfUW3fSyoOYzOG8joy3xldpBanLVg8gEDcvm9KxVjqvA/qJI7y0Rmkc1I7l9vAfWtNzphMC+wlulpaAsa/4PbfVj+WhoNQyhG+m4sP27BA8xuevNT9/W7/2ZVk4324NSowwWkaqo1yuZe1wQMcVhROz2h+g7j/uZD0fiCokWwIDAQAB";
		String merchant_pub_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC8JiMYfBZx5+WGEnKbQOGB4D8BkkH/2VR5x3xKkD84HjeWhnoBtP+cSY9H2uB0tb8GTtPU/qM0mK3JcUDIboVbbZjmSgDZrnAtAM5OXW2oeUdDFQNvCqsM5/IZuKco6Fl8KNp0GNDla8gcw7YNWFeOyoIJbHVCfRDn5bibSNwJiQIDAQAB";
		String merchant_pfx_key = "c:\\merchant.pfx";

		String a = "abcde";
		System.out.println("����="+a);

		//ǩ��
		String b = Rsa.sign(a,merchant_pfx_key,"11111111");
		System.out.println("ǩ��="+b);


		//��ǩ
		boolean c = Rsa.verify(b,merchant_pub_key,a);
		System.out.println("��ǩ="+c);

		//����
    	String d = "876578900988098765678899";
		System.out.println("key="+d);
        String e = TripleDes.encrypt(d, a);
		System.out.println("��������="+e);
        String e1 = TripleDes.encrypt(d, b);
		System.out.println("ǩ������="+e1);

        String f = Rsa.encrypt(d, merchant_pub_key);
		System.out.println("key����="+f);

		//����
		String g = Rsa.decrypt(f, merchant_pfx_key, "11111111");
		System.out.println("key="+g);

		String h = TripleDes.decrypt(g, e);
		System.out.println("����="+h);
		String h1 = TripleDes.decrypt(g, e1);
		System.out.println("ǩ��="+h1);

		System.out.println(a.equals(h));
		System.out.println(b.equals(h1));*/
	}
}
