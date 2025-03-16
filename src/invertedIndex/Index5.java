/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */




 /*
  summary of methods in this file:

Constructor (Index5): Initializes the sources map and the index map.

setN Method: Sets the total number of documents (N).

printPostingList Method: Prints the document IDs in a posting list.

printDictionary Method: Prints the entire dictionary (inverted index).

buildIndex Method: Builds the inverted index from a list of files.

indexOneLine Method: Indexes a single line of text from a file.

stopWord Method: Checks if a word is a stop word.

stemWord Method: Stems a word (currently just returns the word as-is).

intersect Method: Intersects two posting lists.

find_24_01 Method: Finds documents that contain all the words in the given phrase.

sort Method: Sorts an array of words using bubble sort.

store Method: Stores the index and source records to a file.

storageFileExists Method: Checks if a storage file exists.

createStore Method: Creates a new storage file.

load Method: Loads the index and source records from a storage file.

These comments provide a clear explanation of each 
  */
package invertedIndex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Math.log10;
import static java.lang.Math.sqrt;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.io.PrintWriter;

/**
 *
 * @author ehab
 */
public class Index5 {

    //--------------------------------------------
    int N = 0;
    public Map<Integer, SourceRecord> sources;  // store the doc_id and the file name.

    public HashMap<String, DictEntry> index; // THe inverted index
    //--------------------------------------------

    /**
     * Constructor for Index5 class.
     * Initializes the sources map and the index map.
     */
    public Index5() {
        sources = new HashMap<Integer, SourceRecord>();
        index = new HashMap<String, DictEntry>();
    }

    /**
     * Sets the total number of documents (N).
     * @param n The total number of documents.
     */
    public void setN(int n) {
        N = n;
    }

    //---------------------------------------------
    /**
     * Prints the posting list for a given Posting object.
     * @param p The Posting object to print.
     */
    public void printPostingList(Posting p) {
        System.out.print("[");
        while (p != null) {
            System.out.print("" + p.docId);
            p = p.next;
            if (p != null) {
                System.out.print(",");
            }
        }
        System.out.println("]");
    }

    //---------------------------------------------
    /**
     * Prints the entire dictionary (inverted index).
     */
    public void printDictionary() {
        Iterator it = index.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            DictEntry dd = (DictEntry) pair.getValue();
            System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "]       =--> ");
            printPostingList(dd.pList);
        }
        System.out.println("------------------------------------------------------");
        System.out.println("*** Number of terms = " + index.size());
    }
    
   
    //-----------------------------------------------
    /**
     * Builds the inverted index from a list of files.
     * @param files An array of file names to index.
     */
    public void buildIndex(String[] files) {  // from disk not from the internet
        int fid = 0;
        for (String fileName : files) {
            try (BufferedReader file = new BufferedReader(new FileReader(fileName))) {
                if (!sources.containsKey(fileName)) {
                    sources.put(fid, new SourceRecord(fid, fileName, fileName, "notext"));
                }
                String ln;
                int flen = 0;
                while ((ln = file.readLine()) != null) {
                    /// -2- **** complete here ****
                    ///**** hint   flen +=  ________________(ln, fid);
                     flen += indexOneLine(ln, fid);
                }
                sources.get(fid).length = flen;

            } catch (IOException e) {
                System.out.println("File " + fileName + " not found. Skip it");
            }
            fid++;
        }
        //   printDictionary();
    }

    //----------------------------------------------------------------------------  
    /**
     * Indexes a single line of text from a file.
     * @param ln The line of text to index.
     * @param fid The file ID.
     * @return The number of words indexed in the line.
     */
    public int indexOneLine(String ln, int fid) {
        int flen = 0;

        String[] words = ln.split("\\W+");
      //   String[] words = ln.replaceAll("(?:[^a-zA-Z0-9 -]|(?<=\\w)-(?!\\S))", " ").toLowerCase().split("\\s+");
        flen += words.length;
        for (String word : words) {
            word = word.toLowerCase();
            if (stopWord(word)) {
                continue;
            }
            word = stemWord(word);
            // check to see if the word is not in the dictionary
            // if not add it
            if (!index.containsKey(word)) {
                index.put(word, new DictEntry());
            }
            // add document id to the posting list
            if (!index.get(word).postingListContains(fid)) {
                index.get(word).doc_freq += 1; //set doc freq to the number of doc that contain the term 
                if (index.get(word).pList == null) {
                    index.get(word).pList = new Posting(fid);
                    index.get(word).last = index.get(word).pList;
                } else {
                    index.get(word).last.next = new Posting(fid);
                    index.get(word).last = index.get(word).last.next;
                }
            } else {
                index.get(word).last.dtf += 1;
            }
            //set the term_fteq in the collection
            index.get(word).term_freq += 1;
            if (word.equalsIgnoreCase("lattice")) {

                System.out.println("  <<" + index.get(word).getPosting(1) + ">> " + ln);
            }

        }
        return flen;
    }

