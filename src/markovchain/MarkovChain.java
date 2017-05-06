/*
 * N-th order Markov Model
 */
package markovchain;

import java.util.Random;
import java.util.LinkedList;

import java.io.*;
import java.util.*;

/**
 * @author ducnguyen
 * @version 1.0
 */
public class MarkovChain {
    public static int order = 3;
    
    public static boolean debug_flag = false;

    // HashMap for freguency
    public static LinkedHashMap<LinkedList<String>, Integer> frequencyMap = new LinkedHashMap<>();        

    // Random value
    static Random rnd = new Random();

    /**
     * 
     * @return
     * @throws FileNotFoundException 
     */
    public static BufferedReader getBufferedReader() throws FileNotFoundException {
        // Get file path
        String filePath = new java.io.File("").getAbsolutePath() + "/src/markovchain/text/";

        // Get file name
        String fileName = filePath + "test.txt";
//        String fileName = filePath + "test_2.txt";
//        String fileName = filePath + "test_3.txt";

        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        
        return br;
    }
    
    /**
     * Generate Markov Model
     * 
     * @param line 
     * @return  
     */
    public static LinkedList<String> markovChain(String line) {
        // Define a LinkedHashMap 
        LinkedHashMap<LinkedList<String>, LinkedList<String>> hmap = new LinkedHashMap<>();  
        
        // Define a LinkedHashMap
//        LinkedHashMap<LinkedList<String>, LinkedList<String>> fmap = new LinkedHashMap<>();  
        
        // Define final Markov Chain
        LinkedList<String> finalChain;

        // Generate Markov Data
        LinkedList dataList = getDataList(line);
        
        // Generate Markov Frequency Map
        getFrequencyMap(dataList);
                
        for (int N = 0; N < dataList.size(); N++) {

            if(dataList.size() == 1) {
                break;
            }
            // Adding the first array 
            if (N == 0) {
                if (N < dataList.size()) {
                    if (order == 1) {
                        String currentKey = dataList.get(N).toString();
                        String nextKey = dataList.get(N + 1).toString();

                        LinkedList<String> currentKeyArray = new LinkedList<>();
                        LinkedList<String> nextValueArray = new LinkedList<>();

                        currentKeyArray.add(currentKey);
                        nextValueArray.add(nextKey);

                        hmap.put(currentKeyArray, nextValueArray);
                    } else {                        
                        LinkedList<String> currentKeyArray = getKeyArray(N, dataList);

                        int nextIndex = N + order;

                        String nextValue = dataList.get(nextIndex).toString();

                        LinkedList<String> nextValueArray = new LinkedList<>();

                        nextValueArray.add(nextValue);

                        hmap.put(currentKeyArray, nextValueArray);
                    }
                }
            } else {
                if (dataList.size() - N >= order) {
                    LinkedList<String> currentKeyArray = getKeyArray(N, dataList);

                    if (!hmap.containsKey(currentKeyArray)) {
                        LinkedList<String> currentValueArr = hmap.get(currentKeyArray);
                        
                        if (currentValueArr == null) {
                            if (dataList.size() - N == order) {
                                hmap.put(currentKeyArray, new LinkedList<>());
                            } else {
                                LinkedList<String> nextValueArr = new LinkedList<>();
                                
                                if((N + order) < dataList.size()) {                            
                                    nextValueArr.add(dataList.get(N + order).toString());
                            
                                    hmap.put(currentKeyArray, nextValueArr);
                                }
                            }
                        }
                    } else {
                        LinkedList<String> currentValueArr = hmap.get(currentKeyArray);
                        
                        if ((N + order) < dataList.size()) {
                            int index = N + order;
                                                        
                            String nextKey = dataList.get(index).toString();
                            
                            if (!currentValueArr.contains(nextKey)) {
                                currentValueArr.add(nextKey);
                            }
                        }
                    }
                }
            }
        }


        finalChain = textGenerator(hmap, dataList);
        
        return finalChain;
    }
    
