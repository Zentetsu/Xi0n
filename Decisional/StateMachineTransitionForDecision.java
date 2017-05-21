package Decisional;

/* Import de biblioth�ques =============*/


/* Description de la classe ===============
Machine � �tat pour la prise de D�cision
=========================================*/
public class StateMachineTransitionForDecision {
	
// ========================================
// ATTRIBUTS
	
	// ------------------------------------
	// PARAMETERS -------------------------
    // ------------------------------------
	
	public Calibration firstSpeed;
	public Calibration secondSpeed; // POUR LE TEST
	public Calibration emergencyStandingStill;
	public Calibration standingStill;
	
	// ------------------------------------
    // SENSORS ----------------------------
    // ------------------------------------
	
	public static float frontSensor;
	public static float rightSideSensor;
	public static int servoRotorPosition;
	
	// ------------------------------------
    // SENSORS MEMORY ---------------------
    // ------------------------------------
	
	
	
	// ------------------------------------
    // STATE MEMORY -----------------------
    // ------------------------------------
	
	public int pS;
	public int nS;
	
// ========================================	
// CONSTRUCTOR
	
	public StateMachineTransitionForDecision () {
		
		firstSpeed = new Calibration ( 200, 200, 1, 1 );
		secondSpeed = new Calibration ( 102, 102, 1, -1 ); // POUR LE TEST
		emergencyStandingStill = new Calibration ( 0, 0, 2, 2 );
		standingStill = new Calibration ( 0, 0, 0, 0 );
		
		frontSensor = 0;
		rightSideSensor = 0;
		servoRotorPosition = 0;
		
		pS = 3;
		nS = 3;
	}
	
// ========================================	
// METHODES
	
	// ------------------------------------
    // FONCTIONS PRINCIPALES --------------
    // ------------------------------------
	
	public void readSensors () {
		// TODO
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
		case 0 :
			nS = 0;
			break;
		
		// �tat d'erreur mineur : la machine est pi�g�e dans cet �tat
		case 1 :
			nS = 1;
			break;
			
		// �tat d'erreur mineur : le robot ne peut pas prendre seul une d�cision, il doit passr en mode manuel pour �tre extrait de sa position
		case 2 :
			nS = 2;
			break;
		
		// �tat permettant d'aller droit jusqu'� trouver un mur pour d�marrer la cartographie
		case 3 :
			nS = 4;
			break;
		
		// POUR LE TEST
		case 4 :
			nS = 3;
			break;
			
		// En cas d'erreur sur le typage on passe dans l'�tat des erreurs majeurs	
		default :
			nS = 0;
			break;
		}
	}
	
	/* Description des fonctions ----------
	Enregistre dans la m�moire toutes
	les informations n�cessaire sur
	les capteurs et l'�tat de la machien
	n�cessaire pour les prochaines
	*executions du bloc F et G
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
		case 0 :
			return ( emergencyStandingStill );
		
		// �tat d'erreur mineur : la machine est pi�g�e dans cet �tat
		case 1 :
			return ( standingStill );
		
		// �tat d'erreur mineur : le robot ne peut pas prendre seul une d�cision, il doit passr en mode manuel pour �tre extrait de sa position
		case 2 :
			// TODO : Waiting for controler command
			return ( standingStill );
			
		// �tat permettant d'aller droit jusqu'� trouver un mur pour d�marrer la cartographie
		case 3 :
			return ( firstSpeed );
		
		// POUR LE TEST
		case 4 :
			return ( secondSpeed );
		
		// Conid�ration d'une erreur majeure
		default :
			return ( emergencyStandingStill );
			
		}
	}
	
// ========================================	
// MAIN PROGRAMME - TEST
			
	public static void main(String[] args) {
			
		// DECLARATION 
		StateMachineTransitionForDecision SMT = new StateMachineTransitionForDecision ();
		FilterCalibration FT = new FilterCalibration();
		Calibration speeds = new Calibration (0,0,0,0);
		Calibration calibratedSpeeds = new Calibration ( FT.filter(speeds) );
			
		// CHARGEMENT DE L'ETALONNAGE
		boolean testLoad = FT.loadCalibrationFile();
		System.out.println("-------------- ETALONNAGE --------------");
		if ( testLoad )
			System.out.println(FT);
		else
			System.out.println("LOADING ERROR");
		System.out.println("----------------------------------------");
			
		// WHILE DE LA MAE
		while ( true ) {
				
			// lecture des valeurs des capteurs
			SMT.readSensors ();
				
			// traitement par la machine � �tat pour la prise de d�cision
			SMT.FBloc();
			SMT.MBloc();
			speeds = SMT.GBloc();
				
			// �talonnage de vitesse demand�e
			calibratedSpeeds = FT.filter(speeds) ;
				
			// transmission de la vitesse
			System.out.println("--> | "+calibratedSpeeds+" |");
			
		}
	}
	
//========================================	
	
}