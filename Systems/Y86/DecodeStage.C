#include <string>
#include <cstdint>
#include "RegisterFile.h"
#include "PipeRegField.h"
#include "PipeReg.h"
#include "F.h"
#include "D.h"
#include "M.h"
#include "E.h"
#include "W.h"
#include "Stage.h"
#include "Instructions.h"
#include "ExecuteStage.h"
#include "MemoryStage.h"
#include "DecodeStage.h"
#include "Instructions.h"
#include "Status.h"
#include "Debug.h"
#include <iostream>

//Brandon is working on this
bool E_bubble;
bool DecodeStage::doClockLow(PipeReg ** pregs, Stage ** stages)
{
   
   //get instance of executeStage
   ExecuteStage *exec =(ExecuteStage*)stages[ESTAGE];
   MemoryStage *mem =(MemoryStage*)stages[MSTAGE];

   bool err;
   D * dreg = (D*)pregs[DREG];
   E * ereg = (E*)pregs[EREG];
   W * wreg = (W*)pregs[WREG];
   M * mreg = (M*)pregs[MREG];
   
   uint64_t stat = dreg->getstat()->getOutput();
   uint64_t icode = dreg->geticode()->getOutput();
   uint64_t ifun = dreg->getifun()->getOutput();
   uint64_t valC  = dreg->getvalC()->getOutput();
   uint64_t valP = dreg->getvalP()->getOutput();
   uint64_t dstE = d_dstE(icode,(dreg->getrB()->getOutput()));
   uint64_t dstM = d_dstM(icode,(dreg->getrA()->getOutput()));
   srcA = d_srcA(icode,dreg->getrA()->getOutput());// d_srcA
   srcB = d_srcB(icode,dreg->getrB()->getOutput());//d_srcB
   uint64_t valB = fwdB(srcB, mreg, wreg,mem, exec);
   uint64_t valA = selFwdA(srcA, mreg, wreg,mem, exec,valP,icode);
   E_bubble = calculateControlSignals(ereg->geticode()->getOutput(),ereg->getdstM()->getOutput(),exec->getCnd()); // defined in private

   //std::cout << "valA: " << valA << std::endl;
   //std::cout << "valB IN DECODE: " << valB << std::endl;
   //std::cout << "srcB IN DECODE: " << srcB << std::endl;

   setEInput(ereg, stat, icode, ifun, valC, valA, valB, dstE, dstM,srcA,srcB);
}
bool DecodeStage::calculateControlSignals(uint64_t E_icode, uint64_t E_dstM,bool Cnd)
{
   if((E_icode == IJXX && !Cnd)||((E_icode == IMRMOVQ || E_icode == IPOPQ) &&(E_dstM == srcA || E_dstM == srcB)) )
   {
      return true;
   }
   return false;
}

void DecodeStage::doClockHigh(PipeReg ** pregs){
   E * ereg = (E *) pregs[EREG];
   D * dreg = (D *) pregs[DREG];
   if(!E_bubble)
   {
   ereg->getstat()->normal();
   ereg->geticode()->normal();
   ereg->getifun()->normal();
   ereg->getvalC()->normal();
   ereg->getvalA()->normal();
   ereg->getvalB()->normal();
   ereg->getdstE()->normal();
   ereg->getdstM()->normal();
   ereg->getsrcA()->normal();
   ereg->getsrcB()->normal();
   }
   else
   {
   ereg->getstat()->bubble(SAOK);
   ereg->geticode()->bubble(INOP);
   ereg->getifun()->bubble();
   ereg->getvalC()->bubble();
   ereg->getvalA()->bubble();
   ereg->getvalB()->bubble();
   ereg->getdstE()->bubble(RNONE);
   ereg->getdstM()->bubble(RNONE);
   ereg->getsrcA()->bubble(RNONE);
   ereg->getsrcB()->bubble(RNONE);
   }
}

