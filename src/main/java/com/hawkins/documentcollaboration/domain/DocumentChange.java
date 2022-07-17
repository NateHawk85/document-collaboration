package com.hawkins.documentcollaboration.domain;

public class DocumentChange
{
	private String elementId;
	private String newValue;
	
	public String getElementId()
	{
		return elementId;
	}
	
	public void setElementId(String elementId)
	{
		this.elementId = elementId;
	}
	
	public String getNewValue()
	{
		return newValue;
	}
	
	public void setNewValue(String newValue)
	{
		this.newValue = newValue;
	}
}