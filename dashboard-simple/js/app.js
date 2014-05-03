var client = new Messaging.Client("localhost", 8000, "dashboard");
var gauge;
$(document).ready(function () {
 gauge = new JustGage({
        id: "gauge",
        value: 0,
        min: -20,
        max: 60,
        title: " ",
        showMinMax: false,
        levelColors: ["#007CFF", "#a9d70b", "#ff0000"],
        levelColorsGradient: false,
        label: "Celsius"
    });
var options = {
        timeout: 3,
        onSuccess: function () {
		client.subscribe("home/status");
		client.subscribe("home/temperature");
        },
        onFailure: function (message) {
            alert("Connection failed");
        }
    };
    client.onMessageArrived = function (message) {
        var topic = message.destinationName;
        if (topic === "home/status") {
           alert(message.payloadString);
        } else if (topic === "home/temperature") {
            setTemperature(message.payloadString);
        }
    };
    client.connect(options);
});
function setTemperature(temp) {
    gauge.refresh(parseFloat(temp));
}