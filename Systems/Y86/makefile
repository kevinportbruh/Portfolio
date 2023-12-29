CC = g++
CFLAGS = -g -c -Wall -std=c++11 -O0
OBJ = yess.o Memory.o Tools.o RegisterFile.o \
ConditionCodes.o Loader.o Simulate.o F.o D.o \
E.o M.o W.o ExecuteStage.o MemoryStage.o DecodeStage.o \
FetchStage.o WritebackStage.o PipeRegField.o PipeReg.o

.C.o:
	$(CC) $(CFLAGS) $< -o $@





yess: $(OBJ)


yess.o: Debug.h Memory.h Loader.h RegisterFile.h  ConditionCodes.h \
PipeReg.h Stage.h Simulate.h

Loader.o: Loader.h Memory.h

Memory.o: Memory.h Tools.h

ConditionCodes.o: ConditionCodes.h Tools.h

RegisterFile.o: RegisterFile.h Tools.h

Tools.o: Tools.h

F.o: F.h PipeRegField.h PipeReg.h 

D.o: D.h Instructions.h RegisterFile.h PipeReg.h PipeRegField.h Status.h

E.o: E.h Instructions.h RegisterFile.h PipeReg.h PipeRegField.h Status.h

M.o: M.h Instructions.h RegisterFile.h PipeReg.h PipeRegField.h Status.h

W.o: W.h Instructions.h RegisterFile.h PipeReg.h PipeRegField.h Status.h

ExecuteStage.o: ExecuteStage.h RegisterFile.h PipeRegField.h PipeReg.h F.h D.h M.h W.h Stage.h \
Status.h Debug.h

MemoryStage.o: MemoryStage.h RegisterFile.h PipeRegField.h PipeReg.h F.h D.h M.h W.h Stage.h \
Status.h Debug.h

DecodeStage.o: DecodeStage.h RegisterFile.h PipeRegField.h PipeReg.h F.h D.h M.h W.h Stage.h \
Status.h Debug.h

FetchStage.o: FetchStage.h RegisterFile.h PipeRegField.h PipeReg.h F.h D.h M.h W.h Stage.h \
Status.h Debug.h

WritebackStage.o: WritebackStage.h RegisterFile.h PipeRegField.h PipeReg.h F.h D.h M.h W.h Stage.h \
Status.h Debug.h

PipeRegField.o: PipeRegField.h

PipeReg.o: PipeReg.h

Simulate.o: Simulate.h PipeRegField.h PipeReg.h F.h D.h E.h \
M.h W.h Stage.h ExecuteStage.h MemoryStage.h DecodeStage.h FetchStage.h \
WritebackStage.h Memory.h RegisterFile.h ConditionCodes.h


clean:
	rm $(OBJ) yess

run:
	make clean
	make yess
	./run.sh

