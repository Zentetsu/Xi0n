#ifndef DEF_SENSOR
#define DEF_SENSOR

#include "Arduino.h"

#include "./Ultrason.h"
#include "./InfraRedSensor.h"


class Sensor {
	public:
		Sensor(int init_InfraRedSensor_Pin, int init_echo_Pin, int init_trigger_Pin);
		~Sensor();

		float getDistanceUltrasion();
		float getDistanceInfraRedSensor();

	private:
		InfraRedSensor *infraRedSensor;
		Ultrason *ultrason;
		
};

#endif