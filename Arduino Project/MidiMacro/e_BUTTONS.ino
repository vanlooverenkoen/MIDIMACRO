/*Button*/
void debounceBtn(int btnPin, int channel, int control) {
  int lastButtonState = LOW;
  int buttonState = LOW;
  boolean isPushed = false;
  switch (btnPin) {
    case encoderSW:
      lastButtonState = encoderBtnLastState;
      buttonState = encoderBtnState;
      isPushed = encoderBtnIsPushed;
      break;
    case btn1:
      lastButtonState = btn1LastState;
      buttonState = btn1State;
      isPushed = btn1IsPushed;
      break;
    case btn2:
      lastButtonState = btn2LastState;
      buttonState = btn2State;
      isPushed = btn2IsPushed;
      break;
    case btn3:
      lastButtonState = btn3LastState;
      buttonState = btn3State;
      isPushed = btn3IsPushed;
      break;
    case btn4:
      lastButtonState = btn4LastState;
      buttonState = btn4State;
      isPushed = btn4IsPushed;
      break;
    case btn5:
      lastButtonState = btn5LastState;
      buttonState = btn5State;
      isPushed = btn5IsPushed;
      break;
    case btn6:
      lastButtonState = btn6LastState;
      buttonState = btn6State;
      isPushed = btn6IsPushed;
      break;
    case btn7:
      lastButtonState = btn7LastState;
      buttonState = btn7State;
      isPushed = btn7IsPushed;
      break;
    case btn8:
      lastButtonState = btn8LastState;
      buttonState = btn8State;
      isPushed = btn8IsPushed;
      break;
    case btnA:
      lastButtonState = btnALastState;
      buttonState = btnAState;
      isPushed = btnAIsPushed;
      break;
    case btnB:
      lastButtonState = btnBLastState;
      buttonState = btnBState;
      isPushed = btnBIsPushed;
      break;
  }

  int reading = digitalRead(btnPin);
  if (reading != lastButtonState) {
    lastDebounceTime = millis();
  }

  if ((millis() - lastDebounceTime) > debounceDelay) {
    if (reading != buttonState) {
      buttonState = reading;
      if (buttonState == HIGH) {
        if (isPushed) {
          print(channel, control, VALUE_0);
          //print("Pushed the button\n");
          //MIDI.sendControlChange(CONTROL_0, VALUE_0, CHANNEL_1);
          isPushed = false;
        } else {
          print(channel, control, VALUE_127);
          //print("Released the button\n");
          //MIDI.sendControlChange(CONTROL_0, VALUE_127, CHANNEL_1);
          isPushed = true;
        }
      }
    }
  }

  switch (btnPin) {
    case encoderSW:
      encoderBtnLastState = reading;
      encoderBtnState = buttonState;
      encoderBtnIsPushed = isPushed;
      break;
    case btn1:
      btn1LastState = reading;
      btn1State = buttonState;
      btn1IsPushed = isPushed;
      break;
    case btn2:
      btn2LastState = reading;
      btn2State = buttonState;
      btn2IsPushed = isPushed;
      break;
    case btn3:
      btn3LastState = reading;
      btn3State = buttonState;
      btn3IsPushed = isPushed;
      break;
    case btn4:
      btn4LastState = reading;
      btn4State = buttonState;
      btn4IsPushed = isPushed;
      break;
    case btn5:
      btn5LastState = reading;
      btn5State = buttonState;
      btn5IsPushed = isPushed;
      break;
    case btn6:
      btn6LastState = reading;
      btn6State = buttonState;
      btn6IsPushed = isPushed;
      break;
    case btn7:
      btn7LastState = reading;
      btn7State = buttonState;
      btn7IsPushed = isPushed;
      break;
    case btn8:
      btn8LastState = reading;
      btn8State = buttonState;
      btn8IsPushed = isPushed;
      break;
    case btnA:
      btnALastState = reading;
      btnAState = buttonState;
      btnAIsPushed = isPushed;
      break;
    case btnB:
      btnBLastState = reading;
      btnBState = buttonState;
      btnBIsPushed = isPushed;
      break;
  }
}

void debounceResetBtn() {
  int reading = digitalRead(resetBtn);

  if (reading != btnResetLastState) {
    // reset the debouncing timer
    lastDebounceTime = millis();
  }

  if ((millis() - lastDebounceTime) > debounceDelay) {
    // whatever the reading is at, it's been there for longer
    // than the debounce delay, so take it as the actual current state:

    // if the button state has changed:
    if (reading != btnResetState) {
      btnResetState = reading;

      // only toggle the LED if the new button state is HIGH
      if (btnResetState == LOW) {
        print("Pressed");
        digitalWrite(resetPin, LOW);
        delay(200);
        digitalWrite(resetPin, HIGH);
        delay(200);
      }
      println();
    }
  }
  btnResetLastState = reading;
}
