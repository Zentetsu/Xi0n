#include "./../../include/Mobility/Mobility.h"

using namespace std;


Mobility::Mobility() {
		rightMotor = new Motor();
		leftMotor = new Motor();
}

Mobility::~Mobility() {
	delete rightMotor;
	delete leftMotor;
}