package classes;

public final class Constants {
	
	private Constants() {}
	
	public static final String databaseName = "daviddb"; // The name of the database. Can be changed as necessary.
	
	public static final String userCollection = "users"; // The name of the user collection.
	public static final String userIDField = "unique_id"; // The name of the ID field in the userCollection.
	public static final String userPrefs = "favorites"; // The name of the array of a user's favorite itemID's.
	
	public static final String itemCollection = "movies"; // Because the test case uses movies.
	public static final String itemIDField = "movie_id"; // The name of the ID field in the itemCollection.
	public static final String itemName = "title"; // Again because the example uses movies.
	public static final String itemCategory = "genre"; // Identify what type of item we're dealing with.
	
	public static final String mongoClient = "mongo"; // The name of the MongoClient.
	public static final int numberOfRecommendations = 5; // Relatively small number to ensure good recs.
	
}
