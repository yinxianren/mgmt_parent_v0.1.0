package com.rxh.utils;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SicPayXMlUtil {

    public Map getXmlToMap(String xml) {
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isNotBlank(xml)) {
            try {
                Document doc = DocumentHelper.parseText(xml);//将xml转为dom对象
                Element root = doc.getRootElement();//获取根节点
                Element headElement = root.element("head");//获取名称为queryRequest的子节点
                List<Element> headElements = headElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
                for (Object obj : headElements) {  //遍历子元素
                    headElement = (Element) obj;
                    map.put(headElement.getName(), headElement.getTextTrim());//getName
                }
                Element bodyElement = root.element("body");//获取名称为queryRequest的子节点
                List<Element> bodyElements = bodyElement.elements();//获取这个子节点里面的所有子元素，也可以element.elements("userList")指定获取子元素
                for (Object obj : bodyElements) {  //遍历子元素
                    bodyElement = (Element) obj;
                    map.put(bodyElement.getName(), bodyElement.getTextTrim());//getName
                }
                return map;
            } catch (Exception e) {
                e.printStackTrace();
                return map;
            }
        }
        return map;
    }

    public static void main(String[] args) {
        String s = "<?xml version=\"1.0\" encoding=\"utf-8\"?><ipay>\n" +
                "<head>\n" +
                "<version>2.0.0</version>\n" +
                "<agencyId>574034451110001</agencyId>\n" +
                "<msgType>02</msgType>\n" +
                "<tranCode>100001</tranCode>\n" +
                "<reqMsgId>12311321323131</reqMsgId>\n" +
                "<payMsgId>201906201416269794</payMsgId>\n" +
                "<reqDate>20190620141557</reqDate>\n" +
                "<respDate>20190620141626</respDate>\n" +
                "<respType>S</respType>\n" +
                "<respCode>000000</respCode>\n" +
                "<respMsg>成功</respMsg>\n" +
                "</head>\n" +
                "<body>\n" +
                "<merchantId>00003784</merchantId>\n" +
                "<merchantName>通联</merchantName>\n" +
                "</body>\n" +
                "</ipay>";
        SicPayXMlUtil sicPayXMlUtil = new SicPayXMlUtil();
        sicPayXMlUtil.getXmlToMap(s);
    }
}
