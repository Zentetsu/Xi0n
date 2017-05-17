#include "Arduino.h"

#include "./../include/Robot.h"

using namespace std;


Robot::Robot() {
	communication = new Communication();
	mobility = new Mobility();
	sensor = new Sensor();
}

Robot::~Robot() {
	delete communication;
	delete mobility;
	delete sensor;

	cout << "delete Robot" << endl;
}