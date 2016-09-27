package upvotes;

import java.io.*;

public class Solution {

  public static int[][] getNonDecSeq(int length, int[] input) {
    int[][] output = new int[2][];
    int[] starts = new int[length];
    int[] ends = new int[length];
    for(int i = 0; i < length; i++) {
      starts[i] = -1;
      ends[i] = -1;
    }

    int prev = input[0];
    int startingIndex = 0;
    int prevIndex = 0;
    for(int i = 1; i < length; i++) {
      if(input[i] < prev) {
        starts[i] = -1;
        int endIndex = prevIndex;
        if(endIndex != startingIndex) {
          starts[startingIndex] = startingIndex;
          ends[startingIndex] = endIndex;
        }
        startingIndex = i;
      } else {
        starts[i] = startingIndex;
      }
      prevIndex = i;
      prev = input[i];
    }
    if(startingIndex != length - 1) {
      starts[startingIndex] = startingIndex;
      ends[startingIndex] = length - 1;
    }
    
    output[0] = starts;
    output[1] = ends;

    return output;
  }

  public static int[][] getNonIncSeq(int length, int[] input) {
    int[][] output = new int[2][];
    int[] starts = new int[length];
    int[] ends = new int[length];
    for(int i = 0; i < length; i++) {
      starts[i] = -1;
      ends[i] = -1;
    }

    int prev = input[0];
    int startingIndex = 0;
    int prevIndex = 0;
    for(int i = 1; i < length; i++) {
      if(input[i] > prev) {
        starts[i] = -1;
        int endIndex = prevIndex;
        if(endIndex != startingIndex) {
          starts[startingIndex] = startingIndex;
          ends[startingIndex] = endIndex;
        }
        startingIndex = i;
      } else {
        starts[i] = startingIndex;
      }
      prevIndex = i;
      prev = input[i];
    }
    if(startingIndex != length - 1) {
      starts[startingIndex] = startingIndex;
      ends[startingIndex] = length - 1;
    }
    
    output[0] = starts;
    output[1] = ends;

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

    // Array filled with starting index of sequence if sequence exists
    int[][] temp = getNonDecSeq(numDays, upvotes);
    int[] nonDecStarts = temp[0];
    int[] nonDecEnds = temp[1];
    temp = getNonIncSeq(numDays, upvotes);
    int[] nonIncStarts = temp[0];
    int[] nonIncEnds = temp[1];

    /*
    for(int i = 0; i < numDays; i++) {
      System.out.print(nonDecStarts[i] + " ");
    }
    System.out.println();
    
    for(int i = 0; i < numDays; i++) {
      System.out.print(nonDecEnds[i] + " ");
    }
    System.out.println();

    for(int i = 0; i < numDays; i++) {
      System.out.print(nonIncStarts[i] + " ");
    }
    System.out.println();
    
    for(int i = 0; i < numDays; i++) {
      System.out.print(nonIncEnds[i] + " ");
    }
    System.out.println();
    System.out.println();
    */

    // Compute value for first window
    int prevNonDec = 0;
    int prevTemp = nonDecStarts[0];
    for(int i = 1; i < window; i ++) {
      int currTemp = nonDecStarts[i];
      if(prevTemp != -1 && prevTemp == currTemp)
        prevNonDec += i - prevTemp;
      prevTemp = currTemp;
    }
    
    
    int prevNonInc = 0;
    prevTemp = nonIncStarts[0];
    for(int i = 1; i < window; i ++) {
      int currTemp = nonIncStarts[i];
      if(prevTemp != -1 && prevTemp == currTemp)
        prevNonInc += i - prevTemp;
      prevTemp = currTemp;
    }
    
    int prevNonDecStart = nonDecStarts[0];
    int prevNonDecEnd = nonDecStarts[window - 1];
    int prevNonIncStart = nonIncStarts[0];
    int prevNonIncEnd  = nonIncStarts[window - 1];
    
    // window moving from left to right
    for(int i = 1 ; i < numDays - window + 1; i++) {
      int wStart = i;
      int wEnd = i + window - 1;
      int currNonDecStart = nonDecStarts[wStart];
      int currNonDecEnd = nonDecStarts[wEnd];
      int currNonIncStart = nonIncStarts[wStart];
      int currNonIncEnd = nonIncStarts[wEnd];
      //System.out.println(prevNonDec + "," + prevNonInc);
      System.out.println(prevNonDec - prevNonInc);
      // Reduce number of nonDecs 
      if(currNonDecStart == prevNonDecStart && currNonDecStart != -1)
        prevNonDec -= Math.min(wEnd - 1, nonDecEnds[currNonDecStart]) - wStart + 1;
      // Increase number of nonDecs
      if(currNonDecEnd == prevNonDecEnd && currNonDecEnd != -1)
        prevNonDec += wEnd - Math.max(wStart, currNonDecEnd);
      // Reduce number of nonIncs
      if(currNonIncStart == prevNonIncStart && currNonIncStart != -1)
        prevNonInc -= Math.min(wEnd - 1, nonIncEnds[currNonIncStart]) - wStart + 1;
      // Increase number of nonIncs
      if(currNonIncEnd == prevNonIncEnd && currNonIncEnd != -1)
        prevNonInc += wEnd - Math.max(wStart, currNonIncEnd);
        
      prevNonDecStart = currNonDecStart;
      prevNonDecEnd = currNonDecEnd;
      prevNonIncStart = currNonIncStart;
      prevNonIncEnd = currNonIncEnd;
    }
    //System.out.println(prevNonDec + "," + prevNonInc);
    System.out.println(prevNonDec - prevNonInc);
  }
}