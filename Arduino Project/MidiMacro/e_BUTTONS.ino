/*Button*/
void debounceBtn(int index) {
  int btnPin = buttons[index];
  int lastButtonState = buttonsLastState[index];
  int buttonState = buttonsState[index];
  boolean isPushed = buttonsIsPushed[index];
  int channel = buttonsChannel[index];
  int control = buttonsControl[index];
  /*
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
  */
  int reading = digitalRead(btnPin);
  
  if (reading != lastButtonState) {
    lastDebounceTime = millis();
  }
  
  if ((millis() - lastDebounceTime) > debounceDelay) {
    if (reading != buttonState) {
      buttonState = reading;
      if (buttonState == HIGH) {
        if (btnPin == encoderSW)
          print(channel, control, VALUE_0);
        else
          print(channel, control, VALUE_127);
        isPushed = true;
      } else {
        if (btnPin == encoderSW)
          print(channel, control, VALUE_127);
        else
          print(channel, control, VALUE_0);
        isPushed = false;
      }
    }
  }

  buttonsLastState[index] =  reading;
  buttonsState[index] = buttonState;
  buttonsIsPushed[index] = isPushed;
}

void debounceLedBtn() {
  int reading = digitalRead(ledBtn);
  if (reading != btnLedLastState) {
    lastDebounceTime = millis();
  }

  if ((millis() - lastDebounceTime) > debounceDelay) {
    if (reading != btnLedState) {
      btnLedState = reading;
      if (btnLedState == HIGH) {
        refreshLeds(!showLeds);
        publishMqttMessageToArduino(showLeds);
      }
    }
  }
  btnLedLastState = reading;
}
