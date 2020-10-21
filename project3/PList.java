import java.util.Scanner;

public class PList implements PListInterface {

  private ObjectNode pointer;
  private ObjectNode trailer;
  private ObjectNode header;
  private int length = 0;

  public PList(){
    header = new ObjectNode(-1,null);
  } //default constructor

  public void append(Object item){
    trailer = header;
    pointer = header.getNext();
    while (pointer != null){
      trailer = pointer;
      pointer = pointer.getNext();
    }
    trailer.setNext(new ObjectNode(item,pointer));
    length += 1;
  }//add method

  public void print(){
    pointer = header.getNext();
    while (pointer != null){
      System.out.println(pointer.getData());
      pointer = pointer.getNext();
    }
  }//print method

  public void add(Object item){
    trailer = header;
    header.setNext(new ObjectNode(item,trailer.getNext()));
    length += 1;
  }//add method

  public void concatenate(PListInterface plist){
    pointer = header.getNext();
    while (pointer.getNext() != null){
      pointer = pointer.getNext();
    }
    ObjectNode lastNode = pointer;
    for (int i = 0; i < plist.length(); i++){
      lastNode.setNext(new ObjectNode(plist.get(i),null));
      lastNode = lastNode.getNext();
    }
  }//concatenate method

  public void delete(int index){
    int temp = 0;
    ObjectNode tail = header;
    ObjectNode middle = header.getNext();
    ObjectNode leader = middle.getNext();
    if (index < length){
      while (temp != index){
        tail = tail.getNext();
        middle = middle.getNext();
        leader = leader.getNext();
        temp += 1;
      }
      tail.setNext(leader);
    }
  }//delete method

  public Object get(int index){
    int temp = 0;
    pointer = header.getNext();
    if (index < length){
      while (temp != index){
        pointer = pointer.getNext();
        temp += 1;
      }
      return (pointer.getData());
    }
    else {return ("Incorrect Index");}
  }//get method

  public void insert(Object item, int index){
    int temp = 0;
    pointer = header.getNext();
    trailer = header;
    if (index <= length){
      while (temp != index){
        pointer = pointer.getNext();
        trailer = trailer.getNext();
        temp += 1;
      }
      trailer.setNext(new ObjectNode(item, pointer));
    }
    else if (index > length){
      while (pointer != null){
        pointer = pointer.getNext();
        trailer = trailer.getNext();
      }
      trailer.setNext(new ObjectNode(item, pointer));
    }
  }//insert method

  public int length(){
    return (length);
  }//length method

  public void remove(Object item){
    trailer = header;
    ObjectNode middle = header.getNext();
    ObjectNode leader = middle.getNext();
    while (middle != null && !middle.getData().equals(item)){
      trailer = trailer.getNext();
      middle = middle.getNext();
      leader = leader.getNext();
    }
    trailer.setNext(leader);
  }//remove method

  public void sort(){
    for (int i = 1; i < length; i++){
      ObjectNode follower = header;
      trailer = header.getNext();
      pointer = trailer.getNext();
      for (int j = 1; j<length;j++){
        if(trailer.getData().toString().compareTo(pointer.getData().toString()) > 0){
          ObjectNode tempNode = pointer;
          pointer = trailer;
          trailer = tempNode;
          follower.setNext(trailer);
          pointer.setNext(tempNode.getNext());
          trailer.setNext(pointer);
          }
          if(pointer.getNext()!= null){
            trailer = trailer.getNext();
            pointer = pointer.getNext();
            follower = follower.getNext();
          }
        }//check if first node is greater than next node
      }
  }//sort method with bubble sort

  public void create(){
    header.setNext(null);
    length = 0;
  }//create method

  public String toString(){
    String returnString = "";
    pointer = header.getNext();
    while (pointer != null){
      returnString += ("" + pointer.getData() + " ");
      pointer = pointer.getNext();
    }
    return returnString;
  }//toString method

  public boolean equals(PList enteredList){
    boolean equal = true;
    for (int i = 0; i < length-1; i++){
      if (get(i).toString().compareTo(enteredList.get(i).toString()) != 0){
        equal = false;
      }
    }
    return equal;
  }//equals method

  public static void main(String[] args){
    //beginning of concatenate test
    System.out.println("Here is a quick test of the concatenate method: ");
    PList testList1 = new PList();
    PList testList2 = new PList();
    testList1.add(1);
    testList1.add("dog");
    testList1.add(34.1);
    testList1.add("house");
    testList1.add(19);
    testList2.add(9);
    testList2.add("carnival");
    testList2.add(32.89);
    testList2.add("rose");
    testList2.add(204);
    System.out.println("First list: ");
    testList1.print();
    System.out.println("");
    System.out.println("Second list: ");
    testList2.print();
    System.out.println("");
    testList1.concatenate(testList2);
    System.out.println("Second list concatenated onto first list: ");
    testList1.print();
    System.out.println("");
    System.out.println("End of concatenation test.");
    System.out.println("");
    //end of concatenate test
    //mixed type testing
    System.out.println("Mixed Type Testing: ");
    System.out.println("This is the original list: ");
    testList1.create();
    testList1.add(12.56);
    testList1.add("Banana");
    testList1.add(35);
    testList1.add("Apple");
    testList1.print();
    System.out.println("");
    System.out.println("Removing 35 from list: ");
    testList1.remove(35);
    testList1.print();
    System.out.println("");
    System.out.println("Inserting Dog into index 2: ");
    testList1.insert("Dog",2);
    testList1.print();
    System.out.println("");
    System.out.println("Sorted List: ");
    testList1.sort();
    testList1.print();
    System.out.println("");
    System.out.println("End of mixed typed testing.");
    System.out.println("");
    //end of mixed type testing
    PList headedList = new PList();
    Scanner scan = new Scanner(System.in);
    boolean quit = false;
    String itemChoice;
    int indexChoice;
    while (quit != true){
      System.out.println("What would you like to do? (enter 'quit' to exit program)");
      String userChoice = scan.nextLine();
      switch (userChoice) {
        case "add":
          System.out.print("Enter item: ");
          itemChoice = scan.nextLine();
          headedList.add(itemChoice);
          break;
        case "append":
          System.out.print("Enter item: ");
          itemChoice = scan.nextLine();
          headedList.append(itemChoice);
          break;
        case "create":
          headedList.create();
          break;
        case "delete":
          System.out.print("Enter index: ");
          indexChoice = scan.nextInt();
          headedList.delete(indexChoice);
          scan.nextLine();
          break;
        case "get":
          System.out.print("Enter index: ");
          indexChoice = scan.nextInt();
          System.out.println(headedList.get(indexChoice));
          scan.nextLine();
          break;
        case "insert":
          System.out.print("Enter item: ");
          itemChoice = scan.nextLine();
          System.out.print("Enter index: ");
          indexChoice = scan.nextInt();
          headedList.insert(itemChoice,indexChoice);
          scan.nextLine();
          break;
        case "length":
          System.out.println(headedList.length());
          break;
        case "print":
          headedList.print();
          break;
        case "remove":
          System.out.print("Enter item: ");
          itemChoice = scan.nextLine();
          headedList.remove(itemChoice);
          break;
        case "sort":
          headedList.sort();
          break;
        case "quit":
          System.out.println("Goodbye.");
          quit = true;
          break;
      }//switch statement
    }
  }//main

}//Public class PList
