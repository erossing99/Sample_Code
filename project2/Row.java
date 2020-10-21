import java.util.Arrays;

public class Row{

  public Object[] rowValues;
  public int[] rowInts;           //These arrays are used
  public boolean[] rowBooleans;   //to access the different
  public double[] rowDoubles;     //value types with each
  public String[] rowStrings;     //bit operator in the select query

  public Row(String[] values, String[] types){
    rowValues = new Object[values.length];
    rowInts = new int[values.length];
    rowDoubles = new double[values.length];
    rowBooleans = new boolean[values.length];
    rowStrings = new String[values.length];
    //int indexCount = 0;
    for (int indexCount = 0; indexCount < values.length; indexCount++){
      String eachType = types[indexCount].strip();
      if (eachType.equals("int")){
        Integer convertedInt = Integer.valueOf(values[indexCount].strip());
        rowValues[indexCount] = convertedInt;
        rowInts[indexCount] = convertedInt;
        rowDoubles[indexCount] = -1.0;
        rowBooleans[indexCount] = false;
        rowStrings[indexCount] = "-1";
      }
      else if (eachType.equals("double")){
        Double convertedDouble = Double.valueOf(values[indexCount].strip());
        rowValues[indexCount] = convertedDouble;
        rowDoubles[indexCount] = convertedDouble;
        rowInts[indexCount] = -1;
        rowBooleans[indexCount] = false;
        rowStrings[indexCount] = "-1";
      }
      else if (eachType.equals("boolean")){
        Boolean convertedBool = Boolean.valueOf(values[indexCount].strip());
        rowValues[indexCount] = convertedBool;
        rowBooleans[indexCount] = convertedBool;
        rowInts[indexCount] = -1;
        rowDoubles[indexCount] = -1.0;
        rowStrings[indexCount] = "-1";
      }
      else if (eachType.equals("String")){
        String convertedString = String.valueOf(values[indexCount].strip());
        rowValues[indexCount] = convertedString;
        rowStrings[indexCount] = convertedString;
        rowInts[indexCount] = -1;
        rowDoubles[indexCount] = -1.0;
        rowBooleans[indexCount] = false;
      }
    }//for loop
  }//Constructor

public String toString(){
  return Arrays.toString(rowValues);
}

  public Object[] getRowValues(){
    return rowValues;
  }//getRowValues

  public int[] getRowInts(){
    return rowInts;
  }

  public double[] getRowDoubles(){
    return rowDoubles;
  }

  public boolean[] getRowBooleans(){
    return rowBooleans;
  }

  public String[] getRowStrings(){
    return rowStrings;
  }

}//Row
