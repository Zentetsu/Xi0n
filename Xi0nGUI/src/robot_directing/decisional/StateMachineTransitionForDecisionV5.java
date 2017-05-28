package robot_directing.decisional;

/* Import de biblioth�ques =============*/
import physic.robot.RobotConfig;
import gui.Xi0nSimulation;
import physic.robot.FrontalSensor;
import physic.robot.LateralSensor;
import tools.Chrono;

/* Description de la classe ===============
Machine � �tat pour la prise de d�cision
=========================================*/
public class StateMachineTransitionForDecisionV5 {
	
// ========================================
// ATTRIBUTS
	
	// ------------------------------------
	// PARAMETERS -------------------------
    // ------------------------------------
	
	// Confiugration des vitesses et parit� des roues utilis�s comme sorties par la machine � �tat
	
	public static final RobotConfig WALL_FINDER_SPEED = new RobotConfig ( 200, 200, 1, 1 ); // vitesse d'avancement avec une m�me tension sur chauqe roue, pour la recherche de mur
	public static final RobotConfig WALL_RIDER_SPEED = new RobotConfig ( 200, 200, 1, 1 ); // vitesse d'avancement avec une m�me tension sur chauqe roue, pour le suivit de mur
	public static final RobotConfig WALL_RIDER_AWAY_SPEED = new RobotConfig ( 200, 190, 1, 1 ); // vitesse d'avancement avec une tension l�g�rement plus forte � gauche qu'� droite pour effectuer un l�ger virage vers la droite
	public static final RobotConfig WALL_RIDER_NEAR_SPEED = new RobotConfig ( 160, 200, 1, 1 ); // vitesse d'avancement avec une tension l�g�rement plus forte � droite qu'� gauche pour effectuer un l�ger virage vers la gauche
	public static final RobotConfig WALL_RIDER_AWAY_BACK_SPEED = new RobotConfig ( WALL_RIDER_AWAY_SPEED.getRightPower0to255(), WALL_RIDER_AWAY_SPEED.getLeftPower0to255(), 1, 1 ); // vitesses des roues �chang�es entre chaque roue de la version classique de cette vitesses
	public static final RobotConfig WALL_RIDER_NEAR_BACK_SPEED = new RobotConfig ( WALL_RIDER_NEAR_SPEED.getRightPower0to255(), WALL_RIDER_NEAR_SPEED.getLeftPower0to255(), 1, 1 ); // vitesses des roues �chang�es entre chaque roue de la version classique de cette vitesses
	public static final RobotConfig WALL_RIDER_FAR_AWAY_SPEED = new RobotConfig ( 200, 150, 1, 1 ); // vitesse d'avancement avec une tension plus forte � gauche qu'� droite pour effectuer un fort virage vers la droite
	public static final RobotConfig STANDING_LEFT_ROTATION_SPEED = new RobotConfig ( 150, 150, -1, 1 ); // vitesse de rotation sur place vers la gauche
	public static final RobotConfig STANDING_RIGHT_ROTATION_SPEED = new RobotConfig ( 150, 150, 1, -1 ); // vitesse de rotation sur place vers la droite
	public static final RobotConfig EMERGENCY_STANDING_STILL_SPEED = new RobotConfig ( 0, 0, 2, 2 ); // vitesse pour l'arr�te d'urgence
	public static final RobotConfig STANDING_STILL_SPEED = new RobotConfig ( 0, 0, 0, 0 ); // vitesses pour l'arr�t du robot
	
	// Seuils de la machine � �tat
	
	public static final float THRESHOLD_ANGLE = 35; // seuil d'angle utilis� pour d�tecter les obstacles sur le capteur frontal
	public static final int MD_LEFT_ROT_1_2_MAX = 1000; // seuil temporel avant de red�marrer la rotation vers la droite apr�s une rotation vers la gauche ( RIGHT_ROT_1 apr�s LEFT_ROT_1_2 )
	
