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


		/*!
		 *  \brief change the direction of the robot
		 *
		 *  \param new_direction : new direction
		 */
		void setDirection(int new_direction);

		/*!
		 *  \brief return the direction of the robot
		 *
		 *  \return the direction
		 */
		int getDirection();


		/*!
		 *  \brief allows the motor to ratote
		 *
		 *  enables the rotation of the motor
		 */
		void enable();

		/*!
		 *  \brief desables the rotation of the motor
		 */
		void disable();


		/*!
		 *  \brief Control the new direction and the new speed of the Robot
		 *
		 *  change the state of the deplacement of the robot and set the new speed
		 *
		 *  \param new_direction_M1 : the new direction of the left motor
		 *  \param new_direction_M2 : the new direction of the right motor
		 *  \param new_speed_M1 : the new speed of the left motor
		 *  \param new_speed_M2 : the new speed of the right motor
		 */
		void move(int new_direction_M1, int new_direction_M2, int new_Speed_M1, int new_Speed_M2);


		/*!
		 *  \brief change the old direction to forword
		 *
		 *  check the old direction and in each case aplly a diferent procedure to swap in forward
		 */
		void forward();
		/*!
		 *  \brief change the old direction to backward
		 *
		 *  check the old direction and in each case aplly a diferent procedure to swap in backward
		 */
		void backward();


		/*!
		 *  \brief change the old direction to a standing rotation
		 *
		 *  check the old direction and in each case aplly a diferent procedure to swap in standing rotation
		 */
		void standingRotation(int new_direction);

 
		/*!
		 *  \brief enable brake
		 */
		void brake();

		/*!
		 *  \brief desable brake
		 */
		void stopBrake();


		/*!
		 *  \brief change the motor speed rotation
		 *
		 * \parama speed_M1 : the new value of speed for the left motor
		 * \parama speed_M2 : the new value of speed for the right motor
		 */
		void setSpeed(int speed_M1, int speed_M2);

	private:
		Motor *left_Motor;/*!< object used to control the left motor*/
		Motor *right_Motor;/*!< object used to control the right motor*/

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