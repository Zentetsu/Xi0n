#include "./Communication.h"

using namespace std;


Communication::Communication() {
	Serial.println ("CREATE COMMUNICATION");

}

Communication::~Communication() {
	Serial.println ("DELETE COMMUNICATION");
}