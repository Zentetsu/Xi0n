#include "./Sensor.h"

using namespace std;


Sensor::Sensor() {
	Serial.println ("CREATE SENSOR");
	//allOrNothingSensor = new AllOrNothingSensor();
	infraRedSensor = new InfraRedSensor(A2);
	ultrason = new Ultrason(9, 2);
}

Sensor::~Sensor() {
	Serial.println ("DELETE SENSOR");
	//delete allOrNothingSensor;
	delete infraRedSensor;
	delete ultrason;
}

float Sensor::getDistanceUltrasion() {

	return ultrason->getDistance();
}

float Sensor::getDistanceInfraRedSensor() {

	return infraRedSensor->getDistance();
}