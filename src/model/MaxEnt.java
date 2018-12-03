package model;

import utils.DataProcessing;

import java.io.*;
import java.util.*;

/**
 *  This class implements the maximum entropy model. The algorithm used in this class
 *   is IIS.
 * @author Super-Tang
 */
public class MaxEnt
{
    private final static boolean DEBUG = true;
    private static final double EPSILON = 0.000001;
    private int n;
    private int minY = 0;
    private int maxY = 5;
    private double[] empiricalExpects;
    private double w[];
    private List<Instance> instances = new ArrayList<>();
    private List<FeatureFunction> functions = new ArrayList<>();
    private List<Feature> features = new ArrayList<>();

    /**
     *  Constructors
     * @param trainInstance the instances used for training
     */
    public MaxEnt(List<Instance> trainInstance) {
        instances.addAll(trainInstance);
        n = trainInstance.size();
        createFeatureFunctions(instances);
        w = new double[functions.size()];
        empiricalExpects = new double[functions.size()];
        calculateEmpiricalExpects();
    }

    /**
     *  Create feature functions from the training instances. If instanceA's 3th feature is 1, then
     *  there will be a feature function f(3,1,instanceA'label)
     * @param instances the training instances
     */
    private void createFeatureFunctions(List<Instance> instances) {
        LinkedHashSet<Feature> featureSet = new LinkedHashSet<>();
        for (Instance instance : instances) {
            featureSet.add(instance.getFeature());
            for(int i=0; i<instance.getFeature().getValues().length; i++){
                FeatureFunction featureFunction = new  FeatureFunction(i,instance.getFeature().getValues()[i],instance.getLabel());
                if(!functions.contains(featureFunction)){
                    functions.add(featureFunction);
                }
            }
        }
        features = new ArrayList<>(featureSet);
        if (DEBUG) {
            System.out.println("# features = " + features.size());
            System.out.println("# functions = " + functions.size());
        }

    }

    /**
     *  Calculate the condition probability
     */
    private double[][] calculateProbYGivenX() {
        double[][] condProb = new double[features.size()][maxY + 1];
        for (int y = minY; y <= maxY; y++) {
            for (int i = 0; i < features.size(); i++) {
                double z = 0;
                for (int j = 0; j < functions.size(); j++) {
                    z += w[j] * functions.get(j).apply(features.get(i), y);
                }
                condProb[i][y] = Math.exp(z);
            }
        }
        for (int i = 0; i < features.size(); i++) {
            double normalize = 0;
            for (int y = minY; y <= maxY; y++) {
                normalize += condProb[i][y];
            }
            for (int y = minY; y <= maxY; y++) {
                condProb[i][y] /= normalize;
            }
        }
        return condProb;
    }

    /**
     *   IIS training
     * @param fileName the training weights file
     */
    public void train(String fileName){
        File file = new File(fileName);
        if(file.exists()){
            loadWeights(fileName);
        }
        double delta;
        int k = 0;
        while (k < 200){
            k += 1;
            for (int i = 0; i < functions.size(); i++) {
                delta = calculateDelta(empiricalExpects[i], i);
                w[i] += delta;
            }
            saveWeights(fileName);
            if (DEBUG) {
                System.out.println("ITERATIONS: " + k + " " + Arrays.toString(w));
            }
        }
    }

    /**
     *  Load the weights from the file
     * @param file the training weights file
     */
    public void loadWeights(String file){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(file)));
            String line = bufferedReader.readLine();
            String []tokens = line.split(" ");
            for(int i=0; i<tokens.length; i++){
                w[i] = Double.parseDouble(tokens[i]);
            }
        }catch (Exception e){
            System.out.println("Something wrong while loading weights.");
        }
    }

    /**
     *  Save the weights of each iteration so that when something goes wrong, there is no need to
     *  start all over again
     */
    private void saveWeights(String file){
        try {
            FileWriter fileWriter = new FileWriter(new File(file));
            for (double aW : w) {
                fileWriter.write(aW + " ");
            }
            fileWriter.close();
        }catch (Exception e){
            System.out.println("Something wrong while writing weights into file.");
        }
    }

    /**
     *  Classify
     * @param instance the instance to be classified
     * @return the most possible label of the instance
     */
    public int classify(Instance instance) {
        double max = 0;
        int label = 0;
        for (int y = minY; y <= maxY; y++) {
            double sum = 0;
            for (int i = 0; i < functions.size(); i++) {
                sum += Math.exp(w[i] * functions.get(i).apply(instance.getFeature(), y));
            }
            if (sum > max) {
                max = sum;
                label = y;
            }
        }
        return label;
    }

    /**
     *  Calculate the empirical expects
     */
    private void calculateEmpiricalExpects() {
        for (Instance instance : instances) {
            int y = instance.getLabel();
            Feature feature = instance.getFeature();
            for (int i = 0; i < functions.size(); i++) {
                empiricalExpects[i] += functions.get(i).apply(feature, y);
            }
        }
        for (int i = 0; i < functions.size(); i++) {
            empiricalExpects[i] /= 1.0 * n;
        }
        if (DEBUG) {
            System.out.println(Arrays.toString(empiricalExpects));
        }
    }

    /**
     *   Calculate the sum of f(x,y)
     * @param feature
     * @param y
     * @return
     */
    private int applyF(Feature feature, int y) {
        int sum = 0;
        for (FeatureFunction function1 : functions) {
            sum += function1.apply(feature, y);
        }
        return sum;
    }

    /**
     *  Calculate the answer of the equation in IIS algorithm, see the report
     * @param empirical the empirical Expects
     * @param fi the feature function number
     * @return the answer of equation
     */
    private double calculateDelta(double empirical, int fi) {
        double delta = 0;
        double fNewton, dfNewton;
        double pyx[][] = calculateProbYGivenX();
        int iterations = 0;
        Instance instance;
        Feature feature;
        int index, f, y;
        double prod;
        while (iterations < 100) {
            fNewton = dfNewton = 0;
            for (Instance instance1 : instances) {
                instance = instance1;
                feature = instance.getFeature();
                index = features.indexOf(feature);
                for (y = minY; y <= maxY; y++) {
                    f = applyF(feature, y);
                    prod = pyx[index][y] * functions.get(fi).apply(feature, y) * Math.exp(delta * f);
                    fNewton += prod;
                    dfNewton += prod * f;
                }
            }
            fNewton = empirical - fNewton / n;
            dfNewton = -dfNewton / n;
            if (Math.abs(fNewton) < 0.001) {
                return delta;
            }
            double ratio = fNewton / dfNewton;
            delta -= ratio;
            if (Math.abs(ratio) < EPSILON) {
                return delta;
            }
            iterations++;
        }
        throw new RuntimeException("IIS did not converge");
    }
}

