package calibration;

/* Import de bibliothèques =============*/
import java.util.ArrayList;
import java.lang.Math;

/* Description de la classe ===============
Classe qui permet d'enregistrer différents
étalons
=========================================*/
public class MotorParrallelCalibration {

// ========================================
// ATTRIBUTS	
	
	public ArrayList<ParrallelCalibration> parrallelCalibrations = new ArrayList<>();
	
// ========================================	
// METHODES

    // ------------------------------------
    // FONCTIONS UTILITAIRES --------------
    // ------------------------------------
	
    /* Description de la fonction ---------
	ajout d'un étalon
	*/
	public void add ( ParrallelCalibration cP ) {
		parrallelCalibrations.add(cP);
	}
	
   
	/* Description de la fonction ---------
 	new et ajout d'un étalon
	*/
	public void addnew ( MovementEnum movType, boolean leaderWheel, float powerLeft, float powerRight ) {
		parrallelCalibrations.add( new ParrallelCalibration( movType, leaderWheel, powerLeft, powerRight) );
	}
	
	/* Description de la fonction ---------
 	new et ajout d'un étalon
	*/
	public void addnew ( MovementEnum movType, float centralPower ) {
		parrallelCalibrations.add( new ParrallelCalibration( movType, centralPower ) );
	}
	
	/* Description de la fonction ---------
 	suppression de l'étalon
	*/
	public void remove ( int i ) {
		parrallelCalibrations.remove(i);
	}
	
	/* Description de la fonction ---------
 	suppression de l'étalon
	*/
	public void remove ( ParrallelCalibration cP )	{
		parrallelCalibrations.remove(cP);
	}
	
	/* Description de la fonction ---------
 	suppression de tous les étalons
	*/
	public void removeAll () {
		while ( size() != 0 ){
			parrallelCalibrations.remove(0);
		}
	}
	
	/* Description de la fonction ---------
 	taille de la liste des étalons
	*/
	public int size () {
		return ( parrallelCalibrations.size() );
	}
	
	/* Description de la fonction ---------
 	retourne l'étalon choisit de la
 	liste
	*/
	public ParrallelCalibration get ( int i ) {
		return ( parrallelCalibrations.get(i) );
	}
	
	/* Description de la fonction ---------
	retourne le dernier étalon du type
	entré
	*/
	public ParrallelCalibration getLastMovType( MovementEnum movType, int N ) {
		for ( int i = N-1; i >= 0; i-- ) {
			if ( movType == parrallelCalibrations.get(i).getME() )
				return parrallelCalibrations.get(i);
		}
		return new ParrallelCalibration (movType, 0);
	}
	
	/* Description de la fonction ---------
	fixe un des étalons
	*/
	public void set ( int i, ParrallelCalibration pC ) {
		parrallelCalibrations.set( i, pC );
	}
	
	/* Description de la fonction ---------
	échange les 2 étalons sélectionnés
	*/
	public void exchange ( int i, int j ) {
		ParrallelCalibration pCTemp = parrallelCalibrations.get(i);
		parrallelCalibrations.set ( i, parrallelCalibrations.get(j) );
		parrallelCalibrations.set ( j, pCTemp );
	}
	
	/* Description de la fonction ---------
	retourne le nombre d'étlon du type
	sélectionné
	*/
	public int typeNumber ( MovementEnum movType ) {
		int N = 0;
		for ( int i = 0; i < size(); i++ ) {
			if ( get(i).getME() == movType )
				N++;
		}
		return ( N );
	}
	
	/* Description de la fonction ---------
	sépare les types dans la liste
	*/
	public void seperateTypes () {
		MovementEnum movType;
		int J = 0;
		for ( int type = 0; type <= 4; type++ ) {
			switch ( type ){
			case 0:
				movType = MovementEnum.STAND;
				break;
			case 1:
				movType = MovementEnum.FORWARD;
				break;
			case 2:
				movType = MovementEnum.BACK;
				break;
			case 3:
				movType = MovementEnum.ROTATIONLEFT;
				break;
			case 4:
				movType = MovementEnum.ROTATIONRIGHT;
				break;
			default:
				movType = MovementEnum.STAND;
				break;
			}
			int N = typeNumber ( movType );
			int I = 0;
			for ( int i = 0; i < N; i++ ){
				I = 0;
				while ( get(I).getME() != movType )
					I++;
				exchange ( I, J );
				J++;
			}
		}
	}
	