//----------------------------------------------------------------------------  
    /**
     * Checks if a word is a stop word.
     * @param word The word to check.
     * @return True if the word is a stop word, false otherwise.
     */
    boolean stopWord(String word) {
        if (word.equals("the") || word.equals("to") || word.equals("be") || word.equals("for") || word.equals("from") || word.equals("in")
                || word.equals("a") || word.equals("into") || word.equals("by") || word.equals("or") || word.equals("and") || word.equals("that")) {
            return true;
        }
        if (word.length() < 2) {
            return true;
        }
        return false;

    }
//----------------------------------------------------------------------------  

    /**
     * Stems a word (currently just returns the word as-is).
     * @param word The word to stem.
     * @return The stemmed word.
     */
    String stemWord(String word) { //skip for now
        return word;
//        Stemmer s = new Stemmer();
//        s.addString(word);
//        s.stem();
//        return s.toString();
    }

    //----------------------------------------------------------------------------
    /**
     * This method finds the intersection of two sorted linked lists of postings.
     * A posting represents a document ID (`docId`), and the intersection consists
     * of document IDs present in both lists.

     * Intersects two posting lists.
     * @param pL1 The first sorted linked list of postings.
     * @param pL2 The second sorted linked list of postings.
     * @return A new linked list containing only the common document IDs.
     */
    Posting intersect(Posting pL1, Posting pL2) {

        Posting answer = null; // Head of the intersection list
        Posting last = null;   // Pointer to track the last node in the intersection list

        // Traverse both posting lists
        while (pL1 != null && pL2 != null) {
            if (pL1.docId == pL2.docId) { // Found a common document ID
                Posting newPosting = new Posting(pL1.docId); // Create a new node
                if (answer == null) { // If this is the first common node
                    answer = last = newPosting; // Set it as the head
                } else {
                    last.next = newPosting; // Append to the result list
                    last = last.next;       // Move the last pointer
                }
                // Move both pointers forward since we found a match
                pL1 = pL1.next;
                pL2 = pL2.next;
            }
            else if (pL1.docId > pL2.docId) {
                // Move pL2 forward since its docId is smaller
                pL2 = pL2.next;
            }
            else {
                // Move pL1 forward since its docId is smaller
                pL1 = pL1.next;
            }
        }

        return answer; // Return the intersection list
    }


    /**
     * Finds documents that contain all the words in the given phrase.
     * @param phrase The phrase to search for.
     * @return A string containing the document IDs and titles that match the phrase.
     */
    public String find_24_01(String phrase) { // any number of terms non-optimized search
        String result = "";
        String[] words = phrase.split("\\W+");
        int len = words.length;

        // Fix this: If a word is not in the hash table, it will crash...
        if (!index.containsKey(words[0].toLowerCase())) {
            return "No results found.\n";
        }

        Posting posting = index.get(words[0].toLowerCase()).pList;
        int i = 1;

        while (i < len) {
            if (!index.containsKey(words[i].toLowerCase())) {
                return "No results found.\n";
            }
            posting = intersect(posting, index.get(words[i].toLowerCase()).pList);
            if (posting == null) {
                return "No results found.\n";
            }
            i++;
        }

        while (posting != null) {
            //System.out.println("\t" + sources.get(num));
            result += "\tfound in document --> " + posting.docId + " - " ;
            result += "path is --> " + sources.get(posting.docId).title + " - " ;
            result += "document length --> " + sources.get(posting.docId).length + "\n";
            posting = posting.next;
        }

        return result.isEmpty() ? "No results found.\n" : result;
    }

    //---------------------------------
    /**
     * Sorts an array of words using bubble sort.
     * @param words The array of words to sort.
     * @return The sorted array of words.
     */
    String[] sort(String[] words) {  //bubble sort
        boolean sorted = false;
        String sTmp;
        //-------------------------------------------------------
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < words.length - 1; i++) {
                int compare = words[i].compareTo(words[i + 1]);
                if (compare > 0) {
                    sTmp = words[i];
                    words[i] = words[i + 1];
                    words[i + 1] = sTmp;
                    sorted = false;
                }
            }
        }
        return words;
    }

     //---------------------------------

    /**
     * Stores the index and source records to a file.
     * @param storageName The name of the file to store the data in.
     */
    public void store(String storageName) {
        try {
            String pathToStorage = System.getProperty("user.dir")+storageName;
            Writer wr = new FileWriter(pathToStorage);
            for (Map.Entry<Integer, SourceRecord> entry : sources.entrySet()) {
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue().URL + ", Value = " + entry.getValue().title + ", Value = " + entry.getValue().text);
                wr.write(entry.getKey().toString() + ",");
                wr.write(entry.getValue().URL.toString() + ",");
                wr.write(entry.getValue().title.replace(',', '~') + ",");
                wr.write(entry.getValue().length + ","); //String formattedDouble = String.format("%.2f", fee );
                wr.write(String.format("%4.4f", entry.getValue().norm) + ",");
                wr.write(entry.getValue().text.toString().replace(',', '~') + "\n");
            }
            wr.write("section2" + "\n");

            Iterator it = index.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                DictEntry dd = (DictEntry) pair.getValue();
                //  System.out.print("** [" + pair.getKey() + "," + dd.doc_freq + "] <" + dd.term_freq + "> =--> ");
                wr.write(pair.getKey().toString() + "," + dd.doc_freq + "," + dd.term_freq + ";");
                Posting p = dd.pList;
                while (p != null) {
                    //    System.out.print( p.docId + "," + p.dtf + ":");
                    wr.write(p.docId + "," + p.dtf + ":");
                    p = p.next;
                }
                wr.write("\n");
            }
            wr.write("end" + "\n");
            wr.close();
            System.out.println("=============EBD STORE=============");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//=========================================    
    /**
     * Checks if a storage file exists.
     * @param storageName The name of the storage file.
     * @return True if the file exists, false otherwise.
     */
    public boolean storageFileExists(String storageName){
        java.io.File f = new java.io.File("/home/ehab/tmp11/rl/"+storageName);
        if (f.exists() && !f.isDirectory())
            return true;
        return false;
            
    }
