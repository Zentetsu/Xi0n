#include "./Ultrason.h"

using namespace std;


Ultrason::Ultrason(int new_echo_Pin, int new_trigger_Pin) {
	//Serial.println ("CREATE ULTRASON");

	echo_Pin = new_echo_Pin;
	trigger_Pin = new_trigger_Pin;

	pinMode(echo_Pin, INPUT); 
	pinMode(trigger_Pin, OUTPUT);
}

Ultrason::~Ultrason() {
	//Serial.println ("DELETE ULTRASON");
}

float Ultrason::getDistance() {
	digitalWrite(trigger_Pin, HIGH);
	delayMicroseconds(10);
	digitalWrite(trigger_Pin, LOW);


	return (pulseIn(echo_Pin, HIGH) / 58.0);
}