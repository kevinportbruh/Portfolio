#include <iostream>
#include <string>
#include "WordList.h"
#include "NgramList.h"
#include "NgramFast.h"

/* You may NOT declare any global variables. */

/* You may add data members to the NgramFast class by adding them to NgramFast.h */
/* You may add more methods to the NgramFast class.  Put prototypes in NgramFast.h */
/* You may add more classes.  If you do, they need to be cleanly
 * separated into a .h and a .C files. You would add an include
 * for the .h at the top.  You would need to change the makefile
 * so that your new classes are compiled and linked with the
 * executable. */

/*
 * Ngrams
 *
 * Takes as input the size of the ngrams to be built and the list  
 * of words to build the ngrams from and builds a collection
 * of ngrams. You may not use the C++ STL to define a collection object.
 * You need to implement your own. 
 *
 * param: int ngramSz - size of the ngram
 * param: const WordList & wl - list of the words use
 */
Ngrams::Ngrams(int ngramSz, WordList & wl)
{
   //Add any code you need to initialize whatever structure
   //you use to hold your ngrams.
   //make hashtable null
   for (int i = 0; i < TABLE_SIZE; i++) {
    myHashTable.buckets[i] = NULL;
   } 
    
   //This code doesn't change
   this->ngramSz = ngramSz;
   wl.beginIter();
   while (!wl.endIter())
   {
      std::string ngram = wl.getNextNgram(ngramSz);
      wl.incrementIter();
      //Insert the ngram in the collection object
      if (!ngram.empty()) insertNgram(ngram);
   }

   //You can make additional calls here if you need to.
   Ngram_t ** bruh =addNodesToTempList();
   arraySorted=radixSort(bruh);

   

}


/*
 * Ngrams destructor
 *
 * Automatically called when Ngrams object goes out of scope.
 * Delete any space dynamically allocated to hold the ngrams.
 */
Ngrams::~Ngrams()
{
   //loop thru hashtable and delete each node
   for(int i =0; i < TABLE_SIZE; i++){
      Ngram_t *cur = myHashTable.buckets[i];
      while(cur != NULL){
         Ngram_t *temp = cur;
         cur = cur->next;
         delete temp;
      }
   }

   delete[] arraySorted;
}


/*
 * insertNgram
 *
 * Inserts ngram into whatever structure you choose to hold
 * your ngrams. If the ngram is already in the collection, the
 * insert simply increments the count.
 *
 * param: std::string s - ngram to be inserted
 * return: none
 */
void Ngrams::insertNgram(std::string s)
{
   //get valid index
   unsigned int i = hash(s) % TABLE_SIZE;
   
   Ngram_t *cur =  myHashTable.buckets[i];
   while(cur != NULL){
      if(cur->key== s){
         cur->count++;
         return;
      }
      cur = cur->next;
   }

   //if it gets here then ngram not in bucket
   Ngram_t * newItem = new Ngram_t();
   newItem->key = s;
   newItem->count = 1;
   newItem->next = myHashTable.buckets[i];
   myHashTable.buckets[i] = newItem;
   
}

unsigned int Ngrams::hash(std::string s){
   const char * str = s.c_str();
   unsigned int hash = 0;
   unsigned int prime = 16777619; // A large prime number
   while (*str) {
        hash = (hash * prime) ^ *str;
        str++;
   }
   return hash;
}


/**
 * This will add the ngram nodes into an unordered array at which point it will be sorted
*/

Ngrams::Ngram_t** Ngrams::addNodesToTempList() {
   //iterate through hashtable
   ngram_count =0;
   for(int i = 0; i < TABLE_SIZE; i++){
      Ngram_t *cur = myHashTable.buckets[i];
      while(cur != NULL){
         ngram_count++;
         cur = cur->next;
      }
   }
   //create array of ngram nodes
   Ngram_t** tempArray = new Ngram_t*[ngram_count];
  for(int i = 0; i < ngram_count; i++){
     tempArray[i] = NULL;
  }
   
   //go thru hashtable, chain then index and add each ngram to array unordered the array does not support chaining.
   int index = 0;
   for(int i =0; i < TABLE_SIZE; i++){
      Ngram_t *cur = myHashTable.buckets[i];
      while(cur != NULL){
         tempArray[index++] = cur;
         cur = cur->next;
      }
   }
   //when here we have an array of ngram nodes
   //can send to sort function now set alreadyInitialized to true
   arrayReversed = false;
   
   return tempArray;
}

/**
 * getMax
 * gets max value in array
*/
int Ngrams::getMax(Ngram_t ** array){
   int max = array[0]->count;
   
   for(int i = 1; i < ngram_count; i++){
      if(array[i]->count > max){
         max = array[i]->count;
      }
      
   }
   return max;
}

/**
 * does count sort of the array[] according to the digi
*/
Ngrams::Ngram_t **  Ngrams::countSort(Ngram_t ** array,int digi){
   const int max =10;// 0-9
   Ngram_t * output[ngram_count]; //output array
   int freq[max]={0};  //frequency array
   //divide ngram cout at array[i] by digi and mod by 10 to correctly inc freq array

   for(int i = 0; i < ngram_count; i++){
      freq[(array[i]->count/digi)%10]++;
   }
   //prefix sum
   for(int i = 1; i < max; i++){
      freq[i] += freq[i-1];
   }

   //build output array
   for(int i = ngram_count-1; i >=0; i--){
      output[freq[(array[i]->count/digi)%10]-1] = array[i];
      
      freq[(array[i]->count/digi)%10]--;
   }
   for(int i = 0; i < ngram_count; i++){
      array[i] = output[i];
   } 
   
   return array;
}

Ngrams::Ngram_t ** Ngrams::radixSort(Ngram_t ** array){
   //get max value in array
   int max = getMax(array);
  
   //do count sort for every digit
   //Ngram_t ** tempArray = new Ngram_t*[ngram_count];
   for(int digi = 1; max/digi > 0; digi *= 10){
      array = countSort(array,digi);
   }
   
  
   return array;

}


/*
 * addIncreasing
 * 
 * Adds the ngrams to the ngram list in increasing order of ngram count.
 *
 */
void Ngrams::addIncreasing(NgramList * incrLst)
{
   //if the addNodesToTempList function has not been called yet then call it & sort it once

   if(arrayReversed){ //this means the decending list was called first so we need to reverse the array back to acending
      reverseArray(arraySorted);
      arrayReversed = false;
   }
   
  for(int i = 0; i < ngram_count; i++){
         incrLst->addToNgramList(arraySorted[i]->key, arraySorted[i]->count);
   }
 
   
   
  
}

/*
 * addDecreasing
 *
 * Adds the ngrams to the ngram list in decreasing order of ngram count.
 *
 */
void Ngrams::addDecreasing(NgramList * decrLst)
{
         
   //reverse the array because it is sorted in ascending order
   reverseArray(arraySorted);
   arrayReversed = true;// THIS WILL ONLY EVER BE SET TO TRUE HERE
   
   for(int i = 0; i < ngram_count; i++){
      decrLst->addToNgramList(arraySorted[i]->key, arraySorted[i]->count);
   }
}

/*
 * reverseArray
 *
 * Reverses the array of ngrams.
 *
 */
void Ngrams::reverseArray(Ngram_t ** array){
   int i = 0;
   int j = ngram_count-1;
   while(i<j){
      Ngram_t * temp = array[i];
      array[i] = array[j];
      array[j] = temp;
      i++;
      j--;
   }
}






