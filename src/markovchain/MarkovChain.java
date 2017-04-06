/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package markovchain;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;
import java.util.ArrayList;


import java.io.*;
import java.util.*;

/**
 *
 * @author ducnguyen
 */
public class MarkovChain {
    
    /**
     * 
     */
    // Hashmap
    public static HashMap<String, Vector<String>> hmap = new HashMap<String, Vector<String>>();
    static Random rnd = new Random();
    
    public static void main(String[] args) throws FileNotFoundException, IOException {        
        String file_path = new java.io.File("").getAbsolutePath() + "/src/markovchain/text/";
        String file_name = file_path + "test_1.txt";
        String line;
        
        hmap.put("_start", new Vector<String>());
        hmap.put("_end", new Vector<String>());
                                
        FileReader fr = new FileReader(file_name);
        BufferedReader br = new BufferedReader(fr); 
                
        while((line = br.readLine()) != null) {
//            String[] words = line.trim().split(" ");
            
            markovChain(line);
        }
    }
        
    /*
     * Add words
     */
    public static void markovChain(String line) {
        // put each word into an array
        String[] words = line.split(" ");

        // Loop through each word, check if it's already added
        // if its added, then get the suffix vector and add the word
        // if it hasn't been added then add the word to the list
        // if its the first or last word then select the _start / _end key

        for (int i=0; i<words.length; i++) {
            // Add the start and end words to their own
            if (i == 0) {
                Vector<String> startWords = hmap.get("_start");
                startWords.add(words[i]);

                Vector<String> suffix = hmap.get(words[i]);
                
                if (suffix == null) {
                    suffix = new Vector<String>();
                    suffix.add(words[i+1]);
                    hmap.put(words[i], suffix);
                }
            } else if (i == words.length-1) {
                Vector<String> endWords = hmap.get("_end");
                endWords.add(words[i]);
            } else {	
                Vector<String> suffix = hmap.get(words[i]);
                if (suffix == null) {
                    suffix = new Vector<String>();
                    suffix.add(words[i+1]);
                    hmap.put(words[i], suffix);
                } else {
                    suffix.add(words[i+1]);
                    hmap.put(words[i], suffix);
                }
            }
        }		
        textGenerator();
    }


    /*
     * Generate a markov phrase
     */
    public static void textGenerator() {

        // Vector to hold the phrase
        Vector<String> new_sentence = new Vector<String>();

        // String for the next word
        String next_word;

        // Select the first word
        Vector<String> start_words = hmap.get("_start");
        
        int startWordsLen = start_words.size();
        next_word = start_words.get(rnd.nextInt(startWordsLen));
        new_sentence.add(next_word);

        // Keep looping through the words until we've reached the end
        while (nextWord.charAt(nextWord.length()-1) != '.') {
            Vector<String> wordSelection = hmap.get(nextWord);
            
            int wordSelectionLen = wordSelection.size();
            nextWord = wordSelection.get(rnd.nextInt(wordSelectionLen));
            newPhrase.add(nextWord);
        }

        System.out.println("New phrase: " + newPhrase.toString());	
    }
    
}
