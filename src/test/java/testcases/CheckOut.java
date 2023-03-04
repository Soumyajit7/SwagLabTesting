package testcases;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.aventstack.extentreports.MediaEntityBuilder;

import utilities.ExcelDataReaderWriter;
import utilities.Snapshot;

public class CheckOut extends Products {
	public static String checkoutBtn_id = locatorsProp.getProperty("checkoutBtn_id");
	public static String checkout_title_xpath = locatorsProp.getProperty("checkout_title_xpath");
	public static String checkout_continue_id = locatorsProp.getProperty("checkout_continue_id");
	public static String checkout_overview_title_xpath = locatorsProp.getProperty("checkout_overview_title_xpath");
	public static String summary_subtotal_classname = locatorsProp.getProperty("summary_subtotal_label_classname");
	public static String summary_tax_classname = locatorsProp.getProperty("summary_tax_label_classname");
	public static String total_cost_classname = locatorsProp.getProperty("total_cost_classname");
	public static String checkout_finish_button_id = locatorsProp.getProperty("checkout_finish_button_id");
	public static String checkout_finish_xpath = locatorsProp.getProperty("checkout_finish_xpath");
	public static String thankyou_title_xpath = locatorsProp.getProperty("thankyou_title_xpath");
	public static String checkout_complete_msg_xpath = locatorsProp.getProperty("checkout_complete_msg_xpath");
	public static String screenShotFileName, screenshotPath;

