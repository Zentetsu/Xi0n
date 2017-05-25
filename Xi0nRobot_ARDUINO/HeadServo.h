#ifndef DEF_HEADSERVO
#define DEF_HEADSERVO

#include <Servo.h>
#include "Arduino.h"


class HeadServo {
	public:
		HeadServo(int new_ServoMotor_Pin);
		~HeadServo();

		void setPosition(int new_position);
		int getPosition();

	private:
		Servo servo;
		int servoMap;

};

#endif