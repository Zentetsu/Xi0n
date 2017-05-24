#include "./HeadServo.h"

using namespace std;


HeadServo::HeadServo() {
	Serial.println ("CREATE HEADSERVO");
	servo.attach(6);
	setPosition(0);
}

HeadServo::~HeadServo() {
	Serial.println ("DELETE HEADSERVO");	
}

void HeadServo::setPosition(int new_position) {
	servo.write(new_position); 
}

int HeadServo::getPosition() {

	return servoMap;
}