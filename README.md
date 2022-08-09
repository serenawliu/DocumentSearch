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

Run a performance test that does 2M searches with random search
terms, and measures execution time. Which approach is fastest? Why?

Search results:
Run time for string search in milliseconds:  37869 ms
Run time for regex search in milliseconds:  37692 ms
Run time for preprocess search in milliseconds:  1221 ms

Index Search has the fastest execution time. Since the files are already processed by the time index search is called, 
the execution time is composed of accesses to the map of stored word indices and word frequencies. 
For both string and regular expression matching, each word in the text files
would need to be processed at the time of the search - operations that are O(n) time, where n = the number of characters in the text file.

Provide some thoughts on what you would do on the software or
hardware side to make this program scale to handle massive content
and/or very large request volume (5000 requests/second or more).

On the software side, there are many additions that could be made: building a cache of commonly searched terms and their frequencies,
using the MapReduce programming model,
using ElasticSearch which has a more robust indexing and data ingestion process, or creating an inverted index - a map of 
unique words and a list of documents they can be found in.

On the hardware side, distributing the search process across multiple 
threads or multiple servers (including load balancing)