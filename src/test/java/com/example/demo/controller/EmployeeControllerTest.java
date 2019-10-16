package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Spy;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.demo.config.JwtRequestFilter;
import com.example.demo.controller.EmployeeController;
import com.example.demo.controller.helper.EmployeeResourceAssembler;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
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
public class EmployeeControllerTest
{
	/**
	 * Logger instance
	 *
	 * @see Logger
	 * @see LogManager
	 */
	private static final Logger logger = LogManager.getLogger(EmployeeControllerTest.class);

	private static final String RESPONSE_EXPECTED_EMPLOYEES = "{\"total\":1,\"employees\":[{\"id\":1,\"firstName\":\"Rodrigo\","
			+ "\"middleInitial\":\"Sr.\",\"lastName\":\"Moncada\",\"dateOfBirth\":\"1992-01-18\","
			+ "\"dateOfEmployment\":\"2018-01-01\",\"status\":true,\"links\":[]}]}";
	
	private static final String RESPONSE_EXPECTED_DELETED = "{\"total\":1,\"employees\":"
			+ "[{\"id\":1,\"firstName\":\"Rodrigo\",\"middleInitial\":\"Sr.\",\"lastName\":\"Moncada\","
			+ "\"dateOfBirth\":\"1992-01-18\",\"dateOfEmployment\":\"2018-01-01\",\"status\":true,"
			+ "\"links\":[]}]}";

	private static final String RESPONSE_EXPECTED_EMPLOYEE = "{\"id\":1,\"firstName\":\"Rodrigo\","
			+ "\"middleInitial\":\"Sr.\",\"lastName\":\"Moncada\",\"dateOfBirth\":\"1992-01-18\","
			+ "\"dateOfEmployment\":\"2018-01-01\",\"status\":true}";
	
	private static final String EMPLOYEE_JSON = "{\"firstName\":\"Rodrigo\",\"middleInitial\":\"M.C.C.\",\"lastName\":\"Moncada\","
			+ "\"dateOfBirth\":\"1992-01-18\",\"dateOfEmployment\":\"2018-01-01\",\"status\":true}";
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private UserService userService;

	@MockBean
	private EmployeeService employeeService;
	
	@MockBean
	private EmployeeResourceAssembler employeeResourceAssembler;
	
	@Spy
	private final JwtRequestFilter securityConfiguration = new JwtRequestFilter("");
	
	@Before
	public void setup() {
		List<Employee> employees = new ArrayList<>();
		Employee employee = new Employee();
		employee.setId(1);
		employee.setFirstName("Rodrigo");
		employee.setMiddleInitial("Sr.");
		employee.setLastName("Moncada");
		employee.setDateOfBirth(LocalDate.of(1992, Month.JANUARY, 18));
		employee.setDateOfEmployment(LocalDate.of(2018, Month.JANUARY, 01));
		employee.setStatus(true);
		employees.add(employee);
		
		List<Employee> employeesDeleted = new ArrayList<>();
		Employee employeeDeleted = new Employee();
		employeeDeleted.setId(2);
		employeeDeleted.setFirstName("Rod");
		employeeDeleted.setMiddleInitial("Mr.");
		employeeDeleted.setLastName("Rodriguez");
		employeeDeleted.setDateOfBirth(LocalDate.of(1992, Month.JANUARY, 18));
		employeeDeleted.setDateOfEmployment(LocalDate.of(2018, Month.JANUARY, 01));
		employeeDeleted.setStatus(false);
		employeesDeleted.add(employee);
		
		when(employeeService.retrieveAllEmployees()).thenReturn(employees);
		when(employeeService.retrieveEmployeeById(1)).thenReturn(employee);
		when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);
		when(employeeService.updateEmployee(1, employee)).thenReturn(employee);
		when(employeeService.deleteEmployee(1)).thenReturn(1);
		when(employeeService.retrieveDeletedEmployees()).thenReturn(employeesDeleted);
		employeeDeleted.setStatus(true);
		when(employeeService.updateEmployee(2)).thenReturn(employeeDeleted);
		
