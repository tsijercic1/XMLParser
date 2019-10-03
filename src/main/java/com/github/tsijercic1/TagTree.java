package com.github.tsijercic1;

import java.util.ArrayList;

class TagTree {
    private ArrayList<String> tags;
    private int currentIndex = 0;

    TagTree(ArrayList<String> tags) {
        this.tags = tags;
    }
    Tag getRoot() throws InvalidXMLException {
        Tag root = new Tag(tags.get(currentIndex));
        currentIndex++;
        root.setChildren(getChildren(tags));
        currentIndex = 0;
        return root;
    }

    private boolean isOpenningTag(String tag){
        return tag.matches("<[a-zA-Z].*");
    }
    private boolean isContentTag(String tag){
        return tag.matches("<_Content>.*");
    }
    private boolean isClosingTag(String tag){
        return tag.matches("</[a-zA-Z].*");
    }
    private boolean isSelfClosingTag(String tag){
        return tag.matches("<[a-zA-Z]+?.*?/>");
    }
    private ArrayList<Tag> getChildren(ArrayList<String> tags) throws InvalidXMLException {
        ArrayList<Tag> children = new ArrayList<>();
        boolean isOpen = false;
        while(currentIndex < tags.size()){
            if(isSelfClosingTag(tags.get(currentIndex))){
                children.add(new Tag(tags.get(currentIndex)));
                currentIndex++;
            }else if(isContentTag(tags.get(currentIndex))){
                children.add(new Tag(tags.get(currentIndex)));
                currentIndex++;
            }else if(isOpenningTag(tags.get(currentIndex))){
                Tag tag = new Tag(tags.get(currentIndex));
                currentIndex++;
                tag.setChildren(getChildren(tags));
                children.add(tag);
                isOpen = true;
            }else if(isClosingTag(tags.get(currentIndex))){
                if(isOpen){
                    isOpen = false;
                    String name = children.get(children.size()-1).getName();
                    String closingTag = tags.get(currentIndex);
                    closingTag = closingTag.replace(name,"");
                    closingTag = closingTag.replace("</","");
                    closingTag = closingTag.replace(">","");
                    closingTag = closingTag.trim();
                    if(!closingTag.isEmpty())
                        throw new InvalidXMLException("Crossing tags: \"" + name + " not properly closed!\"");
                    currentIndex++;
                }
                else{
                    break;
                }
            }
        }
        return children;
    }

}
