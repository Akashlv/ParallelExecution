/**
 * 
 */
package com.synchronoss.inow.vfo.actions;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import com.synchronoss.inow.common.base.WebDriverActions;
import com.synchronoss.inow.vfo.objects.OrderListPageObjects;

/**
 * @author Devaraj Bhat
 *
 */
public class OrderListPageActions {

	public Logger logger = Logger.getLogger(OrderListPageActions.class);

	// Method to logout from the application @Devaraj Bhat
	public void logoutFromApplication(WebDriver driver) {

		OrderListPageObjects orderListPgObjects = new OrderListPageObjects(driver);
		WebDriverActions webDriverPgActions = new WebDriverActions();

		webDriverPgActions.switchToTheCurrentWindow(driver);
		logger.info("Successfully Switched to current window");

		webDriverPgActions.switchToTopFrame(driver);
		logger.info("Successfully Switched to top frame");

		orderListPgObjects.clickOnLogoutLink();
		logger.info("Successfully clicked on logout button");
	}

}
