#ifndef DEF_CONTROLLED
#define DEF_CONTROLLED

/*!
 * \file ControlLed.h
 * \brief ControlLed structure's
 * \author MORANT Thbiaut
 */

#include "Arduino.h"

#include "./LED.h"


class ControlLed {
	public:
		/*!
		 *  \brief Constructor
		 *
		 *  Constructor of the Constructor Class'
		 */
		ControlLed();

		/*!
		 *  \brief Destructor
		 *
		 *  Destructor of the ControlLed Class'
		 */
		~ControlLed();


		/*!
		 *  \brief swap betaween led_auto and led_manual in function of the mode of control of the robot
		 *
		 * \parama new_mode : new mode
		 */
		void setMode(int new_mode);

		/*!
		 *  \brief return the actual mode
		 *
		 * \return actual mode
		 */
		int getMode();

	private:
		LED *led_auto;/*!< object used to show the mode Auto*/
		LED *led_manual;/*!< object used to show the mode Manual*/

		int mode;/*!< actived mode*/
		
};

#endif