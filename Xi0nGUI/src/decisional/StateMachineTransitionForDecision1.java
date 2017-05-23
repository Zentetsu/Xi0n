package decisional;

/* Import de bibliothèques =============*/


/* Description de la classe ===============
Machine à état pour la prise de Décision
=========================================*/
public class StateMachineTransitionForDecision1 {
	
// ========================================
// ATTRIBUTS
	
	// ------------------------------------
	// PARAMETERS -------------------------
    // ------------------------------------
	
	public static final Calibration WALL_FINDER_SPEED = new Calibration ( 200, 200, 1, 1 );
	public static final Calibration WALL_RIDER_SPEED = new Calibration ( 200, 200, 1, 1 );
	public static final Calibration EMERGENCY_STANDING_STILL = new Calibration ( 0, 0, 2, 2 );
	public static final Calibration STANDING_STILL = new Calibration ( 0, 0, 0, 0 );
	public static final Calibration STANDING_LEFT_ROTATION = new Calibration ( 200, 200, -1, 1 );
	public static final Calibration STANDING_RIGHT_ROTATION = new Calibration ( 200, 200, 1, -1 );
	
	// ------------------------------------
    // SENSORS ----------------------------
    // ------------------------------------
	
	private float frontSensor;
	private float rightSideSensor;
	private int servoRotorPosition;
	
	// ------------------------------------
    // SENSORS MEMORY ---------------------
    // ------------------------------------
	
	
	
	// ------------------------------------
    // STATE MEMORY -----------------------
    // ------------------------------------
	
	private StateMachineTransmissionEnum pS;
	private StateMachineTransmissionEnum nS;
	
	// ------------------------------------
    // OUTPUTS ----------------------------
    // ------------------------------------
	
	private Calibration speeds = new Calibration ( 0,0,0,0 );
	
// ========================================	
// CONSTRUCTOR
	
	public StateMachineTransitionForDecision1 () {
		
		readSensors();
		
		pS = StateMachineTransmissionEnum.WALL_FINDER;
		nS = StateMachineTransmissionEnum.WALL_FINDER;
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
	
	public Calibration getSpeeds () {
		return ( speeds );
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
			nS = StateMachineTransmissionEnum.EMERGENCY_STANDING_STILL;
			break;
		
		// état d'erreur mineur : la machine est piégée dans cet état
		case STANDING_STILL :
			nS = StateMachineTransmissionEnum.STANDING_STILL;
			break;
			
		// état d'erreur mineur : le robot ne peut pas prendre seul une décision, il doit passr en mode manuel pour être extrait de sa position
		case MANUAL :
			nS = StateMachineTransmissionEnum.MANUAL;
			break;
		
		// état permettant d'aller droit jusqu'à trouver un mur pour démarrer la cartographie
		case WALL_FINDER :
			nS = StateMachineTransmissionEnum.WALL_RIDER;
			break;
		
		// POUR LE TEST
		case WALL_RIDER :
			if ( frontSensor == 1 )
				nS = StateMachineTransmissionEnum.WALL_FINDER;
			else
				nS = StateMachineTransmissionEnum.WALL_RIDER;
			break;
			
		case FRONT_WALL_RIDER_ROTATION :
			nS = StateMachineTransmissionEnum.FRONT_WALL_RIDER_ROTATION;
			break;
			
		case NO_RIGHT_WALL_RIDER_ROTATION :
			nS = StateMachineTransmissionEnum.NO_RIGHT_WALL_RIDER_ROTATION;
			break;
			
			
		// En cas d'erreur sur le typage on passe dans l'état des erreurs majeurs	
		default :
			nS = StateMachineTransmissionEnum.EMERGENCY_STANDING_STILL;
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
		pS = nS;
	}
	
	/* Description des fonctions ----------
	Calcule la sortie souhaitée par la
	prise de décision, retourne les
	vitesses de chaque roue associé à
	l'état proposé
	*/
	public Calibration GBloc () {
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
		
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face
		case FRONT_WALL_RIDER_ROTATION :
			speeds = STANDING_LEFT_ROTATION;
			return ( STANDING_LEFT_ROTATION );
			
		//état pour tourner à DROITE lorsque on perd le mur sur notre droite
		case NO_RIGHT_WALL_RIDER_ROTATION :
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
		StateMachineTransitionForDecision1 SMT = new StateMachineTransitionForDecision1 ();
		FilterCalibration FT = new FilterCalibration();
		Calibration speeds = new Calibration (0,0,0,0);
		Calibration calibratedSpeeds = new Calibration ( FT.filter(speeds) );
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