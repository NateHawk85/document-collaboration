package com.hawkins.documentcollaboration.domain;

import java.util.HashMap;
import java.util.Map;

public class Document
{
	private final Map<String, String> elementIdValueMap = new HashMap<>();
	
	public Map<String, String> getElementIdValueMap()
	{
		return elementIdValueMap;
	}
}