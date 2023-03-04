package utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.io.FileHandler;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Snapshot {
	public static void takeScreenshot(WebDriver driver, String fileName) {
		TakesScreenshot screenshot = (TakesScreenshot) driver;

		File srcFile = screenshot.getScreenshotAs(OutputType.FILE);

		String destPath = "./src/test/resources/screenshots/" + fileName;
		File destFile = new File(destPath);

		try {
			FileHandler.copy(srcFile, destFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getUniqueFileName() {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
	    Date now = new Date();
	    String fileName = "screenshot_" + dateFormat.format(now) + ".png";
	    return fileName;
	}
}
