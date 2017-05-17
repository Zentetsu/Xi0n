#ifndef DEF_ROBOT
#define DEF_ROBOT

#include "Arduino.h"

#include <string>
#include <iostream>
#include "./Communication/Communication.h"
#include "./Sensor/Sensor.h"
#include "./Mobility/Mobility.h"


class Robot {
	public:
		Robot();
		~Robot();

	private:
		Communication *communication;
		Mobility *mobility;
		Sensor *sensor;

};

#endif