uint64_t DecodeStage::d_srcA(uint64_t icode, uint64_t d_rA )
{
   uint64_t d_srcA;
   if(icode == IRRMOVQ || icode == IRMMOVQ || icode == IOPQ || icode == IPUSHQ)
   {
      d_srcA = d_rA;
   }
   else if(icode == IPOPQ || icode == IRET)
   {
      d_srcA = RSP;

   }
   else{
      return RNONE;
   }
   return d_srcA;

}
uint64_t DecodeStage::d_srcB(uint64_t icode, uint64_t d_rB )
{
   
   if(icode == IRMMOVQ || icode == IOPQ || icode == IMRMOVQ)
   {
     //std::cout<<"returning d_rB for srcB"<< std::endl;
     return d_rB;
   }
   else if(icode == IPUSHQ || icode == IPOPQ || icode == ICALL || icode == IRET)
   {
      //std::cout<<"returning RSP for srcB"<< std::endl;
      return RSP;
   }
   
   //std::cout<<"returning rnone for srcB"<< std::endl;
   return RNONE;

   

}
void DecodeStage::setEInput(E * ereg, uint64_t stat, uint64_t icode,
                              uint64_t ifun, uint64_t valC, uint64_t valA,
                              uint64_t valB, uint64_t dstE, uint64_t dstM,
                              uint64_t srcA, uint64_t srcB)
{
   ereg->getstat()->setInput(stat);
   ereg->geticode()->setInput(icode);
   ereg->getifun()->setInput(ifun);
   ereg->getvalC()->setInput(valC);
   ereg->getvalA()->setInput(valA);
   ereg->getvalB()->setInput(valB);
   ereg->getdstE()->setInput(dstE);
   ereg->getdstM()->setInput(dstM);
   ereg->getsrcA()->setInput(srcA);
   ereg->getsrcB()->setInput(srcB);
  
}


uint64_t DecodeStage::d_dstE(uint64_t D_icode, uint64_t D_rb){
   uint64_t d_dstE = RNONE; //default
   if(D_icode == IRRMOVQ || D_icode == IIRMOVQ || D_icode == IOPQ)
   {
      d_dstE = D_rb;
   }
   else if (D_icode == IPUSHQ || D_icode == IPOPQ || D_icode == ICALL || D_icode == IRET){
      d_dstE = RSP;
   }
   return d_dstE;
}

uint64_t DecodeStage::d_dstM(uint64_t D_icode, uint64_t D_ra){
   uint64_t d_dstM = RNONE; //default
   if(D_icode == IMRMOVQ || D_icode == IPOPQ) {
      d_dstM = D_ra;
   }
   return d_dstM;
}

uint64_t DecodeStage::fwdB(uint64_t srcB, M * mreg, W * wreg,MemoryStage * mem, ExecuteStage * exec){
   bool flag =0;
  
   if(srcB == RNONE){
      //std::cout<<"CHOSE RNONE"<<std::endl;
      return 0;
   }
   else if (srcB == exec->getedstE()){
      //std::cout<<"fwdB from evale"<<std::endl;
      return exec->getevalE();
   }
   else if(srcB == mreg->getdstM()->getOutput())
   {
      return mem->getmvalM();
   }

   
   else if (srcB == mreg->getdstE()->getOutput()){
      //std::cout<<"fwdB from mvale"<<std::endl;
      return mreg->getvalE()->getOutput();
   }
   else if (srcB == wreg->getdstM()->getOutput()){
      //std::cout<<"fwdB from wvale"<<std::endl;
      return wreg->getvalM()->getOutput();
   }
   else if (srcB == wreg->getdstE()->getOutput()){
      //std::cout<<"fwdB from wvale"<<std::endl;
      return wreg->getvalE()->getOutput();
   }

   //std::cout<<"fwdB from reg& srcb ="<< RegisterFile::getInstance()->readRegister(srcB, flag) <<std::endl;
   return RegisterFile::getInstance()->readRegister(srcB, flag);
}

uint64_t DecodeStage::selFwdA(uint64_t srcA,M * mreg,W * wreg,MemoryStage * mem, ExecuteStage * exec,uint64_t valP,uint64_t icode){
   bool flag =0;
   if(icode == ICALL || icode == IJXX)
   {
      return valP;
   }
   else if(srcA == RNONE){
    return 0;
   }
   
   else if (srcA == exec->getedstE()){
      return exec->getevalE();
   }
   else if(srcA == mreg->getdstM()->getOutput())
   {
      return mem->getmvalM();
   }

   else if (srcA == mreg->getdstE()->getOutput()){
      return mreg->getvalE()->getOutput();
   }
   else if (srcA == wreg->getdstM()->getOutput()){
      return wreg->getvalM()->getOutput();
   }
   else if (srcA == wreg->getdstE()->getOutput()){
      return wreg->getvalE()->getOutput();
   }
   return RegisterFile::getInstance()->readRegister(srcA, flag); 
}
uint64_t DecodeStage::getdsrcA()
{
   return srcA;
}
uint64_t DecodeStage::getdsrcB()
{
   return srcB;
}


