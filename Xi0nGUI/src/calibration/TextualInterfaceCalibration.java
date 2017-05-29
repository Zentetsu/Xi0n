package calibration;

/* Import de bibliothèques =============*/
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import physic.robot.RobotConfig;
import tools.Keyboard;

/* Description de la classe ===============
Interface textuelle du programme permettant
la création du fichier d'étalonnage
=========================================*/
public class TextualInterfaceCalibration {

// ========================================
// ATTRIBUTS
	
	private MotorParrallelCalibration motorParrallelCalibration = new MotorParrallelCalibration (); // enregistrement des étalons
	private boolean printCom = true; // affichage des commandes dans le prochain Mainprint
	private boolean run = false; // lancement du mouvement du robot par l'étalon sélectionné
	private boolean runcon = false; // lancement du mouvement du robot par l'étalon sélectionné sur commande du controleur
	private int selected = 0; // étalon sélectionne
	private boolean quit = false; // quitter le programme avant le prochain Mainprint
	private ParrallelCalibration pCtry = new ParrallelCalibration ( MovementEnum.STAND, 0 ); // étalonnage d'essai
	private boolean calSelected = false; // si l'étalon test est sélectionné
	private boolean calShown = false; // si l'étalon test est smontré
	
// ========================================	
// METHODES

	// ------------------------------------
	// FONCTIONS D'AFFICHAGE --------------
	// ------------------------------------
	
    /* Description de la fonction ---------
	fonction d'affichage principale de
	l'interface
	*/
	public void mainPrint () {
		System.out.println ( " *********************************************************************************************" );
		System.out.println ( " *                                       CALIBRATION                                         *" );
		System.out.println ( " *********************************************************************************************" );
		if ( run )
			System.out.println ( " *|||--> RUNING                                                                              *" );
		else if ( runcon )
			System.out.println ( " *|||--> WAITING CONTROLLER                                                                  *" );
		else
			System.out.println ( " *|||--> OFF                                                                                 *" );
		System.out.println ( " *********************************************************************************************" );
		for ( int i = 0; i< motorParrallelCalibration.size(); i++ ) {
			int stringlength;
			if ( i == selected && !calSelected ) {
				System.out.println ( " */-----------------------------------------------------------------------------------------\\*" );
				System.out.print ( " *|"+i+" - " );
				stringlength= (new String(" *|"+i+" - ")).length();
			}
			else {
				System.out.print ( " * "+i+" - " );
				stringlength= (new String(" *|"+i+" - ")).length();
			}
			System.out.print ( motorParrallelCalibration.get(i).toString() );
			if ( 92 - motorParrallelCalibration.get(i).getLengthString() - stringlength >=0 ) {
				for ( int j = 0; j< ( 92 - motorParrallelCalibration.get(i).getLengthString() - stringlength ); j++ ) {
					System.out.print ( " " );
				}
				if ( i == selected && !calSelected )
					System.out.print ( "|*" );
				else
					System.out.print ( " *" );
			}
			System.out.print ( "\n" );
			if ( i == selected && !calSelected )
				System.out.println ( " *\\-----------------------------------------------------------------------------------------/*" );
		}
		System.out.println ( " *********************************************************************************************" );
		if ( calShown && calSelected ) {
			System.out.println ( " * ---CALIBRATION TRIAL---                                                                   *" );
			System.out.println ( " */-----------------------------------------------------------------------------------------\\*" );
			System.out.print ( " *|Trial - " );
			System.out.print ( pCtry );
			if ( 81 - pCtry.getLengthString() >=0 ) {
				for ( int j = 0; j< ( 81 - pCtry.getLengthString() ); j++ ) {
					System.out.print ( " " );
				}
				System.out.print ( "|*" );
			}
			System.out.print ( "\n" );
			System.out.println ( " *\\-----------------------------------------------------------------------------------------/*" );
			System.out.println ( " *********************************************************************************************" );
		}
		else if ( calShown ) {
			System.out.println ( " * ---CALIBRATION TRIAL---                                                                   *" );
			System.out.print ( " * Trial - " );
			System.out.print ( pCtry );
			if ( 81 - pCtry.getLengthString() >=0 ) {
				for ( int j = 0; j< ( 81 - pCtry.getLengthString() ); j++ ) {
					System.out.print ( " " );
				}
				System.out.print ( " *" );
			}
			System.out.print ( "\n" );
			System.out.println ( " *********************************************************************************************" );
		}
		if ( printCom ) {
			System.out.println ( " * ---COMMANDS---                                                                            *" );
			System.out.println ( " * >> s [x]                   : sélectionne l'étalon x                                       *" );
			System.out.println ( " * >> s cal                   : sélectionne l'étalon test                                    *" );
			System.out.println ( " * >> add [type] [power]      : ajoute un étalon du type et CentralPower sélectionné         *" );
			System.out.println ( " * >> del                     : supprime l'étalon sélectionné                                *" );
			System.out.println ( " * >> del [x]                 : supprime l'étalon x                                          *" );
			System.out.println ( " * >> delall                  : supprime tous les étalons                                    *" );
			System.out.println ( " * >> lp [power]              : affecte le power à la roue gauche de l'étalon sélectionné    *" );
			System.out.println ( " * >> rp [power]              : affecte le power à la roue droite de l'étalon sélectionné    *" );
			System.out.println ( " * >> showcal [type] [power]  : affiche l'étalon test pour le type et le power proposé       *" );
			System.out.println ( " * >> trycal [type] [power]   : affiche et sel l'étalon test pour le type et le power proposé*" );
			System.out.println ( " * >> run                     : fait avancer le robot à l'étalon sélectionné                 *" );
			System.out.println ( " * >> runcon //TODO           : fait avancer le robot à l'étalon sélectionné sur controleur  *" );
			System.out.println ( " * >> stop                    : stoppe le robot                                              *" );
			System.out.println ( " * >> generate                : génère le fichier d'étalonnage                               *" );
			System.out.println ( " * >> order                   : ordonne les étalonnages                                      *" );
			System.out.println ( " * >> com                     : affiche les commandes                                        *" );
			System.out.println ( " * >> quit                    : quitte le programme                                          *" );
			System.out.println ( " *********************************************************************************************" );
			printCom = false;
		}
	}
	
