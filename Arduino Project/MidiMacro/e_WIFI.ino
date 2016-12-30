void callback(char* topic, byte* payload, unsigned int length) {
  payload[length] = '\0';
  String s = String((char*)payload);
  print(topic);
  print(s);
  println();
  s = "";
}

void connectWifi() {
  status = WiFi.begin(ssid, pass);
  if ( status != WL_CONNECTED) {
    // don't do anything else:
    setColor(1, 0, 0);  // red
    print("Could Not Connect ");
    print(status);
    while (true);
  } else {
    print("Connected to WIFI");
    println();
    client.setServer(server, SERVER_PORT);
    client.setCallback(callback);
    if (client.connect("MIDIMACRO_ARDUINO")) {
      client.subscribe(MQTT_TO_HARDWARE);
    }
  }
}

void publishMqttMessage(String message) {
  char msg[50];
  message.toCharArray(msg, 50);
  client.publish(MQTT_TO_SOFTWARE, msg);
}

void lookForInComingMessages() {
  client.loop();
}
