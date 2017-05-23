package decisional;

/* Import de biblioth�ques =============*/


/* Description de la classe ===============
Machine � �tat pour la prise de D�cision
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
	D�termine le nouvel �tat de la machine
	*/
	public void FBloc () {
		switch ( pS ) {
		
		// �tat d'erreur majeur : la machine est pi�g�e dans cet �tat
		case EMERGENCY_STANDING_STILL :
			nS = StateMachineTransmissionEnum.EMERGENCY_STANDING_STILL;
			break;
		
		// �tat d'erreur mineur : la machine est pi�g�e dans cet �tat
		case STANDING_STILL :
			nS = StateMachineTransmissionEnum.STANDING_STILL;
			break;
			
		// �tat d'erreur mineur : le robot ne peut pas prendre seul une d�cision, il doit passr en mode manuel pour �tre extrait de sa position
		case MANUAL :
			nS = StateMachineTransmissionEnum.MANUAL;
			break;
		
		// �tat permettant d'aller droit jusqu'� trouver un mur pour d�marrer la cartographie
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
			
			
		// En cas d'erreur sur le typage on passe dans l'�tat des erreurs majeurs	
		default :
			nS = StateMachineTransmissionEnum.EMERGENCY_STANDING_STILL;
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
	public Calibration GBloc () {
		switch ( pS ) {
		
		// �tat d'erreur majeur : la machine est pi�g�e dans cet �tat
		case EMERGENCY_STANDING_STILL :
			speeds = EMERGENCY_STANDING_STILL;
			return ( EMERGENCY_STANDING_STILL );
		
		// �tat d'erreur mineur : la machine est pi�g�e dans cet �tat
		case STANDING_STILL :
			speeds = STANDING_STILL;
			return ( STANDING_STILL );
		
		// �tat d'erreur mineur : le robot ne peut pas prendre seul une d�cision, il doit passr en mode manuel pour �tre extrait de sa position
		case MANUAL :
			// TODO : Waiting for controler command
			speeds = STANDING_STILL;
			return ( STANDING_STILL );
			
		// �tat permettant d'aller droit jusqu'� trouver un mur pour d�marrer la cartographie
		case WALL_FINDER :
			speeds = WALL_FINDER_SPEED;
			return ( WALL_FINDER_SPEED );
		
		//�tat pour longer un mur
		case WALL_RIDER :
			speeds = WALL_RIDER_SPEED;
			return ( WALL_RIDER_SPEED );
		
		//�tat pour tourner � GAUCHE lorsque on rencontre un mur en face
		case FRONT_WALL_RIDER_ROTATION :
			speeds = STANDING_LEFT_ROTATION;
			return ( STANDING_LEFT_ROTATION );
			
		//�tat pour tourner � DROITE lorsque on perd le mur sur notre droite
		case NO_RIGHT_WALL_RIDER_ROTATION :
			speeds = STANDING_RIGHT_ROTATION;
			return ( STANDING_RIGHT_ROTATION );
		
		// Consid�ration d'une erreur majeure
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
			
			// traitement par la machine � �tat pour la prise de d�cision
			SMT.FBloc();
			SMT.MBloc();
			speeds = SMT.GBloc();
				
			// �talonnage de vitesse demand�e
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