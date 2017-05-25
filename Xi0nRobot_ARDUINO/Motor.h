#ifndef DEF_MOTOR
#define DEF_MOTOR

/*!
 * \file Motor.h
 * \brief Motor structure's
 * \author MORANT Thbiaut
 */

#include "Arduino.h"


class Motor {
	public:
		/*!
		 *  \brief Constructor
		 *
		 *  Constructor of the motor Class'
		 *
		 *  \param init_motor_Pin1 : first pin of the motor
		 *  \param init_motor_Pin2 : second pin of the motor
		 *  \param init_enable_Pin : pin to start or stop the motor
		 */
		Motor(int init_motor_Pin1, int init_motor_Pin2, int init_enable_Pin);

		/*!
		 *  \brief Destructor
		 *
		 *  Destructor of the motor Class'
		 */
		~Motor();


		/*!
		 *  \brief assign the new direction for the motor
		 *
		 *  assign the new value for the pin1 and pin2
		 *
		 *  \param v1_HIGH_LOW : control the first part of the H-bridge
		 *  \param v2_HIGH_LOW : control the second part of the H-bridge
		 */
		void run(int v1_HIGH_LOW, int v2_HIGH_LOW);


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
		 *  \brief Change the rotation of the motor
		 */
		void changeRotationDirection();

		/*!
		 *  \brief get the rotation direction of the motor
		 *
		 *  \return the direction of the motor
		 */
		int getRotationDirection();
 

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
		 */
		void setSpeed(int new_Speed);

		/*!
		 *  \brief get the motor speed rotation
		 *
		 *  \return this value
		 */
		int getSpeed();

	private:
		int motor_Pin1;/*!< First pin of the motor*/
		int motor_Pin2;/*!< Second pin of the motor*/
		int enable_Pin;/*!< Pin to control the motor*/

		int rotation_Direction;/*!< rotation direction of the motor*/

		int speed;/*!< motor speed rotation*/

		int pin1_HIGH_LOW;/*!< first part of the H-bridge*/
		int pin2_HIGH_LOW;/*!< second part of the H-bridge*/
};

#endif