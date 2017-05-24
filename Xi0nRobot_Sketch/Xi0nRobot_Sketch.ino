#include <AllOrNothingSensor.h>
#include <Communication.h>
#include <ControlLed.h>
#include <InfraRedSensor.h>
#include <LED.h>
#include <Mobility.h>
#include <Motor.h>
#include <Robot.h>
#include <Sensor.h>
#include <Ultrason.h>

 
Robot *robot;
int headposition;
int up_down;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  robot = new Robot();
  headposition = 0;
  up_down = 1;
}

void loop() {
  if(up_down)
    headposition++;
  else
    headposition--;

  robot->setHeadPosition(headposition);
  Serial.println (robot->getDistanceUltrasion());
  Serial.println (robot->getDistanceInfraRedSensor());

  if(headposition == 44)
    up_down = 1;
  else if(headposition == 134)
    up_down = 0;
} 
