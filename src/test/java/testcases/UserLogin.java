package testcases;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.aventstack.extentreports.MediaEntityBuilder;

import base.BaseTest;
import utilities.Snapshot;
import utilities.ExcelDataReaderWriter;
import utilities.ImageLoad;

public class UserLogin extends BaseTest {
	static String usernameField_id = locatorsProp.getProperty("username_field_id");
	static String passwordField_id = locatorsProp.getProperty("password_field_id");
	static String loginButton_id = locatorsProp.getProperty("login_button_id");
	static String errorMsgContainer_className = locatorsProp.getProperty("errorMessageContainer_className");
	static String productTitle_xpath = locatorsProp.getProperty("productTitle_xpath");
	static String testData_path = locatorsProp.getProperty("testData_path");
	static String screenshotPath, screenShotFileName;

	/*
	 * *****************************************************************************
	 * # Keyword name : LoginTest. # Description : To test login page with multiple
	 * username and password. # Developed by : Soumyajit Pan # Date : 28th Feb, 2023
	 * *****************************************************************************
	 */
	public static void LoginTest() throws IOException {
		LinkedHashSet<LinkedHashMap<String, String>> loginTestData;

		// Read Keyword Data from new XLSX sheet to get dataSet
		mainData_filepath = configProp.getProperty("mainData");
		String filePath = System.getProperty("user.dir") + mainData_filepath;
		loginTestData = ExcelDataReaderWriter.readDataFromExcel(filePath, "LoginTest");

		// System.out.println("loginTestData-------->" + loginTestData);

		for (LinkedHashMap<String, String> dataMap : loginTestData) {
			String testcase = dataMap.get("TestCase");
			String username = dataMap.get("username");
			String userPassword = dataMap.get("password");
			String expected_message = dataMap.get("output");

			if (controllerTestcase.equalsIgnoreCase(testcase)) {
				setUp();

				String usernameField_id = locatorsProp.getProperty("username_field_id");
				String passwordField_id = locatorsProp.getProperty("password_field_id");
				String loginButton_id = locatorsProp.getProperty("login_button_id");
				String errorMsgContainer_className = locatorsProp.getProperty("errorMessageContainer_className");
				String productTitle_xpath = locatorsProp.getProperty("productTitle_xpath");
				String screenshotPath, screenShotFileName;

				// login
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(usernameField_id)));
				WebElement usernameElement = driver.findElement(By.id(usernameField_id));
				usernameElement.clear();
				usernameElement.sendKeys((username == null) ? "" : username);

				WebElement passwordElement = driver.findElement(By.id(passwordField_id));
				passwordElement.clear();
				passwordElement.sendKeys((userPassword == null) ? "" : userPassword);

				driver.findElement(By.id(loginButton_id)).click();
				test.info("Login -> username : " + username + " and password : " + userPassword);

				try {
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(productTitle_xpath)));
					String title = driver.findElement(By.xpath(productTitle_xpath)).getText();
					// wait sometime to load all images in webpage
					ImageLoad.imagesLoaded(driver);
					// take screenshot
					screenShotFileName = Snapshot.getUniqueFileName();
					Snapshot.takeScreenshot(driver, screenShotFileName);
					screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"
							+ screenShotFileName;
					test.info("Took screenshot" + "<br/> File location -> " + screenshotPath);
					if (title.equalsIgnoreCase("Products")) {
						test.pass(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, title + "Home Page ")
								.build());
						test.pass("User successfully loggedin Product home page.");
					} else {
						test.fail(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, title + "Home Page ")
								.build());
						test.fail("User could not loggedin Product home page.");
					}
					Logout();
				} catch (Exception er) {
					wait.until(
							ExpectedConditions.visibilityOfElementLocated(By.className(errorMsgContainer_className)));
					WebElement errorContainer = driver.findElement(By.className(errorMsgContainer_className));
					List<WebElement> errorMessages = errorContainer.findElements(By.tagName("h3"));
					WebElement errorMessage = errorMessages.get(0);
					String messageText = errorMessage.getText();
					test.info("Got error message -> " + messageText);

					screenShotFileName = Snapshot.getUniqueFileName();
					Snapshot.takeScreenshot(driver, screenShotFileName);
					screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"
							+ screenShotFileName;
					test.info("Took screenshot" + "<br/> File location -> " + screenshotPath);
					if (messageText.equalsIgnoreCase(expected_message)) {
						test.pass(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, messageText).build());
						test.pass("Expected error message and actual error message is sucessfully matched.");
					} else {
						System.out.println(messageText);
						System.out.println(expected_message);
						test.fail(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, messageText).build());
						test.fail("Expected error message and actual error message is not matched.");
					}
				}
				tearDown();
			}
		}
	}

	/*
	 * *****************************************************************************
	 * # Keyword name : Login. # Description : To test login page with single
	 * username and password. # Developed by : Soumyajit Pan # Date : 28th Feb, 2023
	 * *****************************************************************************
	 */
	public static void Login() throws IOException {
		LinkedHashSet<LinkedHashMap<String, String>> loginData;

		// Read Keyword Data from new XLSX sheet to get dataSet
		mainData_filepath = configProp.getProperty("mainData");
		String filePath = System.getProperty("user.dir") + mainData_filepath;
		loginData = ExcelDataReaderWriter.readDataFromExcel(filePath, "Login");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(usernameField_id)));

		for (LinkedHashMap<String, String> dataMap : loginData) {
			String testcase = dataMap.get("TestCase");
			String username = dataMap.get("username");
			String userPassword = dataMap.get("password");

			if (controllerTestcase.equalsIgnoreCase(testcase)) {
				WebElement usernameElement = driver.findElement(By.id(usernameField_id));
				usernameElement.clear();
				usernameElement.sendKeys((username == null) ? "" : username);

				WebElement passwordElement = driver.findElement(By.id(passwordField_id));
				passwordElement.clear();
				passwordElement.sendKeys((userPassword == null) ? "" : userPassword);

				driver.findElement(By.id(loginButton_id)).click();
				test.info(
						"Method Name : Login <br/>Login -> username : " + username + " and password : " + userPassword);
				break;
			}
		}
	}

	/*
	 * *****************************************************************************
	 * # Keyword name : validateLogin # Description : To validate the login page
	 * after login # Developed by : Soumyajit Pan # Date : 1st March, 2023
	 * *****************************************************************************
	 */
	public static void validateLogin() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(productTitle_xpath)));
			String title = driver.findElement(By.xpath(productTitle_xpath)).getText();
			// wait sometime to load all images in webpage
			ImageLoad.imagesLoaded(driver);
			// take screenshot
			screenShotFileName = Snapshot.getUniqueFileName();
			Snapshot.takeScreenshot(driver, screenShotFileName);
			screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/" + screenShotFileName;
			test.info("Took screenshot" + "<br/> File location -> " + screenshotPath);
			if (title.equalsIgnoreCase("Products")) {
				test.pass(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, title + "Home Page ").build());
				test.pass("Method Name : validateLogin <br/>User successfully loggedin Product home page.");
			} else {
				test.fail(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, title + "Home Page ").build());
				test.fail("Method Name : validateLogin <br/>User could not loggedin Product home page.");
			}
		} catch (Exception er) {
			test.info(er.getMessage());
			test.fail("Method Name : validateLogin <br/>User could not loggedin Product home page.");
		}
	}

	/*
	 * *****************************************************************************
	 * # Keyword name : Logout # Description : To logout after the test # Developed
	 * by : Soumyajit Pan # Date : 28th Feb, 2023
	 * *****************************************************************************
	 */
	public static void Logout() {
		try {
			String menu_button_id = locatorsProp.getProperty("menu_button_id");
			String logout_id = locatorsProp.getProperty("logout_id");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(menu_button_id)));
			driver.findElement(By.id(menu_button_id)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(logout_id)));
			driver.findElement(By.id(logout_id)).click();
			screenShotFileName = Snapshot.getUniqueFileName();
			Snapshot.takeScreenshot(driver, screenShotFileName);
			screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/" + screenShotFileName;
			test.pass(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, "Page after logged out").build());
			test.pass("Method Name : Logout <br/>User Loggedout successfully!");
		} catch (Exception e) {
			test.info(e.getMessage());
			test.fail("Method Name : Logout <br/>User could not Logout!");
		}
	}
}