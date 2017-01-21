$(function onReady() { readPort(); });

function writePort(val) {
	$.get("/writePort", {value : val}, function(data) { $("#status").text(data); })
		.fail(function() { $("#status").text("Failed to write port!"); });
}

function readPort() {
	$.get("/readPort", {}, function(data) { $("#status").text(data); })
		.fail(function() { $("#status").text("Failed to read port!"); });
}
