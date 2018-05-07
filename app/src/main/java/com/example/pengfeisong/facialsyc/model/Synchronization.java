package com.example.pengfeisong.facialsyc.model;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by pengfeisong on 5/6/18.
 */

public class Synchronization {

    LinkedList<Float> scoreWindow;
    int dataSize;

    public Synchronization(int size) {
        this.dataSize = size;
        scoreWindow = new LinkedList<Float>();
    }

    public static double synScore(ArrayList<Float> list1, ArrayList<Float> list2) {
        double similarity = 0;
        double xSum = 0;
        double ySum = 0;
        double xSquSum = 0;
        double ySquSum = 0;
        double xySum = 0;
        int n = list1.size();
        for(int i = 0; i < Math.min(list1.size(), list2.size()); i++) {
            float x = list1.get(i);
            float y = list2.get(i);
            xSum += x;
            xSquSum += x*x;
            ySum += y;
            ySquSum += y*y;
            xySum += x*y;
            System.out.println("x = " + x + " y = " + y);
        }
        similarity = ((n * xySum) - (xSum * ySum)) / (Math.sqrt(n * xSquSum - xSum * xSum) * Math.sqrt(n * ySquSum - ySum * ySum));
        return similarity;
    }

}
