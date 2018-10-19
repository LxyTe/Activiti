package com.dist.processvariabletest;

import java.io.Serializable;

public class Person  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	  private int day;
	  private String name;
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Person [day=" + day + ", name=" + name + "]";
	}
	public Person(int day, String name) {
		super();
		this.day = day;
		this.name = name;
	}
	  public Person() {
		  
	  }
	  
	  
}

