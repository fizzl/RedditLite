package net.fizzl.redditlite.model;

public class SynchronousHttpClientException extends Exception {
	
	public SynchronousHttpClientException(String string) {
		super(string);
	}

	public SynchronousHttpClientException(Exception e) {
		super(e);
	}

	private static final long serialVersionUID = 1L;

}
