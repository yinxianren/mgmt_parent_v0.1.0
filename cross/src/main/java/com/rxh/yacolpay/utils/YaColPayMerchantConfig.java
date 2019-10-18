package com.rxh.yacolpay.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

public class YaColPayMerchantConfig {
	public static final String ConfigFileName = "merchant.properties";
	private String partner_id;
	private String mgsRequestUrl;
	private String masRequestUrl;
	private String pasRequestUrl;
	private String publicKey;
	private String publicCheckKey;
	private String privateKeyRSA;
	private String privateKeyMD5;
	private static YaColPayMerchantConfig config;
	private Properties properties;

	public static YaColPayMerchantConfig getConfig() {
		if (null == config) {
			config = new YaColPayMerchantConfig();
		}
		return config;
	}

	public void loadPropertiesFromPath(String rootPath) {
		File file = new File(rootPath + File.separator + "merchant.properties");

		InputStream in = null;
		if (file.exists()) {
			try {
				in = new FileInputStream(file);
				this.properties = new Properties();
				this.properties.load(in);
				loadProperties(this.properties);

				if (null != in) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

				}

				if (null != in)
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			} catch (FileNotFoundException e) {
				e.printStackTrace();

				if (null != in) {
					try {
						in.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}

				if (null != in)
					try {
						in.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
			} catch (IOException e) {
				e.printStackTrace();

				if (null != in) {
					try {
						in.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				if (null != in)
					try {
						in.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
			} finally {
				if (null != in)
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		} else
			System.out.println(rootPath + "merchant.properties" + "不存在,加载参数失败");
	}

	public void loadPropertiesFromSrc() {
		InputStream in = null;
		try {
			in = YaColPayMerchantConfig.class.getClassLoader().getResourceAsStream("merchant.properties");

			if (null != in) {
				this.properties = new Properties();
				try {
					this.properties.load(in);
				} catch (IOException e) {
					throw e;
				}
			}
			loadProperties(this.properties);

			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			if (null != in)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		} catch (IOException e) {
			e.printStackTrace();

			if (null != in) {
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}

			if (null != in)
				try {
					in.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
		} finally {
			if (null != in)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public void loadProperties(Properties pro) {
		String value = pro.getProperty("mgsRequestUrl");
		if (!StringUtils.isBlank(value)) {
			this.mgsRequestUrl = value.trim();
		}
		value = pro.getProperty("partner_id");
		if (!StringUtils.isBlank(value)) {
			this.partner_id = value.trim();
		}
		value = pro.getProperty("masRequestUrl");
		if (!StringUtils.isBlank(value)) {
			this.masRequestUrl = value.trim();
		}
		value = pro.getProperty("pasRequestUrl");
		if (!StringUtils.isBlank(value)) {
			this.pasRequestUrl = value.trim();
		}
		value = pro.getProperty("publicKey");
		if (!StringUtils.isBlank(value)) {
			this.publicKey = value.trim();
		}
		value = pro.getProperty("publicCheckKey");
		if (!StringUtils.isBlank(value)) {
			this.publicCheckKey = value.trim();
		}
		value = pro.getProperty("privateKeyRSA");
		if (!StringUtils.isBlank(value)) {
			this.privateKeyRSA = value.trim();
		}
		value = pro.getProperty("privateKeyMD5");
		if (!StringUtils.isBlank(value)) {
			this.privateKeyMD5 = value.trim();
		}
	}
	
	public String getMgsRequestUrl() {
		return this.mgsRequestUrl;
	}
	
	public String getPartner_id() {
		return this.partner_id;
	}

	public void setMgsRequestUrl(String mgsRequestUrl) {
		this.mgsRequestUrl = mgsRequestUrl;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}
	
	public String getMasRequestUrl() {
		return this.masRequestUrl;
	}

	public void setMasRequestUrl(String masRequestUrl) {
		this.masRequestUrl = masRequestUrl;
	}

	public String getPasRequestUrl() {
		return this.pasRequestUrl;
	}

	public void setPasRequestUrl(String pasRequestUrl) {
		this.pasRequestUrl = pasRequestUrl;
	}

	public String getPublicKey() {
		return this.publicKey;
	}

	public String getPublicCheckKey() {
		return this.publicCheckKey;
	}
	
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public void setPublicCheckKey(String publicCheckKey) {
		this.publicCheckKey = publicCheckKey;
	}
	
	public String getPrivateKeyRSA() {
		return this.privateKeyRSA;
	}

	public void setPrivateKeyRSA(String privateKeyRSA) {
		this.privateKeyRSA = privateKeyRSA;
	}

	public String getPrivateKeyMD5() {
		return this.privateKeyMD5;
	}

	public void setPrivateKeyMD5(String privateKeyMD5) {
		this.privateKeyMD5 = privateKeyMD5;
	}

	public Properties getProperties() {
		return this.properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public static void main(String[] args) {
	}
}