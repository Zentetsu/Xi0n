package network;

/*******************************************************
              LIBRAIRIES IMPORTATION
 *******************************************************/
import processing.core.PApplet;
import processing.serial.Serial;
import robot_directing.InputManager;



public class XbeeSerialCommunication extends PApplet implements Runnable{

	/*******************************************************
	 * GLOBAL STATES VARIABLES
	 *******************************************************/

	public boolean isAutomatic;          // Operating mode: Auto = true, Manual = false
	public int infraredRemote;           // Distance given by the infra-red sensor
	public int ultrasoundRemote;         // Distance given by the ultrasound sensor
	public int scanAngle;                // Sweep angle of the servomotor
	private int rightMotorDutyCycle;      // Duty cycle of the right motor
	private int leftMotorDutyCycle;       // Duty cycle of the left motor
	private int moveDirectionRightMotor;  // Move direction to the right motor
	private int moveDirectionLeftMotor;   // Move direction to the left motor
	public Serial myPort;                // xbee serial communication on the computer side

	/*******************************************************
	 * COMMUNICATION VARIABLES
	 *******************************************************/
	boolean frameStart = false;
	boolean frameSelectType = false;
	boolean frameGetValue = false;
	boolean frameFinish = false;
	boolean frameValid = false;
	private boolean isConnected;
	int frameType;
	int frameValue;
	int increment;

	/*******************************************************
	 * MAIN PROGRAM SETTINGS
	 *******************************************************/

	public XbeeSerialCommunication(boolean isAutomatic) {
		// Xbee serial initialization
		String[] myList = Serial.list();
		this.isAutomatic = isAutomatic;
		if (myList.length > 0){
			this.myPort = new Serial(this, myList[0], 9600);
			Thread com = new Thread(this);
			com.start();
			this.isConnected = true;
		}
		else
			this.isConnected = false;
	}
	
	public boolean isConnected() {
		return this.isConnected;
	}	

	/*******************************************************
	 * MAIN PROGRAM LOOP
	 *******************************************************/
	private void update() {
		while (this.myPort.available() > 0) {
			readSerial();
			//System.out.println("RIGHT MOTOR : " + this.rightMotorDutyCycle + " " + this.moveDirectionRightMotor);
			//System.out.println("LEFT  MOTOR : " + this.leftMotorDutyCycle + " " + this.moveDirectionLeftMotor);
		}
		
		writeSerial();
	}

	@Override
	public void run() {
		while(true){
			this.update();
			//delay(10);
		}

	}

	/*******************************************************
	 * READ SERIAL FUNCTION
	 * 
	 * FRAME TYPES: => Ultrasound remote: $0A! => Infra-red remote $1A! => Scan
	 * angle: $2A!
	 *******************************************************/
	private void readSerial() {
		// Read the data on serial
		int n = myPort.read();
		char serialValue = (char) n;
		//System.out.println(n);

		// Frame Decoding
		if (frameStart == false) {
			if (serialValue == '$') {
				frameStart = true;
				increment = 0;
				//System.out.println("$ detect");
			} else
				frameFinish = true;
		} else {
			increment++;
			if (frameSelectType == false) {
				frameType = serialValue;
				if (frameType == '0') {
					//System.out.println("Changing ultrasound remote");
					frameSelectType = true;
				} else if (frameType == '1') {
					//System.out.println("Changing infra-red remote");
					frameSelectType = true;
				} else if (frameType == '2') {
					//System.out.println("Changing scan angle");
					frameSelectType = true;
				} else {
					//System.out.println("TYPE ERROR");
					frameFinish = true;
				}
			} else if (frameGetValue == false) {
				frameValue = serialValue;
				//System.out.print("Value save: ");
				//System.out.println(frameValue);
				frameGetValue = true;
			} else if (frameFinish == false) {
				if (serialValue == '!') {
					frameValid = true;
					//System.out.println("The Frame Is Valid");
				} else {
					//System.out.println("The Frame Isn't Valid");
				}
				frameFinish = true;
			}
		}

		if ((frameFinish == true) || (increment == 3)) {
			if (frameValid == true) {
				//System.out.println("DATA UPDATE...");
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
				//System.out.println("DATA UPDATED");
			} else {
				//System.out.println("FRAME ERROR");
			}

			/* INITIALIZATION FOR THE NEXT FRAME */
			//System.out.println("INITIALIZATION FOR THE NEXT FRAME");
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
	private void writeSerial() {
		/* OPERATING MODE FRAME */
		myPort.write("$0");
		if (this.isAutomatic)
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
		myPort.write((char)leftMotorDutyCycle);
		myPort.write("!");

		/* RIGHT ENGINE MOVEMENT DIRECTION */
		myPort.write("$3");
		myPort.write((char)moveDirectionRightMotor);
		myPort.write("!");

		/* LEFT ENGINE MOVEMENT DIRECTION */
		myPort.write("$4");
		myPort.write((char)moveDirectionLeftMotor);
		myPort.write("!");
	}

	// UPDATE DE VALUES OF MOTORS
	public void update(InputManager input) {
		// RIGHT MOTOR
		if (input.RIGHT > 0){
			this.moveDirectionRightMotor = 1; // FORWARD
		}
		else if (input.RIGHT < 0){
			this.moveDirectionRightMotor = 2; // BACKWARD
		}
		else {
			this.moveDirectionRightMotor = 0; // STOP
		}
		this.rightMotorDutyCycle = Math.round(Math.abs(input.RIGHT));
		
		
		// LEFT MOTOR
		if (input.LEFT > 0){
			this.moveDirectionLeftMotor = 1; // FORWARD
		}
		else if (input.LEFT < 0){
			this.moveDirectionLeftMotor = 2; // BACKWARD
		}
		else {
			this.moveDirectionLeftMotor = 0; // STOP
		}
		this.leftMotorDutyCycle = Math.round(Math.abs(input.LEFT));
		//this.isAutomatic = input.getMode();
	}
}