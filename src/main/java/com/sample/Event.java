package com.sample;

public class Event {
	
	private String source;
	private String body;
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public Event(String source, String body) {
		super();
		this.source = source;
		this.body = body;
	}
	
}
