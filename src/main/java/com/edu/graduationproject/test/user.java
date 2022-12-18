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

public class user {
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
		String url = "http://localhost:8080/product/list";
		driver.get(url);

	}

	// @Test(priority = 1)
	// public void search() throws Exception{
	// try {
	//// System.setProperty("webdriver.chrome.driver",
	// "D:\\chrome\\chromedriver.exe");
	//// driver = new ChromeDriver();
	//// String url = "http://localhost:8080/admin/home/index";
	//// driver.get(url);
	//
	// Thread.sleep(5000);
	// driver.findElement(By.xpath("//header/nav[1]/div[1]/div[1]/form[1]/input[1]")).sendKeys("giày
	// nam");
	//
	//
	//
	//
	//
	// TestNGResult.put("2", new Object[] { 1d, "Demo Website not login ", "move to
	// login page","move to login page", "Pass", });
	// } catch (Exception e) {
	// TestNGResult.put("2", new Object[] { 1d, "Demo Website not login ", "Get
	// open","Not move to login page ", "Fail", });
	// org.testng.Assert.assertTrue(false);
	// }
	//
	//
	// }

	@Test(priority = 2)
	public void searchcate1() {

		try {

			driver.findElement(By.xpath("//header/nav[1]/div[1]/div[1]/ul[1]/li[1]/a[1]")).click();

			Thread.sleep(7000);

			TestNGResult.put("3",
					new Object[] { 2d, "Search Giày nam", "Show giày name", "Giay nam in display", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("3",
					new Object[] { 2d, "Search Giày nam", "Show giày name", "Giay nam not in display", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 3)
	public void searchcate2() {

		try {

			driver.findElement(By.xpath("//header/nav[1]/div[1]/div[1]/ul[1]/li[2]/a[1]")).click();

			Thread.sleep(7000);

			TestNGResult.put("4", new Object[] { 3d, "Search Giày nữ", "Show giày nữ", "Giay nữ in display", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("4",
					new Object[] { 3d, "Search Giày nữ", "Show giày nữ", "Giay nữ not in display", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 4)
	public void searchcate3() {

		try {

			driver.findElement(By.xpath("//header/nav[1]/div[1]/div[1]/ul[1]/li[3]/a[1]")).click();

			Thread.sleep(7000);

			TestNGResult.put("5",
					new Object[] { 4d, "Search phụ kiện", "Show phụ kiện ", "phụ kiện displaying", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("5",
					new Object[] { 4d, "Search phụ kiện", "Show phụ kiện ", "phụ kiện not in display", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 5)
	public void searchcate4() {

		try {

			driver.findElement(By.xpath("//header/nav[1]/div[1]/div[1]/ul[1]/li[6]/a[1]")).click();

			Thread.sleep(7000);

			TestNGResult.put("6", new Object[] { 5d, "Search saleoff", "Show product saleoff ",
					"product sale off is displaying", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("6", new Object[] { 5d, "Search saleoff", "Show product saleoff  ",
					"product sale off not display", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 6)
	public void filter() {

		try {

			driver.findElement(By.xpath("//button[contains(text(),'Danh mục')]")).click();
			Thread.sleep(5000);

			driver.findElement(By.xpath("//a[contains(text(),'> Giày thể thao nam')]")).click();
			Thread.sleep(5000);
			driver.findElement(By.xpath("//a[contains(text(),'> Giày tây & slippon')]")).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath("//a[contains(text(),'> Sandal nữ')]")).click();
			Thread.sleep(3000);

			TestNGResult.put("7", new Object[] { 6d, "Filer product", "show product ", "showproduct", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("7",
					new Object[] { 6d, "Filer product", "show product ", "product not display", "fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 7)
	public void cart() {

		try {

			driver.findElement(By.xpath("//header/nav[1]/div[1]/div[1]/a[1]")).click();
			Thread.sleep(7000);

			Thread.sleep(6000);
			WebElement add = driver.findElement(By.xpath(
					"//body/div[1]/div[1]/main[1]/article[1]/main[1]/div[1]/div[3]/div[1]/div[1]/div[3]/div[2]/button[1]"));
			add.click();

			Thread.sleep(3000);
			WebElement view = driver
					.findElement(By.xpath("//header/nav[1]/div[1]/div[2]/a[1]/i[1]"));
			view.click();
			Thread.sleep(3000);

			TestNGResult.put("8", new Object[] { 7d, "Open cart page", "Get open", "Open", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("8", new Object[] { 7d, "Open cart page", "Get open ", "Can't open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 9)
	public void opensignin() {

		try {

			driver.findElement(By.xpath("//header/nav[1]/div[1]/div[1]/a[1]")).click();
			Thread.sleep(3000);

			WebElement add = driver.findElement(By.xpath(
					"//a[@id='avatarDropdown']"));
			add.click();

			Thread.sleep(2000);
			WebElement view = driver
					.findElement(By.xpath("//a[contains(text(),'Đăng ký')]"));
			view.click();
			Thread.sleep(3000);

			WebElement tt = driver.findElement(By.xpath("//h2[contains(text(),'Đăng ký')]"));
			Thread.sleep(2000);
			org.testng.Assert.assertEquals(tt.getText(), "Đăng ký");
			Thread.sleep(2000);

			TestNGResult.put("10", new Object[] { 9d, "Open signin page", "Get open", "Open", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("10", new Object[] { 9d, "Open signin page", "Get open ", "Can't open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 8)
	public void opensigup() {

		try {

			driver.findElement(By.xpath("//header/nav[1]/div[1]/div[1]/a[1]")).click();
			Thread.sleep(3000);

			WebElement add = driver.findElement(By.xpath(
					"//a[@id='avatarDropdown']"));
			add.click();

			Thread.sleep(2000);
			WebElement view = driver
					.findElement(By.xpath("//a[contains(text(),'Đăng nhập')]"));
			view.click();
			Thread.sleep(3000);

			WebElement tt = driver.findElement(By.xpath("/html/body/div[1]/main/article/section/div/div/div/div/h2"));
			Thread.sleep(2000);
			org.testng.Assert.assertEquals(tt.getText(), "Đăng nhập");
			Thread.sleep(2000);

			TestNGResult.put("9", new Object[] { 8d, "Open signup page", "Get open", "Open", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("9", new Object[] { 8d, "Open sigup page", "Get open ", "Can't open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}

	@Test(priority = 10)
	public void forgot() {

		try {

			driver.findElement(By.xpath("//header/nav[1]/div[1]/div[1]/a[1]")).click();
			Thread.sleep(3000);

			WebElement add = driver.findElement(By.xpath(
					"//a[@id='avatarDropdown']"));
			add.click();

			Thread.sleep(2000);
			WebElement view = driver
					.findElement(By.xpath("//a[contains(text(),'Quên mật khẩu')]"));
			view.click();
			Thread.sleep(3000);

			WebElement tt = driver.findElement(By.xpath("//h2[contains(text(),'Quên mật khẩu')]"));
			Thread.sleep(2000);
			org.testng.Assert.assertEquals(tt.getText(), "Quên mật khẩu");
			Thread.sleep(2000);

			TestNGResult.put("11", new Object[] { 10d, "Open signup page", "Get open", "Open", "Pass", });

		} catch (Exception e) {
			TestNGResult.put("11", new Object[] { 10d, "Open sigup page", "Get open ", "Can't open", "Fail", });
			org.testng.Assert.assertTrue(false);
		}

	}
	// @Test(priority = 7)
	// public void voucher() {
	//
	//
	// try {
	//
	//
	// driver.findElement(By.xpath("//*[@id=\"sidebarMenu\"]/div/div/a[6]")).click();
	//
	// TestNGResult.put("8", new Object[] { 7d, "Open voucher setting", "Get open
	// ","Open", "Pass", });
	//
	// } catch (Exception e) {
	// TestNGResult.put("8", new Object[] { 7d, "Open voucher setting", "Get open
	// ","Can't open", "Fail", });
	// org.testng.Assert.assertTrue(false);
	// }
	//
	// }
	// @Test(priority = 8)
	// public void auth() {
	//
	//
	// try {
	//
	//
	//
	// driver.findElement(By.xpath("//*[@id=\"sidebarMenu\"]/div/div/a[7]")).click();
	//
	//
	// Thread.sleep(2000);
	// WebElement tt = driver.findElement(By.xpath("//strong[contains(text(),'Cấp
	// quyền')]"));
	// Thread.sleep(2000);
	// org.testng.Assert.assertEquals(tt.getText(), "Cấp quyền");
	// TestNGResult.put("9", new Object[] { 8d, "Open auth setting", "Get open
	// ","Open", "Pass", });
	//
	// } catch (Exception e) {
	// TestNGResult.put("9", new Object[] { 8d, "Open auth setting", "Get open
	// ","Can't open", "Fail", });
	// org.testng.Assert.assertTrue(false);
	// }
	//
	// }
	// @Test(priority = 9)
	// public void back() {
	//
	//
	// try {
	//
	//
	//
	//
	// driver.findElement(By.xpath("//*[@id=\"sidebarMenu\"]/div/div/a[8]")).click();
	//
	//
	//
	// TestNGResult.put("10", new Object[] { 9d, "Back to list", "Get open product
	// list","Open", "Pass", });
	//
	// } catch (Exception e) {
	// TestNGResult.put("10", new Object[] { 9d, "Back to list", "Get open product
	// list","Can't open", "Fail", });
	// org.testng.Assert.assertTrue(false);
	// }
	//
	// }

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
			Thread.sleep(3000);
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
				FileOutputStream out = new FileOutputStream(new File(EX_DIR + "User.xlsx"));
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
