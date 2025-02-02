package com.ibretail.ibretaildemo.membership;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class WrongCredentialException extends RuntimeException{
	public WrongCredentialException(HttpStatus unauthorized, String message)  {
		super(message);
	}
}
