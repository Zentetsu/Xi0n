#ifndef DEF_INFRAREDSENSOR
#define DEF_INFRAREDSENSOR

#include "Arduino.h"


class InfraRedSensor {
	public:
		InfraRedSensor(int init_sensor_Pin);
		~InfraRedSensor();

		float getDistance();

	private:
		int sensor_Pin;
		
};

#endif