# JAVA_NAMED_REQUEST
This class exist for create nammed prepared request in Java.
For use this : 

		String _SQL = "SELECT userName FROM myTable WHERE one = :id"
		          + "AND name = :username"
		          + "AND code = :code"
		          + "AND pseudo = :userpseudo"
		          + "AND five = :five";
		          
		Hashtable<String, String> SQL_PARAMS = new Hashtable<String, String>();
		SQL_PARAMS.put("id", "9787");
		SQL_PARAMS.put("username", "Lulu");
		SQL_PARAMS.put("code", "786-45_66");
		SQL_PARAMS.put("userpseudo", "Foo");
		SQL_PARAMS.put("five", "Sils");
		
And not this : 

		String _SQL = "SELECT userName FROM myTable WHERE one = ?" // param 1 (id)
		          + "AND name = ?" // param 2 (username)
		          + "AND code = ?" // param 3 (code)
		          + "AND pseudo = ?" // param 4 (pseudo)
		          + "AND five = ?"; // param 5
		          
		stmt = prepareStatement(_SQL);
		
		Hashtable<String, String> SQL_PARAMS = new Hashtable<String, String>();
		stmt.setString(1, "9787"); // param 1 (id)
		stmt.setString(2, "Lulu"); // param 2 (username)
		stmt.setString(3, "786-45_66"); // param 3 (code)
		stmt.setString(4, "Foo"); // param 4 (pseudo)
		stmt.setString(5, "Sils"); // param 5


# How to Use? 

    // Create your query with nammed parameters like that ":param"
		String _SQL = "SELECT userName FROM myTable WHERE one = :id"
		          + "AND name = :username"
		          + "AND code = :code"
		          + "AND pseudo = :userpseudo"
		          + "AND five = :five";
		          
		// Create an HasTable and set the values of your parameters.
		Hashtable<String, String> SQL_PARAMS = new Hashtable<String, String>();
		SQL_PARAMS.put("id", "9787");
		SQL_PARAMS.put("username", "Lulu");
		SQL_PARAMS.put("code", "786-45_66");
		SQL_PARAMS.put("userpseudo", "Foo");
		SQL_PARAMS.put("five", "Sils");
		
		// Init the prepare nammed SQL
		PREPARE_SQL prepareSql = new PREPARE_SQL(_SQL, SQL_PARAMS);
			
		// Execute the request and get the resultSet.
		ResultSet result = PREPARE_SQL.executeQuery();
		
		// Play with your results...
	  if (result.next()){
			result.getString("userName"));
		}
