/**
 * 
 */
package com.synchronoss.inow.common.base;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

/**
 * @author Devaraj Bhat
 *
 */
public class WebDriverActions {

	public Logger logger = Logger.getLogger(WebDriverActions.class);

	// Method to navigate @Devaraj Bhat
	public void navigateURL(WebDriver driver, String url) {

		driver.navigate().to(url);
		logger.info("Navigated to URL : " + url);
	}

	// Method to switch the window @Devaraj Bhat
	public void switchToTheCurrentWindow(WebDriver driver) {

		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
			logger.info("Switched to child widnow");
		}
	}

	// Method to switch to Top frame @Devaraj Bhat
	public void switchToTopFrame(WebDriver driver) {

		driver.switchTo().frame("topFrame");
		logger.info("Switched to top frame");
	}

	// Method to switch to Main frame @Devaraj Bhat
	public void switchToMainFrame(WebDriver driver) {

		driver.switchTo().frame("mainFrame");
		logger.info("Switched to main frame");
	}

}
