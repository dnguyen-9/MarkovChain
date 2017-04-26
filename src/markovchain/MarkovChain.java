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

    // HashMap for freguency
    public static LinkedHashMap<LinkedList<String>, Integer> frequencyMap = new LinkedHashMap<>();        

    // Random value
    static Random rnd = new Random();

    /**
     * main()
     * 
     * @param args
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {

        BufferedReader br = getBufferedReader();

        System.out.println("#################");
        System.out.println("# order: " + order + "\t#");
        System.out.println("#################\n");
        
        String line;
        
        while ((line = br.readLine()) != null) {   
            System.out.println("######################################");
            System.out.println("# Original Text: " + line + "\n");
            
            markovChain(line);
        }
    }
    
    /**
     * 
     * @return
     * @throws FileNotFoundException 
     */
    public static BufferedReader getBufferedReader() throws FileNotFoundException {
        // Get file path
        String filePath = new java.io.File("").getAbsolutePath() + "/src/markovchain/text/";

        // Get file name
        String fileName = filePath + "test_3.txt";

//        String line;

        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        
        return br;
    }

    /**
     * Generate Markov Model
     * 
     * @param line 
     */
    public static void markovChain(String line) {
        // Define a LinkedHashMap 
        LinkedHashMap<LinkedList<String>, LinkedList<String>> hmap = new LinkedHashMap<>();        

        // Generating Markov Data
        LinkedList dataList = getDataList(line);
        
        // Generating Markov Frequency Map
        getFrequencyMap(dataList);
                
        for (int N = 0; N < dataList.size(); N++) {

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


        textGenerator(hmap, dataList);
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
        
        System.out.println("Markov Frequency Map: " + frequencyMap + "\n");
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
        
        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            for (int k = 0; k < word.length(); k++) {
                char c = word.charAt(k);

                String currentCharToString = Character.toString(c);

                dataList.add(currentCharToString);
            }
        }
        
        System.out.println("Markov Data: " + dataList + "\n");
        
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
     */
    public static void textGenerator(LinkedHashMap<LinkedList<String>, LinkedList<String>> hmap, LinkedList<String> dataList) {
        System.out.println("Markov Map: " + hmap + "\n");
        
        LinkedList<String> chain = new LinkedList<>();

        for (LinkedList keyList : hmap.keySet()) {
            if (chain.isEmpty()) {                
                for (int k = 0; k < order; k++) {
                    int index = k;

                    String currentKey = keyList.get(index).toString();

                    chain.add(currentKey);
                }

                LinkedList<String> nextWordsList = hmap.get(keyList);
                
                String nextWord = getNextWord(nextWordsList);

                chain.add(nextWord);
            } else {
                for (int N = 1; N < chain.size(); N++) {
                    if (chain.size() - N == order) {
                        LinkedList<String> currentKeyArray = getKeyArray(N, chain);

                        if (hmap.containsKey(currentKeyArray)) {
                            LinkedList<String> nextWordsList = hmap.get(currentKeyArray);

                            if(nextWordsList.isEmpty()) {
                                break;
                            } else {
                                String nextWord = getNextWord(nextWordsList);
                                chain.add(nextWord);
                            }
                        }
                    }

                    if (chain.size() == dataList.size()) {   
                        break;
                    }
                }
            }
        }

        System.out.println("Markov Chain: " + chain + "\n");
        
        printMarkovText(chain);
                
        System.out.println("\n");
    }
    
    /**
     * Generate Markov Text
     * 
     * @param chain
     */
    public static void printMarkovText(LinkedList<String> chain) {         
        System.out.print("Markov Text: ");
        
        for (int N = 0; N < chain.size(); N++) {
            System.out.print("" + chain.get(N) + "");
        }
    }    
}
