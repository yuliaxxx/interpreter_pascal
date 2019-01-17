package ru.smandroid;

import java.util.HashMap;

public class ASTInterpreter implements NodeVisitor{

    private Parser parser;
    public HashMap pVars;

    public ASTInterpreter(Parser parser){
        this.parser = parser;
        pVars = new HashMap();
    }

    @Override
    public float visit(Node node) throws Exception {
        if(node.getClass().equals(BinOp.class)){
            return visitBinOp(node);
        }
        else if (node.getClass().equals(Number.class)){
            return visitNumber(node);
        }
        else if (node.getClass().equals(UnaryOp.class)){
            return visitUnaryOp(node);
        }
        else if (node.getClass().equals(Var.class)){
            return visitVar(node);
        }
        else if (node.getClass().equals(Block.class)){
            visitBlock(node);
        }
        else if (node.getClass().equals(Assigns.class)){
            visitAssing(node);
        }
        else if (node.getClass().equals(Empty.class)){
            visitEmpty(node);
        }
        return 0;
    }

    private float visitUnaryOp(Node node) throws Exception {
        System.out.println("visit UnaryOp");
        UnaryOp op = (UnaryOp) node;
        if(op.getToken().getType().equals(TokenType.MINUS)){
            return - visit(op.getExpr());
        }
        if(op.getToken().getType().equals(TokenType.PLUS)){
            return +visit(op.getExpr());
        }
        throw new Exception("UnaryOp error!");
    }


    public float visitBinOp(Node node) throws Exception {
        System.out.println("visit BinOp");
        BinOp binop = (BinOp) node;
        if (binop.getOp().getType().equals(TokenType.PLUS)){
            return visit(binop.getLeft())+visit(binop.getRight());
        }
        else if (binop.getOp().getType().equals(TokenType.MINUS)){
            return visit(binop.getLeft())-visit(binop.getRight());
        }
        else if (binop.getOp().getType().equals(TokenType.MUL)){
            return visit(binop.getLeft())*visit(binop.getRight());
        }
        else if (binop.getOp().getType().equals(TokenType.DIV)){
            return visit(binop.getLeft())/visit(binop.getRight());
        }
        throw new Exception("Interpreter error!");
    }

    public float visitNumber(Node node){
        System.out.println("visit Number");
        Number number = (Number) node;
        return Float.parseFloat(number.getToken().getValue());
    }

    public void visitBlock(Node node) throws Exception {
        System.out.println("visit Block");
        Block block = (Block) node;
        System.out.println(block.innerBlocks);
        for (Node innerBlock: block.innerBlocks){
            visit(innerBlock);
        }
    }

    public void visitAssing(Node node) throws Exception {
        System.out.println("visit Assing");
        Assigns assigns = (Assigns) node;
        Node varName = assigns.getLeft();
        String varN = ((Var) varName).getValue();
        if (pVars.containsKey(varN)){
            pVars.replace(varN,visit(assigns.getRight()));
        }
        else {
            pVars.put(varN,visit(assigns.getRight()));
        }

    }

    public float visitVar(Node node) throws Exception {
        System.out.println("visit Var");
        Var var = (Var) node;
        String varName = var.getToken().getValue();
        if(pVars.containsKey(varName)){
            float val = (float) pVars.get(varName);
            return val;
        }
        else {
            throw new Exception("Unknown var: " + varName);
        }

    }

    public void visitEmpty(Node node){
        System.out.println("visit Empty");
    }

    public float interpreter() throws Exception {
        Node three = parser.parse();
        System.out.println(three);
        return visit(three);
    }

    public static void main(String[] args) throws Exception {
        Lexer lexer = new Lexer("BEGIN x:= 2 + 3 * (2 + 3);  y:= 2 / 2 -- 2 + 3 * ((1 + 1) + (1 + 1)); END.");
        Parser parser = new Parser(lexer);
        ASTInterpreter interpreter = new ASTInterpreter(parser);
        System.out.println(interpreter.interpreter());
        System.out.println(interpreter.pVars);
    }
}