	// ------------------------------------
    // SENSORS ----------------------------
    // ------------------------------------
	
	private float rightSideDistance; // distance du mur d�tect�e par le capteur lat�ral
	private float frontalDistance; // distance du mur d�tect�e par le capteur frontal
	private float servoAngle; // distance du mur d�tect�e par le capteur frontal
	
	// ------------------------------------
    // CHRONOMETER ------------------------
    // ------------------------------------
	
	private Chrono chronoWallRider = new Chrono (); // Chronom�tre de retour sur une trajectoire droite
	private Chrono chronoLeftRot12 = new Chrono (); // Chronom�tre de patience apr�s une rotation vers la gauche
	
	// ------------------------------------
    // SENSORS MEMORY ---------------------
    // ------------------------------------
	
	private long memorisedDurationWallRider_1; // dur�e m�moris�e de conservation de l' �tat de ride near ou away
	private long memorisedDurationWallRider_2; // dur�e m�moris�e de conservation de l' �tat de ride near back ou away back ( on cherchera � v�rifier si il est > � memorisedDurationWallRider_1 / 2 )
	private long memorisedDurationLeftRot12; // dur�e m�moris�e de conservation de l'�tat LEFT_ROT_1_2
	
	// ------------------------------------
    // STATE MEMORY -----------------------
    // ------------------------------------
	
	private State5 pre2PS; // �tat pr�c�dent de pre1PS et diff�rent de pre1PS
	private State5 pre1PS; // �tat pr�c�dent de pS et diff�rent de pS
	private State5 pS; // �tat pr�cedent du robot
	private State5 nS; // �tat suivant du robot
	
	// ------------------------------------
    // OUTPUTS ----------------------------
    // ------------------------------------
	
	private RobotConfig speeds = new RobotConfig ( 0,0,0,0 ); // variable utilis�es pour la sortie de la vitesse par le bloc G du mod�le FMG de la machine � �tat
	
// ========================================	
// CONSTRUCTOR
	
	public StateMachineTransitionForDecisionV5 () {
		//readSensorsSimu();
<<<<<<< HEAD
		//readSensorsCapteur();
=======
		readSensors();
>>>>>>> branch 'dev' of https://github.com/haze-sama/Xi0n.git
		pre2PS = State5.FINDER;
		pre1PS = State5.FINDER;
		pS = State5.FINDER;
		nS = State5.FINDER;
		memorisedDurationWallRider_1 = 0;
		memorisedDurationWallRider_2 = 0;
		memorisedDurationLeftRot12 = 0;
		speeds = new RobotConfig ( 0,0,0,0 );
	}
	
// ========================================	
// METHODES
	
	// ------------------------------------
    // FONCTIONS PRINCIPALES --------------
    // ------------------------------------
	
	/* Description des fonctions ----------
	Remise � z�ro de la machine � �tat
	du m�me format que le constructeur
	*/
	public void reset () {
		// readSensorsSimu();
		//readSensorsCapteur();
		pre2PS = State5.FINDER;
		pre1PS = State5.FINDER;
		pS = State5.FINDER;
		nS = State5.FINDER;
		memorisedDurationWallRider_1 = 0;
		memorisedDurationWallRider_2 = 0;
		memorisedDurationLeftRot12 = 0;
		speeds = new RobotConfig ( 0,0,0,0 );
	}
	
	/* Description des fonctions ----------
	Lecture des capteurs pour la simultion,
	Les valeurs des capteurs sont celles
	renvoy�s par la simulation
	( c'est-�-dire des valeurs qui
	correspondent � des collisions entre
	les obstacles et les rectangles de
	capteurs dans la simulation )
	*/
	public void readSensorsSimu () {
		rightSideDistance = Xi0nSimulation.INSTANCE.getLateralDistance();
		frontalDistance = Xi0nSimulation.INSTANCE.getFrontalDistance();
		servoAngle = Xi0nSimulation.INSTANCE.getSensorAngle();
	}
	
