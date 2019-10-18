package com.rxh.util;

import com.rxh.pojo.Excel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/4/2
 * Time: 9:53
 * Project: Management
 * Package: com.rxh.utils
 */
public class ExcelUtils {
    private static final String SUFFIX_CSV = ".csv";
    private static final String SUFFIX_XLS = ".xls";
    private static final String SUFFIX_XLSX = ".xlsx";

    // private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    public static Excel getWorkbooks(MultipartFile multipartFile) throws IOException {
        byte[]  bt=multipartFile.getBytes();
        ByteArrayInputStream bis= new ByteArrayInputStream(bt,0,bt.length);
        if (multipartFile.getOriginalFilename().endsWith(SUFFIX_XLS)) {
            return new Excel(multipartFile.getOriginalFilename(), new HSSFWorkbook(bis));
        } else if (multipartFile.getOriginalFilename().endsWith(SUFFIX_XLSX)) {
            return new Excel(multipartFile.getOriginalFilename(), new XSSFWorkbook(bis));
        } else if (multipartFile.getOriginalFilename().endsWith(SUFFIX_CSV)) {
            Reader reader = new InputStreamReader(new BOMInputStream(bis), UTF_8);
            return new Excel(multipartFile.getOriginalFilename(), new CSVParser(reader, CSVFormat.DEFAULT));
        }
        return null;
    }




    /**
     * @Description:将一个类的所有声明属性和值存入map（属性值为int型时，0时不加入，
     * 属性值为String型或Long时为null和“”不加入）
     * @Date:2018/4/12
     * @return 属性名和值对应的map
     */
    public static Map<String, Object> setConditionMap(Object obj){
        Map<String, Object> map = new HashMap<String, Object>();
        if(obj==null){
            return null;
        }
        try {
            //获取该类的所有声明的属性的名字
            Field[] fields = obj.getClass().getDeclaredFields();
            //循环遍历以属性名作为key以boject的get方法获取的值作为value存入map
            for(Field field : fields){
                String fieldName =  field.getName();
                if(getValueByFieldName(fieldName,obj)!=null)
                    map.put(fieldName,  getValueByFieldName(fieldName,obj));
            }
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }
        return map;
    }
    /**
     * 根据属性名获取该类此属性的值
     * @param fieldName
     * @param object
     * @return get方法的返回值
     */
    private static Object getValueByFieldName(String fieldName,Object object){
        //拼接get方法
        String firstLetter=fieldName.substring(0,1).toUpperCase();
        String getter = "get"+firstLetter+fieldName.substring(1);
        try {
            //从实体类中获取该get方法
            Method method = object.getClass().getMethod(getter, new Class[]{});
            //利用该实体类对象执行该get方法
            Object value = method.invoke(object, new Object[] {});
            //如果是时间格式化
            if(value.getClass().equals(Date.class)){
                Date date =  new Date(((Date)value).getTime());
                SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return time.format(date);
            }
            return value;
        } catch (Exception e) {
            return null;
        }
    }

    /**成功打印出excel表格的方法
     * @Description: 根据数据集合以及要生成的表名和存储路径在该路径下生成数据集合导出的excel文件
     * @param: list 要导出的数据集合
     * @param: sheetName 要生成的excel sheet的名字集合
     * @param: path 要保存的文件路径
     * @Date:2019/4/12
     * */


}