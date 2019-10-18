package com.rxh.utils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.record.Country;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * 
 * 功能描述：GeoIp2 获取 ip所在国家
 * 
 * @author : 庄梦蝶殇 2015-5-6
 */
public class GeoIP2Utils {
    /**
     * 日志记录
     */
    private final static Logger logger = LoggerFactory.getLogger(GeoIP2Utils.class);
    
    private static DatabaseReader reader;
    static {
    }
    
    /**
     * 
     * 获取 ip所在国家的ISO编码
     * 
     * @param ip ip地址
     * @return ip所在国家的ISO编码
     */
    public static String getIsoCountry(String ip) {
        Country country = getCountry(ip);
        return null == country ? "" : country.getIsoCode();
    }
    
    /**
     * 
     * 获取 ip所在国家对象
     * 
     * @param ip ip地址
     * @return ip所在国家对象
     */
    private static Country getCountry(String ip) {
        return response(ip).getCountry();
    }
    
    /**
     * 
     * 获取 geoip2返回信息
     * 
     * @param ip ip地址
     * @return geoip2返回信息(getCountry-ip所在国家、getContinent-ip所在大陆、getRegisteredCountry-ip注册国家、getRepresentedCountry-ip代表国家、getTraits-ip特性: isp、用户类型、自治系统号...)
     */
    public static CountryResponse response(String ip) {
        CountryResponse response = null;
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            response = reader.country(ipAddress);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return response;
    }
    
    /**
     * 
     * 重现加载数据库文件
     * 
     * @param name GeoIp2国家数据库文件(mmdb格式)
     */
    public static void reload(String name) {
        File database = new File(name);
        try {
            reader = new DatabaseReader.Builder(database).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
