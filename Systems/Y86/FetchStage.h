//class to perform the combinational logic of
//the Fetch stage
class FetchStage: public Stage
{
   private:
    bool D_bubble;
      void setDInput(D * dreg, uint64_t stat, uint64_t icode, uint64_t ifun, 
                     uint64_t rA, uint64_t rB,
                     uint64_t valC, uint64_t valP);
      uint64_t selectPC(F * freg, M * mreg, W * wreg);
      bool needvalC(uint64_t icode);
      bool needRegIds(uint64_t f_icode);
      void getRegIds(uint64_t f_code, bool err, uint64_t * rA, uint64_t * rB);
      uint64_t PCincrement(uint64_t f_pc, bool needRegIds, bool needValC);
      uint64_t buildvalC(uint64_t f_pc, bool regIds);
      uint64_t predictPC(uint64_t f_icode, uint64_t f_valC, uint64_t f_valP);
      bool instr_valid(uint64_t f_icode);
      uint64_t f_icod (bool mem_error, uint64_t icode);
      uint64_t f_ifunn (bool mem_error, uint64_t ifun);
      uint64_t f_stat(bool mem_error, bool instr_valid, uint64_t f_icode);
      bool fStall(uint64_t E_icode,uint64_t D_icode,uint64_t M_icode, uint64_t E_dstM, uint64_t d_srcA, uint64_t d_srcB);
      bool dStall(uint64_t E_icode, uint64_t E_dstM, uint64_t d_srcA, uint64_t d_srcB);
      void calculateControlSingals(bool * fstall, bool * dstall, DecodeStage * d, E * ereg, ExecuteStage * e, D * dreg, M * mreg);
      void bubbleD(D * dreg);
      bool bubble_D(uint64_t E_icode, bool e_Cnd, DecodeStage * d, D * dreg,E * ereg, M * mreg);


      bool f_Stall;
      bool d_Stall;
  public:
      bool doClockLow(PipeReg ** pregs, Stage ** stages);
      void doClockHigh(PipeReg ** pregs);

};
