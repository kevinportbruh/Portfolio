// this is writeback class
class WritebackStage : public Stage {

    private:
void reg_w(uint64_t w_vale,uint64_t reg, bool err);

    public:
      
        bool doClockLow(PipeReg ** pregs, Stage ** stages);
        void doClockHigh(PipeReg ** pregs);


};