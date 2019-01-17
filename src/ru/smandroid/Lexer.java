package ru.smandroid;

public class Lexer {
    private String text;
    private int pos = 0;
    private Character currentCharacter;

    public Lexer(String text){
        this.text = text;
        currentCharacter = text.charAt(pos);

    }

    public void forward(){
        pos +=1;
        if(pos > text.length() - 1){
            currentCharacter = null;
        }
        else {
            currentCharacter = text.charAt(pos);
        }
    }


    public void skip(){
        while ((currentCharacter !=null) && Character.isSpaceChar(currentCharacter)){
            forward();
        }
    }

    public Character peek(){
        Character character;
        int peek_pos = pos + 1;
        if (peek_pos > text.length() - 1){
            character = null;
        }
        else {
            character = text.charAt(peek_pos);
        }
        return character;
    }


    public Token number(){
        String result = "";
        while ((currentCharacter !=null) && Character.isDigit(currentCharacter) | currentCharacter == '.'){
            result += currentCharacter;
            forward();
        }
        if(result.contains(".") && result.length() > 1){
            return new Token (TokenType.FLOAT, result);
        }
        else{
            return new Token(TokenType.INTEGER, result);
        }
    }

    public Token symbol(){
        String result = "";
        while ((currentCharacter !=null) && Character.isLetterOrDigit(currentCharacter)){
            result += currentCharacter;
            forward();
        }
        if (result.equals("BEGIN")){
            return new Token(TokenType.BEGIN, result);
        }
        else if(result.equals("END")){
            return new Token(TokenType.END, result);
        }
        else {
            return new Token(TokenType.VAR,result);
        }
    }

    public Token nextToken() throws Exception {
        while (currentCharacter != null){
            if (currentCharacter == '.'){
                forward();
                return new Token(TokenType.DOT, ".");
            }

            if(Character.isSpaceChar(currentCharacter)){
                skip();
                continue;
            }

            if(Character.isDigit(currentCharacter)){
                return number();
            }
            if(Character.isLetterOrDigit(currentCharacter)){
                return symbol();
            }
            if(currentCharacter == '+'){
                forward();
                return new Token(TokenType.PLUS, "+");
            }

            if(currentCharacter == '-'){
                forward();
                return new Token(TokenType.MINUS, "-");
            }

            if(currentCharacter == '*'){
                forward();
                return new Token(TokenType.MUL, "*");
            }

            if(currentCharacter == '/'){
                forward();
                return new Token(TokenType.DIV, "/");
            }
            if(currentCharacter == '('){
                forward();
                return new Token(TokenType.LPAREN, "(");
            }
            if(currentCharacter == ')'){
                forward();
                return new Token(TokenType.RPAREN, ")");
            }
            if(currentCharacter == '^'){
                forward();
                return new Token(TokenType.POW, ")");
            }
            if (currentCharacter == ';'){
                forward();
                return new Token(TokenType.SEMI, ";");
            }
            if (currentCharacter == ':' && peek() == '='){
                forward();
                forward();
                return new Token(TokenType.ASSIGN, ":=");
            }

            throw new Exception("unknown token!");
        }
        return new Token(TokenType.EOF, null);
    }
}
