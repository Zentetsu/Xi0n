import processing.core.PApplet;
/*******************************************************
              LIBRAIRIES IMPORTATION
*******************************************************/
import processing.serial.*;

/*******************************************************
 * GLOBAL STATES VARIABLES
 *******************************************************/

public class Network extends PApplet {

	public boolean mode; // Operating mode: Auto = true, Manual = false
	public int infraredRemote; // Distance given by the infra-red sensor
	public int ultrasoundRemote; // Distance given by the ultrasound sensor
	public int scanAngle; // Sweep angle of the servomotor
	public int rightMotorDutyCycle; // Duty cycle of the right motor
	public int leftMotorDutyCycle; // Duty cycle of the left motor
	public int moveDirectionRightMotor; // Move direction to the right motor
	public int moveDirectionLeftMotor; // Move direction to the left motor
	public Serial myPort; // xbee serial communication on the computer side

	/*******************************************************
	 * COMMUNICATION VARIABLES
	 *******************************************************/
	boolean frameStart = false;
	boolean frameSelectType = false;
	boolean frameGetValue = false;
	boolean frameFinish = false;
	boolean frameValid = false;
	int frameType;
	int frameValue;
	int increment;

	/*******************************************************
	 * MAIN PROGRAM SETTINGS
	 *******************************************************/
	public void setup() {
		// Xbee serial initialization
		String[] myList = Serial.list();
		System.out.println(myList[0]);
		myPort = new Serial(this, Serial.list()[0], 9600);

		// Global Variables initialization
		mode = false;
		rightMotorDutyCycle = 2;
		leftMotorDutyCycle = 4;

	}

	/*******************************************************
	 * MAIN PROGRAM LOOP
	 *******************************************************/
	public void draw() {
		if (myPort.available() > 0)
			readSerial();
		writeSerial();
		delay(100);
	}

	/*******************************************************
	 * READ SERIAL FUNCTION
	 * 
	 * FRAME TYPES: => Ultrasound remote: $0A! => Infra-red remote $1A! => Scan
	 * angle: $2A!
	 *******************************************************/
	public void readSerial() {
		// Read the data on serial
		int n = myPort.read();
		char serialValue = (char) n;
		System.out.println(serialValue);

		// Frame Decoding
		if (frameStart == false) {
			if (serialValue == '$') {
				frameStart = true;
				increment = 0;
				System.out.println("$ detect");
			} else
				frameFinish = true;
		} else {
			increment++;
			if (frameSelectType == false) {
				frameType = serialValue;
				if (frameType == '0') {
					System.out.println("Changing ultrasound remote");
					frameSelectType = true;
				} else if (frameType == '1') {
					System.out.println("Changing infra-red remote");
					frameSelectType = true;
				} else if (frameType == '2') {
					System.out.println("Changing scan angle");
					frameSelectType = true;
				} else {
					System.out.println("TYPE ERROR");
					frameFinish = true;
				}
			} else if (frameGetValue == false) {
				frameValue = serialValue;
				System.out.print("Value save: ");
				System.out.println(frameValue);
				frameGetValue = true;
			} else if (frameFinish == false) {
				if (serialValue == '!') {
					frameValid = true;
					System.out.println("The Frame Is Valid");
				} else
					System.out.println("The Frame Isn't Valid");
				frameFinish = true;
			}
		}

		if ((frameFinish == true) || (increment == 3)) {
			if (frameValid == true) {
				System.out.println("DATA UPDATE...");
				switch (frameType) {
				case '0':
					/* ULTRASOUND REMOTE UPDATE */
					ultrasoundRemote = frameValue;
					break;
				case '1':
					/* INFRARED REMOTE UPDATE */
					infraredRemote = frameValue;
					break;
				case '2':
					/* SCAN ANGLE UPDATE */
					scanAngle = frameValue;
					break;
				}
				System.out.println("DATA UPDATED");
			} else
				System.out.println("FRAME ERROR");

			/* INITIALIZATION FOR THE NEXT FRAME */
			System.out.println("INITIALIZATION FOR THE NEXT FRAME");
			frameStart = false;
			frameSelectType = false;
			frameGetValue = false;
			frameFinish = false;
			frameValid = false;
			increment = 0;
		}
	}

	/****************************************************************
	 * WRITE SERIAL FUNCTION FRAME TYPES: => Changing operating mode: $0A! =>
	 * Changing the right engine duty cycle: $1A! => Changing the left engine
	 * duty cycle: $2A! => Changing the right engine movement direction: $3A! =>
	 * Changing the left engine movement direction: $4A!
	 ****************************************************************/
	void writeSerial() {
		/* OPERATING MODE FRAME */
		myPort.write("$0");
		if (mode == true)
			myPort.write("1");
		else
			myPort.write("0");
		myPort.write("!");

		/* RIGHT ENGINE DUTY CYCLE FRAME */
		myPort.write("$1");
		myPort.write(rightMotorDutyCycle);
		myPort.write("!");

		/* LEFT ENGINE DUTY CYCLE FRAME */
		myPort.write("$2");
		myPort.write(leftMotorDutyCycle);
		myPort.write("!");

		/* RIGHT ENGINE MOVEMENT DIRECTION */
		myPort.write("$3");
		myPort.write(moveDirectionRightMotor);
		myPort.write("!");

		/* LEFT ENGINE MOVEMENT DIRECTION */
		myPort.write("$4");
		myPort.write(moveDirectionLeftMotor);
		myPort.write("!");
	}
	
	public static void main(String[] args) {
		PApplet.main("Network");
	}
	
}