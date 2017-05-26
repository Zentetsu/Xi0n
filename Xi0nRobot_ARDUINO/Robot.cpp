 #include "./Robot.h"


Robot::Robot(int init_motorL_Pin1, int init_motorL_Pin2, int init_enableL_Pin, int init_motorR_Pin1, int init_motorR_Pin2, int init_enableR_Pin, int init_InfraRedSensor_Pin, int init_echo_Pin, int init_trigger_Pin, int new_ServoMotor_Pin) {
	//Serial.println ("CREATE ROBOT");
	//communication = new Communication();
	//controlLed = new ControlLed();
	mobility = new Mobility(init_motorL_Pin1, init_motorL_Pin2, init_enableL_Pin, init_motorR_Pin1, init_motorR_Pin2, init_enableR_Pin);
	sensor = new Sensor(init_InfraRedSensor_Pin, init_echo_Pin, init_trigger_Pin);
	headServo = new HeadServo(new_ServoMotor_Pin);
}

Robot::~Robot() {
	//Serial.println ("DELETE ROBOT");
	delete mobility;
	delete sensor;
	delete headServo;
}

void Robot::deplacemnt(int new_direction_M1, int new_direction_M2, int new_Speed_M1, int new_Speed_M2) {
	//Serial.println ("Deplacemnt");
	switch(new_direction_M1+new_direction_M2) {
		case -2:
			if(!((getDistanceUltrasion() <= 5) && (new_Speed_M1 > new_Speed_M2)))
				mobility->move(new_direction_M1, new_direction_M2, new_Speed_M1, new_Speed_M2);

			break;
		case 0:
			mobility->move(new_direction_M1, new_direction_M2, new_Speed_M1, new_Speed_M2);

			break;
		case 2:
			if(!(getDistanceInfraRedSensor() > 25))
				if(!((getDistanceUltrasion() <= 5) && (new_Speed_M1 > new_Speed_M2)))
					mobility->move(new_direction_M1, new_direction_M2, new_Speed_M1, new_Speed_M2);

			break;

		default:
			mobility->move(new_direction_M1, new_direction_M2, new_Speed_M1, new_Speed_M2);

			break;
	}
}

float Robot::getDistanceUltrasion() {

	return sensor->getDistanceUltrasion();
}

float Robot::getDistanceInfraRedSensor() {

	return sensor->getDistanceInfraRedSensor();
}

void Robot::setHeadPosition(int new_position) {
	headServo->setPosition(new_position);
}

int Robot::getHeadPosition() {

	return headServo->getPosition();
}