//----------------------------------------------------    
    /**
     * Creates a new storage file.
     * @param storageName The name of the storage file to create.
     */
    public void createStore(String storageName) {
        try {
            String pathToStorage = "/home/ehab/tmp11/"+storageName;
            Writer wr = new FileWriter(pathToStorage);
            wr.write("end" + "\n");
            wr.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//----------------------------------------------------      
     /**
     * Loads the index and source records from a storage file.
     * @param storageName The name of the storage file.
     * @return The loaded index.
     */
    public HashMap<String, DictEntry> load(String storageName) {
        try {
            String pathToStorage = "/home/ehab/tmp11/rl/"+storageName;         
            sources = new HashMap<Integer, SourceRecord>();
            index = new HashMap<String, DictEntry>();
            BufferedReader file = new BufferedReader(new FileReader(pathToStorage));
            String ln = "";
            int flen = 0;
            while ((ln = file.readLine()) != null) {
                if (ln.equalsIgnoreCase("section2")) {
                    break;
                }
                String[] ss = ln.split(",");
                int fid = Integer.parseInt(ss[0]);
                try {
                    System.out.println("**>>" + fid + " " + ss[1] + " " + ss[2].replace('~', ',') + " " + ss[3] + " [" + ss[4] + "]   " + ss[5].replace('~', ','));

                    SourceRecord sr = new SourceRecord(fid, ss[1], ss[2].replace('~', ','), Integer.parseInt(ss[3]), Double.parseDouble(ss[4]), ss[5].replace('~', ','));
                    //   System.out.println("**>>"+fid+" "+ ss[1]+" "+ ss[2]+" "+ ss[3]+" ["+ Double.parseDouble(ss[4])+ "]  \n"+ ss[5]);
                    sources.put(fid, sr);
                } catch (Exception e) {

                    System.out.println(fid + "  ERROR  " + e.getMessage());
                    e.printStackTrace();
                }
            }
            while ((ln = file.readLine()) != null) {
                //     System.out.println(ln);
                if (ln.equalsIgnoreCase("end")) {
                    break;
                }
                String[] ss1 = ln.split(";");
                String[] ss1a = ss1[0].split(",");
                String[] ss1b = ss1[1].split(":");
                index.put(ss1a[0], new DictEntry(Integer.parseInt(ss1a[1]), Integer.parseInt(ss1a[2])));
                String[] ss1bx;   //posting
                for (int i = 0; i < ss1b.length; i++) {
                    ss1bx = ss1b[i].split(",");
                    if (index.get(ss1a[0]).pList == null) {
                        index.get(ss1a[0]).pList = new Posting(Integer.parseInt(ss1bx[0]), Integer.parseInt(ss1bx[1]));
                        index.get(ss1a[0]).last = index.get(ss1a[0]).pList;
                    } else {
                        index.get(ss1a[0]).last.next = new Posting(Integer.parseInt(ss1bx[0]), Integer.parseInt(ss1bx[1]));
                        index.get(ss1a[0]).last = index.get(ss1a[0]).last.next;
                    }
                }
            }
            System.out.println("============= END LOAD =============");
            //    printDictionary();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return index;
    }
}

//=====================================================================