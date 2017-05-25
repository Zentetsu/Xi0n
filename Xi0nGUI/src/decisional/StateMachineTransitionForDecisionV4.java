package decisional;

/* Import de bibliothï¿½ques =============*/
import view.robot.RobotConfig;
import view.Xi0nSimulation;
import view.robot.FrontalSensor;
import view.robot.LateralSensor;
import tools.Chrono;

/* Description de la classe ===============
Machine ï¿½ ï¿½tat pour la prise de Dï¿½cision
=========================================*/
public class StateMachineTransitionForDecisionV4 {
	
// ========================================
// ATTRIBUTS
	
	// ------------------------------------
	// PARAMETERS -------------------------
    // ------------------------------------
	
	public static final RobotConfig WALL_FINDER_SPEED = new RobotConfig ( 150, 150, 1, 1 );
	public static final RobotConfig WALL_RIDER_SPEED = new RobotConfig ( 150, 150, 1, 1 );
	public static final RobotConfig WALL_RIDER_AWAY_SPEED = new RobotConfig ( 150, 102, 1, 1 );
	public static final RobotConfig WALL_RIDER_NEAR_SPEED = new RobotConfig ( 102, 150, 1, 1 );
	public static final RobotConfig WALL_RIDER_AWAY_BACK_SPEED = new RobotConfig ( WALL_RIDER_AWAY_SPEED.getRightPower0to255(), WALL_RIDER_AWAY_SPEED.getLeftPower0to255(), 1, 1 );
	public static final RobotConfig WALL_RIDER_NEAR_BACK_SPEED = new RobotConfig ( WALL_RIDER_NEAR_SPEED.getRightPower0to255(), WALL_RIDER_NEAR_SPEED.getLeftPower0to255(), 1, 1 );
	public static final RobotConfig EMERGENCY_STANDING_STILL_SPEED = new RobotConfig ( 0, 0, 2, 2 );
	public static final RobotConfig STANDING_STILL_SPEED = new RobotConfig ( 0, 0, 0, 0 );
	public static final RobotConfig STANDING_LEFT_ROTATION_SPEED = new RobotConfig ( 102, 102, -1, 1 );
	public static final RobotConfig STANDING_RIGHT_ROTATION_SPEED = new RobotConfig ( 102, 102, 1, -1 );
	
	public static final float THRESHOLD_ANGLE = 35;
	
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
	
	// ------------------------------------
    // SENSORS MEMORY ---------------------
    // ------------------------------------
	
	private long memorisedDurationWallRider_1;
	private long memorisedDurationWallRider_2;
	
	// ------------------------------------
    // STATE MEMORY -----------------------
    // ------------------------------------
	
	private State4 pS;
	private State4 nS;
	
	// ------------------------------------
    // OUTPUTS ----------------------------
    // ------------------------------------
	
	private RobotConfig speeds = new RobotConfig ( 0,0,0,0 );
	
// ========================================	
// CONSTRUCTOR
	
	public StateMachineTransitionForDecisionV4 () {
		readSensors();
		pS = State4.WALL_FINDER;
		nS = State4.WALL_FINDER;
		memorisedDurationWallRider_1 = 0;
		memorisedDurationWallRider_2 = 0;
	}
	
// ========================================	
// METHODES
	
	// ------------------------------------
    // FONCTIONS PRINCIPALES --------------
    // ------------------------------------
	
	public void reset () {
		readSensors();
		pS = State4.WALL_FINDER;
		nS = State4.WALL_FINDER;
		memorisedDurationWallRider_1 = 0;
		memorisedDurationWallRider_2 = 0;
	}
	
	public void readSensors () {
		rightSideDistance = Xi0nSimulation.INSTANCE.getLateralDistance();
		frontalDistance = Xi0nSimulation.INSTANCE.getFrontalDistance();
		servoAngle = Xi0nSimulation.INSTANCE.getSensorAngle();
	}
	
	public RobotConfig getRobotConfig () {
		return ( speeds );
	}
	
	public State4 getState () {
		return ( pS );
	}
	
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
			nS = State4.EMERGENCY_STANDING_STILL;
			break;
		
		// ï¿½tat d'erreur mineur : la machine est piï¿½gï¿½e dans cet ï¿½tat
		case STANDING_STILL :
			nS = State4.STANDING_STILL;
			break;
			
