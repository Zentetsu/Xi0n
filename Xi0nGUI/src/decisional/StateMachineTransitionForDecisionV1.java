package decisional;

/* Import de bibliothèques =============*/
import java.util.ArrayList;
import view.robot.RobotConfig;

/* Description de la classe ===============
Machine à état pour la prise de Décision
=========================================*/
public class StateMachineTransitionForDecisionV1 {
	
// ========================================
// ATTRIBUTS
	
	// ------------------------------------
	// PARAMETERS -------------------------
    // ------------------------------------
	
	public static final RobotConfig WALL_FINDER_SPEED = new RobotConfig ( 200, 200, 1, 1 );
	public static final RobotConfig WALL_RIDER_SPEED = new RobotConfig ( 200, 200, 1, 1 );
	public static final RobotConfig EMERGENCY_STANDING_STILL = new RobotConfig ( 0, 0, 2, 2 );
	public static final RobotConfig STANDING_STILL = new RobotConfig ( 0, 0, 0, 0 );
	public static final RobotConfig STANDING_LEFT_ROTATION = new RobotConfig ( 200, 200, -1, 1 );
	public static final RobotConfig STANDING_RIGHT_ROTATION = new RobotConfig ( 200, 200, 1, -1 );
	
	public static float thresholdWallFinderFront = 20 ;
	public static float thresholdMaxRightSide = 50;
	public static float thresholdMinFront = 15;
	public static float thresholdFrontWallRotationRightSide = 10;
	public static float thresholdNoRightWallRotationRightSide = 10;
	public static float thresholdFrontWallPostFinder = 20;
	
	// ------------------------------------
    // SENSORS ----------------------------
    // ------------------------------------
	
	private float frontSensor;
	private float rightSideSensor;
	private int servoRotorPosition;
	
	// ------------------------------------
    // SENSORS MEMORY ---------------------
    // ------------------------------------
	
