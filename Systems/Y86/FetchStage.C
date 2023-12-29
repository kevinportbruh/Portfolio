#include <string>
#include <cstdint>
#include "RegisterFile.h"
#include "PipeRegField.h"
#include "PipeReg.h"
#include "F.h"
#include "D.h"
#include "E.h"
#include "M.h"
#include "W.h"
#include "Stage.h"
#include "ExecuteStage.h"
#include "MemoryStage.h"
#include "DecodeStage.h"
#include "FetchStage.h"
#include "Instructions.h"
#include "Status.h" 
#include "Debug.h"
#include "Tools.h"
#include "Memory.h"
#include <iostream>

/*
 * doClockLow:
 * Performs the Fetch stage combinational logic that is performed when
 * the clock edge is low.
 *
 * @param: pregs - array of the pipeline register sets (F, D, E, M, W instances)
 * @param: stages - array of stages (FetchStage, DecodeStage, ExecuteStage,
 *         MemoryStage, WritebackStage instances)
 */
bool D_bubble;
bool FetchStage::doClockLow(PipeReg ** pregs, Stage ** stages)
{
   bool mem_error;
   bool valCFlag = false, RegIdFlag = false;
   F * freg = (F *) pregs[FREG];
   D * dreg = (D *) pregs[DREG];
   E * ereg = (E*) pregs[EREG];
   M * mreg = (M*) pregs[MREG];
   W * wreg = (W*) pregs[WREG];
   uint64_t f_pc = 0, icode = 0, ifun = 0, valC = 0, valP = 0;
   uint64_t rA = RNONE, rB = RNONE, stat = SAOK;
   uint64_t f_icode;
   uint64_t f_ifun;
   //code missing here to select the value of the PC
   f_pc = selectPC(freg,mreg,wreg);
   //std::cout << "f_pc: " << f_pc << std::endl;
   //and fetch the instruction from memory
   uint8_t inByte = Memory::getInstance()->getByte(f_pc,mem_error);
   //Fetching the instruction will allow the icode, ifun,
   ifun = Tools::getBits(inByte, 0 , 3);
   icode = Tools::getBits(inByte,4,7);
   f_icode = f_icod(mem_error,icode);
   f_ifun = f_ifunn(mem_error,ifun);
   bool ins_valid = instr_valid(f_icode);
   stat = f_stat(mem_error,ins_valid,f_icode);
   //rA, rB, and valC to be set.
   //The lab assignment describes what methods need to be
   //written.
   valCFlag = needvalC(f_icode);
   RegIdFlag = needRegIds(f_icode);
   valP = PCincrement(f_pc, RegIdFlag, valCFlag);

  if(RegIdFlag == true)
  { 
      getRegIds(f_pc, mem_error, &rA, &rB);
  }
  if(valCFlag == true){
      valC = buildvalC(f_pc,RegIdFlag);
  }
  // get d instance for the d_srcA/B for control signals
  DecodeStage *decode =(DecodeStage*)stages[DSTAGE];
  ExecuteStage *execute =(ExecuteStage*)stages[ESTAGE];

  calculateControlSingals(&f_Stall, &d_Stall, decode, ereg,execute,dreg,mreg);
  //


   //The value passed to setInput below will need to be changed
   freg->getpredPC()->setInput(predictPC(f_icode, valC, valP));

   //provide the input values for the D register
   setDInput(dreg, stat, f_icode, f_ifun, rA, rB, valC, valP);
   return false;
}

void FetchStage::bubbleD(D * dreg)
{
      dreg->getstat()->bubble(SAOK);
      dreg->geticode()->bubble(INOP);
      dreg->getifun()->bubble(0);
      dreg->getrA()->bubble(RNONE);
      dreg->getrB()->bubble(RNONE);
      dreg->getvalC()->bubble(0);
      dreg->getvalP()->bubble(0);

}

/* doClockHigh
 * applies the appropriate control signal to the F
 * and D register intances
 *
 * @param: pregs - array of the pipeline register (F, D, E, M, W instances)
 */
