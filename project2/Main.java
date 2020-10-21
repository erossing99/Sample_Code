import java.util.Scanner;

public class Main{

  public static Database data;

  public static void main(String[] args){
    Scanner scan = new Scanner(System.in);
    String quit = "";
    data = new Database();
    while (quit != "quit"){
      System.out.println("What would you like to do?");
      String query = scan.nextLine();
      quit = data.getQuery(query);
    }
  }

}
