#include <time.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "search.h"

int main(int argc, char *argv[]){
  int minPow;
  int maxPow;
  int maxReps;
  char *testChoices;

  if (argc < 4) { //not enough args to run program
    printf("usage: ./search_benchmark <minpow> <maxpow> <repeats> [which]\n");
    printf("  which is a combination of:\n");
    printf("    a : Linear Array Search\n");
    printf("    l : Linked List Search\n");
    printf("    b : Binary Array Search\n");
    printf("    t : Binary Tree Search\n");
    printf("    (default all)\n");
    return -1;
  }
  else if(argc < 5){  //test choices were not specified so perform all tests
    minPow = atoi(argv[1]);
    maxPow = atoi(argv[2]);
    maxReps = atoi(argv[3]);
    testChoices = "ablt";
  }
  else { //test choices specified so only perform chosen algorithms
    minPow = atoi(argv[1]);
    maxPow = atoi(argv[2]);
    maxReps = atoi(argv[3]);
    testChoices = argv[4];
  }

  int minLength = 1<<minPow;  //take 2^minPow using left bitshifts
  int maxLength = 1<<maxPow;  //take 2^maxPow using left bitshifts

  int run_linear_array = 0;
  int run_linear_list = 0;
  int run_binary_search = 0;
  int run_binary_tree = 0;

  int stringLength = strlen(testChoices);
  for (int c = 0; c < stringLength; c++){ //determining which tests to run
    if (testChoices[c] == 'a'){
      run_linear_array = 1;
    }
    else if (testChoices[c] == 'b'){
      run_binary_search = 1;
    }
    else if (testChoices[c] == 'l'){
      run_linear_list = 1;
    }
    else if (testChoices[c] == 't'){
      run_binary_tree = 1;
    }
  }

  printf("    LENGTH  SEARCHES"); //setting up display
  if(run_linear_array){
    printf("          array");
  }
  if(run_linear_list){
    printf("           list");
  }
  if(run_binary_search){
    printf("         binary");
  }
  if(run_binary_tree){
    printf("           tree");
  }
  printf("\n");

  clock_t begin, end;

  for (int i = minLength ; i <= maxLength ; i *= 2){ //beginning tests
    int searches = (2 * i) * maxReps;         //find totals searches for this structure size

    printf("%10d %9d", i, searches);

    double runTime;

    if (run_linear_array){
      int *array = make_evens_array(i);        //creating my array
      begin = clock();                          //starting time after allocation
      for (int reps = 0; reps < searches; reps++){
        for (int currSearch = 0; currSearch < ((2*i) - 1); currSearch++){
          linear_array_search(array, i, currSearch);
        }//current search value for loop
      }//repetitions for loop
      end = clock();                          //ending time before deallocation
      runTime =  ((double) (end - begin)) / CLOCKS_PER_SEC;   //calculate how much time passed during search repetitions
      printf("%15.4e",runTime);
      free(array);
    }

    if (run_linear_list){                       //repeats same pattern as above
      list_t *linkedList = make_evens_list(i);
      begin = clock();
      for (int reps = 0; reps < searches; reps++){
        for (int currSearch = 0; currSearch < ((2*i) - 1); currSearch++){
          linkedlist_search(linkedList, i, currSearch);
        }//current search value for loop
      }//repetitions for loop
      end = clock();
      runTime =  ((double) (end - begin)) / CLOCKS_PER_SEC;
      printf("%15.4e",runTime);
      list_free(linkedList);
    }

    if (run_binary_search){                       //repeats same pattern as above
      int *array = make_evens_array(i);        //creating my array
      begin = clock();
      for (int reps = 0; reps < searches; reps++){
        for (int currSearch = 0; currSearch < ((2*i) - 1); currSearch++){
          binary_array_search(array, i, currSearch);
        }//current search value for loop
      }//repetitions for loop
      end = clock();
      runTime =  ((double) (end - begin)) / CLOCKS_PER_SEC;
      printf("%15.4e",runTime);
      free(array);
    }

    if (run_binary_tree){                       //repeats same pattern as above
      bst_t *binaryTree = make_evens_tree(i);
      begin = clock();
      for (int reps = 0; reps < searches; reps++){
        for (int currSearch = 0; currSearch < ((2*i) - 1); currSearch++){
          binary_tree_search(binaryTree, i, currSearch);
        }//current search value for loop
      }//repetitions for loop
      end = clock();
      runTime =  ((double) (end - begin)) / CLOCKS_PER_SEC;
      printf("%15.4e",runTime);
      bst_free(binaryTree);
    }
    printf("\n");
  }//size of table for loop

  //finished running tests
  return 0;
}
