/**
 * 
 */
package com.synchronoss.inow.vfo.testscripts;

import org.apache.log4j.Logger;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.synchronoss.inow.common.annotation.TestCase;
import com.synchronoss.inow.vfo.actions.LoginPageActions;
import com.synchronoss.inow.vfo.actions.OrderListPageActions;
import com.synchronoss.inow.vfo.base.WebDriverBase;

/**
 * @author Devaraj Bhat
 *
 */

public class VFO_INOW_TEST_001 extends WebDriverBase {

	public Logger logger = Logger.getLogger(VFO_INOW_TEST_001.class);

	LoginPageActions loginPgActions = new LoginPageActions();
	OrderListPageActions OrderListPageActions = new OrderListPageActions();
	
	@Test
	@TestCase(Author = "Devaraj Bhat")
	public void SCRIPT_VFO_INOW_TEST_001() {

		try {
			
			loginPgActions.loginToApplication(driver, vfoURL, autSendUserName, autSendPassWord, moduleNameForASR, true);
			Reporter.log("Successfully logged into the Send Side of Application", true);
	
		} catch (Exception e) {
			
			getscreenshot("VFO_INOW_TEST_001");
			logger.info("Screenshot has been taken for failed test scripts");
			e.printStackTrace();
		}
		
		finally {
			
			OrderListPageActions.logoutFromApplication(driver);
			Reporter.log("Successfully logged out from Application");
		}
	}

}
