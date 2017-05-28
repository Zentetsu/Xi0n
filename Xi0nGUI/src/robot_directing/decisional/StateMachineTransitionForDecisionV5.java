package robot_directing.decisional;

/* Import de bibliothèques =============*/
import physic.robot.RobotConfig;
import gui.Xi0nSimulation;
import physic.robot.FrontalSensor;
import physic.robot.LateralSensor;
import tools.Chrono;

/* Description de la classe ===============
Machine à état pour la prise de décision
=========================================*/
public class StateMachineTransitionForDecisionV5 {
	
// ========================================
// ATTRIBUTS
	
	// ------------------------------------
	// PARAMETERS -------------------------
    // ------------------------------------
	
	// Confiugration des vitesses et parité des roues utilisés comme sorties par la machine à état
	
	public static final RobotConfig WALL_FINDER_SPEED = new RobotConfig ( 200, 200, 1, 1 ); // vitesse d'avancement avec une même tension sur chauqe roue, pour la recherche de mur
	public static final RobotConfig WALL_RIDER_SPEED = new RobotConfig ( 200, 200, 1, 1 ); // vitesse d'avancement avec une même tension sur chauqe roue, pour le suivit de mur
	public static final RobotConfig WALL_RIDER_AWAY_SPEED = new RobotConfig ( 200, 190, 1, 1 ); // vitesse d'avancement avec une tension légèrement plus forte à gauche qu'à droite pour effectuer un léger virage vers la droite
	public static final RobotConfig WALL_RIDER_NEAR_SPEED = new RobotConfig ( 160, 200, 1, 1 ); // vitesse d'avancement avec une tension légèrement plus forte à droite qu'à gauche pour effectuer un léger virage vers la gauche
	public static final RobotConfig WALL_RIDER_AWAY_BACK_SPEED = new RobotConfig ( WALL_RIDER_AWAY_SPEED.getRightPower0to255(), WALL_RIDER_AWAY_SPEED.getLeftPower0to255(), 1, 1 ); // vitesses des roues échangées entre chaque roue de la version classique de cette vitesses
	public static final RobotConfig WALL_RIDER_NEAR_BACK_SPEED = new RobotConfig ( WALL_RIDER_NEAR_SPEED.getRightPower0to255(), WALL_RIDER_NEAR_SPEED.getLeftPower0to255(), 1, 1 ); // vitesses des roues échangées entre chaque roue de la version classique de cette vitesses
	public static final RobotConfig WALL_RIDER_FAR_AWAY_SPEED = new RobotConfig ( 200, 150, 1, 1 ); // vitesse d'avancement avec une tension plus forte à gauche qu'à droite pour effectuer un fort virage vers la droite
	public static final RobotConfig STANDING_LEFT_ROTATION_SPEED = new RobotConfig ( 150, 150, -1, 1 ); // vitesse de rotation sur place vers la gauche
	public static final RobotConfig STANDING_RIGHT_ROTATION_SPEED = new RobotConfig ( 150, 150, 1, -1 ); // vitesse de rotation sur place vers la droite
	public static final RobotConfig EMERGENCY_STANDING_STILL_SPEED = new RobotConfig ( 0, 0, 2, 2 ); // vitesse pour l'arrête d'urgence
	public static final RobotConfig STANDING_STILL_SPEED = new RobotConfig ( 0, 0, 0, 0 ); // vitesses pour l'arrêt du robot
	
	// Seuils de la machine à état
	
	public static final float THRESHOLD_ANGLE = 35; // seuil d'angle utilisé pour détecter les obstacles sur le capteur frontal
	public static final int MD_LEFT_ROT_1_2_MAX = 1000; // seuil temporel avant de redémarrer la rotation vers la droite après une rotation vers la gauche ( RIGHT_ROT_1 après LEFT_ROT_1_2 )
	
	// ------------------------------------
    // SENSORS ----------------------------
    // ------------------------------------
	
	private float rightSideDistance; // distance du mur détectée par le capteur latéral
	private float frontalDistance; // distance du mur détectée par le capteur frontal
	private float servoAngle; // distance du mur détectée par le capteur frontal
	
	// ------------------------------------
    // CHRONOMETER ------------------------
    // ------------------------------------
	
	private Chrono chronoWallRider = new Chrono (); // Chronomètre de retour sur une trajectoire droite
	private Chrono chronoLeftRot12 = new Chrono (); // Chronomètre de patience après une rotation vers la gauche
	
	// ------------------------------------
    // SENSORS MEMORY ---------------------
    // ------------------------------------
	
	private long memorisedDurationWallRider_1; // durée mémorisée de conservation de l' état de ride near ou away
	private long memorisedDurationWallRider_2; // durée mémorisée de conservation de l' état de ride near back ou away back ( on cherchera à vérifier si il est > à memorisedDurationWallRider_1 / 2 )
	private long memorisedDurationLeftRot12; // durée mémorisée de conservation de l'état LEFT_ROT_1_2
	
