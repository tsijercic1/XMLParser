package com.github.tsijercic1;

import java.util.ArrayList;

class TagTree {
    private ArrayList<String> tags;
    private Tag root;
    private int currentIndex = 0;
    public TagTree(ArrayList<String> tags) {
        this.tags = tags;
    }
    public Tag getRoot(){
        root = new Tag(tags.get(currentIndex));
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
    private ArrayList<Tag> getChildren(ArrayList<String> tags){
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
