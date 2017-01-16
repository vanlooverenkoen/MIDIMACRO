#define amountButtons 11
#define amountPots 8

int buttons[amountButtons];
int buttonsLastState[amountButtons];
int buttonsState[amountButtons];
int buttonsIsPushed[amountButtons];
int buttonsChannel[amountButtons];
int buttonsControl[amountButtons];

int potentioMeters[amountPots];
int potentioMetersPreviousValue[amountPots];
int potentioMetersChannel[amountPots];
int potentioMetersControl[amountPots];

void setup() {
  // put your setup code here, to run once:
  showLeds = true;
  greenLed = 0;
  redLed = 0;
  blueLed = 0;
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  setColor(0, 0, 1); //Blue
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
  pinMode(ledBtn, INPUT);
  btnLedLastState = LOW;
  btnLedState = -1;
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

  //Creating Arrays for a cleaner loop
  //Deck A
  buttons[0] = btn1;
  buttons[1] = btn2;
  buttons[2] = btn3;
  buttons[3] = btn4;
  buttons[4] = btnA;
  buttonsLastState[0] = btn1LastState;
  buttonsLastState[1] = btn2LastState;
  buttonsLastState[2] = btn3LastState;
  buttonsLastState[3] = btn4LastState;
  buttonsLastState[4] = btnALastState;
  buttonsState[0] = btn1State;
  buttonsState[1] = btn2State;
  buttonsState[2] = btn3State;
  buttonsState[3] = btn4State;
  buttonsState[4] = btnAState;
  buttonsIsPushed[0] = btn1IsPushed;
  buttonsIsPushed[1] = btn2IsPushed;
  buttonsIsPushed[2] = btn3IsPushed;
  buttonsIsPushed[3] = btn4IsPushed;
  buttonsIsPushed[4] = btnAIsPushed;
  buttonsChannel[0] = btn1Channel;
  buttonsChannel[1] = btn2Channel;
  buttonsChannel[2] = btn3Channel;
  buttonsChannel[3] = btn4Channel;
  buttonsChannel[4] = btnAChannel;
  buttonsControl[0] = btn1Control;
  buttonsControl[1] = btn2Control;
  buttonsControl[2] = btn3Control;
  buttonsControl[3] = btn4Control;
  buttonsControl[4] = btnAControl;

  //Deck B
  buttons[5] = btn5;
  buttons[6] = btn6;
  buttons[7] = btn7;
  buttons[8] = btn8;
  buttons[9] = btnB;
  buttonsLastState[5] = btn5LastState;
  buttonsLastState[6] = btn6LastState;
  buttonsLastState[7] = btn7LastState;
  buttonsLastState[8] = btn8LastState;
  buttonsLastState[9] = btnBLastState;
  buttonsState[5] = btn5State;
  buttonsState[6] = btn6State;
  buttonsState[7] = btn7State;
  buttonsState[8] = btn8State;
  buttonsState[9] = btnBState;
  buttonsIsPushed[5] = btn5IsPushed;
  buttonsIsPushed[6] = btn6IsPushed;
  buttonsIsPushed[7] = btn7IsPushed;
  buttonsIsPushed[8] = btn8IsPushed;
  buttonsIsPushed[9] = btnBIsPushed;
  buttonsChannel[5] = btn5Channel;
  buttonsChannel[6] = btn6Channel;
  buttonsChannel[7] = btn7Channel;
  buttonsChannel[8] = btn8Channel;
  buttonsChannel[9] = btnBChannel;
  buttonsControl[5] = btn5Control;
  buttonsControl[6] = btn6Control;
  buttonsControl[7] = btn7Control;
  buttonsControl[8] = btn8Control;
  buttonsControl[9] = btnBControl;

  //Rotation Btn
  buttons[10] = encoderSW;
  buttonsLastState[10] = encoderBtnLastState;
  buttonsState[10] = encoderBtnState;
  buttonsIsPushed[10] = encoderBtnIsPushed;
  buttonsChannel[10] = encoderBtnChannel;
  buttonsControl[10] = encoderBtnControl;

  //Sliders
  potentioMeters[0] = slider1Pin;
  potentioMeters[1] = slider2Pin;
  potentioMeters[2] = potentiometer1;
  potentioMeters[3] = potentiometer2;
  potentioMeters[4] = potentiometer3;
  potentioMeters[5] = potentiometer4;
  potentioMeters[6] = potentiometer5;
  potentioMeters[7] = potentiometer6;
  potentioMetersPreviousValue[0] = previousValueSlider1;
  potentioMetersPreviousValue[1] = previousValueSlider2;
  potentioMetersPreviousValue[2] = previousValuePotentiometer1;
  potentioMetersPreviousValue[3] = previousValuePotentiometer2;
  potentioMetersPreviousValue[4] = previousValuePotentiometer3;
  potentioMetersPreviousValue[5] = previousValuePotentiometer4;
  potentioMetersPreviousValue[6] = previousValuePotentiometer5;
  potentioMetersPreviousValue[7] = previousValuePotentiometer6;
  potentioMetersChannel[0] =slider1Channel;
  potentioMetersChannel[1] =slider2Channel;
  potentioMetersChannel[2] =pot1Channel;
  potentioMetersChannel[3] =pot2Channel;
  potentioMetersChannel[4] =pot3Channel;
  potentioMetersChannel[5] =pot4Channel;
  potentioMetersChannel[6] =pot5Channel;
  potentioMetersChannel[7] =pot6Channel;
    
  potentioMetersControl[0] =slider1Control;
  potentioMetersControl[1] =slider2Control;
  potentioMetersControl[2] =pot1Control;
  potentioMetersControl[3] =pot2Control;
  potentioMetersControl[4] =pot3Control;
  potentioMetersControl[5] =pot4Control;
  potentioMetersControl[6] =pot5Control;
  potentioMetersControl[7] =pot6Control;
 
  /*Setup Wifi*/
  /*Sets the right pins for the wifi module on the Arduino Mega*/
  WiFi.setPins(8, 7, 4);
  connectWifi();
}