    /* Description de la fonction ---------
	fonction d'execution de la Strin entrée
	en paramètre, affichage des erreurs sur*
	l'entrée de la commande, ressort true,
	si la commande est valide
	*/
	public boolean executeCom ( String com ) {
		
		Keyboard keyboardTest = new Keyboard ();
		String parameter;
		String parameter1;
		String parameter2;
		boolean comCorrect = false;
		
		// Execute s
		if ( com.startsWith("s ")) {
			parameter = com.substring (2);
			if ( parameter.equals("cal")) {
				if ( calShown ) {
					calSelected = true;
					comCorrect = true;
				}
				else
					System.out.println("NO CALIBRATION TRIAL CALCULATED");
			}
			else {
				if ( motorParrallelCalibration.size() != 0 ) {
					boolean testEntier = keyboardTest.isInteger ( parameter );
					if (!testEntier)
						System.out.println("INTEGER PARAMTER EXPECTED");
					else {
						calSelected = false;
						this.s(Integer.parseInt(parameter));
						getCorrectSelected ( selected );
						comCorrect = true;
					}
				}
				else
					System.out.println("NO CALIBRATION SELECTABLE");
			}
		}
		
		// Execute add
		else if ( com.startsWith("add ")) {
			parameter = com.substring (4);
			parameter1 = separateGet1 ( parameter );
			parameter2 = separateGet2 ( parameter );
			if ( !keyboardTest.isInteger( parameter1 ) )
				System.out.println("INTEGER FIRST PARAMTER EXPECTED");
			else
			{
				boolean testFloat = keyboardTest.isFloat ( parameter2 );
				if (!testFloat)
					System.out.println("FLOAT SECOND PARAMTER EXPECTED");
				else {
					int type = Integer.parseInt(parameter1);
					float value = (float)(Double.parseDouble(parameter2));
					if ( type== 1 || type== 2 || type== 3 || type== 4 ) {
						if ( value>=0.4 && value<=1 ) {
							if ( type == 1) {
								if ( !isCalDone ( MovementEnum.FORWARD, value ) ) {
									this.add(MovementEnum.FORWARD, value);
									comCorrect = true;
								}
								else
									System.out.println("CALIBRATION OF "+value*100+" % OF MAX POWER ALREADY DONE FOR FORWARD MOVEMENT");
							}
							if ( type == 2) {
								if ( !isCalDone ( MovementEnum.BACK, value ) ) {
									this.add(MovementEnum.BACK, value);
									comCorrect = true;
								}
								else
									System.out.println("CALIBRATION OF "+value*100+" % OF MAX POWER ALREADY DONE FOR BACK MOVEMENT");
							}
							if ( type == 3) {
								if ( !isCalDone ( MovementEnum.ROTATIONLEFT, value ) ) {
									this.add(MovementEnum.ROTATIONLEFT, value);
									comCorrect = true;
								}
								else
									System.out.println("CALIBRATION OF "+value*100+" % OF MAX POWER ALREADY DONE FOR LEFT ROTATION MOVEMENT");
							}
							if ( type == 4) {
								if ( !isCalDone ( MovementEnum.ROTATIONRIGHT, value ) ) {
									this.add(MovementEnum.ROTATIONRIGHT, value);
									comCorrect = true;
								}
								else
									System.out.println("CALIBRATION OF "+value*100+" % OF MAX POWER ALREADY DONE FOR RIGHT ROTATION MOVEMENT");
							}
							if ( comCorrect ) {
								calSelected = false;
								calShown = false;
								selected = getMaxSelectable ();
								getCorrectSelected ( selected );
							}
							
						}
						else
							System.out.println("0.4 <= VALUE <= 1 EXPECTED");
					}
					else
						System.out.println("1, 2, 3 OR 4 TYPE EXPECTED");
				}
			}
		}
		
		// Execute delall
		else if ( com.equals("delall") ) {
			if ( run )
				this.stop();
			this.delall();
			calSelected = false;
			calShown = false;
			selected = 0;
			comCorrect = true;
		}
		
		// Execute del
		else if ( com.startsWith("del ")) {
			parameter = com.substring (4);
			if ( parameter.equals("cal")) {
				if ( calShown ) {
					calSelected = false;
					calShown = false;
					comCorrect = true;
				}
				else
					System.out.println("NO CALIBRATION TRIAL TO DELETE");
			}
			else {
				boolean testEntier = keyboardTest.isInteger ( parameter );
				if (!testEntier)
					System.out.println("INTEGER PARAMTER EXPECTED");
				else {
					if ( motorParrallelCalibration.size() != 0 ) {
						this.del(Integer.parseInt(parameter));
						calSelected = false;
						calShown = false;
						if ( motorParrallelCalibration.size() != 0 )
							getCorrectSelected ( selected );
						else
							selected = 0;
						if ( motorParrallelCalibration.size() == 0 && run )
							this.stop();
						comCorrect = true;
					}
					else
						System.out.println("NO CALIBRATION DELETABLE");
				}
			}
		}
		
		else if ( com.equals("del")) {
			if ( calShown && calSelected ) {
				calSelected = false;
				calShown = false;
				selected = 0;
				comCorrect = true;
			}
			else {
				if ( motorParrallelCalibration.size() != 0 ) {
					this.del(selected);
					calSelected = false;
					calShown = false;
					if ( motorParrallelCalibration.size() != 0 )
						getCorrectSelected ( selected );
					else
						selected = 0;
					if ( motorParrallelCalibration.size() == 0 && run )
						this.stop();
					comCorrect = true;
				}
				else
					System.out.println("NO CALIBRATION DELETABLE");
			}
		}
		
		// Execute lp
		else if ( com.startsWith("lp ")) {
			parameter = com.substring (3);
			boolean testFloat = keyboardTest.isFloat ( parameter );
			if (!testFloat)
				System.out.println("FLOAT PARAMTER EXPECTED");
			else {
				if ( motorParrallelCalibration.size() != 0 ) {
					if ( !calSelected ) {
						float value = (float)(Double.parseDouble(parameter));
						if ( value>=0.4 && value<=1 ) {
							calSelected = false;
							calShown = false;
							lp ( value );
							getCorrectSelected ( selected );
							comCorrect = true;
						}
						else
							System.out.println("0.4 <= VALUE <= 1 EXPECTED");
					}
					else
						System.out.println("CALIBRATION TRIAL UNWORKABLE");
				}
				else
					System.out.println("NO CALIBRATION WORKABLE");
			}
		}
		
		// Execute rp
		else if ( com.startsWith("rp ")) {
			parameter = com.substring (3);
			boolean testFloat = keyboardTest.isFloat ( parameter );
			if (!testFloat)
				System.out.println("FLOAT PARAMTER EXPECTED");
			else {
				if ( motorParrallelCalibration.size() != 0 ) {
					if ( !calSelected ) {
						float value = (float)(Double.parseDouble(parameter));
						if ( value>=0.4 && value<=1 ) {
							calSelected = false;
							calShown = false;
							getCorrectSelected ( selected );
							rp ( value );
							comCorrect = true;
						}
						else
							System.out.println("0.4 <= VALUE <= 1 EXPECTED");
					}
					else
						System.out.println("CALIBRATION TRIAL UNWORKABLE");
				}
				else
					System.out.println("NO CALIBRATION WORKABLE");
			}
		}
	
		// Execute run
		else if ( com.equals("runcon") ) {
			if ( motorParrallelCalibration.size() != 0 ) {
				this.runcon();
				comCorrect = true;
			}
			else
				System.out.println("NO CALIBRATION SELECTED");
		}
		
		else if ( com.equals("run") ) {
			if ( motorParrallelCalibration.size() != 0 ) {
				this.run();
				comCorrect = true;
			}
			else
				System.out.println("NO CALIBRATION SELECTED");
		}
		
		// Execute stop
		else if ( com.equals("stop") ) {
			this.stop();
			comCorrect = true;
		}
		
		// Execute showcal
		if ( com.startsWith("showcal ")) {
			parameter = com.substring (8);
			parameter1 = separateGet1 ( parameter );
			parameter2 = separateGet2 ( parameter );
			if ( !keyboardTest.isInteger( parameter1 ) )
				System.out.println("INTEGER FIRST PARAMTER EXPECTED");
			else
			{
				int type = Integer.parseInt(parameter1);
				boolean testFloat = keyboardTest.isFloat ( parameter2 );
				if (!testFloat)
					System.out.println("FLOAT SECOND PARAMTER EXPECTED");
				else {
					MovementEnum tempMoveEnum;
					if ( type== 1 || type== 2 || type== 3 || type== 4 ) {
						switch ( type ){
						case 1:
							tempMoveEnum = MovementEnum.FORWARD;
							break;
						case 2:
							tempMoveEnum = MovementEnum.BACK;
							break;
						case 3:
							tempMoveEnum = MovementEnum.ROTATIONLEFT;
							break;
						case 4:
							tempMoveEnum = MovementEnum.ROTATIONRIGHT;
							break;
						default:
							tempMoveEnum = MovementEnum.STAND;
							break;
						}
						if ( isCalDone( tempMoveEnum, (float)0.4) && isCalDone( tempMoveEnum, 1) ) {
							float value = (float)(Double.parseDouble(parameter2));
							if ( value>=0.4 && value<=1 ) {
								pCtry = calibrate ( tempMoveEnum, value );
								calShown = true;
								comCorrect = true;
							}
							else
								System.out.println("0.4 <= VALUE <= 1 EXPECTED");
						}
						else
							System.out.println("40 % AND 100% CALIBRATION NEEDED");
					}
					else
						System.out.println("1, 2, 3 OR 4 TYPE EXPECTED");
				}
			}
		}
		
		// Execute trycal
		if ( com.startsWith("trycal ")) {
			parameter = com.substring (7);
			parameter1 = separateGet1 ( parameter );
			parameter2 = separateGet2 ( parameter );
			if ( !keyboardTest.isInteger( parameter1 ) )
				System.out.println("INTEGER FIRST PARAMTER EXPECTED");
			else
			{
				int type = Integer.parseInt(parameter1);
				boolean testFloat = keyboardTest.isFloat ( parameter2 );
				if (!testFloat)
					System.out.println("FLOAT SECOND PARAMTER EXPECTED");
				else {
					MovementEnum tempMoveEnum;
					if ( type== 1 || type== 2 || type== 3 || type== 4 ) {
						switch ( type ) {
						case 1:
							tempMoveEnum = MovementEnum.FORWARD;
							break;
						case 2:
							tempMoveEnum = MovementEnum.BACK;
							break;
						case 3:
							tempMoveEnum = MovementEnum.ROTATIONLEFT;
							break;
						case 4:
							tempMoveEnum = MovementEnum.ROTATIONRIGHT;
							break;
						default:
							tempMoveEnum = MovementEnum.STAND;
							break;
						}
						if ( isCalDone( tempMoveEnum, (float)0.4 ) && isCalDone( tempMoveEnum, 1 ) ) {
							float value = (float)(Double.parseDouble(parameter2));
							if ( value>=0.4 && value<=1 ) {
								pCtry = calibrate ( tempMoveEnum, value );
								calShown = true;
								calSelected = true;
								comCorrect = true;
							}
							else
								System.out.println("0.4 <= VALUE <= 1 EXPECTED");
						}
						else
							System.out.println("40 % AND 100% CALIBRATION NEEDED");
					}
					else
						System.out.println("1, 2, 3 OR 4 TYPE EXPECTED");
				}
			}
		}
		
		// Execute generate
		else if ( com.equals("generate") ) {
			if ( isCalDone(MovementEnum.FORWARD, (float)0.4) && isCalDone(MovementEnum.FORWARD,1) &&  isCalDone(MovementEnum.BACK,(float)0.4) && isCalDone(MovementEnum.BACK,1) && isCalDone(MovementEnum.ROTATIONLEFT,(float)0.4) && isCalDone(MovementEnum.ROTATIONLEFT,1) && isCalDone(MovementEnum.ROTATIONRIGHT,(float)0.4) && isCalDone(MovementEnum.ROTATIONRIGHT,1) ) {
				order();
				comCorrect = this.generate();
				if ( !comCorrect )
					System.out.println("DOCUMENT UNWRITABLE");
			}
			else
				System.out.println("40 % AND 100% CALIBRATION NEEDED FOR ALL TYPE OF MOVEMENT");
		}
		
		// Execute order
		else if ( com.equals("order") ){
			motorParrallelCalibration.order();
			comCorrect = true;
		}
			
		
		// Execute com
		else if ( com.equals("com") ) {
			this.com();
			comCorrect = true;
		}
		
		// Execute quit
		else if ( com.equals("quit") ) {
			this.quit();
			comCorrect = true;
		}
		
		// ERROR
		if ( !comCorrect )
			System.out.println("INVALID COMMAND");
		
		return (comCorrect);
		
	}
	
