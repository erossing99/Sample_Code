
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <unistd.h>

int *read_text_deltas(char *fname, int *len){
  FILE *file = fopen(fname,"r");
  if (file == NULL){
    *len = -1;
    return NULL;
  }
  if(fscanf(file,"%*d") == EOF){ //checking to see if file is empty
    fclose(file);
    *len = -1;
    return NULL;
  }
  rewind(file);
  int isEnd = 0;
  int length = 0;
  while (isEnd != EOF) {  //checks to see if reached the end of file
    isEnd = fscanf(file,"%*d"); //reads one int at a time
    length++; //adding up the number of integers in the file
  }
  length--; //decreases by one to get rid of last empty line in file
  int *arr = malloc((length+1) * sizeof(int)); //allocating my int array
  rewind(file);
  fscanf(file,"%d", &arr[0]);
  int location = 0;
  while (location != length){    //inputing data to array
    fscanf(file,("%d"), &arr[location+1]);
    arr[location+1] = arr[location+1] + arr[location];  // adds previous index's integer to current index's integer
    location++;
  }
  fclose(file);
  *len = length;
  return arr;
}
// Reads integers in text delta format from the file named by fname
// and returns an array of them. The first integer in the file gives
// the starting point and subsequent integers are changes from the
// previous total.
//
// Opens the file with fopen() and scans through its contents using
// fscanf() counting how many text integers exist in it.  Then
// allocates an array of appropriate size using malloc(). Uses
// rewind() to go back to the beginning of the file then reads
// integers into the allocated array. Closes the file after reading
// all ints.  Returns a pointer to the allocated array.
//
// The argument len is a pointer to an integer which is set to the
// length of the array that is allocated by the function.
//
// If the file cannot be opened with fopen() or if there are no
// integers in the file, sets len to -1 and returns NULL.


int *read_int_deltas(char *fname, int *len){
  FILE *file = fopen(fname,"r");
  if (file == NULL){
    *len = -1;
    return NULL;
  }
  struct stat sb;
  int result = stat(fname, &sb);
  if (result == -1 || sb.st_size < sizeof(int)){
    *len = -1;
    fclose(file);
    return NULL;
  }
  int totalBytes = sb.st_size;
  int *arr = malloc(totalBytes+4);
  int totalInts = totalBytes / 4;
  fread(&arr[0], sizeof(int), 1, file);  //adds first integer to array
  int loopNumber = 0;
  while (loopNumber < totalInts){    //checks if number of loops is still less than the amount of integers to be read
    fread(&arr[loopNumber+1], sizeof(int), 1, file);
    arr[loopNumber+1] = arr[loopNumber] + arr[loopNumber+1];  // adds previous index's integer to current index's integer
    loopNumber++;
  }
  fclose(file);
  *len = totalInts;
  return arr;

}
// Reads integers in binary delta format from the file named by fname
// and returns an array of them.  The first integer in the file gives
// the starting point and subsequent integers are changes from the
// previous total.
//
// Integers in the file are in binary format so the size of the file
// in bytes indicates the quantity of integers. Uses the stat() system
// call to determine the file size in bytes which then allows an array
// of appropriate size to be allocated. DOES NOT scan through the file
// to count its size as this is not needed.
//
// Opens the file with fopen() and uses repeated calls to fread() to
// read binary integers into the allocated array. Does delta
// computations as integers are read. Closes the file after reading
// all ints.
//
// The argument len is a pointer to an integer which is set to
// the length of the array that is allocated by the function.
//
// If the file cannot be opened with fopen() or file is smaller than
// the size of 1 int, sets len to -1 and returns NULL.
