README: How We Use Data in this Application
========================================================
Built by David Khavari, 10gen: The MongoDB Company, 2013

Overview
========
Two types of data are used in this project:
	1. A .json which contains many different json documents.
	2. A .dsv (delimiter separated values) file containing the items,
	which are whatever type of product we are recommending over. These
	could be retail items or many other things, but in the example file,
	they are movies.

Sample Data Details
===================
The small_user.set.json is data collected from interns at 10gen. Each user
document contains a list of that user's favorite movies.

The small_movie_dataset.dsv is a bar-separated file (eg:'|'), containing on
each line a movie ID number, the bar, then the movie's name.

Users can build or import their own data, as long as they use .json for the users,
and a bar-separated file for the products.

Importing Data
==============
The .json file is manually imported using the following command:
mongoimport --db database --collection collection filename --jsonArray

The .dsv file is automatically imported by the Java program.

We essentially designed Recommender using two different ways of importing
data just to demonstrate how both of these work.
