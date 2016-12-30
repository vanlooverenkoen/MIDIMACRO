void loop() {
  //Sliders And Potentiometers
  /*
    for (int i = 0 ; i < 8; i ++) {
      sliderPrevVal[i] = checkSliderPot(sliderPins[i], sliderPrevVal[i], sliderChannels[i], sliderControls[i]);
    }
  */
  /*
    //Slider1
    previousValueSlider1 = checkSliderPot(slider1Pin, previousValueSlider1, slider1Channel, slider1Control);
    //Slider2
    previousValueSlider2 = checkSliderPot(slider2Pin, previousValueSlider2, slider2Channel, slider2Control);
    //Potentiometer 1
    previousValuePotentiometer1 = checkSliderPot(potentiometer1, previousValuePotentiometer1, pot1Channel, pot1Control);
    //Potentiometer 2
    previousValuePotentiometer2 = checkSliderPot(potentiometer2, previousValuePotentiometer2, pot2Channel, pot2Control);
    //Potentiometer 3
    previousValuePotentiometer3 = checkSliderPot(potentiometer3, previousValuePotentiometer3, pot3Channel, pot3Control);
    //Potentiometer 4
    previousValuePotentiometer4 = checkSliderPot(potentiometer4, previousValuePotentiometer4, pot4Channel, pot4Control);
    //Potentiometer 5
    previousValuePotentiometer5 = checkSliderPot(potentiometer5, previousValuePotentiometer5, pot5Channel, pot5Control);
    //Potentiometer 6
    previousValuePotentiometer6 = checkSliderPot(potentiometer6, previousValuePotentiometer6, pot6Channel, pot6Control);
  */
  //Btn 1
  debounceBtn(btn1, btn1Channel, btn1Control);
  //Btn 2
  debounceBtn(btn2, btn2Channel, btn2Control);
  //Btn 3
  debounceBtn(btn3, btn3Channel, btn3Control);
  //Btn 4
  debounceBtn(btn4, btn4Channel, btn4Control);
  //Btn 5
  debounceBtn(btn5, btn5Channel, btn5Control);
  //Btn 6
  debounceBtn(btn6, btn6Channel, btn6Control);
  //Btn 7
  debounceBtn(btn7, btn7Channel, btn7Control);
  //Btn 8
  debounceBtn(btn8, btn8Channel, btn8Control);
  /*
    //Btn Load Track Deck A
    debounceBtn(btnA, btnAChannel, btnAControl);
    //Btn Load Track Deck B
    debounceBtn(btnB, btnBChannel, btnBControl);
    //Btn Reset
    debounceResetBtn();
    //Rotary Encoder Btn
    debounceBtn(encoderSW, encoderBtnChannel, encoderBtnControl);
    //Rotary Encoder
    checkEncoderIncremental();
    //Ultra Sonic
    previousValuesDistance1 = checkUltraSonicDistance(usDistance1Trig, usDistance1Echo, previousValuesDistance1, us1Channel, us1Control);
    //Ultra Sonic 2
    previousValuesDistance2 = checkUltraSonicDistance(usDistance2Trig, usDistance2Echo, previousValuesDistance2, us2Channel, us2Control);
    //Loop the client for  MQTT Messages
    lookForInComingMessages();
    /
  */
}
