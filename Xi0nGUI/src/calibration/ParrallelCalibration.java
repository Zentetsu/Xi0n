package calibration;

/* Import de bibliothèques =============*/


/* Description de la classe ===============
Classe d'étalon pour le programme
d'étalonnage
=========================================*/
public class ParrallelCalibration {
	
// ========================================
// ATTRIBUTS	
	
	private boolean leaderWheel; // LEFT : false, RIGHT : true
	private float powerLeft;
	private float powerRight;
	private float powerCentral;
	private MovementEnum movType;
	
// ========================================	
// CONSTRUCTOR
	
	public ParrallelCalibration ( MovementEnum movType, boolean leaderWheel, float powerLeft, float powerRight ) {
		this.leaderWheel = leaderWheel;
		this.powerLeft = powerLeft;
		this.powerRight = powerRight;
		if ( leaderWheel )
			powerCentral = powerRight;
		else
			powerCentral = powerLeft;
		this.movType = movType;
	}
	
	public ParrallelCalibration ( MovementEnum movType, boolean leaderWheel, float powerLeft, float powerRight, float powerCentral ) {
		this.leaderWheel = leaderWheel;
		this.powerLeft = powerLeft;
		this.powerRight = powerRight;
		this.powerCentral = powerCentral;
		this.movType = movType;
	}
	
	public ParrallelCalibration ( MovementEnum movType, float powerCentral ) {
		this.leaderWheel = true;
		this.powerLeft = powerCentral;
		this.powerRight = powerCentral;
		this.powerCentral = powerCentral;
		this.movType = movType;
	}
	
// ========================================	
// METHODES
	
    // ------------------------------------
    // FONCTIONS UTILITAIRES --------------
    // ------------------------------------
	
	/* Description des fonctions ----------
	Getteurs et Setteurs et toString
	*/
	
	public boolean isLeaderWheelRight() {
		return leaderWheel;
	}

	public void setLeaderWheel(boolean leaderWheel) {
		this.leaderWheel = leaderWheel;
	}

	public float getPowerLeft() {
		return powerLeft;
	}

	public void setPowerLeft(float powerLeft) {
		this.powerLeft = powerLeft;
	}

	public float getPowerRight() {
		return powerRight;
	}

	public void setPowerRight(float powerRight) {
		this.powerRight = powerRight;
	}
	
	public void setPowerCentral (float powerCentral) {
		this.powerCentral = powerCentral;
	}
	
	public float getPowerCentral () {
		return powerCentral;
	}
	
	public float getPowerLead () {
		if ( leaderWheel ) {
			return ( powerRight );
		}
		else {
			return ( powerLeft );
		}
	}
	
	public void setCorrectLeaderWheel () {
		if ( Math.abs ( powerCentral - powerLeft ) < Math.abs ( powerCentral - powerRight ) )
			leaderWheel = false;
		else
			leaderWheel = true;
	}
	
	public MovementEnum getME () {
		return this.movType;
	}
	
	public String toString ()
	{
		String s = new String ();
		if ( movType == MovementEnum.STAND )
			s += "STAND    ";
		else if ( movType == MovementEnum.FORWARD )
			s += "FORWARD  ";
		else if ( movType == MovementEnum.BACK )
			s += "BACK     ";
		else if ( movType == MovementEnum.ROTATIONLEFT )
			s += "LEFT ROT ";
		else
			s += "RIGHT ROT";

		s += " / C = "+powerCentral+" \\    |";
		
		s += " L: ";
		s += powerLeft;
		if ( !leaderWheel )
			s += " |  [<<<]  | R: ";
		else
			s += " |  [>>>]  | R: ";
		s += powerRight;
		s += " |";
		
		return ( s );
	}
	
	public int getLengthString ()
	{
		return ( this.toString().length() );
	}
	
//========================================	
	
}