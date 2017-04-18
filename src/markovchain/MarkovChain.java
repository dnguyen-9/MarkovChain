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
import java.text.DecimalFormat;
import java.util.*;

/**
 *
 * @author ducnguyen
 */
public class MarkovChain {
//    public int order = 1;
    
    // Hashmap for first-order markov chain
    public static HashMap<String, Vector<String>> hmap = new HashMap<String, Vector<String>>();
//    public static HashMap<Vector<String>, Vector<String>> hmap = new HashMap<Vector<String>, Vector<String>>();

    
    // Random value
    static Random rnd = new Random();
    
    public static void main(String[] args) throws FileNotFoundException, IOException {  
        // Get file path
        String filePath = new java.io.File("").getAbsolutePath() + "/src/markovchain/text/";
        
        // Get file name
        String fileName = filePath + "test_1.txt";
        
        String line;
       
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr); 

        while((line = br.readLine()) != null) {
            markovChain(line);
        }
    }   
    
//    public static void addWordToMarkovChain(String word, Double p, String author) {
//        Vector<String> chainArray = hmap.get("chain");
//        chainArray.add(word);
//
//        Vector matrixArray = hmap.get("matrix");
//        matrixArray.add(new Double(0.5));
//
//        Vector<String> authorArray = hmap.get("author");
//        authorArray.add(author);
//    }
    
    /*
     * Add words
     */
    public static void markovChain(String line) {
        // put each word into an array
        String[] words = line.trim().split("[\\s\\.]+");
//        String[] words = line.trim().split("\\s");

        Vector<String> first_arr = new Vector<String>();

//        int order = 1;
//        int k = 0;
        
        // loop        
        for (int i = 0; i < words.length; i++) {
//            String word = words[i];
            
//            System.out.println("i = " + i + " word[i] = " + words[i]);
//            System.out.println("before: " + hmap);
            
//            if(words[i].contains(".")) {
//                String newWord = words[i].replace(".", "");
//                String dot = ".";
//            }
//            

            
            
            if(i == 0) {
                hmap.put(words[i], new Vector<String>());   
//                while(k < order) {
//                    first_arr.add(words[i+k]);
//                    k++;
//                }
//
//                hmap.put(first_arr, new Vector<String>());   
            } else {
                if (hmap.containsKey(words[i])) {                
//                    System.out.println("~~ true");

                    Vector<String> arr = hmap.get(words[i-1]);

                    if(arr == null) {
//                        System.out.println("words[i-1] = " + words[i-1]);
//                        System.out.println("words[i] = " + words[i]);
//                        if((i+1) < words.length) {
//                            System.out.println("words[i+1] = " + words[i+1]);
//                        }
                        
                        arr = new Vector<String>();
                        arr.add(words[i]);
    
                        hmap.put(words[i-1], arr);
                    }   
                    
                    System.out.println(hmap);
                    
                } else {
//                    System.out.println("false");
//                    System.out.println("words[i-1] = " + words[i-1]);
//                    System.out.println("words[i] = " + words[i]);
                    
                    Vector<String> prevArr = hmap.get(words[i-1]);                    
                    Vector<String> currentArr = hmap.get(words[i]);

                    if((i+1) < words.length) {
//                        System.out.println("words[i+1] = " + words[i+1]);
                        Vector<String> nextArr = hmap.get(words[i+1]);

//                        System.out.println("prevArr = " + prevArr);
//                        System.out.println("currentArr = " + currentArr);
//                        System.out.println("nextArr = " + nextArr);

                        if(currentArr == null) {
                            if(nextArr == null) {
                                currentArr = new Vector<String>();
                                currentArr.add(words[i]);

                                hmap.put(words[i-1], currentArr);
                            } else {
                                if(prevArr == null) {
                                    currentArr = new Vector<String>();
                                    currentArr.add(words[i]);

                                    hmap.put(words[i-1], currentArr);
                                } else {
                                    prevArr.add(words[i]);

                                    hmap.put(words[i], currentArr);
                                }
                            }
                        }   
                    }
                }        
            }
        }
        
//        System.out.println(hmap + "\n");

        generatetTransitionMatrix(hmap);
    }

    
    public static void generatetTransitionMatrix(HashMap<String, Vector<String>> hmap) {
        // Define dictionary to store all the words to be cross-checked
        Vector<String> dictionary = new Vector<String>();

        // initilize number of cols
        int totalCol = hmap.size();
                
        // Add key to dictionary
        for (String key : hmap.keySet()) {            
            dictionary.add(key);
        }
                
        // initilize number of rows
        int totalRow = dictionary.size();
        
        // initialize transition matrix
        float[][] m = new float[totalRow][totalCol];

        // first row
        int row = 0;
        
        // Loop through array to create the transition matrix
        for (String key : hmap.keySet()) {
            Vector<String> data = hmap.get(key);
            
            for(int i = 0; i < data.size(); i++) {
                String word = data.get(i);
                
                for(int col = 0; col < totalCol; col++) { 
                    if(word.equals(dictionary.get(col))) {
//                        System.out.println("~~row: " + row + " ~~col: " + col);
                        
                        // Total words in data array
                        float size = data.size();
                        
                        // Calculate probability
                        float p = 1/size;

//                        System.out.println("p = " + p);
//                        System.out.println("~~~");

                        m[row][col] = (float) (Math.round(p * 100.0) / 100.0);
                    }
                }                
            }
            
            row++;
            
//            for(int row = 0; 
////            for (int i = 0; i < row; i++) {
////                char firstChar = key.charAt(0);
////                
////                for (int j = 0; j < col; i++) {
////                
////                    if(Character.isUpperCase(firstChar)){
////                        m[0][0] = 1/hmap.get(key).size();
////                    }
////                }
////            }
        }
        
        textGenerator(m);
    }

    /*
     * Print out transition matrix
     */
    public static void printMatrix(float[][] m) {        
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[i].length; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    /*
     * Generate a markov text
     */
    public static void textGenerator(float[][] m) {
        printMatrix(m);
        
        // Vector to hold the phrase
        Vector<String> newSentence = new Vector<String>();
        
        String nextWord;
        
//        for (String key : hmap.keySet()) {
//            Vector<String> data = hmap.get(key);
            
//            int startWordsLen = data.size();
            
//            System.out.print(startWordsLen + "\n");
//            
//            System.out.print(rnd.nextInt(startWordsLen)+ "\n");
            
//            nextWord = data.get(rnd.nextInt(startWordsLen));
            
//            System.out.print(nextWord);
            
//            newSentence.add(nextWord);
            
//            for(int i = 0; i < data.size(); i++) {
//                String word = data.get(i);
//            }
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j < m[i].length; j++) {
                    
                    float p = m[i][j];
                    
                    if(p != 0.00) {
                        System.out.print(p + " ");
                    }                    
                }
            }
