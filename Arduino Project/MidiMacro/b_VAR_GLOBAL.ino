/*Wifi Variables*/
#define ssid "MIDIMACRO"
#define pass "midimacropasswd"
uint8_t status = WL_IDLE_STATUS;
WiFiClient wifiClient;

/*MQTT Variables*/
#define server "your_ip"
#define MQTT_TO_SOFTWARE "MIDIMACRO_TO_SOFTWARE"
#define MQTT_TO_HARDWARE "MIDIMACRO_TO_HARDWARE_ARDUINO"
#define MQTT_TO_HARDWARE_ANDROID "MIDIMACRO_TO_HARDWARE_ANDROID"
#define SERVER_PORT 1883
PubSubClient client(wifiClient);

/*DEBOUNCE Values*/
#define debounceDelay 30
unsigned long  lastDebounceTime;

/*Slider 1 Values*/
#define slider1Pin A1
int previousValueSlider1;
#define slider1Channel CHANNEL_2
#define slider1Control CONTROL_0

/*Slider 2 Values*/
#define slider2Pin A0
int previousValueSlider2;
#define slider2Channel CHANNEL_2
#define slider2Control CONTROL_1

/*Potentiometer 1 Values*/
#define potentiometer1 A13
int previousValuePotentiometer1;
#define pot1Channel CHANNEL_2
#define pot1Control CONTROL_2

/*Potentiometer 2 Values*/
#define potentiometer2 A12
int previousValuePotentiometer2;
#define pot2Channel CHANNEL_2
#define pot2Control CONTROL_3

/*Potentiometer 3 Values*/
#define potentiometer3 A11
int previousValuePotentiometer3;
#define pot3Channel CHANNEL_2
#define pot3Control CONTROL_4

/*Potentiometer 4 Values*/
#define potentiometer4 A10
int previousValuePotentiometer4;
#define pot4Channel CHANNEL_2
#define pot4Control CONTROL_5

/*Potentiometer 5 Values*/
#define potentiometer5 A9
int previousValuePotentiometer5;
#define pot5Channel CHANNEL_2
#define pot5Control CONTROL_6

/*Potentiometer 6 Values*/
#define potentiometer6 A8
int previousValuePotentiometer6;
#define pot6Channel CHANNEL_2
#define pot6Control CONTROL_7

/*Btn 1 Values*/
#define btn1 31
int btn1LastState;
int btn1State;
boolean btn1IsPushed;
#define btn1Channel CHANNEL_1
#define btn1Control CONTROL_0

/*Btn 2 Values*/
#define btn2 30
int btn2LastState;
int btn2State;
boolean btn2IsPushed;
#define btn2Channel CHANNEL_1
#define btn2Control CONTROL_1

/*Btn 3 Values*/
#define btn3 28
int btn3LastState;
int btn3State;
boolean btn3IsPushed;
#define btn3Channel CHANNEL_1
#define btn3Control CONTROL_2

/*Btn 4 Values*/
#define btn4 29
int btn4LastState;
int btn4State;
boolean btn4IsPushed;
#define btn4Channel CHANNEL_1
#define btn4Control CONTROL_3

/*Btn 5 Values*/
#define btn5 26
int btn5LastState;
int btn5State;
boolean btn5IsPushed;
#define btn5Channel CHANNEL_1
#define btn5Control CONTROL_4

/*Btn 6 Values*/
#define btn6 27
int btn6LastState;
int btn6State;
boolean btn6IsPushed;
#define btn6Channel CHANNEL_1
#define btn6Control CONTROL_5

/*Btn 7 Values*/
#define btn7 25
int btn7LastState;
int btn7State;
boolean btn7IsPushed;
#define btn7Channel CHANNEL_1
#define btn7Control CONTROL_6

/*Btn 8 Values*/
#define btn8 24
int btn8LastState;
int btn8State;
boolean btn8IsPushed;
#define btn8Channel CHANNEL_1
#define btn8Control CONTROL_7

/*Btn A Values*/
#define btnA 48
int btnALastState;
int btnAState;
boolean btnAIsPushed;
#define btnAChannel CHANNEL_1
#define btnAControl CONTROL_8

/*Btn B Values*/
#define btnB 49
int btnBLastState;
int btnBState;
boolean btnBIsPushed;
#define btnBChannel CHANNEL_1
#define btnBControl CONTROL_9

#define ledBtn 22
int btnLedLastState;
int btnLedState;

/*Rotary Encoder Btn Values*/
#define encoderSW 47
int encoderBtnLastState;
int encoderBtnState;
boolean encoderBtnIsPushed;
#define encoderBtnChannel CHANNEL_1
#define encoderBtnControl CONTROL_10

/*Rotary Encoder Incremental Values*/
#define encoderCLK 11
#define encoderDT 12
int encoderCounter;
int lastEncoderCounter;
int encoderState;
int encoderLastState;
#define encoderUpAndDownChannel CHANNEL_1
#define encoderUpControl CONTROL_11
#define encoderDownControl CONTROL_12

/*Ultra Sonic Distance 1 Values*/
#define usDistance1Trig 10
#define usDistance1Echo 9
int previousValuesDistance1;
#define us1Channel CHANNEL_3
#define us1Control CONTROL_1

/*Ultra Sonic Distance 2 Values*/
#define usDistance2Trig 3
#define usDistance2Echo 2
int previousValuesDistance2;
#define us2Channel CHANNEL_3
#define us2Control CONTROL_2

/*RGB Led*/
bool showLeds;
int redLed;
int blueLed;
int greenLed;
#define redPin 34
#define greenPin 37
#define bluePin 36

/*WIFI*/
bool isPubsubConnected;
