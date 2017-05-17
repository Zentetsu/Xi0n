#ifndef DEF_LED
#define DEF_LED

#include "Arduino.h"

#include "./LED.h"


class LED {
	public:
		LED(int init_pin);
		~LED();

		void enable();
		void disable();

		int getState();

	private:
		int pin;
		int state;
		
};

#endif