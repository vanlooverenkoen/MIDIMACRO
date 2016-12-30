/*
int sliderPins[8];
int sliderPrevVal[8];
int sliderChannels[8];
int sliderControls[8];
*/
void setup() {
  // put your setup code here, to run once:
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  setColor(0, 0, 1); //Blue
  //Setting the resetPin to High, otherwise the board will not boot up
  digitalWrite(resetPin, HIGH);
  delay(200);
  /*Default Settings*/
  lastDebounceTime = 0;
  /*For testing*/
  Serial.begin (9600);
  /*Build in led*/
  pinMode(LED_BUILTIN, OUTPUT);
  /*Setup Slider1*/
  pinMode(slider1Pin, INPUT);
  previousValueSlider2 = -1;
  /*Setup Slider2*/
  pinMode(slider2Pin, INPUT);
  previousValueSlider2 = -1;
  /*Setup Btn 1*/
  pinMode(btn1, INPUT);
  btn1LastState = -1;
  btn1State = -1;
  btn1IsPushed = false;
  /*Setup Btn 2*/
  pinMode(btn2, INPUT);
  btn2LastState = -1;
  btn2State = -1;
  btn2IsPushed = false;
  /*Setup Btn 3*/
  pinMode(btn3, INPUT);
  btn3LastState = -1;
  btn3State = -1;
  btn3IsPushed = false;
  /*Setup Btn 4*/
  pinMode(btn4, INPUT);
  btn4LastState = -1;
  btn4State = -1;
  btn4IsPushed = false;
  /*Setup Btn 5*/
  pinMode(btn5, INPUT);
  btn5LastState = -1;
  btn5State = -1;
  btn5IsPushed = false;
  /*Setup Btn 6*/
  pinMode(btn6, INPUT);
  btn6LastState = -1;
  btn6State = -1;
  btn6IsPushed = false;
  /*Setup Btn 7*/
  pinMode(btn7, INPUT);
  btn7LastState = -1;
  btn7State = -1;
  btn7IsPushed = false;
  /*Setup Btn 8*/
  pinMode(btn8, INPUT);
  btn8LastState = -1;
  btn8State = -1;
  btn8IsPushed = false;
  /*Setup Btn A*/
  pinMode(btnA, INPUT);
  btnALastState = -1;
  btnAState = -1;
  btnAIsPushed = false;
  /*Setup Btn B*/
  pinMode(btnB, INPUT);
  btnBLastState = -1;
  btnBState = -1;
  btnBIsPushed = false;
  /*Setup Btn Reset*/
  pinMode(resetBtn, INPUT);
  pinMode(resetPin, OUTPUT);
  btnResetLastState = LOW;
  btnResetState = -1;
  /*Setup Rotary Btn*/
  pinMode(encoderSW, INPUT);
  encoderBtnLastState = -1;
  encoderBtnState = -1;
  encoderBtnIsPushed = false;
  digitalWrite(encoderSW, HIGH);
  /*Setup Rotary Incremental*/
  pinMode(encoderCLK, INPUT);
  pinMode(encoderDT, INPUT);
  encoderCounter = 0;
  encoderLastState = -1;
  /*Setup Potentiometer 1 */
  pinMode(potentiometer1, INPUT);
  previousValuePotentiometer1 = -1;
  /*Setup Potentiometer 2 */
  pinMode(potentiometer2, INPUT);
  previousValuePotentiometer2 = -1;
  /*Setup Potentiometer 3 */
  pinMode(potentiometer3, INPUT);
  previousValuePotentiometer3 = -1;
  /*Setup Potentiometer 4 */
  pinMode(potentiometer4, INPUT);
  previousValuePotentiometer4 = -1;
  /*Setup Potentiometer 5 */
  pinMode(potentiometer5, INPUT);
  previousValuePotentiometer5 = -1;
  /*Setup Potentiometer 6 */
  pinMode(potentiometer6, INPUT);
  previousValuePotentiometer6 = -1;
  /*Setup UltraSonic Distance 1 */
  pinMode(usDistance1Trig, OUTPUT);
  pinMode(usDistance1Echo, INPUT);
  previousValuesDistance1 = -1;
  /*Setup UltraSonic Distance 2 */
  pinMode(usDistance2Trig, OUTPUT);
  pinMode(usDistance2Echo, INPUT);
  previousValuesDistance2 = -1;

  /*Setup Midi*/
  //MIDI.begin(MIDI_CHANNEL_OMNI);

  /*Setup Wifi*/
  /*Sets the right pins for the wifi module on the Arduino Mega*/
  WiFi.setPins(8, 7, 4);
  connectWifi();

/*
  sliderPins = {slider1Pin
                          , slider2Pin
                          , potentiometer1
                          , potentiometer2
                          , potentiometer3
                          , potentiometer4
                          , potentiometer5
                          , potentiometer6
                         };

  sliderPrevVal = {previousValueSlider1
                             , previousValueSlider2
                             , previousValuePotentiometer1
                             , previousValuePotentiometer2
                             , previousValuePotentiometer3
                             , previousValuePotentiometer4
                             , previousValuePotentiometer5
                             , previousValuePotentiometer6
                            };
  sliderChannels = {slider1Channel
                              , slider2Channel
                              , pot1Channel
                              , pot2Channel
                              , pot3Channel
                              , pot4Channel
                              , pot5Channel
                              , pot6Channel
                             };
  sliderControls = {slider1Control
                              , slider2Control
                              , pot1Control
                              , pot2Control
                              , pot3Control
                              , pot4Control
                              , pot5Control
                              , pot6Control
                             };
*/

  print("MIDUINO Ready\n");
  /*Led lights up to show that program is ready to load*/
  for (int i = 0 ; i < 4; i ++) {
    digitalWrite(LED_BUILTIN, LOW);
    delay(100);
    digitalWrite(LED_BUILTIN, HIGH);
    delay(100);
  }
  digitalWrite(LED_BUILTIN, LOW);
  setColor(0, 1, 0);  // green
}
