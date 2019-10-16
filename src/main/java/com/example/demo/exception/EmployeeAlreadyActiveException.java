package com.example.demo.exception;

/**
 * @author <a href="mailto:rodrigo.moncada@live.com">Rodrigo Moncada</a>
 *
 */
public class EmployeeAlreadyActiveException extends RuntimeException  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public EmployeeAlreadyActiveException(String message) {
		super(message);
	}
}