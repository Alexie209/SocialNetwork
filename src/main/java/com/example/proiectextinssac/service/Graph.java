package com.example.proiectextinssac.service;


import com.example.proiectextinssac.domain.User;

import java.util.*;

public class Graph {

    private final Map<Long, List<Long>> adjVertices = new HashMap<>();
    private List<Long> keyList;

    public Graph(Service service) {
        createGraph(service);
    }

    public void addVertex(Long id) {
        adjVertices.putIfAbsent(id, new ArrayList<>());
    }

    public List<Long> getAdjVertices(Long id) {
        return adjVertices.get(id);
    }

    public void addEdge(Long id1, Long id2) {
        adjVertices.get(id1).add(id2);
        adjVertices.get(id2).add(id1);
    }

    public void createGraph(Service service) {
        Iterable<User> users = service.printAll();
        keyList = new ArrayList<>();
        for (User user : users) {
            keyList.add(user.getId());
        }

        for (User user : users) {
            addVertex(user.getId());
        }

        for (User user : users) {
            List<User> friends = service.getFriends(user);
            for (User user1 : friends) {
                addEdge(user.getId(), user1.getId());
            }
        }
    }

    public Set<Long> breadthFirstTraversal(Long root) {
        Set<Long> visited = new LinkedHashSet<>();
        Queue<Long> queue = new LinkedList<>();
        queue.add(root);
        visited.add(root);
        while (!queue.isEmpty()) {
            Long vertex = queue.poll();
            for (Long v : getAdjVertices(vertex)) {
                if (!visited.contains(v)) {
                    visited.add(v);
                    queue.add(v);
                }
            }
        }
        return visited;
    }

    public Set<Set<Long>> poolOfConnectedComponents() {
        Set<Set<Long>> stringSet = new HashSet<>();
        ArrayList<String> stringBuilder = new ArrayList<>();
        for (Long key : keyList) {
            stringBuilder.add(key.toString());
        }
        for (String s : stringBuilder) {
            stringSet.add(this.breadthFirstTraversal(Long.parseLong(s)));
        }
        return stringSet;
    }

    public int getNrOfConnectedComponents() {
        Set<Set<Long>> stringSet = poolOfConnectedComponents();
        return stringSet.size();
    }

    public Set<Long> getLargestConnectedComponent() {
        int max = 0;
        Set<Long> setMax = new HashSet<>();
        Set<Set<Long>> stringSet = poolOfConnectedComponents();
        for (Set<Long> set : stringSet) {
            if (set.size() > max) {
                max = set.size();
                setMax = set;
            }
        }
        return setMax;
    }
}
