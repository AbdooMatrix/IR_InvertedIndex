# Inverted Index Project

This project implements an inverted index for a collection of documents. The inverted index allows for efficient full-text searches, where you can quickly find all documents that contain a given word or phrase.

## Project Structure

- `Index5.java`: This is the main class that builds and manages the inverted index. It includes methods for building the index, printing the index, and searching the index.
- `DictEntry.java`: This class represents an entry in the dictionary (inverted index). It includes the document frequency, term frequency, and a posting list.
- `Posting.java`: This class represents a posting in the posting list. It includes the document ID and term frequency.
- `SourceRecord.java`: This class represents a source record, which includes the document ID, URL, title, text, norm, and length.
- `Stemmer.java`: This class implements the Porter Stemming Algorithm, which is used to reduce words to their root form.
- `Test.java`: This is the main class that tests the functionality of the inverted index. It builds the index from a collection of documents, prints the index, and allows for searching the index.

## How to Use

1. **Set Up the Collection Directory**: In the `Test.java` file, change the `files` variable to point to your collection directory. For example:
    ```java
    String files = "C:\\path\\to\\your\\collection\\";
    ```

2. **Build the Index**: Run the `Test` class to build the index from the documents in the collection directory. The index will be stored in a file named `index`.

3. **Search the Index**: After building the index, you can search for phrases using the console input. The program will print the document IDs and titles that match the search phrase.

## Methods

### Index5.java

- `Index5()`: Constructor that initializes the sources map and the index map.
- `setN(int n)`: Sets the total number of documents (N).
- `printPostingList(Posting p)`: Prints the posting list for a given Posting object.
- `printDictionary()`: Prints the entire dictionary (inverted index).
- `buildIndex(String[] files)`: Builds the inverted index from a list of files.
- `indexOneLine(String ln, int fid)`: Indexes a single line of text from a file.
- `boolean stopWord(String word)`: Checks if a word is a stop word.
- `String stemWord(String word)`: Stems a word (currently just returns the word as-is).
- `Posting intersect(Posting pL1, Posting pL2)`: Intersects two posting lists.
- `public String find_24_01(String phrase)`: Finds documents that contain all the words in the given phrase.
- `String[] sort(String[] words)`: Sorts an array of words using bubble sort.
- `public void store(String storageName)`: Stores the index and source records to a file.
- `public boolean storageFileExists(String storageName)`: Checks if a storage file exists.
- `public void createStore(String storageName)`: Creates a new storage file.
- `public HashMap<String, DictEntry> load(String storageName)`: Loads the index and source records from a storage file.

### DictEntry.java

- `boolean postingListContains(int i)`: Checks if the posting list contains a given document ID.
- `int getPosting(int i)`: Gets the term frequency for a given document ID.
- `void addPosting(int i)`: Adds a posting to the posting list.
- `DictEntry()`: Constructor that initializes the posting list.
- `DictEntry(int df, int tf)`: Constructor that initializes the document frequency and term frequency.

### Posting.java

- `Posting(int id, int t)`: Constructor that initializes the document ID and term frequency.
- `Posting(int id)`: Constructor that initializes the document ID.

### SourceRecord.java

- `SourceRecord(int f, String u, String tt, int ln, Double n, String tx)`: Constructor that initializes the source record with all fields.
- `SourceRecord(int f, String u, String tt, String tx)`: Constructor that initializes the source record with default norm and length.

### Stemmer.java

- `void addString(String s)`: Adds a string to the stemmer buffer.
- `void add(char ch)`: Adds a character to the stemmer buffer.
- `void add(char[] w, int wLen)`: Adds an array of characters to the stemmer buffer.
- `String toString()`: Returns the stemmed word as a string.
- `int getResultLength()`: Returns the length of the stemmed word.
- `char[] getResultBuffer()`: Returns the buffer containing the stemmed word.
- `void stem()`: Stems the word in the buffer.
