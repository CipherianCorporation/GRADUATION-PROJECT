package com.edu.graduationproject.testJunit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class RestAssuredWithSelenium {
    static WebDriver driver;
    public static final String RestAssuredURL = "https://restful-booker.herokuapp.com/ping";

    @BeforeTest
    public static void beforeAnything() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions headlessOptions = new ChromeOptions();
        headlessOptions.addArguments("headless");
        driver = new ChromeDriver(headlessOptions);
    }

    @Test
    public void shouldAnswerWithTrue() {
        driver.get(RestAssuredURL);
    }

    @AfterTest
    public static void afterEverything() {
        if (driver != null) {
            driver.close();
        }
    }
}