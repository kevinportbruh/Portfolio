//this is the decode stage
class DecodeStage: public Stage
{
   private:
      void setEInput(E * ereg, uint64_t stat, uint64_t icode, uint64_t ifun,
                     uint64_t valC, uint64_t valA, uint64_t valB, uint64_t dstE,
                     uint64_t dstM, uint64_t srcA, uint64_t srcB);

      uint64_t d_srcA(uint64_t icode, uint64_t d_rA);
      uint64_t d_srcB(uint64_t icode, uint64_t d_rB);
      uint64_t d_dstE(uint64_t D_icode, uint64_t D_rb);
      uint64_t d_valB(uint64_t d_regB, bool err);

      uint64_t d_dstM(uint64_t D_icode, uint64_t D_rb);
      uint64_t selFwdA(uint64_t srcA,M * mreg,W * wreg, MemoryStage * mem,ExecuteStage * exec,uint64_t valP,uint64_t icode);
      bool calculateControlSignals(uint64_t E_icode, uint64_t E_dstM, bool Cnd);

      uint64_t fwdB(uint64_t srcB, M * mreg, W * wreg,MemoryStage * mem, ExecuteStage * exec);
      uint64_t srcA;
      uint64_t srcB;
      bool E_bubble;


      

  public:

      bool doClockLow(PipeReg ** pregs, Stage ** stages);
      void doClockHigh(PipeReg ** pregs);
      uint64_t getdsrcA();
      uint64_t getdsrcB();
};