#include <fstream>
#include <iostream>
#include <string>
#include <assert.h>
#include "Line.h"
#include "Set.h"
#include "Cache.h"
#include "Simulate.h"


/* Simulator constructor
 * Dynamically create a Cache object and initialize the Simulator object.
 */

Simulate::Simulate(int32_t associativity, int32_t blockOffsetBits, 
                   int32_t setIndexBits, bool verbose)
{
   hits = misses = evictions = 0;
   cacheP = new Cache(associativity, blockOffsetBits, setIndexBits, verbose);
   this->verbose = verbose;
}

/*
 * Destroy the Simulator object.
 */
Simulate::~Simulate()
{
   delete cacheP;
}

void Simulate::run(std::string filename)
{
   std::fstream fs;
   fs.open(filename, std::fstream::in);
   std::string line;
   
   //TODO
   //Loop, reading lines from the file.
   //From each line, get the type and the address.
   //Depending upon the type (L or S), you'll pass it to the
   //accessCache method. (See assignment.)
   //If the verbose flag is true, print out the input line (see csim-ref).
   std::string x, data;
   int64_t address =0;
   while(fs >> x >> data){
      address = std::stol(data,nullptr,16);
      if(verbose){
         std::cout << x<< " " << data << " ";
      }

      if(x == "L" || x == "S"){
        
        
         accessCache(address);
           std::cout <<"\n";
      }

      else if(x=="M"){
      
         accessCache(address);
         accessCache(address);
           std::cout <<"\n";
      }
      else{};
     
   }

}

/* accessCache
 * Takes as input a 64 bit address.
 * Uses the address to access the cache pointed to by cacheP
 * and updates hits, misses, and evictions.
 */
void Simulate::accessCache(int64_t address)
{
   //TODO
   //cacheP contains a pointer to the cache object
   //Call the appropriate methods in the Cache class
   //to determine whether to increment hits, misses, and/or evictions
   //If verbose is true, output the verbose output (see assignment)

   //check if hit or miss, inc counter accordingly
   //print according to verbose

   if(cacheP->isHit(address)){
      hits++;
      if(verbose){
         std::cout << "hit ";
      }
   }else{
      misses++;
      if(verbose){
         std::cout << "miss ";
      }

      if(cacheP->isEvict(address)){
         evictions++;
         if(verbose){
            std::cout << "eviction ";
         }
      }
   }
 
   cacheP->update(address);

}

/*
 * printSummary
 * Print the number of hits, misses, and evictions at the end of
 * the simulation.
 */
void Simulate::printSummary()
{
   std::fstream fs;
   printf("hits:%d misses:%d evictions:%d\n", hits, misses, evictions);
   fs.open(".csim_results", std::fstream::out);
   assert(fs.is_open());
   fs << hits << " " << misses << " " << evictions <<"\n";;
   fs.close();
}

