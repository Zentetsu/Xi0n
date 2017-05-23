package decisional;

/* Import de bibliothèques =============*/


/* Description de l'enumération ===========
Enueration des états pour la machine à
état de la prise de décision.
=========================================*/
public enum StateMachineTransmissionEnum {

// ========================================
//ENUMERATION 
	
	EMERGENCY_STANDING_STILL,
	STANDING_STILL,
	MANUAL,
	WALL_FINDER,
	WALL_RIDER,
	FRONT_WALL_RIDER_ROTATION, // LEFT ROTATION RIDING WALL
	NO_RIGHT_WALL_RIDER_ROTATION, // RIGHT ROTATION RIDING WALL

// ========================================

}