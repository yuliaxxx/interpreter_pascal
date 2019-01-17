package ru.smandroid;

public class Var extends Node{
    private Token token;
    private String value;

    public Var(Token token){
        this.token = token;
        this.value = token.getValue();
    }

    public Token getToken(){
        return token;
    }

    public String getValue(){
        return value;
    }

    public String toString(){
        return String.format("Var (%s)", token);
    }

}
