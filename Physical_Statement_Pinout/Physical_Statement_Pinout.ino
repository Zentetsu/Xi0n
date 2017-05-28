/********************************************************************
 * Control program of a mobile robot developed in the framework of
 * part three of the red wire robotic project
 * 
 * First year at UPSSITECH - Toulouse
 * 
 * VERSION: V02
 * DATE: 25/05/17
 * NAMES: MORANT Thibaut & PECCHIOLI Mathieu
********************************************************************/

/********************************************************************
 * LIBRAIRIES
********************************************************************/
#include <ControlLed.h>
#include <InfraRedSensor.h>
#include <LED.h>
#include <Mobility.h>
#include <Motor.h>
#include <Robot.h>
#include <Sensor.h>
#include <Ultrason.h>

/********************************************************************
 * STATEMENT OF THE PHYSICAL PINOUT
********************************************************************/
#define PinIR A2                  // pinout of the front IR sensor
#define PinUltrasoundTrig 2       // pinout trigger right side ultrasound
#define PinUltrasoundEcho 9       // pinout right side ultrasound echo
#define PinServoMotor 6           // pinout servomotor front side scanning
#define PinTX 1                   // Transmission pin series
#define PinRX 0                   // Receive pin series
#define PinPWMMotorLeft 10        // Left motor PWM pinout
#define PinPWMMotorRight 11       // Right motor PWM pinout
#define PinDigitalMotorLeft1 7    // Enable pin 1 left motor
#define PinDigitalMotorLeft2 4    // Enable pin 2 left motor
#define PinDigitalMotorRight1 12  // Enable pin 1 right motor
#define PinDigitalMotorRight2 8   // Enable pin 2 right motor

/********************************************************************
 * GLOBAL VARIABLES
********************************************************************/
boolean mode;                 // Operating mode: Auto = true, Manual = false
int infraRedRemote;           // Distance given by the infra-red sensor
int ultrasoundRemote;         // Distance given by the ultrasound sensor
int scanAngle;                // Sweep angle of the ServoMotor
int rightMotorDutyCycle;      // Duty cycle of the right motor
int leftMotorDutyCycle;       // Duty cycle of the left motor
int moveDirectionRightMotor;  // Right motor direction
int moveDirectionLeftMotor;   // Left motor direction

int incChar = 0;
Robot *robot;
int up_down;


/********************************************************************
 * MAIN PROGRAM - SETTINGS
********************************************************************/
void setup() {
  Serial.begin(9600);
  robot = new Robot(PinDigitalMotorLeft1, PinDigitalMotorLeft2, PinPWMMotorLeft, PinDigitalMotorRight1, PinDigitalMotorRight2, PinPWMMotorRight, PinIR,PinUltrasoundEcho, PinUltrasoundTrig, PinServoMotor);
  scanAngle = 89;
  up_down = 1;
}

/********************************************************************
 * MAIN PROGRAM - LOOP
********************************************************************/
void loop() {
  assignHeadPosition();
  assignDistanceUltrason();
  assignDistanceInfraRed();
  communication();
  setDeplacemnt();
  delay(50);
}


/********************************************************************
 * TREATEMENT FROM THE ULTRASONIC DISTANCE SENSOR
********************************************************************/
void assignDistanceUltrason() {
  ultrasoundRemote = robot->getDistanceUltrasion();
}


/********************************************************************
 * TREATEMENT FROM THE INFRARED DISTANCE SENSOR
********************************************************************/
void assignDistanceInfraRed() {
  infraRedRemote = robot->getDistanceInfraRedSensor();
}


/********************************************************************
 * SERVOMOTOR ANGLE MANAGEMENT
********************************************************************/
void assignHeadPosition() {
  if(up_down)
    scanAngle += 5;
  else
    scanAngle -= 5;

  robot->setHeadPosition(scanAngle);

  if(scanAngle == 66)
    up_down = 1;
  else if(scanAngle == 111)
    up_down = 0;
}