void FetchStage::doClockHigh(PipeReg ** pregs)
{
   F * freg = (F *) pregs[FREG];
   D * dreg = (D *) pregs[DREG];
   if(D_bubble)
   {
      FetchStage::bubbleD(dreg);
   }
   if (!f_Stall){
      freg->getpredPC()->normal();
   }else{
      freg->getpredPC()->stall();
   }
   if(!d_Stall && !D_bubble)
   {
   dreg->getstat()->normal();
   dreg->geticode()->normal();
   dreg->getifun()->normal();
   dreg->getrA()->normal();
   dreg->getrB()->normal();
   dreg->getvalC()->normal();
   dreg->getvalP()->normal();
   }
   else
   {
   dreg->getstat()->stall();
   dreg->geticode()->stall();
   dreg->getifun()->stall();
   dreg->getrA()->stall();
   dreg->getrB()->stall();
   dreg->getvalC()->stall();
   dreg->getvalP()->stall();
   }
   
}

/* setDInput
 * provides the input to potentially be stored in the D register
 * during doClockHigh
 *
 * @param: dreg - pointer to the D register instance
 * @param: stat - value to be stored in the stat pipeline register within D
 * @param: icode - value to be stored in the icode pipeline register within D
 * @param: ifun - value to be stored in the ifun pipeline register within D
 * @param: rA - value to be stored in the rA pipeline register within D
 * @param: rB - value to be stored in the rB pipeline register within D
 * @param: valC - value to be stored in the valC pipeline register within D
 * @param: valP - value to be stored in the valP pipeline register within D
*/
void FetchStage::setDInput(D * dreg, uint64_t stat, uint64_t icode, 
                           uint64_t ifun, uint64_t rA, uint64_t rB,
                           uint64_t valC, uint64_t valP)
{
   dreg->getstat()->setInput(stat);
   dreg->geticode()->setInput(icode);
   dreg->getifun()->setInput(ifun);
   dreg->getrA()->setInput(rA);
   dreg->getrB()->setInput(rB);
   dreg->getvalC()->setInput(valC);
   dreg->getvalP()->setInput(valP);
}
     
void FetchStage::getRegIds(uint64_t f_pc, bool err, uint64_t * rA, uint64_t * rB)
{
      
      uint8_t inByte = Memory::getInstance()->getByte(f_pc+1,err);
      *rA = Tools::getBits(inByte,4,7);
      *rB = Tools::getBits(inByte,0,3);

}
uint64_t FetchStage::f_icod (bool mem_error, uint64_t icode)
{
   if(mem_error)
   {
      return INOP;
   }
   return icode;
}
uint64_t FetchStage::f_ifunn (bool mem_error, uint64_t ifun)
{
   if(mem_error)
   {
      return FNONE;
   }
   return ifun;
}
uint64_t FetchStage::selectPC(F * freg, M * mreg, W * wreg){
   uint64_t f_pc =0;
   if (mreg->geticode()->getOutput() == IJXX && !(mreg->getCnd()->getOutput())){
      f_pc = mreg->getvalA()->getOutput();
   }
   else if(wreg->geticode()->getOutput() == IRET){
      f_pc = wreg->getvalM()->getOutput();
   }
   else{
      f_pc = freg->getpredPC()->getOutput();
   }

   return f_pc;


}


bool FetchStage::needvalC(uint64_t f_icode){
   if (f_icode == IIRMOVQ || f_icode ==  IRMMOVQ || f_icode == IMRMOVQ || f_icode == IJXX || f_icode == ICALL){
      return true;
   }
   else{
      return false;
   }
}


