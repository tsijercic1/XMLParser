package com.github.tsijercic1;

import java.util.*;

public class Node {
    private String name;
    private String content;
    private Map<String,String> attributes;
    private ArrayList<Node> childNodes;

    public Node() {
        attributes = new TreeMap<>();
        childNodes = new ArrayList<>();
    }

    public Node(String name) {
        this();
        this.name = name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public void addChildNode(Node node) {
        childNodes.add(node);
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public ArrayList<Node> getChildNodes() {
        return childNodes;
    }
    public ArrayList<Node> getChildNodes(String name){
        ArrayList<Node> nodes = new ArrayList<>(childNodes);
        nodes.removeIf(node -> !node.getName().equals(name));
        return nodes;
    }
    public Set<String> getAttributeKeys(){
        return attributes.keySet();
    }
    public Collection<String> getAttributeValues(){
        return attributes.values();
    }

}
