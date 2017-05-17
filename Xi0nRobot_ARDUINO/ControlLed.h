#ifndef DEF_CONTROLLED
#define DEF_CONTROLLED

#include "Arduino.h"

#include "./LED.h"


class ControlLed {
	public:
		ControlLed();
		~ControlLed();

		void setMode(int new_mode);
		int getMode();

	private:
		LED *led_auto;
		LED *led_manual;

		int mode;
		
};

#endif