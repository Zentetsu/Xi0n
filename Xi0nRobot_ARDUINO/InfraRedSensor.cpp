#include "./InfraRedSensor.h"

using namespace std;


InfraRedSensor::InfraRedSensor(int init_sensor_Pin) {
	//Serial.println ("CREATE INFRAREDSENSOR");
	sensor_Pin = init_sensor_Pin;
	pinMode(sensor_Pin, INPUT);
}

InfraRedSensor::~InfraRedSensor() {
	//Serial.println ("DELETE INFRAREDSENSOR");
}

//TODO : check result give by it
float InfraRedSensor::getDistance() {

	return analogRead(sensor_Pin)*0.48828125;
}