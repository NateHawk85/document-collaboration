package com.hawkins.documentcollaboration.controller;

import com.hawkins.documentcollaboration.domain.Change;
import com.hawkins.documentcollaboration.service.DocumentModificationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SocketsController
{
	private static final int UPDATE_DELAY_IN_MILLISECONDS = 250;
	
	private static boolean isAlreadyRecording = false;
	
	private final DocumentModificationService documentModificationService;
	private final SimpMessagingTemplate template;
	
	public SocketsController(DocumentModificationService documentModificationService, SimpMessagingTemplate template)
	{
		this.documentModificationService = documentModificationService;
		this.template = template;
	}
	
	@MessageMapping("/collaboration")
	@SendTo("/topic/collaboration")
	public void stuff(List<Change> changes)
	{
		changes.forEach(documentModificationService::addChangeToDocument);
		
		if (isAlreadyRecording)
		{
			return;
		}
		
		isAlreadyRecording = true;
		new Thread(() ->
				   {
					   try
					   {
						   Thread.sleep(UPDATE_DELAY_IN_MILLISECONDS);
						   reportToWebSockets();
					   } catch (InterruptedException ie)
					   {
					   } finally
					   {
						   isAlreadyRecording = false;
					   }
				   }).start();
	}
	
	public void reportToWebSockets()
	{
		template.convertAndSend("/topic/someDifferentThing", documentModificationService.applyDocumentChanges());
	}
}