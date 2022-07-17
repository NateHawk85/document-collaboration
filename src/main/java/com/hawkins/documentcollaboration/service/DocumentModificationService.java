package com.hawkins.documentcollaboration.service;

import com.hawkins.documentcollaboration.domain.Change;
import com.hawkins.documentcollaboration.domain.DocumentChange;
import com.hawkins.documentcollaboration.domain.DocumentContext;
import com.hawkins.documentcollaboration.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DocumentModificationService
{
	private final DocumentRepository documentRepository;
	
	public DocumentModificationService(DocumentRepository documentRepository)
	{
		this.documentRepository = documentRepository;
	}
	
	public void addChangeToDocument(Change change)
	{
		DocumentContext documentContext = documentRepository.findDocument();
		Map<String, List<Change>> temporaryChanges = documentContext.getTemporaryChanges();
		List<Change> changesForElement = temporaryChanges.get(change.getElementId());
		
		if (changesForElement == null)
		{
			changesForElement = new ArrayList<>();
		}
		
		changesForElement.add(change);
		
		temporaryChanges.put(change.getElementId(), changesForElement);
	}
	
	public List<DocumentChange> applyDocumentChanges()
	{
		DocumentContext documentContext = documentRepository.findDocument();
		
		List<DocumentChange> changes = documentContext.applyChanges();
		documentRepository.saveDocument(documentContext);
		
		return changes;
	}
}