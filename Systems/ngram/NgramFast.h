
#define TABLE_SIZE 200000
class Ngrams 
{
   //ngrams will be a hashtable
   //need extra hash item class
   //the item class will hold the string and count for later sorting
   //the instert function will be a hash function
   //sorting functions go here
   
   struct Ngram{
      std::string key;
      int count;
      //this 'next' is for collisons NOT LINKED LIST
      struct Ngram * next;
   };
   typedef struct Ngram Ngram_t;

   struct NgramTable{
      Ngram_t * buckets[TABLE_SIZE];
   };


   private:
      int ngramSz;
      
      int ngram_count;// this is the size of the array
      
      bool arrayReversed;//when false array is in descending order (default), when true array is in ascending order
      Ngram_t ** arraySorted;

      struct NgramTable myHashTable;
      void insertNgram(std::string s);
      unsigned int hash(std::string s);

      Ngrams::Ngram_t ** addNodesToTempList();
      //try radix sort
      Ngrams::Ngram_t **  radixSort(Ngram_t ** array);
      Ngrams::Ngram_t **  countSort(Ngram_t ** array,int digi);

      int getMax(Ngram_t ** array);
      void reverseArray(Ngram_t ** array);
      
   public:
      Ngrams(int ngramSz, WordList & wl);
      ~Ngrams();
      void addIncreasing(NgramList *);
      void addDecreasing(NgramList *);
      
};
