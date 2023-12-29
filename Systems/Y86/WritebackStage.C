#include <string>
#include <cstdint>
#include "RegisterFile.h"
#include "PipeRegField.h"
#include "PipeReg.h"
#include "F.h"
#include "D.h"
#include "M.h"
#include "W.h"
#include "Stage.h"
#include "WritebackStage.h"
#include "Status.h"
#include "Debug.h"
#include  "Instructions.h"
#include <iostream>

bool WritebackStage::doClockLow(PipeReg ** pregs, Stage ** stages){
    M * mreg = (M*)pregs[MREG];
    W * wreg = (W*)pregs[WREG];
  bool err = false;
   uint64_t stat = wreg->getstat()->getOutput();
   uint64_t icode = wreg->geticode()->getOutput();
   
   uint64_t valE  = wreg->getvalE()->getOutput();
   uint64_t valM = wreg->getvalM()->getOutput();
   uint64_t dstE = wreg->getdstE()->getOutput();
   uint64_t dstM = wreg->getdstM()->getOutput();
   //std::cout<<"valM: "<<valM<<std::endl;
   //std::cout<<"valE: "<<valE<<std::endl;

   
    reg_w(valE,dstE,err);
    reg_w(valM,dstM,err);
    
    if(stat != SAOK)
    {
        return true;
    }
    else{
    return false;
    }

}

void WritebackStage::doClockHigh(PipeReg ** pregs){
    W * wreg = (W*)pregs[WREG];
    bool err;
    reg_w(wreg->getvalM()->getOutput(),wreg->getdstM()->getOutput(),err);
}

void WritebackStage::reg_w(uint64_t w_val,uint64_t reg, bool err){
   
   RegisterFile::getInstance()->writeRegister(w_val,reg,err);
}
   