	// ------------------------------------
	// FONCTIONS EXECUTABLES --------------
	// ------------------------------------
	
    /* Description de la fonction ---------
	changement de l'étalon sélectionnés
	*/
	private void s ( int x ) {
		selected = x;
	}
	
    /* Description de la fonction ---------
	ajout d'un nouvel étalon
	*/
	private void add (  MovementEnum movType, float centralPower ) {
		motorParrallelCalibration.addnew( movType, centralPower);
	}
	
    /* Description de la fonction ---------
	suppression de l'étalon indiqué
	*/
	private void del ( int x ) {
		if ( x >= 0 && x < motorParrallelCalibration.size() ) {
			if ( motorParrallelCalibration.size() == 1 && run )
				this.stop();
			motorParrallelCalibration.remove(x);
		}
	}
	
    /* Description de la fonction ---------
	suppression de tous les étalons
	*/
	private void delall () {
		motorParrallelCalibration.removeAll ();
	}
	
    /* Description de la fonction ---------
	fixe la puissance à droite et la
	puissance à droite à la valeur central
	puis fixe celle de gauche à celle
	demandée
	*/
	private void lp ( float power ) {
		motorParrallelCalibration.get(selected).setPowerLeft(motorParrallelCalibration.get(selected).getPowerCentral());
		motorParrallelCalibration.get(selected).setPowerRight(motorParrallelCalibration.get(selected).getPowerCentral());
		motorParrallelCalibration.get(selected).setPowerLeft(power);
		motorParrallelCalibration.get(selected).setCorrectLeaderWheel();
	}
	
