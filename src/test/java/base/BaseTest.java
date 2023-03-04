package base;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;
import utilities.ExcelDataReaderWriter;

public class BaseTest {

	public static WebDriver driver;
	public static WebDriverWait wait = null;
	public static Properties configProp = new Properties();
	public static Properties locatorsProp = new Properties();
	public static FileReader configFile_reader = null;
	public static FileReader locatorFile_reader = null;
	public static ExtentSparkReporter htmlReporter;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static LinkedHashSet<LinkedHashMap<String, String>> dataSet;
	public static LinkedHashSet<LinkedHashMap<String, String>> regDataSet;
	public static String regressionData_filepath, mainData_filepath;
	public static String controllerTestcase = "", testCase = "";
	static String reportPath = System.getProperty("user.dir") + "/src/test/resources/repots/SwagLabTestReport.html";

	/*
	 * *****************************************************************************
	 * # Method Name : setUpReport # Description : Setup the extent report #
	 * Developed by : Soumyajit Pan # Date : 28th Feb, 2023
	 * *****************************************************************************
	 */
	@BeforeTest
	public void setUpReport() throws IOException {
		String configFileName = "src/test/resources/configfiles/config.properties";
		String locatorsFileName = "src/test/resources/configfiles/locatotrs.properties";

		configFile_reader = new FileReader(configFileName);
		locatorFile_reader = new FileReader(locatorsFileName);
		configProp.load(configFile_reader);
		locatorsProp.load(locatorFile_reader);

		// Make Report
		String userName = configProp.getProperty("username");
		htmlReporter = new ExtentSparkReporter(reportPath);
		htmlReporter.config().setDocumentTitle("Extent Report Demo");
		htmlReporter.config().setReportName("Test Report");
		htmlReporter.config().setTheme(Theme.STANDARD);
		htmlReporter.config().setEncoding("utf-8");
		htmlReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");

		extent = new ExtentReports();
		extent.attachReporter(htmlReporter);
		extent.setSystemInfo("OS", "Windows 10");
		extent.setSystemInfo("Host Name", "localhost");
		extent.setSystemInfo("Environment", "QA");
		extent.setSystemInfo("UserName", userName);
	}

	/*
	 * *****************************************************************************
	 * # Method Name : setUp # Description : Setup the browser # Developed by :
	 * Soumyajit Pan # Date : 28th Feb, 2023
	 * *****************************************************************************
	 */
	public static void setUp() throws IOException {
		String browser = configProp.getProperty("browser");
		String url = configProp.getProperty("url");

		int waitTime = Integer.parseInt(configProp.getProperty("wait"));

		if (browser.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firfox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
		} else {
			test.log(Status.FAIL, browser + " browser is not available!");
		}

		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
		driver.get(url);
	}

	/*
	 * *****************************************************************************
	 * # Method Name : tearDown # Description : Close the browser # Developed by :
	 * Soumyajit Pan # Date : 28th Feb, 2023
	 * *****************************************************************************
	 */
	public static void tearDown() throws IOException {
		driver.close();
		driver.quit();
		Status status = test.getStatus();
		String filepath = System.getProperty("user.dir") + regressionData_filepath;
		ExcelDataReaderWriter.excelDataInWriteBySearch("TestCase", "Status", testCase, status.toString(), filepath,
				"Regreesion");
		ExcelDataReaderWriter.excelDataInWriteBySearch("TestCase", "TestReport Path", testCase, reportPath, filepath,
				"Regreesion");
	}

	/*
	 * *****************************************************************************
	 * # Method Name : exportReport # Description : Export the extent report #
	 * Developed by : Soumyajit Pan # Date : 28th Feb, 2023
	 * *****************************************************************************
	 */
	@AfterTest
	public void exportReport() {
		// System.out.println("Report Created!");
		extent.flush();
	}

}
