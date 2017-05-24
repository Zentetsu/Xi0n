package decisional;

/* Import de biblioth�ques =============*/
import view.robot.RobotConfig;
import tools.SensorValues;

/* Description de la classe ===============
Machine � �tat pour la prise de D�cision
=========================================*/
public class StateMachineTransitionForDecisionV1 {
	
// ========================================
// ATTRIBUTS
	
	// ------------------------------------
	// PARAMETERS -------------------------
    // ------------------------------------
	
	public static final RobotConfig WALL_FINDER_SPEED = new RobotConfig ( 200, 200, 1, 1 );
	public static final RobotConfig WALL_RIDER_SPEED = new RobotConfig ( 200, 200, 1, 1 );
	public static final RobotConfig EMERGENCY_STANDING_STILL_SPEED = new RobotConfig ( 0, 0, 2, 2 );
	public static final RobotConfig STANDING_STILL_SPEED = new RobotConfig ( 0, 0, 0, 0 );
	public static final RobotConfig STANDING_LEFT_ROTATION_SPEED = new RobotConfig ( 200, 200, -1, 1 );
	public static final RobotConfig STANDING_RIGHT_ROTATION_SPEED = new RobotConfig ( 200, 200, 1, -1 );
	
	public static final int rightSideSizeMemory = 20;
	public static final int frontSizeMemory = 20;
	
	public static final float thresholdFrontWallMinFinder = 20 ;
	public static final float thresholdFrontWallMin = 20 ;
	
	public static final float thresholdNoRightWallMaxFinder = 50;
	public static final float thresholdNoRightWallMax = 50;
	
	public static final float thresholdFrontWallRotationRightSide = 15;
	
	
	
	public static final float avarageDistanceRightWall = 15;
	
	
	public static final float thresholdNoRightWallRotationRightSide = 3;
	public static final float thresholdFrontWallRotationPostFinder = 20;
	
	
	//public static final float maxDistanceRightWall = 30;
	
	// ------------------------------------
    // SENSORS ----------------------------
    // ------------------------------------
	
	private float frontSensor;
	private float rightSideSensor;
	private int servoRotorPosition;
	
	// ------------------------------------
    // SENSORS MEMORY ---------------------
    // ------------------------------------
	
	private SensorValues rightSideValues;
	private SensorValues frontValues;
	
	// ------------------------------------
    // STATE MEMORY -----------------------
    // ------------------------------------
	
	private State pS;
	private State nS;
	
	// ------------------------------------
    // OUTPUTS ----------------------------
    // ------------------------------------
	
	private RobotConfig speeds = new RobotConfig ( 0,0,0,0 );
	
// ========================================	
// CONSTRUCTOR
	
	public StateMachineTransitionForDecisionV1 () {
		readSensors();
		pS = State.WALL_FINDER;
		nS = State.WALL_FINDER;
		rightSideValues = new SensorValues ( rightSideSizeMemory, rightSideSensor );
		frontValues = new SensorValues ( frontSizeMemory, rightSideSensor );
	}
	
// ========================================	
// METHODES
	
	// ------------------------------------
    // FONCTIONS PRINCIPALES --------------
    // ------------------------------------
	
	public void readSensors () {
		// TODO
	}
	
	public void readSensorsSimulation ( int i ) {
		// TODO : TO DELETE
		switch ( i ) {
		case 0 :
			frontSensor = 1000;
			rightSideSensor = 1000;
			servoRotorPosition = 1000;
			break;
		case 220 :
			frontSensor = 15;
			rightSideSensor = 1000;
			break;
		case 309 :
			frontSensor = 1000;
			rightSideSensor = 17;
			break;
		case 520 :
			frontSensor = 15;
			rightSideSensor = 1000;
			break;
		case 709 :
			frontSensor = 1000;
			rightSideSensor = 17;
			break;
		}
			
	}
	
	public RobotConfig getRobotConfig () {
		return ( speeds );
	}
	
	public State getState () {
		return ( pS );
	}
	
	public float getFrontSensor() {
		return frontSensor;
	}

	public void setFrontSensor(float frontSensor) {
		this.frontSensor = frontSensor;
	}

	public float getRightSideSensor() {
		return rightSideSensor;
	}

	public void setRightSideSensor(float rightSideSensor) {
		this.rightSideSensor = rightSideSensor;
	}

	public int getServoRotorPosition() {
		return servoRotorPosition;
	}

	public void setServoRotorPosition(int servoRotorPosition) {
		this.servoRotorPosition = servoRotorPosition;
	}
	
	/*public boolean isDifferent ( Calibration speeds ) {
		// TODO : TO DELETE
		return ( this.speeds.equals(speeds) );
	}*/
	
