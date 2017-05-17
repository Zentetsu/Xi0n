#include "./InfraRedSensor.h"

using namespace std;


InfraRedSensor::InfraRedSensor(int init_sensor_Pin) {
	Serial.println ("CREATE INFRAREDSENSOR");
	sensor_Pin = init_sensor_Pin;

	analogReference(EXTERNAL);
}

InfraRedSensor::~InfraRedSensor() {
	Serial.println ("DELETE INFRAREDSENSOR");
}

//TODO : check result give by it
float InfraRedSensor::getDistance() {
	float read = 0;

	for(int i = 0; i < 10; i++)
		read += analogRead(sensor_Pin);

	return (32076.69016894*pow(read, -1.245865753));
}