/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.typessmti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author jessica
 */
public class TypesCalculator {

    public static void main(String[] args) {
        String input = args[0];
        BufferedReader agentsBufferedReader = getReader(input);
        List<ArrayList<Triple>> matrix = readToMatrix(agentsBufferedReader);
        List<Agent> men = extractMen(matrix);
        List<Agent> women = extractWomen(matrix);
        List<Agent> sortedWomen= sortAgents(women);
        List<Agent> sortedMen= sortAgents(men);
        List<ArrayList<Agent>> groupedMen = groupAgentsIdenticalPrefs(sortedMen);
        List<ArrayList<Agent>> groupedWomen = groupAgentsIdenticalPrefs(sortedWomen);
        List<ArrayList<Agent>> typedMen = findTypes(groupedMen, sortedWomen);
        List<ArrayList<Agent>> typedWomen = findTypes(groupedWomen, sortedMen);
        
        System.out.println("untyped women size " + women.size());
        System.out.println("typed women size " + typedWomen.size());
        System.out.println("untyped men size " + men.size());
        System.out.println("typed men size " + typedMen.size());
//        for (ArrayList<Agent> type : typedMen) {
//            if (type.size() > 1) {
//                System.out.println("type");
//                for (Triple<Integer, Agent> pair : type) {
//                    System.out.println(pair.getKey());
//                }
//            }
//        }
    }
    
    
    
    public static List<ArrayList<Agent>> findTypes(List<ArrayList<Agent>> groupedAgents, List<Agent> hospitals) {
        List<ArrayList<Agent>> groupedAgentsReturned = new ArrayList<>();
        for (ArrayList<Agent> group : groupedAgents) {
            ArrayList<Agent> groupCopy = (ArrayList<Agent>) group.clone();
            while (!groupCopy.isEmpty()) {
                ArrayList<Agent> type = new ArrayList<>();
                Agent agentOne = groupCopy.get(0);
                type.add(agentOne);
                groupCopy.remove(agentOne);
                for (int i = 0; i < groupCopy.size(); i++) {
                    Agent agentI = groupCopy.get(i);
                    if (areAgentsConsideredIdentical(agentOne, agentI, hospitals)) {
                        type.add(agentI);
                        groupCopy.remove(agentI);
                    }
                }
                groupedAgentsReturned.add(type);
            }
        }
        return groupedAgentsReturned;
    }
    
    public static boolean areAgentsConsideredIdentical(Agent r1, Agent r2, List<Agent> hospitals) {
        for (Agent p : hospitals) {
            if (!p.areAgentsRankedIdentically(r1, r2)) {
                return false;
            }
        }
        return true;
    }
    
    
    public static List<Agent> sortAgents(List<Agent> unsortedList) {
        Collections.sort(unsortedList);
        return unsortedList;
    }

    
    public static List<ArrayList<Agent>> groupAgentsIdenticalPrefs(List<Agent> agents) {
        List<ArrayList<Agent>> groupedAgents = new ArrayList<>();
        ArrayList<Agent> currentList = new ArrayList<>();
        currentList.add(agents.get(0));
        Agent currentAgent = agents.get(0);
        int pos = 1;
        while (pos < agents.size()) {
            Agent newAgent = agents.get(pos);
            if (currentAgent.compareTo(newAgent) == 0) {
                currentList.add(agents.get(pos));
                currentAgent = newAgent;
            } else {
                groupedAgents.add(currentList);
                currentList = new ArrayList();
                currentList.add(agents.get(pos));
                currentAgent = newAgent;
            }
            pos++;
            if (pos == agents.size()) {
                if (!currentList.isEmpty()) {
                    groupedAgents.add(currentList);
                }
            }

        }
        return groupedAgents;
    }
    
    
    
