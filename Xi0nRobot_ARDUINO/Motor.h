#ifndef DEF_MOTOR
#define DEF_MOTOR

#include "Arduino.h"


class Motor {
	public:
		Motor(int init_motor_Pin1, int init_motor_Pin2, int init_enable_Pin);
		~Motor();

		void run(int v1_HIGH_LOW, int v2_HIGH_LOW);

		void enable();
		void disable();

		void changeRotationDirection();
		int getRotationDirection();
 
		void brake();
		void stopBrake();

		void setSpeed(int new_Speed);
		int getSpeed();

	private:
		int motor_Pin1;
		int motor_Pin2;
		int enable_Pin;

		int rotation_Direction;

		int speed;

		int pin1_HIGH_LOW;
		int pin2_HIGH_LOW;
};

#endif