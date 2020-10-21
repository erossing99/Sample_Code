#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "deltas.h"

int main(int argc, char *argv[]){

  if(argc < 3){
    printf("usage: %s <format> <filename> <height>\n",argv[0]);
    printf(" <format> is one of\n");
    printf(" text : text ints are in the given filename\n");
    printf(" int  : binary ints are in the given filename\n");
    return 1;
  }

  char *format = argv[1];
  char *fileName = argv[2];
  char *maxHeight = argv[3];

  int max_height = atoi(maxHeight);

  int length = -1;
  int *deltaArr = NULL; //pointer to my array to be filled by data from files

  if (strcmp(format, "text") == 0){
    printf("Reading text format\n");
    deltaArr = read_text_deltas(fileName, &length);
  }
  else if (strcmp(format, "int") == 0){
    printf("Reading binary int format\n");
    deltaArr = read_int_deltas(fileName, &length);
  }
  else{
    printf("Unrecognized Command\n");
    return -1;
  }

  print_graph(deltaArr, length, max_height);

  free(deltaArr);
  return 1;

}
