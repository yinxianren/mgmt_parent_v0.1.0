package com.rxh.utils;

import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.HashMap;

import javax.crypto.Cipher;

import org.apache.log4j.Logger;


/**
 *
 * @author xie
 *
 */
public class Rsa {

	private static Logger logger = Logger.getLogger(Rsa.class.getName());

	private static final String ENCODING = "UTF-8";


	/**
	 * ʹ��rsa����ǩ�����㷨֧��MD5withRSA\SHA256withRSA
	 * @param data
	 * @param pfx_path
	 * @param key_pass
	 * @param algorithm
	 * @return
	 * @throws Exception
	 */
	public static String sign(String data, String pfx_path, String key_pass,String algorithm) throws Exception {

		try {
			RSAPrivateKey pbk = getPrivateKey(pfx_path, key_pass);

			//Class<?> c = Class.forName("sun.security.rsa.SunRsaSign");
			//Provider provider = (Provider)c.newInstance();
			//Signature signet = Signature.getInstance(algorithm,provider);
			Signature signet = Signature.getInstance(algorithm);
			signet.initSign(pbk);
			signet.update(data.getBytes(ENCODING));
			byte[] signed = signet.sign();

			return CreditBase64.encode(signed);

		} catch (Exception e) {
			//logger.error(Strings.toString(e));
			return "";
		}
	}

	/**
	 * ʹ��rsa������ǩ���㷨֧��MD5withRSA\SHA256withRSA
	 * @param signMsg
	 * @param publicKey
	 * @param data
	 * @param algorithm
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(String signMsg, String publicKey, String data,String algorithm) throws Exception {

		try {
			byte[] btsData = CreditBase64.decode(signMsg);
			byte[] btsKey = CreditBase64.decode(publicKey);

			//Class<?> c = Class.forName("com.sun.net.ssl.internal.ssl.Provider");
			//Provider provider = (Provider)c.newInstance();
			//KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA",provider);
			KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(btsKey);
			RSAPublicKey pbk = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);

			//Class<?> c2 = Class.forName("sun.security.rsa.SunRsaSign");
			//Provider provider2 = (Provider)c2.newInstance();
			//Signature signetcheck = Signature.getInstance(algorithm,provider2);
			Signature signetcheck = Signature.getInstance(algorithm);
			signetcheck.initVerify(pbk);
			signetcheck.update(data.getBytes(ENCODING));

			return signetcheck.verify(btsData);

		} catch (Exception e) {
			logger.error(Strings.toString(e));
			return false;
		}
	}

	/**
	 * ʹ��rsa���м��ܣ��㷨֧��MD5withRSA\SHA256withRSA
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String data,String publicKey) throws Exception {

		//Class<?> c = Class.forName("sun.security.rsa.SunRsaSign");
		//Provider provider = (Provider)c.newInstance();
		//KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA",provider);
		KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(CreditBase64.decode(publicKey));
		RSAPublicKey pbk = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);

		//Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING","SunJCE");
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, pbk);

		byte[] encDate = cipher.doFinal(data.getBytes(ENCODING));

		return CreditBase64.encode(encDate);
	}

	/**
	 * ʹ��rsa���м��ܣ��㷨֧��MD5withRSA\SHA256withRSA
	 * @param data
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static String encryptCreditBase64(String data,String publicKey) throws Exception {

		//Class<?> c = Class.forName("sun.security.rsa.SunRsaSign");
		//Provider provider = (Provider)c.newInstance();
		//KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA",provider);
		KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(CreditBase64.decode(publicKey));
		RSAPublicKey pbk = (RSAPublicKey) rsaKeyFac.generatePublic(keySpec);

		//Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING","SunJCE");
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, pbk);

		byte[] encDate = cipher.doFinal(data.getBytes(ENCODING));

		return CreditBase64.encode(encDate);
	}


	/**
	 * ʹ��rsa���н��ܣ��㷨֧��MD5withRSA\SHA256withRSA
	 * @param data
	 * @param pfx_path
	 * @param pfx_pass
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String data, String pfx_path, String pfx_pass) throws Exception {

		try {

			RSAPrivateKey pbk = getPrivateKey(pfx_path, pfx_pass);

			//Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING","SunJCE");
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
			cipher.init(Cipher.DECRYPT_MODE, pbk);

			byte[] btSrc =cipher.doFinal(CreditBase64.decode(data));

			return new String(btSrc,ENCODING);

		} catch (Exception e) {
			logger.error(Strings.toString(e));
			return "";
		}
	}

	private static HashMap<String,PrivateKey> privateKeys = new HashMap<String,PrivateKey>();
	/**
	 * ��ȡ�ļ���˽Կ
	 * @param keyPath
	 * @param passwd
	 * @return
	 * @throws Exception
	 */
	public static RSAPrivateKey getPrivateKey(String keyPath, String passwd) throws Exception {


		//Class<?> c = Class.forName("com.sun.net.ssl.internal.ssl.Provider");
		//Provider provider = (Provider)c.newInstance();
		//KeyStore ks = KeyStore.getInstance("PKCS12",provider);
		PrivateKey prikey = privateKeys.get(keyPath);
		if(prikey == null) {
			KeyStore ks = KeyStore.getInstance("PKCS12");
			FileInputStream fis = new FileInputStream(keyPath);

			char[] nPassword = null;
			if ((passwd == null) || passwd.trim().equals("")) {
				nPassword = null;
			} else {
				nPassword = passwd.toCharArray();
			}
			ks.load(fis, nPassword);
			fis.close();

			Enumeration<String> enumq = ks.aliases();
			String keyAlias = null;
			if (enumq.hasMoreElements()) {
				keyAlias = (String) enumq.nextElement();
			}

			prikey = (PrivateKey) ks.getKey(keyAlias, nPassword);

			privateKeys.put(keyPath, prikey);
		}

		return (RSAPrivateKey) prikey;
	}