	/* Description des fonctions ----------
	Lecture des capteurs
	*/
	public void readSensors () {
		rightSideDistance = Xi0nSimulation.INSTANCE.getLateralDistanceFromRobot();
		frontalDistance = Xi0nSimulation.INSTANCE.getFrontalDistanceFromRobot();
		servoAngle = Xi0nSimulation.INSTANCE.getSensorAngle();
	}
	
	/* Description des fonctions ----------
	Retourne la configuration pour les
	moteurs
	*/
	public RobotConfig getRobotConfig () {
		return ( speeds );
	}
	
	/* Description des fonctions ----------
	Retourne l'�tat de la machine � �tat,
	notemment utilis� pour le debuggage
	*/
	public State5 getState () {
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
		
		// �tat d'erreur majeur : la machine est pi�g� dans cet �tat
		case EMERGENCY_STAND :
			nS = State5.EMERGENCY_STAND;
			break;
		
		// �tat d'erreur mineur : la machine est pi�g� dans cet �tat
		case STAND :
			nS = State5.STAND;
			break;
			
		// �tat d'erreur mineur : le robot ne peut pas prendre seul une d�cision, il doit passr en mode manuel pour �tre extrait de sa position. Cet �tat permet la commande manuelle du robot m�me via le mode Automatique
		case MANUAL :
			// TODO : Waiting for controler command
			nS = State5.MANUAL;
			break;
		
		// �tat permettant d'aller droit jusqu'� trouver un mur pour d�marrer la cartographie
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
		
		// �tat de suivi des murs
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
			
		// �tat de suivi des murs lorqu'on s'en �loigne
		case RIDER_AWAY :
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_1_1;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State5.RIDER_FAR_AWAY;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH && ( ! ( pre1PS == State5.RIDER && pre2PS == State5.RIDER_NEAR_BACK ) ) )
				nS = State5.RIDER_AWAY_BACK;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.RIDER;
			else
				nS = State5.RIDER_AWAY;
			break;
		
		// �tat de suivi des murs lorqu'on se rapproche apr�s s'�tre �loign�
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
			
		// �tat de suivi des murs lorqu'on s'est beaucoup �loign�
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
			
			
		// �tat de suivi des murs lorsqu'on s'en rapproche
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
			
		// �tat de suivi des murs lorsqu'on s'en �loigne apr�s s'�tre rapproch�
		case RIDER_NEAR_BACK :
			// TODO : RIGHT ROTATION
			chronoWallRider.stop();
			if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_1_1;
			else if ( frontalDistance <= FrontalSensor.FRONTAL_LENGTH && rightSideDistance <= LateralSensor.STOP_LENGTH && servoAngle < THRESHOLD_ANGLE && servoAngle > (-1)*THRESHOLD_ANGLE )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State5.RIGHT_ROT_1;
			else if ( this.memorisedDurationWallRider_2 > ( this.memorisedDurationWallRider_1 / 2 ) )
				nS = State5.RIDER;
			else
				nS = State5.RIDER_NEAR_BACK;
			chronoWallRider.resume();
			break;
		
		// �tat pour tourner � GAUCHE lorsque on rencontre un mur en face apr�s le wall finder
		case LEFT_ROT_NO_RIGHT_WALL :
			if ( rightSideDistance <= LateralSensor.WARNING_LENGTH )
				nS = State5.LEFT_ROT_1_1;
			else
				nS = State5.LEFT_ROT_NO_RIGHT_WALL;
			break;

			
		// �tat pour tourner � GAUCHE lorsque on rencontre un mur en face �tape 1
		case LEFT_ROT_1_1 :
			if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State5.LEFT_ROT_1_2;
			else
				nS = State5.LEFT_ROT_1_1;
			break;
		