    public static List<Agent> extractWomen(List<ArrayList<Triple>> matrix) {
        List<Agent> women = new ArrayList<Agent>();
        for (int row = 0; row < matrix.size(); row++) {
            Agent woman = new Agent(row);
            List<Triple> rowList = matrix.get(row);
            List<List<Integer>> prefList = new ArrayList<>();
            Collections.sort(rowList, new Comparator<Triple>() {
                public int compare(Triple o1,
                        Triple o2) {
                    return Double.valueOf(o1.getValue()).compareTo(o2.getValue());
                }
            });
            List<Integer> tie= new ArrayList<>();
            tie.add(rowList.get(0).getColumn());
            double currentVal = rowList.get(0).getValue();
            for (int i=1;i<rowList.size();i++) {
                
                Triple t = rowList.get(i);
                if (t.getValue()==currentVal) {
                    tie.add(t.getColumn());
                }
                else {
                    prefList.add(tie);
                    tie = new ArrayList<>();
                    tie.add(t.getColumn());
                }
                currentVal = rowList.get(i).getValue();
                
            }
            woman.setPrefList(prefList);
            women.add(woman);
        }
        return women;
    }

    
    public static  List<Agent> extractMen(List<ArrayList<Triple>> matrix) {
        List<ArrayList<Triple>> revMatrix = getColumns(matrix);
        List<Agent> men = new ArrayList<Agent>();
        for (int row = 0; row < revMatrix.size(); row++) {
            Agent man = new Agent(row);
            
            List<Triple> rowList = revMatrix.get(row);
            List<List<Integer>> prefList = new ArrayList<>();
            Collections.sort(rowList, new Comparator<Triple>() {
                public int compare(Triple o1,
                        Triple o2) {
                    return Double.valueOf(o1.getValue()).compareTo(Double.valueOf(o2.getValue()));
                }
            });
            List<Integer> tie= new ArrayList<>();
            tie.add(rowList.get(0).getColumn());
            double currentVal = rowList.get(0).getValue();
            for (int i=1;i<rowList.size();i++) {
                
                Triple t = rowList.get(i);
                if (t.getValue()==currentVal) {
                    tie.add(t.getColumn());
                }
                else {
                    prefList.add(tie);
                    tie = new ArrayList<>();
                    tie.add(t.getColumn());
                }
                currentVal = rowList.get(i).getValue();
                
            }
            man.setPrefList(prefList);
            men.add(man);
        }
        return men;
    }
    
     public static List<ArrayList<Triple>> getColumns(List<ArrayList<Triple>> matrix) {
        int rows = matrix.size();
        int columns = matrix.get(0).size();
        List<ArrayList<Triple>> newMatrix = new ArrayList<>();
        for (int i = 0; i < columns; i++) {
            ArrayList column = new ArrayList<>();
            for (int j = 0; j < rows; j++) {
                column.add(matrix.get(j).get(i));
            }
            newMatrix.add(column);
        }
        return newMatrix;
    }
    

    public static List<ArrayList<Triple>> readToMatrix(BufferedReader reader) {
        List<ArrayList<Triple>> listOfLists = new ArrayList<>();
        try {
            int rows = Integer.parseInt(reader.readLine());
            int columns = Integer.parseInt(reader.readLine());
            String line = "";
            int row = 0;
            while ((line = reader.readLine()) != null) {
                ArrayList<Triple> list = new ArrayList<>();
                String[] splitArray = line.trim().split("\\s+");
                
                for (int col = 0; col < splitArray.length; col++) {
                    double value = Double.parseDouble(splitArray[col]);
                    list.add(new Triple(row,col,value));
                }
                listOfLists.add(list);
                row++;
            }
            return listOfLists;

        } catch (IOException e) {
            System.out.print("I/O Error");
            System.exit(0);
        }
        return null;

    }

    public static BufferedReader getReader(String name) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(name)));
        } catch (FileNotFoundException e) {
            System.out.print("File " + name + " cannot be found");
        }
        return in;
    }

}