    /**
     * Generate a list of Markov frequency map
     * 
     * @param list
     */
    public static void getFrequencyMap(LinkedList<String> list) {
        for (int N = 0; N < list.size(); N++) {            
            LinkedList<String> keyArray = new LinkedList<>();

            for (int k = 0; k < order; k++) {
                int index = N + k;
                
                if(N + order <= list.size()) {
                    String key = list.get(index);
                    keyArray.add(key);
                } else {
                    break;
                }
            }
            
            if(list.size() - N >= order) {
                if (!frequencyMap.containsKey(keyArray)) {    
                    frequencyMap.put(keyArray, 1);
                } else {
                    int word_count = frequencyMap.get(keyArray);
                    word_count++;

                    frequencyMap.put(keyArray, word_count);
                }
            } else {
                break;
            }
        }
        
        // Debugging
        if(debug_flag) {
            System.out.println("##\n# Markov Frequency Map (initial): \n#\n#\t" + frequencyMap + "\n##");
        }
    }    
    
    /**
     * Generate a list of Markov data
     * 
     * @param line
     * @return 
     */
    public static LinkedList<String> getDataList(String line) {
        // Split character from a word, and put each into a list
        String[] words = line.trim().split("");

        LinkedList dataList = new LinkedList();
        
        for (String word : words) {
            for (int k = 0; k < word.length(); k++) {
                char c = word.charAt(k);

                String currentCharToString = Character.toString(c);

                dataList.add(currentCharToString);
            }
        }
        
        // Debugging
        if(debug_flag) {
            System.out.println("#\n# Markov Data: \n#\n#\t" + dataList);
        }
        
        return dataList;
    }    
    
    /**
     * Generate a key array
     * 
     * @param N
     * @param list
     * @return 
     */
    public static LinkedList<String> getKeyArray(int N, LinkedList<String> list) {
        // Get array of character based on order
        LinkedList<String> array = new LinkedList<>();

        for (int k = 0; k < order; k++) {
            int index = N + k;
            
            String key = list.get(index);
            array.add(key);
        }
        
        return array;
    }
    
    /**
     * Generate next word
     * 
     * @param nextWordsArray
     * @return 
     */
    public static String getNextWord(LinkedList nextWordsArray) {         
        int size = nextWordsArray.size();

        int index = rnd.nextInt(size);

        String nextWord = nextWordsArray.get(index).toString();

        return nextWord;
    }
    
