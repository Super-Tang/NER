package main;

import model.Instance;
import model.MaxEnt;
import utils.DataProcessing;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String... args) throws Exception {
        String file1 = "features6.txt";
        List<Instance> instances = DataProcessing.readFeatureSet(file1);
        MaxEnt maxEnt = new MaxEnt(instances);
        String file2 = "weights6.txt";
        List<Instance> instances1 = DataProcessing.readFeatureSet("test6.txt");
        maxEnt.train(file2);
        maxEnt.loadWeights(file2);
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i=0; i<instances1.size();i++) {
            //TODO: need to change the code below according to the feature template
//            if(i-1>=0) {
//                instances1.get(i).getFeature().setLabel(arrayList.get(i-1),1);
//            }
            int predict = maxEnt.classify(instances1.get(i));
            arrayList.add(predict);
        }
        DataProcessing.writingResults(arrayList,"results5");
    }
}
