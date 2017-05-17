#ifndef DEF_MOTOR
#define DEF_MOTOR

#include "Arduino.h"

#include <string>
#include <iostream>


class Motor {
	public:
		Motor();
		~Motor();

		void setPin(int new_Pin);
		int getPin();

		void enable();
		void disable();

		void changeRotationDirection(int new_Direction);
		int getRotationDirection();
 
		void brake();
		void stopnBrake();

	private:
		int pin;
		int rotation_Direction;

};

#endif