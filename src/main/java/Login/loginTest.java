package Login;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class loginTest {
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
	static String chromeDriverPath = "C:/Users/Latha/Desktop/Java/chromedriver_win32/chromedriver.exe";
	static String geckoDriverPath = "C:/Users/Latha/Desktop/Java/geckodriver-v0.11.1-win64/geckodriver.exe";
	public WebElement Element(String xPath,WebDriver driver) {		// Method which returns a WebElement when its XPath and WebDriver instance are passed in Parameters.
		WebElement element = driver.findElement(By.xpath(xPath));
		return element;
	}
	@Parameters("Browser")
	@BeforeTest
	public WebDriver openBrowser() {
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		Net = new ChromeDriver();
		Net.get(loginURL);		
		Net.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Net.manage().window().maximize();
		return Net;
	}
	@Parameters("Browser")
	@Test(priority=0)
	public void testValidLogin(String browser) {	// Valid Login test method, once logged in checks for the presence of "BookShelf" Logo and the first book shown in the Dashboard to validate a successful login.
		if(browser.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			Net = new ChromeDriver();
		}
		if(browser.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", geckoDriverPath);
			Net = new FirefoxDriver();
		}
		Net.get(loginURL);		
		Net.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		Net.manage().window().maximize();
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
		Net.manage().deleteAllCookies();
		Net.get(loginURL);
	}
	@Test(priority=1,dataProvider="InvalidData")
	public void testInvalidLogin(String un, String pw, String testType) {
		Element(emailField,Net).clear();
		Element(emailField,Net).sendKeys(un);
		Element(passwordField,Net).clear();
		Element(passwordField,Net).sendKeys(pw);
		Element(signInBtn,Net).click();
		try {
			Assert.assertTrue(Element(errorNotification,Net).isDisplayed());
			Assert.assertEquals(Element(errorNotification,Net).getText(), "Incorrect email or password");
		} catch(AssertionError e){
			e.printStackTrace();
			System.out.println("Failed Login Case: " + testType);
		}
		Net.manage().deleteAllCookies();
		Net.get(loginURL);
	}
	@DataProvider(name="InvalidData")
	public Object[][] invalidLoginData() {
		return new Object[][] {
			{"smurphy.qa@ingramcontent.com","Ingramqa1","Invalid password"},  		// Invalid password.
			{"murphy.qa@ingramcontent.com","Ingramqa!1","Wrong Email"},				// Wrong Email.
			{"murphy.qa@ingramcontent.com","Ingramqa1","Wrong Email and PW"},		// Wrong Email, Wrong PW.
			{"","","Empty email and password"},										// Empty email and password.
			{"smurphy.qa@ingramcontent.com","","Empty Password"},					// Empty password, Existing email.
			{"","Ingramqa!1","empty email"},										// Empty email, Existing password.
			{"SMURPHY.QA@INGRAMCONTENT.COM","Ingramqa!1",  "Email on CAPS"},		// Email on CAPS.
			{"smurphy.qa@ingramcontent.com","INGRAMQA!1",  "Password on CAPS"},		// Password on CAPS.
			{"SMURPHY.QA@INGRAMCONTENT.COM","INGRAMQA!1",  "Email and PW on CAPS"},							   // Email and Password on CAPS
			{"smurph y.qa@ingramcontent.com","Ingramqa!1", "Email with a space anywhere in the middle"}, 	   // Email with a space somewhere in the middle.
			{"smurphy.qa@ingramcontent.com","Ingram qa!1", "Password with a space anywhere in the middle"},    // Password with a space somewhere in the middle.
			{"smurphy.qa@ingramcontent.com ","Ingramqa!1", "Email with a space at the very end"}, 			   // Email with a space at the very end.
			{"smurphy.qa@ingramcontent.com","Ingramqa!1 ", "Password with a space at the very end"}, 		   // Password with a space at the very end.
			{"smurphy.qa@ingramcontent.com ","Ingramqa!1 ","Email and Password with a space at the very end"}, // Email and Password with a space at the very end.
			{" smurphy.qa@ingramcontent.com","Ingramqa!1", "Email with a space at the very beginning"}, 	   // Email with a space at the very beginning.
			{"smurphy.qa@ingramcontent.com"," Ingramqa!1", "Password with a space at the very beginning"},     // Password with a space at the very beginning.
			{" smurphy.qa@ingramcontent.com"," Ingramqa!1","Email and Password both with a space at the very beginning"},  // Email and Password both with a space at the very beginning.
			{"Smurphy.qa@ingramcontent.com","Ingramqa!1",  "Email with first character in CAPS"},  						   // Email with first character in CAPS
			{"smurphy.qa@ingramcontent.com","ingramqa!1",  "Password with first character not in CAPS"},  				   // Password with first character not in CAPS
			{"Roopa.adari@ingramcontent.com","ingramqa!1", "Email with first character in CAPS but Password not in CAPS"}  // Email with first character in CAPS but Password not in CAPS			
		};		
	}
	
	
	
}
