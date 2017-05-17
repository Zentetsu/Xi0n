#include "./Robot.h"


Robot::Robot() {
	Serial.println ("CREATE ROBOT");
	communication = new Communication();
	controlLed = new ControlLed();
	mobility = new Mobility();
	sensor = new Sensor();
}

Robot::~Robot() {
	Serial.println ("DELETE ROBOT");
	delete communication;
	delete mobility;
	delete sensor;
}

void Robot::Deplacemnt(int new_direction_M1, int new_direction_M2, int new_Speed_M1, int new_Speed_M2) {
	mobility->move(new_direction_M1, new_direction_M2, new_Speed_M1, new_Speed_M2);
}