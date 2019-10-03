package com.github.tsijercic1;

import java.util.ArrayList;

class Tag{
    private String tag;
    private ArrayList<Tag> children;

    Tag(String tag) {
        this.tag = tag;
        children = new ArrayList<>();
    }
    void addChild(Tag tag){
        children.add(tag);
    }

    String getTag() {
        return tag;
    }

    ArrayList<Tag> getChildren() {
        return children;
    }

    void setTag(String tag) {
        this.tag = tag;
    }

    void setChildren(ArrayList<Tag> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        String result = tag;
        for(Tag child : children){
            result+="\n\t"+child.toString("\t\t");
        }
        return result;
    }

    private String toString(String space){
        String result = tag;
        for(Tag child : children){
            result+="\n"+space+child.toString("\t"+space);
        }
        return result;
    }

    String getName() {
        StringBuilder name = new StringBuilder();
        for(int i=1; i<tag.length();i++){
            if(tag.charAt(i)==' '
                    || tag.charAt(i)=='\t'
                    || tag.charAt(i)=='\n'
                    || tag.charAt(i)=='\r'
                    || tag.charAt(i)=='>'
                    || tag.charAt(i)=='/'){
                break;
            }
            name.append(tag.charAt(i));
        }
        return name.toString();
    }
}
