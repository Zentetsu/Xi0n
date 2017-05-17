#include "./Motor.h"

using namespace std;


Motor::Motor(int init_motor_Pin1, int init_motor_Pin2, int init_enable_Pin) {
	Serial.println ("CREATE MOTOR");
	motor_Pin1 = init_motor_Pin1;
	motor_Pin2 = init_motor_Pin2;
	enable_Pin = init_enable_Pin;

	pin1_HIGH_LOW = LOW;
	pin2_HIGH_LOW = LOW;

	speed = 0;

	rotation_Direction = 0;	

	pinMode(motor_Pin1, OUTPUT);
	pinMode(motor_Pin2, OUTPUT);
	pinMode(enable_Pin, OUTPUT);
}

Motor::~Motor() {
	Serial.println ("DELETE MOTOR");	
}

void  Motor::run(int v1_HIGH_LOW, int v2_HIGH_LOW) {
	digitalWrite(motor_Pin1, v1_HIGH_LOW);
	digitalWrite(motor_Pin2, v2_HIGH_LOW);
}

void Motor::enable() {
	if (pin1_HIGH_LOW == pin2_HIGH_LOW) {
		pin1_HIGH_LOW = HIGH;

		rotation_Direction = 1;	
	}

	run(pin1_HIGH_LOW, pin2_HIGH_LOW);
}

void Motor::disable() {
	run(0, 0);
}

void Motor::changeRotationDirection() {
	pin1_HIGH_LOW = ((pin2_HIGH_LOW = pin1_HIGH_LOW) == 0) ?  1: 0;
	rotation_Direction = (pin1_HIGH_LOW == 1) ?  1: -1;

	run(pin1_HIGH_LOW, pin2_HIGH_LOW);
}

int Motor::getRotationDirection() {

	return rotation_Direction;
}

void Motor::brake() {
	run(1, 1);
}

void Motor::stopBrake() {
	run(pin1_HIGH_LOW, pin2_HIGH_LOW);
}

void Motor::setSpeed(int new_Speed) {
	speed = new_Speed;

	analogWrite(enable_Pin, speed);
}

int Motor::getSpeed() {

	return speed;
}