    /* Description de la fonction ---------
	fixe la puissance à droite et la
	puissance à droite à la valeur central
	puis fixe celle de droite à celle
	demandée
	*/
	private void rp ( float power ) {
		motorParrallelCalibration.get(selected).setPowerLeft(motorParrallelCalibration.get(selected).getPowerCentral());
		motorParrallelCalibration.get(selected).setPowerRight(motorParrallelCalibration.get(selected).getPowerCentral());
		motorParrallelCalibration.get(selected).setPowerRight(power);
		motorParrallelCalibration.get(selected).setCorrectLeaderWheel();
	}
	
    /* Description de la fonction ---------
	demande au robot de se lancer sur
	l'étalonnage sélectionné
	*/
	private void run () {
		run = true;
		runcon = false;
	}
	
    /* Description de la fonction ---------
	demande au robot de se lancer sur
	l'étalonnage sélectionné sur ordre
	du controleur
	*/
	private void runcon () {
		run = false;
		runcon = true;
	}
	
    /* Description de la fonction ---------
	stoppe le déplacement du robot
	*/
	private void stop () {
		run = false;
		runcon = false;
	}
	
    /* Description de la fonction ---------
	éffectue un test d'étalonnage
	*/
	private ParrallelCalibration calibrate ( MovementEnum movType, float power ) {
		order();
		return (motorParrallelCalibration.calibration ( movType, power ));
	}
	
