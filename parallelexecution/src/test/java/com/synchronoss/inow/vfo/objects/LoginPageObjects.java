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
import org.openqa.selenium.support.ui.Select;

/**
 * @author Devaraj Bhat
 *
 */
public class LoginPageObjects {

	public Logger logger = Logger.getLogger(LoginPageObjects.class);

	// User Name Textbox @Devaraj Bhat
	@FindBy(how = How.ID, using = "loginName")
	private WebElement locatorForUserName;

	public WebElement getUserNameTextBox() {
		return this.locatorForUserName;
	}

	public void enterValueInUserNameTextBox(String userName) {

		getUserNameTextBox().sendKeys(userName);
		logger.info("User Name Entered");
	}

	// Password Textbox @Devaraj Bhat
	@FindBy(how = How.ID, using = "password")
	private WebElement locatorForPassword;

	public WebElement getPasswordTextBox() {
		return this.locatorForPassword;
	}

	public void enterValueInPasswordTextBox(String passWord) {

		getPasswordTextBox().sendKeys(passWord);
		logger.info("Password Entered");
	}

	// Module DropDown @Devaraj Bhat
	@FindBy(how = How.NAME, using = "serviceRequestType")
	private WebElement locatorForModuleDropDown;

	public WebElement getModuleDropDown() {
		return this.locatorForModuleDropDown;
	}

	public void selectModule(String moduleName) {

		Select moduleNameWebElement = new Select(getModuleDropDown());
		moduleNameWebElement.selectByVisibleText(moduleName);
		logger.info("Selected module name");
	}

	// Login button @Devaraj Bhat
	@FindBy(how = How.XPATH, using = "//img[@src='images/login.gif']")
	private WebElement locatorForLoginButton;

	public WebElement getLoginButton() {
		return this.locatorForLoginButton;
	}

	public void clickOnLoginButton() {

		getLoginButton().click();
		logger.info("Clicked on login button");
	}
	
	public LoginPageObjects(WebDriver driver) {
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
	}
}
