/*Encoder*/
void checkEncoderIncremental() {
  encoderState = digitalRead(encoderCLK);
  if (encoderState != encoderLastState) {
    if (digitalRead(encoderDT) != encoderState) {
      encoderCounter++;
      if (encoderCounter % 2 == 0) {
        print(encoderUpAndDownChannel, encoderUpControl, VALUE_127);
        print(encoderUpAndDownChannel, encoderUpControl, VALUE_0);
      }
    } else {
      encoderCounter--;
      if (encoderCounter % 2 == 0) {
        print(encoderUpAndDownChannel, encoderDownControl , VALUE_127);
        print(encoderUpAndDownChannel, encoderDownControl , VALUE_0);
      }
    }
  }
  encoderLastState = encoderState;
}
