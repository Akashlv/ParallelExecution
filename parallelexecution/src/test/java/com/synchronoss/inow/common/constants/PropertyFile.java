package com.synchronoss.inow.common.constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class PropertyFile {

	Logger logger = Logger.getLogger(PropertyFile.class.getName());

	protected Constants constantsValue = new Constants();

	// To Load the Property files values @Devaraj Bhat
	public Properties readProperFile(String relativePath) throws IOException {

		System.out.println("Property file path is : " + relativePath);
		File file = new File(relativePath);
		FileInputStream prpoertyFileHandler = new FileInputStream(file);
		Properties properties = new Properties();
		properties.load(prpoertyFileHandler);

		return properties;
	}

}
