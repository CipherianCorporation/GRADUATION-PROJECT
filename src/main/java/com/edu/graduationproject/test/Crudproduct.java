package com.edu.graduationproject.test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Crudproduct {
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

	@BeforeTest
	public void open() {
		System.setProperty("webdriver.chrome.driver", "D:\\chome\\chromedriver.exe");
		driver = new ChromeDriver();
		String url = "http://localhost:8080/admin/home/index";
		driver.get(url);

	}

	@Test(priority = 1)
	public void launcherfail() throws Exception {
		try {
			// System.setProperty("webdriver.chrome.driver",
			// "D:\\chrome\\chromedriver.exe");
			// driver = new ChromeDriver();
			// String url = "http://localhost:8080/admin/home/index";
			// driver.get(url);

			Thread.sleep(2000);
			WebElement tt = driver.findElement(By.xpath("/html/body/div[1]/main/article/section/div/div/div/div/h2"));
			Thread.sleep(2000);
			org.testng.Assert.assertEquals(tt.getText(), "Đăng nhập");

			TestNGResult.put("2", new Object[] { 1d, "Demo Website not login ", "move to login page",
					"move to login page", "Pass", });
		} catch (Exception e) {
			TestNGResult.put("2",
					new Object[] { 1d, "Demo Website not login ", "Get open", "Not move to login page ", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 2)
	public void loginadmin() {

		try {

			driver.findElement(By.name("username")).sendKeys("batman");

			driver.findElement(By.name("password")).sendKeys("1221");

			driver.findElement(By.name("login")).click();

			Thread.sleep(2000);
			WebElement tt = driver.findElement(By.xpath("//strong[contains(text(),'Bảng điều khiển')]"));
			Thread.sleep(2000);
			org.testng.Assert.assertEquals(tt.getText(), "Bảng điều khiển");
			TestNGResult.put("3", new Object[] { 2d, "Login with admin ", "Open ADMIN Page ", "Get Open", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("3", new Object[] { 2d, "Login winth admin", "Open ADMIN Page", "Get Open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 3)
	public void product() {

		try {
			driver.findElement(By.xpath("/html[1]/body[1]/header[1]/header[1]/nav[1]/div[1]/div[1]/a[2]")).click();
			Thread.sleep(2000);
			WebElement tt = driver.findElement(By.xpath("//strong[contains(text(),'Sản phẩm')]"));
			Thread.sleep(5000);
			org.testng.Assert.assertEquals(tt.getText(), "Sản phẩm");

			driver.findElement(By.xpath("//input[@id='form6Example3']")).sendKeys("Giày Cao");

			Thread.sleep(2000);
			driver.findElement(
					By.xpath("//body/main[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[3]/input[1]"))
					.sendKeys("120000");

			Thread.sleep(2000);
			driver.findElement(By.xpath("//input[@id='prodStock']")).sendKeys("4");
			Select color = new Select(driver.findElement(
					By.xpath("//body/main[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[5]/select[1]")));
			color.selectByVisibleText("BLACK");
			color.selectByIndex(0);

			Thread.sleep(2000);
			Select size = new Select(driver.findElement(
					By.xpath("//body/main[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[6]/select[1]")));
			size.selectByVisibleText("33");
			size.selectByIndex(0);

			Thread.sleep(2000);
			driver.findElement(By.xpath(
					"/html[1]/body[1]/main[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[7]/div[1]/input[1]"))
					.sendKeys("5");

			Thread.sleep(2000);
			Select cate = new Select(driver.findElement(
					By.xpath("//body/main[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/form[1]/div[8]/select[1]")));
			cate.selectByVisibleText("Giày nam");
			size.selectByIndex(0);
			Thread.sleep(2000);

			Select cate8 = new Select(driver.findElement(By.xpath("//*[@id=\"ex1-tabs-1\"]/div/form/div[9]/select")));
			cate8.selectByVisibleText("Dép nam");
			size.selectByIndex(2);
			Thread.sleep(2000);

			driver.findElement(By.xpath("//*[@id=\"available1\"]")).click();

			driver.findElement(By.xpath("//button[contains(text(),'Tạo mới')]")).click();

			Thread.sleep(5000);
			driver.findElement(By.xpath("//button[contains(text(),'Tạo mới')]")).click();

			Thread.sleep(10000);

			TestNGResult.put("4",
					new Object[] { 3d, "Created product", "Create is success", "Create is success", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("4",
					new Object[] { 3d, "Created product", "Create is success", "Create is success", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@AfterTest
	public void close() {
		driver.close();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
				FileOutputStream out = new FileOutputStream(new File(EX_DIR + "Product.xlsx"));
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
