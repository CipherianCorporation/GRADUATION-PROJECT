package com.edu.graduationproject.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import dev.failsafe.internal.util.Assert;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.tagext.TryCatchFinally;

import org.apache.hc.core5.util.Asserts;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestCartheroapp {

	WebDriver driver;
	private XSSFWorkbook workbook;
	private XSSFSheet worksheet;
	private Map<String, Object[]> TestNGResult;

	private final String EX_DIR = "C:\\Users\\Admin\\Documents\\GRADUATION-PROJECT\\src\\main\\resources\\test_data\\";

	@BeforeClass
	public void connect() {
		TestNGResult = new LinkedHashMap<String, Object[]>();
		workbook = new XSSFWorkbook();
		worksheet = workbook.createSheet("TestNG");

		TestNGResult.put("1", new Object[] { "Test Step", "Action", "Expected", "Actual", "Result" });
	}

	@Test(priority = 1)
	public void launcher() throws Exception {
		try {
			System.setProperty("webdriver.chrome.driver", "D:\\chome\\chromedriver.exe");
			driver = new ChromeDriver();
			String url = "http://localhost:8080/product/list";
			driver.get(url);
			TestNGResult.put("2", new Object[] { 1d, "Demo Website", "Get open", "Pass", });
		} catch (Exception e) {
			TestNGResult.put("2", new Object[] { 1d, "Demo Website", "Get open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 2)
	public void viewcart() {

		try {
			Thread.sleep(6000);
			WebElement add = driver.findElement(By.xpath(
					"/html/body/div[1]/div/main/article/main/div/div[3]/div[1]/div/div[3]/div[2]/button"));
			add.click();

			Thread.sleep(3000);
			WebElement view = driver
					.findElement(By.xpath("/html/body/div[1]/div/header/nav/div/div[2]/a"));
			view.click();
			Thread.sleep(3000);
			TestNGResult.put("3", new Object[] { 2d, "Add product and view cart ", "View cart", "Pass", });
		} catch (InterruptedException e) {
			TestNGResult.put("3", new Object[] { 2d, "Add product and view cart ", "View cart", "fail", });
			org.testng.Assert.assertTrue(false);
			e.printStackTrace();

		}

	}

	@Test(priority = 4)
	public void clearcart() {
		try {
			Thread.sleep(8000);
			WebElement add = driver.findElement(By.xpath(
					"/html/body/div[1]/div/main/article/main/div/div[3]/div[1]/div/div[3]/div[2]/button"));
			add.click();
			WebElement add2 = driver.findElement(By.xpath(
					"/html/body/div[1]/div/main/article/main/div/div[3]/div[2]/div/div[3]/div[2]/button"));
			add2.click();

			Thread.sleep(3000);
			WebElement view = driver
					.findElement(By.xpath("/html/body/div[1]/div/header/nav/div/div[2]/a"));
			view.click();
			Thread.sleep(2000);
			WebElement clear = driver.findElement(By.xpath("//button[contains(text(),'Xóa hết')]"));
			clear.click();
			Thread.sleep(3000);
			WebElement back = driver.findElement(By.className("text-body"));
			back.click();
			Thread.sleep(3000);
			TestNGResult.put("5",
					new Object[] { 4d, "Add product in the cart and clearall  ", "clear all product", "Pass", });
		} catch (InterruptedException e) {
			TestNGResult.put("5",
					new Object[] { 4d, "Add product in the cart and clearall  ", "clear all product", "fail", });
			org.testng.Assert.assertTrue(false);
			e.printStackTrace();
		}

	}

	@Test(priority = 3)

	public void backtohome() {
		try {
			WebElement back = driver.findElement(By.className("text-body"));
			back.click();
			TestNGResult.put("4", new Object[] { 3d, "Back to home ", "Back home", "Pass", });
		} catch (Exception e) {

			TestNGResult.put("4", new Object[] { 3d, "Back to home ", "Back home", "fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 5)
	public void Quantity() {
		try {
			Thread.sleep(6000);
			WebElement add = driver.findElement(By.xpath(
					"/html/body/div[1]/div/main/article/main/div/div[3]/div[1]/div/div[3]/div[2]/button"));
			add.click();
			WebElement add2 = driver.findElement(By.xpath(
					"/html/body/div[1]/div/main/article/main/div/div[3]/div[2]/div/div[3]/div[2]/button"));
			add2.click();

			Thread.sleep(3000);

			WebElement view = driver
					.findElement(By.xpath("/html/body/div[1]/div/header/nav/div/div[2]/a"));
			view.click();
			Thread.sleep(1000);
			WebElement plus = driver
					.findElement(By.xpath(
							"/html/body/div[1]/div/main/article/main/div/section/div/div/div/div/div/div/div[1]/div/div[2]/div[3]/button[2]"));
			plus.click();
			Thread.sleep(3000);
			WebElement minus = driver
					.findElement(By.xpath(
							"/html/body/div[1]/div/main/article/main/div/section/div/div/div/div/div/div/div[1]/div/div[2]/div[3]/button[1]"));
			minus.click();
			WebElement back = driver.findElement(By.className("text-body"));
			back.click();
			Thread.sleep(3000);
			TestNGResult.put("6",
					new Object[] { 5d, "Add product in the cart and Setup quantity", "Setupquantity", "Pass", });
		} catch (InterruptedException e) {
			TestNGResult.put("6",
					new Object[] { 5d, "Add product in the cart and Setup quantity ", "Setupquantity", "fail", });
			org.testng.Assert.assertTrue(false);
			e.printStackTrace();
		}

	}

	@Test(priority = 6)
	public void order() {
		try {

			Thread.sleep(6000);
			WebElement add = driver.findElement(By.xpath(
					"/html/body/div[1]/div/main/article/main/div/div[3]/div[1]/div/div[3]/div[2]/button"));
			add.click();
			WebElement add2 = driver.findElement(By.xpath(
					"/html/body/div[1]/div/main/article/main/div/div[3]/div[2]/div/div[3]/div[2]/button"));
			add2.click();

			Thread.sleep(3000);
			WebElement view = driver
					.findElement(By.xpath("/html/body/div[1]/div/header/nav/div/div[2]/a"));
			view.click();
			Thread.sleep(2000);
			WebElement order = driver.findElement(By
					.xpath("/html/body/div[1]/div/main/article/main/div/section/div/div/div/div/div/div/div[2]/div/a"));
			order.click();

			WebElement name = driver.findElement(By.name("username"));
			WebElement pass = driver.findElement(By.name("password"));
			WebElement login = driver.findElement(By.name("login"));
			name.sendKeys("thuan");
			pass.sendKeys("123456");
			login.click();
			Thread.sleep(3000);
			TestNGResult.put("7",
					new Object[] { 6d, "Add product in the cart and Order  ", "login And order", "Pass", });
		} catch (InterruptedException e) {
			TestNGResult.put("7",
					new Object[] { 6d, "Add product in the cart and Order  ", "Login and order", "fail", });
			org.testng.Assert.assertTrue(false);
			e.printStackTrace();
		}

	}

	// @Test
	// public void quantity() {
	//
	// }
	//
	// @Test
	// public void pay() {
	//
	// }

	@AfterClass
	public void suiteteardown() {
		Set<String> keyset = TestNGResult.keySet();
		int rownum = 0;
		for (String key : keyset) {
			CellStyle rowstyle = workbook.createCellStyle();
			Row row = worksheet.createRow(rownum++);
			Object[] objarr = TestNGResult.get(key);
			int cellnum = 0;
			for (Object obj : objarr) {
				int flag = cellnum++;
				Cell cell = row.createCell(flag);
				if (obj instanceof Date) {
					cell.setCellValue((Date) obj);
				} else if (obj instanceof Boolean) {
					cell.setCellValue((Boolean) obj);
				} else if (obj instanceof String) {
					cell.setCellValue((String) obj);
				} else if (obj instanceof Double) {
					cell.setCellValue((Double) obj);
				}

			}
			try {
				FileOutputStream out = new FileOutputStream(new File(EX_DIR + "cart_result.xlsx"));
				workbook.write(out);
				out.close();
				System.out.println("Successfully !!");
			} catch (Exception e) {
				System.out.println("suiteteardown() :" + e.getMessage());
				// TODO: handle exception
			}
		}
	}
}
