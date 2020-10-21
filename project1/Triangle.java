import java.util.Scanner;
import java.awt.*;
import java.lang.Math;

public class Triangle{

  public double xPosition;
  public double yPosition;
  public double width;
  public double height;
  public Color shapeColor;

  public Triangle(double tempXPos,double tempYPos,double tempWidth,double tempheight){
    xPosition = tempXPos;
    yPosition = tempYPos;
    width = tempWidth;
    height = tempheight;
    System.out.println("Triangle Created");

  }//Triangle constructor

  public double calculatePerimeter(){
    double hypotenuse = Math.sqrt(((width/2)*(width/2)) + (height*height));
    double perimeter = hypotenuse + hypotenuse + width;
    return perimeter;

  }//perimeter

  public double calculateArea(){
    double area = (width * height)/2;
    return area;

  }//area

  public void setColor(Color colorChoice){
    shapeColor = colorChoice;

  }//color

  public void setPos(double newxPos,double newyPos){
    xPosition = newxPos;
    yPosition = newyPos;

  }//set Position

  public void setHeight(double newHeight){
    height =newHeight;

  }//set Height

  public void setWidth(double newWidth){
    width = newWidth;

  }//set width

  public Color getColor(){
    return shapeColor;

  }//get Color

  public double getXPos(){
    return xPosition;

  }//get xPos

  public double getYPos(){
    return yPosition;

  }//get yPos

  public double getHeight(){
    return height;

  }//get Height

  public double getWidth(){
    return width;

  }//get Width

}//Triangle Class
