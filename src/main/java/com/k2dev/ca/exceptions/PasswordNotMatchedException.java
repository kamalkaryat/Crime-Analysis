package com.k2dev.ca.exceptions;

public class PasswordNotMatchedException extends RuntimeException{
	private static final long serialVersionUID = 2999061852590965838L;
	public PasswordNotMatchedException(String msg) {
		super(msg);
	}
}
