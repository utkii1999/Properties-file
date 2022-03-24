package mpack;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class youtube {
	public static void main(String[] args)
	{
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.youtube.com/");
		driver.manage().window().maximize();
		driver.findElement(By.xpath("//input[@id='search']")).sendKeys("pip");
		driver.findElement(By.id("search-icon-legacy")).click();
		driver.manage().timeouts().implicitlyWait(4, TimeUnit.SECONDS);
		driver.findElement(By.xpath("//yt-formatted-string[contains(text(),'Guide Dogs')]")).click();
		String time = driver.findElement(By.xpath("//span[@aria-label='4 minutes, 5 seconds']")).getText();
		int f_time=Integer.valueOf(time);
		int cur_time=0;
		while(cur_time!=f_time)
		{
			cur_time=Integer.parseInt(driver.findElement(By.xpath("//span[@class='ytp-time-current']")).getText());
			System.out.println(cur_time);
		}
	
	    

	}

}
