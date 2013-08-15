Recommender README

Welcome to Recommender!

APPLICATION WEBSITE

This is a fully-deployed example of Recommender.

If you're looking to use the application with no installation required,
simply visit the following link:

www.mongodbrecommender.com

QUICK INSTALLATION

1. Git pull the application from this page.

2. Install Apache Tomcat 7, or continue if you already have: http://tomcat.apache.org/download-70.cgi

3. Compile the app into a .war file, either using an IDE or the command line, and put Recommender-Repair.war into Tomcat's webapps directory, which you can find here: tomcat/TOMCAT_VERSION/libexec/webapps

4. Install the latest version of MongoDB or continue if you already have: http://www.mongodb.org/downloads

RUNNING THE PROGRAM

1. Run the following command in terminal: mongod --setParameter textSearchEnabled = true
	
	Leave mongod running. It's required for Recommender to interact with MongoDB.

2. Open up a new tab in your terminal and import the sample data using the following two commands:

	mongoimport -d daviddb -c movies --type csv --fields movie_id,title,genre,img,plot movie_dataset.csv
	mongoimport -d daviddb -c users small_user_dataset.json --jsonArray

3. Open up an instance of the mongo shell.

4. Run the following commands in the mongo shell, to enable text search:
	
	use daviddb
	db.movies.ensureIndex("title" : "text")
	
5. Start up Tomcat by visiting the tomcat/TOMCAT_VERSION/libexec/bin and running the startup.sh script:

	./startup.sh

6. Visit login page of Recommender in a browser (which should by default be at the following URL, given that you have named your .war file Recommender: localhost:8080/Recommender/login.jsp), and enjoy the site!

BUILDING AND MODIFYING THE SOURCE CODE

1. Download the entire repository from Recommender's GitHub page: git pull https://github.com/10gen-interns/mongo-recsys

2. Edit the files as necessary either using a text editor or IDE (I would recommend Eclipse EE).

3. Build the project into a .war file either using terminal or using an IDE such as Eclipse, then run it on Tomcat with the instructions above.

CONTACT

David Khavari: david.khavari@10gen.com
