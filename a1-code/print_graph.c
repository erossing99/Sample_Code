#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "deltas.h"

void print_graph(int *data, int len, int max_height){   //works

    int max;
    int min;
    for (int i = 0; i < len; i++){ //finding max and min values in data
      if(i == 0){ //base case
        max = data[i];
        min = data[i];
      }
      if (max < data[i]){  //is new number greater than previous max?
        max = data[i];
      }
      if (min > data[i]){ //is new number less than previous min?
        min = data[i];
      }
    }//for loop

    int range = max - min;
    float units_per_height = range / (float)max_height;

    //start of graph printing

    printf("length: %d\n", len);
    printf("min: %d\n", min);
    printf("max: %d\n", max);
    printf("range: %d\n", range);
    printf("max_height: %d\n", max_height);
    printf("units_per_height: %.2f\n", units_per_height);

    printf("     ");
    for(int i = 0; i < len; i++){    //drawing upper axis
      if (i == 0){
        printf("+");
      }
      else if ((i % 5) == 0){
        printf("+");
      }
      else{
        printf("-");
      }
    }
    printf("\n");

    for (int i = max_height; i >= 0; i--){    //drawing body of graph
      int cutoff = min + (i * units_per_height);
      printf("%3d |", cutoff);
      for (int j = 0; j < len; j++){  //draws X's
        if(data[j] >= cutoff){
          printf("X");
        }
        else{
          printf(" ");
        }
      }
      printf("\n");
    }

    printf("     ");

    int amountOfPlus = 0;

    for(int i = 0; i < len; i++){    //drawing lower axis
      if (i == 0){
        printf("+");
        amountOfPlus++;
      }
      else if ((i % 5) == 0){
        printf("+");
        amountOfPlus++;
      }
      else{
        printf("-");
      }
    }
    printf("\n");

    printf("     ");
    int bottomNum = 0;
    for (int i = 0; i < amountOfPlus; i++){  //drawing bottom axis numbers
      printf("%-5d", bottomNum);
      bottomNum += 5;
    }

    printf("\n");

}
// Prints a graph of the values in data which is an array of integers
// that has len elements. The max_height argument is the height in
// character rows that the maximum number data[] should be.  A sample
// graph is as follows:
//
// length: 50
// min: 13
// max: 996
// range: 983
// max_height: 10
// units_per_height: 98.30
//      +----+----+----+----+----+----+----+----+----+----
// 996 |                X
// 897 |       X        X X            X
// 799 |  X    X X   X  X X    X       X                X
// 701 |  XX   X X   X  XXX    X      XX   XXX    X   X XX
// 602 |  XX   X X  XX  XXX X  X      XX  XXXX    XX  X XX
// 504 |  XX   XXX  XX  XXX XX X      XXX XXXX XX XX  X XX
// 406 |  XX X XXX XXXX XXX XX X  XXX XXX XXXXXXXXXX  X XX
// 307 | XXX X XXX XXXXXXXXXXX X XXXX XXXXXXXXXXXXXXX X XX
// 209 | XXX XXXXXXXXXXXXXXXXX XXXXXX XXXXXXXXXXXXXXXXX XX
// 111 | XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//  13 |XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
//      +----+----+----+----+----+----+----+----+----+----
//      0    5    10   15   20   25   30   35   40   45
