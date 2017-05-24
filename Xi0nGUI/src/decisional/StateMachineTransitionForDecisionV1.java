package decisional;

/* Import de bibliothï¿½ques =============*/
import view.robot.RobotConfig;
import tools.SensorValues;

/* Description de la classe ===============
Machine ï¿½ ï¿½tat pour la prise de Dï¿½cision
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
	
	public static final float thresholdFrontWallRotationRightSide = 20;
	
	
	
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
		// WALL FINDER
		case 220 :
			frontSensor = 15;
			rightSideSensor = 1000;
			break;
		// LEFT ROT POST WALL FINDER
		case 280 :
			frontSensor = 1000;
			rightSideSensor = 17;
			break;
		// LEFT ROT
		case 310 :
			frontSensor = 1000;
			rightSideSensor = 22;
			break;
		// WALL RIDER
		case 520 :
			frontSensor = 15;
			rightSideSensor = 1000;
			break;
		// LEFT ROT
		case 709 :
			frontSensor = 1000;
			rightSideSensor = 17;
			break;
		// WALL RINDER
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
	Dï¿½termine le nouvel ï¿½tat de la machine
	*/
	public void FBloc () {
		switch ( pS ) {
		
		// ï¿½tat d'erreur majeur : la machine est piï¿½gï¿½e dans cet ï¿½tat
		case EMERGENCY_STANDING_STILL :
			nS = State.EMERGENCY_STANDING_STILL;
			break;
		
		// ï¿½tat d'erreur mineur : la machine est piï¿½gï¿½e dans cet ï¿½tat
		case STANDING_STILL :
			nS = State.STANDING_STILL;
			break;
			
		// ï¿½tat d'erreur mineur : le robot ne peut pas prendre seul une dï¿½cision, il doit passr en mode manuel pour ï¿½tre extrait de sa position
		case MANUAL :
			// TODO : Waiting for controler command
			nS = State.MANUAL;
			break;
		
		// ï¿½tat permettant d'aller droit jusqu'ï¿½ trouver un mur pour dï¿½marrer la cartographie
		case WALL_FINDER :
			if ( frontSensor < thresholdFrontWallMinFinder )
				nS = State.FRONT_WALL_RIDER_ROTATION_POST_FINDER;
			/*
			// TODO
			else if ( rightSideValues.get(rightSideValues.size()-2) < thresholdNoRightWallMaxFinder && rightSideSensor >= thresholdNoRightWallMaxFinder )
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_1;
			*/
			else
				nS = State.WALL_FINDER;
			break;
		
		// état de suvit des murs
		case WALL_RIDER :
			if ( frontSensor < thresholdFrontWallMin )
				nS = State.FRONT_WALL_RIDER_ROTATION_2;
			else if ( rightSideSensor >= thresholdNoRightWallMax )
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_1;
			else
				nS = State.WALL_RIDER;
			break;
		
		//ï¿½tat pour tourner ï¿½ GAUCHE lorsque on rencontre un mur en face aprï¿½s le wall finder
		case FRONT_WALL_RIDER_ROTATION_POST_FINDER :
			if ( frontSensor < thresholdFrontWallRotationPostFinder )

				nS = State.FRONT_WALL_RIDER_ROTATION_2;
			else
				nS = State.FRONT_WALL_RIDER_ROTATION_POST_FINDER;
			break;

		/////////////
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face
		case FRONT_WALL_RIDER_ROTATION_2 :
			if ( rightSideSensor > thresholdFrontWallRotationRightSide )
				nS = State.WALL_RIDER;
			else
				nS = State.FRONT_WALL_RIDER_ROTATION_2;
			break;
			
		//ï¿½tat pour tourner ï¿½ DROITE lorsque on perd le mur sur notre droite ï¿½tape 1
		case NO_RIGHT_WALL_RIDER_ROTATION_1 :
			if ( rightSideSensor < thresholdNoRightWallMax )
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_2;
			else
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_1;
			break;
		
		//ï¿½tat pour tourner ï¿½ DROITE lorsque on perd le mur sur notre droite ï¿½tape 2
		case NO_RIGHT_WALL_RIDER_ROTATION_2 :
			if ( rightSideSensor > thresholdNoRightWallMax )
				nS = State.WALL_RIDER;
			else
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_2;
			break;
			
		// En cas d'erreur sur le typage on passe dans l'ï¿½tat des erreurs majeurs	
		default :
			nS = State.EMERGENCY_STANDING_STILL;
			break;
		}
	}
	
	/* Description des fonctions ----------
	Enregistre dans la mï¿½moire toutes
	les informations nï¿½cessaire sur
	les capteurs et l'ï¿½tat de la machien
	nï¿½cessaire pour les prochaines
	executions du bloc F et G
	*/
	public void MBloc () {
		rightSideValues.add(rightSideSensor);
		frontValues.add(frontSensor);
		pS = nS;
	}
	
	/* Description des fonctions ----------
	Calcule la sortie souhaitï¿½e par la
	prise de dï¿½cision, retourne les
	vitesses de chaque roue associï¿½ ï¿½
	l'ï¿½tat proposï¿½
	*/
	public RobotConfig GBloc () {
		switch ( pS ) {
		
		// ï¿½tat d'erreur majeur : la machine est piï¿½gï¿½e dans cet ï¿½tat
		case EMERGENCY_STANDING_STILL :
			speeds = EMERGENCY_STANDING_STILL_SPEED;
			return ( EMERGENCY_STANDING_STILL_SPEED );
		
		// ï¿½tat d'erreur mineur : la machine est piï¿½gï¿½e dans cet ï¿½tat
		case STANDING_STILL :
			speeds = STANDING_STILL_SPEED;
			return ( STANDING_STILL_SPEED );
		
		// ï¿½tat d'erreur mineur : le robot ne peut pas prendre seul une dï¿½cision, il doit passr en mode manuel pour ï¿½tre extrait de sa position
		case MANUAL :
			// TODO : Waiting for controler command
			speeds = STANDING_STILL_SPEED;
			return ( STANDING_STILL_SPEED );
		
			// ï¿½tat permettant d'aller droit jusqu'ï¿½ trouver un mur pour dï¿½marrer la cartographie
		case WALL_FINDER :
			speeds = WALL_FINDER_SPEED;
			return ( WALL_FINDER_SPEED );
		
		//ï¿½tat pour longer un mur
		case WALL_RIDER :
			speeds = WALL_RIDER_SPEED;
			return ( WALL_RIDER_SPEED );
		
		case FRONT_WALL_RIDER_ROTATION_POST_FINDER :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
				
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face
		case FRONT_WALL_RIDER_ROTATION_2 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
			
		//ï¿½tat pour tourner ï¿½ DROITE lorsque on perd le mur sur notre droite
		case NO_RIGHT_WALL_RIDER_ROTATION_1 :
			speeds = STANDING_RIGHT_ROTATION_SPEED;
			return ( STANDING_RIGHT_ROTATION_SPEED );
			
		case NO_RIGHT_WALL_RIDER_ROTATION_2 :
			speeds = STANDING_RIGHT_ROTATION_SPEED;
			return ( STANDING_RIGHT_ROTATION_SPEED );
		
		// Considï¿½ration d'une erreur majeure
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
			
			// traitement par la machine ï¿½ ï¿½tat pour la prise de dï¿½cision
			SMT.FBloc();
			SMT.MBloc();
			speeds = SMT.GBloc();
				
			// ï¿½talonnage de vitesse demandï¿½e
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