var stompClient = null;
var currentKeyStrokes = [];
var isRecordingInput = false;
var isConnected = false;

var modifierKeysPressed = {
	ctrlKey: false,
	altKey: false,
	shiftKey: false
};

function setConnected(connected) {
	isConnected = connected;
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	} else {
		$("#conversation").hide();
	}
}

function connect() {
	var socket = new SockJS('/gs-guide-websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, (frame) => {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/someDifferentThing', (changes) => {
			showChanges(JSON.parse(changes.body));
		});
		stompClient.subscribe('/topic/collaboration', (keystrokes) => {
			console.log("SOMETHING")
		});
	});
}

function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function sendUpdatesToCollaborationServer(keystrokes) {
	stompClient.send("/sockets/collaboration", {}, JSON.stringify(keystrokes))
}

function showChanges(changes) {
	changes.forEach(change => {
		$("#" + change.elementId).val(change.newValue);
	});
}

$(() => {
	$("form").on('submit', (e) => {
		e.preventDefault();
	});
	$("#connect").click(() => {
		connect();
	});
	$("#disconnect").click(() => {
		disconnect();
	});
	const $inputLocations = $(".input-location");
	$inputLocations.keydown((event) => {
		// TODO - don't send on paste, you'll have to handle that separately
		updateModifierKeys(event);

		// do not record modified inputs
		if (modifierKeysPressed.altKey || modifierKeysPressed.ctrlKey) {
			return;
		}

		currentKeyStrokes.push({
			"elementId": event.target.id,
			"selectionStartIndex": event.target.selectionStart,
			"selectionEndIndex": event.target.selectionEnd,
			"key": event.key,
		});

		if (isRecordingInput) {
			return;
		}

		isRecordingInput = true;
		setTimeout(() => {
			if (isConnected) {
				sendUpdatesToCollaborationServer(currentKeyStrokes);
				currentKeyStrokes = [];
			}
			isRecordingInput = false;
		}, 100);
	});
	$inputLocations.keyup((event) => updateModifierKeys(event));
});

function updateModifierKeys(event) {
	for (const key in modifierKeysPressed) {
		if (modifierKeysPressed.hasOwnProperty(key)) {
			modifierKeysPressed[key] = event[key];
		}
	}
}

// TODO - for testing
function showPressedCharacter(event) {
	console.log("STUFF: " + event.key);
	if (String.fromCharCode(event.keyCode).match(/(\w|\s)/g)) {
		//pressed key is a char
		console.log("CHARACTER");
	} else {
		//pressed key is a non-char
		//e.g. 'esc', 'backspace', 'up arrow'
		console.log("NOT CHARACTER");
	}
}