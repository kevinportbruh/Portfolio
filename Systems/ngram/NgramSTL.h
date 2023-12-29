
//Lots of STL header files included here.
//You shouldn't have to include any others.
#include <iostream>
#include <set>
#include <string>
#include <array>
#include <iterator>
#include <list>
#include <tuple>
#include <utility>
#include <map>
#include <unordered_map>
#include <vector>
#include <bits/stdc++.h>
#include <stdlib.h>
#include <algorithm>
#include <functional>

//You will need to add more method headers and data members
//to the class declaration below.
#define TABLE_SIZE 100000
class Ngrams 
{  
   

   private:
      int ngramCount;
      std::unordered_map<std::string,int> ngramTable; // this is the hash table

      std::pair<std::string, int> ** pairArr; // we will take the ngrams and directly put them into this and sort it every time we insert something
      int ngramSz;
      bool reversed;
      void insertNgram(std::string s);
      bool compare(std::pair<std::string ,int> i, std::pair<std::string, int> j);
      std::pair<std::string, int> ** addNodesToTempList(); // returns an array of pairs


      //radix sort
      std::pair<std::string, int> ** radixSort(std::pair<std::string, int> ** array);
      std::pair<std::string, int> ** countSort(std::pair<std::string, int> ** array, int digi);
      int getMax(std::pair<std::string, int> ** array);
      void reverseArray(std::pair<std::string, int> ** array);


   public:
      Ngrams(int ngramSz, WordList & wl);
      ~Ngrams();
      void addIncreasing(NgramList * );
      void addDecreasing(NgramList * );
};

