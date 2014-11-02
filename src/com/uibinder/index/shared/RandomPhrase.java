package com.uibinder.index.shared;

import java.io.Serializable;

/**
 * 
 * @author Neill
 *
 */
public class RandomPhrase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String randomPhrase;
	String author;
	
	public RandomPhrase(){
		
	}
	
	public RandomPhrase(String randomPhrase, String author){
		this.randomPhrase = randomPhrase;
		this.author = author;
	}
	
	public String getRandomPhrase(){
		return randomPhrase;
	}
	
	public String getAuthor(){
		return author;
	}

}
