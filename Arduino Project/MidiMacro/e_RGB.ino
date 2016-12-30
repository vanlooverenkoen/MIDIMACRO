void setColor(int red, int green, int blue)
{
  if (red == 1)
    digitalWrite(redPin, HIGH);
  else
    digitalWrite(redPin, LOW);
  if (green == 1)
    digitalWrite(greenPin, HIGH);
  else
    digitalWrite(greenPin, LOW);
  if (blue == 1)
    digitalWrite(bluePin, HIGH);
  else
    digitalWrite(bluePin, LOW);
}