	/*
	private RobotConfig speedAdaptation () {
		
		float maxDiff = maxDistanceRightWall - avarageDistanceRightWall;
		float adaptedRightSideSensor = rightSideSensor;
		if ( adaptedRightSideSensor > maxDistanceRightWall )
			adaptedRightSideSensor = maxDistanceRightWall;
		else if ( adaptedRightSideSensor < 0 )
			adaptedRightSideSensor = 0;
		float diff = adaptedRightSideSensor - avarageDistanceRightWall;
		
		if ( diff == 0 )
			return ( speeds );
		if ( diff > 0 )
			return ( new RobotConfig (  ) )
	}*/
	
	/*
	public float generateX () {
		float maxDiff = maxDistanceRightWall - avarageDistanceRightWall;
		float adaptedRightSideSensor = rightSideSensor;
		if ( adaptedRightSideSensor > maxDistanceRightWall )
			adaptedRightSideSensor = maxDistanceRightWall;
		else if ( adaptedRightSideSensor < 0 )
			adaptedRightSideSensor = 0;
		float diff = adaptedRightSideSensor - avarageDistanceRightWall;
		
		float X = ((float)0.5)*(diff / maxDiff);
		
		return ( X );
	}*/
	
	// ------------------------------------
    // MAE BLOCS --------------------------
    // ------------------------------------
	
	/* Description des fonctions ----------
	D�termine le nouvel �tat de la machine
	*/
	public void FBloc () {
		switch ( pS ) {
		
		// �tat d'erreur majeur : la machine est pi�g�e dans cet �tat
		case EMERGENCY_STANDING_STILL :
			nS = State.EMERGENCY_STANDING_STILL;
			break;
		
		// �tat d'erreur mineur : la machine est pi�g�e dans cet �tat
		case STANDING_STILL :
			nS = State.STANDING_STILL;
			break;
			
		// �tat d'erreur mineur : le robot ne peut pas prendre seul une d�cision, il doit passr en mode manuel pour �tre extrait de sa position
		case MANUAL :
			// TODO : Waiting for controler command
			nS = State.MANUAL;
			break;
		
		// �tat permettant d'aller droit jusqu'� trouver un mur pour d�marrer la cartographie
		case WALL_FINDER :
			if ( frontSensor < thresholdFrontWallMinFinder )
				nS = State.FRONT_WALL_RIDER_ROTATION_POST_FINDER;
			else if ( rightSideSensor >= thresholdNoRightWallMaxFinder )
				nS = State.WALL_FINDER;
			break;
		
		// POUR LE TEST
		case WALL_RIDER :
			if ( frontSensor < thresholdFrontWallMin )
				nS = State.FRONT_WALL_RIDER_ROTATION_2;
			else if ( rightSideSensor >= thresholdNoRightWallMax )
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_1;
			else
				nS = State.WALL_RIDER;
			break;
		
		//�tat pour tourner � GAUCHE lorsque on rencontre un mur en face apr�s le wall finder
		case FRONT_WALL_RIDER_ROTATION_POST_FINDER :
			if ( rightSideSensor < thresholdFrontWallRotationPostFinder )
				nS = State.FRONT_WALL_RIDER_ROTATION_2;
			else
				nS = State.FRONT_WALL_RIDER_ROTATION_POST_FINDER;
			break;
			
		//�tat pour tourner � GAUCHE lorsque on rencontre un mur en face
		case FRONT_WALL_RIDER_ROTATION_2 :
			if ( rightSideSensor > thresholdFrontWallRotationRightSide )
				nS = State.WALL_RIDER;
			else
				nS = State.FRONT_WALL_RIDER_ROTATION_2;
			break;
			
		//�tat pour tourner � DROITE lorsque on perd le mur sur notre droite �tape 1
		case NO_RIGHT_WALL_RIDER_ROTATION_1 :
			if ( rightSideSensor < thresholdNoRightWallMax )
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_2;
			else
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_1;
			break;
		
		//�tat pour tourner � DROITE lorsque on perd le mur sur notre droite �tape 2
		case NO_RIGHT_WALL_RIDER_ROTATION_2 :
			if ( rightSideSensor > thresholdNoRightWallMax )
				nS = State.WALL_RIDER;
			else
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_2;
			break;
			
		// En cas d'erreur sur le typage on passe dans l'�tat des erreurs majeurs	
		default :
			nS = State.EMERGENCY_STANDING_STILL;
			break;
		}
	}
	
