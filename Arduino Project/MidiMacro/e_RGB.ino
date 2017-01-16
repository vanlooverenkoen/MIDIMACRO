void setColor(int red, int green, int blue)
{
  blueLed = blue;
  redLed = red;
  greenLed = green;
  if (red == 1) {
    if (showLeds) {
      digitalWrite(redPin, HIGH);
    } else {
      digitalWrite(redPin, LOW);
    }
  } else
    digitalWrite(redPin, LOW);
  if (green == 1) {
    if (showLeds) {
      digitalWrite(greenPin, HIGH);
    } else {
      digitalWrite(greenPin, LOW);
    }
  } else
    digitalWrite(greenPin, LOW);
  if (blue == 1) {
    if (showLeds) {
      digitalWrite(bluePin, HIGH);
    } else {
      digitalWrite(bluePin, LOW);
    }
  } else
    digitalWrite(bluePin, LOW);
}

void refreshLeds(bool showLedsChange) {
  showLeds = showLedsChange;
  setColor(redLed, greenLed, blueLed);
}

