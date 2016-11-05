package Login;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;

public class testLoginWithJAVA implements Runnable{
	
	static String emailField = "//*[@id='email-field']";
	static String passwordField = "//*[@id='password-field']";
	static String signInBtn = "//*[@id='signin-form']/input[4]";
	static String errorNotification = "//*[@id='notification-container']/div";
	static String forgotPwdLink = "//*[@id='signin-form']/a";
	static String closeBtn = "//*[@id='modal-header']/button";
	static String loginURL = "https://bookshelf.vitalsource.com/#/user/signin";
	static String userName = "smurphy.qa@ingramcontent.com";
	static String passwrd =  "Ingramqa!1";
	static String logo = "//*[@id='logo']/a/img";
	static String firstBook = "//*[@id='library-card-9781515402183-0']";
	static WebDriver Net;
	
	public testLoginWithJAVA(String browser) {
		if(browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver","C:/Users/Latha/Desktop/Java/geckodriver-v0.11.1-win64/geckodriver.exe");
			Net = new FirefoxDriver();
		}
		if(browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "C:/Users/Latha/Desktop/Java/chromedriver_win32/chromedriver.exe");
			Net = new ChromeDriver();
		}
		Net.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Net.manage().window().maximize();
	}
	
	public static void main(String[] args) {
		String[] browser = {"chrome","firefox","ie"};
		ExecutorService loop = Executors.newFixedThreadPool(20);
		for(int k=0; k<3; k++) {
			loop.execute(new testLoginWithJAVA(browser[k]));
			System.out.println("Loop: " + k);
		}
		loop.shutdown();
		
	}
	
	public void run() {
		testValidLogin();
	}
	
	public WebElement Element(String xPath,WebDriver driver) {
		WebElement element = driver.findElement(By.xpath(xPath));
		return element;
	}
	public void testValidLogin() {
		Element(emailField,Net).clear();
		Element(emailField,Net).sendKeys(userName);
		Element(passwordField,Net).clear();
		Element(passwordField,Net).sendKeys(passwrd);
		Element(signInBtn,Net).click();
		Element(closeBtn,Net).click();
		try {
			Assert.assertTrue(Element(logo,Net).isDisplayed());
			Assert.assertTrue(Element(firstBook,Net).isDisplayed());
		} catch(AssertionError e){
			e.printStackTrace();
		}
	}
	
	
}
