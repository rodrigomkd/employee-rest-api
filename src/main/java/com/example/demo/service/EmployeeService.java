package com.example.demo.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.data.EmployeeRepository;
import com.example.demo.exception.EmployeeAlreadyActiveException;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.model.Employee;

/**
 * @author <a href="mailto:rodrigo.moncada@live.com">Rodrigo Moncada</a>
 *
 */
@Service
public class EmployeeService
{

	/**
	 * Logger instance
	 * 
	 * @see Logger
	 * @see LogManager
	 */
	private static final Logger logger = LogManager.getLogger(EmployeeService.class);

	@Autowired
	private EmployeeRepository employeeRepository;

	/**
	 * Retrieve all employees
	 * 
	 * @param status Active or Inactive
	 * @return List<Employee> List of Employees
	 */
	private List<Employee> retrieveEmployeesByStatus(boolean status) {
		logger.info("Entering retrieveAllEmployees()");
		List<Employee> employees = employeeRepository.findAllByStatus(status);
		if (employees == null || employees.isEmpty()) {
			throw new EmployeeNotFoundException("There are no Employees");
		}
		logger.info("Exiting retrieveAllEmployees()");
		
		return employees;
		
	}
	
	/**
	 * Retrieve all employees
	 * 
	 * @param 
	 * @return List<Employee> List of Employees
	 */
	public List<Employee> retrieveAllEmployees() {
		logger.info("Entering retrieveAllEmployees()");
		List<Employee> employees = employeeRepository.findAllByStatus(true);
		if (employees == null || employees.isEmpty()) {
			throw new EmployeeNotFoundException("There are no Employees");
		}
		logger.info("Exiting retrieveAllEmployees()");
		
		return employees;
		
	}
	
	/**
	 * Retrieve all Employees by status
	 *  
	 * @return List<Employee> List of Employees
	 */
	public List<Employee> retrieveDeletedEmployees() {
		return retrieveEmployeesByStatus(false);	
	}
	
	/**
	 * Retrieve an Employee by Id
	 * 
	 * @param employeeId Employee's Id
	 * @return Employee The employee object
	 */
	public Employee retrieveEmployeeById(Integer employeeId) {
		logger.info("Entering retrieveEmployeeById() for Id:" + employeeId);
		Employee employee = getEmployee(employeeId);
		logger.info("Exiting retrieveEmployeeById() for employeeId: " + employeeId);
		
		return employee;
	}
	
	/**
	 * Save Employee on database
	 * 
	 * @param employee The Employee object
	 * @return Employee The object saved
	 */
	public Employee saveEmployee(Employee employee) {
		logger.info("Entering saveEmployee() for employee: " + employee);
		employee = employeeRepository.save(employee);
		logger.info("Exiting saveEmployee() for employee: " + employee);

		return employee;
	}
	
	/**
	 * Update Employee existed on database
	 * 
	 * @param employeeId
	 * @param employee
	 * @return
	 */
	public Employee updateEmployee(Integer id, Employee employee) {
		logger.info("Entering updateEmployee() for Id:" + id);
		
		getEmployee(id);
		employee.setId(id);
		employee = employeeRepository.save(employee);
		logger.info("Exiting updateEmployee() for employeeId: " + id);
		
		return employee;
	}
	
	/**
	 * Update Employee status to Active
	 * 
	 * @param employeeId
	 * @param employee
	 * @return
	 */
	public Employee updateEmployee(Integer id) {
		logger.info("Entering updateEmployee() status for ID:" + id);
		
		if (! employeeRepository.existsById(id)) {
			throw new EmployeeNotFoundException("The Employee with ID: " + id + ", was not found");
		}
		
		Employee employee = employeeRepository.getOne(id);
		if(employee.isStatus()) {
			throw new EmployeeAlreadyActiveException("The Employee with ID: " + id + " is already Active.");
		}
		employee.setStatus(true);
		employee = employeeRepository.save(employee);
		logger.info("Exiting updateEmployee() for ID: " + id);
		
		return employee;
	}
	
	/**
	 * @param employeeId
	 * @return
	 */
	public Integer deleteEmployee(Integer employeeId) {
		logger.info("Entering deleteEmployee() for employeeId:" + employeeId);
		Employee employee = getEmployee(employeeId);
		employee.setStatus(false);
		employeeRepository.save(employee);

		logger.info("Exiting deleteEmployee() for employeeId:" + employeeId);
		return employeeId;
	}
	
	/**
	 * Get Employee by ID
	 * 
	 * @param id Employee ID
	 * @return Employe
	 */
	private Employee getEmployee(Integer employeeId) {
		logger.info("Entering getEmployee() for employeeId:" + employeeId);
		
		Employee employee = employeeRepository.findByIdAndStatus(employeeId, true)
				.orElseThrow(() -> new EmployeeNotFoundException("Employee data with id: " 
				+ employeeId + ", was not found"));
		
		logger.info("Entering getEmployee() for employeeId:" + employeeId);
		return employee;
	}
}