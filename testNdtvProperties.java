package mpack;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.opentelemetry.exporter.logging.SystemOutLogExporter;

public class testNdtvProperties {
	WebDriver driver;
	public static String browserName = null;

	static Properties prop = new Properties();
	static String projectPath = System.getProperty("user.dir");

	@BeforeClass
	public static void getProperties() {
		try {

			InputStream input = new FileInputStream(projectPath + "/src/test/java/config/config.properties");
			prop.load(input);
			testNdtvProperties.browserName = prop.getProperty("browser_name");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getCause());
			System.out.println(e.getStackTrace());
		}
	}

	@Test(priority = 1)
	public void testNdtvPage() {
		// WebDriverManager.chromedriver().driverVersion("85.0.4183.38").setup();
//		System.out.println(browser);

		if (browserName.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-notifications");
			driver = new ChromeDriver(options);
		} else if (browserName.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", projectPath + "/drivers/geckodriver.exe");
			FirefoxOptions options = new FirefoxOptions();
			options.setProfile(new FirefoxProfile());
			options.addPreference("dom.webnotifications.enabled", false);

			driver = new FirefoxDriver(options);
		}

		driver.get(prop.getProperty("website_link"));

		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		String actual = driver.findElement(By.linkText(prop.getProperty("liveTv_linktext"))).getText();
		System.out.println(actual);
		Assert.assertEquals(actual, "LIVE TV");

	}

	@Test(priority = 2)
	public void testWeatherPage() {
		driver.findElement(By.xpath(prop.getProperty("way_to_weather_tab"))).click();
		driver.findElement(By.xpath(prop.getProperty("weather_tab"))).click();
		Assert.assertTrue(driver.findElement(By.cssSelector(prop.getProperty("weather_page"))).isDisplayed());

	}

	@Test(priority = 3)
	public void testWeather() {

		String cities[] = prop.getProperty("cities").toString().split("#");
		for (int i = 0; i < cities.length; i++) {
			String celcius_temp = "//*[@title='" + cities[i] + "']//span[@class='tempRedText']";
			String fahrenheit_temp = "//*[@title='" + cities[i] + "']//span[@class='tempWhiteText']";

			WebElement checkbox = driver.findElement(By.id(cities[i]));
			if (!checkbox.isSelected()) {
				checkbox.click();

			}
			System.out.println(
					"Celcius Temperature in " + cities[i] + " " + driver.findElement(By.xpath(celcius_temp)).getText());
			System.out.println("Fahrenheit Temperature in " +cities[i] + " "
					+ driver.findElement(By.xpath(fahrenheit_temp)).getText());

		}

	}
}
