#ifndef DEF_ULTRASON
#define DEF_ULTRASON

#include "Arduino.h"


class Ultrason {
	public:
		Ultrason(int new_echo_Pin, int new_trigger_Pin);
		~Ultrason();

		float getDistance();

	private:
		int echo_Pin;
		int trigger_Pin;
};

#endif