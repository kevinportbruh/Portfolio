#include <iostream>
#include <string>
#include <assert.h>
#include "Line.h"
#include "Set.h"
#include "Cache.h"


Cache::Cache(int32_t associativity, int32_t blockOffsetBits, 
             int32_t setIndexBits, bool verbose)
{
   int32_t i, j;
   
   numSets = 1<<setIndexBits;  // if s=3 then S=2^3 =8,   1<<3 =8🧙‍♂️


   //the rest of this is complete
   sets = new Set[numSets];  //create an array of Set objects
   for (i = 0; i < numSets; i++)
   {
      sets[i].initialize(i, associativity, verbose); //initialize each Set object
   }

   this->associativity = associativity;        //the way
   this->blockOffsetBits = blockOffsetBits;    //the number of bits for the block offset
   this->setIndexBits = setIndexBits;          //the number of bits for the set index
   this->verbose = verbose;                    //use this to turn on/off debugging print statements
}

/*
 * Destruct the dynamically allocated data.
 */
Cache::~Cache()
{
   //delete set field 🧙‍♂️
   delete[] sets;
   
}

/* 
 * isHit
 * Returns true if accessing the cache with the provided address results in
 * a hit. Calls isHit in the Set class.
 */
bool Cache::isHit(int64_t address)
{
   // (high to low) t + s + b bits 

   int64_t tag = getBits((blockOffsetBits+setIndexBits),63,address);
   int64_t setNum = getBits(blockOffsetBits,(blockOffsetBits+setIndexBits-1),address);

   return sets[setNum].isHit(tag);
   
}

/*
 * isEvict
 * Returns true if accessing the cache with the provided address results in
 * an eviction because the Set is full. Calls isFull in the Set class.
 */
bool Cache::isEvict(int64_t address)
{
   //in the Set class using the appropriate Set object 🧙‍♂️

   int64_t tag = getBits((blockOffsetBits+setIndexBits),63,address);
   int64_t setNum = getBits(blockOffsetBits,(blockOffsetBits+setIndexBits-1),address);

  
      return sets[setNum].isFull();
   
}

/*
 * update
 * Updates the set in which the tag of the address is to be stored
 * by calling the updateSet method using the appropriate Set object.
 */
void Cache::update(int64_t address)
{
  

   int64_t tag = getBits((blockOffsetBits+setIndexBits),63,address);
   int64_t setNum = getBits(blockOffsetBits,(blockOffsetBits+setIndexBits-1),address);
   sets[setNum].updateSet(tag);

}

/*
 * getBits
 * Takes as input a source, a starting bit number, and an ending bit number
 * and returns those bits from the source.
 * For example, getBits(60, 62, 0x7000000000000000) returns 7
 */
int64_t Cache::getBits(int32_t start, int32_t end, int64_t source)
{
  
   //from CS 3481 lab 🧙‍♂️
   uint64_t temp = 0;
   uint64_t mask = 0;

   if (start >=0 && end <=63 && start <=end){
      mask = ((1ull<<(end-start +1))-1 ) <<start;
      temp = (source & mask) >>start;      
   }
   if (end==63){
      return source >> start;
   }
   return temp;
}

/*
 * print
 * Output the contents of the cache.  This may be useful for debugging.
 * You can add a call to this in the Simulate run method.
*/
void Cache::print()
{
   std::cout << "Number of Sets: " << numSets << ", ";
   std::cout << "Associativity: " << associativity << "\n";

   int32_t i, j;
   for (i = 0; i < numSets; i++)
   {
      sets[i].print();   //call print method in Set class
   }
   std::cout << "\n";
}

