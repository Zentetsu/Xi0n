#ifndef DEF_MOBILITY
#define DEF_MOBILITY

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