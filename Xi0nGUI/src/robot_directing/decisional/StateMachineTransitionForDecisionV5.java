package robot_directing.decisional;

import gui.Xi0nSimulation;
import physic.robot.FrontalSensor;
import physic.robot.LateralSensor;
import physic.robot.RobotConfig;
import tools.Chrono;

/* Description de la classe ===============
Machine ï¿½ ï¿½tat pour la prise de Dï¿½cision
=========================================*/
public class StateMachineTransitionForDecisionV5 {
	
// ========================================
// ATTRIBUTS
	
	// ------------------------------------
	// PARAMETERS -------------------------
    // ------------------------------------
	
	public static final RobotConfig WALL_FINDER_SPEED = new RobotConfig ( 200, 200, 1, 1 );
	public static final RobotConfig WALL_RIDER_SPEED = new RobotConfig ( 200, 200, 1, 1 );
	public static final RobotConfig WALL_RIDER_AWAY_SPEED = new RobotConfig ( 200, 150, 1, 1 );
	public static final RobotConfig WALL_RIDER_NEAR_SPEED = new RobotConfig ( 150, 200, 1, 1 );
	public static final RobotConfig WALL_RIDER_AWAY_BACK_SPEED = new RobotConfig ( WALL_RIDER_AWAY_SPEED.getRightPower0to255(), WALL_RIDER_AWAY_SPEED.getLeftPower0to255(), 1, 1 );
	public static final RobotConfig WALL_RIDER_NEAR_BACK_SPEED = new RobotConfig ( WALL_RIDER_NEAR_SPEED.getRightPower0to255(), WALL_RIDER_NEAR_SPEED.getLeftPower0to255(), 1, 1 );
	public static final RobotConfig WALL_RIDER_FAR_AWAY_SPEED = new RobotConfig ( 255, 102, 1, 1 );
	public static final RobotConfig EMERGENCY_STANDING_STILL_SPEED = new RobotConfig ( 0, 0, 2, 2 );
	public static final RobotConfig STANDING_STILL_SPEED = new RobotConfig ( 0, 0, 0, 0 );
	public static final RobotConfig STANDING_LEFT_ROTATION_SPEED = new RobotConfig ( 150, 150, -1, 1 );
	public static final RobotConfig STANDING_RIGHT_ROTATION_SPEED = new RobotConfig ( 150, 150, 1, -1 );
	
	public static final float THRESHOLD_ANGLE = 35;
	
	public static final int MD_LEFT_ROT_1_2_MAX = 1000;
	
	// ------------------------------------
    // SENSORS ----------------------------
    // ------------------------------------
	
	private int rightSideDistance;
	private int frontalDistance;
	private float servoAngle;
	
	// ------------------------------------
    // CHRONOMETER ------------------------
    // ------------------------------------
	
	private Chrono chronoWallRider = new Chrono ();
	private Chrono chronoLeftRot12 = new Chrono ();
	
	// ------------------------------------
    // SENSORS MEMORY ---------------------
    // ------------------------------------
	
	private long memorisedDurationWallRider_1;
	private long memorisedDurationWallRider_2;
	private long memorisedDurationLeftRot12;
	
	// ------------------------------------
    // STATE MEMORY -----------------------
    // ------------------------------------
	
	private State5 pS;
	private State5 nS;
	
	// ------------------------------------
    // OUTPUTS ----------------------------
    // ------------------------------------
	
	private RobotConfig speeds = new RobotConfig ( 0,0,0,0 );
	
// ========================================	
// CONSTRUCTOR
	
	public StateMachineTransitionForDecisionV5 () {
		readSensorsSimu();
		pS = State5.FINDER;
		nS = State5.FINDER;
		memorisedDurationWallRider_1 = 0;
		memorisedDurationWallRider_2 = 0;
		memorisedDurationLeftRot12 = 0;
	}
	
// ========================================	
// METHODES
	
	// ------------------------------------
    // FONCTIONS PRINCIPALES --------------
    // ------------------------------------
	
