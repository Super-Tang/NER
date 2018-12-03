package utils;

import model.Instance;

import java.io.*;
import java.util.*;

/**
 *  This class is used for processing data
 * @author Super-Tang
 */
public class DataProcessing {
    private static final HashMap<String, String> LABELSID = new HashMap<>();
    private static final HashMap<String,String> WORDSID = new HashMap<>();
    public static final HashMap<String, String> WORDS2LABEL = new HashMap<>();
    public static final int THRESHOLD = 5;

    /**
     *  Read the features from file and change it to specific format
     * @param path the file path that contains the features of each instance
     * @return a list of instance
     * @throws FileNotFoundException if no such file exists
     */
    public static List<Instance> readFeatureSet(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        List<Instance> instances = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if(line.length()  < 1){
                continue;
            }
            List<String> tokens = Arrays.asList(line.split("\\s"));
            String s1 = tokens.get(0);
            int label = Integer.parseInt(s1);
            int[] features = new int[tokens.size() - 1];
            for (int i = 1; i < tokens.size() ; i++) {
                String s = tokens.get(i);
                features[i - 1] = "null".equals(s) ? 0 : Integer.parseInt(s);
            }
            Instance instance = new Instance(label, features);
            instances.add(instance);
        }
        scanner.close();
        return instances;
    }

    /**
     *  There is no comment here
     */
    private static void initWordsID(){
        String tempString;
        try {
            BufferedReader bufferedReader1 = new BufferedReader(new FileReader(new File("C:\\Users\\Administrator\\Desktop\\LabelExtract\\src\\data\\word2id.txt")));
            while ((tempString = bufferedReader1.readLine()) != null) {
                WORDSID.put(tempString.split("\t")[0], tempString.split("\t")[1]);
            }
        }catch (Exception e) {
            System.out.println("Something wrong while initializing the word map");
        }
    }

    /**
     *  Here. too
     */
    private static void initLabelsID() {
        LABELSID.put("O", "0");
        LABELSID.put("B-DNA","1");
        LABELSID.put("I-DNA","1");
        LABELSID.put("B-RNA","2");
        LABELSID.put("I-RNA","2");
        LABELSID.put("B-protein","3");
        LABELSID.put("I-protein","3");
        LABELSID.put("B-cell_type","4");
        LABELSID.put("I-cell_type","4");
        LABELSID.put("B-cell_line","5");
        LABELSID.put("I-cell_line","5");
    }

    /**
     *  Here, again
     */
    private static void initWords2Label() {
        String tempString;
        try {
            BufferedReader bufferedReader3 = new BufferedReader(new FileReader(new File("C:\\Users\\Administrator\\Desktop\\LabelExtract\\src\\data\\word2ids1.txt")));
            while ((tempString = bufferedReader3.readLine()) != null) {
                if (tempString.length() > 1) {
                    WORDS2LABEL.put(tempString.split("\t")[0], tempString.split("\t")[1]);
                }
            }
        } catch (Exception e) {
            System.out.println("Something wrong while reading words' labels");
        }
    }

    /**
     *   emm...
     */
    public static void initMaps(){
        initLabelsID();
        initWordsID();
        initWords2Label();
    }

    /**
     *  Change the representation of each instance
     */
    private static void changeRep(){
        try {
            FileReader fileReader = new FileReader(new File("C:\\Users\\Super-Tang\\Desktop\\ll\\Genia4ERtraining\\Genia4ERtask1.iob2"));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String tempString;
            ArrayList<String> strings = new ArrayList<>();
            while ((tempString = bufferedReader.readLine()) != null) {
                strings.add(tempString);
            }
            FileWriter fileWriter = new FileWriter("word2ids1.txt");
            String i, j;
            for (String s : strings) {
                if ("".equals(s)) {
                    fileWriter.write(s + "\n");
                } else {
                    i = s.split("\t")[0];
                    j = s.split("\t")[1];
                    fileWriter.write(WORDSID.get(i) + "\t" + LABELSID.get(j) + "\n");
                }
            }
            fileWriter.flush();
            fileWriter.close();
        }catch (Exception e) {
            System.out.println("Something wrong while transferring data...");
        }
    }

    /**
     *  Change the label of each instance to a number
     */
    public static HashMap<String, HashSet<Integer>> labelTransfer(){
        HashMap<String,HashSet<Integer>>arrayList = new HashMap<>();
        try {
            FileReader fileReader = new FileReader(new File("C:\\Users\\Super-Tang\\Desktop\\ll\\Genia4ERtraining\\Genia4ERtask1.iob2"));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String tempString;
            ArrayList<String> string = new ArrayList<>();
            while ((tempString = bufferedReader.readLine()) != null) {
                string.add(tempString);
            }
            tempString = "-1";
            HashSet<Integer> arrayList1;
            for(String s : string){
                if(s.length() < 1) {
                    continue;
                }
                int value = LABELSID.get(tempString) == null ? 0 : Integer.parseInt(LABELSID.get(tempString));
                arrayList1 = arrayList.get(LABELSID.get(s.split("\t")[1]));
                if(arrayList1 == null){
                    arrayList1 = new HashSet<>();
                }
                arrayList1.add(value);
                arrayList.put(LABELSID.get(s.split("\t")[1]), arrayList1);
                tempString = s.split("\t")[1];
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return arrayList;
    }

    /**
     *  Transfer the test file according to the feature template chose
     */
    private static void transferTestText(){
        try{
            FileReader fileReader = new FileReader(new File("C:\\Users\\Administrator\\Desktop\\Genia4EReval1.raw"));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String tempString;
            FileWriter fileWriter = new FileWriter("test6.txt");
            ArrayList<String> strings = new ArrayList<>();
            String i;
            String before1, before, temp, after, after1, key;
            while ((tempString = bufferedReader.readLine()) != null) {
                // TODO : need to change the code below according to the feature template
                if(tempString.length() < 1){
                    for(int j = 0; j < strings.size();j++){
                        before = j-1.>= 0 ? strings.get(j-1) : "0\t0";
//                        before1 = j-2.>= 0 ? strings.get(j-2) : "0\t0";
                        temp = strings.get(j);
                        after = j < strings.size() - 1 ? strings.get(j+1) : "0\t0";
                        after1 = j < strings.size() - 2 ? strings.get(j+2) : "0\t0";
                        i = temp.split("\t")[0];
                        if(!DataProcessing.WORDSID.containsKey(i)){
                            DataProcessing.WORDSID.put(i,String.valueOf(DataProcessing.WORDSID.size()+1));
                        }
                        key = "11 " + DataProcessing.WORDSID.get(i) + " " + containsUpperLetter(i) + " " + containsDigits(i)
                                + " " + containsSlash(i) + " " + DataProcessing.WORDSID.get(before.split("\t")[0]) + " 0 " +
                                DataProcessing.WORDSID.get(after.split("\t")[0]) + " " + DataProcessing.WORDSID.get(after1.split("\t")[0]);
                        fileWriter.write(key + "\n");
                    }
                    fileWriter.write("\n");
                    strings.clear();
                }else {
                    strings.add(tempString);
                }
            }
            fileWriter.flush();
            fileWriter.close();
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something wrong while transferring data...");
        }
    }

    /**
     *  Change the features of each instance
     */
    private static void changeFeature(){
        try {
            FileReader fileReader = new FileReader(new File("C:\\Users\\Super-Tang\\Desktop\\ll\\Genia4ERtraining\\Genia4ERtask1.iob2"));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String tempString;
            ArrayList<String> strings = new ArrayList<>();
            while ((tempString = bufferedReader.readLine()) != null) {
                strings.add(tempString);
            }
            FileWriter fileWriter = new FileWriter("Feature.txt");
            String i, j;
            int containsUpper, containsDigit, containsSlash;
            for (String s : strings) {
                if ("".equals(s)) {
                    fileWriter.write(s + "\n");
                } else {
                    i = s.split("\t")[0];
                    j = s.split("\t")[1];
                    containsUpper = containsUpperLetter(i);
                    containsDigit = containsDigits(i);
                    containsSlash = containsSlash(i);
                    fileWriter.write(WORDSID.get(i) +  "\t" + containsUpper + "\t" + containsDigit + "\t" + containsSlash + "\t"   +LABELSID.get(j)  + "\n");
                }
            }
            fileWriter.flush();
            fileWriter.close();
        }catch (Exception e) {
            System.out.println("Something wrong while transferring data...");
        }
    }

    /**
     *  Feature one : contains capitals ?
     * @return 1 if the string contains capital else 0
     */
    private static int containsUpperLetter(String s) {
        return !s.matches("[a-z]+") ? 1 : 0;
    }

    /**
     *   Feature two: contains digits?
     * @return 1 if the string contains digits else 0
     */
    private static int containsDigits(String s){
        return s.matches(".*\\d+.*") ? 1 : 0;
    }

    /**
     *   Feature three : contains special symbols - or /
     * @return 1 if contains such symbols else 0
     */
    private static int containsSlash(String s){
        return s.contains("-") || s.contains("/") ? 1 : 0;
    }

    /**
     *   Format the result of prediction. Change the numbers to the labels they represent
     * @param arrayList the array contains the label of each instance
     * @param file the results file
     */
    public static void writingResults(ArrayList<Integer> arrayList, String file){
        try {
            FileWriter fileWriter = new FileWriter(new File(file));
            int i = 0;
            FileReader fileReader = new FileReader(new File("C:\\Users\\Administrator\\Desktop\\Genia4EReval2.raw"));
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String temp;
            while ((temp = bufferedReader.readLine()) != null){
                if(temp.equals("") || temp.startsWith("###")){
                    fileWriter.write(temp + "\n");
                    continue;
                }
                String  label;
                switch (arrayList.get(i)){
                    case 1:
                        label = arrayList.get(i-1) != 1 ? "B-DNA" : "I-DNA";
                        break;
                    case 2:
                        label = arrayList.get(i-1) != 2 ? "B-RNA" : "I-RNA";
                        break;
                    case 3:
                        label = arrayList.get(i-1) != 3 ? "B-protein" : "I-protein";
                        break;
                    case 4:
                        label = arrayList.get(i-1) != 4 ? "B-cell_type" : "I-cell_type";
                        break;
                    case 5:
                        label = arrayList.get(i-1) != 5 ? "B-cell_line" : "I-cell_line";
                        break;
                    default:
                        label = "O";
                }
                fileWriter.write(temp + "\t" + label +"\n");
                i++;
            }
            bufferedReader.close();
            fileWriter.close();
        }catch (Exception e){
            System.out.println("Something wrong while writing the results");
        }
    }

    public static void main(String[] args) {
        DataProcessing.initMaps();
//        DataProcessing.changeFeature();
        DataProcessing.transferTestText();
    }
}
