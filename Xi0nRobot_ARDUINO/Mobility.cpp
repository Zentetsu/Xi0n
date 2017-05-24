#include "./Mobility.h"


using namespace std;


Mobility::Mobility() {
	Serial.println ("CREATE MOBILITY");
	left_Motor = new Motor(7, 4, 10);
	right_Motor = new Motor(12, 8, 11);

	//TODO : assign speed for the two motors

	direction = 0;
}

Mobility::~Mobility() {
	Serial.println ("DELETE MOBILITY");
	delete left_Motor;
	delete right_Motor;
}

void Mobility::setDirection(int new_direction) {
	direction = new_direction;
}

int Mobility::getDirection() {

	return direction;
}


void Mobility::enable() {
	left_Motor->enable();
	right_Motor->enable();
}

void Mobility::disable() {
	left_Motor->disable();
	right_Motor->disable();
}

void Mobility::move(int new_direction_M1, int new_direction_M2, int new_Speed_M1, int new_Speed_M2) {
	switch(new_direction_M1 + new_direction_M2) {
		case -2:
			Serial.println ("backward");
			backward();
			setSpeed(new_Speed_M1, new_Speed_M2);
			setDirection(-1);

			break;
		case 0:
			Serial.println ("stop/left or right standing rotation");
			if(new_direction_M1 == 0)
				disable();
			else {
				standingRotation(new_direction_M1);
				setSpeed(new_Speed_M1, new_Speed_M2);
			}

			setDirection(0);

			break;
		case 2:
			Serial.println ("forward");
			forward();
			setSpeed(new_Speed_M1, new_Speed_M2);
			setDirection(1);

			break;
		case 8:
			Serial.println ("brake");
			brake();
			setDirection(4);

			break;
	}
}

void Mobility::forward() {
			Serial.println ("get forward");

	if (left_Motor->getRotationDirection() == -1)
		left_Motor->changeRotationDirection();

	if (right_Motor->getRotationDirection() == -1)
		right_Motor->changeRotationDirection();

	switch(direction) {
		case 0:
			enable();

			break;
		case 4:
			stopBrake();

			break;
	}
}

void Mobility::backward() {
			Serial.println ("get backward");


	if (left_Motor->getRotationDirection() == 1)
		left_Motor->changeRotationDirection();

	if (right_Motor->getRotationDirection() == 1)
		right_Motor->changeRotationDirection();


	switch(getDirection()) {
		case 0:
			enable();

			break;
		case 4:
			stopBrake();

			break;
	}
}

void Mobility::standingRotation(int new_direction) {
			Serial.println ("get standing");
	switch(getDirection()) {
		case -1:
			if(new_direction == -1)
				right_Motor->changeRotationDirection();
			else
				left_Motor->changeRotationDirection();

			break;
		case 0:
			if(new_direction == -1) {
				if (left_Motor->getRotationDirection() != -1)
					left_Motor->changeRotationDirection();

				if (right_Motor->getRotationDirection() == -1)
					right_Motor->changeRotationDirection();

			} else {
				if (left_Motor->getRotationDirection() == -1)
					left_Motor->changeRotationDirection();

				if (right_Motor->getRotationDirection() != -1)
					right_Motor->changeRotationDirection();
			}

			enable();

			break;
		case 1:
			if(new_direction == -1)
				left_Motor->changeRotationDirection();
			else
				right_Motor->changeRotationDirection();

			break;
		case 2:
			if(new_direction == -1) {
				if (left_Motor->getRotationDirection() != -1)
					left_Motor->changeRotationDirection();

				if (right_Motor->getRotationDirection() == -1)
					right_Motor->changeRotationDirection();

			} else {
				if (left_Motor->getRotationDirection() == -1)
					left_Motor->changeRotationDirection();

				if (right_Motor->getRotationDirection() != -1)
					right_Motor->changeRotationDirection();
			}

			break;
		case 4:
			if(new_direction == -1) {
				if (left_Motor->getRotationDirection() != -1)
					left_Motor->changeRotationDirection();

				if (right_Motor->getRotationDirection() == -1)
					right_Motor->changeRotationDirection();

			} else {
				if (left_Motor->getRotationDirection() == -1)
					left_Motor->changeRotationDirection();

				if (right_Motor->getRotationDirection() != -1)
					right_Motor->changeRotationDirection();
			}

			stopBrake();

			break;
	}

}

void Mobility::brake() {
			Serial.println ("get brake");
	left_Motor->brake();
	right_Motor->brake();
}

void Mobility::stopBrake() {
			Serial.println ("stop brake");
	left_Motor->stopBrake();
	right_Motor->stopBrake();
}

void Mobility::setSpeed(int speed_M1, int speed_M2) {
	left_Motor->setSpeed(speed_M1);
	right_Motor->setSpeed(speed_M2);
}