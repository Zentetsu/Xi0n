#ifndef DEF_SENSOR
#define DEF_SENSOR

#include <string>
#include <iostream>
#include "./Ultrason.h"
#include "./InfraRedSensor.h"
#include "./AllOrNothingSensor.h"


class Sensor {
	public:
		Sensor();
		~Sensor();

	private:
		AllOrNothingSensor *allOrNothingSensor;
		InfraRedSensor *infraRedSensor;
		Ultrason *ultrason;
};

#endif