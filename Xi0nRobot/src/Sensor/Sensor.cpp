#include "./../../include/Sensor/Sensor.h"

using namespace std;


Sensor::Sensor() {
	allOrNothingSensor = new AllOrNothingSensor();
	infraRedSensor = new InfraRedSensor();
	ultrason = new Ultrason();
}

Sensor::~Sensor() {
	delete allOrNothingSensor;
	delete infraRedSensor;
	delete ultrason;
}