	private float previousRightSideSensor;
	
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
		switch (i) {
		case 50 : 
			frontSensor = 1;
		}
	}
	
	public RobotConfig getRobotConfig () {
		return ( speeds );
	}
	
	public State getState () {
		return ( pS );
	}
	
	/*public boolean isDifferent ( Calibration speeds ) {
		return ( this.speeds.equals(speeds) );
	}*/
	
	// ------------------------------------
    // MAE BLOCS --------------------------
    // ------------------------------------
	
	/* Description des fonctions ----------
	Détermine le nouvel état de la machine
	*/
	public void FBloc () {
		switch ( pS ) {
		
		// état d'erreur majeur : la machine est piégée dans cet état
		case EMERGENCY_STANDING_STILL :
			nS = State.EMERGENCY_STANDING_STILL;
			break;
		
		// état d'erreur mineur : la machine est piégée dans cet état
		case STANDING_STILL :
			nS = State.STANDING_STILL;
			break;
			
		// état d'erreur mineur : le robot ne peut pas prendre seul une décision, il doit passr en mode manuel pour être extrait de sa position
		case MANUAL :
			// TODO : Waiting for controler command
			nS = State.MANUAL;
			break;
		
		// état permettant d'aller droit jusqu'à trouver un mur pour démarrer la cartographie
		case WALL_FINDER :
			if ( frontSensor < thresholdWallFinderFront )
				nS = State.FRONT_WALL_RIDER_ROTATION_POST_FINDER;
			else
				nS = State.WALL_FINDER;
			break;
		
		// POUR LE TEST
		case WALL_RIDER :
			if ( frontSensor < thresholdMinFront )
				nS = State.FRONT_WALL_RIDER_ROTATION;
			else if ( rightSideSensor >= thresholdMaxRightSide )
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_1;
			else
				nS = State.WALL_RIDER;
			break;
		
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face après le wall finder
		case FRONT_WALL_RIDER_ROTATION_POST_FINDER :
			if ( rightSideSensor < thresholdFrontWallPostFinder )
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_2;
			else
				nS = State.FRONT_WALL_RIDER_ROTATION_POST_FINDER;
			break;
			
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face
		case FRONT_WALL_RIDER_ROTATION :
			if ( rightSideSensor > rightSideSensor + thresholdFrontWallRotationRightSide )
				nS = State.WALL_RIDER;
			else
				nS = State.FRONT_WALL_RIDER_ROTATION;
			break;
			
		//état pour tourner à DROITE lorsque on perd le mur sur notre droite étape 1
		case NO_RIGHT_WALL_RIDER_ROTATION_1 :
			if ( rightSideSensor < thresholdMaxRightSide )
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_2;
			else
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_1;
			break;
		
		//état pour tourner à DROITE lorsque on perd le mur sur notre droite étape 2
		case NO_RIGHT_WALL_RIDER_ROTATION_2 :
			if ( previousRightSideSensor > rightSideSensor + thresholdNoRightWallRotationRightSide )
				nS = State.WALL_RIDER;
			else
				nS = State.NO_RIGHT_WALL_RIDER_ROTATION_2;
			
		// En cas d'erreur sur le typage on passe dans l'état des erreurs majeurs	
		default :
			nS = State.EMERGENCY_STANDING_STILL;
			break;
		}
	}
	
	/* Description des fonctions ----------
	Enregistre dans la mémoire toutes
	les informations nécessaire sur
	les capteurs et l'état de la machien
	nécessaire pour les prochaines
	executions du bloc F et G
	*/
	public void MBloc () {
		previousRightSideSensor = rightSideSensor;
		pS = nS;
	}
	
	/* Description des fonctions ----------
	Calcule la sortie souhaitée par la
	prise de décision, retourne les
	vitesses de chaque roue associé à
	l'état proposé
	*/
	public RobotConfig GBloc () {
		switch ( pS ) {
		
		// état d'erreur majeur : la machine est piégée dans cet état
		case EMERGENCY_STANDING_STILL :
			speeds = EMERGENCY_STANDING_STILL;
			return ( EMERGENCY_STANDING_STILL );
		
		// état d'erreur mineur : la machine est piégée dans cet état
		case STANDING_STILL :
			speeds = STANDING_STILL;
			return ( STANDING_STILL );
		
		// état d'erreur mineur : le robot ne peut pas prendre seul une décision, il doit passr en mode manuel pour être extrait de sa position
		case MANUAL :
			// TODO : Waiting for controler command
			speeds = STANDING_STILL;
			return ( STANDING_STILL );
		
			// état permettant d'aller droit jusqu'à trouver un mur pour démarrer la cartographie
		case WALL_FINDER :
			speeds = WALL_FINDER_SPEED;
			return ( WALL_FINDER_SPEED );
		
		//état pour longer un mur
		case WALL_RIDER :
			speeds = WALL_RIDER_SPEED;
			return ( WALL_RIDER_SPEED );
		
		case FRONT_WALL_RIDER_ROTATION_POST_FINDER :
			speeds = STANDING_LEFT_ROTATION;
			
			
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face
		case FRONT_WALL_RIDER_ROTATION :
			speeds = STANDING_LEFT_ROTATION;
			return ( STANDING_LEFT_ROTATION );
			
		//état pour tourner à DROITE lorsque on perd le mur sur notre droite
		case NO_RIGHT_WALL_RIDER_ROTATION_1 :
			speeds = STANDING_RIGHT_ROTATION;
			return ( STANDING_RIGHT_ROTATION );
			
		case NO_RIGHT_WALL_RIDER_ROTATION_2 :
			speeds = STANDING_RIGHT_ROTATION;
			return ( STANDING_RIGHT_ROTATION );
		
		// Considération d'une erreur majeure
		default :
			speeds = EMERGENCY_STANDING_STILL;
			return ( EMERGENCY_STANDING_STILL );
			
		}
	}
	
// ========================================	
// MAIN PROGRAMME - TEST
			
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
			
			/* lecture des valeurs des capteurs ( simulation )
			SMT.readSensorsSimulation ( i_simu );
			previousCalibratedSpeeds = calibratedSpeeds;*/
			
			// traitement par la machine à état pour la prise de décision
			SMT.FBloc();
			SMT.MBloc();
			speeds = SMT.GBloc();
				
			// étalonnage de vitesse demandée
			calibratedSpeeds = FT.filter(speeds) ;
			
			// transmission de la vitesse
			System.out.println("--> | "+calibratedSpeeds+" |");
			
			/* transmission de la vitesse ( simultation )
			if ( SMT.isDifferent(previousCalibratedSpeeds) )
				System.out.println("\n--> | "+calibratedSpeeds+" |");
			else {
				if ( i_simu/20 == 0 )
					System.out.print("|");
			}*/
			
		}
	}
	
//========================================	
	
}