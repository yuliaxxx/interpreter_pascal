package ru.smandroid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {

    private Lexer lexer;
    private Token currentToken;

    public Parser (Lexer lexer) throws Exception {
        this.lexer = lexer;
        currentToken = this.lexer.nextToken();
    }

    public void checkTokenType(TokenType type) throws Exception {
        if(currentToken.getType() == type){
            currentToken = lexer.nextToken();
        }
        else {
            throw new Exception("Parser error!");
        }

    }


    private Node programm() throws Exception {
        Node result = compState();
        checkTokenType(TokenType.DOT);
        return result;
    }


    private Node compState() throws Exception {
        checkTokenType(TokenType.BEGIN);
        ArrayList<Node> nodes = new ArrayList<Node>(statementList());
        checkTokenType(TokenType.END);
        Node result = new Block();
        for (Node node : nodes) {
            ((Block) result).innerBlocks.add(node);
        }
        return result;
    }

    private ArrayList<Node> statementList() throws Exception {
        Node node = statement();
        ArrayList<Node> result = new ArrayList<>();
        result.add(node);

        while (currentToken.getType().equals(TokenType.SEMI)){
            checkTokenType(TokenType.SEMI);
            result.add(statement());
        }
        if (currentToken.getType().equals(TokenType.VAR)){
            throw new Exception("Parser error!");
        }
        return result;

    }

    private Node statement() throws Exception {
        Node result;
        if(currentToken.getType().equals(TokenType.BEGIN)){
            result = compState();
        }
        else if (currentToken.getType().equals(TokenType.VAR)){
            result = assigned();
        }

        else {
            result = new Empty();
        }
        return result;
    }
    private Node assigned() throws Exception {
        Node left = var();
        Token token = currentToken;
        checkTokenType(TokenType.ASSIGN);
        Node right = expr();
        Node result = new Assigns(left,right);
        return result;

    }
    private Node var() throws Exception {
        Node result = new Var(currentToken);
        checkTokenType(TokenType.VAR);
        return result;
    }

    private Node empty(){
        return new Empty();
    }

    private Node factor() throws Exception {
        Token token = currentToken;
        if (token.getType().equals(TokenType.MINUS)){
            checkTokenType(TokenType.MINUS);
            return new UnaryOp(token, factor());
        }
        else if (token.getType().equals(TokenType.PLUS)){
            checkTokenType(TokenType.PLUS);
            return new UnaryOp(token, factor());
        }
        else if(token.getType().equals(TokenType.INTEGER)){
            checkTokenType(TokenType.INTEGER);
            return new Number(token);
        }
        else if(token.getType().equals(TokenType.FLOAT)){
            checkTokenType(TokenType.FLOAT);
            return new Number(token);
        }
        else if(token.getType().equals(TokenType.LPAREN)){
            checkTokenType(TokenType.LPAREN);
            Node result = expr();
            checkTokenType(TokenType.RPAREN);
            return result;
        }
        else {
            return var();
        }

    }

    public Node term() throws Exception {
        Node result = factor();
        List<TokenType> ops = Arrays.asList(TokenType.DIV, TokenType.MUL);
        while (ops.contains(currentToken.getType())){
            Token token = currentToken;
            if (token.getType() == TokenType.DIV){
                checkTokenType(TokenType.DIV);

            }
            else if (token.getType() == TokenType.MUL){
                checkTokenType(TokenType.MUL);

            }
            result = new BinOp(result, token, factor());
        }
        return result;
    }
    public Node expr() throws Exception {

        List<TokenType>  ops = Arrays.asList(TokenType.PLUS, TokenType.MINUS);
        Node result = term();
        while (ops.contains(currentToken.getType())){
            Token token = currentToken;
            if (token.getType() == TokenType.PLUS){
                checkTokenType(TokenType.PLUS);

            }
            if (token.getType() == TokenType.MINUS){
                checkTokenType(TokenType.MINUS);
            }
            result = new BinOp(result, token, term());
        }
        return result;
    }

    public Node parse() throws Exception{
        return programm();
    }

    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer("BEGIN bbb2 := 2.5; b := 3444; END.");
        Parser parser = new Parser(lexer);
        System.out.println(parser.parse());
    }

}
