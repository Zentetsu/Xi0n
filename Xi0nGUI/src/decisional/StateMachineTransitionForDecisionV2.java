package decisional;

/* Import de biblioth�ques =============*/
import view.robot.RobotConfig;
import view.robot.RobotConstant;
import view.Xi0nSimulation;
import view.robot.LateralSensor;
import tools.Chrono;

/* Description de la classe ===============
Machine � �tat pour la prise de D�cision
=========================================*/
public class StateMachineTransitionForDecisionV2 {
	
// ========================================
// ATTRIBUTS
	
	// ------------------------------------
	// PARAMETERS -------------------------
    // ------------------------------------
	
	public static final RobotConfig WALL_FINDER_SPEED = new RobotConfig ( 200, 200, 1, 1 );
	public static final RobotConfig WALL_RIDER_SPEED = new RobotConfig ( 200, 200, 1, 1 );
	public static final RobotConfig WALL_RIDER_AWAY_SPEED = new RobotConfig ( 200, 50, 1, 1 );
	public static final RobotConfig WALL_RIDER_NEAR_SPEED = new RobotConfig ( 50, 200, 1, 1 );
	public static final RobotConfig WALL_RIDER_AWAY_BACK_SPEED = new RobotConfig ( WALL_RIDER_AWAY_SPEED.getRightPower0to255(), WALL_RIDER_AWAY_SPEED.getLeftPower0to255(), 1, 1 );
	public static final RobotConfig WALL_RIDER_NEAR_BACK_SPEED = new RobotConfig ( WALL_RIDER_NEAR_SPEED.getRightPower0to255(), WALL_RIDER_NEAR_SPEED.getLeftPower0to255(), 1, 1 );
	public static final RobotConfig EMERGENCY_STANDING_STILL_SPEED = new RobotConfig ( 0, 0, 2, 2 );
	public static final RobotConfig STANDING_STILL_SPEED = new RobotConfig ( 0, 0, 0, 0 );
	public static final RobotConfig STANDING_LEFT_ROTATION_SPEED = new RobotConfig ( 200, 200, -1, 1 );
	public static final RobotConfig STANDING_RIGHT_ROTATION_SPEED = new RobotConfig ( 200, 200, 1, -1 );
	
	public static final float thresholdAngle = 5;
	
	// ------------------------------------
    // SENSORS ----------------------------
    // ------------------------------------
	
	private int rightSideDistance;
	private int frontalDistance;
	private float servoAngle;
	
	// ------------------------------------
    // CHRONOMETER ------------------------
    // ------------------------------------
	
	private Chrono chrono = new Chrono ();
	
	// ------------------------------------
    // SENSORS MEMORY ---------------------
    // ------------------------------------
	
	// ------------------------------------
    // STATE MEMORY -----------------------
    // ------------------------------------
	
	private State2 pS;
	private State2 nS;
	
	// ------------------------------------
    // OUTPUTS ----------------------------
    // ------------------------------------
	
	private RobotConfig speeds = new RobotConfig ( 0,0,0,0 );
	
// ========================================	
// CONSTRUCTOR
	
	public StateMachineTransitionForDecisionV2 () {
		readSensors();
		pS = State2.WALL_FINDER;
		nS = State2.WALL_FINDER;
	}
	
// ========================================	
// METHODES
	
	// ------------------------------------
    // FONCTIONS PRINCIPALES --------------
    // ------------------------------------
	
	public void reset () {
		readSensors();
		pS = State2.WALL_FINDER;
		nS = State2.WALL_FINDER;
	}
	
	public void readSensors () {
		rightSideDistance = Xi0nSimulation.INSTANCE.getLateralDistance();
		frontalDistance = Xi0nSimulation.INSTANCE.getFrontalDistance();
		servoAngle = Xi0nSimulation.INSTANCE.getSensorAngle();
	}
	
	public RobotConfig getRobotConfig () {
		return ( speeds );
	}
	
	public State2 getState () {
		return ( pS );
	}
	
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
			nS = State2.EMERGENCY_STANDING_STILL;
			break;
		
		// �tat d'erreur mineur : la machine est pi�g�e dans cet �tat
		case STANDING_STILL :
			nS = State2.STANDING_STILL;
			break;
			
		// �tat d'erreur mineur : le robot ne peut pas prendre seul une d�cision, il doit passr en mode manuel pour �tre extrait de sa position
		case MANUAL :
			// TODO : Waiting for controler command
			nS = State2.MANUAL;
			break;
		