//        }
        
//        System.out.println("New phrase: " + newSentence);	
        
        

        	
    }

//    public static void textGenerator() {
//
//        // Vector to hold the phrase
//        Vector<String> newPhrase = new Vector<String>();
//
//        // String for the next word
//        String nextWord;
//
//        // Select the first word
//        Vector<String> start_words = hmap.get("_start");
//        
//        int startWordsLen = start_words.size();
//        nextWord = start_words.get(rnd.nextInt(startWordsLen));
//        newPhrase.add(nextWord);
//
//        // Keep looping through the words until we've reached the end
//        while (nextWord.charAt(nextWord.length()-1) != '.') {
//            Vector<String> wordSelection = hmap.get(nextWord);
//            
//            int wordSelectionLen = wordSelection.size();
//            nextWord = wordSelection.get(rnd.nextInt(wordSelectionLen));
//            newPhrase.add(nextWord);
//        }
//
//        System.out.println("New phrase: " + newPhrase.toString());	
//    }
    
    public static int countWords(String filename) throws IOException {
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr); 
        String line;
        
        int sum = 0;
        
        while((line = br.readLine()) != null) {
            String[] words = line.trim().split(" ");

            for (int i=0; i < words.length; i++) {
                ++sum;
            }            
        }
        
        sum = sum - 1;
        
        return sum;
    }
    
    public static int countLines(String filename) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filename));
        try {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        } finally {
            is.close();
        }
    }

    public MarkovChain() {
        this.order = 0;
    }
}
