package lexer;

import static control.Control.ConLexer.dump;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import lexer.Token.Kind;
import util.Todo;

public class Lexer
{
  String fname; // the input file name to be compiled
  InputStream fstream; // input stream for the above file
  PushbackInputStream push; // pushBack input stream for the above file;
  
  
  public Lexer(String fname, InputStream fstream)
  {
    this.fname = fname;
    this.fstream = fstream;
    this.push = new PushbackInputStream(fstream);
  }
  
  // exercise7: Read the MiniJava specification and 
  // study carefully the forming rules for each kind of token
  private boolean isInteger(int c)
  {
	  if ('0' <= c && c <= '9')
	  {
		  return true;
	  }
	  else
	  {
		  return false;
	  }
  }
  
  private boolean isCharacter(int c)
  {
	  if (('a' <= c && c <= 'z') ||('A' <= c && c <= 'Z'))
	  {
		  return true;
	  }
	  else
	  {
		  return false;
	  }
  }

  private boolean gotoNextToken() throws Exception
  {
	  int c = 0;
	  while ((c = this.push.read()) != ' ')
	  {
		  ;
	  }
	  if (' ' == c || '\t' == c || '\n' == c)
	  {
		  this.push.unread(c);
		  return true;
	  }
	  else
	  {
		  return false;
	  }
  }
  
  // When called, return the next token (refer to the code "Token.java")
  // from the input stream.
  // Return TOKEN_EOF when reaching the end of the input stream.
  private Token nextTokenInternal() throws Exception	
  {
    // int c = this.fstream.read(); it will contain a bug when we read a '('
	// it do not contain the character pushed back;	  
    int c = this.push.read();
	if (-1 == c)
    	// The value for "lineNum" is now "null",
    	// you should modify this to an appropriate
    	// line number for the "EOF" token.
    	return new Token(Kind.TOKEN_EOF, 21);

    // skip all kinds of "blanks"
    while (' ' == c || '\t' == c || '\n' == c) {
    	c = this.fstream.read();
    }
    if (-1 == c)
    	return new Token(Kind.TOKEN_EOF, 21, "EOF");

    switch (c) {
    case '+':
    	return new Token(Kind.TOKEN_ADD, 13, "+");
    case '-':
    	return new Token(Kind.TOKEN_SUB, 57, "-");
    case '*':
    	return new Token(Kind.TOKEN_TIMES, 60, "*");
    case '(':
    	return new Token(Kind.TOKEN_LPAREN, 34, "(");
    case ')':
    	return new Token(Kind.TOKEN_RPAREN, 53, ")");
    case '{':
    	return new Token(Kind.TOKEN_LBRACE, 31, "{");
    case '}':
    	return new Token(Kind.TOKEN_RBRACE, 49, "}");
    case '[':
    	return new Token(Kind.TOKEN_LBRACK, 32, "[");
    case ']':
    	return new Token(Kind.TOKEN_RBRACK, 50, "]");
    case '.':
    	return new Token(Kind.TOKEN_DOT, 19, ".");
    case ';':
    	return new Token(Kind.TOKEN_SEMI, 54, ";");
    case '<':
    	return new Token(Kind.TOKEN_LT, 35, "<");
    case '=':
    	return new Token(Kind.TOKEN_ASSIGN, 15, "=");
    case '&':
    	c = this.push.read();
    	if (c == '&')
    	{
    		return new Token(Kind.TOKEN_AND,14, "&&");
    	}
    	else
    	{
    		this.gotoNextToken();
    		System.out.println("Error: Bad argument &...");
    		return null;
    	}
    case '0':
    	c = this.push.read();
    	if (c == ' ')
    	{
    		return new Token(Kind.TOKEN_NUM, 41, "0");
    	}
    	else
    	{
    		this.gotoNextToken();
    		System.out.println("Error: Bad argument 0...");
    		return null;
    	}
    default:
    	// Lab 1, exercise 2: supply missing code to
    	// lex other kinds of tokens.
    	// Hint: think carefully about the basic
    	// data structure and algorithms. The code
    	// is not that much and may be less than 50 lines. If you
    	// find you are writing a lot of code, you
    	// are on the wrong way.
    	while (this.isInteger(c))
    	{
    		String temp = "";
    		
    		while (this.isInteger(c))
        	{
        		temp = temp + (char)c;
        		c = this.push.read();
        	}
        	if (' ' == c || '\t' == c || '\n' == c)
        	{
        		return new Token(Kind.TOKEN_NUM, 41, temp);
        	}
        	else if ('(' == c || ')' == c || '{' == c || '}' == c || '[' == c || ']' == c || '.' == c || ';' == c)
        	{
        		this.push.unread(c);
        		return new Token(Kind.TOKEN_NUM, 41, temp);
        	}
        	else
        	{
        		this.gotoNextToken();
        		System.out.println("Error: Bad argument 1...");
        		return null;
        	}
    	}
    	while (this.isCharacter(c))
    	{
    		String temp = "";
    		while (this.isInteger(c)|| this.isCharacter(c)|| c == '_')
        	{
        		temp = temp + (char)c;
        		c = this.push.read();
        	}
        	if (' ' == c || '\t' == c || '\n' == c)
        	{
        		return new Token(Kind.TOKEN_ID, 28, temp);
        	}
        	else if ('(' == c || ')' == c || '{' == c || '}' == c || '[' == c || ']' == c || '.' == c || ';' == c)
        	{
        		this.push.unread(c);
        		return new Token(Kind.TOKEN_ID, 28, temp);
        	}
        	else
        	{
        		this.gotoNextToken();
        		System.out.println("Error: Bad argument A...");
        		return null;
        	}
    	}
    	return null;
    }
  }

  public Token nextToken()
  {
    Token t = null;

    try {
      t = this.nextTokenInternal();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    if (dump)
      System.out.println(t.toString());
    return t;
  }
}
