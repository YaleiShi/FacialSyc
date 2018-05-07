package com.example.pengfeisong.facialsyc.model;

import android.support.annotation.NonNull;

import com.affectiva.android.affdex.sdk.detector.Face;
import com.example.pengfeisong.facialsyc.MainActivity;

import java.util.LinkedList;

/**
 * Created by pengfeisong on 5/7/18.
 */

public class Joy {


    class JoyDataPoint implements Comparable<JoyDataPoint>{
        float score;
        long time;

        JoyDataPoint(float s, long t) {
            score = s;
            time = t;
        }

        public int compareTo(@NonNull JoyDataPoint p2) {
            return (int)(this.time - p2.time) / 1000;
        }
    }


    LinkedList<JoyDataPoint> window1;
    LinkedList<Float> window2;

    public Joy() {
        window1 = new LinkedList<JoyDataPoint>();
        window2 = new LinkedList<Float>();
    }




    public float lastTenSecMeanJoy(Face face) {
        float meanJoy = 0;
        float joy = face.emotions.getJoy();
        JoyDataPoint joyDP = new JoyDataPoint(joy, System.currentTimeMillis());

        if(window1.peekFirst() == null || joyDP.compareTo(window1.peekFirst()) < 10) {//Compare to timestamp of the first node of the list and the current node
            window1.add(joyDP);
        } else {
            window1.add(joyDP);
            window1.removeFirst();
        }

        //Using  weighted moving average (WMA) to calculate the average joy score
        for(int i = 0; i < window1.size(); i++) {
            //Add each value of the data point in the list with weighting
            meanJoy += window1.get(i).score * (i + 1);
        }
        meanJoy /= (window1.size() * (window1.size() + 1)) / 2;
        return meanJoy;
    }

    public float last100MeanJoy(Face face) {
        float meanJoy = 0;
        float joy = face.emotions.getJoy();
        if(window2.size() >= 100) window2.removeFirst();
        window2.add(joy);
        //Using  weighted moving average (WMA) to calculate the average joy score
        for(int i = 0; i < window2.size(); i++) {
            //Add each value of the data point in the list with weighting
            meanJoy += window2.get(i);
        }
        meanJoy /= window2.size();
        return meanJoy;
    }

    public float last100MeanJoyWMA(Face face) {
        float meanJoy = 0;
        float joy = face.emotions.getJoy();
        if(window2.size() >= 100) window2.removeFirst();
        window2.add(joy);
        //Using  weighted moving average (WMA) to calculate the average joy score
        for(int i = 0; i < window2.size(); i++) {
            //Add each value of the data point in the list with weighting
            meanJoy += window2.get(i) * (i + 1);
        }
        meanJoy /= (window2.size() * (window2.size() + 1)) / 2;
        return meanJoy;
    }
}
