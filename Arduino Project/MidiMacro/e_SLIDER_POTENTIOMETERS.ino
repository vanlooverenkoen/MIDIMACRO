/*SLIDER / Potentiometer*/
int checkSliderPot(int sliderPotPin, int previousValue, int channel, int control) {
  int sensorValue = analogRead(sliderPotPin);
  int realValue = map(sensorValue, 0, 1023, 0, 127);
  if (previousValue != realValue && (previousValue + 1 != realValue && previousValue - 1 != realValue)) {
    print(channel, control, realValue);
    return realValue;
  } else {
    return previousValue;
  }
}
