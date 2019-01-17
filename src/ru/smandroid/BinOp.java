package ru.smandroid;

public class BinOp  extends Node {

    private Node left;
    private Token op;
    private Node right;

    public BinOp(Node left, Token op, Node right) {
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public Node getLeft() {
        return left;
    }

    public Node getRight() {
        return right;
    }

    public Token getOp() {
        return op;
    }

    public String toString() {
        return String.format("BinOp%s (%s, %s)", op.getValue(), left, right);
    }
}

