package com.example.pengfeisong.facialsyc.model;

import android.support.annotation.NonNull;

import com.affectiva.android.affdex.sdk.detector.Face;
import com.example.pengfeisong.facialsyc.MainActivity;

import java.util.LinkedList;

/**
 * Created by pengfeisong on 5/7/18.
 */

public class Anger {


    class AngerDataPoint implements Comparable<AngerDataPoint>{
        float score;
        long time;

        AngerDataPoint(float s, long t) {
            score = s;
            time = t;
        }

        public int compareTo(@NonNull AngerDataPoint p2) {
            return (int)(this.time - p2.time) / 1000;
        }
    }


    LinkedList<AngerDataPoint> window1;
    LinkedList<Float> window2;

    public Anger() {
        window1 = new LinkedList<AngerDataPoint>();
        window2 = new LinkedList<Float>();
    }




    public float lastTenSecMeanAnger(Face face) {
        float meanJoy = 0;
        float anger = face.emotions.getAnger();
        AngerDataPoint angerDP = new AngerDataPoint(anger, System.currentTimeMillis());

        if(window1.peekFirst() == null || angerDP.compareTo(window1.peekFirst()) < 10) {//Compare to timestamp of the first node of the list and the current node
            window1.add(angerDP);
        } else {
            window1.add(angerDP);
            window1.removeFirst();
        }

        //Using  weighted moving average (WMA) to calculate the average anger score
        for(int i = 0; i < window1.size(); i++) {
            //Add each value of the data point in the list with weighting
            meanJoy += window1.get(i).score * (i + 1);
        }
        meanJoy /= (window1.size() * (window1.size() + 1)) / 2;
        return meanJoy;
    }

    public float last100MeanAnger(Face face) {
        float meanAnger = 0;
        float anger = face.emotions.getAnger();
        if(window2.size() >= 100) window2.removeFirst();
        window2.add(anger);
        //Using  weighted moving average (WMA) to calculate the average joy score
        for(int i = 0; i < window2.size(); i++) {
            //Add each value of the data point in the list with weighting
            meanAnger += window2.get(i);
        }
        meanAnger /= window2.size();
        return meanAnger;
    }

    public float last100MeanAngerWMA(Face face) {
        float meanAnger = 0;
        float anger = face.emotions.getAnger();
        if(window2.size() >= 100) window2.removeFirst();
        window2.add(anger);
        //Using  weighted moving average (WMA) to calculate the average joy score
        for(int i = 0; i < window2.size(); i++) {
            //Add each value of the data point in the list with weighting
            meanAnger += window2.get(i) * (i + 1);
        }
        meanAnger /= (window2.size() * (window2.size() + 1)) / 2;
        return meanAnger;
    }
}