	private static HashMap<String,PublicKey> publicKeys = new HashMap<String,PublicKey>();
	/**
	 * ��ȡ�ļ��Ĺ�Կ
	 * @param keyPath
	 * @param passwd
	 * @return
	 * @throws Exception
	 */
	public static RSAPublicKey getPublicKey(String keyPath, String passwd) throws Exception {

		//Class<?> c = Class.forName("com.sun.net.ssl.internal.ssl.Provider");
		//Provider provider = (Provider)c.newInstance();
		//KeyStore ks = KeyStore.getInstance("PKCS12",provider);
		PublicKey pubkey = publicKeys.get(keyPath);

		if(pubkey == null) {

			KeyStore ks = KeyStore.getInstance("PKCS12");

			FileInputStream fis = new FileInputStream(keyPath);

			char[] nPassword = null;
			if ((passwd == null) || passwd.trim().equals("")) {
				nPassword = null;
			} else {
				nPassword = passwd.toCharArray();
			}
			ks.load(fis, nPassword);
			fis.close();

			Enumeration<String> enumq = ks.aliases();
			String keyAlias = null;
			if (enumq.hasMoreElements()) {
				keyAlias = (String) enumq.nextElement();
			}

			Certificate cert = ks.getCertificate(keyAlias);

			pubkey = cert.getPublicKey();

			publicKeys.put(keyPath, pubkey);

		}

		return (RSAPublicKey) pubkey;
	}

	/**
	 * ����RSA��˽Կ�������ڴ˽Կ��
	 * @param info
	 * @return
	 */
	/*public static boolean makeCerts(RsaInfo info) {

		FileOutputStream fout = null;
		try {

			//Class<?> c = Class.forName("com.sun.net.ssl.internal.ssl.Provider");
			//Provider provider = (Provider)c.newInstance();
			//KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA",provider);
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

			//�����Դ
			SecureRandom secureRandom = new SecureRandom();
			//KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(info.getKeySize(), secureRandom);
			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			PublicKey publicKey = keyPair.getPublic();
			PrivateKey privateKey = keyPair.getPrivate();

			info.setPublicKeyCreditBase64(CreditBase64.encode(publicKey.getEncoded()));
			info.setPrivateKeyCreditBase64(CreditBase64.encode(privateKey.getEncoded()));

			char[] pwd = info.getPrivateKeyPwd().toCharArray();

			KeyStore store = KeyStore.getInstance("PKCS12");
			store.load(null, null);

			X509V3CertificateGenerator certGen = new X509V3CertificateGenerator();

			certGen.setSerialNumber(info.getSerial());
			certGen.setIssuerDN(new X500Principal(info.getIssuer()));
			certGen.setSubjectDN(new X500Principal(info.getSubject()));

			certGen.setNotBefore(new Date());
			Calendar now = Calendar.getInstance();
			now.add(Calendar.DATE, info.getValidDate());
			certGen.setNotAfter(now.getTime());

			certGen.setPublicKey(keyPair.getPublic());
			certGen.setSignatureAlgorithm(info.getAlgorithm());
			Security.addProvider(new BouncyCastleProvider());
			X509Certificate cert = certGen.generateX509Certificate(keyPair.getPrivate());

			store.setKeyEntry(info.getAtlas(), keyPair.getPrivate(),pwd, new Certificate[] { cert });
			String path="d:/"+info.getAtlas()+".pfx";
//			String path=Config.getConVal("RSA.SAVE_TO_PATH")+info.getAtlas()+".pfx";
//			fout = new FileOutputStream(Config.getConVal("RSA.SAVE_TO_PATH")+info.getAtlas()+".pfx");
			fout = new FileOutputStream(path);
			store.store(fout, pwd);
			fout.close();
			info.setPrivateKeyPath(path);
		} catch(Exception e) {
			logger.error(Strings.toString(e));
			return false;
		} finally {
			if(fout != null) {

				try {
					fout.close();
				} catch (IOException e) {
					logger.error(Strings.toString(e));
				}
			}
		}

		return true;
	}*/

