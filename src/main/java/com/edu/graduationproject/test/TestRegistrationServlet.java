package com.edu.graduationproject.test;



import static org.testng.Assert.assertEquals;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.edu.graduationproject.utils.ExcelUtil;

import io.github.bonigarcia.wdm.WebDriverManager;


public class TestRegistrationServlet {
	String url = "http://localhost:8080/account/signup";
	public WebDriver driver;
	Map<String, Object[]> res;
	int index;

	@BeforeClass
	public void initiateStep() {
		res = new LinkedHashMap<String, Object[]>();
		index = 0;
		res.put("" + index, new Object[] { "Test ID", "Action", "Username","Email","Pass","Repass", "Expected", "Actual", "Result" });
	}

	@AfterClass
	public void finalizeStep() {
		res.entrySet().forEach(entry -> {
			System.out.println(entry.getKey() + " : " + Arrays.toString(entry.getValue()));
		});
		try {
			ExcelUtil.exportTestResultExcel(Paths.get("src","main","resources","test_data", "a.xlsx").toFile(), res);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@BeforeMethod
	public void launchBrowser() throws Exception {
		System.out.println("Launching Chrome browser...");
		WebDriverManager.chromedriver().setup();
		ChromeOptions options = new ChromeOptions();
		driver = new ChromeDriver(options);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get(url);
	}

	@Test(dataProvider = "RegistrationDataProvider")
	public void testRegistration(String username, String email, String password, String repass) throws Exception {
		try {
			System.out.println("testRegistration1 running...");
			// sending test Input data
			driver.findElement(By.name("username")).sendKeys(username);
			System.out.println(username);
			driver.findElement(By.name("email")).sendKeys(email);
			System.out.println(email);
			driver.findElement(By.name("password")).sendKeys(password);
			System.out.println(password);
			driver.findElement(By.name("repass")).sendKeys(repass);
			System.out.println(repass);
			
			// check login result by checking page title
			
			
			driver.findElement(By.xpath("//*[@id=\"termCheck\"]")).click();
			Thread.sleep(1000);
			driver.findElement(By.name("ok")).click();
			
			
			WebElement tt = driver.findElement(By.xpath("/html[1]/body[1]/div[1]/div[1]/main[1]/article[1]/section[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[1]/div[6]/span[1]"));
			Thread.sleep(6000);
			
			String t = "Please check your email to verify your account";
			org.testng.Assert.assertEquals(tt.getText(), t);
			
			
			if(tt.getText().equalsIgnoreCase(t)) {
				index++;
				res.put("" + index, new Object[] { index, "Check sign up",username,email, password,repass, "Signup is sucess",
						"Login Pass", "Passed" });
			}
			else {
				index++;
				res.put("" + index, new Object[] { index, "Check signiup", username,email, password,repass, "Sign up is fail",
						"Sign up is fail", "Fail" });
			}
			
			
				
			
		} catch (Exception e) {
			index++;
			res.put("" + index, new Object[] { index, "Sign up page", username,email, password,repass, "Sigup is succes",
					"Signup fail", "Fail" });
			 org.testng.Assert.assertTrue(false);
		}
	}
	
			
			
			
	

	@DataProvider
	public Object[][] RegistrationDataProvider() throws Exception {
		String excelFilePath = Paths.get("src","main","resources","test_data", "a.xlsx").toFile().getAbsolutePath();
		Object[][] arr = ExcelUtil.getTableArray(excelFilePath, "Sheet1", 4);
		return arr;
	}

	@AfterMethod
	public void terminateBrowser() {
		driver.close();
	}
}
