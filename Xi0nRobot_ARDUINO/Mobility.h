#ifndef DEF_MOBILITY
#define DEF_MOBILITY

/*!
 * \file Mobility.h
 * \brief Mobility structure's
 * \author MORANT Thbiaut
 */

#include "Arduino.h"
#include "./Motor.h"


class Mobility {
	public:
		/*!
		 *  \brief Constructor
		 *
		 *  Constructor of the Mobility Class'
		 *
		 *  \param init_motorL_Pin1 : first pin of the left motor 
		 *  \param init_motorL_Pin2 : second pin of the left motor
		 *  \param init_enableL_Pin : pin to start or stop the left motor
		 *  \param init_motorR_Pin1 : first pin of the right motor 
		 *  \param init_motorR_Pin2 : second pin of the right motor
		 *  \param init_enableR_Pin : pin to start or stop the right motor
		 */
		Mobility(int init_motorL_Pin1, int init_motorL_Pin2, int init_enableL_Pin, int init_motorR_Pin1, int init_motorR_Pin2, int init_enableR_Pin);

		/*!
		 *  \brief Destructor
		 *
		 *  Destructor of the Mobility Class'
		 */
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