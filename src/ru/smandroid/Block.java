package ru.smandroid;

import java.util.ArrayList;

public class Block extends Node {

    public ArrayList<Node> innerBlocks;

    public Block(){
        this.innerBlocks = new ArrayList();
    }

}
