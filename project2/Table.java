public class Table{

  public Row[] rows = new Row[10];
  public String tableName;
  public String[] columnNames;
  public String[] columnTypes;
  public int currentIndex = 0;


  public Table(String tableName, String[] columnNames, String[] columnTypes){
    this.tableName = tableName;
    this.columnNames = columnNames;
    this.columnTypes = columnTypes;
  }//Table Constructor

  public String getTableName(){
    return tableName;
  }

  public String[] getColumnNames(){
    return columnNames;
    }

  public void printColumnNames(){
    int colCount = 0;
    for (String name : columnNames){
      if (colCount < (columnNames.length-1)){
        System.out.print(name.strip() + ", ");
        colCount += 1;
      }
      else{
        System.out.println(name.strip());
      }
    }
  }

  public void printColumnTypes(){
    int colCount = 0;
    for (String type : columnTypes){
      if (colCount < (columnTypes.length-1)){
        System.out.print(type.strip() + ", ");
        colCount += 1;
      }
      else{
        System.out.println(type.strip());
      }
    }
  }

  public void printTableValues(){
    for (Row eachRow : rows){
    if (eachRow != null){
      int rowCount = columnNames.length;
      int currentCount = 0;
      for (Object val : eachRow.getRowValues()){
        if (currentCount < (rowCount-1)){
          System.out.print("" + val + ", ");
          currentCount += 1;
          }
        else{
          System.out.println(val);
          }
        }
      }
    }
  }

  public String toString(){
   String s = "";
   if (rows[0] != null){
   for(int i = 0; i < rows.length; i++){
     s += rows[i].toString() + "\n";
    }
  }
   return s;
 }

  public String[] getColumnTypes(){
    return columnTypes;
  }

  public Row[] getRows(){
    return rows;
  }

  public void addRow(String[] values){
    if (currentIndex == rows.length){
      Row[] tempRows = new Row[2 * rows.length];
      for (int i = 0; i < rows.length; i++){
        tempRows[i] = rows[i];
      }
      rows = tempRows;
    }
    Row newRow = new Row(values,columnTypes);
    rows[currentIndex] = newRow;
    currentIndex += 1;
  }

  public void selectValues(String[] conditColumnNames,String conditional){
    String[] conditionalParts = conditional.split(" ");
    if (conditionalParts.length > 3){
      int extraWords = conditionalParts.length - 3;
      String newString = "";
      newString += conditionalParts[2];
      for (int i = 0; i < extraWords; i++){
        newString += " ";
        newString += conditionalParts[3+i];
      }
      conditionalParts[2] = newString;
      String[] tempArray = new String[3];
      for (int i = 0; i < 3 ; i++){
        tempArray[i] = conditionalParts[i];
      }
      conditionalParts = tempArray;
    }
    for (String parts : conditionalParts){
      parts = parts.strip();
    }
    String type = "null";
    int conditionalIndex = 10;
    for (int i = 0; i < columnNames.length; i++){
      if (conditionalParts[0].equals(columnNames[i].strip())){
        type = columnTypes[i].strip();
        conditionalIndex = i;
      }
    }
    if (type.equals("int")){ //Checks if the value type being entered is int
      for (int i = 0; i < conditColumnNames.length; i++){
        if (i != conditColumnNames.length-1){
          System.out.print(conditColumnNames[i].strip() + ", ");
        }
        else{
          System.out.println(conditColumnNames[i].strip());
        }
      }
      int conditionalColIndex = -1;
      for (String name : conditColumnNames){
        conditionalColIndex += 1;
        for (int i = 0; i < columnNames.length; i++){
          if(columnNames[i].strip().equals(name.strip())){
            if (i != conditColumnNames.length-1){
              System.out.print(columnTypes[i].strip() + ", ");
            }
            else{
              System.out.println(columnTypes[i].strip());
            }
          }
        }
      }
      int convertedValue = Integer.valueOf(conditionalParts[2]);
      if (conditionalParts[1].equals("<=")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowInts()[conditionalIndex] <= convertedValue){
            int conditionalColIndex1 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex1 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex1 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
      else if (conditionalParts[1].equals("<")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowInts()[conditionalIndex] < convertedValue){
            int conditionalColIndex2 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex2 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex2 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
      else if (conditionalParts[1].equals("=")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowInts()[conditionalIndex] == convertedValue){
            int conditionalColIndex3 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex3 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex3 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
      else if (conditionalParts[1].equals("!=")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowInts()[conditionalIndex] != convertedValue){
            int conditionalColIndex4 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex4 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex4 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
      else if (conditionalParts[1].equals(">")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowInts()[conditionalIndex] > convertedValue){
            int conditionalColIndex5 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex5 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex5 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
      else if (conditionalParts[1].equals(">=")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowInts()[conditionalIndex] >= convertedValue){
            int conditionalColIndex6 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex6 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex6 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
    }//end of "int" if statement

    else if (type.equals("boolean")){//Checks if the value type being entered is boolean
      for (int i = 0; i < conditColumnNames.length; i++){
        if (i != conditColumnNames.length-1){
          System.out.print(conditColumnNames[i].strip() + ", ");
        }
        else{
          System.out.println(conditColumnNames[i].strip());
        }
      }
      int conditionalColIndex = -1;
      for (String name : conditColumnNames){
        conditionalColIndex += 1;
        for (int i = 0; i < columnNames.length; i++){
          if(columnNames[i].strip().equals(name.strip())){
            if (conditionalColIndex != conditColumnNames.length-1){
              System.out.print(columnTypes[i].strip() + ", ");
            }
            else{
              System.out.println(columnTypes[i].strip());
            }
          }
        }
      }
      boolean convertedValue = Boolean.valueOf(conditionalParts[2]);
      if (conditionalParts[1].equals("=")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowBooleans()[conditionalIndex] == convertedValue){
            int conditionalColIndex1 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex1 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex1 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
      else if (conditionalParts[1].equals("!=")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowBooleans()[conditionalIndex] != convertedValue){
            int conditionalColIndex2 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex2 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex2 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
    }//end of "boolean" check

    else if (type.equals("double")){//Checks if the value type being entered is double
      for (int i = 0; i < conditColumnNames.length; i++){
        if (i != conditColumnNames.length-1){
          System.out.print(conditColumnNames[i].strip() + ", ");
        }
        else{
          System.out.println(conditColumnNames[i].strip());
        }
      }
      int conditionalColIndex = -1;
      for (String name : conditColumnNames){
        conditionalColIndex += 1;
        for (int i = 0; i < columnNames.length; i++){
          if(columnNames[i].strip().equals(name.strip())){
            if (conditionalColIndex != conditColumnNames.length-1){
              System.out.print(columnTypes[i].strip() + ", ");
            }
            else{
              System.out.println(columnTypes[i].strip());
            }
          }
        }
      }
      double convertedValue = Double.valueOf(conditionalParts[2]);
      if (conditionalParts[1].equals("<=")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowDoubles()[conditionalIndex] <= convertedValue){
            int conditionalColIndex1 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex1 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex1 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
      else if (conditionalParts[1].equals("<")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowDoubles()[conditionalIndex] < convertedValue){
            int conditionalColIndex2 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex2 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex2 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
      else if (conditionalParts[1].equals("=")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowDoubles()[conditionalIndex] == convertedValue){
            int conditionalColIndex3 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex3 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex3 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
      else if (conditionalParts[1].equals("!=")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowDoubles()[conditionalIndex] != convertedValue){
            int conditionalColIndex4 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex4 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex4 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
      else if (conditionalParts[1].equals(">")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowDoubles()[conditionalIndex] > convertedValue){
            int conditionalColIndex5 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex5 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex5 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
      else if (conditionalParts[1].equals(">=")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowDoubles()[conditionalIndex] >= convertedValue){
            int conditionalColIndex6 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex6 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex6 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
    }//end of double check

    else if (type.equals("String")){//Checks if the value type being entered is String
      for (int i = 0; i < conditColumnNames.length; i++){
        if (i != conditColumnNames.length-1){
          System.out.print(conditColumnNames[i].strip() + ", ");
        }
        else{
          System.out.println(conditColumnNames[i].strip());
        }
      }
      int conditionalColIndex = -1;
      for (String name : conditColumnNames){
        conditionalColIndex += 1;
        for (int i = 0; i < columnNames.length; i++){
          if(columnNames[i].strip().equals(name.strip())){
            if (conditColumnNames.length == 1){
              System.out.println(columnTypes[i].strip());
            }
            else if (conditionalColIndex != conditColumnNames.length-1){
              System.out.print(columnTypes[i].strip() + ", ");
            }
            else{
              System.out.println(columnTypes[i].strip());
            }
          }
        }
      }
      String convertedValue = conditionalParts[2];
      if (conditionalParts[1].equals("=")){
        for (Row eachRow : rows){
          if (eachRow != null){
            //System.out.println(eachRow.getRowStrings()[conditionalIndex]);
          if(eachRow.getRowStrings()[conditionalIndex].equals(convertedValue)){
            int conditionalColIndex1 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex1 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if (conditColumnNames.length == 1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else if(conditionalColIndex1 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
      else if (conditionalParts[1].equals("!=")){
        for (Row eachRow : rows){
          if (eachRow != null){
          if(eachRow.getRowStrings()[conditionalIndex].compareTo(convertedValue) != 0){
            int conditionalColIndex2 = -1;
            for (String name : conditColumnNames){
              conditionalColIndex2 += 1;
              for (int i = 0; i < columnNames.length; i++){
                if(name.equals(columnNames[i].strip())){
                  if(conditionalColIndex2 == conditColumnNames.length-1){
                    System.out.println(eachRow.getRowValues()[i]);
                  }
                  else{
                    System.out.print("" + eachRow.getRowValues()[i] + ", ");
                  }
                }
              }
            }
          }
          }
        }
      }
    }//end of String check
  }//selectValues method

}//Table
