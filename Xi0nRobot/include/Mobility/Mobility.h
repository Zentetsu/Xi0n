#ifndef DEF_MOBILITY
#define DEF_MOBILITY

#include "Arduino.h"

#include "./Motor.h"
#include <string>
#include <iostream>


class Mobility {
	public:
		Mobility();
		~Mobility();

	private:
		Motor *rightMotor;
		Motor *leftMotor;
};

#endif