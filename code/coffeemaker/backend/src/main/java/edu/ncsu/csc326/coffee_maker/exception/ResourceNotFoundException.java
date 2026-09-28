package edu.ncsu.csc326.coffee_maker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception if a resource is not found in the database.
 *
 * @author CSC 326 Course Staff
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /** Default serial version uid*/
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs the exception with the given message.
	 * @param message exception message
	 */
	public ResourceNotFoundException(String message) {
        super(message);
    }

}
