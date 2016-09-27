package upvotes;

import java.io.*;
import java.util.Arrays;

public class Solution {
  
  public static int[][] getSeqs(int length, int[] input) {
    int[][] output = new int[2][];
    int[] nonDec = new int[length];
    int[] nonInc = new int[length];
    Arrays.fill(nonDec, -1);
    Arrays.fill(nonInc, -1);
    
    int nonDecPrev = input[0];
    int nonDecStartingIndex = 0;
    int nonDecPrevIndex = 0;
    int nonDecJ = 0;
    int nonIncPrev = input[0];
    int nonIncStartingIndex = 0;
    int nonIncPrevIndex = 0;
    int nonIncJ = 0;
    for(int i = 1; i < length; i++) {
      if(input[i] < nonDecPrev) {
        int endIndex = nonDecPrevIndex;
        if(endIndex != nonDecStartingIndex) {
          nonDec[nonDecJ] = nonDecStartingIndex;
          nonDecJ ++ ;
          nonDec[nonDecJ] = endIndex;
          nonDecJ ++ ;
        }
        nonDecStartingIndex = i;
      }
      nonDecPrevIndex = i;
      nonDecPrev = input[i];
      
      if(input[i] > nonIncPrev) {
        int endIndex = nonIncPrevIndex;
        if(endIndex != nonIncStartingIndex) {
          nonInc[nonIncJ] = nonIncStartingIndex;
          nonIncJ ++ ;
          nonInc[nonIncJ] = endIndex;
          nonIncJ ++ ;
        }
        nonIncStartingIndex = i;
      }
      nonIncPrevIndex = i;
      nonIncPrev = input[i];
    }
    if(nonDecStartingIndex != length - 1) {
      nonDec[nonDecJ] = nonDecStartingIndex;
      nonDecJ ++ ;
      nonDec[nonDecJ] = length - 1;
    }
    if(nonIncStartingIndex != length - 1) {
      nonInc[nonIncJ] = nonIncStartingIndex;
      nonIncJ ++ ;
      nonInc[nonIncJ] = length - 1;
    }
    
    output[0] = nonDec;
    output[1] = nonInc;
    return output;
  }
  
  public static int getNumSubranges(int start, int end) {
    // 1 + .... + end - start
    int n = end - start;
    return n*(n + 1)/2;
  }

  public static void main(String args[] ) throws Exception {
    StringBufferInputStream s = new StringBufferInputStream("10 3\n1 2 3 1 1 1 2 3 1 1");
    System.setIn(s);

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String line = br.readLine();

    // Processing input
    int numDays = Integer.parseInt(line.split(" ")[0]);
    int window = Integer.parseInt(line.split(" ")[1]);
    if(numDays == 0 || window == 0 || window > numDays)
      return;

    line = br.readLine();
    int[] upvotes = new int[numDays];
    String[] tokens = line.split(" ");
    for(int i = 0; i < numDays; i++)
      upvotes[i] = Integer.parseInt(tokens[i]);
    // End of processing input
    
    // Array with alternating start-end index of sequences
    int[][] temp = getSeqs(numDays, upvotes);
    int[] nonDec = temp[0];
    int[] nonInc = temp[1];

    /*
    for(int i = 0; i < numDays; i++) {
      System.out.print(nonDec[i] + " ");
    }
    System.out.println();
    
    for(int i = 0; i < numDays; i++) {
      System.out.print(nonInc[i] + " ");
    }
    System.out.println();
    */
    int nonDecStart = 0;
    int nonIncStart = 0;
    
    for(int i = 0 ; i < numDays - window + 1; i++) {
      int wStart = i;
      int wEnd = i + window - 1;
      // Get number of nonDec subranges
      int numNonDec = 0;
      int j = nonDecStart;
      while(true) {
        if(j == numDays || nonDec[j] == -1)
          break;
        int rStart = nonDec[j];
        int rEnd = nonDec[j + 1];
        if(rEnd > wStart && rStart < wEnd) {
          int start = Math.max(wStart, rStart);
          int end = Math.min(wEnd, rEnd);
          numNonDec += getNumSubranges(start, end);
        } else if(rStart > wEnd) {
          break;
        } else if(wStart > rEnd) {
          nonDecStart = j + 2;
        }
        j += 2;
      }
      // Get number of nonInc subranges
      int numNonInc = 0;
      j = nonIncStart;
      while(true) {
        if(j == numDays || nonInc[j] == -1)
          break;
        int rStart = nonInc[j];
        int rEnd = nonInc[j + 1];
        if(rEnd > wStart && rStart < wEnd) {
          int start = Math.max(wStart, rStart);
          int end = Math.min(wEnd, rEnd);
          numNonInc += getNumSubranges(start, end);
        } else if(rStart > wEnd) {
          break;
        } else if(wStart > rEnd) {
          nonIncStart = j + 2;
        }   
        j += 2;
      }
      //System.out.println(numNonDec + "," + numNonInc);
      System.out.println(numNonDec - numNonInc);
    }
  }

}