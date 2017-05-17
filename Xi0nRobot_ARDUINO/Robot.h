#ifndef DEF_ROBOT
#define DEF_ROBOT

#include "Arduino.h"

#include "./Communication.h"
#include "./ControlLed.h"
#include "./Sensor.h"
#include "./Mobility.h"


class Robot {
	public:
		Robot();
		~Robot();

		void Deplacemnt(int direction_M1, int direction_M2, int speed_M1, int speed_M2);

	private:
		Communication *communication;
		ControlLed *controlLed;
		Mobility *mobility;
		Sensor *sensor;
		
};

#endif