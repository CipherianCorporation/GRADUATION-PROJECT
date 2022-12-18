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
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Admin {
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

			driver.findElement(By.name("password")).sendKeys("123123");

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
			Thread.sleep(2000);
			org.testng.Assert.assertEquals(tt.getText(), "Sản phẩm");
			TestNGResult.put("4", new Object[] { 3d, "Open product setting", "Get open ", "Open", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("4", new Object[] { 3d, "Open product setting", "Get open ", "Can't open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 4)
	public void user() {

		try {

			driver.findElement(By.xpath("//*[@id=\"sidebarMenu\"]/div/div/a[3]")).click();

			Thread.sleep(2000);
			WebElement tt = driver.findElement(By.xpath("//strong[contains(text(),'Người dùng')]"));
			Thread.sleep(2000);
			org.testng.Assert.assertEquals(tt.getText(), "Người dùng");
			TestNGResult.put("5", new Object[] { 4d, "Open user setting", "Get open ", "Open", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("5", new Object[] { 4d, "Open user setting", "Get open ", "Can't open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 5)
	public void order() {

		try {

			driver.findElement(By.xpath("//*[@id=\"sidebarMenu\"]/div/div/a[4]")).click();

			Thread.sleep(2000);
			WebElement tt = driver.findElement(By.xpath("//strong[contains(text(),'Đơn hàng')]"));
			Thread.sleep(2000);
			org.testng.Assert.assertEquals(tt.getText(), "Đơn hàng");
			TestNGResult.put("6", new Object[] { 5d, "Open Order setting", "Get open ", "Open", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("6", new Object[] { 5d, "Open Order setting", "Get open ", "Can't open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 6)
	public void category() {

		try {

			driver.findElement(By.xpath("//*[@id=\"sidebarMenu\"]/div/div/a[5]")).click();

			Thread.sleep(2000);
			WebElement tt = driver.findElement(By.xpath("/html/body/main/div/div/section/div/div/h5/strong"));
			Thread.sleep(2000);
			org.testng.Assert.assertEquals(tt.getText(), "Danh mục & danh mục phụ");
			TestNGResult.put("7", new Object[] { 6d, "Open Category setting", "Get open ", "Open", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("7", new Object[] { 6d, "Open Category setting", "Get open ", "Can't open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 7)
	public void voucher() {

		try {

			driver.findElement(By.xpath("//*[@id=\"sidebarMenu\"]/div/div/a[6]")).click();

			TestNGResult.put("8", new Object[] { 7d, "Open voucher setting", "Get open ", "Open", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("8", new Object[] { 7d, "Open voucher setting", "Get open ", "Can't open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 8)
	public void auth() {

		try {

			driver.findElement(By.xpath("//*[@id=\"sidebarMenu\"]/div/div/a[7]")).click();

			Thread.sleep(2000);
			WebElement tt = driver.findElement(By.xpath("//strong[contains(text(),'Cấp quyền')]"));
			Thread.sleep(2000);
			org.testng.Assert.assertEquals(tt.getText(), "Cấp quyền");
			TestNGResult.put("9", new Object[] { 8d, "Open auth setting", "Get open ", "Open", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("9", new Object[] { 8d, "Open auth setting", "Get open ", "Can't open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 9)
	public void back() {

		try {

			driver.findElement(By.xpath("//*[@id=\"sidebarMenu\"]/div/div/a[8]")).click();

			TestNGResult.put("10", new Object[] { 9d, "Back to list", "Get open product list", "Open", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("10", new Object[] { 9d, "Back to list", "Get open product list", "Can't open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	// @Test(priority = 2)
	// public void truemail() {
	//
	//
	// try {
	//
	//
	// WebElement tt =
	// driver.findElement(By.xpath("/html/body/div[1]/main/article/section/div/div/div/div/div/form/div[3]/span"));
	// Thread.sleep(2000);
	// org.testng.Assert.assertEquals(tt.getText(), "Chúng tôi đã gửi liên kết để
	// đặt lại đến email cảu bạn. Vui lòng kiểm tra!!!");
	// TestNGResult.put("3", new Object[] { 2d, "Input True mail and check message",
	// "True", "Pass", });
	//
	// } catch (Exception e) {
	// TestNGResult.put("3", new Object[] { 2d, "Input True mail and check message",
	// "True mail", "Fail", });
	// org.testng.Assert.assertTrue(false);
	// }
	//
	// }

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
				FileOutputStream out = new FileOutputStream(new File(EX_DIR + "Admin.xlsx"));
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
