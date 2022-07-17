package com.hawkins.documentcollaboration.domain;

public class Change
{
	private String elementId;
	private String key;
	private int selectionStartIndex;
	private int selectionEndIndex;
	
	public String getElementId()
	{
		return elementId;
	}
	
	public void setElementId(String elementId)
	{
		this.elementId = elementId;
	}
	
	public String getKey()
	{
		return key;
	}
	
	public void setKey(String key)
	{
		this.key = key;
	}
	
	public int getSelectionStartIndex()
	{
		return selectionStartIndex;
	}
	
	public void setSelectionStartIndex(int selectionStartIndex)
	{
		this.selectionStartIndex = selectionStartIndex;
	}
	
	public int getSelectionEndIndex()
	{
		return selectionEndIndex;
	}
	
	public void setSelectionEndIndex(int selectionEndIndex)
	{
		this.selectionEndIndex = selectionEndIndex;
	}
}