    /**
     * Generate a Markov text
     * 
     * @param hmap
     * @param dataList
     * @return 
     */
    public static LinkedList<String> textGenerator(LinkedHashMap<LinkedList<String>, LinkedList<String>> hmap, LinkedList<String> dataList) {
        // Debugging
        if(debug_flag) {
            System.out.println("# Markov Map: \n#\n#\t" + hmap + "\n#");
        }
        
        int size;
        int index;
        
        // Chain to store data
        LinkedList<String> chain = new LinkedList<>();
        
        // Chain to store prospective data
        LinkedList<String> nextChain = new LinkedList<>();

        for (LinkedList keyList : hmap.keySet()) {
            if (chain.isEmpty()) {      
                for (int k = 0; k < order; k++) {
                    index = k;

                    String currentKey = keyList.get(index).toString();

                    chain.add(currentKey);
                    nextChain.add(currentKey);
                }
                
                int count = frequencyMap.get(chain);
                count --;
                
                frequencyMap.put(chain, count);
                
                LinkedList<String> nextWordsArray = hmap.get(keyList);
                
                String nextWord = getNextWord(nextWordsArray);
                
                nextChain.removeFirst();
                nextChain.add(nextWord);
                
                int nextCount = frequencyMap.get(nextChain);
                
                chain.add(nextWord);                
                
                nextCount--;
                frequencyMap.put(nextChain, nextCount);
            } else {
                for (int N = 1; N < chain.size(); N++) {
                    if (chain.size() - N == order) {
                        // Get an array of words based on order
                        LinkedList<String> currentKeyArray = getKeyArray(N, chain);
                        
                        // Check if HasMap contains array of words
                        if (hmap.containsKey(currentKeyArray)) {                                                      
                            LinkedList<String> nextWordsArray = hmap.get(currentKeyArray);
                            
                            if(nextWordsArray.isEmpty()) {
                                break;
                            } else {                                                                
                                // Guess next word
                                size = nextWordsArray.size();
                                
                                String nextWord;
                                LinkedList<String> nextKeyArray;
                                
                                if(size == 1) {                     
                                    nextKeyArray = getKeyArray(N, chain);
                                    nextWord = nextWordsArray.get(0);
                                    
                                    nextKeyArray.removeFirst();
                                    nextKeyArray.add(nextWord);

                                    int nextKeyArrayCount = frequencyMap.get(nextKeyArray);
                                    
                                    if(nextKeyArrayCount != 0) {
                                        chain.add(nextWord);

                                        nextKeyArrayCount--;

                                        frequencyMap.put(nextKeyArray, nextKeyArrayCount);     
                                    }
                                } else {
                                    int chainSize = chain.size();
                                    int dataSize = dataList.size();
                                    
                                    if(chainSize != dataSize) {
                                        for(int i = 0; i < size; i++) {
                                            nextKeyArray = getKeyArray(N, chain);

                                            index = rnd.nextInt(size);  
                                            
                                            nextWord = nextWordsArray.get(index);
                                            
                                            nextKeyArray.removeFirst();
                                            nextKeyArray.add(nextWord);

                                            int nextKeyArrayCount = frequencyMap.get(nextKeyArray);

                                            if(nextKeyArrayCount != 0) {
                                                chain.add(nextWord);

                                                nextKeyArrayCount--;
                                                frequencyMap.put(nextKeyArray, nextKeyArrayCount);   
                                                
                                                break;                                                
                                            }
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                        
//                        System.out.println("frequencyMap: " + frequencyMap);
//                        System.out.println("\nchain: " + chain + "\n-----------\n");
                    }
                }
            }
        }

//        System.out.println("# Markov Frequency Map (final): \n#\n#\t" + frequencyMap + "\n####");
//        System.out.println("# Markov Chain: \n#\n#\t" + chain + "\n####");
//        System.out.println("\n");
        
        return chain;
    }
    
    /**
     * Generate Markov Text
     * 
     * @param chain
     */
    public static void generateMarkovText(LinkedList<String> chain) {  
        if(debug_flag) {
            System.out.print("##\n# Markov Text: \t\t");
        }
        
        for (int N = 0; N < chain.size(); N++) {
            System.out.print("" + chain.get(N) + "");
        }
    }  
    
    /**
     * Generate Markov Text
     * 
     * @param chain
     * @return 
     */
    public static String generateOutput(LinkedList<String> chain) {         
//        System.out.print("##\n# Markov Text: \t\t");

        String output = null;

        for (int N = 0; N < chain.size(); N++) {
            output = "" + chain.get(N) + "";
        }
        
        return output;
    }
    
    /**
     * Generate Final Result
     * 
     * @param line
     * @param chain
     */
    public static void generateFinalResult(String line, LinkedList<String> chain) {
//        System.out.println("################");
//        System.out.println("# Final Result # ");
        if(debug_flag) {
            System.out.println("################");
            System.out.println("# Original Text: \t" + line.trim());
        }
        
        // Generate Markov Text
        generateMarkovText(chain);
        
        // Debugging
        if(debug_flag) {
            System.out.println("\n##");
            System.out.println("# Markov Frequency Map (final): \n#\n#\t" + frequencyMap + "\n#\n");
        }
        System.out.println("\n");
    }
    
    /**
     * main()
     * 
     * @param args
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {        
        BufferedReader br = getBufferedReader();
        
        // Debugging
        if(debug_flag) {
            System.out.println("################");
            System.out.println("# Order (N): \t\t" + order);
        }
        
        String line;
        
        LinkedList<String> chain;
        
        while ((line = br.readLine()) != null) {               
//            System.out.println("############");
//            System.out.println("# Analysis #");
//            System.out.println("############");

            if(debug_flag) {
                System.out.println("##\n# Original Text: \t" + line.trim() + "\n##");
            }
            
            chain = markovChain(line);
          
            generateFinalResult(line, chain);
        }
    }
}
