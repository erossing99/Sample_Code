import java.util.Scanner;
import java.awt.*;

public class Circle{

  public double xPosition;
  public double yPosition;
  public double radius;
  public Color shapeColor;

  public Circle(double tempxPosition,double tempyPosition,double tempRadius){
    xPosition = tempxPosition;
    yPosition = tempyPosition;
    radius = tempRadius;

  }//circle constructor

  public double calculatePerimeter(){
    double perimeter = 2 * 3.14 * radius;
    return perimeter;

  }//perimeter

  public double calculateArea(){
    double area = 3.14 * radius * radius;
    return area;

  }//area

  public void setColor(Color colorChoice){
    shapeColor = colorChoice;

  }//color

  public void setPos(double newxPos,double newyPos){
    xPosition = newxPos;
    yPosition = newyPos;

  }//set Position

  public void setRadius(double newRadius){
    radius = newRadius;

  }//set Radius

  public Color getColor(){
    return shapeColor;

  }//get Color

  public double getXPos(){
    return xPosition;

  }//get xPos

  public double getYPos(){
    return yPosition;

  }//get yPos

  public double getRadius(){
    return radius;

  }//get Radius

} //public class Circle
