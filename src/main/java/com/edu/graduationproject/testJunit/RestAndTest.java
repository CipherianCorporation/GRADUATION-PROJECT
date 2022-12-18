package com.edu.graduationproject.testJunit;

import static org.testng.AssertJUnit.assertEquals;
import org.openqa.selenium.remote.Response;
import org.springframework.beans.factory.annotation.Autowired;

import org.testng.annotations.Test;
import com.edu.graduationproject.entity.User;
import com.edu.graduationproject.repository.UserRepository;

public class RestAndTest {
	public static final String RestAssuredURL = "https://restful-booker.herokuapp.com/ping";

	@Test
	public void BookerPingTest() {
		// Response response = RestAssured.get(
		// 		RestAssuredURL);
		// Assertions.assertEquals(201, response.getStatusCode());
	}
}
