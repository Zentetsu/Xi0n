#ifndef DEF_ULTRASON
#define DEF_ULTRASON

/*!
 * \file Ultrason.h
 * \brief Ultrason structure's
 * \author MORANT Thbiaut
 */

#include "Arduino.h"


class Ultrason {
	public:
		/*!
		 *  \brief Constructor
		 *
		 *  Constructor of the Ultrason Class'
		 *
		 *  \param new_echo_Pin : pin for the reception of information
		 *  \param new_trigger_Pin : pin for the emition of information
		 */
		Ultrason(int new_echo_Pin, int new_trigger_Pin);

		/*!
		 *  \brief Destructor
		 *
		 *  Destructor of the Ultrason Class'
		 */
		~Ultrason();


		/*!
		 *  \brief return the distance between an object and the robot
		 *
		 *  \return the distance
		 */
		float getDistance();

	private:
		int echo_Pin;/*!< pin for the reception of information*/
		int trigger_Pin;/*!< pin for the emition of information*/
};

#endif