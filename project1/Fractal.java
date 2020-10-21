import java.util.Scanner;
import java.awt.*;

public class Fractal{
    public static Canvas drawing;
    public static String colorchoice;
  //public String shapeChoice;

  //public Fractal(String tempShapeChoice){
  //  shapeChoice = tempShapeChoice;

  //}Fractal constructor

  public static void triangleFractal(Triangle baseTriangle){
    System.out.println("Entered Triangle Fractal");
    double xpos = baseTriangle.getXPos();
    double ypos = baseTriangle.getYPos();
    double width = baseTriangle.getWidth();
    double height = baseTriangle.getHeight();
  //  double newXPos = xpos/1.25;
//    double newYPos = ypos/1.25;
    double newWidth = width/2;
    double newHeight = height/2;
    if (newWidth>10){
    //  baseTriangle.setPos(xpos-newWidth,ypos);
    //  baseTriangle.setHeight(newHeight);
  //    baseTriangle.setWidth(newWidth);
      Triangle triangle1 = new Triangle((xpos-newWidth),ypos,newWidth,newHeight);
      drawing.drawShape(triangle1);
      triangleFractal(triangle1);
      //baseTriangle.setPos(xpos+(width + newWidth),ypos);
      Triangle triangle2 = new Triangle((xpos+width),ypos,newWidth,newHeight);
      drawing.drawShape(triangle2);
      triangleFractal(triangle2);
      //baseTriangle.setPos(xpos-(width/2)-(newWidth/2),ypos+height);
      Triangle triangle3 = new Triangle((xpos+(newWidth/2)),(ypos-height),newWidth,newHeight);
      drawing.drawShape(triangle3);
      triangleFractal(triangle3);
      //triangleFractal(baseTriangle);
    }
  }//triangleFractal

  public static void main(String[] args){
    drawing = new Canvas(800,800);
    Scanner scan = new Scanner(System.in);
    System.out.println("What fractal shape would you like? Choices: Triangle Circle Rectangle");
    String shapeChoice = scan.nextLine();
    if (shapeChoice.equals("Triangle")){
      System.out.println("Enter Triangle Height: ");
      double height = scan.nextDouble();
      System.out.println(height);
      System.out.println("Enter Triangle Width: ");
      double width = scan.nextDouble();
      System.out.println(width);
      System.out.println("Enter Triangle X-Coordinate: ");
      double xcor = scan.nextDouble();
      System.out.println(xcor);
      System.out.println("Enter Triangle Y-Coordinate: ");
      double ycor = scan.nextDouble();
      System.out.println(ycor);
      scan.nextLine();
      //System.out.println("Enter Triangle Color: ");
      //colorchoice = scan.nextLine();
      Triangle firstTriangle = new Triangle(xcor,ycor,width,height);
      firstTriangle.setColor(Color.RED);
      drawing.drawShape(firstTriangle);
      Fractal.triangleFractal(firstTriangle);
    }//Triangle if

  }//main

}//public class Fractal
