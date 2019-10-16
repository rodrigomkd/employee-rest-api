package com.example.demo.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.controller.UserController;
import com.example.demo.service.UserService;

/**
 * @author <a href="mailto:rodrigo.moncada@live.com">Rodrigo Moncada</a>
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@PropertySource("classpath:application.properties")
public class UserControllerTest {
	
	/**
	 * Logger instance
	 *
	 * @see Logger
	 * @see LogManager
	 */
	private static final Logger logger = LogManager.getLogger(UserControllerTest.class);
	
	private static final String USER_RESPONSE_EXPECTED = 
			"{\"user\":\"test\",\"pwd\":null,\"token\":\"Bearer eyJhbGciOiJIUzUxMiJ9\"}";
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private UserService userService;
	
	@Before
	public void setup() {
		when(userService.getJWTToken("test","test")).thenReturn("Bearer eyJhbGciOiJIUzUxMiJ9");
	}
	
	@Test
	public void testAuthToken() throws Exception {
		logger.info("[START] testAuthToken.");

		RequestBuilder request = MockMvcRequestBuilders.post(UserController.USER_API + "/token")
				.param("user", "test")
				.param("password", "test")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("[RESPONSE] " + contentAsString);
		JSONAssert.assertEquals(USER_RESPONSE_EXPECTED, contentAsString, false);

		logger.info("[END] testAuthToken.");
	}
	
	@Test
	public void testAuthTokenWithoutParams() throws Exception {
		logger.info("[START] testAuthTokenWithoutParams.");

		RequestBuilder request = MockMvcRequestBuilders.post(UserController.USER_API + "/token")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(request).andExpect(status().isBadRequest()).andReturn();

		logger.info("[END] testAuthTokenWithoutParams.");
	}
}
