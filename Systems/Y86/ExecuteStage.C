#include <string>
#include <cstdint>
#include "RegisterFile.h"
#include "PipeRegField.h"
#include "PipeReg.h"
#include "Memory.h"
#include "F.h"
#include "D.h"
#include "E.h"
#include "M.h"
#include "W.h"
#include "Instructions.h"
#include "Stage.h"
#include "ExecuteStage.h"
#include "MemoryStage.h"
#include "Status.h"
#include "Debug.h"
#include "iostream"
#include "Tools.h"
#include "ConditionCodes.h"

//global variables
uint64_t e_dstE =0;
uint64_t e_valE =0;
bool e_Cnd;




bool ExecuteStage::doClockLow(PipeReg ** pregs, Stage ** stages){
    E * ereg = (E*)pregs[EREG];
    M * mreg = (M*)pregs[MREG];
    W * wreg = (W*)pregs[WREG];
    // mem stat instance to get m_stat
    MemoryStage *mem =(MemoryStage*)stages[MSTAGE];
    uint64_t m_stat = mem->getmstat();
    uint64_t W_stat = wreg->getstat()->getOutput();
    //
    uint64_t stat = ereg->getstat()->getOutput();
    uint64_t icode = ereg->geticode()->getOutput();
    uint64_t ifun = ereg->getifun()->getOutput();
    uint64_t valA =ereg->getvalA()->getOutput(), valB = ereg->getvalB()->getOutput();
    uint64_t valC = ereg->getvalC()->getOutput();
    uint64_t dstM = ereg->getdstM()->getOutput();
     e_Cnd = cond(icode,ifun);
    M_bubble = calculateControlSignals(m_stat,W_stat); // defined in private
    

    uint64_t aluAval = aluA(icode, valC, valA);
    uint64_t aluBval = aluB(icode, valB);

    e_valE = aluOut(aluAval, aluBval, aluFun(icode, ifun));
    e_dstE = edstE(icode,ereg->getdstE()->getOutput(), e_Cnd);  

    ccComp(setCC(icode,m_stat,W_stat), e_valE, aluAval, aluBval, aluFun(icode, ifun)); 
    /*
    print debugging statement god was here
    //std::cout << "valB in execute: " << valB << std::endl;
    //std::cout << "valA in execute: " << valA << std::endl;
    //std::cout << "srcA FROM EXECUTE: " << ereg->getsrcA()->getOutput() << std::endl;
    //std::cout << "srcB FROM EXECUTE: " << ereg->getsrcB()->getOutput() << std::endl;
    //std::cout << "e_valc: " << valC << std::endl;     
    //std::cout << "e_valc2: " << ereg->getvalC()->getOutput() << std::endl;
    //std::cout << "e_valE: " << e_valE << std::endl;
    */
     
    setMInput(mreg, stat, icode, e_Cnd, e_valE, valA, e_dstE, dstM);
}

void ExecuteStage::doClockHigh(PipeReg ** pregs){
    E * ereg = (E*)pregs[EREG];
    M * mreg = (M*)pregs[MREG];
    if(!M_bubble)
    {
    mreg->getstat()->normal();
    mreg->geticode()->normal();
    mreg->getCnd()->normal();
    mreg->getvalE()->normal();
    mreg->getvalA()->normal();
    mreg->getdstE()->normal();
    mreg->getdstM()->normal();
    }
    else
    {
    mreg->getstat()->bubble(SAOK);
    mreg->geticode()->bubble(INOP);
    mreg->getCnd()->bubble();
    mreg->getvalE()->bubble();
    mreg->getvalA()->bubble();
    mreg->getdstE()->bubble(RNONE);
    mreg->getdstM()->bubble(RNONE);
    }
}


void ExecuteStage::setMInput(M * mreg, uint64_t stat, uint64_t icode, uint64_t Cnd,
                            uint64_t valE, uint64_t valA, uint64_t dstE, uint64_t dstM)
{
    mreg->getstat()->setInput(stat);
    mreg->geticode()->setInput(icode);
    mreg->getCnd()->setInput(Cnd);
    mreg->getvalE()->setInput(valE);
    mreg->getvalA()->setInput(valA);
    mreg->getdstE()->setInput(dstE);
    mreg->getdstM()->setInput(dstM);
}
uint64_t ExecuteStage::aluFun(uint64_t icode, uint64_t ifun)
{
    if(icode == IOPQ)
    {
        
        return ifun;
    }
    else
    {
       
        return ADDQ;
    }

}
bool ExecuteStage::cond(uint64_t icode,uint64_t ifun)
{
    bool poo = false;
    uint8_t ofcond = ConditionCodes::getInstance()->getConditionCode(OF,poo);
    uint8_t zfcond = ConditionCodes::getInstance()->getConditionCode(ZF,poo);
    uint8_t sfcond = ConditionCodes::getInstance()->getConditionCode(SF,poo);
    if(icode == IJXX || icode == ICMOVXX)
    {
        if(ifun == UNCOND)
        {
            return 1;
        }
        else if(ifun == LESSEQ )
        {
            return (sfcond^ofcond)|zfcond;
        }
        else if(ifun == LESS )
        {
            return sfcond^ofcond;
        }
        else if(ifun == EQUAL)
        {
            return zfcond;
        }
        else if(ifun == NOTEQUAL)
        {
            return !(zfcond);
        }
        else if(ifun == GREATER)
        {
            return !(sfcond^ofcond)&!zfcond;
        }
        else if(ifun == GREATEREQ)
        {
            return !(sfcond^ofcond);
        }
        
    }
    else
    return 0;

}
uint64_t ExecuteStage::aluA(uint64_t icode, uint64_t valC, uint64_t valA )
{
    if(icode == IRRMOVQ || icode == IOPQ)
    {
        return valA;
    }
    else if(icode == IIRMOVQ || icode == IRMMOVQ || icode == IMRMOVQ)
    {
        return valC;
    }
    else if(icode == ICALL || icode == IPUSHQ)
    {
        return -8;
    }
    else if(icode == IRET || icode == IPOPQ)
    {
        return 8;
    }
    else
    {
        return 0;
    }

}



