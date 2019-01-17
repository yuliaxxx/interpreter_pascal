package ru.smandroid;

public class Assigns  extends Node{
    private Node left;
    private Node right;

    public Assigns(Node left, Node right){
        this.left = left;
        this.right = right;
    }

    public Node getLeft(){
        return left;
    }

    public Node getRight(){
        return right;
    }

    public String toString(){
        return String.format("Assigns (%s, %s)", left, right);
    }


}
