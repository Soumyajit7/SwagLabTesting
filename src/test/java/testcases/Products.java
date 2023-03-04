package testcases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.MediaEntityBuilder;

import utilities.Snapshot;

public class Products extends UserLogin {
	public static String products_name_className = locatorsProp.getProperty("products_name_className");
	public static String products_price_className = locatorsProp.getProperty("products_price_className");
	public static String backToProduct_id = locatorsProp.getProperty("backToProduct_id");
	public static String productImg_className = locatorsProp.getProperty("productImg_className");
	public static String productName_xpath = locatorsProp.getProperty("productName_xpath");
	public static String filter_field_xpath = locatorsProp.getProperty("filter_field_xpath");
	public static String shopping_cart_link_classname = locatorsProp.getProperty("shopping_cart_link_classname");
	public static String cart_item_classname = locatorsProp.getProperty("cart_item_classname");
	public static String continue_shopping_id = locatorsProp.getProperty("continue_shopping_id");
	public static String your_cart_xpath = locatorsProp.getProperty("your_cart_xpath");
	public static String add_to_cart_xpath = locatorsProp.getProperty("add_to_cart_xpath");
	public static String screenShotFileName, screenshotPath;

	/*
	 * *****************************************************************************
	 * # Keyword name : checkAllProducts # Description : Open each product and take
	 * screenshot # Developed by : Soumyajit Pan # Date : 1st March, 2023
	 * *****************************************************************************
	 */
	public static void checkAllProducts() throws IOException {
		try {
			List<WebElement> productsList = driver.findElements(By.className(products_name_className));
			int numberOfProducts = productsList.size();
			test.info("Method Name : checkAllProducts <br/>Total products present in the page -> " + numberOfProducts);
			ArrayList<String> productsName = new ArrayList<String>();
			for (int i = 0; i < numberOfProducts; ++i) {
				productsName.add(productsList.get(i).getText());
			}
			System.out.println(productsName);

			for (String product : productsName) {
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(product)));
				driver.findElement(By.linkText(product)).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(productImg_className)));
				String productName = driver.findElement(By.xpath(productName_xpath)).getText();

				screenShotFileName = Snapshot.getUniqueFileName();
				Snapshot.takeScreenshot(driver, screenShotFileName);
				screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"
						+ screenShotFileName;
				test.info("Took screenshot" + "<br/> File location -> " + screenshotPath);
				test.pass(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, productName).build());

				driver.findElement(By.id(backToProduct_id)).click();
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(product)));
			}
			test.pass("Method Name : checkAllProducts <br/>All product items are successfully working.");
		} catch (Exception e) {
			System.out.println();
			System.out.println(e.getMessage());
			System.out.println();
			test.fail("Method Name : checkAllProducts <br/>" + e.getMessage());
		}
	}

	/*
	 * *****************************************************************************
	 * # Keyword name : checkFilters # Description : Test the sort the products by
	 * filter operation # Developed by : Soumyajit Pan # Date : 2nd March, 2023
	 * *****************************************************************************
	 */
	public static void checkFilters() {
		try {
			List<WebElement> productsList = driver.findElements(By.className(products_name_className));
			WebElement filterField = driver.findElement(By.xpath(filter_field_xpath));
			Select filterSelect = new Select(filterField);
			List<WebElement> options = filterSelect.getOptions();
			test.info("Method Name : checkFilters <br/>Total options present in the filter -> " + options.size());

			for (int i = 0; i < options.size(); ++i) {
				filterField = driver.findElement(By.xpath(filter_field_xpath));
				filterSelect = new Select(filterField);
				options = filterSelect.getOptions();

				String filterOption = options.get(i).getText();
				filterSelect.selectByIndex(i);

				screenShotFileName = Snapshot.getUniqueFileName();
				Snapshot.takeScreenshot(driver, screenShotFileName);
				screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"
						+ screenShotFileName;
				test.info("Took screenshot" + "<br/> File location -> " + screenshotPath);
				test.pass(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, filterOption).build());

				if (filterOption.startsWith("Name")) {
					productsList = driver.findElements(By.className(products_name_className));
				} else {
					productsList = driver.findElements(By.className(products_price_className));
				}

				test.info("Filter applied : " + filterOption);
				ArrayList<String> productsName = new ArrayList<String>();
				for (int j = 0; j < productsList.size(); ++j) {
					productsName.add(productsList.get(j).getText());
				}
				test.info("Products : " + productsName);

				boolean isSorted = isSorted(productsName, filterOption);
				if (isSorted) {
					test.pass("Products are sorted as per the filter successfully!");
				} else {
					test.fail("Products are not sorted as per the filter!");
				}
			}
		} catch (Exception e) {
			test.fail(e.getMessage());
		}
	}

	// To check Arraylist is sort or not
	public static boolean isSorted(ArrayList<String> products, String filter) {
		if (filter.equalsIgnoreCase("Name (A to Z)")) {
			for (int i = 0; i < products.size() - 1; i++) {
				if (products.get(i).compareTo(products.get(i + 1)) > 0) {
					return false;
				}
			}
		} else if (filter.equalsIgnoreCase("Name (Z to A)")) {
			for (int i = 0; i < products.size() - 1; i++) {
				if (products.get(i).compareTo(products.get(i + 1)) < 0) {
					return false;
				}
			}
		} else if (filter.equalsIgnoreCase("Price (low to high)")) {
			ArrayList<Float> floatList = new ArrayList<Float>();
			for (String str : products) {
				floatList.add(Float.parseFloat(str.replaceAll("[^\\d.]", "")));
			}
			for (int i = 0; i < floatList.size() - 1; i++) {
				if (floatList.get(i).compareTo(floatList.get(i + 1)) > 0) {
					return false;
				}
			}
		} else if (filter.equalsIgnoreCase("Price (high to low)")) {
			ArrayList<Float> floatList = new ArrayList<Float>();
			for (String str : products) {
				floatList.add(Float.parseFloat(str.replaceAll("[^\\d.]", "")));
			}
			for (int i = 0; i < floatList.size() - 1; i++) {
				if (floatList.get(i).compareTo(floatList.get(i + 1)) < 0) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * *****************************************************************************
	 * # Keyword name : addCartProducts # Description : Shopping all the products in
	 * the product home page # Developed by : Soumyajit Pan # Date : 2nd March, 2023
	 * *****************************************************************************
	 */
	public static void addCartProducts() {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(products_name_className)));
			List<WebElement> productsList = driver.findElements(By.className(products_name_className));
			// counted the number of products in product page
			int numberOfProducts = productsList.size();
			test.info("Method Name : addCartProducts <br/>Total number of products present in the product page : "
					+ numberOfProducts);

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(shopping_cart_link_classname)));
			WebElement shoppingCart = driver.findElement(By.className(shopping_cart_link_classname));
			// going on shopping cart page
			shoppingCart.click();

			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(your_cart_xpath)));
			// check your cart page or not
			if (driver.findElement(By.xpath(your_cart_xpath)).getText().equals("Your Cart")) {
				test.pass("Successfully navigated into Your Cart Page.");

				screenShotFileName = Snapshot.getUniqueFileName();
				Snapshot.takeScreenshot(driver, screenShotFileName);
				screenshotPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"
						+ screenShotFileName;
				test.info("Took screenshot" + "<br/> File location -> " + screenshotPath);
				test.pass(MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath, "Your Cart Page").build());

				// Store the items which is already present in yourcart page
				List<WebElement> cartItems = driver.findElements(By.className(products_name_className));

				if (cartItems.size() == 0) {
					// click on continue shopping button
					driver.findElement(By.id(continue_shopping_id)).click();
					wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(add_to_cart_xpath)));
					test.info("No items present in your cart page so navigate to continue shopping");

					// add to cart
					List<WebElement> addToCartItems = driver.findElements(By.xpath(add_to_cart_xpath));
					int count = 0;
					for (int i = 0; i < addToCartItems.size(); ++i) {
						wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(add_to_cart_xpath)));
						addToCartItems.get(i).click();
						count += 1;
					}

					// again navigate to your cart page
					wait.until(
							ExpectedConditions.visibilityOfElementLocated(By.className(shopping_cart_link_classname)));
					shoppingCart = driver.findElement(By.className(shopping_cart_link_classname));
					shoppingCart.click();
					test.info("Number of total added to cart product items : " + count);
				}
				wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(cart_item_classname)));
				cartItems = driver.findElements(By.className(cart_item_classname));

				if (cartItems.size() == numberOfProducts) {
					test.pass("Method Name : addCartProducts <br/>Successfully added to cart all the products.");
				} else {
					test.fail("Method Name : addCartProducts <br/>Could not added to cart all product items!");
				}
			} else {
				test.fail("Method Name : addCartProducts <br/>Could not navigate into Your Cart Page.");
			}
		} catch (Exception e) {
			System.out.println("Method Name : addCartProducts <br/>" + e.getMessage());
		}

	}

}
