#include "Odysseus.h"

configuration OdysseusAppC {}
implementation {
	components MainC, OdysseusC as App, LedsC;
	components ActiveMessageC as Radio;
	components new AMSenderC(AM_RADIO_MSG);
	components new AMReceiverC(AM_RADIO_MSG);
	components SerialActiveMessageC as Serial;
	components new HamamatsuS10871TsrC() as LightSensor;
	components new TimerMilliC();

	App.Boot -> MainC.Boot;
	App.Leds -> LedsC;
	App.LightRead -> LightSensor;
	App.Timer -> TimerMilliC;

	App.RadioControl ->  Radio;
	App.RadioPacket -> AMSenderC;
	App.RadioReceive -> AMReceiverC;
 	App.RadioSend -> AMSenderC;

	App.SerialControl -> Serial;
	App.SerialPacket -> Serial;
	App.SerialReceive -> Serial.Receive[AM_MSG];
  	App.SerialSend -> Serial.AMSend[AM_MSG];
}