void setDeplacemnt() {
    robot->deplacemnt(moveDirectionLeftMotor, moveDirectionRightMotor, leftMotorDutyCycle, rightMotorDutyCycle);
}


/********************************************************************
 * COMMUNICATION: SENDING SENSOR DISTANCE AND SCAN ANGLE
 * Ultrasonic:  "$0A$"
 * Infrared:    "$1A$"
 * ScanAngle:   "$2A$"
********************************************************************/
void communication() {
  Serial.print("$0");
  char c = ultrasoundRemote;
  Serial.print(c);
  Serial.print("!");
  Serial.print("$1");
  char b = infraRedRemote;
  Serial.print(b);
  Serial.print("!");
  Serial.print("$2");
  char a = scanAngle;
  Serial.print(a);
  Serial.print("!");
}


/********************************************************************
 * PROCESSING OF DATA FROM SERIAL DATA
 * FRAME RECEIVE EXAMPLE:   "$0A!"
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
        //Serial.println("$ detect");
      } else frameFinish = true;
    } else {
      increment++;
      if(frameSelectType == false) {
        type = c;
        if(type == '0') {
          //Serial.println("Changing the operating mode");
          frameSelectType = true;
        } else if(type == '1') {
          //Serial.println("Changing the right engine duty cycle");
          frameSelectType = true;
        } else if(type == '2') {
          //Serial.println("Changing the left engine duty cycle");
          frameSelectType = true;
        } else if(type == '3') {
        //Serial.println("Changing the right engine movement direction");
        frameSelectType = true;
        } else if(type == '4') {
        //Serial.println("Changing the left engine movement direction");
        frameSelectType = true;
        } else {
          //Serial.println("TYPE ERROR");
          frameFinish = true;
        }
      } else if(frameGetValue ==  false) {
        value = c;
        //Serial.print("Value save: ");
        //Serial.println(value);
        frameGetValue = true;
      } else if(frameFinish == false) {
        if(c == '!') {
          frameValid = true;
          //Serial.println("The Frame Is Valid");
        } //else Serial.println("The Frame Isn't Valid");
        frameFinish = true;
      }
    }

    if((frameFinish == true) || (increment == 3)) {
      if(frameValid == true) {
        //Serial.println("DATA UPDATE...");
        switch (type) {
          case '0':
            /* MODE UPDATE */
            if(value == '0') {
              mode = false;
              //Serial.println("MANUAL OPERATING MODE");
            } else {
              mode = true;
              //Serial.println("AUTO OPERATING MODE");
            }
            break;
          case '1':
            /* DUTY CYCLE RIGHT ENGINE UPDATE */
            rightMotorDutyCycle = value;
            //Serial.print("DUTY CYCLE RIGHT: ");
            //Serial.println(rightMotorDutyCycle);
            break;
          case '2':
            /* DUTY CYCLE LEFT ENGINE UPDATE */
            leftMotorDutyCycle = value;
            //Serial.print("DUTY CYCLE LEFT: ");
            //Serial.println(leftMotorDutyCycle);
            break;
          case '3':
            /* MOVEMENT DIRECTION RIGHT ENGINE UPDATE */
            moveDirectionRightMotor = value;
            //Serial.print("MOVEMENT DIRECTION RIGHT ENGINE: ");
            //Serial.println(leftMotorDutyCycle);
            break;
          case '4':
            /* MOVEMENT DIRECTION LEFT ENGINE UPDATE */
            moveDirectionLeftMotor = value;
            //Serial.print("MOVEMENT DIRECTION LEFT ENGINE: ");
            //Serial.println(leftMotorDutyCycle);
            break;
        }
        //Serial.println("DATA UPDATED");
      }
      //else Serial.println("FRAME ERROR");

      /* INITIALIZATION FOR THE NEXT FRAME */
      //Serial.println("INITIALIZATION FOR THE NEXT FRAME");
      frameStart = false;
      frameSelectType = false;
      frameGetValue = false;
      frameFinish = false;
      frameValid = false;
      increment = 0;
    }
}
