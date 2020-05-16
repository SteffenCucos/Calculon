package calculon;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CharacterIterator implements Iterator<Character> {

	private int index = 0;
	private String string;
	
	public CharacterIterator(final String string) {
		  if (string == null) {
			  throw new NullPointerException();
		  }
		    
		  this.string = string;
	}

    public boolean hasNext() {
      return index < string.length();
    }

    public Character next() {
      if (!hasNext()) {
    	  throw new NoSuchElementException();
      }
        
      return string.charAt(index++);
    }
    
    public Character peek() {
    	if (!hasNext()) {
      	  throw new NoSuchElementException();
        }
    	
    	return string.charAt(index);
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
}