    /* Description de la fonction ---------
	génère le fichier d'étalonnage
	*/
	private boolean generate () {
		order();
		File f = new File ( "assets/calibration/calibration.txt" );
		
		try {
			f.createNewFile();
		}
		catch ( IOException e ) {
			return ( false );
		}
		
		FileWriter fW;
		try {
			
			fW = new FileWriter ( f );
			for ( int i = 255;  i >= 0; i-- ) {
				if ( (float)i/(float)255 >= 0.4 && (float)i/(float)255 <= 1 ) {
					fW.write( ""+(-1)*powerToInt(calibrate(MovementEnum.BACK, (float)i/(float)255).getPowerLeft()) );
					fW.write("\n");
				}
			}
			for ( int i = 0;  i <= 255; i++ ) {
				if ( (float)i/(float)255 >= 0.4 && (float)i/(float)255 <= 1 ) {
					fW.write( ""+powerToInt(calibrate(MovementEnum.FORWARD, (float)i/(float)255).getPowerLeft()) );
					fW.write("\n");
				}
			}
			fW.write("...\n");
			for ( int i = 255;  i >= 0; i-- ) {
				if ( (float)i/(float)255 >= 0.4 && (float)i/(float)255 <= 1 ) {
					fW.write( ""+(-1)*powerToInt(calibrate(MovementEnum.ROTATIONLEFT, (float)i/(float)255).getPowerLeft()) );
					fW.write("\n");
				}
			}
			for ( int i = 0;  i <= 255; i++ ) {
				if ( (float)i/(float)255 >= 0.4 && (float)i/(float)255 <= 1 ) {
					fW.write( ""+powerToInt(calibrate(MovementEnum.ROTATIONRIGHT, (float)i/(float)255).getPowerLeft()) );
					fW.write("\n");
				}
			}
			fW.write("...\n");
			for ( int i = 255;  i >= 0; i-- ) {
				if ( (float)i/(float)255 >= 0.4 && (float)i/(float)255 <= 1 ) {
					fW.write( ""+(-1)*powerToInt(calibrate(MovementEnum.BACK, (float)i/(float)255).getPowerRight()) );
					fW.write("\n");
				}
			}
			for ( int i = 0;  i <= 255; i++ ) {
				if ( (float)i/(float)255 >= 0.4 && (float)i/(float)255 <= 1 ) {
					fW.write( ""+powerToInt(calibrate(MovementEnum.FORWARD, (float)i/(float)255).getPowerRight()) );
					fW.write("\n");
				}
			}
			fW.write("...\n");
			for ( int i = 255;  i >= 0; i-- ) {
				if ( (float)i/(float)255 >= 0.4 && (float)i/(float)255 <= 1 ) {
					fW.write( ""+(-1)*powerToInt(calibrate(MovementEnum.ROTATIONRIGHT, (float)i/(float)255).getPowerRight()) );
					fW.write("\n");
				}
			}
			for ( int i = 0;  i <= 255; i++ ) {
				if ( (float)i/(float)255 >= 0.4 && (float)i/(float)255 <= 1 ) {
					fW.write( ""+powerToInt(calibrate(MovementEnum.ROTATIONLEFT, (float)i/(float)255).getPowerRight()) );
					fW.write("\n");
				}
			}
			fW.write("\n");
			fW.close();
			return true;
		}
		catch ( IOException e ) {
			return ( false );
		}
	}
	