	private static boolean ret = true;
	/**
	 *
	 * @param args
	 * @throws Exception
	 */
	private static String getRSAPublicKeyAsNetFormat(byte[] encodedPrivkey) {
		try {
			StringBuffer buff = new StringBuffer(1024);

			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			RSAPublicKey pukKey=(RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(encodedPrivkey));

			buff.append("<RSAKeyValue>");
			buff.append("<Modulus>"
					+ CreditBase64.encode(removeMSZero(pukKey.getModulus().toByteArray()))
					+ "</Modulus>");
			buff.append("<Exponent>"
					+ CreditBase64.encode(removeMSZero(pukKey.getPublicExponent()
							.toByteArray())) + "</Exponent>");
			buff.append("</RSAKeyValue>");
			return buff.toString().replaceAll("[ \t\n\r]", "");
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}
	private static byte[] removeMSZero(byte[] data) {
		byte[] data1;
		int len = data.length;
		if (data[0] == 0) {
			data1 = new byte[data.length - 1];
			System.arraycopy(data, 1, data1, 0, len - 1);
		} else
			data1 = data;

		return data1;
	}

	public static void main(String[] args) throws Exception {

		/*		RsaInfo info = new RsaInfo(BigInteger.valueOf(Long.parseLong(SeqNo.randomNumber(15))),"11111111");
		info.setSubject("����","�㶫","����","л��Ȩ");
		makeCerts(info);
		System.out.println(info.getPublicKeyCreditBase64());

		if(ret)return;*/

/*		String px = "D:/dna/dna.soeasy/project/easyTester/merchant.pfx";
		String pw = "11111111";
		String pk1 = "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgGiYdXXRFuYDIG5Q8wKc49Bj4kz4J/2DMRN7NIipZt1GxeYGx3hCPAVRv45fFTTvd50WKQQK1fbBABec3MUIghTvJ+r9bulaxaMPkNM69DKMTMFun5bwtkTUmfx+X1T1+9/l2LKc+JC9ux3vxYYkl0iz/Kd4qrol6FRM79GV/QrLAgMBAAE=";
		String sm = "NEbIaffX9UihUVUv6P0KW+IggX1pkJw8CM8Sc/fYpn/u5Ko4B0HUeIhUElzIQeSEdXqDJ0iCqWMJiXf3c6xdYHYRdh61nuYnXsCamFAyfSM1ZGs9ufTZgGZr0WMzxSdct+Z/NSKZGA3DhfI7lX8eJ6Sqv4WYX7zyTR2uIxHT6cs=";
		String d = "0210 190011 022600000001 201702266474226 022664742268 032017022600000018 0.10 20170226 0226150943150943 03 0000";
		boolean v = Rsa.verify(sm, pk1, d, "SHA256WITHRSA");
		//sm = Rsa.sign(d,px,pw,"SHA1WITHRSA");
		//System.out.println(sm);
		//v = Rsa.verify(sm, pk1, d, "SHA1WITHRSA");

		System.out.println(v);
		*/
		boolean stop = true;

		byte[] temp1=CreditBase64.decode("");
		String ver1=getRSAPublicKeyAsNetFormat(temp1);//ת����Կ
		System.out.println(ver1);
		if(stop) return;

		//System.out.println(Rsa.decrypt("CMK4dTlFLOnK7rhj1crEhfILYUw/tzra1V8K9Av9oB6F6eo8EUzuESIqz50Q+wxlUcg+/fXcq/pyZ+cCUfR0VhrhrWYysotr7oo3O/VmVGFSEL+33jRkX5LcIulXd7G66HM15xLhz1UlOMljJQpEB9e05OOliOMvMXq0zCeKkJA=", px, pw));


		String epp = "D:/dna/dna.soeasy/project/easyTester/merchant.pfx";
		String ppw = "11111111";
		RSAPublicKey pk = Rsa.getPublicKey(epp, ppw);
		System.out.println(CreditBase64.encode(pk.getEncoded()));
		String dec = Rsa.decrypt("SUNMkQbg/9ht8b3nVunW9nZAjXtW3JsJWpoxwz9LNj9QI8JzET/Lcj4e/FPJD9AmQ7NmyvMMPduR2/s8TQbUFziBnQLg+lV1dEx3DrfxRPKOJd1DIT3njGimhz7MHBywLPRu2WkhT6Ga3uw6hUvqgQrXxmOSkc+vx2wsl2n9vsg=",epp,ppw);
		System.out.println(dec);

		if(stop) return;

		String signMsg = "N74vIYtIpvXLdpNyix16STIsbF+0Zua6x0UUuyEIk8eeP7uYkTlOyz4/J+KqHD0XA5EffQ0xVorJZywQjHnz4aQ1oP6JoS+a8TqyvI+q3mJnX3kaMfBiaDvCLv65T0c8OTKGceonumvEzBtczCS5Hm/7DQDHBDMGnrkAcdszjQGm9+rmsfAwIlzOUdHoIdnI/93jFWiaiS+VC6LbCQq1/ww/7CDpe566wNjex3OHVkS+0vKjAURDppnQJJQaY7pZQNaEVX9XSpCRpIJG60lgXD75TuGgfq8FJ/B41+2hFXHXGozRWQXBBDM8g5LUnU2PeEHPwdfmJgVSkHnOAbGKFA==";
		String pkey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAlJDjMFKfofvAT1hNa2UG8vWqpfs3/3qBl3iC2e4ZYYO5f+nRg5V5rqgtcrCh7DGf9G2RWTf6dYb4zWWJCRglvj5KL4p9weC7SgVSO8P5quF+NNdQ8WTnfXO6Gq4Hpf2cMGfJgbHbRCdyLW+TOaQ6gkwwECUMq1KDyLAXWCr4AyJ8dIPxxC0jw3TtnVTS7co0kNBjP2YurFl0BVbsyZXG62+x6W4u1j67+Hlkm6eXf7NJ68dNVBeuJmzkzhXIGTrg+IOR0mGBLo7pfBmh1LQbMKpA8gjmwc38i3lHfhzxpdaMB1yfCwk0X+Tk2Qemz/5asQ1qkfHV/1n2WBj7wZ7snQIDAQAB";
		String data = "0200 190001 502016000001 201603046328867 030463288676 100 6228480082811820214";

		boolean verify = Rsa.verify(signMsg, pkey, data, "SHA256WITHRSA");

		System.out.println(verify);

		//if(ret)return;

		String pfxPath = "E:\\yilianpfx-Encipherment.pfx";
		String pfxPass = "42124114";
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJ1fKGMV/yOUnY1ysFCk0yPP4bfOolC/nTAyHmoser+1yzeLtyYsfitYonFIsXBKoAYwSAhNE+ZSdXZs4A5zt4EKoU+T3IoByCoKgvpCuOx8rgIAqC3O/95pGb9n6rKHR2sz5EPT0aBUUDAB2FJYjA9Sy+kURxa52EOtRKolSmEwIDAQAB";

		String pfxPath2048 = "d:\\easy_user.pfx";
		String pfxPass2048 = "11111111";
		String publicKey2048 = "MIIBITANBgkqhkiG9w0BAQEFAAOCAQ4AMIIBCQKCAQBnHMDszsJE8u/gZUf1MqNcZUW/DF1bcaJ9Qb8EesWP1BBDZOKRiFPuefKcSFRMp9e/vHkJOOWjtYt7Yi/zdiQTkXNoiCi6EvV9eqdumOoPsl/VyQDAhAO+TS6mbzW/Um3w0n16Uq71THKP81K9/4qnUdW8e2Jc4y7QV+LQ5z8vrErijtvmtz4vnDrJ+4IWGbza8ryZ1KFHA/hLUb+9xHaDrt79WX2f13epaWEpINKPeTmoqJ/c9XkDeIrZQI3e2V+Gi91yxHPr0/Sl5wRu1z9NmalAcompB6iGKc5lAU4xw8866KvEymW+/cuNObMdg2GI4ZdDM5AGXTHXQdota4NRAgMBAAE=";
		String a = "you want to xxx me?";

		String b = Rsa.encrypt(a, publicKey);
		System.out.println("encrypt:"+b);
		b = Rsa.decrypt(b, pfxPath, pfxPass);
		System.out.println("decrypt:"+b);
		b = Rsa.encrypt(a, publicKey2048);
		System.out.println("encrypt 2048:"+b);
		b = Rsa.decrypt(b, pfxPath2048, pfxPass2048);
		System.out.println("decrypt 2048:"+b);

		//algorithm:MD5withRSA\SHA256withRSA
		b = Rsa.sign(a, pfxPath2048, pfxPass2048, "MD5withRSA");
		System.out.println("\r\nsign:"+b);
		boolean c = Rsa.verify(b, publicKey2048, a,"MD5withRSA");
		System.out.println("verify:"+c);

		b = Rsa.sign(a, pfxPath, pfxPass, "MD5withRSA");
		System.out.println("\r\nsign:"+b);
		c = Rsa.verify(b, publicKey, a,"MD5withRSA");
		System.out.println("verify:"+c);


		b = Rsa.sign(a, pfxPath2048, pfxPass2048, "SHA256withRSA");
		System.out.println("\r\nsign:"+b);
		c = Rsa.verify(b, publicKey2048, a,"SHA256withRSA");
		System.out.println("verify:"+c);

		b = Rsa.sign(a, pfxPath, pfxPass, "SHA256withRSA");
		System.out.println("\r\nsign:"+b);
		c = Rsa.verify(b, publicKey, a,"SHA256withRSA");
		System.out.println("verify:"+c);

		//if(ret)return;


		//String aa = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJ1fKGMV/yOUnY1ysFCk0yPP4bfOolC/nTAyHmoser+1yzeLtyYsfitYonFIsXBKoAYwSAhNE+ZSdXZs4A5zt4EKoU+T3IoByCoKgvpCuOx8rgIAqC3O/95pGb9n6rKHR2sz5EPT0aBUUDAB2FJYjA9Sy+kURxa52EOtRKolSmEwIDAQAB";
		String bb = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmHYF1FrGOiZF24GBcH5uPoWLZR+q6OKxKa+9crDCDlpnVqzWF9Be2CSec4mjDDjBqqzKXBfiA1CJhHKUOKDHeJC/oeCrXSzvC8ZRKIFz7bFlky2kstbQcczUf6cPyRdsWIpOY/i2x9YEaOPI5RjPI32e3+DGUWW1R/a92jV70MQIDAQAB";

		byte[] btsKey = CreditBase64.decode(bb);
		KeyFactory rsaKeyFac = KeyFactory.getInstance("RSA");
		X509EncodedKeySpec keySpecaa = new X509EncodedKeySpec(btsKey);
		RSAPublicKey publickey = (RSAPublicKey) rsaKeyFac.generatePublic(keySpecaa);

		String algorithm = publickey.getAlgorithm(); // ��ȡ�㷨
		KeyFactory keyFact = KeyFactory.getInstance(algorithm);
		BigInteger prime = null;
		if ("RSA".equals(algorithm)) { // �����RSA����
			RSAPublicKeySpec keySpec = (RSAPublicKeySpec)keyFact.getKeySpec(publickey, RSAPublicKeySpec.class);
			prime = keySpec.getModulus();
		} else if ("DSA".equals(algorithm)) { // �����DSA����
			DSAPublicKeySpec keySpec = (DSAPublicKeySpec)keyFact.getKeySpec(publickey, DSAPublicKeySpec.class);
			prime = keySpec.getP();
		}
		int len = prime.toString(2).length(); // ת��Ϊ�����ƣ���ȡ��Կ����
		System.out.println(len);

		if(ret) {
			return;
		}
	}
}
