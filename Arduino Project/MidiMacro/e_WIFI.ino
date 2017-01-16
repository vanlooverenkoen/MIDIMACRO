void callback(char* topic, byte* payload, unsigned int length) {
  payload[length] = '\0';
  String s = String((char*)payload);
  if (s.equals("NL")) {
    refreshLeds(false);
  } else if (s.equals("L")) {
    refreshLeds(true);
  }
}

void connectWifi() {
  status = WiFi.begin(ssid, pass);
  if (isConnected()) {
    print("Connected to WIFI");
    println();
    client.setServer(server, SERVER_PORT);
    client.setCallback(callback);
    if (client.connect("MIDIMACRO_ARDUINO")) {
      client.subscribe(MQTT_TO_HARDWARE);
      /*Led lights up to show that program is ready to load*/
      isPubsubConnected = true;
      setColor(0, 1, 0);  // green
    } else {
      isPubsubConnected = false;
      setColor(1, 0, 1);  // purple
    }
  } else {
    // don't do anything else:
    setColor(1, 0, 0);  // red
  }
}

bool isConnected() {
  if (status == WL_CONNECTED)
    return true;
  else
    return false;
}

void publishMqttMessage(String message) {
  char msg[50];
  message.toCharArray(msg, 50);
  client.publish(MQTT_TO_SOFTWARE, msg);
  client.publish(MQTT_TO_HARDWARE_ANDROID, msg);
}


void publishMqttMessageToArduino(boolean ledsOnOff) {
  String message;
  if (ledsOnOff)
    message = "L";
  else
    message = "NL";
  char msg[3];
  message.toCharArray(msg, 3);
  client.publish(MQTT_TO_HARDWARE_ANDROID, msg);
}

void lookForInComingMessages() {
  isPubsubConnected = client.loop();
}