uint64_t FetchStage::predictPC(uint64_t f_icode, uint64_t f_valC, uint64_t f_valP) {
    uint64_t f_predPC = f_valP; 

    if (f_icode == IJXX || f_icode == ICALL) {
        f_predPC = f_valC;
    }
    return f_predPC;
}
bool FetchStage::instr_valid(uint64_t f_icode)
{
   switch(f_icode)
   {
      case INOP :
         return true;
      case IHALT :
         return true;
      case IRRMOVQ :
         return true;
      case IIRMOVQ :
         return true;
      case IRMMOVQ :
         return true;
      case IMRMOVQ :
         return true;
      case IOPQ :
         return true;
      case IJXX :
         return true;
      case ICALL :
         return true;
      case IRET :
         return true;
      case IPUSHQ :
         return true;
      case IPOPQ :
         return true;
      default :
         return false;
   }

}
uint64_t FetchStage::f_stat(bool mem_error, bool instr_valid, uint64_t f_icode)
{
   if(mem_error)
   {
      return SADR;
   }
   else if(!instr_valid)
   {
      return SINS;
   }
   else if(f_icode == IHALT)
   {
      return SHLT;
   }
   return SAOK;

}

//Set fstall && dstall
bool FetchStage::fStall(uint64_t E_icode,uint64_t D_icode,uint64_t M_icode, uint64_t E_dstM, uint64_t d_srcA, uint64_t d_srcB){
   return ((E_icode == IMRMOVQ || E_icode == IPOPQ)
         && (E_dstM == d_srcA || E_dstM == d_srcB) || ((IRET == E_icode || IRET == D_icode || IRET == M_icode)));
}

bool FetchStage::dStall(uint64_t E_icode, uint64_t E_dstM, uint64_t d_srcA, uint64_t d_srcB){
   return ((E_icode == IMRMOVQ || E_icode == IPOPQ)
         && (E_dstM == d_srcA || E_dstM == d_srcB));
}
bool FetchStage::bubble_D(uint64_t E_icode, bool e_Cnd, DecodeStage * d, D * dreg, E * ereg, M*mreg)
{
   uint64_t E_dstM = ereg->getdstM()->getOutput();
   if((E_icode == IJXX && !e_Cnd) || !(E_icode == IMRMOVQ || E_icode == IPOPQ && (E_dstM == d->getdsrcA() && ereg->getdstM()->getOutput() == d->getdsrcB()  ))
   && (IRET == E_icode || IRET == dreg->geticode()->getOutput()||IRET == mreg->geticode()->getOutput()))
   {
      return true;
   }
   return false;
}
void FetchStage::calculateControlSingals(bool * fstall, bool * dstall, DecodeStage * d, E * ereg, ExecuteStage * e, D * dreg, M * mreg ){
   *fstall = fStall(ereg->geticode()->getOutput(), dreg->geticode()->getOutput(),mreg ->geticode()->getOutput(), ereg->getdstM()->getOutput(), d->getdsrcA(), d->getdsrcB());
   *dstall = dStall(ereg->geticode()->getOutput(), ereg->getdstM()->getOutput(), d->getdsrcA(), d->getdsrcB());
   D_bubble = bubble_D(ereg->geticode()->getOutput(),e->getCnd(),d,dreg,ereg,mreg);
}

bool FetchStage::needRegIds(uint64_t f_icode)
{
  
   
   if(f_icode == IRRMOVQ ||f_icode == IOPQ||f_icode == IPUSHQ||f_icode == IPOPQ||f_icode == IIRMOVQ||f_icode == IRMMOVQ||f_icode == IMRMOVQ)
   {
      return true;
   }
   else
   {
      return false;
   }

}

uint64_t FetchStage::PCincrement(uint64_t f_pc, bool needRegIds, bool needValC) {
    uint64_t next_pc = f_pc + 1; // increment by default
    if (needRegIds) {
        next_pc += 1; // 2 for instructions that need register IDs
    }
    if (needValC) {
        next_pc += 8; // 8 for instructions that need a 64-bit immediate value
    }
    return next_pc;
}

uint64_t FetchStage::buildvalC(uint64_t f_pc, bool regIds) {
   bool err = false;
   uint8_t bytes[8];
   if (regIds) {
      for (int i = 0; i < 8; i++) {
         bytes[i] = Memory::getInstance()->getByte(f_pc + 2 + i, err);
      }
   }
   else{
      for (int i = 0; i < 8; i++) {
         bytes[i] = Memory::getInstance()->getByte(f_pc + 1 + i, err);
      }
   }
   
  
   return Tools::buildLong(bytes);
   
}