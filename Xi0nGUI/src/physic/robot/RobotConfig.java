package physic.robot;

/* Import de bibliothèques =============*/


/* Description de la classe ===============
Classe de couple de vitesses à communiquer
au robot
=========================================*/
public class RobotConfig {
	
// ========================================
// ATTRIBUTS	
	
	private int leftPower0to255;
	private int rightPower0to255;
	private int leftDirection;
	private int rightDirection;
	
// ========================================	
// CONSTRUCTOR
	
	public RobotConfig ( int leftPower0to255, int rightPower0to255, int leftDirection, int rightDirection ) {
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
	
	public RobotConfig ( RobotConfig config ) {
		leftPower0to255 = config.getLeftPower0to255();
		rightPower0to255 = config.getRightPower0to255();
		leftDirection = config.getLeftDirection();
		rightDirection = config.getRightDirection();
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
	
	public boolean equals ( RobotConfig config ) {
		return ( this.leftDirection==config.leftDirection && this.leftPower0to255== config.leftPower0to255 && this.rightDirection == config.rightDirection && this.rightPower0to255 == config.rightPower0to255 );
	}
	
	public String toString() {
		return  ( "LP = "+leftPower0to255*leftDirection+" | RP = "+rightPower0to255*rightDirection );
	}
	
//========================================	
	
}