    /* Description de la fonction ---------
	ordonne les étalons dans l'ordre
	souhaité par le programme dans la
	plupart des fonctions
	*/
	private void order () {
		motorParrallelCalibration.order();
	}
	
    /* Description de la fonction ---------
	permet de réafficher les commandes
	*/
	private void com () {
		printCom = true;
	}
	
    /* Description de la fonction ---------
	quitte le programme
	*/
	private void quit () {
		quit = true;
	}
	
	// ------------------------------------
	// FONCTIONS UTILITAIRES --------------
	// ------------------------------------
	
	/* Description de la fonction ---------
	retourne la string précédent le
	premier espace de la string en
	paramètre
	*/
	private String separateGet1 ( String s ) {
		String s2 = "";
		for ( int i = 0; i < s.length(); i++ ) {
			if ( !s.substring(i,i+1).equals(" ") )
				s2 += s.substring(i,i+1);
			else
				return s2;	
		}
		return s2;
	}
	
	/* Description de la fonction ---------
	retourne la string suivant le
	premier espace de la string en
	paramètre
	*/
	private String separateGet2 ( String s ) {
		String s2 = "";
		int N = 0;
		for ( N = 0; !s.substring(N,N+1).equals(" ") && N < s.length()-1 ; N++ ) {}
		for ( int i = N+1; i < s.length(); i++ ) {
			s2 += s.substring(i,i+1);
		}
		return s2;
	}
	
