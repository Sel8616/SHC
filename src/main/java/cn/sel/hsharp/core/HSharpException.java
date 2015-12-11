package cn.sel.hsharp.core;

public final class HSharpException extends Exception
{
    private static final long serialVersionUID = -7630783644275391978L;

    public HSharpException()
    {
        super("Unknown ERROR!");
    }

    public HSharpException(String errorMessage)
    {
        super(errorMessage);
    }
}