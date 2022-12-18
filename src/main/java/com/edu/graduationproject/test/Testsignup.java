package com.edu.graduationproject.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.jsonwebtoken.io.IOException;

public class Testsignup {
	public WebDriver driver;
	private XSSFWorkbook workbook;
	private XSSFSheet worksheet;
	private Map<String, Object[]> TestNGResult;
	private Map<String, String[]> dataLoginTest;

	private final String EX_DIR = "C:\\Users\\Admin\\Documents\\GRADUATION-PROJECT\\src\\main\\resources\\test_data\\";

	
	private void readDataFromExcel() {
		try {
			dataLoginTest = new  HashMap<String,String[]>();
			worksheet = workbook.getSheet("Sheet1");
			if(worksheet == null) {
				System.out.println("No found");
			}else {
				Iterator<Row> rowIterator = worksheet.iterator();
				DataFormatter dataformat = new DataFormatter();
				while(rowIterator.hasNext()){
					Row row = rowIterator.next();
					if(row.getRowNum()>=1) {
						Iterator<Cell> celliterator = row.cellIterator();
						String key = "";
						String name = "";
						String mail = "";
						String pass = "";
						String repass = "";
						String excepted ="";
						while(celliterator.hasNext()) {
							Cell cell = celliterator.next();
						if(cell.getColumnIndex() == 0) {
								key = dataformat.formatCellValue(cell);
						}else if(cell.getColumnIndex() == 1) {
							 name = dataformat.formatCellValue(cell);
						}else if(cell.getColumnIndex() == 2) {
							 mail = dataformat.formatCellValue(cell);
						}
						else if(cell.getColumnIndex() == 3) {
							pass = dataformat.formatCellValue(cell);
						}
						else if(cell.getColumnIndex() == 4) {
							 repass = dataformat.formatCellValue(cell);
						}
						else if(cell.getColumnIndex() == 5) {
							 excepted = dataformat.formatCellValue(cell);
						}
					String[] myArr = {name,mail,pass,repass,excepted};
					dataLoginTest.put(key, myArr);
						}
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void takeScreenshot (WebDriver driver, String outputSrc) throws IOException{
		
	}

	public void writeImage() {
		
	}


	@BeforeClass
	public void suittest() {
		try {
			TestNGResult = new LinkedHashMap<String, Object[]>();
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			workbook = new XSSFWorkbook(new FileInputStream(new File(EX_DIR + "RegistrationData.xlsx")));
			worksheet = workbook.getSheet("Sheet1");
			readDataFromExcel();
			driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
			workbook = new XSSFWorkbook();
			worksheet = workbook.createSheet("TestNG Result");
			
			CellStyle rowstyle = workbook.createCellStyle();
			rowstyle.setAlignment(HorizontalAlignment.CENTER);
			rowstyle.setVerticalAlignment(VerticalAlignment.CENTER);
			rowstyle.setWrapText(true);
			
			TestNGResult.put("1", new Object[] {"STT","Username","mail","password","repass","action","Excepted","actual","status","datecheck","link","image"});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Test
	public void testtitle() {
		try {
			Set<String> keyset = dataLoginTest.keySet();
			int index = 1;
			for(String key : keyset) {
				String[] value = dataLoginTest.get(key);
				String username = value[0];
				String mail = value[1];
				String pass = value[2];
				String repass = value[3];
				String excepted = value[4];
				 
				 
				LocalDateTime myDateobj = LocalDateTime.now();
				DateTimeFormatter myFormatobj = DateTimeFormatter.ofPattern("HH:mm:ss | dd-MM-yyyy");
				String formattedDate = myDateobj.format(myFormatobj);
				
				driver.get("http://localhost:8080/account/signup");
				
				driver.findElement(By.id("floatingUsername")).sendKeys(username);
				System.out.println(username);
				driver.findElement(By.id("floatingEmail")).sendKeys(mail);
				System.out.println(mail);
				driver.findElement(By.id("password")).sendKeys(pass);
				System.out.println(pass);
				driver.findElement(By.id("floatingRetype")).sendKeys(repass);
				System.out.println(repass);
				Thread.sleep(1000);
				String actutitle = driver.getTitle();
				
				driver.findElement(By.xpath("//*[@id=\"termCheck\"]")).click();
				Thread.sleep(1000);
				driver.findElement(By.name("ok")).click();
				WebElement tt = driver.findElement(By.xpath("/html/body/div[1]/main/article/section/div/div/div/div/form/div/div[6]/span"));
				Thread.sleep(5000);
				String t = "Please check your email to verify your account";
				
				if(tt.getText().equalsIgnoreCase(t)) {
					TestNGResult.put(String.valueOf(index +1), new Object[] {
							String.valueOf(index),
							username,
							mail,
							pass,
							repass,
							"Tes sign up",
							excepted,
							"Sign is sucess",
							"PASS",
							formattedDate,
							""
							
					});
				}else {
					TestNGResult.put(String.valueOf(index +1), new Object[] {
							String.valueOf(index),
							username,
							mail,
							pass,
							repass,
							"testtitle",
							excepted,
							"Sign up is fail",
							"Fail",
							formattedDate,
							""
							
					});
					
				}
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
//	public void testpass() {
//		try {
//			Set<String> keyset = dataLoginTest.keySet();
//			int index = 1;
//			for(String key : keyset) {
//				String[] value = dataLoginTest.get(key);
//				String username = value[0];
//				String mail = value[1];
//				String pass = value[2];
//				String repass = value[3];
//				String excepted = value[4];
//				 
//				 
//				LocalDateTime myDateobj = LocalDateTime.now();
//				DateTimeFormatter myFormatobj = DateTimeFormatter.ofPattern("HH:mm:ss | dd-MM-yyyy");
//				String formattedDate = myDateobj.format(myFormatobj);
//				
//				driver.get("http://localhost:8080/account/signup");
//				
//				driver.findElement(By.id("floatingUsername")).sendKeys(username);
//				System.out.println(username);
//				driver.findElement(By.id("floatingEmail")).sendKeys(mail);
//				System.out.println(mail);
//				driver.findElement(By.id("password")).sendKeys(pass);
//				System.out.println(pass);
//				driver.findElement(By.id("floatingRetype")).sendKeys(repass);
//				System.out.println(repass);
//				Thread.sleep(1000);
//				driver.findElement(By.name("ok")).click();
//				
//				
//				String actutitle = driver.getTitle();
//				if(pass.equalsIgnoreCase(repass)) {
//					TestNGResult.put(String.valueOf(index +1), new Object[] {
//							String.valueOf(index),
//							username,
//							mail,
//							pass,
//							repass,
//							"testpassword",
//							excepted,
//							actutitle,
//							"PASS",
//							formattedDate,
//							""
//							
//					});
//				}else {
//					TestNGResult.put(String.valueOf(index +1), new Object[] {
//							String.valueOf(index),
//							username,
//							mail,
//							pass,
//							repass,
//							"testpassword",
//							excepted,
//							actutitle,
//							"Fail",
//							formattedDate,
//							""
//							
//					});
//					
//				}
//				index++;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	@AfterClass
	public void suiteteardown(){
		Set<String> keyset = TestNGResult.keySet();
		int rownum = 0;
		for(String key : keyset) {
			CellStyle rowstyle = workbook.createCellStyle();
			Row row = worksheet.createRow(rownum++);
			Object[] objarr = TestNGResult.get(key);
			int cellnum = 0;
			for(Object obj : objarr) {
				int flag = cellnum ++;
				Cell cell = row.createCell(flag);
				if(obj instanceof Date) {
					cell.setCellValue((Date) obj);
				}else if(obj instanceof Boolean) {
					cell.setCellValue((Boolean) obj);
				}else if(obj instanceof String) {
					cell.setCellValue((String) obj);
				}else if(obj instanceof Double) {
					cell.setCellValue((Double) obj);
				}
				
			}
			try {
				FileOutputStream out  = new FileOutputStream(new File(EX_DIR + "RegistrationData_result.xlsx"));
				workbook.write(out);
				out.close();
				System.out.println("Successfully !!");
			} catch (Exception e) {
				System.out.println("suiteteardown() :" +e.getMessage());
				// TODO: handle exception
			}
		}
	}


}