	/* Description de la fonction ---------
	retourne le plus grand numéro
	d'étalon qui puisse être sélectionné
	*/
	private int getMaxSelectable () {
		return (motorParrallelCalibration.size()-1);
	}
	
	/* Description de la fonction ---------
	permet de sélectionner un numéro 
	d'étalon valide
	*/
	private void getCorrectSelected ( int selected ) {
		if ( selected<0 )
			this.selected = 0;
		else if ( selected>getMaxSelectable () )
			this.selected = getMaxSelectable ();
	}
	
	/* Description de la fonction ---------
	vériie que l'étalon proposé existe
	*/
	private boolean isCalDone ( MovementEnum movType, float power ) {
		for ( int i= 0; i<motorParrallelCalibration.size(); i++ ) {
			if ( motorParrallelCalibration.get(i).getPowerCentral () == power && motorParrallelCalibration.get(i).getME() == movType )
				return true;
		}
		return false;
	}
	
	/* Description de la fonction ---------
	retourne l'étalon sélectionné
	*/
	public ParrallelCalibration getSelectedPC () {
		if ( motorParrallelCalibration.size() == 0 ) {
			if ( !calSelected )
				return ( motorParrallelCalibration.get(selected) );
			else
				return ( pCtry );
		}
		return ( new ParrallelCalibration ( MovementEnum.STAND, 0) );
	}
	
	/* Description de la fonction ---------
	retourne la valeur de quit
	*/
	public boolean getQuit () {
		return quit;
	}
	
