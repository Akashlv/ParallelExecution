/**
 * 
 */
package com.synchronoss.inow.vfo.objects;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

/**
 * @author Devaraj Bhat
 *
 */
public class OrderListPageObjects {

	public Logger logger = Logger.getLogger(OrderListPageObjects.class);

	// Logout Link @Devaraj Bhat
	@FindBy(how = How.XPATH, using = "//map/area[4]")
	private WebElement locatorForLogoutLink;

	public WebElement getLogoutLink() {
		return this.locatorForLogoutLink;
	}

	public void clickOnLogoutLink() {
		getLogoutLink().click();
		logger.info("Clicked on logout link");
	}

	public OrderListPageObjects(WebDriver driver) {
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
	}

}
