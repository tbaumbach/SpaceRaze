/*
 * Created on 2005-jan-03
 */
package sr.general;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WMPABOD
 *
 * Improved version of the StringTokenizer class which can handle empty tokens
 */
public class StringTokenizerPlusPlus {
  private List<String> tokens = new ArrayList<String>();
  private int currentToken = 0;

  public StringTokenizerPlusPlus(String inData, String delimiter) {

    if (inData.length() > 0) {

      int tmpIndex = inData.indexOf(delimiter);
      int currentIndex = 0;
      while (tmpIndex > -1) {

        if (currentIndex != tmpIndex) {
          tokens.add(inData.substring(currentIndex, tmpIndex));
        } else {
          tokens.add("");
        }
//        currentIndex = tmpIndex + 1;
        currentIndex = tmpIndex + delimiter.length();

        if (currentIndex == inData.length()) {
            tmpIndex = -1;
        } else {
          tmpIndex = inData.substring(currentIndex).indexOf(delimiter);
          if (tmpIndex > -1){
            tmpIndex = tmpIndex + currentIndex;
          }
        }
      }

      if (currentIndex == inData.length()) {
        tokens.add("");
      } else{
        tokens.add(inData.substring(currentIndex));
      }
    }
  }

  public String nextToken() {
    String returnString;
    returnString = (String) tokens.get(currentToken);
    currentToken++;
    return returnString;

  }

  public int countTokens() {
    return tokens.size() - currentToken;
  }

  public boolean hasMoreTokens(){
    return currentToken < tokens.size();
  }

  public void reset(){
    currentToken = 0;
  }
}
