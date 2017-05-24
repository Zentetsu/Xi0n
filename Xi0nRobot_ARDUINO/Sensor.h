#ifndef DEF_SENSOR
#define DEF_SENSOR

#include "Arduino.h"

#include "./Ultrason.h"
#include "./InfraRedSensor.h"
//#include "./AllOrNothingSensor.h"


class Sensor {
	public:
		Sensor();
		~Sensor();

		float getDistanceUltrasion();
		float getDistanceInfraRedSensor();

	private:
		//AllOrNothingSensor *allOrNothingSensor;
		InfraRedSensor *infraRedSensor;
		Ultrason *ultrason;
		
};

#endif