		// �tat permettant d'aller droit jusqu'� trouver un mur pour d�marrer la cartographie
		case WALL_FINDER :
			if ( frontalDistance <= RobotConstant.HEIGHT && rightSideDistance > LateralSensor.WARNING_LENGTH && servoAngle < thresholdAngle && servoAngle > (-1)*thresholdAngle )
				nS = State2.FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL;
			else if ( frontalDistance <= RobotConstant.HEIGHT && rightSideDistance > LateralSensor.WARNING_LENGTH && servoAngle < thresholdAngle && servoAngle > (-1)*thresholdAngle )
				nS = State2.FRONT_WALL_RIDER_ROTATION;
			else if ( rightSideDistance <= LateralSensor.WARNING_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State2.WALL_RIDER;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State2.WALL_RIDER_NEAR;
			else
				nS = State2.WALL_FINDER;
			break;
		
		// �tat de suivi des murs
		case WALL_RIDER :
			// TODO : RIGHT ROTATION
			if ( frontalDistance <= RobotConstant.HEIGHT && rightSideDistance > LateralSensor.WARNING_LENGTH && servoAngle < thresholdAngle && servoAngle > (-1)*thresholdAngle )
				nS = State2.FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL;
			else if ( frontalDistance <= RobotConstant.HEIGHT && rightSideDistance <= LateralSensor.WARNING_LENGTH && servoAngle < thresholdAngle && servoAngle > (-1)*thresholdAngle )
				nS = State2.FRONT_WALL_RIDER_ROTATION;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State2.WALL_RIDER_AWAY;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State2.WALL_RIDER_NEAR;
			else
				nS = State2.WALL_RIDER;
			break;
			
		// �tat de suivi des murs lorqu'on s'en �loigne
		case WALL_RIDER_AWAY :
			// TODO : RIGHT ROTATION
			if ( frontalDistance <= RobotConstant.HEIGHT && rightSideDistance > LateralSensor.WARNING_LENGTH && servoAngle < thresholdAngle && servoAngle > (-1)*thresholdAngle )
				nS = State2.FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL;
			else if ( frontalDistance <= RobotConstant.HEIGHT && rightSideDistance <= LateralSensor.WARNING_LENGTH && servoAngle < thresholdAngle && servoAngle > (-1)*thresholdAngle )
				nS = State2.FRONT_WALL_RIDER_ROTATION;
			else if ( rightSideDistance <= LateralSensor.WARNING_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State2.WALL_RIDER;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State2.WALL_RIDER_NEAR;
			else
				nS = State2.WALL_RIDER_AWAY;
			break;
			
		// �tat de suivi des murs lorsqu'on s'en rapproche
		case WALL_RIDER_NEAR :
			// TODO : RIGHT ROTATION
			if ( frontalDistance <= RobotConstant.HEIGHT && rightSideDistance > LateralSensor.WARNING_LENGTH && servoAngle < thresholdAngle && servoAngle > (-1)*thresholdAngle )
				nS = State2.FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL;
			else if ( frontalDistance <= RobotConstant.HEIGHT && rightSideDistance <= LateralSensor.WARNING_LENGTH && servoAngle < thresholdAngle && servoAngle > (-1)*thresholdAngle )
				nS = State2.FRONT_WALL_RIDER_ROTATION;
			else if ( rightSideDistance <= LateralSensor.WARNING_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State2.WALL_RIDER;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State2.WALL_RIDER_AWAY;
			else
				nS = State2.WALL_RIDER_NEAR;
			break;
		
		//�tat pour tourner � GAUCHE lorsque on rencontre un mur en face apr�s le wall finder
		case FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL :
			if ( rightSideDistance <= LateralSensor.WARNING_LENGTH && rightSideDistance <= LateralSensor.WARNING_LENGTH )
				nS = State2.FRONT_WALL_RIDER_ROTATION;
			else
				nS = State2.FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL;
			break;

			
		//�tat pour tourner � GAUCHE lorsque on rencontre un mur en face
		case FRONT_WALL_RIDER_ROTATION :
			if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State2.WALL_RIDER;
			else
				nS = State2.FRONT_WALL_RIDER_ROTATION;
			break;
		
		//�tat pour tourner � DROITE lorsque on perd le mur sur notre droite �tape 1
		// TODO
		case NO_RIGHT_WALL_RIDER_ROTATION :
			if ( rightSideDistance < LateralSensor.WARNING_LENGTH && rightSideDistance >= LateralSensor.STOP_LENGTH )
				nS = State2.NO_RIGHT_WALL_RIDER_ROTATION;
			else
				nS = State2.NO_RIGHT_WALL_RIDER_ROTATION;
			break;
			
		// En cas d'erreur sur le typage on passe dans l'�tat des erreurs majeurs	
		default :
			nS = State2.EMERGENCY_STANDING_STILL;
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
			
		//�tat pour longer un mur lorsque l'on s'en �loigne
		case WALL_RIDER_AWAY :
			speeds = WALL_RIDER_AWAY_SPEED;
			return ( WALL_RIDER_AWAY_SPEED );
				
			//�tat pour longer un mur lorsque l'on s'en rapproche
		case WALL_RIDER_NEAR :
			speeds = WALL_RIDER_NEAR_SPEED;
			return ( WALL_RIDER_NEAR_SPEED );
		
		case FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
				
		//�tat pour tourner � GAUCHE lorsque on rencontre un mur en face
		case FRONT_WALL_RIDER_ROTATION :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
			
		//�tat pour tourner � DROITE lorsque on perd le mur sur notre droite
		case NO_RIGHT_WALL_RIDER_ROTATION :
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