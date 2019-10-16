package com.example.demo.exception;

/**
 * @author <a href="mailto:rodrigo.moncada@live.com">Rodrigo Moncada</a>
 *
 */
public class UserNotFoundException extends RuntimeException  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2514911664208693314L;

	/**
	 * @param message
	 */
	public UserNotFoundException(String message) {
		super(message);
	}
}