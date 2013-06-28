package net.fizzl.redditlite.model;

public class RedditModel {
	// Account management
	
	// Singleton
	private RedditModel() {
	}
	public RedditModel getInstance() {
		if(me == null) {
			me = new RedditModel();
		}
		return me;
	}
	private static RedditModel me;
}