	/* Description de la fonction ---------
	passe une valeur de 0 à 1 à une valeur
	de 0 à 255
	*/
	private int powerToInt ( float p )
	{
		return ( (int)(p*255) );
	}
	
	/* Description de la fonction ---------
	adapte la classe d'étalon pour le
	programme étalon à la classe pour le
	déplacement du robot
	*/
	private RobotConfig pCtoC ( ParrallelCalibration pC ) {
		int leftPower0to255 = powerToInt ( pC.getPowerLeft() );
		int rightPower0to255 = powerToInt ( pC.getPowerRight() );
		switch ( pC.getME() ) {
		case STAND:
			return ( new RobotConfig ( leftPower0to255, rightPower0to255, 0, 0 ));
		case FORWARD :
			return ( new RobotConfig ( leftPower0to255, rightPower0to255, 1, 1 ));
		case BACK :
			return ( new RobotConfig ( leftPower0to255, rightPower0to255, -1, -1 ));
		case ROTATIONLEFT :
			return ( new RobotConfig ( leftPower0to255, rightPower0to255, -1, 1 ));
		case ROTATIONRIGHT :
			return ( new RobotConfig ( leftPower0to255, rightPower0to255, 1, -1 ));
		default :
			return ( new RobotConfig ( leftPower0to255, rightPower0to255, 0, 0 ));
		}
		
	}
	
	/* Description de la fonction ---------
	retourne l'étalon pour le tester
	sur le robot
	*/
	public RobotConfig getSelectedCalibration () {
		return pCtoC ( getSelectedPC() );
	}
	
	/* Description de la fonction ---------
	retourne l'étalon pour le tester
	sur le robot, renvoi un étalon nul si
	le run est nul
	*/
	public RobotConfig getCalibrationToRun () {
		if ( run )
			return ( getSelectedCalibration () );
		else
			return ( new RobotConfig ( 0, 0, 0, 0 ) );
	}
	
	/* Description de la fonction ---------
	créer un nouvel étalon et l'ajoute à
	la liste
	*/
	public void addnew ( MovementEnum movType, boolean leaderWheel, float powerLeft, float powerRight ) {
		motorParrallelCalibration.addnew( movType, leaderWheel, powerLeft, powerRight);
	}
	
// ========================================	
// MAIN PROGRAMME
	
	public static void main(String[] args) {
		
		Keyboard keyboard = new Keyboard ();
		TextualInterfaceCalibration textualInterfaceCalibration = new TextualInterfaceCalibration();
		
		textualInterfaceCalibration.addnew(MovementEnum.FORWARD, true, (float)0.4, (float)0.4);
		textualInterfaceCalibration.addnew(MovementEnum.FORWARD, true, 1, (float)1);
		textualInterfaceCalibration.addnew(MovementEnum.BACK, true, (float)0.4, (float)0.4);
		textualInterfaceCalibration.addnew(MovementEnum.BACK, true, (float)1, 1);
		textualInterfaceCalibration.addnew(MovementEnum.ROTATIONLEFT, true, (float)0.4, (float)0.4);
		textualInterfaceCalibration.addnew(MovementEnum.ROTATIONLEFT, true, 1, 1);
		textualInterfaceCalibration.addnew(MovementEnum.ROTATIONRIGHT, true, (float)0.4, (float)0.4);
		textualInterfaceCalibration.addnew(MovementEnum.ROTATIONRIGHT, true, (float)1, 1);
		
		while ( !textualInterfaceCalibration.getQuit() ) {
			textualInterfaceCalibration.mainPrint();
			boolean comCorrect;
			do {
				System.out.print ( ">> " );
				String com = keyboard.entrerKeyboardString ();
				comCorrect = textualInterfaceCalibration.executeCom ( com );
			} while ( !comCorrect );
			/* EFFACER LE SHELL : CETTE VERSION LA NE FONCTIONNE PAS
			try {
				Runtime.getRuntime().exec("cls/clear");
			}
			catch ( Exception e ) {} */
			System.out.print ( "\n\n\n\n\n\n\n\n\n\n" );
		}
		
	}

// ========================================    

}

