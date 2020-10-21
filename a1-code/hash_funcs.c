#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "hashmap.h"


long hashcode(char key[]){
  union {
    char str[8];
    long num;
  } strnum;
  strnum.num = 0;

  for(int i=0; i<8; i++){
    if(key[i] == '\0'){
      break;
    }
    strnum.str[i] = key[i];
  }
  return strnum.num;
}

int   next_prime(int num){

  int foundPrime = -1; //false when -1, true when 1
  int returnedPrime = num;

  if (returnedPrime == 2){ //setting base cases
    return 2;
  }
  else if (returnedPrime == 3){
    return 3;
  }

  if ((returnedPrime % 2) == 0){ //checks to see if num is even, if so changes it to next odd
    returnedPrime++;
  }

  while (foundPrime != 1){
    int divisorAmount = 0;
    for (int i = 2; i <= (returnedPrime/2) ; i++){
      if ((returnedPrime % i) == 0){
        divisorAmount++;
        break;
      }
    }
    if(divisorAmount == 0){
      return returnedPrime;
    }
    returnedPrime += 2; //now checking if next odd number is prime
  }

  return -1;
}


void  hashmap_init(hashmap_t *hm, int table_size){

  hm->item_count = 0;
  hm->table_size = table_size;
  hm->table = malloc(table_size * sizeof(hashnode_t));
  for (int i = 0; i < table_size; i++){
    hm->table[i] = NULL;
  }

}


int   hashmap_put(hashmap_t *hm, char key[], char value[]){

  long hashVal = hashcode(key);
  int tableIndex = hashVal % (hm->table_size);
  if (tableIndex < 0){
    tableIndex = tableIndex * -1;
  }

  hashnode_t *firstNode = hm->table[tableIndex]; //first element in bucket of tableIndex

  if (firstNode == NULL){    //if NULL then nothing in bucket so list need not be traversed
    hashnode_t *newNode = malloc(sizeof(hashnode_t));
    strcpy(newNode->key, key);   //new node is getting key and val
    strcpy(newNode->val, value);
    newNode->next = NULL;
    hm->item_count += 1;
    hm->table[tableIndex] = newNode;   // then the new node is added to the end of the list
    return 1;
  }

  if (strcmp(firstNode->key, key) == 0){ //if first node's key is entered key then change val
    strcpy(firstNode->val,value);
    printf("Overwriting previous key/val\n");
    return 0;
  }

  while (firstNode->next != NULL){ //starts traversing list
    if (strcmp(firstNode->next->key, key) == 0){ //checks to see if following key matches inputed key
      strcpy(firstNode->next->val, value);
      printf("Overwriting previous key/val\n");
      return 0;
    }
    else{   //if not matching key it moves firstNode onto the next node
      if (firstNode->next != NULL){
        firstNode = firstNode->next;
      }
    }
  } //if it reaches the end of the while loop firstNode should be pointing at the last node of the list
  hashnode_t *newNode = malloc(sizeof(hashnode_t));
  strcpy(newNode->key, key);
  strcpy(newNode->val, value);
  hm->item_count += 1;
  firstNode->next = newNode;   // then the new node is added to the end of the list
  return 1;
}

char *hashmap_get(hashmap_t *hm, char key[]){

  long hashVal = hashcode(key);
  int tableIndex = hashVal % (hm->table_size);
  if (tableIndex < 0){
    tableIndex = tableIndex * -1;
  }

  hashnode_t *firstNode = hm->table[tableIndex];

  while (firstNode != NULL){  //traversing list
    if (strcmp(firstNode->key, key) == 0){
      return firstNode->val; //returns when the key is found
    }
    else{
      firstNode = firstNode->next;
    }
  }

  return NULL; //only reaches this if the key was not found
}

void  hashmap_show_structure(hashmap_t *hm){

  printf("item_count: %d\n", hm->item_count);
  printf("table_size: %d\n", hm->table_size);
  printf("load_factor: %.4f\n", (float)(hm->item_count)/(hm->table_size));

  for(int i = 0; i < hm->table_size; i++){
    printf("%3d : ", i);
    hashnode_t *node = hm->table[i];
    while (node != NULL){
      printf("{(%ld) %s : %s} ", hashcode(node->key), node->key, node->val);
      node = node->next;
    }
    printf("\n");
  }
}

void  hashmap_free_table(hashmap_t *hm){
  for (int i = 0; i < hm->table_size; i++){
    hashnode_t *node = hm->table[i];
    while (node != NULL){   //traverses each linked list and frees every node
      hashnode_t *freeNode = node;
      node = node->next;
      free(freeNode);
    }
  }
  free(hm->table);
  hm->table = NULL;
  hm->table_size = 0;
  hm->item_count = 0;
}

void  hashmap_expand(hashmap_t *hm){
  int oldTableSize = hm->table_size;
  long newTableSize = next_prime(2 * hm->table_size+1);
  hashmap_t newMap;
  hashmap_init(&newMap, newTableSize);

  for(int i = 0; i < oldTableSize; i++){ //copying data from old Table to newMap
    hashnode_t *node = hm->table[i];
    while(node != NULL){
      hashmap_put(&newMap, node->key, node->val);
      node = node->next;
    }
  }
  *hm = newMap; //reassigns pointer hm to the new expanded hash table

}

void  hashmap_write_items(hashmap_t *hm, FILE *out){

  for(int i = 0; i < hm->table_size; i++){
    hashnode_t *node = hm->table[i];
    while(node != NULL){
      fprintf(out, "%12s : %s\n", node->key, node->val);
      node = node->next;
    }
  }

}

void  hashmap_save(hashmap_t *hm, char *filename){

  FILE *writingFile = fopen(filename, "w");
  if (writingFile == NULL){
    printf("File was unable to be opened.\n");
    return;
  }

  fprintf(writingFile, "%d %d\n", hm->table_size, hm->item_count);
  hashmap_write_items(hm, writingFile);
  fclose(writingFile);
}

int   hashmap_load(hashmap_t *hm, char *filename){

  FILE *loadingFile = fopen(filename, "r"); //opens file and returns if error occurs
  if (loadingFile == NULL){
    printf("ERROR: could not open file '%s'\n", filename);
    return 0;
  }

  int newTableSize;
  int newItemCount;
  fscanf(loadingFile, "%d %d", &newTableSize, &newItemCount);  //grabs table size and item count from first line

  hashmap_t newMap;
  hashmap_init(&newMap, newTableSize);

  char currentKey[128];
  char currentVal[128];

  for (int i = 0; i < newItemCount; i++){    //goes through and adds each key/val pair to new hashmap
    fscanf(loadingFile, "%12s : %s", currentKey, currentVal);
    hashmap_put(&newMap, currentKey, currentVal);
  }

  *hm = newMap; //reassigns old hm to new hashmap
  fclose(loadingFile);
  return 1;
}
