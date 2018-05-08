package com.example.pengfeisong.facialsyc.model;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by pengfeisong on 4/30/18.
 */

public class FacialInfo {
    private LinkedList<Float> joys;
    private LinkedList<Float> angers;
    private LinkedList<Float> browRaise;
    private LinkedList<Float> attentions;
    private LinkedList<Float> smiles;

    private int dataNums;



    public FacialInfo(int size) {
        joys = new LinkedList<Float>();
        angers = new LinkedList<Float>();
        browRaise = new LinkedList<Float>();
        attentions = new LinkedList<Float>();
        smiles = new LinkedList<Float>();
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

    public void addBrowRaise(float br) {
        if(browRaise.size() < dataNums) {
            browRaise.add(br);
        } else {
            browRaise.removeFirst();
            browRaise.add(br);
        }
    }

    public void addAttention(float at) {
        if(attentions.size() < dataNums) {
            attentions.add(at);
        } else {
            attentions.removeFirst();
            attentions.add(at);
        }
    }

    public void addSmile(float s) {
        if(smiles.size() < dataNums) {
            smiles.add(s);
        } else {
            smiles.removeFirst();
            smiles.add(s);
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

    public ArrayList<Float> getBrowRaiseData() {
        ArrayList<Float> res= new ArrayList<>();
        Iterator cur = this.browRaise.iterator();
        while(cur.hasNext()) {
            res.add((float)cur.next());
        }
        return res;
    }

    public ArrayList<Float> getAttentionData() {
        ArrayList<Float> res= new ArrayList<>();
        Iterator cur = this.attentions.iterator();
        while(cur.hasNext()) {
            res.add((float)cur.next());
        }
        return res;
    }

    public ArrayList<Float> getSmilesData() {
        ArrayList<Float> res= new ArrayList<>();
        Iterator cur = this.smiles.iterator();
        while(cur.hasNext()) {
            res.add((float)cur.next());
        }
        return res;
    }

}