	/*
	 * *****************************************************************************
	 * # Keyword name : navigateToCheckOut # Description : Navigate to checkout page
	 * # Developed by : Soumyajit Pan # Date : 2nd March, 2023
	 * *****************************************************************************
	 */
	public static void navigateToCheckOut() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(checkoutBtn_id)));
			driver.findElement(By.id(checkoutBtn_id)).click();
			test.pass("Method Name : navigateToCheckOut <br/>Navigating to Checkout Page.");
		} catch (Exception e) {
			test.fail(
					"Method Name : navigateToCheckOut <br/>Could not navigate to Checkout Page.<br/>" + e.getMessage());
		}
	}

	/*
	 * *****************************************************************************
	 * # Keyword name : validateCheckOut # Description : Validate the page is
	 * checkout or not # Developed by : Soumyajit Pan # Date : 2nd March, 2023
	 * *****************************************************************************
	 */
	public static void validateCheckOut() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(checkout_title_xpath)));

			if (driver.findElement(By.xpath(checkout_title_xpath)).getText()
					.equalsIgnoreCase("Checkout: Your Information")) {
				test.pass("Method Name : validateCheckOut <br/>Successfully navigated to Checkout page.");

				screenShotFileName = Snapshot.getUniqueFileName();
				Snapshot.takeScreenshot(driver, screenShotFileName);
				screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"
						+ screenShotFileName;
				test.info("Took screenshot" + "<br/> File location -> " + screenshotPath);
				test.pass(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, "Your Cart Page").build());

			} else {
				test.fail("Method Name : validateCheckOut <br/>Could not navigated to Checkout page.");
			}
		} catch (Exception e) {
			test.fail("Method Name : validateCheckOut <br/>" + e.getMessage());
		}
	}

	/*
	 * *****************************************************************************
	 * # Keyword name : checkoutFormTest # Description : Test the checkot form using
	 * multple user data # Developed by : Soumyajit Pan # Date : 3rd March, 2023
	 * *****************************************************************************
	 */
	public static void checkoutFormTest() throws IOException {
		try {
			LinkedHashSet<LinkedHashMap<String, String>> checkoutFormTestData;

			// Read Keyword Data from new XLSX sheet to get dataSet
			mainData_filepath = configProp.getProperty("mainData");
			String filePath = System.getProperty("user.dir") + mainData_filepath;
			checkoutFormTestData = ExcelDataReaderWriter.readDataFromExcel(filePath, "checkoutFormTest");

			for (LinkedHashMap<String, String> dataMap : checkoutFormTestData) {
				String testcase = (dataMap.get("TestCase") == null) ? "" : dataMap.get("TestCase");
				String firstname = (dataMap.get("FirstName") == null) ? "" : dataMap.get("FirstName");
				String lastname = (dataMap.get("LastName") == null) ? "" : dataMap.get("LastName");
				String zipcode = (dataMap.get("ZipCode") == null) ? "" : dataMap.get("ZipCode");
				String message = (dataMap.get("Message") == null) ? "" : dataMap.get("Message");

				if (controllerTestcase.equalsIgnoreCase(testcase)) {
					driver.navigate().refresh();
					String firstname_id = locatorsProp.getProperty("firstname_id");
					String lastname_id = locatorsProp.getProperty("lastname_id");
					String zipcode_id = locatorsProp.getProperty("zipcode_id");
					String errormsg_xpath = locatorsProp.getProperty("checkout_errormsg_xpath");

					driver.findElement(By.id(firstname_id)).clear();
					driver.findElement(By.id(firstname_id)).sendKeys(firstname);
					driver.findElement(By.id(lastname_id)).clear();
					driver.findElement(By.id(lastname_id)).sendKeys(lastname);
					driver.findElement(By.id(zipcode_id)).clear();
					driver.findElement(By.id(zipcode_id)).sendKeys(zipcode);

					// screeenshot
					screenShotFileName = Snapshot.getUniqueFileName();
					Snapshot.takeScreenshot(driver, screenShotFileName);
					screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"
							+ screenShotFileName;
					test.info("Took screenshot" + "<br/> File location -> " + screenshotPath);
					test.pass(MediaEntityBuilder
							.createScreenCaptureFromPath(screenshotPath, "Filled data into the checkout form").build());

					driver.findElement(By.id(checkout_continue_id)).click();

					try {
						String checkout_error_msg = driver.findElement(By.xpath(errormsg_xpath)).getText();
						// screeenshot
						screenShotFileName = Snapshot.getUniqueFileName();
						Snapshot.takeScreenshot(driver, screenShotFileName);
						screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"
								+ screenShotFileName;
						test.info("Took screenshot" + "<br/> File location -> " + screenshotPath);
						test.pass(MediaEntityBuilder
								.createScreenCaptureFromPath(screenshotPath, "Checkout Form with error message")
								.build());

						if (checkout_error_msg.equalsIgnoreCase(message)) {
							test.info("Method Name : checkoutFormTest <br/>Got an error message : " + checkout_error_msg
									+ "<br/>Expected error message : " + message);
							test.pass("Actual error message and expected error message is matched.");
						} else {
							test.info("Method Name : checkoutFormTest <br/>Got an error message : " + checkout_error_msg
									+ "<br/>Expected error message : " + message);
							test.fail("Actual error message and expected error message is not matched.");
						}
					} catch (Exception e) {
						// screeenshot
						screenShotFileName = Snapshot.getUniqueFileName();
						Snapshot.takeScreenshot(driver, screenShotFileName);
						screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"
								+ screenShotFileName;
						test.info("Took screenshot" + "<br/> File location -> " + screenshotPath);
						test.pass(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath,
								"After successfull Checkout with no error message").build());

						test.pass("Method Name : checkoutFormTest <br/>No error message found!");
					}
				}
			}
		} catch (Exception e) {
			test.fail("Method Name : checkoutFormTest <br/>" + e.getMessage());
		}
	}

	/*
	 * *****************************************************************************
	 * # Keyword name : validateCheckoutData # Description : Verify the product name
	 * and cost after checkout # Developed by : Soumyajit Pan # Date : 3rd March,
	 * 2023
	 * *****************************************************************************
	 */
	public static void validateCheckoutData() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(checkout_overview_title_xpath)));
			if (driver.findElement(By.xpath(checkout_overview_title_xpath)).getText()
					.equalsIgnoreCase("Checkout: Overview")) {
				test.pass(
						"Method Name : validateCheckoutData <br/>Successfully navigated into Checkout Overview Page.");

				List<WebElement> itemsName = driver.findElements(By.className(products_name_className));
				List<WebElement> itemsPrice = driver.findElements(By.className(products_price_className));
				LinkedHashMap<String, Float> itemsNamePriceLists = new LinkedHashMap<String, Float>();
				Float expectedTotalCost = (float) 0;

				// To make the table into the report
				StringBuilder tableBuilder = new StringBuilder();
				tableBuilder.append("<table>");
				tableBuilder.append("<tr><th>Item</th><th>Price</th></tr>");

				for (int i = 0; i < itemsName.size(); ++i) {
					String itemName = itemsName.get(i).getText();
					String itemPrice = itemsPrice.get(i).getText();

					tableBuilder.append("<tr><td>").append(itemName).append("</td><td>").append(itemPrice)
							.append("</td></tr>");

					Float itemPriceFloat = Float.parseFloat(itemPrice.replaceAll("[^\\d.]", ""));
					expectedTotalCost += itemPriceFloat;
					itemsNamePriceLists.put(itemName, itemPriceFloat);
				}

				// sub total
				String subTotal = driver.findElement(By.className(summary_subtotal_classname)).getText();
				String[] parts = subTotal.split(":");
				tableBuilder.append("<tr><td><b>").append(parts[0]).append("</b></td><td><b>").append(parts[1])
						.append("</b></td></tr>");

				// sub tax
				String subTax = driver.findElement(By.className(summary_tax_classname)).getText();
				parts = subTax.split(":");
				Float taxPrice = Float.parseFloat(parts[1].trim().substring(1));
				expectedTotalCost += taxPrice;
				tableBuilder.append("<tr><td><b>").append(parts[0]).append("</b></td><td><b>").append(parts[1])
						.append("</b></td></tr>");

				// grand total price
				String toalCost = driver.findElement(By.className(total_cost_classname)).getText();
				parts = toalCost.split(":");
				Float actualTotalCost = Float.parseFloat(parts[1].trim().substring(1));
				tableBuilder.append("<tr><td><b>").append(parts[0]).append("</b></td><td><b>").append(parts[1])
						.append("</b></td></tr>");

				tableBuilder.append("</table>");
				String tableHtml = tableBuilder.toString();
				test.info(tableHtml);

				// scroll to the total cost element to take screenshot
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].scrollIntoView(true);",
						driver.findElement(By.className(total_cost_classname)));

				// screeenshot
				screenShotFileName = Snapshot.getUniqueFileName();
				Snapshot.takeScreenshot(driver, screenShotFileName);
				screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"
						+ screenShotFileName;
				test.info("Took screenshot" + "<br/> File location -> " + screenshotPath);
				test.pass(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, "Total cost in chekcout")
						.build());

				test.info(
						"Actual total cost : $" + actualTotalCost + "<br/>Expected total cost : $" + expectedTotalCost);
				if (expectedTotalCost.equals(actualTotalCost)) {
					test.pass(
							"Method Name : validateCheckoutData <br/>Actual total cost and expected total cost is successfully matched!");
				} else {
					test.fail(
							"Method Name : validateCheckoutData <br/>Actual total cost and expected total cost is not matched!");
				}

			} else {
				// screeenshot
				screenShotFileName = Snapshot.getUniqueFileName();
				Snapshot.takeScreenshot(driver, screenShotFileName);
				screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"
						+ screenShotFileName;
				test.info("Took screenshot" + "<br/> File location -> " + screenshotPath);
				test.fail(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath,
						"After successfull navigate into Checkout Overview Page").build());

				test.fail("Method Name : validateCheckoutData <br/>Could not navigate into Checkout Overview Page.");
			}
		} catch (Exception e) {
			test.fail("Method Name : validateCheckoutData <br/>" + e.getMessage());
		}
	}

	/*
	 * *****************************************************************************
	 * # Keyword name : checkoutFinish # Description : Click on finish and verify
	 * the messages # Developed by : Soumyajit Pan # Date : 3rd March, 2023
	 * *****************************************************************************
	 */
	public static void checkoutFinish() {
		try {
			driver.findElement(By.id(checkout_finish_button_id)).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(checkout_finish_xpath)));
			if (driver.findElement(By.xpath(checkout_finish_xpath)).getText().equalsIgnoreCase("Checkout: Complete!")) {
				test.pass("Method Name : checkoutFinish <br/>Successfully navigated into the checkout complete page.");

				String thankyou_msg = driver.findElement(By.xpath(thankyou_title_xpath)).getText();
				String checkoutComplete_msg = driver.findElement(By.xpath(checkout_complete_msg_xpath)).getText();

				test.info("Actual title message : " + thankyou_msg
						+ "<br/>Expected title message : Thank you for your order!");

				test.info("Actual checkout message : " + checkoutComplete_msg
						+ "<br/>Expected checkout message : Your order has been dispatched, and will arrive just as fast as the pony can get there!");

				if (thankyou_msg.equalsIgnoreCase("Thank you for your order!") && checkoutComplete_msg.equalsIgnoreCase(
						"Your order has been dispatched, and will arrive just as fast as the pony can get there!")) {

					// screeenshot
					screenShotFileName = Snapshot.getUniqueFileName();
					Snapshot.takeScreenshot(driver, screenShotFileName);
					screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"
							+ screenShotFileName;
					test.info("Took screenshot" + "<br/> File location -> " + screenshotPath);
					test.pass(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, "Checkout Complete")
							.build());

					test.pass("Method Name : checkoutFinish <br/>Check successfully completed!");
				} else {
					test.fail("Method Name : checkoutFinish <br/>Checkout message is not displayed!");
				}
			} else {
				test.fail("Method Name : checkoutFinish <br/>Checkout is incomplete!");
			}
		} catch (Exception e) {
			test.fail("Method Name : checkoutFinish <br/>" + e.getMessage());
		}
	}

}