	// ------------------------------------
    // STATE MEMORY -----------------------
    // ------------------------------------
	
	private State5 pre2PS; // état précédent de pre1PS et différent de pre1PS
	private State5 pre1PS; // état précédent de pS et différent de pS
	private State5 pS; // état précedent du robot
	private State5 nS; // état suivant du robot
	
	// ------------------------------------
    // OUTPUTS ----------------------------
    // ------------------------------------
	
	private RobotConfig speeds = new RobotConfig ( 0,0,0,0 ); // variable utilisées pour la sortie de la vitesse par le bloc G du modèle FMG de la machine à état
	
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
	Remise à zéro de la machine à état
	du même format que le constructeur
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
	renvoyés par la simulation
	( c'est-à-dire des valeurs qui
	correspondent à des collisions entre
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
	Retourne l'état de la machine à état,
	notemment utilisé pour le debuggage
	*/
	public State5 getState () {
		return ( pS );
	}
	
	// ------------------------------------
    // MAE BLOCS --------------------------
    // ------------------------------------
	
	
	/* Description des fonctions ----------
	Détermine le nouvel état de la machine
	*/
	public void FBloc () {
		
		switch ( pS ) {
		
		// état d'erreur majeur : la machine est piégé dans cet état
		case EMERGENCY_STAND :
			nS = State5.EMERGENCY_STAND;
			break;
		
		// ï¿½tat d'erreur mineur : la machine est piégé dans cet état
		case STAND :
			nS = State5.STAND;
			break;
			
		// état d'erreur mineur : le robot ne peut pas prendre seul une décision, il doit passr en mode manuel pour être extrait de sa position. Cet état permet la commande manuelle du robot même via le mode Automatique
		case MANUAL :
			// TODO : Waiting for controler command
			nS = State5.MANUAL;
			break;
		
		// état permettant d'aller droit jusqu'à trouver un mur pour démarrer la cartographie
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
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH && ( ! ( pre1PS == State5.RIDER && pre2PS == State5.RIDER_NEAR_BACK ) ) )
				nS = State5.RIDER_AWAY_BACK;
			else if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.RIDER;
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
			
		// état de suivi des murs lorqu'on s'est beaucoup éloigné
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
			else if ( this.memorisedDurationWallRider_2 > ( this.memorisedDurationWallRider_1 / 2 ) )
				nS = State5.RIDER;
			else
				nS = State5.RIDER_NEAR_BACK;
			chronoWallRider.resume();
			break;
		
		// état pour tourner à GAUCHE lorsque on rencontre un mur en face après le wall finder
		case LEFT_ROT_NO_RIGHT_WALL :
			if ( rightSideDistance <= LateralSensor.WARNING_LENGTH )
				nS = State5.LEFT_ROT_1_1;
			else
				nS = State5.LEFT_ROT_NO_RIGHT_WALL;
			break;

			
		// état pour tourner à GAUCHE lorsque on rencontre un mur en face étape 1
		case LEFT_ROT_1_1 :
			if ( rightSideDistance <= LateralSensor.STOP_LENGTH )
				nS = State5.LEFT_ROT_2;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State5.LEFT_ROT_1_2;
			else
				nS = State5.LEFT_ROT_1_1;
			break;
		
		// état pour tourner à GAUCHE lorsque on rencontre un mur en face, état suivant LEFT_ROT_1_1 lorsqu'on ne détecte plus de mur, un timer se lance et si on ne retrouve pas de mur avant le dépassement su seuil, on entame une rotation vers la droite pour retrouver le mur le plus proche étape 1
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
			
		//état pour tourner à GAUCHE lorsque on rencontre un mur en face étape 2
		case LEFT_ROT_2 :
			if ( rightSideDistance <= LateralSensor.WARNING_LENGTH && rightSideDistance > LateralSensor.STOP_LENGTH )
				nS = State5.LEFT_ROT_1_1;
			else if ( rightSideDistance > LateralSensor.WARNING_LENGTH )
				nS = State5.RIDER_AWAY;
			else
				nS = State5.LEFT_ROT_2;
			break;
		
		// état pour tourner à DROITE lorsque on perd le mur sur notre droite étape 1
		case RIGHT_ROT_1 :
			if ( rightSideDistance <= LateralSensor.WARNING_LENGTH )
				nS = State5.RIGHT_ROT_2;
			else
				nS = State5.RIGHT_ROT_1;
			break;
			
		// état pour tourner à DROITE lorsque on perd le mur sur notre droite, après en avoir retrouvé un avec la capteur latéral étape 2
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
		
		// En cas d'erreur sur le typage on passe dans l'état des erreurs majeurs	
		default :
			nS = State5.EMERGENCY_STAND;
			break;
		}
		
	}
	
