package com.hawkins.documentcollaboration.domain;

import java.util.*;
import java.util.stream.Collectors;

public class DocumentContext
{
	private final Document document = new Document();
	private final Map<String, List<Change>> temporaryChanges = new HashMap<>();
	
	public Map<String, List<Change>> getTemporaryChanges()
	{
		return temporaryChanges;
	}
	
	public List<DocumentChange> applyChanges()
	{
		List<DocumentChange> documentChanges = new ArrayList<>();
		
		Set<String> changedIds = new HashSet<>();
		temporaryChanges.forEach((elementId, changes) ->
								 {
									 // current location does not already exist, initialize it
									 document.getElementIdValueMap().putIfAbsent(elementId, "");
			
									 for (int i = 0; i < changes.size(); i++)
									 {
										 processChange(changes, i);
									 }
			
									 changedIds.add(elementId);
								 });
		
		addChanges(changedIds, documentChanges);
		
		temporaryChanges.clear();
		
		return documentChanges;
	}
	
	// TODO - can get quite buggy with collaboration. Look into it
	private void processChange(List<Change> changes, int index)
	{
		Change change = changes.get(index);
		
		String currentValueAtElement = document.getElementIdValueMap().get(change.getElementId());
		
		if (change.getKey().length() == 1)
		{
			// TODO - lots of incorrect substrings when collaboratively editing. May have to save state per client, but that could get messy
			String updatedValue =
					currentValueAtElement.substring(0, change.getSelectionStartIndex()) +
					change.getKey() +
					currentValueAtElement.substring(change.getSelectionEndIndex());
			document.getElementIdValueMap().put(change.getElementId(), updatedValue);
			
		} else if ("Backspace".equals(change.getKey()))
		{
			String updatedValue =
					currentValueAtElement.substring(0, change.getSelectionStartIndex()) +
					currentValueAtElement.substring(change.getSelectionEndIndex());
			document.getElementIdValueMap().put(change.getElementId(), updatedValue);
		} else // TODO - special characters? Should be ignored by frontend, but still. Gotta validate
		{
			System.out.println("DOING NOTHING - ");
			System.out.println("Element id: " + change.getElementId());
			System.out.println("Key: " + change.getKey());
			System.out.println("Index range: " + change.getSelectionStartIndex() + " - " + change.getSelectionEndIndex());
		}
		
		
		// if key is character or symbol, add it
		// if key is backspace, remove character at index
	}
	
	private void addChanges(Set<String> changedIds, List<DocumentChange> documentChanges)
	{
		changedIds.forEach(elementId ->
						   {
							   DocumentChange documentChange = new DocumentChange();
							   documentChange.setElementId(elementId);
							   documentChange.setNewValue(document.getElementIdValueMap().get(elementId));
							   documentChanges.add(documentChange);
						   });
	}
	
	
	private Map<String, List<Character>> copyDocumentChanges(Map<String, List<Character>> documentChanges)
	{
		return documentChanges.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, e -> List.copyOf(e.getValue())));
	}
}