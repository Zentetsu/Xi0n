package tools;

/* Import de bibliothèques =============*/
import java.util.ArrayList;
import java.util.Scanner;

/* Description de la classe ===============
Classe qui permet la capture entrée par
l'utilisateur au clavier
=========================================*/
public class Keyboard {

// ========================================
// ATTRIBUTS
	
	private Scanner scanner = new Scanner(System.in);
    private ArrayList<String> listeInteger = new ArrayList<String>();
    
 // ========================================	
 // CONSTRUCTOR
    
    public Keyboard () {
    	this.fillListInteger();
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
    public String entrerKeyboardString ()
    {
        return this.scanner.nextLine();
    }
    
    /* Description de la fonction ---------
	fonction de capture d'entier
	*/
    public int entrerClavierInt ()
    {
    	this.fillListInteger();
    	String string = new String();
    	int y = 0;
    	do
    	{
    		string=this.scanner.next();
    		if(isInteger(string))
    			y = Integer.parseInt(string);
    	}
    	while(!isInteger(string));
    	return y;
    }
    
    /* Description de la fonction ---------
	fonction d'adaption de la liste
	d'entiers
	*/
    private void fillListInteger ()
    {
    	for(Integer i = 0;i<10;i++){
    		listeInteger.add(new String(i.toString()));
    	}
    }
   
    /* Description de la fonction ---------
	fonction de test de la chaine de
	caractère qui peut être adaptée pour
	être transformée en entier
	*/
    public boolean isInteger ( String s )
    {
    	if(s.isEmpty()){
    		return false;
    	}
    	for(int i = 0 ; i< s.length() ; i++){
    		if(!listeInteger.contains(s.substring(i, i+1))){
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
    public boolean isFloat ( String s )
    {
    	boolean testPoint = false;
    	if(s.isEmpty()){
    		return false;
    	}
    	for(int i = 0 ; i< s.length() ; i++)
    	{
    		if (!listeInteger.contains(s.substring(i, i+1)))
    		{
    			if ( !testPoint ) {
    				if ( s.substring(i, i+1).equals(".") )
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
 	fonction pour fermer le clavier
 	*/
    public void close()
    {
    	scanner.close();
    }
   
//========================================
    
}

