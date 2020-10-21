import java.util.Scanner;
import java.awt.*;

public class Rectangle{

  public double xPosition;
  public double yPosition;
  public double width;
  public double height;
  public Color shapeColor;

  public Rectangle(double tempXPos,double tempYPos,double tempWidth,double tempheight){
    xPosition = tempXPos;
    yPosition = tempYPos;
    width = tempWidth;
    height = tempheight;

  }//Rectangle constructor

  public double calculatePerimeter(){
    double perimeter = width + width + height + height;
    return perimeter;

  }//perimeter

  public double calculateArea(){
    double area = (width * height);
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

}//Rectangle Class