	/* Description des fonctions ----------
	Enregistre dans la m�moire toutes
	les informations n�cessaire sur
	les capteurs et l'�tat de la machien
	n�cessaire pour les prochaines
	executions du bloc F et G
	*/
	public void MBloc () {
		rightSideValues.add(rightSideSensor);
		frontValues.add(frontSensor);
		pS = nS;
	}
	
	/* Description des fonctions ----------
	Calcule la sortie souhait�e par la
	prise de d�cision, retourne les
	vitesses de chaque roue associ� �
	l'�tat propos�
	*/
	public RobotConfig GBloc () {
		switch ( pS ) {
		
		// �tat d'erreur majeur : la machine est pi�g�e dans cet �tat
		case EMERGENCY_STANDING_STILL :
			speeds = EMERGENCY_STANDING_STILL_SPEED;
			return ( EMERGENCY_STANDING_STILL_SPEED );
		
		// �tat d'erreur mineur : la machine est pi�g�e dans cet �tat
		case STANDING_STILL :
			speeds = STANDING_STILL_SPEED;
			return ( STANDING_STILL_SPEED );
		
		// �tat d'erreur mineur : le robot ne peut pas prendre seul une d�cision, il doit passr en mode manuel pour �tre extrait de sa position
		case MANUAL :
			// TODO : Waiting for controler command
			speeds = STANDING_STILL_SPEED;
			return ( STANDING_STILL_SPEED );
		
			// �tat permettant d'aller droit jusqu'� trouver un mur pour d�marrer la cartographie
		case WALL_FINDER :
			speeds = WALL_FINDER_SPEED;
			return ( WALL_FINDER_SPEED );
		
		//�tat pour longer un mur
		case WALL_RIDER :
			speeds = WALL_RIDER_SPEED;
			return ( WALL_RIDER_SPEED );
		
		case FRONT_WALL_RIDER_ROTATION_POST_FINDER :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
				
		//�tat pour tourner � GAUCHE lorsque on rencontre un mur en face
		case FRONT_WALL_RIDER_ROTATION_2 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
			
		//�tat pour tourner � DROITE lorsque on perd le mur sur notre droite
		case NO_RIGHT_WALL_RIDER_ROTATION_1 :
			speeds = STANDING_RIGHT_ROTATION_SPEED;
			return ( STANDING_RIGHT_ROTATION_SPEED );
			
		case NO_RIGHT_WALL_RIDER_ROTATION_2 :
			speeds = STANDING_RIGHT_ROTATION_SPEED;
			return ( STANDING_RIGHT_ROTATION_SPEED );
		
		// Consid�ration d'une erreur majeure
		default :
			speeds = EMERGENCY_STANDING_STILL_SPEED;
			return ( EMERGENCY_STANDING_STILL_SPEED );
			
		}
	}
	
// ========================================	
// MAIN PROGRAMME - TEST
	
	/*
	public static void main(String[] args) {
			
		// DECLARATION 
		StateMachineTransitionForDecisionV1 SMT = new StateMachineTransitionForDecisionV1 ();
		FilterCalibration FT = new FilterCalibration();
		RobotConfig speeds = new RobotConfig (0,0,0,0);
		RobotConfig calibratedSpeeds = new RobotConfig ( FT.filter(speeds) );
		//Calibration previousCalibratedSpeeds = new Calibration ( calibratedSpeeds );
			
		// CHARGEMENT DE L'ETALONNAGE
		boolean testLoad = FT.loadCalibrationFile();
		System.out.println("-------------- ETALONNAGE --------------");
		if ( testLoad )
			System.out.println(FT);
		else
			System.out.println("LOADING ERROR");
		System.out.println("----------------------------------------");
		
		System.out.println("--> | "+calibratedSpeeds+" |");
			
		// WHILE DE LA MAE
		for ( int i_simu = 0; true; i_simu++ ) {
			
			// lecture des valeurs de capteurs
			SMT.readSensors ();
			
			//lecture des valeurs des capteurs ( simulation )
			//SMT.readSensorsSimulation ( i_simu );
			//previousCalibratedSpeeds = calibratedSpeeds;
			
			// traitement par la machine � �tat pour la prise de d�cision
			SMT.FBloc();
			SMT.MBloc();
			speeds = SMT.GBloc();
				
			// �talonnage de vitesse demand�e
			calibratedSpeeds = FT.filter(speeds) ;
			
			// transmission de la vitesse
			System.out.println("--> | "+calibratedSpeeds+" |");
			
			//transmission de la vitesse ( simultation )
			//if ( SMT.isDifferent(previousCalibratedSpeeds) )
			//	System.out.println("\n--> | "+calibratedSpeeds+" |");
			//else {
			//	if ( i_simu/20 == 0 )
			//		System.out.print("|");
			}
			
		}
	} */
	
//========================================	
	
}