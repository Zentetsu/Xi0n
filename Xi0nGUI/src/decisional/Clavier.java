package decisional;

/* Import de bibliothèques =============*/
import java.util.ArrayList;
import java.util.Scanner;

/* Description de la classe ===============
Classe qui permet la capture entrée par
l'utilisateur au clavier
=========================================*/
public class Clavier {

// ========================================
// ATTRIBUTS
	
	private Scanner scanner = new Scanner(System.in);
    private ArrayList<String> listeEntier = new ArrayList<String>();
    
 // ========================================	
 // CONSTRUCTOR
    
    public Clavier () {
    	this.remplirListEntier();
    }
    
// ========================================	
// METHODES
    
    // ------------------------------------
    // FONCTIONS PRINCIPALES --------------
    // ------------------------------------
    
    /* Description de la fonction ---------
	fonction de capture des chaines de
	carctères
	*/
    public String entrerClavierString ()
    {
        return this.scanner.nextLine();
    }
    
    /* Description de la fonction ---------
	fonction de capture d'entier
	*/
    public int entrerClavierInt ()
    {
    	this.remplirListEntier();
    	String string = new String();
    	int y = 0;
    	do
    	{
    		string=this.scanner.next();
    		if(isEntier(string))
    			y = Integer.parseInt(string);
    	}
    	while(!isEntier(string));
    	return y;
    }
    
    /* Description de la fonction ---------
	fonction d'adaption de la liste
	d'entiers
	*/
    private void remplirListEntier ()
    {
    	for(Integer i = 0;i<10;i++){
    		listeEntier.add(new String(i.toString()));
    	}
    }
   
    /* Description de la fonction ---------
	fonction de test de la chaine de
	caractère qui peut être adaptée pour
	être transformée en entier
	*/
    public boolean isEntier ( String chaine )
    {
    	if(chaine.isEmpty()){
    		return false;
    	}
    	for(int i = 0 ; i< chaine.length() ; i++){
    		if(!listeEntier.contains(chaine.substring(i, i+1))){
    			return false;
    		}
    	}
    	return true;
    }
    
    /* Description de la fonction ---------
	fonction de test de la chaine de
	caractère qui peut être adaptée pour
	être transformée en float
	*/
    public boolean isFloat ( String chaine )
    {
    	boolean testPoint = false;
    	if(chaine.isEmpty()){
    		return false;
    	}
    	for(int i = 0 ; i< chaine.length() ; i++)
    	{
    		if (!listeEntier.contains(chaine.substring(i, i+1)))
    		{
    			if ( !testPoint ) {
    				if ( chaine.substring(i, i+1).equals(".") )
    					testPoint = true;
    				else
    					return false;
    			}
    			else
    				return false;
    		}
    	}
    	return true;
    }
    
    /* Description de la fonction ---------
 	fonction qui permet d'effectuer une
 	pause avant de continuer le programme
 	*/
    public void continuer ()
    {
    	System.out.println("Entrer 0 pour continuer");
    	String temp = "";
    	while ( !(temp.equals("0")) )
    		temp = this.scanner.next();
    }
    
    /* Description de la fonction ---------
 	fonction pour fermer le clavier
 	*/
    public void close()
    {
    	scanner.close();
    }
   
//========================================
    
}

