#include "./Sensor.h"

using namespace std;


Sensor::Sensor() {
	Serial.println ("CREATE SENSOR");
	allOrNothingSensor = new AllOrNothingSensor();
	infraRedSensor = new InfraRedSensor(A0);
	ultrason = new Ultrason(1, 1);
}

Sensor::~Sensor() {
	Serial.println ("DELETE SENSOR");
	delete allOrNothingSensor;
	delete infraRedSensor;
	delete ultrason;
}