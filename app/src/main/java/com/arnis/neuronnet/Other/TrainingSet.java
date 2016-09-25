package com.arnis.neuronnet.Other;

import com.arnis.neuronnet.Retrofit.Data;
import com.arnis.neuronnet.Retrofit.Stock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by arnis on 05.09.2016.
 */
public class TrainingSet {
    public int getSetEntries() {
        return setEntries;
    }

    private int setEntries;
    private ArrayList<Set> trainSet;

    public TrainingSet() {
        trainSet = new ArrayList<>();
        setEntries=0;
    }

    public void addEntry(Set set){
        trainSet.add(set);
        setEntries++;
    }

    public Set getEntry(int index){
        return trainSet.get(index);
    }

    public void addTrainStocks(List<Stock> stocks,int predictWindow,int prediction){
        List<Double> percentage = new ArrayList<>();

        double difference;
        double res;

        for (int i = 0; i < stocks.size() - 1; i++) {
            difference = (stocks.get(i+1).average()-stocks.get(i).average());
            res = difference/stocks.get(i).average();
            if (res>1)
                res=1;
            percentage.add(res);
        }

        if (predictWindow+prediction<=percentage.size()) {

            for (int i = 0; i <= percentage.size() - (predictWindow + prediction); i++) {
                double[] window = new double[predictWindow];
                double[] predict = new double[prediction];
                int j;
                for (j = 0; j < predictWindow; j++) {
                    window[j] = percentage.get(i + j);
                }
                int k = 0;
                for (; j < prediction + predictWindow; j++) {
                    predict[k++] = percentage.get(i + j);
                }
                addEntry(new Set(window, predict));
            }
        }

    }
    public void addWorkStocks(List<Stock> stocks,int predictWindow){
        List<Double> percentage = new ArrayList<>();

        double difference;
        double res;
        if (stocks.size()>predictWindow) {
            for (int i = stocks.size() - predictWindow - 1; i < stocks.size() - 1; i++) {
                difference = (stocks.get(i + 1).average() - stocks.get(i).average());
                res = difference / stocks.get(i).average();
                if (res > 1)
                    res = 1;
                percentage.add(res);
            }

            double[] window = new double[predictWindow];
            for (int j = 0; j < predictWindow; j++) {
                window[j] = percentage.get(j);
            }
            addEntry(new Set(stocks.get(predictWindow).Symbol, window));
        }
    }

    public static class Set{
        private String description;
        private double[] inputValues;
        private double[] desiredOutput;

        private Set(double[] inputValues, double[] desiredOutput) {
            this.description = description;
            this.inputValues = inputValues;
            this.desiredOutput = desiredOutput;
        }
        private Set(String description, double[] inputValues) {
            this.description = description;
            this.inputValues = inputValues;
        }

        public String getDescription() {
            return description;
        }

        public double getInputValue(int index){
            return inputValues[index];
        }

        public double getOutputValue(int index){
            return desiredOutput[index];
        }

        public double[] getInputValues() {
            return inputValues;
        }

        public double[] getDesiredOutput() {
            return desiredOutput;
        }


    }
}
