$(function onReady() { readPort(); fetchTemperatureReadings(); });

function writePort(val) {
	$.get("/writePort", {value : val}, function(data) { $("#status").text(data["status"]); })
		.fail(function() { $("#status").text("Failed to write port!"); });
}

function readPort() {
	$.get("/readPort", {}, function(data) { $("#status").text(data["status"]); })
		.fail(function() { $("#status").text("Failed to read port!"); });
}

function fetchTemperatureReadings() {
	$.get("/readTemp", {}, function(data) {
		ul = $("#readings");
		ul.html("");
		ul.appendTo("body");
		$(data).each(function (index, item) {
			ul.append($(document.createElement("li"))
				.text(item["name"] + ": " + item["celsius"] + "C"));
		});
	});
}
