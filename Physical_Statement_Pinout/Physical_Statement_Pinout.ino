/********************************************************************
 * STATEMENT OF THE PHYSICAL PINOUT
********************************************************************/
#define PinIR 14                // pinout of the front IR sensor
#define PinUltrasoundTrig 9     // pinout trigger right side ultrasound
#define PinUltrasoundEcho 10    // pinout right side ultrasound echo
#define PinActuator 6           // pinout actuator front side scanning
#define PinTX 1                 // Transmission pin series
#define PinRX 0                 // Receive pin series
#define PinPWMMotorRight 5      // Right motor PWM pinout
#define PinPWMMotorLeft 3       // Left motor PWM pinout
#define PinDigitalMotorRight1 13  // Enable pin 1 right motor
#define PinDigitalMotorRight2 8   // Enable pin 2 right motor
#define PinDigitalMotorLeft1 12   // Enable pin 1 left motor
#define PinDigitalMotorLeft2 7    // Enable pin 2 left motor

/********************************************************************
 * GLOBAL VARIABLES
********************************************************************/
boolean mode;               // Operating mode: Auto = true, Manual = false
int infraRedRemote;         // Distance given by the infra-red sensor
int ultrasoundRemote;       // Distance given by the ultrasound sensor
int scanAngle;              // Sweep angle of the actuator
int rightMotorDutyCycle;    // Duty cycle of the right motor
int leftMotorDutyCycle;     // Duty cycle of the left motor
int moveDirectionRightMotor;  // Right motor direction
int moveDirectionLeftMotor;   // Left motor direction

int incChar = 0;

void setup() {
  Serial.begin(9600);
  pinMode(PinUltrasoundEcho, INPUT); 
  pinMode(PinUltrasoundTrig, OUTPUT);
}

void loop() {
  ultrasonic();
  infrared();
  actuator();
  engine();
  communication();
  Serial.println("\n");
  delay(100);
}

/********************************************************************
 * TREATEMENT FROM THE ULTRASONIC DISTANCE SENSOR
********************************************************************/
void ultrasonic() {
  long duration;  
  digitalWrite(PinUltrasoundTrig, LOW);
  delayMicroseconds(2);
  digitalWrite(PinUltrasoundTrig, HIGH);
  delayMicroseconds(10);
  digitalWrite(PinUltrasoundTrig, LOW);
  duration = pulseIn(PinUltrasoundEcho, HIGH);
  ultrasoundRemote = (duration/2) / 29.1;
  //Serial.print(ultrasoundRemote);  
  //Serial.print(" cm    ");
}

/********************************************************************
 * TREATEMENT FROM THE INFRARED DISTANCE SENSOR
********************************************************************/
void infrared() {
  infraRedRemote = 10;
}

/********************************************************************
 * ACTUATOR ANGLE MANAGEMENT
********************************************************************/
void actuator() {
  
}

/********************************************************************
 * ENGINE MANAGEMENT
********************************************************************/
void engine() {
  
}

/********************************************************************
 * COMMUNICATION: SENDING SENSOR DISTANCE AND SCAN ANGLE
 * Ultrasonic:  "$0A$"
 * Infrared:    "$1A$"
 * ScanAngle:   "$2A$"
********************************************************************/
void communication() {
  Serial.print("$0");
  Serial.print(ultrasoundRemote);
  Serial.print("$");
  Serial.print("$1");
  Serial.print(infraRedRemote);
  Serial.print("$");
  Serial.print("$2");
  Serial.print(scanAngle);
  Serial.print("$");
}

/********************************************************************
 * PROCESSING OF DATA FROM SERIAL DATA
 * FRAME RECEIVE EXAMPLE:   "$0A&" = "$TYPE;DCR;DCL$"
 *    0 = Changing the operating mode
 *            0 = Manual  1 = Auto
 *    1 = Changing the right engine duty cycle
 *            A = value of duty cycle
 *    2 = Changing the left engine duty cycle
 *            A = value of duty cycle
 *    3 = Changing the right engine movement direction
 *            A = sense
 *    4 = Changing the left engine movement direction
 *            A = sense
*********************************************************************/
void serialEvent() {
    /* PROGRESS VARIABLE IN THE FRAME */
    static boolean frameStart = false;
    static boolean frameSelectType = false;
    static boolean frameGetValue = false;
    static boolean frameFinish = false;
    static boolean frameValid = false;

    /* DATA VARIABLE IN THE FRAME */
    static unsigned char type;
    static unsigned char value;
    static int increment;

    /* FRAME DECODING */
    unsigned char c = Serial.read();
    
    if(frameStart == false) {
      if(c == '$') {
        frameStart = true;
        increment = 0;
        Serial.println("$ detect");
      } else frameFinish = true;
    } else {
      increment++;
      if(frameSelectType == false) {
        type = c;
        if(type == '0') {
          Serial.println("Changing the operating mode");
          frameSelectType = true;
        } else if(type == '1') {
          Serial.println("Changing the right engine duty cycle");
          frameSelectType = true;
        } else if(type == '2') {
          Serial.println("Changing the left engine duty cycle");
          frameSelectType = true;
        } else if(type == '3') {
        Serial.println("Changing the right engine movement direction");
        frameSelectType = true;
        } else if(type == '4') {
        Serial.println("Changing the left engine movement direction");
        frameSelectType = true;
        } else {
          Serial.println("TYPE ERROR");
          frameFinish = true;
        }
      } else if(frameGetValue ==  false) {
        value = c;
        Serial.print("Value save: ");
        Serial.println(value);
        frameGetValue = true;
      } else if(frameFinish == false) {
        if(c == '$') {
          frameValid = true;
          Serial.println("The Frame Is Valid");
        } else Serial.println("The Frame Isn't Valid");
        frameFinish = true;
      }
    }

    if((frameFinish == true) || (increment == 3)) {
      if(frameValid == true) {
        Serial.println("DATA UPDATE...");
        switch (type) {
          case '0':
            /* MODE UPDATE */
            if(value == '0') {
              mode = false;
              Serial.println("MANUAL OPERATING MODE");
            } else {
              mode = true;
              Serial.println("AUTO OPERATING MODE");
            }
            break;
          case '1':
            /* DUTY CYCLE RIGHT ENGINE UPDATE */
            rightMotorDutyCycle = value;
            Serial.print("DUTY CYCLE RIGHT: ");
            Serial.println(rightMotorDutyCycle);
            break;
          case '2':
            /* DUTY CYCLE LEFT ENGINE UPDATE */
            leftMotorDutyCycle = value;
            Serial.print("DUTY CYCLE LEFT: ");
            Serial.println(leftMotorDutyCycle);
            break;
          case '3':
            /* MOVEMENT DIRECTION RIGHT ENGINE UPDATE */
            moveDirectionRightMotor = value;
            Serial.print("MOVEMENT DIRECTION RIGHT ENGINE: ");
            Serial.println(leftMotorDutyCycle);
            break;
          case '4':
            /* MOVEMENT DIRECTION LEFT ENGINE UPDATE */
            moveDirectionLeftMotor = value;
            Serial.print("MOVEMENT DIRECTION LEFT ENGINE: ");
            Serial.println(leftMotorDutyCycle);
            break;
        }
        Serial.println("DATA UPDATED");
      }
      else Serial.println("FRAME ERROR");

      /* INITIALIZATION FOR THE NEXT FRAME */
      Serial.println("INITIALIZATION FOR THE NEXT FRAME");
      frameStart = false;
      frameSelectType = false;
      frameGetValue = false;
      frameFinish = false;
      frameValid = false;
      increment = 0;
    }
}
