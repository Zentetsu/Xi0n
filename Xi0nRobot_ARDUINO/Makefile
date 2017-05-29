CC=g++
CPPFLAGS=-g -idirafter /Volumes/Macintosh\ HD/Applications/Arduino.app/ 
LDFLAGS=-idirafter /Volumes/Macintosh\ HD/Applications/Arduino.app/ 
SRC=$(wildcard ./*.cpp)
OBJS=$(SRC:.cpp=.o)
DIREXEC=./
DIRSRC=./
DIRBIN=./
EXEC=main


all: clean main
	#mv $(EXEC) $(DIREXEC)
	#mv ./$(OBJS) $(DIRBIN)

%.o: %.cpp
	$(CC) -o $@ -c $< $(CPPFLAGS)

main: $(OBJS)
	$(CC) -o $@ $^ $(LDFLAGS)

clean: simple_clean
	rm -rf $(DIREXEC)$(EXEC)

simple_clean:
	rm -rf $(DIRBIN)*.o
	rm -rf $(DIREXEC)*.o
