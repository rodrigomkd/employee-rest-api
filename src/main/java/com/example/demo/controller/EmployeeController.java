package com.example.demo.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.controller.helper.EmployeeResourceAssembler;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;

import io.swagger.annotations.ApiOperation;

/**
 * @author <a href="mailto:rodrigo.moncada@ibm.com">Rodrigo Moncada</a>
 *
 */
@RestController
@RequestMapping(EmployeeController.EMPLOYEE_API)
public class EmployeeController {

	/**
	 * Logger instance
	 * 
	 * @see Logger
	 * @see LogManager
	 */
	private static final Logger logger = LogManager.getLogger(EmployeeController.class);

	private static final String PRODUCES_DATA = "application/json;charset=UTF-8";
	
	public static final String EMPLOYEE_API = "/api/v1/employees/";

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private EmployeeResourceAssembler resourceAssembler;

	/**
	 * Returns a List containing all the employees available.
	 * 
	 * @return
	 */
	@ApiOperation(value = "Retrieve all Active Employees", notes = "Current Status: DONE")
	@GetMapping(produces = { PRODUCES_DATA })
	public ResponseEntity<Object> retrieveEmployees() {
		logger.info("[GET] Retrieving all active Employees.");
		List<Resource<Employee>> resources = new ArrayList<>();
		employeeService.retrieveAllEmployees().forEach(employee -> resources.add(resourceAssembler.toResource(employee)));
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("total", resources.size());
		map.put("employees", resources);
		logger.info("[GET] List of Employees found.");
		
		return ResponseEntity.ok(map);
	}

	/**
	 * Returns Employee by ID.
	 * 
	 * @return
	 */
	@ApiOperation(value = "Retrieve Employee based on the ID", notes =
			"Current Status: DONE")
	@GetMapping(value = "/{id}", produces = { PRODUCES_DATA })
	public ResponseEntity<Object> retrieveEmployeeById(@PathVariable Integer id) { 
		logger.info("[GET] Retrieving Employee from ID:" + id); 
		Employee employee = employeeService.retrieveEmployeeById(id);
		logger.info("[GET] Employee found."); 
		
		return ResponseEntity.ok(resourceAssembler.toResource(employee));
	}

	/**
	 * Creates a new Employee
	 * 
	 * @return
	 */

	@ApiOperation(value = "Create a new Employee", notes = "Current Status: DONE")
	@PostMapping(produces = { PRODUCES_DATA })
	public ResponseEntity<Object> createEmployee(@Valid @RequestBody Employee employee) {
		logger.info("[POST] Creating a new record for Employee:" + employee);
		employee = employeeService.saveEmployee(employee);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(employee.getId()).toUri();
		
		logger.info("[POST] Employee was created.");
		
		return ResponseEntity.created(location).body(resourceAssembler.toResource(employee));
	}

	/**
	 * Update an existing Employee.
	 * 
	 * @param id The Employee ID
	 * @param Employee The Employee object
	 * @return ResponseEntity<Object> Employee as Resource
	 */
	@ApiOperation(value = "Update Employee based on the ID", notes =
			"Current Status: DONE")
	@PutMapping(value = "/{id}", produces = { PRODUCES_DATA })
	public ResponseEntity<Object> updateEmployee(@PathVariable Integer id, 
			@Valid @RequestBody Employee employee) {
		
		logger.info("[PUT] Updating existing Employee with id: " + id);
		employeeService.updateEmployee(id, employee);		
		logger.info("[PUT] Employee was updated.");
		
		return ResponseEntity.ok(resourceAssembler.toResource(employee));
	}
	
	/**
	 * Returns a List containing all the employees available.
	 * 
	 * @return
	 */
	@ApiOperation(value = "Set Inactive Employee to Active", notes =
			"Current Status: Inactive")
	@PatchMapping(value = "/{id}", produces = { PRODUCES_DATA })
	public ResponseEntity<Object> updateEmployeeStatus(@PathVariable Integer id) {
		logger.info("[PATCH] Updating existing Employee status only with ID:" + id);
		Employee emp = employeeService.updateEmployee(id);
		
		logger.info("[PATCH] Employee status Active updated");
		
		return ResponseEntity.ok(resourceAssembler.toResource(emp));
	}

	/**
	 * Deletes an Employee based on the Id.
	 * 
	 * @return
	 */
	@ApiOperation(value = "Delete an Employee based on the ID", notes =
			"Current Status: DONE")
	@DeleteMapping(value = "/{id}", produces = { PRODUCES_DATA })
	public ResponseEntity<Object> deleteEmployee(@PathVariable Integer id) {
		logger.info("[DELETE] Deleting Employee with id:" + id);
		employeeService.deleteEmployee(id);
		logger.info("[DELETE] Employee deleted");
		
		return ResponseEntity.noContent().build();
	}

	/**
	 * Returns a List containing all the employees available.
	 * 
	 * @return
	 */
	@ApiOperation(value = "Retrieve the communication options for the target employees.", notes =
			"Current Status: DONE")
	@RequestMapping(produces = { PRODUCES_DATA }, method = RequestMethod.OPTIONS)
	public ResponseEntity<Object> optionsEmployeeService() {		
		logger.info("[OPTIONS] Employee API is running");
		
		return ResponseEntity.noContent().header("running", "true").build();
	}
	
	/**
	 * Returns a List containing all the employees available.
	 * 
	 * @return
	 */
	@ApiOperation(value = "Retrieve the Inactive Employees", notes = "Current Status: DONE")
	@GetMapping(value = "/deleted", produces = { PRODUCES_DATA })
	public ResponseEntity<Object> retrieveDeletedEmployees() {
		logger.info("[GET] Retrieving All Inactive Employees");
		
		List<Resource<Employee>> resources = new ArrayList<>();
		employeeService.retrieveDeletedEmployees().forEach(employee -> resources.add(resourceAssembler.toResource(employee)));
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("total", resources.size());
		map.put("employees", resources);
		logger.info("[GET] List of Inactive Employees found.");
		
		return ResponseEntity.ok(map);
	}
}