package utilities;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ImageLoad {

	public static void imagesLoaded(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("img")));
		wait.until(
				ExpectedConditions.and(ExpectedConditions.jsReturnsValue("return document.readyState == 'complete';"),
						ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("img[src]"), 0)));

	}
}
