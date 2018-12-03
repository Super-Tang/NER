package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.Buffer;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class is used for extracting features
 * @author Super-Tang
 */
public class FeatureExtraction {

    private void initMaps(){
        DataProcessing.initMaps();
    }

    /**
     *  Write the features into file
     */
    private void writeToFile(HashMap<String, Integer>[] hashMap) {
        try {
            FileWriter fileWriter = new FileWriter(new File("features6.txt"));
            for (int i = 0; i <= 5; i++) {
                for (String s : hashMap[i].keySet()) {
                    if (hashMap[i].get(s) > DataProcessing.THRESHOLD) {
                        fileWriter.write(i + " " + s + "\n");
                    }
                }
            }
            fileWriter.close();
        } catch (Exception e) {
            System.out.println("Something wrong while writing into file...");
        }
    }

    /**
     *   Feature extraction function 1: extract the features before the current word
     */
    private void extractMoreFeatures(){
        int k;
        int wordCount;
        HashMap<String,Integer> []hashMap = new HashMap[6];
        for(int i=0; i<6; i++){
            hashMap[i] = new HashMap<>();
        }
        String key, before1, before2, before3, line;
        String []tokens;
        try {
            before1 = "0";
            before2 = "0";
            before3 = "0";
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("Feature.txt")));
            while ((line = bufferedReader.readLine()) != null){
                // TODO : need to change the code below according to the feature teeplate
                 if(line.length() < 1){
                     before3 = before2;
                     before2 = before1;
                     before1 = "0";
                     continue;
                 }
                 tokens = line.split("\t");
                 key =  tokens[0] + " " + tokens[1] + " " + tokens[2] +" " + tokens[3] + " " + before1 + " " +
                         DataProcessing.WORDS2LABEL.get(before1) + " " +  before2 + " " + DataProcessing.WORDS2LABEL.get(before2) +
                         " " +  before3 + " " + DataProcessing.WORDS2LABEL.get(before3);
                 k = Integer.parseInt(tokens[4]);
                 wordCount = hashMap[k].containsKey(key) ? hashMap[k].get(key) + 1 : 1;
                 hashMap[k].put(key, wordCount);
                 before3 = before2;
                 before2 = before1;
                 before1 = tokens[0];
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Something wrong while extracting features");
        }
        writeToFile(hashMap);
    }

    /**
     *  Feature extraction function 2: can extract the features after the current word
     */
    private void extract(){
        int k;
        int wordCount;
        HashMap<String,Integer> []hashMap = new HashMap[6];
        for(int i=0; i<6; i++){
            hashMap[i] = new HashMap<>();
        }
        ArrayList<String>sentence = new ArrayList<>();
        String key, before1, before, temp, after, after1,line;
       try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("Feature.txt")));
            while ((line = bufferedReader.readLine()) != null) {
                if (line.length() < 1) {
                    // TODO： need change the code according to the feature template
                    for(int i=0; i<sentence.size(); i++){
                        before = i-1.>= 0 ? sentence.get(i-1) : "0\t0\t0\t0\t0";
//                        before1 = i-2.>= 0 ? sentence.get(i-2) : "0\t0\t0\t0\t0";
                        temp = sentence.get(i);
                        after = i < sentence.size() - 1 ? sentence.get(i+1) : "0\t0\t0\t0\t0";
                        after1 = i < sentence.size() - 2 ? sentence.get(i+2) : "0\t0\t0\t0\t0";
//                        key = temp.split("\t")[0] + " " + temp.split("\t")[1] + " " + temp.split("\t")[2]
//                                + " " + temp.split("\t")[3] + " " + before.split("\t")[0] + " " + before.split("\t")[4] + " " +
//                                before1.split("\t")[0] + " " + before1.split("\t")[4] + " " + after.split("\t")[0];
                        key = temp.split("\t")[0] + " " + temp.split("\t")[1] + " " + temp.split("\t")[2]
                                + " " + temp.split("\t")[3] + " " + before.split("\t")[0] +  " " + before.split("\t")[4] + " "
                                + after.split("\t")[0] + " " + after1.split("\t")[0];
                        k = Integer.parseInt(temp.split("\t")[4]);
                        wordCount = hashMap[k].containsKey(key) ? hashMap[k].get(key) + 1 : 1;
                        hashMap[k].put(key, wordCount);
                    }
                   sentence.clear();
                }else{
                    sentence.add(line);
                }
            }
        }catch (Exception e){
           e.printStackTrace();
            System.out.println("Something wrong while extracting features");
        }
        writeToFile(hashMap);
    }

    public static void main(String[] args) {
       FeatureExtraction featureExtraction = new FeatureExtraction();
       featureExtraction.initMaps();
//        featureExtraction.extractMoreFeatures();
        featureExtraction.extract();
    }

}