uint64_t ExecuteStage::aluB(uint64_t E_icode, uint64_t E_valB) {
    if(E_icode == IRMMOVQ||E_icode == IMRMOVQ||E_icode == IOPQ||E_icode == ICALL 
                                ||E_icode == IPUSHQ||E_icode == IRET||E_icode == IPOPQ){
        return E_valB;
    }
    else if(E_icode == IRRMOVQ||E_icode == IIRMOVQ){
        return 0;
    }
    else{
        return 0;
    }
}

bool ExecuteStage::setCC(uint64_t E_icode, uint64_t m_stat, uint64_t W_stat){
    if(E_icode == IOPQ){
        if(m_stat != SADR && m_stat != SINS && m_stat != SHLT)
        {
            if(W_stat != SADR && W_stat != SINS && W_stat != SHLT)
            {
                return true;
            }
            else
            {
            return false;
            }
        }
        else
        {
        return false;
        }
    }
    else{
        return false;
    }
}
bool ExecuteStage::calculateControlSignals(uint64_t m_stat,uint64_t W_stat)
{
    if(m_stat == SADR || m_stat == SINS || m_stat == SHLT || W_stat == SADR || W_stat == SINS || W_stat == SHLT)
    {
        return true;
    }
    return false;
}
uint64_t ExecuteStage::edstE(uint64_t E_icode, uint64_t E_dstE, uint64_t e_Cnd){
    
    if(E_icode == IRRMOVQ && !e_Cnd){
        return RNONE;
    }
    
        return E_dstE;
    
}

void ExecuteStage::ccComp(bool cc, uint64_t val, uint64_t valA, uint64_t valB, uint64_t aluFun){
    bool err = false;
    /*
    std::cout << "cc: " << cc << std::endl;
    std::cout << "val: " << val << std::endl;
    std::cout << "valA: " << valA << std::endl;
    std::cout << "valB: " << valB << std::endl;
    std::cout << "aluFun: " << aluFun << std::endl;
    */
    if (cc){
        //ZF
        if((val& 0xFFFFFFFFFFFFFFFF) == 0){
            //std::cout << "ZF is true" << std::endl;
            ConditionCodes::getInstance()->setConditionCode(true, ZF, err);
        }
        else{
            //std::cout << "ZF is false" << std::endl;
            ConditionCodes::getInstance()->setConditionCode(false, ZF, err);
        }
        //SF
        if (Tools::sign(val) == 1){
            //std::cout << "SF is true" << std::endl;
            ConditionCodes::getInstance()->setConditionCode(true, SF, err);
        }
        else{
           // std::cout << "SF is false" << (val <0) << std::endl;
            ConditionCodes::getInstance()->setConditionCode(false, SF, err);
        }


        //OF is only set for add and sub
        if (aluFun == ADDQ){
            if(Tools::addOverflow(valA,valB) == 1){
                ConditionCodes::getInstance()->setConditionCode(true, OF, err);
            }
            else{
                ConditionCodes::getInstance()->setConditionCode(false, OF, err);
            }
        }
        else if (aluFun == SUBQ){
            if(Tools::subOverflow(valA,valB) == 1){
                ConditionCodes::getInstance()->setConditionCode(true, OF, err);
            }
            else{
                ConditionCodes::getInstance()->setConditionCode(false, OF, err);
            }
        }

    }

}

uint64_t ExecuteStage::aluOut(uint64_t aluA, uint64_t aluB, uint64_t aluFun )
{
    if(aluFun == ADDQ)
    {
        return aluA + aluB;
    }
    else if(aluFun == SUBQ)
    {
        return aluB - aluA;
    }
    else if(aluFun == ANDQ)
    {
        return aluA & aluB;
    }
    else if(aluFun == XORQ)
    {
        return aluA ^ aluB;
    }
}

uint64_t ExecuteStage::getedstE(){
    return e_dstE;
}

uint64_t ExecuteStage::getevalE(){
    return e_valE;
}
bool ExecuteStage::getCnd()
{
    return e_Cnd;
}