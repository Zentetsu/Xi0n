package Calibration;

/* Import de bibliothèques =============*/


/* Description de la classe ===============
Classe de couple de vitesses à communiquer
au robot
=========================================*/
public class Calibration {
	
// ========================================
// ATTRIBUTS	
	
	private int leftPower0to255;
	private int rightPower0to255;
	private int leftDirection;
	private int rightDirection;
	
// ========================================	
// CONSTRUCTOR
	
	public Calibration ( int leftPower0to255, int rightPower0to255, int leftDirection, int rightDirection ) {
		this.leftPower0to255 = leftPower0to255;
		this.rightPower0to255 = rightPower0to255;
		this.leftDirection = leftDirection;
		this.rightDirection = rightDirection;
		if( this.leftDirection > 1 )
			this.leftDirection = 1;
		if( this.leftDirection < -1 )
			this.leftDirection = -1;
		if( this.rightDirection > 1 )
			this.rightDirection = 1;
		if( this.rightDirection < -1 )
			this.rightDirection = -1;
	}
	
	public Calibration ( int leftPower0to255, int rightPower0to255, MovementEnum movType ) {
		this.leftPower0to255 = leftPower0to255;
		this.rightPower0to255 = rightPower0to255;
		switch ( movType ) {
		case STAND:
			this.leftDirection = 0;
			this.rightDirection = 0;
			break;
		case FORWARD:
			this.leftDirection = 1;
			this.rightDirection = 1;
			break;
		case BACK:
			this.leftDirection = -1;
			this.rightDirection = -1;
			break;
		case ROTATIONLEFT:
			this.leftDirection = -1;
			this.rightDirection = 1;
			break;
		case ROTATIONRIGHT:
			this.leftDirection = 1;
			this.rightDirection = -1;
			break;
		}
	}

// ========================================	
// METHODES
	
	// ------------------------------------
    // FONCTIONS UTILITAIRES --------------
    // ------------------------------------
	
	/* Description des fonctions ----------
	Getteurs et Setteurs et toString
	*/
	
	public int getLeftPower0to255() {
		return leftPower0to255;
	}

	public void setLeftPower0to255(int leftPower0to255) {
		this.leftPower0to255 = leftPower0to255;
	}

	public int getRightPower0to255() {
		return rightPower0to255;
	}

	public void setRightPower0to255(int rightPower0to255) {
		this.rightPower0to255 = rightPower0to255;
	}

	public int getLeftDirection() {
		return leftDirection;
	}

	public void setLeftDirection(int leftDirection) {
		this.leftDirection = leftDirection;
	}

	public int getRightDirection() {
		return rightDirection;
	}

	public void setRightDirection(int rightDirection) {
		this.rightDirection = rightDirection;
	}
	
	public String toString() {
		return  ( "LeftPower = "+leftPower0to255*leftDirection+" | RightPower = "+rightPower0to255*rightDirection );
	}
	
//========================================	
	
}