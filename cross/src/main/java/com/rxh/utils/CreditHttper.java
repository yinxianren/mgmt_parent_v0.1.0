package com.rxh.utils;

import com.rxh.pojo.cross.StreamBuffer;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.ListIterator;


/**
 * http���շ����ߣ�UTF-8����
 *
 * @author xie
 */
public class CreditHttper {

    private static Logger logger = Logger.getLogger(CreditHttper.class.getName());


    public static String read(HttpServletRequest request) throws Exception {
        return new String(streamReaderReadHttp(request.getInputStream()), "UTF-8");
    }

    private static byte[] streamReaderReadHttp(InputStream in) throws IOException {
        LinkedList<StreamBuffer> bufList = new LinkedList<StreamBuffer>();
        int size = 0;
        byte[] buf;

        do {
            buf = new byte[128];
            int num = in.read(buf);
            if (num == -1) {
                break;
            }
            size += num;
            bufList.add(new StreamBuffer(buf, num));
        } while (true);

        buf = new byte[size];
        int pos = 0;
        for (ListIterator<StreamBuffer> p = bufList.listIterator(); p.hasNext(); ) {

            StreamBuffer b = p.next();
            for (int i = 0; i < b.getSize(); ) {
                buf[pos] = b.getBuffer()[i];
                i++;
                pos++;
            }

        }
        return buf;
    }
}
