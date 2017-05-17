#include "./ControlLed.h"

using namespace std;


ControlLed::ControlLed() {
	Serial.println ("CREATE CONTROLLED");
		led_auto = new LED(1);
		led_manual = new LED(1);

		int mode = 0;

}

ControlLed::~ControlLed() {
	Serial.println ("DELETE CONTROLLED");
	delete led_auto;
	delete led_manual;
}

void ControlLed::setMode(int new_mode) {
	mode = new_mode;
	
	if(new_mode==0) {
		led_manual->enable();
		led_auto->disable();
	} else {
		led_auto->enable();
		led_manual->disable();		
	}
}

int ControlLed::getMode() {

	return mode;
}