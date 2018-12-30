package com.tide.ailab.common.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * XML格式化处理类
 * @author User
 * @version
 */
public class XmlFormatter {
    public static String format(String xml) {
        try {
            Document document = DocumentHelper.parseText(xml);
            OutputFormat formater = OutputFormat.createPrettyPrint();
            formater.setIndentSize(4);
            Writer out = new StringWriter();
            XMLWriter writer = new XMLWriter(out, formater);
            writer.write(document);

            return out.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {
        // 未格式化前的xml
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><PARAM><DBID>35</DBID><SEQUENCE>atgtca</SEQUENCE><MAXNS>10</MAXNS><MINIDENTITIES>90</MINIDENTITIES><MAXEVALUE>10</MAXEVALUE><USERNAME>admin</USERNAME><PASSWORD>111111</PASSWORD><TYPE>P</TYPE><RETURN_TYPE>2</RETURN_TYPE></PARAM>";
        System.out.println(XmlFormatter.format(s));
    }
}
