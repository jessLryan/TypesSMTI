package com.mycompany.typessmti;

import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jessica
 */
public class Agent implements Comparable<Agent> {

    private int number;
    private List<List<Integer>> prefList;

    public Agent(int no) {
        number = no;
    }
    
    public int getNumber() {
        return number;
    }

    public void setPrefList(List<List<Integer>> list) {
        prefList = list;
    }

    public List<List<Integer>> getPrefList() {
        return prefList;
    }

    @Override
    public int compareTo(Agent a2) {
        List<List<Integer>> prefList2 = a2.getPrefList();
        int prefListLength = prefList.size();
        int h2prefListLength = prefList2.size();
        for (int i = 0;; i++) {
           // System.out.println("i "+i);
            if (i == prefListLength && h2prefListLength > i ) {
                return -1;
            }
            if (i == h2prefListLength && prefListLength > i ) {
                return 1;
            }
            if (i == h2prefListLength && i == prefListLength) {
                return 0;
            }
            for (int p = 0;; p++) {
               // System.out.println("p "+p);
                if (p == prefList.get(i).size() && prefList2.get(i).size() > p ) {
                    return -1;
                }
                if (p == prefList2.get(i).size() && prefList.get(i).size() > p ) {
                    return 1;
                }
                if (p == prefList2.get(i).size() && p == prefList.get(i).size()) {
                    return 0;
                }
                if (prefList.get(i).get(p) > prefList2.get(i).get(p)) {
                    return 1;
                }
                if (prefList2.get(i).get(p) > prefList.get(i).get(p)) {
                    return -1;
                }
            }
            
        }
        
    }
    
    
    public boolean areAgentsRankedIdentically(Agent r1,Agent r2) {
        for (List<Integer> tie:prefList) {
            int posAgent1 = tie.indexOf(r1.getNumber());
            int posAgent2 = tie.indexOf(r2.getNumber());
            if (posAgent1>=0&&posAgent2>=0&&posAgent1==posAgent2) {
                return true;
            }
            if (posAgent1!=posAgent2) {
                return false;
            }
        }
        return true;
    }
}
