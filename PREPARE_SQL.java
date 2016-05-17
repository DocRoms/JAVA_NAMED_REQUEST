package routines;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

/*
 * Cette classe permet de créer des requêtes préparées nommées.
 * 
 * Attention à ne pas utiliser d'apostrophe autour des paramètres nommés : 
 *     TO_DATE(:temps_arrete,'yyyyMMdd') // fonctionne
 *     TO_DATE(':temps_arrete','yyyyMMdd') // ne fonctionne PAS
 *
 * Ce qui permet l'utilisation des paramètres du type : 
 *  "SELECT * from :maClass WHERE nom = :monNom"
 *  
 * Ce qui rend les requêtes plus claires, et les possibilités de modification plus simple.
 * 
 * Il suffit de passer au controleur la requête sous sa forme nommée, et un HashTable correspondant aux paramètres utilisés
 * et à leur valeur , pour reprendre l'exemple de la requête ci dessus,
 * il faudra avoir deux paramètres dans le hashtable:
 *   "maClass" => "user"
 *   "monNom" => "Dupond"
 *   
 * (un paramètre peut être utilisé à plusieurs endroit)
 * 
 * Ainsi, cette classe se chargera d'associer les deux.
 * 
 * Pour Executer la requête il suffit d'appeller la fonction "executeQuery()" qui vous raportera un ResultSet utilisable.
 * 
 */
public class PREPARE_SQL {
	/**
	 * Permet de créer un tableau d'association <int, string> pour connaitre le numéro 
	 * du point d'intérogation dans la chaine, et son associé nommé.
	 */
	private Hashtable<Integer, String> _ASSOCIATE_TABLE = new Hashtable<Integer, String>();
	
	/**
	 * Dictionnaire associatif entre le nom des paramètres et leur valeur associée.
	 */
	private Hashtable<String, String> _PARAMETERS = new Hashtable<String, String>();
	
	/**
	 * Requête nommée initiée dans le constructeur.
	 */
	private String _NAMMED_REQUEST = null;
	
	/**
	 * Requête préparée automatiquement par cette classe.
	 */
	private String _PREPARED_REQUEST = null;
	
	
	/**
	 * Défini une requête préparée nommée , avec une liste de résultats nommés.
	 * @param request Requête SQL nommée.
	 * @param params Liste des paramètres nommés.
	 * @return
	 */
	public BI_PREPARE_SQL(String request, Hashtable<String, String> params){
		// Sauvegarde des paramètres.
		_PARAMETERS = params;
		_NAMMED_REQUEST = request + " ";
		_PREPARED_REQUEST = request + " ";
		
		// Création de la requête préparée.
		CreatePreparedRequest();
	}
	
	/**
	 * 
	 */
	private void CreatePreparedRequest(){
		// On vérifie qu'il existe bien un paramètre nommé dans la requête;
		boolean existParam = _PREPARED_REQUEST.contains(":");
		
		// On défini une valeur incrémentable, pour connaitre le nombre de paramètres.
		int paramNumber = 0;
		
		// Tant qu'il existe un paramètre nommé, on le stocke dans son tableau associatif (index/nom).
		while (existParam){
			// incrément du numéro du paramètre.
			paramNumber++;
			
			// recherche de la première occurence à ":", qui est le premier caract_re du paramètre nommé.
			int twoPointIndex = _PREPARED_REQUEST.indexOf(":");
			// recherche de la première occurence à " ", "," ou ")" à partir de l'indexe des deux points.
			int lastIndex = 0;
			int spaceIndex = _PREPARED_REQUEST.indexOf(" ", twoPointIndex);
			int comaIndex = _PREPARED_REQUEST.indexOf(",", twoPointIndex);
			int parenthesisIndex = _PREPARED_REQUEST.indexOf(")", twoPointIndex);
			int apostropheIndex = _PREPARED_REQUEST.indexOf("'", twoPointIndex);
			
			// On défini en index de fin de mot, l'index le plus proche du début du mot.
			lastIndex = spaceIndex;
			
			if (comaIndex < lastIndex && comaIndex != -1){
				lastIndex = comaIndex;
			}
			if (parenthesisIndex < lastIndex && parenthesisIndex != -1){
				lastIndex = parenthesisIndex;
			}
			if (apostropheIndex < lastIndex && apostropheIndex != -1){
				lastIndex = apostropheIndex;
			}
			// paramètre trouvé.
			String foundParam = _PREPARED_REQUEST.substring(twoPointIndex, lastIndex);

			// Remplacement du paramètre dans la requête.
			_PREPARED_REQUEST = _PREPARED_REQUEST.replaceFirst(foundParam, "?");		
			
			// Ajout du paramètre et de son index au dictionnaire.
			_ASSOCIATE_TABLE.put(paramNumber,foundParam.substring(1));

			//On essaye de rechercher un autre paramètre.
			existParam = _PREPARED_REQUEST.contains(":");
		}
	}
	
	/**
	 * 
	 * @return la requête préparée sous un format "SELECT * FROM user = ?"
	 */
	public String getPreparedRequest(){
		return _PREPARED_REQUEST;
	}
	
	/**
	 * 
	 * @return la requête nommée sous un format "SELECT * FROM user = :user"
	 */
	public String getNamedRequest(){
		return _NAMMED_REQUEST;
	}
	
	/**
	 * 
	 * @return le hastable contenant les paramètres et leur valeur.
	 */
	public Hashtable<String, String> getParameters(){
		return _PARAMETERS;
	}
	
	/**
	 * 
	 * @return
	 */
	public ResultSet executeQuery() {
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	      
	    // Initialisation de la requête préparée.
  	  	try {
			stmt = BI_SQL.getPreparedStatementUlis(_PREPARED_REQUEST);
			for (int key : _ASSOCIATE_TABLE.keySet()) {
			   String keyValue = _ASSOCIATE_TABLE.get(key);
			   
			   if (_PARAMETERS.containsKey(keyValue)){
				   stmt.setString(key, _PARAMETERS.get(keyValue));
			   }
			}
			
			// Execution de cette requête.
	        rs = stmt.executeQuery();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs;
	}
}
