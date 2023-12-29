#include "WordList.h"
#include "NgramList.h"
#include "NgramSTL.h"

/* You may NOT include any additional header files or declare any global variables. */
/* You may add data members to the class by adding them to NgramSTL.h */
/* You may add more methods to the class. Add the prototype to NgramSTL.h */
/* You can also add functions (outside of any class) to this file, but only */
/* if you absolutely are required to by the STL. */

/*
 * Ngrams
 *
 * Takes as input the size of the ngrams to be built and the list  
 * of words to build the ngrams from and calls insertNgram to
 * insert the ngram into some collection object defined using a 
 * C++ template class. You get to choose the template class.
 * You'll need to add a declaration of the template class object
 * to NgramSTL.h.  You may need more than one.
 *
 * param: int ngramSz - size of the ngram
 * param: const WordList & wl - list of the words use
 */
Ngrams::Ngrams(int ngramSz, WordList & wl)
{
   //Add any code you need to initialize whatever structure
   //you use to hold your ngrams.
   ngramCount=0;
   reversed = false;
      

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

   //add nodes to temp list
   std::pair<std::string, int> ** tempList = addNodesToTempList();
   //ngrams added to list now sort them
   pairArr = radixSort(tempList);

}


/*
 * Ngrams destructor
 *
 * Automatically called when Ngrams object goes out of scope.
 * Delete any space dynamically allocated to hold the ngrams.
 * If you didn't dynamically allocate any date then this 
 * method stays empty.
 */
Ngrams::~Ngrams()
{
   ngramTable.erase(ngramTable.begin());
   for(int i = 0; i < ngramCount; i++) {
      delete pairArr[i];
   }
 


   delete[] pairArr;

}

/*
 * addIncreasing
 *
 * Adds the ngrams to the ngram list in increasing order of ngram count.
 *
 */
void Ngrams::addIncreasing(NgramList * incrLst)
{
   //The call to add it to the list looks something like this:
   //incrLst->addToNgramList(ngramStr, ngramCnt);
   //You will probably have this call inside of a loop
   //get list iterator at the beginning of the list


   //while loop till flag is false,
   //flag will be falsae if there is no more elements in the ngramtable
   if(reversed){
      reverseArray(pairArr);
   }

   for(int i =0;i<ngramCount; i++){
      incrLst->addToNgramList(pairArr[i]->first, pairArr[i]->second);
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
   //The call to add it to the list looks something like this:
   //decrLst->addToNgramList(ngramStr, ngramCnt);
   //You will probably have this call inside of a loop
   reverseArray(pairArr);
   reversed=true;

   for(int i =0;i<ngramCount; i++){
      decrLst->addToNgramList(pairArr[i]->first, pairArr[i]->second);
   }
}


/*
 * insertNgram
 *
 * Inserts the ngram into whatever collection object you choose to hold
 * your ngrams. If the ngram is already in the collection, it should
 * simply increment the count.
 *
 * param: std::string s - ngram to be inserted
 * return: none
 */
void Ngrams::insertNgram(std::string s)
{
   // Given string s, check if it is in the map
   // If it is, increment the count
   // Else add to map and increment count

   auto it = ngramTable.find(s);
   if (it != ngramTable.end()) {
      it->second++;
   } else {
      ngramTable[s] = 1;
      ngramCount++;
   }

   
}

//returns an array of string int pairs
std::pair<std::string, int> ** Ngrams::addNodesToTempList(){
   std::pair<std::string, int> ** tempArray = new std::pair<std::string, int>* [ngramCount]; 
   int idx = 0;
   for(auto &it: ngramTable){
      tempArray[idx] = new std::pair<std::string, int>(it.first, it.second);
      idx++;
   }
   //temp should be an array of pairs now
   return tempArray;
   
}


//radix sort 
std::pair<std::string, int> **  Ngrams::radixSort(std::pair<std::string, int> ** array){

   int max = getMax(array);
  
   //do count sort for every digit
   
   for(int digi = 1; max/digi > 0; digi *= 10){
      array = countSort(array,digi);
   }
   
  
   return array;
}

std::pair<std::string, int> ** Ngrams::countSort(std::pair<std::string, int> ** array, int digi){
   const int max =10;// 0-9
  std::pair<std::string, int> * output[ngramCount]; //output array
   int freq[max]={0};  //frequency array
   //divide ngram cout at array[i] by digi and mod by 10 to correctly inc freq array

   for(int i = 0; i < ngramCount; i++){
      freq[(array[i]->second/digi)%10]++;
   }
   //prefix sum
   for(int i = 1; i < max; i++){
      freq[i] += freq[i-1];
   }

   //build output array
   for(int i = ngramCount-1; i >=0; i--){
      output[freq[(array[i]->second/digi)%10]-1] = array[i];
      
      freq[(array[i]->second/digi)%10]--;
   }
   for(int i = 0; i < ngramCount; i++){
      array[i] = output[i];
   } 
   
   return array;
}

int Ngrams::getMax(std::pair<std::string, int> ** array){
   int max = array[0]->second;

   for(int i = 1; i < ngramCount; i++){
      if(array[i]->second > max){
         max = array[i]->second;
      }
   }
   return max;
}




void  Ngrams::reverseArray(std::pair<std::string, int> ** array){
   int i = 0;
   int j = ngramCount-1;
   while(i<j){
      std::pair<std::string, int> * temp= array[i];
      array[i] = array[j];
      array[j] = temp;
      i++;
      j--;
   }
}



//helper compare method
bool Ngrams::compare(std::pair<std::string ,int> i, std::pair<std::string, int> j) {
  return i.second < j.second;
}





