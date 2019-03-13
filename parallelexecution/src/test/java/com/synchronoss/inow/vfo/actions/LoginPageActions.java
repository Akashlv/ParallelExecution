/**
 * 
 */
package com.synchronoss.inow.vfo.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.synchronoss.inow.common.base.WebDriverActions;
import com.synchronoss.inow.vfo.objects.LoginPageObjects;
import com.synchronoss.inow.vfo.objects.OrderListPageObjects;

/**
 * @author Devaraj Bhat
 *
 */
public class LoginPageActions {

	public Logger logger = Logger.getLogger(LoginPageActions.class);

	public void loginToApplication(WebDriver driver, String url, String userName, String passWord, String moduleName, boolean needToLogin) {
		
		try {
			
			LoginPageObjects loginPgObjects = new LoginPageObjects(driver);
			OrderListPageObjects orderListPgObjects = new OrderListPageObjects(driver);
			WebDriverActions webDriverPgActions = new WebDriverActions();
			
			loginPgObjects.enterValueInUserNameTextBox(userName);
			logger.info("Successfully entered user name as : " + userName);

			loginPgObjects.enterValueInPasswordTextBox(passWord);
			logger.info("Successfully entered password as : " + passWord);

			loginPgObjects.selectModule(moduleName);
			logger.info("Successfully selected module name as : " + moduleName);

			if (needToLogin) {

				loginPgObjects.clickOnLoginButton();
				logger.info("Clicked on login button");
				
				webDriverPgActions.switchToTheCurrentWindow(driver);
				logger.info("Switched to current window");
				
				webDriverPgActions.switchToTopFrame(driver);
				logger.info("Switched to Top frame");

				Assert.assertTrue(orderListPgObjects.getLogoutLink().isDisplayed(), "Failed to login");
				logger.info("Successfully logged in to Application");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
