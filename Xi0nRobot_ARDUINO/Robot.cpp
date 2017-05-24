#include "./Robot.h"


Robot::Robot() {
	Serial.println ("CREATE ROBOT");
	//communication = new Communication();
	//controlLed = new ControlLed();
	mobility = new Mobility();
	sensor = new Sensor();
	headServo = new HeadServo();
}

Robot::~Robot() {
	Serial.println ("DELETE ROBOT");
	delete communication;
	delete mobility;
	delete sensor;
	delete headServo;
}

void Robot::deplacemnt(int new_direction_M1, int new_direction_M2, int new_Speed_M1, int new_Speed_M2) {
	Serial.println ("Deplacemnt");
	mobility->move(new_direction_M1, new_direction_M2, new_Speed_M1, new_Speed_M2);
}

float Robot::getDistanceUltrasion() {

	return sensor->getDistanceUltrasion();
}

float Robot::getDistanceInfraRedSensor() {

	return sensor->getDistanceInfraRedSensor();
}

void Robot::setHeadPosition(int new_position) {
	headServo->setPosition(new_position);
}

int Robot::getHeadPosition() {

	return headServo->getPosition();
}