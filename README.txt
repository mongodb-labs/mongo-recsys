Recommender - A Data Recommendation Engine Built with MongoDB
=============================================================
Built by David Khavari, 10gen: The MongoDB Company, 2013

Purpose
=======
This application seeks to explore the capabilities of MongoDB's document-oriented storage in an applied, real-world context.

Description
===========
Recommender is a simple item recommendation system. The included example is a small dataset of 10gen's interns and their favorite movies.*

Based upon a user's top films, Recommender extracts relations to other people who share one or more films with that user, then uses a scoring algorithm to recommend new movies.

Algorithms like this are extremely common in the real world, with the most notable example being Amazon.com's product recommendation system, which is said to net them billions in revenue each year.

How MongoDB Fits In
===================
This application showcases MongoDB's flexibility and power. MongoDB is used heavily in the app, in ways detailed below:
	- MongoImport is used to import all data.
	- Aggregation is used to choose candidate items for recommendation.
	- The QueryBuilder (Java driver) is used to retrieve data from Mongo.
	- Java driver natively used to modify documents currently stored in the DB.
	- MongoDB's text search feature is used to query items by "title" field.

Specifications
==============
The application is a Java webserver using Apache Tomcat v7.0. Skeleton CSS is used for styling.

How to Get It Up and Running
============================
There are several quick things that need to be done before being able to run and try out this project, which will be discussed here. However, it is key to mention first that I would recommend running this project in Eclipse, and of course, MongoDB must be installed on your machine.

Here are the steps to getting this program up and running:

1. Download it from this repository either using "git pull" or downloading the file as a .zip.

2. Import this project into Eclipse, and ensure that you have Apache Tomcat v7.0 installed and configured.

3. Run "mongod --setParameter textSearchEnabled=true". Without running this command and having mongod running in the background, this program will not work as desired.

4. Use mongoimport to import documents from the data directory in the project. The default database name is "daviddb", and the default user and item collections are "users" and "movies" (no quotes when actually creating these). There is more information on how to do this in the README_data.txt file contained in the data directory.

5. Run the mongo shell and execute the following command: "db.collection.ensureIndex('title', 'text')" if the field you want to text search is named 'title'. Putting 'text' as the string literal value is simply the way text search has been formatted to accept parameters.

6. Once you have data imported and correctly named, you should be all set to run the program!

Using Your Own Data and Making Changes
======================================
It is fairly easy to use your own data with this application. There is a file, src/classes/Constants.java, which is statically imported to the servlets. The names of constants in this file can be changed as necessary, but you have to make sure that your data, as well as the fields you pass to mongoimport, reflect these changes.

There are some fields used for passing information between servlets and jsps, and while you can change those, take care to make the changes both in the servlet and in the jsp, otherwise the data will not display correctly.

Footnotes
=========
*The small_user_dataset (intern movie preferences) aligns with the small_movie_dataset. A larger dataset is also provided, but it does not match with small_user_dataset.
