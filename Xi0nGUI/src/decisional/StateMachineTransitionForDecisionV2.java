package decisional;

/* Import de bibliothï¿½ques =============*/
import view.robot.RobotConfig;
import view.robot.RobotConstant;
import view.Xi0nSimulation;
import view.robot.LateralSensor;

/* Description de la classe ===============
Machine ï¿½ ï¿½tat pour la prise de Dï¿½cision
=========================================*/
public class StateMachineTransitionForDecisionV2 {
	
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
	
	// ------------------------------------
    // SENSORS ----------------------------
    // ------------------------------------
	
	private int rightSideDistance;
	private int frontalDistance;
	
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
	Dï¿½termine le nouvel ï¿½tat de la machine
	*/
	public void FBloc () {
		switch ( pS ) {
		
		// ï¿½tat d'erreur majeur : la machine est piï¿½gï¿½e dans cet ï¿½tat
		case EMERGENCY_STANDING_STILL :
			nS = State2.EMERGENCY_STANDING_STILL;
			break;
		
		// ï¿½tat d'erreur mineur : la machine est piï¿½gï¿½e dans cet ï¿½tat
		case STANDING_STILL :
			nS = State2.STANDING_STILL;
			break;
			
		// ï¿½tat d'erreur mineur : le robot ne peut pas prendre seul une dï¿½cision, il doit passr en mode manuel pour ï¿½tre extrait de sa position
		case MANUAL :
			// TODO : Waiting for controler command
			nS = State2.MANUAL;
			break;
		
		// ï¿½tat permettant d'aller droit jusqu'ï¿½ trouver un mur pour dï¿½marrer la cartographie
		case WALL_FINDER :
			if ( frontalDistance <= RobotConstant.HEIGHT )
				nS = State2.FRONT_WALL_RIDER_ROTATION_POST_FINDER;
			// TODO
			//else if ( rightSideValues.get(rightSideValues.size()-2) < thresholdNoRightWallMaxFinder && rightSideSensor >= thresholdNoRightWallMaxFinder )
			//	nS = State.NO_RIGHT_WALL_RIDER_ROTATION_1;
			else
				nS = State2.WALL_FINDER;
			break;
		
		// état de suvit des murs
		case WALL_RIDER :
			if ( frontalDistance <= 50 )
				nS = State2.FRONT_WALL_RIDER_ROTATION_2;
			/*
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State1.NO_RIGHT_WALL_RIDER_ROTATION_1;
			*/
			else
				nS = State2.WALL_RIDER;
			break;
		
		//ï¿½tat pour tourner ï¿½ GAUCHE lorsque on rencontre un mur en face aprï¿½s le wall finder
		case FRONT_WALL_RIDER_ROTATION_POST_FINDER :
			if ( rightSideDistance < LateralSensor.WARNING_LENGTH && rightSideDistance >= LateralSensor.STOP_LENGTH )
				nS = State2.FRONT_WALL_RIDER_ROTATION_2;
			else
				nS = State2.FRONT_WALL_RIDER_ROTATION_POST_FINDER;
			break;

		//état pour tourner à GAUCHE lorsque on rencontre un mur en face
		case FRONT_WALL_RIDER_ROTATION_2 :
			if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State2.FRONT_WALL_RIDER_ROTATION_TEMP;
			else
				nS = State2.FRONT_WALL_RIDER_ROTATION_2;
			break;
		
		case FRONT_WALL_RIDER_ROTATION_TEMP :
			
		case FRONT_WALL_RIDER_ROTATION_3 :
			
		//ï¿½tat pour tourner ï¿½ DROITE lorsque on perd le mur sur notre droite ï¿½tape 1
		case NO_RIGHT_WALL_RIDER_ROTATION_1 :
			if ( rightSideDistance < LateralSensor.WARNING_LENGTH && rightSideDistance >= LateralSensor.STOP_LENGTH )
				nS = State2.NO_RIGHT_WALL_RIDER_ROTATION_2;
			else
				nS = State2.NO_RIGHT_WALL_RIDER_ROTATION_1;
			break;
		
		//ï¿½tat pour tourner ï¿½ DROITE lorsque on perd le mur sur notre droite ï¿½tape 2
		case NO_RIGHT_WALL_RIDER_ROTATION_2 :
			if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State2.WALL_RIDER;
			else
				nS = State2.NO_RIGHT_WALL_RIDER_ROTATION_2;
			break;
			
		// En cas d'erreur sur le typage on passe dans l'ï¿½tat des erreurs majeurs	
		default :
			nS = State2.EMERGENCY_STANDING_STILL;
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