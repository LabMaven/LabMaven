package com.tide.ailab.common.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * @author User
 */
public class XmlUtil {

	public static void writeConfigFile(String path, String fileName, Document document) {
		try {
			OutputFormat formater = OutputFormat.createPrettyPrint();
			formater.setIndentSize(4);
			FileOutputStream file = new FileOutputStream(path + "/" + fileName);
			XMLWriter xmlWriter = new XMLWriter(file, formater);
			xmlWriter.write(document);

			xmlWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
