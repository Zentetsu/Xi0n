#include "./LED.h"

using namespace std;


LED::LED(int init_pin) {
	//Serial.println ("CREATE LED");
	pin = init_pin;
	state = LOW;

	pinMode(pin, OUTPUT);
}

LED::~LED() {
	//Serial.println ("DELETE LED");
}

void LED::enable() {
	digitalWrite(pin, (state = HIGH));
}

void LED::disable() {
	digitalWrite(pin, (state = LOW));
}

int LED::getState() {

	return state;
}