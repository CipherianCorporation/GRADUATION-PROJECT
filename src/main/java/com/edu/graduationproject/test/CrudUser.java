package com.edu.graduationproject.test;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.edu.graduationproject.utils.ExcelUtil;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CrudUser {

	String url = "http://localhost:8080/admin/home/index";
	public WebDriver driver;
	Map<String, Object[]> res;
	int index;

	// @Test(priority = 1)
	// public void a() {
	// try {
	//
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	//
	//
	//
	//
	// }

	@BeforeClass
	public void initiateStep() {
		res = new LinkedHashMap<String, Object[]>();
		index = 0;
		res.put("" + index, new Object[] { "Test ID", "Action", "Username", "Email", "Pass", "Repass", "Expected",
				"Actual", "Result" });
	}

	@AfterClass
	public void finalizeStep() {
		res.entrySet().forEach(entry -> {
			System.out.println(entry.getKey() + " : " + Arrays.toString(entry.getValue()));
		});
		try {
			ExcelUtil.exportTestResultExcel(Paths.get("src", "main", "resources", "test_data", "a.xlsx").toFile(), res);
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

	@Test(priority = 2, dataProvider = "RegistrationDataProvider")
	public void testRegistration(String username, String password, String fullname, String email, String phone,
			String address, String roll) throws Exception {
		try {

			driver.findElement(By.name("username")).sendKeys("batman");

			driver.findElement(By.name("password")).sendKeys("123123");

			driver.findElement(By.name("login")).click();

			Thread.sleep(3000);
			driver.findElement(By.xpath("//*[@id=\"sidebarMenu\"]/div/div/a[3]")).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath("//a[@id='ex1-tab-1']")).click();
			Thread.sleep(4000);

			System.out.println("testRegistration1 running...");
			// sending test Input data
			driver.findElement(By.name("username")).sendKeys(username);
			System.out.println(username);
			Thread.sleep(1000);
			driver.findElement(By.name("password")).sendKeys(password);
			System.out.println(password);
			Thread.sleep(1000);
			driver.findElement(By.name("fullname")).sendKeys(fullname);
			System.out.println(password);
			Thread.sleep(1000);
			driver.findElement(By.name("email")).sendKeys(email);
			System.out.println(email);
			Thread.sleep(1000);
			driver.findElement(By.name("phone")).sendKeys(phone);
			System.out.println(phone);
			Thread.sleep(1000);
			driver.findElement(By.name("address")).sendKeys(address);
			System.out.println(address);
			Thread.sleep(3000);
			Select role = new Select(driver.findElement(
					By.xpath("//body/main[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[9]/select[1]")));
			role.selectByVisibleText(roll);

			Thread.sleep(2000);

			driver.findElement(By.xpath("//button[contains(text(),'Tạo mới')]")).click();

			Thread.sleep(5000);
			driver.findElement(By.xpath("//button[contains(text(),'Tạo mới')]")).click();

			Thread.sleep(10000);

			index++;
			res.put("" + index,
					new Object[] { index, "Create User ", username, email, password, fullname, "Create is sucess",
							"Create is sucess", "Passed" });

		} catch (Exception e) {
			index++;
			res.put("" + index,
					new Object[] { index, "Create User ", username, email, password, fullname, "Create is sucess",
							"Create is fail", "Fail" });
			org.testng.Assert.assertTrue(false);
		}
	}

	@DataProvider
	public Object[][] RegistrationDataProvider() throws Exception {
		String excelFilePath = Paths.get("src", "main", "resources", "test_data", "a.xlsx").toFile().getAbsolutePath();
		Object[][] arr = ExcelUtil.getTableArray(excelFilePath, "Sheet2", 7);
		return arr;
	}

	@AfterMethod
	public void terminateBrowser() {
		driver.close();
	}
}
