COMPONENT = OdysseusAppC
CC2420_CHANNEL = 11
CFlags = -DCC2420_DEF_RFPower= 5

BUILD_EXTRA_DEPS = Odysseus.class OdysseusApp.class
CLEAN_EXTRA = Odysseus.py Odysseus.class Odysseus.java

Odysseus.class: Odysseus.java
	javac Odysseus.java

Odysseus.java: Odysseus.h
	mig java -target=$(PLATFORM) $(CFLAGS) -java-classname=Odysseus Odysseus.h msg -o $@

OdysseusApp.class: $(wildcard *.java) GraphicInterface.java Odysseus.java
	javac -cp ".:jfreechart-1.0.19/jfreechart-1.0.19-demo.jar:${TOSROOT}/support/sdk/java/tinyos.jar" -target 1.5 -source 1.5 *.java

include $(MAKERULES)

clean:
	rm *.class
