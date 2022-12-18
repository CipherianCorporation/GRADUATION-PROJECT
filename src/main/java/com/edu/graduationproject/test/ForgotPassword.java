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

public class ForgotPassword {

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
			String url = "http://localhost:8080/account/forgotpassword";
			driver.get(url);
			TestNGResult.put("2", new Object[] { 1d, "Demo Website", "Get open", "open", "Pass", });
		} catch (Exception e) {
			TestNGResult.put("2", new Object[] { 1d, "Demo Website", "Get open", "not open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 2)
	public void Wrongmail() {

		try {
			WebElement email = driver.findElement(By.name("email"));

			WebElement login = driver.findElement(
					By.xpath("/html/body/div[1]/main/article/section/div/div/div/div/div/form/div[2]/button"));

			email.sendKeys("thuanle2425@");

			login.click();

			Thread.sleep(1000);
			WebElement tt = driver.findElement(
					By.xpath("/html/body/div[1]/main/article/section/div/div/div/div/div/form/div[3]/span"));
			Thread.sleep(2000);
			org.testng.Assert.assertEquals(tt.getText(), "Tài khoản của bạn không tồn tại");
			TestNGResult.put("3",
					new Object[] { 2d, "Input wrong mail and check message", "Wrong mail", "wrong mail", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("3",
					new Object[] { 2d, "Input wrong mail and check message", "Wrong mail", "have email", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 3)
	public void truemail() {

		try {
			WebElement email = driver.findElement(By.name("email"));

			WebElement login = driver.findElement(
					By.xpath("/html/body/div[1]/main/article/section/div/div/div/div/div/form/div[2]/button"));

			email.sendKeys("thuanle2425@gmail.com");

			login.click();

			Thread.sleep(1000);
			WebElement tt = driver.findElement(
					By.xpath("/html/body/div[1]/main/article/section/div/div/div/div/div/form/div[3]/span"));
			Thread.sleep(2000);
			org.testng.Assert.assertEquals(tt.getText(),
					"Chúng tôi đã gửi liên kết để đặt lại đến email cảu bạn. Vui lòng kiểm tra!!!");
			TestNGResult.put("4",
					new Object[] { 2d, "Input True mail and check message", "True", "check mail", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("4",
					new Object[] { 2d, "Input True mail and check message", "True mail", "wrong mail", "Fail", });
			org.testng.Assert.assertTrue(false);
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
				FileOutputStream out = new FileOutputStream(new File(EX_DIR + "forgot_result.xlsx"));
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
