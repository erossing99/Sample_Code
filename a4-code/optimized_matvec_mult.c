#include "matvec.h"
#include <stdlib.h>

int optimized_matrix_trans_mult_vec(matrix_t mat, vector_t vec, vector_t res){
  if(mat.rows != vec.len){  //error checking
    printf("mat.rows (%ld) != vec.len (%ld)\n",mat.rows,vec.len);
    return 1;
  }

  if(mat.cols != res.len){  //error checking
    printf("mat.cols (%ld) != res.len (%ld)\n",mat.cols,res.len);
    return 2;
  }

  for(int j=0; j < res.len; j++){ //setting matrix to zeros
  VSET(res,j,0);
  }

  for (int i = 0; i < mat.rows ; i++){
    int veci = VGET(vec,i);          //gets value in vec at index (i)
    for (int j = 0; j < mat.cols ; j++){ // iterating through one row at a time allows for sequential memory access
      int elij = MGET(mat,i,j); //gets value in matrix at position (i,j)
      int newSum = VGET(res,j) + (elij * veci);  //retrieves current sum at index (j) and adds on product of elij and veci
      VSET(res,j,newSum); //sets sum at index (j) to newSum
    }
  }

  return 0;
}
