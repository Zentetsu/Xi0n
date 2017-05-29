#ifndef DEF_LED
#define DEF_LED

/*!
 * \file LED.h
 * \brief LED structure's
 * \author MORANT Thbiaut
 */

#include "Arduino.h"

#include "./LED.h"


class LED {
	public:
		/*!
		 *  \brief Constructor
		 *
		 *  Constructor of the LED Class'
		 *
		 *  \param init_pin : pin to light up the led
		 */
		LED(int init_pin);

		/*!
		 *  \brief Destructor
		 *
		 *  Destructor of the LED Class'
		 */
		~LED();


		/*!
		 *  \brief light up the led
		 */
		void enable();

		/*!
		 *  \brief siwtch of the led
		 */
		void disable();


		/*!
		 *  \brief return the state of the LED
		 *
		 * 	\return the state
		 */
		int getState();

	private:
		int pin;/*!< pin to light up the led*/
		int state;/*!< state of the LED*/
		
};

#endif