	public void reset () {
		readSensorsSimu();
		pS = State5.FINDER;
		nS = State5.FINDER;
		memorisedDurationWallRider_1 = 0;
		memorisedDurationWallRider_2 = 0;
		memorisedDurationLeftRot12 = 0;
	}
	
	public void readSensorsSimu () {
		rightSideDistance = Xi0nSimulation.INSTANCE.getLateralDistance();
		frontalDistance = Xi0nSimulation.INSTANCE.getFrontalDistance();
		servoAngle = Xi0nSimulation.INSTANCE.getSensorAngle();
	}
	
	public void readSensorsCapteur () {
		// TODO
	}
	
	public RobotConfig getRobotConfig () {
		return ( speeds );
	}
	
	public State5 getState () {
		return ( pS );
	}
	
	// ------------------------------------
    // MAE BLOCS --------------------------
    // ------------------------------------
	
	
	/* Description des fonctions ----------
	Dï¿½termine le nouvel ï¿½tat de la machine
	*/
	public void FBloc () {
		
		System.out.println(pS);
		
		switch ( pS ) {
		
		// ï¿½tat d'erreur majeur : la machine est piï¿½gï¿½e dans cet ï¿½tat
		case EMERGENCY_STAND :
			nS = State5.EMERGENCY_STAND;
			break;
		
		// ï¿½tat d'erreur mineur : la machine est piï¿½gï¿½e dans cet ï¿½tat
		case STAND :
			nS = State5.STAND;
			break;
			
		// ï¿½tat d'erreur mineur : le robot ne peut pas prendre seul une dï¿½cision, il doit passr en mode manuel pour ï¿½tre extrait de sa position
		case MANUAL :
			// TODO : Waiting for controler command
			nS = State5.MANUAL;
			break;
		
		// ï¿½tat permettant d'aller droit jusqu'ï¿½ trouver un mur pour dï¿½marrer la cartographie
		case FINDER :
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.WARNING_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_NO_RIGHT_WALL;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.WARNING_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_1_1;
			else if ( rightSideDistance <= LateralSensor.WARNING_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State5.RIDER;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.RIDER_NEAR;
			else
				nS = State5.FINDER;
			break;
		
		// état de suivi des murs
		case RIDER :
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_1_1;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State5.RIGHT_ROT_1;
			else if ( rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State5.RIDER_AWAY;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.RIDER_NEAR;
			else
				nS = State5.RIDER;
			break;
			
		// état de suivi des murs lorqu'on s'en éloigne
		case RIDER_AWAY :
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_1_1;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State5.RIDER_FAR_AWAY;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.RIDER_AWAY_BACK;
			else
				nS = State5.RIDER_AWAY;
			break;
		
		// état de suivi des murs lorqu'on se rapproche après s'être éloigné
		case RIDER_AWAY_BACK :
			chronoWallRider.stop();
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_1_1;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State5.RIGHT_ROT_1;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.RIDER_NEAR;
			else if ( this.memorisedDurationWallRider_2 > ( this.memorisedDurationWallRider_1 / 2 ) )
				nS = State5.RIDER;
			else
				nS = State5.RIDER_AWAY_BACK;
			chronoWallRider.resume();
			break;
			
		// état de suivi des murs lorqu'on s'est beaucou éloigné
		case RIDER_FAR_AWAY :
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_1_1;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance <= LateralSensor.WARNING_LENGTH )
				nS = State5.RIDER_FAR_AWAY;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.RIDER_NEAR;
			else
				nS = State5.RIDER_FAR_AWAY;
			
			
		// état de suivi des murs lorsqu'on s'en rapproche
		case RIDER_NEAR :
			// TODO : RIGHT ROTATION
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_1_1;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State5.RIGHT_ROT_1;
			else if ( rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State5.RIDER_NEAR_BACK;
			else
				nS = State5.RIDER_NEAR;
			break;
			
		// état de suivi des murs lorsqu'on s'en éloigne après s'être rapproché
		case RIDER_NEAR_BACK :
			// TODO : RIGHT ROTATION
			chronoWallRider.stop();
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_1_1;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State5.RIGHT_ROT_1;
			else if ( rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State5.RIDER_AWAY;
			else if ( this.memorisedDurationWallRider_2 > ( this.memorisedDurationWallRider_1 / 2 ) )
				nS = State5.RIDER;
			else
				nS = State5.RIDER_NEAR_BACK;
			chronoWallRider.resume();
			break;
		
		//ï¿½tat pour tourner ï¿½ GAUCHE lorsque on rencontre un mur en face aprï¿½s le wall finder
		case LEFT_ROT_NO_RIGHT_WALL :
			if ( rightSideDistance <= LateralSensor.WARNING_LENGTH )
				nS = State5.LEFT_ROT_1_1;
			else
				nS = State5.LEFT_ROT_NO_RIGHT_WALL;
			break;

			
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face étape 1
		case LEFT_ROT_1_1 :
			if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State5.LEFT_ROT_1_2;
			else
				nS = State5.LEFT_ROT_1_1;
			break;
			
		case LEFT_ROT_1_2 :
			if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance <= LateralSensor.WARNING_LENGTH )
				nS = State5.LEFT_ROT_1_3;
			else if ( MD_LEFT_ROT_1_2_MAX <= memorisedDurationLeftRot12 )
				nS = State5.RIGHT_ROT_1;
			else
				nS = State5.LEFT_ROT_1_2;
			break;
			
		case LEFT_ROT_1_3 :
			if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance <= LateralSensor.WARNING_LENGTH )
				nS = State5.LEFT_ROT_1_1;
			else
				nS = State5.LEFT_ROT_1_3;
			break;
			
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face étape 2
		case LEFT_ROT_2 :
			if ( rightSideDistance <= LateralSensor.WARNING_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State5.LEFT_ROT_1_1;
			else if ( rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State5.RIDER_AWAY;
			else
				nS = State5.LEFT_ROT_2;
			break;
		
		//ï¿½tat pour tourner ï¿½ DROITE lorsque on perd le mur sur notre droite ï¿½tape 1
		case RIGHT_ROT_1 :
			if ( rightSideDistance <= LateralSensor.WARNING_LENGTH )
				nS = State5.RIGHT_ROT_2;
			else
				nS = State5.RIGHT_ROT_1;
			break;
			
		//ï¿½tat pour tourner ï¿½ DROITE lorsque on perd le mur sur notre droite ï¿½tape 2
		case RIGHT_ROT_2 :
			if ( rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State5.RIDER;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.LEFT_ROT_1_1;
			else
				nS = State5.RIGHT_ROT_2;
			break;
		
		// En cas d'erreur sur le typage on passe dans l'ï¿½tat des erreurs majeurs	
		default :
			nS = State5.EMERGENCY_STAND;
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
		
		if ( nS != pS ) {
			switch ( nS ) {
			case RIDER_AWAY:
			case RIDER_NEAR:
			case RIDER_AWAY_BACK:
			case RIDER_NEAR_BACK:
				switch ( pS ) {
				case RIDER_AWAY:
				case RIDER_NEAR:
				case RIDER_AWAY_BACK:
				case RIDER_NEAR_BACK:
					chronoWallRider.stop();
					//chrono = new Chrono();
					chronoWallRider.start();
					break;
				default:
					chronoWallRider.start();
					break;
				}
				break;
			default:
				memorisedDurationWallRider_1 = 0;
				memorisedDurationWallRider_2 = 0;
				break;
			}
		}
		else {
			switch ( nS ) {
			case RIDER_AWAY:
			case RIDER_NEAR:
				chronoWallRider.stop();
				memorisedDurationWallRider_1 += chronoWallRider.getDurationMs();
				chronoWallRider.resume();
				break;
			case RIDER_AWAY_BACK:
			case RIDER_NEAR_BACK:
				chronoWallRider.stop();
				memorisedDurationWallRider_2 += chronoWallRider.getDurationMs();
				chronoWallRider.resume();
				break;
			default:
				break;
			}
		}
		
		System.out.println(memorisedDurationLeftRot12);
		
		if ( pS != State5.LEFT_ROT_1_2 &&  nS == State5.LEFT_ROT_1_2 ) {
			chronoLeftRot12.start();
		}
		if ( pS == State5.LEFT_ROT_1_2 &&  nS == State5.LEFT_ROT_1_2 ) {
			chronoLeftRot12.stop();
			memorisedDurationLeftRot12 += chronoLeftRot12.getDurationMs();
			chronoLeftRot12.resume();
		}
		if ( pS == State5.LEFT_ROT_1_2 &&  nS != State5.LEFT_ROT_1_2 ) {
			chronoLeftRot12.stop();
			memorisedDurationLeftRot12 = 0;
		}
		
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
		case EMERGENCY_STAND :
			speeds = EMERGENCY_STANDING_STILL_SPEED;
			return ( EMERGENCY_STANDING_STILL_SPEED );
		
		// ï¿½tat d'erreur mineur : la machine est piï¿½gï¿½e dans cet ï¿½tat
		case STAND :
			speeds = STANDING_STILL_SPEED;
			return ( STANDING_STILL_SPEED );
		
		// ï¿½tat d'erreur mineur : le robot ne peut pas prendre seul une dï¿½cision, il doit passr en mode manuel pour ï¿½tre extrait de sa position
		case MANUAL :
			// TODO : Waiting for controler command
			speeds = STANDING_STILL_SPEED;
			return ( STANDING_STILL_SPEED );
		
			// ï¿½tat permettant d'aller droit jusqu'ï¿½ trouver un mur pour dï¿½marrer la cartographie
		case FINDER :
			speeds = WALL_FINDER_SPEED;
			return ( WALL_FINDER_SPEED );
		
		//ï¿½tat pour longer un mur
		case RIDER :
			speeds = WALL_RIDER_SPEED;
			return ( WALL_RIDER_SPEED );
			
		//ï¿½tat pour longer un mur lorsque l'on s'en éloigne
		case RIDER_AWAY :
			speeds = WALL_RIDER_AWAY_SPEED;
			return ( WALL_RIDER_AWAY_SPEED );
			
		// état de suivi des murs lorqu'on se rapproche après s'être éloigné
		case RIDER_AWAY_BACK :
			speeds = WALL_RIDER_AWAY_BACK_SPEED;
			return ( WALL_RIDER_AWAY_BACK_SPEED );
			
		// état de suivi des murs lorqu'on s'est beaucou éloigné
		case RIDER_FAR_AWAY :
			speeds = WALL_RIDER_FAR_AWAY_SPEED;
			return ( WALL_RIDER_FAR_AWAY_SPEED );
				
			//ï¿½tat pour longer un mur lorsque l'on s'en rapproche
		case RIDER_NEAR :
			speeds = WALL_RIDER_NEAR_SPEED;
			return ( WALL_RIDER_NEAR_SPEED );
			
		// état de suivi des murs lorsqu'on s'en éloigne après s'être rapproché
		case RIDER_NEAR_BACK :
			speeds = WALL_RIDER_NEAR_BACK_SPEED;
			return (WALL_RIDER_NEAR_BACK_SPEED);
		
		case LEFT_ROT_NO_RIGHT_WALL :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
				
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face
		case LEFT_ROT_1_1 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
			
		case LEFT_ROT_1_2 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
			
		case LEFT_ROT_1_3 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
			
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face étape 2
		case LEFT_ROT_2 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
			
		//ï¿½tat pour tourner ï¿½ DROITE lorsque on perd le mur sur notre droite étape 1
		case RIGHT_ROT_1 :
			speeds = STANDING_RIGHT_ROTATION_SPEED;
			return ( STANDING_RIGHT_ROTATION_SPEED );
			
		//ï¿½tat pour tourner ï¿½ DROITE lorsque on perd le mur sur notre droite étape 2
		case RIGHT_ROT_2 :
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