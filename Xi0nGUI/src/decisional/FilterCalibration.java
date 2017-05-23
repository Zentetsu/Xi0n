package decisional;

/* Import de bibliothèques =============*/
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/* Description de la classe ===============
Classe de vitesse pour le robot
=========================================*/
public class FilterCalibration {
	
// ========================================
// ATTRIBUTS
	
	private Map<Integer,Integer> leftMotorSL;
	private Map<Integer,Integer> rightMotorSL;
	private Map<Integer,Integer> leftMotorR;
	private Map<Integer,Integer> rightMotorR;
	
// ========================================	
// CONSTRUCTOR
	
	public FilterCalibration () {
		leftMotorSL = new HashMap<>();
		rightMotorSL = new HashMap<>();
		leftMotorR = new HashMap<>();
		rightMotorR = new HashMap<>();
	}
	
// ========================================	
// METHODES
	
	// ------------------------------------
    // FONCTIONS UTILITAIRES --------------
    // ------------------------------------
	
	/* Description de la fonction ---------
	lecture de la ligne suivante dans le
	fichiers
	*/
	private String readLine ( FileReader fR ) {
		String actualLine = new String ();
		int actualChar;
		try
		{
			do {
				actualChar = fR.read ();
				if ( actualChar != '\n' )
					actualLine += (char)actualChar;
			} while ( (char)actualChar !='\n' );
			return actualLine;
		}
		catch ( IOException e )
		{
			return "";
		}
	}
	
	/* Description de la fonction ---------
	ajoute l'étalon à la map de SLLeft
	*/
	public boolean addLMSL ( String actualLine, int i ) {
		Clavier clavierTest = new Clavier();
		boolean positive = true;
		if ( actualLine.startsWith("-") ) {
			positive = false;
			String actualLineTemp = actualLine.substring(1);
			actualLine = actualLineTemp;
		}
		if ( clavierTest.isEntier(actualLine) ) {
			if ( positive ) {
				leftMotorSL.put((Integer)i,(Integer)(Integer.parseInt(actualLine)) );
			}
			else {
				leftMotorSL.put((Integer)i,(Integer)((-1)*(Integer.parseInt(actualLine))) );
			}
			return true;	
		}
		System.out.println("testFonct");
		return false ;
	}
	
	/* Description de la fonction ---------
	ajoute l'étalon à la map de RLeft
	*/
	public boolean addLMR ( String actualLine, int i ) {
		Clavier clavierTest = new Clavier();
		boolean positive = true;
		if ( actualLine.startsWith("-") ) {
			positive = false;
			String actualLineTemp = actualLine.substring(1);
			actualLine = actualLineTemp;
		}
		if ( clavierTest.isEntier(actualLine) ) {
			if ( positive )
				leftMotorR.put((Integer)i,(Integer)(Integer.parseInt(actualLine)) );
			else
				leftMotorR.put((Integer)i,(Integer)((-1)*(Integer.parseInt(actualLine))) );
			return true;
		}
		return false ;
	}
	
	/* Description de la fonction ---------
	ajoute l'étalon à la map de LSRight
	*/
	public boolean addRMSL ( String actualLine, int i ) {
		Clavier clavierTest = new Clavier();
		boolean positive = true;
		if ( actualLine.startsWith("-") ) {
			positive = false;
			String actualLineTemp = actualLine.substring(1);
			actualLine = actualLineTemp;
		}
		if ( clavierTest.isEntier(actualLine) ) {
			if ( positive )
				rightMotorSL.put((Integer)i,(Integer)(Integer.parseInt(actualLine)) );
			else
				rightMotorSL.put((Integer)i,(Integer)((-1)*(Integer.parseInt(actualLine))) );
			return true;
		}
		return false ;
	}
	
	/* Description de la fonction ---------
	ajoute l'étalon à la map de RRight
	*/
	public boolean addRMR ( String actualLine, int i ) {
		Clavier clavierTest = new Clavier();
		boolean positive = true;
		if ( actualLine.startsWith("-") ) {
			positive = false;
			String actualLineTemp = actualLine.substring(1);
			actualLine = actualLineTemp;
		}
		if ( clavierTest.isEntier(actualLine) ) {
			if ( positive )
				rightMotorR.put((Integer)i,(Integer)(Integer.parseInt(actualLine)) );
			else
				rightMotorR.put((Integer)i,(Integer)((-1)*(Integer.parseInt(actualLine))) );
			return true;
		}
		return false ;
	}
	
