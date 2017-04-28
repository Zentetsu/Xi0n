CC=g++
CPPFLAGS=-g
LDFLAGS=
SRC=$(wildcard ./src/*.cpp) $(wildcard ./src/Communication/*.cpp) $(wildcard ./src/Mobility/*.cpp) $(wildcard ./src/Sensor/*.cpp)
OBJS=$(SRC:.cpp=.o)
DIREXEC=./exec/
DIRSRC=./src/
DIRBIN=./bin/
EXEC=main


all: clean main
	mv $(EXEC) $(DIREXEC)
	mv ./$(OBJS) $(DIRBIN)

%.o: %.cpp
	$(CC) -o $@ -c $< $(CPPFLAGS)

main: $(OBJS)
	$(CC) -o $@ $^ $(LDFLAGS)

clean: simple_clean
	rm -rf $(DIREXEC)$(EXEC)

simple_clean:
	rm -rf $(DIRBIN)*.o
	rm -rf $(DIREXEC)*.o
