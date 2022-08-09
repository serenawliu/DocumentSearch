Document Search

The goal of this exercise is to create a working program to search a set
of documents for the given search term or phrase (single token), and
return results in order of relevance.
Relevancy is defined as number of times the exact term or phrase
appears in the document.
Create three methods for searching the documents:
- Simple string matching
- Text search using regular expressions
- Preprocess the content and then search the index
Prompt the user to enter a search term and search method, execute
the search, and return results. 

The following format is followed:

Enter the search term: <user enters search term>
Search Method: 1) String Match 2) Regular Expression 3) Indexed

Search results:

File2.txt – X matches
File1.txt - X matches
File3.txt – X matches

_____________

To Install and Run this Application: 

Preconditions:
- Java SE Runtime Environment 18.3 or higher is installed (build 10.0.2)
- IntelliJ IDE is installed.

Running the application:
- Clone the DocumentSearch project, or download and unzip the project file. Open this project in the IntelliJ IDE.

- Run SearchMain to run the search engine. Input a search term, then the desired search method.

- Run PerformanceTest to view execution times for 2 million random search terms for each search method.

- Run SearchEngineTest to run a collection of unit tests.

NOTE:
Discussions on performance and scaling this application can be found in file DiscussionQuestions.txt.

A short description of the index search can be found under IndexSearchDescription.