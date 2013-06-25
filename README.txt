Recommender - A Data Recommendation Engine Built with MongoDB
=============================================================
Built by David Khavari, 10gen: The MongoDB Company, 2013

Purpose
=======
This application seeks to explore the capabilities of MongoDB's document-oriented
storage in an applied, real-world context.

Description
===========
Recommender is a simple item recommendation system.
The included example is a small dataset of 10gen's interns and their favorite movies.
Based upon a user's top films, Recommender extracts relations to other people who share
one or more films with that user, then uses a scoring algorithm to recommend new movies.
Algorithms like this are extremely common in the real world, with the most notable example
being Amazon.com's product recommendation system, which is said to net them multiple billions in revenue each year.

How MongoDB Fits In
===================
This application showcases MongoDB's flexibility and power. MongoDB is used heavily in the app, in ways detailed below:
	- MongoImport --jsonarray for user data allows easy import of JSON data.
	- Java driver is used to read a .dsv file into a collection.
	- Aggregation is used to choose candidate items for recommendation.
	- The QueryBuilder (Java driver) is used to retrieve data from Mongo.
	- Java driver natively used to modify documents currently stored in the DB.

Specifications
==============
The application is a Java webserver using Apache Tomcat v7.0. Skeleton CSS is used for styling.
