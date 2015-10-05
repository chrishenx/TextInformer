package model;

import java.util.HashMap;

/**
 * Clase para obtener informacion sobre una texto.
 * @author Christian González León
 * @version 1.0
 */
public class TextProcessor {
  private String text;
  private static double LOG_OF_2;
  
  static {
    LOG_OF_2 = Math.log(2);
  }
  
  public TextProcessor(String text) {
    this.text = text;
  }
  
  public HashMap<Character, Integer> countSymbols() {
    HashMap<Character, Integer> symbolsCount = new HashMap<>();
    for (int i = 0; i < text.length(); i++) {
      Character symbol = text.charAt(i);
      if (symbolsCount.containsKey(symbol)) {
        symbolsCount.put(symbol, symbolsCount.get(symbol) + 1);
      } else {
        symbolsCount.put(symbol, 1);
      }
    }
    return symbolsCount;
  }
  
  public TextInformation calculateInformation() {
    HashMap<Character, Integer> symbolsCount = countSymbols();
    int information = 0;
    double entropy = 0;
    double numSymbols = text.length();
    for (Integer symbolOcurrences : symbolsCount.values()) {
      double probability = symbolOcurrences / numSymbols; 
      if (probability > 0) {
        information += Math.ceil(log2(1 / probability));
        entropy += log2(1 / probability) * probability;
      }
    }
    return new TextInformation(information, entropy);
  }
  
  public static double log2(double val) {
    return Math.log(val) / LOG_OF_2;
  }

}
