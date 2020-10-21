#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "hashmap.h"


int main(int argc, char *argv[]){
  int echo = 0;                                // controls echoing, 0: echo off, 1: echo on
  if(argc > 1 && strcmp("-echo",argv[1])==0) { // turn echoing on via -echo command line option
    echo=1;
  }

  printf("Hashmap Demo\n");
  printf("Commands:\n");
  printf("  hashcode <key>   : prints out the numeric hash code for the given key (does not change the hash map)\n");
  printf("  put <key> <val>  : inserts the given key/val into the hash map, overwrites existing values if present\n");
  printf("  get <key>        : prints the value associated with the given key or NOT FOUND\n");
  printf("  print            : shows contents of the hashmap ordered by how they appear in the table\n");
  printf("  structure        : prints detailed structure of the hash map\n");
  printf("  clear            : reinitializes hash map to be empty with default size\n");
  printf("  save <file>      : writes the contents of the hash map the given file\n");
  printf("  load <file>      : clears the current hash map and loads the one in the given file\n");
  printf("  next_prime <int> : if <int> is prime, prints it, otherwise finds the next prime and prints it\n");
  printf("  expand           : expands memory size of hashmap to reduce its load factor\n");
  printf("  quit             : exit the program\n");

  hashmap_t mainTable;
  hashmap_init(&mainTable, 5);


  while(1){
    char cmd[128];
    int success;
    printf("HM> ");
    success = fscanf(stdin, "%s", cmd);
    if (success == EOF){  //found end of input
      printf("\n");
      break;
    }

    if (strcmp("quit", cmd) == 0){   //quits program
      if(echo){
        printf("quit\n");
      }
      break;
    }

    else if (strcmp("hashcode", cmd) == 0){  //displays hashcode of thing
      fscanf(stdin, "%s", cmd);
      if(echo){
        printf("hashcode %s\n",cmd);
      }
      printf("%ld\n", hashcode(cmd));
    }

    else if (strcmp("put", cmd) == 0){ //puts key/val into hashtable
      char key[128];
      char val[128];
      fscanf(stdin, "%s", key);
      fscanf(stdin, "%s", val);
      if(echo){
        printf("put %s %s\n", key, val);
      }
      hashmap_put(&mainTable,key,val);

    }

    else if (strcmp("get", cmd) == 0){ //gets val associated with key
      char key[128];
      fscanf(stdin, "%s", key);
      if(echo){
        printf("get %s\n",key);
      }
      char *foundVal = hashmap_get(&mainTable, key);
      if (foundVal == NULL){
        printf("NOT FOUND\n");
      }
      else{
        printf("FOUND: %s\n", foundVal);
      }
    }

    else if (strcmp("print", cmd) == 0){   //displays hash table keys : vals
      if(echo){
        printf("print\n");
      }
      for (int i = 0; i < (mainTable.table_size); i++){
        hashnode_t *node = mainTable.table[i];
        while (node != NULL){
          printf("%12s : %s\n", node->key, node->val);
          node = node->next;
        }
      }
    }

    else if (strcmp("structure", cmd) == 0){ //shows structure of hash table
      if(echo){
        printf("structure\n");
      }
      hashmap_show_structure(&mainTable);
    }

    else if (strcmp("clear", cmd) == 0){  //frees table and reinitializes it
      if(echo){
        printf("clear\n");
      }
      hashmap_free_table(&mainTable);
      hashmap_init(&mainTable, HASHMAP_DEFAULT_TABLE_SIZE);
    }

    else if (strcmp("save", cmd) == 0){  //saves table to a file handle
      fscanf(stdin, "%s", cmd);
      if(echo){
        printf("save %s\n",cmd);
      }
      hashmap_save(&mainTable, cmd);
    }

    else if (strcmp("load", cmd) == 0){  //loads table from given file handle
      fscanf(stdin, "%s", cmd);
      if(echo){
        printf("load %s\n",cmd);
      }
      int result = hashmap_load(&mainTable, cmd);
      if (result == 0){
        printf("load failed\n");
      }
    }

    else if (strcmp("next_prime", cmd) == 0){  //gives next prime number after given number
      int enteredNum;
      fscanf(stdin, "%d", &enteredNum);
      if(echo){
        printf("next_prime %d\n", enteredNum);
      }
      printf("%d\n", next_prime(enteredNum));
    }

    else if (strcmp("expand", cmd) == 0){    //expands table
      if(echo){
        printf("expand\n");
      }
      hashmap_expand(&mainTable);
    }

    else{        // unknown command
      if(echo){
        printf("%s\n",cmd);
      }
      printf("unknown command %s\n",cmd);
    }

  }
  hashmap_free_table(&mainTable);   //frees memory at end of program
  return 0;

}