	/* Description des fonctions ----------
	Enregistre dans la mémoire de la MAE
	toutes les informations nécessaire sur
	les capteurs et chronomètres
	et l'état de la machine
	nécessaire pour les prochaines
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
	Calcule la sortie souhaitée par la
	prise de décision, retourne les
	vitesses de chaque roue associé à
	l'état proposé
	*/
	public RobotConfig GBloc () {
		
		switch ( pS ) {
		
		// état d'erreur majeur : la machine est piégé dans cet état
		case EMERGENCY_STAND :
			speeds = EMERGENCY_STANDING_STILL_SPEED;
			return ( EMERGENCY_STANDING_STILL_SPEED );
		
		// état d'erreur mineur : la machine est piégïé dans cet état
		case STAND :
			speeds = STANDING_STILL_SPEED;
			return ( STANDING_STILL_SPEED );
		
		// état d'erreur mineur : le robot ne peut pas prendre seul une décision, il doit passr en mode manuel pour être extrait de sa position. Cet état permet la commande manuelle du robot même via le mode Automatique
		case MANUAL :
			// TODO : Waiting for controler command
			speeds = STANDING_STILL_SPEED;
			return ( STANDING_STILL_SPEED );
		
		// état permettant d'aller droit jusqu'à trouver un mur pour démarrer la cartographie
		case FINDER :
			speeds = WALL_FINDER_SPEED;
			return ( WALL_FINDER_SPEED );
		
		// état pour longer un mur
		case RIDER :
			speeds = WALL_RIDER_SPEED;
			return ( WALL_RIDER_SPEED );
			
		// état pour longer un mur lorsque l'on s'en éloigne
		case RIDER_AWAY :
			if ( pre1PS == State5.RIDER && pre2PS == State5.RIDER_NEAR_BACK ) {
				speeds = WALL_RIDER_SPEED;
				return ( WALL_RIDER_SPEED );
			}
			else {
				speeds = WALL_RIDER_AWAY_SPEED;
				return ( WALL_RIDER_AWAY_SPEED );
			}

			
		// état de suivi des murs lorqu'on se rapproche après s'être éloigné
		case RIDER_AWAY_BACK :
			speeds = WALL_RIDER_AWAY_BACK_SPEED;
			return ( WALL_RIDER_AWAY_BACK_SPEED );
			
		// état de suivi des murs lorqu'on s'est beaucoup éloigné
		case RIDER_FAR_AWAY :
			speeds = WALL_RIDER_FAR_AWAY_SPEED;
			return ( WALL_RIDER_FAR_AWAY_SPEED );
				
		// état pour longer un mur lorsque l'on s'en rapproche
		case RIDER_NEAR :
			speeds = WALL_RIDER_NEAR_SPEED;
			return ( WALL_RIDER_NEAR_SPEED );
			
		// état de suivi des murs lorsqu'on s'en éloigne après s'être rapproché
		case RIDER_NEAR_BACK :
			speeds = WALL_RIDER_NEAR_BACK_SPEED;
			return (WALL_RIDER_NEAR_BACK_SPEED);
		
		// état pour tourner à GAUCHE lorsque on rencontre un mur en face après le wall finder
		case LEFT_ROT_NO_RIGHT_WALL :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
				
		// état pour tourner à GAUCHE lorsque on rencontre un mur en face étape 1
		case LEFT_ROT_1_1 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
		
		// état pour tourner à GAUCHE lorsque on rencontre un mur en face, état suivant LEFT_ROT_1_1 lorsqu'on ne détecte plus de mur, un timer se lance et si on ne retrouve pas de mur avant le dépassement su seuil, on entame une rotation vers la droite pour retrouver le mur le plus proche étape 1
		case LEFT_ROT_1_2 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
			
		// état pour tourner à GAUCHE lorsque on rencontre un mur en face étape 2
		case LEFT_ROT_2 :
			speeds = STANDING_LEFT_ROTATION_SPEED;
			return ( STANDING_LEFT_ROTATION_SPEED );
			
		// état pour tourner à DROITE lorsque on perd le mur sur notre droite étape 1
		case RIGHT_ROT_1 :
			speeds = STANDING_RIGHT_ROTATION_SPEED;
			return ( STANDING_RIGHT_ROTATION_SPEED );
			
		// état pour tourner à DROITE lorsque on perd le mur sur notre droite, après en avoir retrouvé un avec la capteur latéral étape 2
		case RIGHT_ROT_2 :
			speeds = STANDING_RIGHT_ROTATION_SPEED;
			return ( STANDING_RIGHT_ROTATION_SPEED );
		
		// Considération d'une erreur majeure
		default :
			speeds = EMERGENCY_STANDING_STILL_SPEED;
			return ( EMERGENCY_STANDING_STILL_SPEED );
			
		}
	}
	
//========================================	
	
}