		when(employeeResourceAssembler.toResource(any()))
			.thenReturn(new Resource<Employee>(employee));
	}

	@Test
	public void testRetrieveEmployees() throws Exception {
		logger.info("[START] testRetrieveEmployees.");

		RequestBuilder request = MockMvcRequestBuilders.get(EmployeeController.EMPLOYEE_API)
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("[RESPONSE] " + contentAsString);
		JSONAssert.assertEquals(RESPONSE_EXPECTED_EMPLOYEES, contentAsString, false);

		logger.info("[END] testRetrieveEmployees.");
	}
	
	@Test
	public void testRetrieveDeletedEmployees() throws Exception {
		logger.info("[START] testRetrieveDeletedEmployees.");

		RequestBuilder request = MockMvcRequestBuilders.get(EmployeeController.EMPLOYEE_API + "deleted")
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("[RESPONSE] " + contentAsString);
		JSONAssert.assertEquals(RESPONSE_EXPECTED_DELETED, contentAsString, false);

		logger.info("[END] testRetrieveDeletedEmployees.");
	}
	
	@Test
	public void testUpdateEmployeeActive() throws Exception {
		logger.info("[START] testUpdateEmployeeActive.");

		RequestBuilder request = MockMvcRequestBuilders.patch(EmployeeController.EMPLOYEE_API + 2)
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("[RESPONSE] " + contentAsString);
		JSONAssert.assertEquals(RESPONSE_EXPECTED_EMPLOYEE, contentAsString, false);

		logger.info("[END] testUpdateEmployeeActive.");
	}
	
	@Test
	public void testRetrieveEmployeeById() throws Exception {
		logger.info("[START] testRetrieveEmployeeById.");

		RequestBuilder request = MockMvcRequestBuilders.get(EmployeeController.EMPLOYEE_API, 1)
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("[RESPONSE] " + contentAsString);
		JSONAssert.assertEquals(RESPONSE_EXPECTED_EMPLOYEES, contentAsString, false);

		logger.info("[END] testRetrieveEmployeeById.");
	}
		
	@Test
	public void testSaveEmployee() throws Exception {
		logger.info("[START] testSaveEmployee.");
		
		Employee employee = new Employee();
		employee.setFirstName("Rodrigo");
		employee.setMiddleInitial("Sr.");
		employee.setLastName("Moncada");
		employee.setDateOfBirth(LocalDate.of(1992, Month.JANUARY, 18));
		employee.setDateOfEmployment(LocalDate.of(2018, Month.JANUARY, 01));
		employee.setStatus(true);

		RequestBuilder request = MockMvcRequestBuilders.post(EmployeeController.EMPLOYEE_API)
				.content(EMPLOYEE_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andExpect(status().isCreated()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("[RESPONSE] " + contentAsString);
		JSONAssert.assertEquals(RESPONSE_EXPECTED_EMPLOYEE, contentAsString, false);

		logger.info("[END] testSaveEmployee.");
	}
	
	@Test
	public void testSaveEmployeeBadRequest() throws Exception {
		logger.info("[START] testSaveEmployeeBadRequest.");
			
		Employee employee = new Employee();
		employee.setFirstName("Rodrigo");
		employee.setMiddleInitial("Sr.");
		employee.setLastName("Moncada");
		employee.setDateOfBirth(LocalDate.of(2019, Month.JANUARY, 18));
		employee.setDateOfEmployment(LocalDate.of(2018, Month.JANUARY, 01));
		employee.setStatus(true);

		RequestBuilder request = MockMvcRequestBuilders.post(EmployeeController.EMPLOYEE_API)
				.content(employee.toString())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(request).andExpect(status().isBadRequest()).andReturn();

		logger.info("[END] testSaveEmployeeBadRequest.");
	}
	
	@Test
	public void testUpdateEmployee() throws Exception {
		logger.info("[START] testUpdateEmployee.");

		RequestBuilder request = MockMvcRequestBuilders.put(EmployeeController.EMPLOYEE_API + 1)
				.content(EMPLOYEE_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);
		MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
		String contentAsString = result.getResponse().getContentAsString();
		logger.info("[RESPONSE] " + contentAsString);
		JSONAssert.assertEquals(RESPONSE_EXPECTED_EMPLOYEE, contentAsString, false);

		logger.info("[END] testUpdateEmployee.");
	}
		
	@Test
	public void testDeleteEmployeeWithNoValidToken() throws Exception {
		logger.info("[START] testDeleteEmployeeWithNoValidToken.");
		
		String token = "Bearer eyJhbGciOiJIUzUxMiJ9";
		
		RequestBuilder request = MockMvcRequestBuilders.delete(EmployeeController.EMPLOYEE_API + 1).header("Authorization", token)
				.accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(request).andExpect(status().isForbidden()).andReturn();

		logger.info("[END] testDeleteEmployeeWithNoValidToken.");
	}
	
	@Test
	public void testDeleteEmployeeWithoutToken() throws Exception {
		logger.info("[START] testDeleteEmployeeWithoutToken.");

		RequestBuilder request = MockMvcRequestBuilders.delete(EmployeeController.EMPLOYEE_API, 1)
				.accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(request).andExpect(status().isForbidden()).andReturn();

		logger.info("[END] testDeleteEmployeeWithoutToken.");
	}
}