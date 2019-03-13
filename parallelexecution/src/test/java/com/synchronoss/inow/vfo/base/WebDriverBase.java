/**
 * 
 */
package com.synchronoss.inow.vfo.base;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import com.synchronoss.inow.common.constants.Constants;
import com.synchronoss.inow.common.constants.PropertyFile;
import com.synchronoss.inow.common.util.ExcelOperationHandling;

public class WebDriverBase {

	public Logger logger = Logger.getLogger(WebDriverBase.class);

	protected PropertyFile propertyFile = new PropertyFile();

	public WebDriver driver;

	public Properties appPropertyFile;
	public String vfoURL;
	public String autSendUserName;
	public String autSendPassWord;
	public String moduleNameForASR;
	public String autRcvUserName;
	public String autRcvPassWord;
	public String lsrModule;
	public String autSendLsrUserName;
	public String autSendLsrPassWord;
	public String autRcvLsrUserName;
	public String autRcvLsrPassWord;

	Constants constantValues = new Constants();
	ExcelOperationHandling excelOperation = new ExcelOperationHandling();
	
	int row;
	
	// Method to fetch return excel data sheet row number for the matching test case
	public int getclassName(String excelDataWorkBookName, String userStory) {

		try {
			
			excelOperation.excelInitialize(excelDataWorkBookName, userStory);
			Thread.sleep(3000);
			String sTestCaseName = this.toString();
			row = excelOperation.getRowContains(sTestCaseName, 0);

			System.out.println("ROW :" + row);

		} catch (IOException e) {
			e.printStackTrace();
			Reporter.log("unable to intialize excecl file" + e.getMessage());
			logger.info("unable to intialize excecl file" + e.getMessage());

		} catch (InterruptedException e) {
			e.printStackTrace();
			logger.info("Exception for thread sleep" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception while reading from excel: " + e.getMessage());
		}

		return row;
	}

	public void loadProperties() throws IOException {

		appPropertyFile = propertyFile.readProperFile(constantValues.getpropertyFilePath());
		logger.info("Property files are loaded sucessfully");

		vfoURL = appPropertyFile.getProperty("vfoUrl");
		logger.info("URL is : " + vfoURL);

		autSendUserName = appPropertyFile.getProperty("autSendUserName");
		logger.info("User name is : " + autSendUserName);

		autSendPassWord = appPropertyFile.getProperty("autSendPassWord");
		logger.info("Password is : " + autSendPassWord);

		moduleNameForASR = appPropertyFile.getProperty("asrModule");
		logger.info("Module is : " + moduleNameForASR);

		autRcvUserName = appPropertyFile.getProperty("autRcvUserName");
		logger.info("User name is : " + autRcvUserName);

		autRcvPassWord = appPropertyFile.getProperty("autRcvPassWord");
		logger.info("Password is : " + autRcvPassWord);
	}

	// Method to initiate driver @Devaraj Bhat
	@SuppressWarnings("deprecation")
	@Parameters({"browserName"})
	@BeforeClass
	public void initTest(String browserName) throws IOException {

		loadProperties();
		logger.info("Loading Properties");

		DesiredCapabilities capabilities;

		if (browserName.equalsIgnoreCase("Chrome")) {

			System.setProperty("webdriver.chrome.driver", constantValues.getChromePath());

			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-infobars");
			driver = new ChromeDriver(options);
		}

		else if (browserName.equalsIgnoreCase("FF") || browserName.equalsIgnoreCase("FireFox")) {

			System.setProperty("webdriver.gecko.driver", constantValues.getFireFoxPath());
			capabilities = DesiredCapabilities.firefox();
			capabilities.setCapability("marionette", true);
			driver = new FirefoxDriver();
		}
		
		else if (browserName.equalsIgnoreCase("IE") || browserName.equalsIgnoreCase("Internet Explorer")) {

			System.setProperty("webdriver.ie.driver", constantValues.getInternetExplorerPath());
			capabilities = new DesiredCapabilities();

			capabilities.setCapability("browser", "IE");
			capabilities.setCapability("os", "Windows");
			capabilities.setCapability("os_version", "7");
			capabilities.setCapability("nativeEvents", true);
			capabilities.setCapability("ie.ensureCleanSession", true);
			capabilities.setCapability("acceptSslCerts", "true");
			capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
			capabilities.setCapability("browserstack.ie.enablePopups", "false");
			capabilities.setCapability("ignoreZoomSetting", true);
			capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			capabilities.setJavascriptEnabled(true);
			capabilities.setCapability("enablePersistentHover", false);

			DesiredCapabilities.internetExplorer().setCapability("ignoreProtectedModeSettings", true);
			
			driver = new InternetExplorerDriver(capabilities);
			Reporter.log("Launched IE browser", true);
		}
		
		else {
			
			Reporter.log("Browser value is not valid");
		}

		driver.get(vfoURL);
		Reporter.log("Opening Browser", true);
		driver.manage().window().maximize();
		Reporter.log("Maximizing Browser Window", true);
		driver.manage().timeouts().implicitlyWait(35, TimeUnit.SECONDS);
	}

	// Method to close the browser @Devaraj Bhat
	@AfterClass
	public void quitBrowser() {

		driver.quit();
		logger.info("Quit the browser");
		Reporter.log("Quit the browser");
	}
	
	// Method to take the screenshot @Devaraj Bhat
	public void getscreenshot(String testCaseNumber) {

		try {

			String testDataFile = constantValues.getscreenShotFolderPath();
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(testDataFile + testCaseNumber + ".png"));

		} catch (Exception e) {
			Reporter.log(e.getMessage());
		}
	}
}
