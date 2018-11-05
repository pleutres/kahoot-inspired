package com.gorki.dto;

import java.util.Date;

public class OutputMessage extends Message {

	private Date time;
	
	public OutputMessage(Message original) {
		super(original.getId(), original.getType(), original.getMessage());
		this.time = time;
		this.time = new Date();
	}
	
	public Date getTime() {
		return time;
	}
	
	public void setTime(Date time) {
		this.time = time;
	}
}
