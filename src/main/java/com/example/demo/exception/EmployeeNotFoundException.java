package com.example.demo.exception;

/**
 * @author <a href="mailto:rodrigo.moncada@live.com">Rodrigo Moncada</a>
 *
 */
public class EmployeeNotFoundException extends RuntimeException  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2632063712611750935L;

	/**
	 * @param message
	 */
	public EmployeeNotFoundException(String message) {
		super(message);
	}
}