	/* Description de la fonction ---------
	charge le fichier de calibrage
	*/
	public boolean loadCalibrationFile () {
		File f = new File ( "./src/files/calibration.txt" );
		FileReader fR;
		boolean writenTest = true;
		try
		{
			fR = new FileReader ( f );
			String actualLine = new String ();
			int i = -255;
			do {
				if ( writenTest )
					actualLine = readLine ( fR );
				if ( !actualLine.equals("...") && ( ( i >= (int)(0.4*255) && i <= 255 ) || ( i <= (-1)*(int)(0.4*255) && i >= -255 ) ) ) {
					if ( !addLMSL ( actualLine, i ) ) {
						return false;
					}
					writenTest = true;
				}
				else
					writenTest = false;
				i++;
			} while ( !actualLine.equals("...") && i <= 255 && i >= -255 );
			actualLine = readLine ( fR );
			i = -255;
			writenTest = true;
			do {
				if ( writenTest )
					actualLine = readLine ( fR );
				if ( !actualLine.equals("...") && ( ( i >= (int)(0.4*255) && i <= 255 ) || ( i <= (-1)*(int)(0.4*255) && i >= -255 ) ) ) {
					if ( !addLMR ( actualLine, i ) ) {
						return false;
					}
					writenTest = true;
				}
				else
					writenTest = false;
				i++;
			} while ( !actualLine.equals("...") && i <= 255 && i >= -255 );
			actualLine = readLine ( fR );
			i = -255;
			writenTest = true;
			do {
				if ( writenTest )
					actualLine = readLine ( fR );
				if ( !actualLine.equals("...") && ( ( i >= (int)(0.4*255) && i <= 255 ) || ( i <= (-1)*(int)(0.4*255) && i >= -255 ) ) ) {
					if ( !addRMSL ( actualLine, i ) ) {
						return false;
					}
					writenTest = true;
				}
				else
					writenTest = false;
				i++;
			} while ( !actualLine.equals("...") && i <= 255 && i >= -255 );
			actualLine = readLine ( fR );
			i = -255;
			writenTest = true;
			do {
				if ( writenTest )
					actualLine = readLine ( fR );
				if ( !actualLine.equals("...") && ( ( i >= (int)(0.4*255) && i <= 255 ) || ( i <= (-1)*(int)(0.4*255) && i >= -255 ) ) ) {
					if ( !addRMR ( actualLine, i ) ) {
						return false;
					}
					writenTest = true;
				}
				else
					writenTest = false;
				i++;
			} while ( !actualLine.equals("...") && i <= 255 && i >= -255 );
			return true;
		}
		catch ( IOException e )
		{
			return ( false );
		}
	}
	
	/* Description de la fonction ---------
	éffectue l'adaptation de la valeur
	entrée en fonction des valeurs
	invalides et de l'atalonnage
	*/
	public Calibration filter ( int leftPower0to255, int rightPower0to255, int leftDirection, int rightDirection ){
		if ( leftDirection < -1 || leftDirection > 2 || rightDirection < -1 || rightDirection > 2  ) {
			return ( new Calibration ( 0, 0, 0, 0 ) );
		}
		else if ( ( leftPower0to255 < ((int)(0.4*255)) && leftPower0to255 > (-1)*((int)(0.4*255)) ) || leftPower0to255 > 255 || leftPower0to255 < -255) {
			return ( new Calibration ( 0, 0, 0, 0 ) );
		}
		else if ( ( rightPower0to255 < ((int)(0.4*255)) && rightPower0to255 > (-1)*((int)(0.4*255)) ) || rightPower0to255 > 255 || rightPower0to255 < -255) {
			return ( new Calibration ( 0, 0, 0, 0 ) );
		}
		else if ( leftDirection == 0 || rightDirection == 0 ) {
			return ( new Calibration ( 0, 0, 0, 0 ) );
		}
		else if ( leftDirection == 2 || rightDirection ==2 ) {
			return ( new Calibration ( 0, 0, 2, 2 ) );
		}
		else if ( leftDirection == rightDirection ) {
			try {
				return ( new Calibration ( leftMotorSL.get(leftPower0to255), rightMotorSL.get(rightPower0to255), leftDirection, rightDirection ) );
			}
			catch ( NullPointerException e ) {
				return ( new Calibration ( 0, 0, 0, 0 ) );
			}
		}
		else if ( leftDirection != rightDirection ) {
			try {
				return ( new Calibration ( leftMotorR.get(leftPower0to255), rightMotorR.get(rightPower0to255), leftDirection, rightDirection ) );
			}
			catch ( NullPointerException e ) {
				return ( new Calibration ( 0, 0, 0, 0 ) );
			}
		}
		else
			return ( new Calibration ( 0, 0, 0, 0 ) );
	}
	
	public Calibration filter ( Calibration calibration ) {
		return ( this.filter( calibration.getLeftPower0to255(), calibration.getRightPower0to255(), calibration.getLeftDirection(), calibration.getRightDirection() ) );
	}
	
	public String toString () {
		return ( leftMotorSL.toString()+"\n"+rightMotorSL.toString()+"\n"+leftMotorR.toString()+"\n"+rightMotorR.toString()+"\n" );
	}
	
// ========================================	
// MAIN PROGRAMME - TEST
	
	public static void main(String[] args) {
		FilterCalibration filterCalibration = new FilterCalibration();
		boolean testLoad = filterCalibration.loadCalibrationFile();
		if ( testLoad )
			System.out.println(filterCalibration);
		else
			System.out.println("LOADING ERROR");
		System.out.println(filterCalibration.filter(150,150,1,-1));
	}

// ========================================  	
	
}