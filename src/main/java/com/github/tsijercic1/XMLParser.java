package com.github.tsijercic1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class XMLParser {
    private String document = "";

    public XMLParser(String filepath) throws IOException {
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            StringBuilder builder = new StringBuilder();
            int t=0;
            while(true){
                try {
                    if ((t = reader.read()) == -1) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                builder.append((char)t);
            }
            document = builder.toString();
            reader.close();
    }
    public Node getDocumentRootNode() throws InvalidXMLException {
        document=excludeTags(document,"<!--","-->");
        document=excludeTags(document,"<?","?>");
        document = document.trim();
        String[] lines = document.split("\n");
        document = "";
        for (String line : lines) {
            if (!line.isEmpty())
                document = document.concat(line + "\n");
        }
        return createStructure(document);
    }

    private boolean isContentTag(String tag){
        return tag.matches("<_Content>.*");
    }

    private Map<String, String> getAttributesFromTag(String s, int afterLastCharacterIndex) {
        Map<String, String> map = new TreeMap<>();
        ArrayList<String> attributes = new ArrayList<>();
        int numberOfQuotes = 0;
        int startOfAttribute = afterLastCharacterIndex;
        for(int i=afterLastCharacterIndex;i<s.length();i++){
            char element = s.charAt(i);
            if(element == '\"'){
                numberOfQuotes++;
                if(numberOfQuotes%2==0){
                    attributes.add(s.substring(startOfAttribute,i+1).trim());
                    startOfAttribute=i+1;
                }
            }
        }
        for( String attribute : attributes){
            String [] pair = attribute.split("=");
            if(pair.length == 2){
                int beginValue = pair[1].indexOf('\"');
                int endValue = pair[1].lastIndexOf('\"');
                map.put(pair[0].trim(),pair[1].substring(beginValue+1,endValue));
            }
        }
        return map;
    }

    private Node createStructure(String document) throws InvalidXMLException {
        ArrayList<String> tags;
        Node node = null;
        tags = parseDocumentIntoArrayList(document);
        TagTree tagTree = new TagTree(tags);
        Tag root = tagTree.getRoot();
        node = new Node();
        node.setName(root.getName());
        Map<String, String> attributes = getAttributesFromTag(root.getTag(), 1 + node.getName().length());
        attributes.forEach(node::addAttribute);
        ArrayList<Node> nodes = getChildNodes(root);
        for(Node element:nodes){
            node.addChildNode(element);
        }
        return node;
    }

    private ArrayList<Node> getChildNodes(Tag root) {
        ArrayList<Node> nodes = new ArrayList<>();
        for(Tag element : root.getChildren()){
            if(isContentTag(element.getTag())){
                Node node = new Node();
                node.setContent(element.getTag().replace("<_Content>","").replace("</_Content>",""));
                node.setName("");
                nodes.add(node);
            }else{
                Node node = new Node(element.getName());
                Map<String, String> attributes = getAttributesFromTag(element.getTag(), 1 + node.getName().length());
                attributes.forEach(node::addAttribute);
                ArrayList<Node> children = getChildNodes(element);
                children.forEach(node::addChildNode);
                nodes.add(node);
            }
        }

        return nodes;
    }

    private String excludeTags(String document, String leftBorder, String rightBorder) {
        StringBuilder result = new StringBuilder();
        int start=0;
        int end;
        end = document.indexOf(leftBorder);
        if(end == -1)return document;
        for(;;){
            end = document.indexOf(leftBorder,start);
            if(end==-1)break;
            for(int i=start;i<end;i++){
                result.append(document.charAt(i));

            }
            start = document.indexOf(rightBorder,end+4);
        }
        result = new StringBuilder(result.toString().concat(document.substring(start + 3)));

        return result.toString();
    }


    private ArrayList<String> parseDocumentIntoArrayList(String document) throws InvalidXMLException {
        ArrayList<String> dom = new ArrayList<>();
        boolean isInQoutation = false;
        boolean isInTag = false;
        boolean isContent = false;
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < document.length(); i++)
        {
            char element = document.charAt(i);
            if (element == '<')
            {
                if (isContent)
                {
                    temp = new StringBuilder(temp.toString().trim());
                    dom.add("<_Content>"+temp+"</_Content>");
                    temp = new StringBuilder();
                    isContent = false;
                }
                if (isInTag && !isInQoutation)
                    throw new InvalidXMLException("Invalid XML => \"<\" after \"<\"");
                isInTag = true;
                temp.append(element);
            }
            else if (element == '>' && !isInQoutation)
            {
                if (!isInTag)
                    throw new InvalidXMLException("Invalid XML => loose \">\" at: "+document.substring(i));
                isInTag = false;
                temp.append(element);
                temp = new StringBuilder(temp.toString().trim());
                dom.add(temp.toString());
                temp = new StringBuilder();
            }
            else if (element == '\"')
            {
                isInQoutation = !isInQoutation;
                temp.append(element);
            }
            else
            {
                if (isInTag)
                {
                    if (element == '\n')
                    {
                        temp.append(" ");
                    }
                    else
                    {
                        temp.append(element);
                    }
                }
                else if (isContent)
                {
                    temp.append(element);
                }
                else if (element != ' ' && element != '\n' && element != '\r' && element != '\t')
                {
                    isContent = true;
                    temp.append(element);
                }
            }
        }
        if (isInQoutation)
            throw new InvalidXMLException("Invalid XML Qoutation");
        return dom;
    }


}
