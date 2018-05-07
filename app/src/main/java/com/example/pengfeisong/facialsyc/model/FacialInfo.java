package com.example.pengfeisong.facialsyc.model;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by pengfeisong on 4/30/18.
 */

public class FacialInfo {
    private LinkedList<Float> joys;
    private int dataNums;
    private LinkedList<Float> angers;


    public FacialInfo(int size) {
        joys = new LinkedList<Float>();
        angers = new LinkedList<Float>();
        dataNums = size;
    }

    public void addJoy(float jl) {
        if(joys.size() < dataNums) {
            joys.add(jl);
        } else {
            joys.removeFirst();
            joys.add(jl);
        }
    }

    public void addAnger(float ag) {
        if(angers.size() < dataNums) {
            angers.add(ag);
        } else {
            angers.removeFirst();
            angers.add(ag);
        }
    }



    public ArrayList<Float> getJoyData() {
        ArrayList<Float> res= new ArrayList<>();
        Iterator cur = this.joys.iterator();
        while(cur.hasNext()) {
            res.add((float)cur.next());
        }

        return res;
    }

    public ArrayList<Float> getAngerData() {
        ArrayList<Float> res= new ArrayList<>();
        Iterator cur = this.angers.iterator();
        while(cur.hasNext()) {
            res.add((float)cur.next());
        }
        return res;
    }


}