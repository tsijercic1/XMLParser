package com.github.tsijercic1;

import java.util.*;

public class Node {
    private String name;
    private String content;
    private Map<String,String> attributes;
    private ArrayList<Node> childNodes;

    Node() {
        attributes = new TreeMap<>();
        childNodes = new ArrayList<>();
    }

    Node(String name) {
        this();
        this.name = name;
    }

    void setName(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    private String getActualContent(){
        return content;
    }

    public String getContent() {
        if(getName().isEmpty()) return content;
        StringBuilder textContent = new StringBuilder();
        for (Node child : childNodes) {
            if(child.getName().equals("")){
                textContent.append(child.getActualContent());
            }
        }
        return textContent.toString();
    }

    void setContent(String content) {
        this.content = content;
    }

    void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    void addChildNode(Node node) {
        childNodes.add(node);
    }

    public Map<String, String> getAttributes() {
        return new HashMap<>(attributes);
    }

    public ArrayList<Node> getChildNodes() {
        ArrayList<Node> nodes = new ArrayList<>(childNodes);
        nodes.removeIf(node -> node.getName().equals(""));
        return nodes;
    }

    public ArrayList<Node> getChildNodesWithContentAsChildNodes(){
        return new ArrayList<>(childNodes);
    }

    public ArrayList<Node> getChildNodes(String name){
        ArrayList<Node> nodes = new ArrayList<>(childNodes);
        nodes.removeIf(node -> !node.getName().equals(name) || name.equals(""));
        return nodes;
    }

    public Node getChildNode(String name){
        return getNthChildNode(name, 0);
    }

    public Node getNthChildNode(String name, int i){
        ArrayList<Node> nodes = new ArrayList<>(childNodes);
        nodes.removeIf(node -> !node.getName().equals(name) || name.equals(""));
        if(nodes.size()<=i)return null;
        return nodes.get(i);
    }

    public Set<String> getAttributeKeys(){
        return new TreeSet<>(attributes.keySet());
    }

    public Collection<String> getAttributeValues(){
        return new ArrayList<>(attributes.values());
    }


}
