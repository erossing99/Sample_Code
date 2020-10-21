import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;

public class Database{

  public String query;
  public Table[] tables = new Table[5];
  public int currentIndex = 0;
  public PrintWriter writer;
  public Table newTable;
  public String[] colNames;
  public String[] colTypes;
  public String[] colValues;
  public File chosenFile;
  public Scanner fileScan;

  public String getQuery(String querychoice){
    query = querychoice;
    InterpretedQuery interpretedQuery = QueryEvaluator.evaluateQuery(query);
    QueryType queryType = interpretedQuery.getQueryType();
    if (queryType == QueryType.CREATE_STATEMENT){
      if (currentIndex == tables.length){   //resizing table array
        Table[] tempTable = new Table[2 * tables.length];
        for (int i = 0; i < tables.length; i++){
          tempTable[i] = tables[i];
        }
        tables = tempTable;
      }
      if(currentIndex != 0){
        for (Table eachtable : tables){
          if (eachtable != null){
          if (interpretedQuery.getTableName().equals(eachtable.getTableName())){
            System.out.println(interpretedQuery.getTableName() + " already exists in the database.");
            return ("");
        }
      }
      }
    }
    boolean uniqueColumns = true;
    String errorColumn = "null";
    String[] columnNames1 = interpretedQuery.getColumnNames();
    String[] columnNames2 = interpretedQuery.getColumnNames();
    for (int index = 0; index < columnNames1.length ; index++){
      String name = columnNames1[index];
      for (int otherIndex = 0 ; otherIndex < columnNames2.length ; otherIndex++){
        String otherName = columnNames2[otherIndex];
        if (index != otherIndex){
          if(otherName.equals(name)){
            uniqueColumns = false;
            errorColumn = otherName;
                }
              }
            }
          }
          if (uniqueColumns == false){
            System.out.println(errorColumn + " is not a unique column name.");
            return ("");
          }
          else{
            boolean correctTypes = true;
            String incorrectType = "null";
            for (String type : interpretedQuery.getColumnTypes()){
              if (type.equals("int")){
                correctTypes = true;
              }
              else if (type.equals("double")){
                correctTypes = true;
              }
              else if (type.equals("boolean")){
                correctTypes = true;
              }
              else if (type.equals("String")){
                correctTypes = true;
              }
              else{
                correctTypes = false;
                incorrectType = type;
                System.out.println(incorrectType + " is not a valid type.");
                return("");
              }
            }
            Table newTable = new Table(interpretedQuery.getTableName(), interpretedQuery.getColumnNames(),interpretedQuery.getColumnTypes());
            tables[currentIndex] = newTable;
            currentIndex += 1;
            return("");
            }
          }

    else if (queryType == QueryType.INSERT_STATEMENT){
        for (Table eachTable : tables){
          if (eachTable != null){
            if (eachTable.getTableName().equals(interpretedQuery.getTableName())){
              eachTable.addRow(interpretedQuery.getInsertValues());
            }
          }
        }
      }//end of insert

    else if (queryType == QueryType.LOAD_STATEMENT){
        try{
        chosenFile = new File(interpretedQuery.getFileName()+".csv"); //auto adds ".csv" to entered .db file name
        fileScan = new Scanner(chosenFile);
        }
        catch(Exception e){}
        for(Table eachTable : tables){
          if (eachTable != null){
          if(eachTable.getTableName().equals(interpretedQuery.getFileName().replace(".db",""))){
            System.out.println("This table already exists in the database");
            return ("");
            }
          }
        }
        int scanNumber = 0;
        while (fileScan.hasNextLine()){
          if (scanNumber == 0){
            String line = fileScan.nextLine();
            colNames = line.split(",");
            scanNumber += 1;
          }
          else if (scanNumber == 1){
            String line = fileScan.nextLine();
            colTypes = line.split(",");
            scanNumber += 1;
            newTable = new Table(interpretedQuery.getFileName().replace(".db",""),colNames,colTypes);//replace removes .db from tables name
            tables[currentIndex] = newTable;
          }
          else{
            String line = fileScan.nextLine();
            //System.out.println(line);
            String[] colValues = line.split(",");
            newTable.addRow(colValues);
          }
        }
        return ("");
      }//end of load

    else if (queryType == QueryType.STORE_STATEMENT){
      try {
      writer = new PrintWriter(new File(interpretedQuery.getTableName()+ ".db.csv"));
      }
      catch(Exception e){}
      for (Table eachTable : tables){
          if (eachTable != null){
            if (interpretedQuery.getTableName().equals(eachTable.getTableName())){
              int length = 1;
              for (String columnName : eachTable.getColumnNames()){
                if (length != eachTable.getColumnNames().length){
                  writer.print(columnName + ", ");
                  length += 1;
                }
                else{
                  writer.println(columnName);
                }
              }
              length = 1;
              for (String columnType : eachTable.getColumnTypes()){
                if (length != eachTable.getColumnNames().length){
                  writer.print(columnType + ", ");
                  length += 1;
                }
                else{
                  writer.print(columnType + "\n");
                }
              }
              for (Row row : eachTable.getRows()){
                length = 1;
                if (row != null){
                  for (Object val : row.getRowValues()){
                    if (length != eachTable.getColumnNames().length){
                      writer.print(val + ", ");
                      length += 1;
                    }
                    else{
                      writer.print(val + "\n"); //if end of row values it prints a new line
                    }
                  }
                }
              }
            }
          }//table writing starts here
        }
        writer.close();
        return "";
      }//end of store

    else if (queryType == QueryType.PRINT_STATEMENT){
        for (Table eachTable : tables){
          if (eachTable != null){
            if (eachTable.getTableName().equals(interpretedQuery.getTableName())){
              eachTable.printColumnNames(); //utilizes print methods
              eachTable.printColumnTypes(); //to print the column names,
              eachTable.printTableValues(); //column types, and values in order
            }
          }
        }
      }//end of print

    else if (queryType == QueryType.SELECT_STATEMENT){
        for (Table eachTable : tables){
          if (eachTable != null){
            if (eachTable.getTableName().equals(interpretedQuery.getTableName())){
              //Check if column names entered are in that table, then iterate through
              // the conditional column and when the condition is met, add that row
              // to maybe an array, then print out the column,types, and all row values
              int correctColName = 0;
              for(String columnName : interpretedQuery.getColumnNames()){
                for (String otherColumnName : eachTable.getColumnNames()){
                  if (otherColumnName.strip().equals(columnName.strip())){
                    correctColName += 1;
                  }
                }
              }
              if (correctColName == interpretedQuery.getColumnNames().length){
                eachTable.selectValues(interpretedQuery.getColumnNames(),interpretedQuery.getConditional());
              }
              else{
                System.out.println("Column Names do not match the names in the given table.");
                return ("");
              }
            }
          }
        }
      }//end of select

    else if (queryType == QueryType.EXIT_STATEMENT){
        System.out.println("Goodbye");
        return ("quit");
      }//end of exit

  return("");//made this a string method to easily exit method when query wasn't correct
  }//getQuery

}//Database
