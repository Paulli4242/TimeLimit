package org.ccffee.utils.tag;

public class IllegalSyntaxException extends Exception {
    private final int ln;
    private final int col;
    private final Type type;
    @SuppressWarnings("unused")
    public IllegalSyntaxException(){
        type = Type.UNKNOWN;
        ln=-1;
        col=-1;
    }
    public IllegalSyntaxException(String s, Type type){
        super(s);
        this.type = type;
        ln=-1;
        col=-1;
    }
    public IllegalSyntaxException(String s, Type type, int ln, int col){
        super(s);
        this.type = type;
        this.ln = ln;
        this.col = col;
    }

    @SuppressWarnings("unused")
    public int getLine() {
        return ln;
    }

    public int getColumn() {
        return col;
    }

    public Type getType() {
        return type;
    }

    public enum Type{
        UNKNOWN,
        WRONG_CLOSE_TAG,
    }
}