	/* Description de la fonction ---------
	ordonne tous les étalons de la liste
	*/
	public void order () {
		if ( parrallelCalibrations.size() != 0 ) {
			seperateTypes();
			MovementEnum movType;
			int J = 0;
			for ( int type = 0; type <= 4; type++ ) {
				switch ( type ){
				case 0:
					movType = MovementEnum.STAND;
					break;
				case 1:
					movType = MovementEnum.FORWARD;
					break;
				case 2:
					movType = MovementEnum.BACK;
					break;
				case 3:
					movType = MovementEnum.ROTATIONLEFT;
					break;
				case 4:
					movType = MovementEnum.ROTATIONRIGHT;
					break;
				default:
					movType = MovementEnum.STAND;
					break;
				}
				
				boolean permut = false;
				
				int N = typeNumber ( movType );
				do {
					permut = false;
					for ( int i = J; i < J+N-1; i++ ) {
						if ( get(i).getPowerCentral() > get(i+1).getPowerCentral() ) {
							exchange(i, i+1);
							permut = true;
						}
					}
				} while ( permut );
				
				J+=N;
			}
		}
	}
	
    // ------------------------------------
    // FONCTIONS PRINCIPALES --------------
    // ------------------------------------
	
	/* Description de la fonction ---------
	directeur linéaire entré 2 étalons
	*/
	private float linearDirector ( float pLead1, float pLead2, float pReal1, float pReal2 ) {
		return ( (float)( (pReal2 - pReal1)/(pLead2 - pLead1) ) );
	}
	
	/* Description de la fonction ---------
	constante de la fonctions linéaire des
	2 étalons
	*/
	private float linearConstant ( float pLead1, float pLead2, float pReal1, float pReal2 ) {
		return ( pReal1 - pLead1*linearDirector ( pLead1, pLead2, pReal1, pReal2 )  );
	}
	
	/* Description de la fonction ---------
	éffectue un étalonnage
	*/
	public ParrallelCalibration calibration ( MovementEnum movType, float power ) {
		//TODO : WARNING : corriger les erreurs possibles et adapter le calcul pour tous les types de mouvements
		ParrallelCalibration pC = new ParrallelCalibration ( movType, true, 0, 0 );
		//TODO : WARNING : OUT OF BOUNDS
		for ( int i = 1; i < parrallelCalibrations.size(); i++ ) {
			ParrallelCalibration pC2 = parrallelCalibrations.get(i);
			//TODO : WARNING : are we always going in this if ?
			if ( pC2.getPowerLead() >= power && pC2.getME() == movType ) {
				//TODO : WARNING : BE SURE OF THIS
				ParrallelCalibration pC1 = getLastMovType(movType,i);
				float pLead1 = pC1.getPowerLead();
				float pLead2 = pC2.getPowerLead();
				float aLeft = linearDirector ( pLead1, pLead2, pC1.getPowerLeft(), pC2.getPowerLeft() );
				float aRight = linearDirector ( pLead1, pLead2, pC1.getPowerRight(), pC2.getPowerRight() );
				float bLeft = linearConstant ( pLead1, pLead2, pC1.getPowerLeft(), pC2.getPowerLeft() );
				float bRight = linearConstant ( pLead1, pLead2, pC1.getPowerRight(), pC2.getPowerRight() );
				float powerLeft = aLeft*power + bLeft;
				float powerRight = aRight*power + bRight;
				boolean leaderWheel;
				if ( Math.abs ( power - powerLeft ) < Math.abs ( power - powerRight ) ) {
					leaderWheel = false;
				} else {
					leaderWheel = true;
				}
				pC = new ParrallelCalibration ( movType, leaderWheel, powerLeft, powerRight, power );
				return ( pC );
			}
		}
		// RETURN OR EXCEPTION
		return (pC);
	}
	
//========================================
	
}