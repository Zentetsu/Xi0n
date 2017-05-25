#ifndef DEF_ROBOT
#define DEF_ROBOT

#include "Arduino.h"

#include "./Communication.h"
#include "./ControlLed.h"
#include "./Sensor.h"
#include "./Mobility.h"
#include "./HeadServo.h"


class Robot {
	public:
		Robot(int init_motorL_Pin1, int init_motorL_Pin2, int init_enableL_Pin, int init_motorR_Pin1, int init_motorR_Pin2, int init_enableR_Pin, int init_InfraRedSensor_Pin, int init_echo_Pin, int init_trigger_Pin, int new_ServoMotor_Pin);
		~Robot();

		void deplacemnt(int direction_M1, int direction_M2, int speed_M1, int speed_M2);
		float getDistanceUltrasion();
		float getDistanceInfraRedSensor();
		void setHeadPosition(int new_position);
		int getHeadPosition();

	private:
		Communication *communication;
		ControlLed *controlLed;
		Mobility *mobility;
		Sensor *sensor;
		HeadServo *headServo;
		
};

#endif