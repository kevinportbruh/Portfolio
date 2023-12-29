//this is the decode stage
class MemoryStage: public Stage
{
   private:
    void  setWInput(W * wreg, uint64_t stat,uint64_t icode,uint64_t valE, uint64_t valM, uint64_t dstE,uint64_t dstM);
    uint64_t addr(uint64_t M_icode, uint64_t M_valE, uint64_t M_valA);
    bool memWrite(uint64_t M_icode);
    bool memRead(uint64_t M_icode);

   

public:
    bool doClockLow(PipeReg **pregs, Stage **stages);
    void doClockHigh(PipeReg **pregs);
    uint64_t getmvalM();
    uint64_t getmstat();



};