int checkUltraSonicDistance(int trigPin, int echoPin, int previousValue, int channel, int control) {
  long duration, distance;
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);
  distance = (duration / 2) / 29.1;
  if (distance <= 20 && distance >= 0) {
    int realValue = map(distance, 0, 20, 0, 127);
    if (previousValue != -1 && (realValue - previousValue) < 30) {
      print(channel,control, realValue);
    }
    return realValue;
  }
  return previousValue;
}

