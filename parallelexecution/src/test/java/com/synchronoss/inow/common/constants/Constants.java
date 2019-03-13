package com.synchronoss.inow.common.constants;

/**
 * @author Devaraj Bhat
 *
 */
public class Constants {

	// Absolute Path @Devaraj Bhat
	private String currentDirectory = System.getProperty("user.dir");

	public String getAbsolutePath() {
		return this.currentDirectory;
	}

	// Chrome Path @Devaraj Bhat
	private String chromePath = currentDirectory.concat("\\src\\test\\resources\\browserExeFile\\chromedriver.exe");

	public String getChromePath() {
		return this.chromePath;
	}

	// FireFox Path @Devaraj Bhat
	private String fireFoxPath = currentDirectory.concat("\\src\\test\\resources\\browserExeFile\\geckodriver.exe");

	public String getFireFoxPath() {
		return this.fireFoxPath;
	}
	
	// Internet Explorer Path @Devaraj Bhat
	private String internetExplorerPath = currentDirectory.concat("\\src\\test\\resources\\browserExeFile\\IEDriverServer.exe");

	public String getInternetExplorerPath() {
		return this.internetExplorerPath;
	}

	// Excel File Name @Devaraj Bhat
	private String excelFileName = "TestData.xlsx";

	public String getExcelFileName() {
		return this.excelFileName;
	}
	
	// Excel File Sheet Name @Devaraj Bhat
	private String excelSheetNameForLogin = "Login";

	public String getExcelSheetNameForLogin() {
		return this.excelSheetNameForLogin;
	}

	// Excel Path @Devaraj Bhat
	private String testDataPath = currentDirectory.concat("\\src\\test\\resources\\testData\\" + excelFileName);

	public String gettestDataPath() {
		return this.testDataPath;
	}

	// Property File Path @Devaraj Bhat
	private String propertyFilePath = currentDirectory.concat("\\src\\test\\resources\\propertyFiles\\environmentDetails.properties");

	public String getpropertyFilePath() {
		return this.propertyFilePath;
	}
	
	// DB Property File Path @Devaraj Bhat
	private String dbPropertyFilePath = currentDirectory.concat("\\src\\test\\resources\\propertyFiles\\dbDetails.properties");

	public String getDdPropertyFilePath() {
		return this.dbPropertyFilePath;
	}
	
	// ScreenShot Folder Path @Devaraj Bhat
	private String screenShotFolderPath = currentDirectory.concat("\\src\\test\\resources\\screenShot\\");

	public String getscreenShotFolderPath() {
		return this.screenShotFolderPath;
	}

}
