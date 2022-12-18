package com.edu.graduationproject.test;

import static org.testng.Assert.assertEquals;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.edu.graduationproject.utils.ExcelUtil;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Testlogin {
	String url = "http://localhost:8080/security/login/form";
	public WebDriver driver;
	Map<String, Object[]> res;
	int index;

	@BeforeClass
	public void initiateStep() {
		res = new LinkedHashMap<String, Object[]>();
		index = 0;
		res.put("" + index, new Object[] { "Test ID", "Action", "UserName", "Pass", "Expected", "Actual", "Result" });
	}

	@AfterClass
	public void finalizeStep() {
		res.entrySet().forEach(entry -> {
			System.out.println(entry.getKey() + " : " + Arrays.toString(entry.getValue()));
		});
		try {
			ExcelUtil.exportTestResultExcel(
					Paths.get("src", "main", "resources", "test_data", "LoginData.xlsx").toFile(), res);
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

	// @Test(dataProvider = "loginDataProvider")
	// public void testLogin1(String username, String password) {
	// try {
	// System.out.println("testLogin1 running...");
	// // sending test Input data
	// driver.findElement(By.name("username")).sendKeys(username);
	// System.out.println(username);
	// driver.findElement(By.name("password")).sendKeys(password);
	// System.out.println(password);
	// driver.findElement(By.name("login")).click();
	// // check login result by checking page title
	// String expectedTitle = "Shoe Store";
	// String actualTitle = driver.getTitle();
	// assertEquals(expectedTitle, actualTitle);
	//
	//
	// boolean result = expectedTitle.equals(actualTitle);
	// index++;
	// res.put("" + index, new Object[] { index, "Verify login page",
	// String.format(username, password), expectedTitle,
	// actualTitle, result ? "Passed" : "Fail" });
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	@Test(priority = 1)
	public void f() {
		try {
			driver.get(url);
			driver.manage().window().maximize();
			index++;
			res.put("", new Object[] { index, "WebDemo", "", "", "", "Pass" });

		} catch (Exception e) {
			res.put("", new Object[] { index, "WebDemo", "", "", "", "fail" });

			org.testng.Assert.assertTrue(false);
		}
	}

	@Test(priority = 2)
	public void testtitle() {
		try {

			// check login result by checking page title
			String expectedTitle = "SHOESHY";
			String actualTitle = driver.getTitle();
			assertEquals(expectedTitle, actualTitle);
			boolean result = expectedTitle.equals(actualTitle);
			index++;
			res.put("" + index, new Object[] { index, "Title Page", "", expectedTitle,
					actualTitle, result ? "Passed" : "Fail" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test(priority = 3,dataProvider = "loginDataProvider")
	// public void testLogin2(String username, String password) {
	// try {
	// System.out.println("testLogin1 running...");
	// // sending test Input data
	// driver.findElement(By.name("username")).sendKeys(username);
	// System.out.println(username);
	// driver.findElement(By.name("password")).sendKeys(password);
	// System.out.println(password);
	// driver.findElement(By.name("login")).click();
	// // check login result by checking page title
	// String expectedTitle = "Shoe Store";
	// String actualTitle = driver.getTitle();
	// assertEquals(username, password);
	// boolean result = username.equals(password);
	// index++;
	// res.put("" + index, new Object[] { index, "Verify login page",
	// String.format(username, password), expectedTitle,
	// actualTitle, result ? "Passed" : "Fail" });
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	@Test(priority = 3, dataProvider = "loginDataProvider")
	public void testLogin2(String username, String password) {
		try {
			System.out.println("testLogin1 running...");
			// sending test Input data
			driver.findElement(By.name("username")).sendKeys(username);
			System.out.println(username);
			driver.findElement(By.name("password")).sendKeys(password);
			System.out.println(password);
			driver.findElement(By.name("login")).click();
			// check login result by checking page title

			WebElement tt = driver.findElement(By.xpath(
					"/html[1]/body[1]/div[1]/div[1]/header[1]/nav[1]/div[1]/div[2]/div[1]/a[1]/span[1]/span[1]"));
			Thread.sleep(4000);

			// WebElement tt =
			// driver.findElement(By.xpath("/html[1]/body[1]/div[1]/main[1]/article[1]/section[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[1]/div[4]/span[1]"));
			// Thread.sleep(4000);
			// org.testng.Assert.assertEquals(tt.getText(), "Sai thông tin hoặc tài khoản
			// chưa được kích hoạt!");
			//

			if (tt.getText().equalsIgnoreCase(username)) {
				index++;
				res.put("" + index, new Object[] { index, "Verify login page", username, password, "login is Succes",
						"Login Pass", "Passed" });
			} else {
				index++;
				res.put("" + index, new Object[] { index, "Verify login page", username, password, "Login is Succes",
						"Login Fail", "Fail" });
			}

			org.testng.Assert.assertEquals(tt.getText(), username);

		} catch (Exception e) {
			index++;
			res.put("" + index, new Object[] { index, "Verify login page", username, password, "Login is Sussces",
					"Login Fail", "Fail" });
			org.testng.Assert.assertTrue(false);
		}
	}

	@DataProvider
	public Object[][] loginDataProvider() throws Exception {
		String excelFilePath = Paths.get("src", "main", "resources", "test_data", "LoginData.xlsx").toFile()
				.getAbsolutePath();
		Object[][] arr = ExcelUtil.getTableArray(excelFilePath, "Sheet2", 2);
		return arr;
	}

	@AfterMethod
	public void terminateBrowser() {
		driver.close();
	}
}
