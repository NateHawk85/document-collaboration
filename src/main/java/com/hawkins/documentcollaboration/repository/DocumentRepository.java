package com.hawkins.documentcollaboration.repository;

import com.hawkins.documentcollaboration.domain.DocumentContext;
import org.springframework.stereotype.Service;

@Service
public class DocumentRepository
{
	private static final DocumentContext DOCUMENT = new DocumentContext();
	
	// TODO - could eventually expand to multiple documents or whatever
	public DocumentContext findDocument()
	{
		return DOCUMENT;
	}
	
	public void saveDocument(DocumentContext documentContext)
	{
		// TODO - save stuff
	}
}