		// �tat pour tourner � GAUCHE lorsque on rencontre un mur en face, �tat suivant LEFT_ROT_1_1 lorsqu'on ne d�tecte plus de mur, un timer se lance et si on ne retrouve pas de mur avant le d�passement su seuil, on entame une rotation vers la droite pour retrouver le mur le plus proche �tape 1
		case LEFT_ROT_1_2 :
			if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance <= LateralSensor.WARNING_LENGTH )
				nS = State5.LEFT_ROT_1_1;
			else if ( MD_LEFT_ROT_1_2_MAX <= memorisedDurationLeftRot12 )
				nS = State5.RIGHT_ROT_1;
			else
				nS = State5.LEFT_ROT_1_2;
			break;
			
		//�tat pour tourner � GAUCHE lorsque on rencontre un mur en face �tape 2
		case LEFT_ROT_2 :
			if ( rightSideDistance <= LateralSensor.WARNING_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State5.LEFT_ROT_1_1;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State5.RIDER_AWAY;
			else
				nS = State5.LEFT_ROT_2;
			break;
		
		// �tat pour tourner � DROITE lorsque on perd le mur sur notre droite �tape 1
		case RIGHT_ROT_1 :
			if ( rightSideDistance <= LateralSensor.WARNING_LENGTH )
				nS = State5.RIGHT_ROT_2;
			else
				nS = State5.RIGHT_ROT_1;
			break;
			
		// �tat pour tourner � DROITE lorsque on perd le mur sur notre droite, apr�s en avoir retrouv� un avec la capteur lat�ral �tape 2
		case RIGHT_ROT_2 :
			if ( rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State5.RIDER;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH && ( ( pre1PS == State5.RIDER || pre1PS == State5.RIDER_AWAY || pre1PS == State5.RIDER_NEAR || pre1PS == State5.RIDER_AWAY_BACK || pre1PS == State5.RIDER_NEAR_BACK ) || ( pre1PS == State5.RIDER || pre1PS == State5.RIDER_AWAY || pre1PS == State5.RIDER_NEAR || pre1PS == State5.RIDER_AWAY_BACK || pre1PS == State5.RIDER_NEAR_BACK ) ) )
				nS = State5.LEFT_ROT_1_1;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.RIDER_AWAY;
			else
				nS = State5.RIGHT_ROT_2;
			break;
		
		// En cas d'erreur sur le typage on passe dans l'�tat des erreurs majeurs	
		default :
			nS = State5.EMERGENCY_STAND;
			break;
		}
		
	}
	
	/* Description des fonctions ----------
	Enregistre dans la m�moire de la MAE
	toutes les informations n�cessaire sur
	les capteurs et chronom�tres
	et l'�tat de la machine
	n�cessaire pour les prochaines
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
					if ( ( pS == State5.RIDER_AWAY && nS == State5.RIDER_NEAR ) || ( pS == State5.RIDER_NEAR_BACK && nS == State5.RIDER_AWAY_BACK ) || ( pS == State5.RIDER_NEAR_BACK && nS == State5.RIDER_AWAY ) || ( pS == State5.RIDER_AWAY_BACK && nS == State5.RIDER_NEAR ) ) {
						memorisedDurationWallRider_1 = 0;
						memorisedDurationWallRider_2 = 0;
					}
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
		
		if ( pS != nS ) {
			pre2PS = pre1PS;
			pre1PS = pS;
		}
			
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
		
		// �tat d'erreur majeur : la machine est pi�g� dans cet �tat
		case EMERGENCY_STAND :
			speeds = EMERGENCY_STANDING_STILL_SPEED;
			return ( EMERGENCY_STANDING_STILL_SPEED );
		
		// �tat d'erreur mineur : la machine est pi�g�� dans cet �tat
		case STAND :
			speeds = STANDING_STILL_SPEED;
			return ( STANDING_STILL_SPEED );
		
		// �tat d'erreur mineur : le robot ne peut pas prendre seul une d�cision, il doit passr en mode manuel pour �tre extrait de sa position. Cet �tat permet la commande manuelle du robot m�me via le mode Automatique
		case MANUAL :
			// TODO : Waiting for controler command
			speeds = STANDING_STILL_SPEED;
			return ( STANDING_STILL_SPEED );
		
		// �tat permettant d'aller droit jusqu'� trouver un mur pour d�marrer la cartographie
		case FINDER :
			speeds = WALL_FINDER_SPEED;
			return ( WALL_FINDER_SPEED );
		
		// �tat pour longer un mur
		case RIDER :
			speeds = WALL_RIDER_SPEED;
			return ( WALL_RIDER_SPEED );
			
		// �tat pour longer un mur lorsque l'on s'en �loigne
		case RIDER_AWAY :
			if ( pre1PS == State5.RIDER && pre2PS == State5.RIDER_NEAR_BACK ) {
				speeds = WALL_RIDER_SPEED;
				return ( WALL_RIDER_SPEED );
			}
			else {
				speeds = WALL_RIDER_AWAY_SPEED;
				return ( WALL_RIDER_AWAY_SPEED );
			}

			
		// �tat de suivi des murs lorqu'on se rapproche apr�s s'�tre �loign�
		case RIDER_AWAY_BACK :
			speeds = WALL_RIDER_AWAY_BACK_SPEED;
			return ( WALL_RIDER_AWAY_BACK_SPEED );
			
		// �tat de suivi des murs lorqu'on s'est beaucoup �loign�
		case RIDER_FAR_AWAY :
			speeds = WALL_RIDER_FAR_AWAY_SPEED;
			return ( WALL_RIDER_FAR_AWAY_SPEED );
				
		// �tat pour longer un mur lorsque l'on s'en rapproche
		case RIDER_NEAR :
			speeds = WALL_RIDER_NEAR_SPEED;
			return ( WALL_RIDER_NEAR_SPEED );
			
		// �tat de suivi des murs lorsqu'on s'en �loigne apr�s s'�tre rapproch�
		case RIDER_NEAR_BACK :
			speeds = WALL_RIDER_NEAR_BACK_SPEED;
			return (WALL_RIDER_NEAR_BACK_SPEED);
		
		// �tat pour tourner � GAUCHE lorsque on rencontre un mur en face apr�s le wall finder
		case LEFT_ROT_NO_RIGHT_WALL :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
				
		// �tat pour tourner � GAUCHE lorsque on rencontre un mur en face �tape 1
		case LEFT_ROT_1_1 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
		
		// �tat pour tourner � GAUCHE lorsque on rencontre un mur en face, �tat suivant LEFT_ROT_1_1 lorsqu'on ne d�tecte plus de mur, un timer se lance et si on ne retrouve pas de mur avant le d�passement su seuil, on entame une rotation vers la droite pour retrouver le mur le plus proche �tape 1
		case LEFT_ROT_1_2 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
			
		// �tat pour tourner � GAUCHE lorsque on rencontre un mur en face �tape 2
		case LEFT_ROT_2 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
			
		// �tat pour tourner � DROITE lorsque on perd le mur sur notre droite �tape 1
		case RIGHT_ROT_1 :
			speeds = STANDING_RIGHT_ROTATION_SPEED;
			return ( STANDING_RIGHT_ROTATION_SPEED );
			
		// �tat pour tourner � DROITE lorsque on perd le mur sur notre droite, apr�s en avoir retrouv� un avec la capteur lat�ral �tape 2
		case RIGHT_ROT_2 :
			speeds = STANDING_RIGHT_ROTATION_SPEED;
			return ( STANDING_RIGHT_ROTATION_SPEED );
		
		// Consid�ration d'une erreur majeure
		default :
			speeds = EMERGENCY_STANDING_STILL_SPEED;
			return ( EMERGENCY_STANDING_STILL_SPEED );
			
		}
	}
	
//========================================	
	
}