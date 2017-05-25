#include "./HeadServo.h"

using namespace std;


HeadServo::HeadServo(int new_ServoMotor_Pin) {
	//Serial.println ("CREATE HEADSERVO");
	servo.attach(new_ServoMotor_Pin);
	setPosition(0);
}

HeadServo::~HeadServo() {
	//Serial.println ("DELETE HEADSERVO");	
}

void HeadServo::setPosition(int new_position) {
	servo.write(new_position); 
}

int HeadServo::getPosition() {

	return servoMap;
}