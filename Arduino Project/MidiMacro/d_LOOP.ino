void loop() {
  status = WiFi.status();
  if (isConnected() && isPubsubConnected) {
    //Loop For all buttons
    for (int i = 0; i < amountPots; i++) {
      potentioMetersPreviousValue[i] = checkSliderPot(potentioMeters[i], potentioMetersPreviousValue[i], potentioMetersChannel[i], potentioMetersControl[i]);
    }
    //Loop For all buttons
    for (int i = 0; i < amountButtons; i++) {
      debounceBtn(i);
    }
    //Rotary Encoder
    checkEncoderIncremental();
    //Ultra Sonic
    previousValuesDistance1 = checkUltraSonicDistance(usDistance1Trig, usDistance1Echo, previousValuesDistance1, us1Channel, us1Control);
    //Ultra Sonic 2
    previousValuesDistance2 = checkUltraSonicDistance(usDistance2Trig, usDistance2Echo, previousValuesDistance2, us2Channel, us2Control);
    //Loop the client for  MQTT Messages
    lookForInComingMessages();
  } else {
    setColor(1, 0, 0);
  }
  //Btn Led
  debounceLedBtn();
}