		// ï¿½tat d'erreur mineur : le robot ne peut pas prendre seul une dï¿½cision, il doit passr en mode manuel pour ï¿½tre extrait de sa position
		case MANUAL :
			// TODO : Waiting for controler command
			nS = State4.MANUAL;
			break;
			
		// ï¿½tat permettant d'aller droit jusqu'ï¿½ trouver un mur pour dï¿½marrer la cartographie
		case WALL_FINDER :
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.WARNING_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State4.FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.WARNING_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State4.FRONT_WALL_RIDER_ROTATION_1;
			else if ( rightSideDistance <= LateralSensor.WARNING_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State4.WALL_RIDER;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State4.WALL_RIDER_NEAR;
			else
				nS = State4.WALL_FINDER;
			break;
		
		// état de suivi des murs
		case WALL_RIDER :
			// TODO : RIGHT ROTATION
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State4.FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State4.FRONT_WALL_RIDER_ROTATION_1;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State4.NO_RIGHT_WALL_RIDER_ROTATION_1;
			else if ( rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State4.WALL_RIDER_AWAY;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State4.WALL_RIDER_NEAR;
			else
				nS = State4.WALL_RIDER;
			break;
			
		// état de suivi des murs lorqu'on s'en éloigne
		case WALL_RIDER_AWAY :
			// TODO : RIGHT ROTATION
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State4.FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State4.FRONT_WALL_RIDER_ROTATION_1;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State4.NO_RIGHT_WALL_RIDER_ROTATION_1;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State4.WALL_RIDER_AWAY_BACK;
			else
				nS = State4.WALL_RIDER;
			break;
		
		// état de suivi des murs lorqu'on se rapproche après s'être éloigné
		case WALL_RIDER_AWAY_BACK :
			// TODO : RIGHT ROTATION
			chronoWallRider.stop();
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State4.FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State4.FRONT_WALL_RIDER_ROTATION_1;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State4.NO_RIGHT_WALL_RIDER_ROTATION_1;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State4.WALL_RIDER_NEAR;
			else if ( this.memorisedDurationWallRider_2 > ( this.memorisedDurationWallRider_1 / 2 ) )
				nS = State4.WALL_RIDER;
			else
				nS = State4.WALL_RIDER_AWAY_BACK;
			chronoWallRider.resume();
			break;
			
		// état de suivi des murs lorsqu'on s'en rapproche
		case WALL_RIDER_NEAR :
			// TODO : RIGHT ROTATION
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State4.FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State4.FRONT_WALL_RIDER_ROTATION_1;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State4.NO_RIGHT_WALL_RIDER_ROTATION_1;
			else if ( rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State4.WALL_RIDER_NEAR_BACK;
			else
				nS = State4.WALL_RIDER_NEAR;
			break;
			
		// état de suivi des murs lorsqu'on s'en éloigne après s'être rapproché
		case WALL_RIDER_NEAR_BACK :
			// TODO : RIGHT ROTATION
			chronoWallRider.stop();
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State4.FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State4.FRONT_WALL_RIDER_ROTATION_1;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State4.NO_RIGHT_WALL_RIDER_ROTATION_1;
			else if ( rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State4.WALL_RIDER_AWAY;
			else if ( this.memorisedDurationWallRider_2 > ( this.memorisedDurationWallRider_1 / 2 ) )
				nS = State4.WALL_RIDER;
			else
				nS = State4.WALL_RIDER_NEAR_BACK;
			chronoWallRider.resume();
			break;
		
		//ï¿½tat pour tourner ï¿½ GAUCHE lorsque on rencontre un mur en face aprï¿½s le wall finder
		case FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL :
			if ( rightSideDistance <= LateralSensor.WARNING_LENGTH )
				nS = State4.FRONT_WALL_RIDER_ROTATION_1;
			else
				nS = State4.FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL;
			break;

			
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face étape 1
		case FRONT_WALL_RIDER_ROTATION_1 :
			if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State4.FRONT_WALL_RIDER_ROTATION_2;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State4.WALL_RIDER;
			else
				nS = State4.FRONT_WALL_RIDER_ROTATION_1;
			break;
			
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face étape 2
		case FRONT_WALL_RIDER_ROTATION_2 :
			if ( rightSideDistance <= LateralSensor.WARNING_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State4.WALL_RIDER;
			else if ( rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State4.WALL_RIDER_AWAY;
			else
				nS = State4.FRONT_WALL_RIDER_ROTATION_2;
			break;
		
		//ï¿½tat pour tourner ï¿½ DROITE lorsque on perd le mur sur notre droite ï¿½tape 1
		case NO_RIGHT_WALL_RIDER_ROTATION_1 :
			if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State4.WALL_RIDER;
			else if ( rightSideDistance <= LateralSensor.WARNING_LENGTH )
				nS = State4.NO_RIGHT_WALL_RIDER_ROTATION_2;
			else
				nS = State4.NO_RIGHT_WALL_RIDER_ROTATION_1;
			break;
			
		//ï¿½tat pour tourner ï¿½ DROITE lorsque on perd le mur sur notre droite ï¿½tape 2
		case NO_RIGHT_WALL_RIDER_ROTATION_2 :
			if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State4.WALL_RIDER;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State4.WALL_RIDER;
			else
				nS = State4.NO_RIGHT_WALL_RIDER_ROTATION_2;
			break;
		
		// En cas d'erreur sur le typage on passe dans l'ï¿½tat des erreurs majeurs	
		default :
			nS = State4.EMERGENCY_STANDING_STILL;
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

		System.out.println(memorisedDurationWallRider_1);
		System.out.println(memorisedDurationWallRider_2);
		
		if ( nS != pS ) {
			switch ( nS ) {
			case WALL_RIDER_AWAY:
			case WALL_RIDER_NEAR:
			case WALL_RIDER_AWAY_BACK:
			case WALL_RIDER_NEAR_BACK:
				switch ( pS ) {
				case WALL_RIDER_AWAY:
				case WALL_RIDER_NEAR:
				case WALL_RIDER_AWAY_BACK:
				case WALL_RIDER_NEAR_BACK:
					chronoWallRider.stop();
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
			case WALL_RIDER_AWAY:
			case WALL_RIDER_NEAR:
				chronoWallRider.stop();
				memorisedDurationWallRider_1 += chronoWallRider.getDurationMs();
				chronoWallRider.resume();
				break;
			case WALL_RIDER_AWAY_BACK:
			case WALL_RIDER_NEAR_BACK:
				chronoWallRider.stop();
				memorisedDurationWallRider_2 += chronoWallRider.getDurationMs();
				chronoWallRider.resume();
				break;
			default:
				break;
			}
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
			
		//ï¿½tat pour longer un mur lorsque l'on s'en éloigne
		case WALL_RIDER_AWAY :
			speeds = WALL_RIDER_AWAY_SPEED;
			return ( WALL_RIDER_AWAY_SPEED );
			
		// état de suivi des murs lorqu'on se rapproche après s'être éloigné
		case WALL_RIDER_AWAY_BACK :
			speeds = WALL_RIDER_AWAY_BACK_SPEED;
			return ( WALL_RIDER_AWAY_BACK_SPEED );
				
			//ï¿½tat pour longer un mur lorsque l'on s'en rapproche
		case WALL_RIDER_NEAR :
			speeds = WALL_RIDER_NEAR_SPEED;
			return ( WALL_RIDER_NEAR_SPEED );
			
		// état de suivi des murs lorsqu'on s'en éloigne après s'être rapproché
		case WALL_RIDER_NEAR_BACK :
			speeds = WALL_RIDER_NEAR_BACK_SPEED;
			return (WALL_RIDER_NEAR_BACK_SPEED);
		
		case FRONT_WALL_RIDER_ROTATION_NO_RIGHT_WALL :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
				
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face
		case FRONT_WALL_RIDER_ROTATION_1 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
			
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face étape 2
		case FRONT_WALL_RIDER_ROTATION_2 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
			
		//ï¿½tat pour tourner ï¿½ DROITE lorsque on perd le mur sur notre droite étape 1
		case NO_RIGHT_WALL_RIDER_ROTATION_1 :
			speeds = STANDING_RIGHT_ROTATION_SPEED;
			return ( STANDING_RIGHT_ROTATION_SPEED );
			
		//ï¿½tat pour tourner ï¿½ DROITE lorsque on perd le mur sur notre droite étape 2
		case NO_RIGHT_WALL_RIDER_ROTATION_2 :
			speeds = STANDING_RIGHT_ROTATION_SPEED;
			return ( STANDING_RIGHT_ROTATION_SPEED );
		
		// Considï¿½ration d'une erreur majeure
		default :
			speeds = EMERGENCY_STANDING_STILL_SPEED;
			return ( EMERGENCY_STANDING_STILL_SPEED );
			
		}
	}
	
//========================================	
	
}