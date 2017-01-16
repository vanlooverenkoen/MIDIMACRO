void print(int value) {
  print(String(value));
}

void print(int channel, int control, int value) {
  String message = String();
  message += channel;
  message += " - ";
  message += control;
  message += " - ";
  message += value;
  message += " - FROM ARDUINO";
  publishMqttMessage(message);
  print(message);
  println();
}

void print(String value) {
  //Serial.print(value);
}

void println() {
  //Serial.println();
}
