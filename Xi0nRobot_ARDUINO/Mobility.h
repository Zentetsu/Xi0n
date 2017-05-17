#ifndef DEF_MOBILITY
#define DEF_MOBILITY

#include "Arduino.h"

#include "./Motor.h"


class Mobility {
	public:
		Mobility();
		~Mobility();

		void setDirection(int new_direction);
		int getDirection();

		void enable();
		void disable();

		void move(int new_direction_M1, int new_direction_M2, int new_Speed_M1, int new_Speed_M2);

		void forward();
		void backward();

		void standingRotation(int new_direction);

		void brake();
		void stopBrake();

		void setSpeed(int speed_M1, int speed_M2);

	private:
		Motor *right_Motor;
		Motor *left_Motor;

		/*
		 *	Direction:
		 *		. 1		-> forward
		 *		. -1	-> backward
		 *		. 0		-> stopped // left or right standing rotation
		 *		. 4		-> brake
		 */
		int direction;
		
};

#endif