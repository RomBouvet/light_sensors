COMPONENT = OdysseusAppC
CC2420_CHANNEL = 11
CFlags = -DCC2420_DEF_RFPower= 5

BUILD_EXTRA_DEPS = Odysseus.class OdysseusApp.class
CLEAN_EXTRA = Odysseus.py Odysseus.class Odysseus.java

Odysseus.py: Odysseus.h
	mig python -target=$(PLATFORM) $(CFLAGS) -python-classname=Odysseus Odysseus.h msg -o $@

Odysseus.class: Odysseus.java
	javac Odysseus.java

Odysseus.java: Odysseus.h
	mig java -target=$(PLATFORM) $(CFLAGS) -java-classname=Odysseus Odysseus.h msg -o $@

OdysseusApp.class: $(wildcard *.java) Odysseus.java
	javac -target 1.4 -source 1.4 *.java


include $(MAKERULES)

