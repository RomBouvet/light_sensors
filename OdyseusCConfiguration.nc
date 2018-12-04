#include "Odyseus.h"
#include "Timer.h"


configuration OdyseusC{}
implementation {
	components OdyseusAppC;
	// Partie Mesurer la luminosité
	components new HamamatsuS10871TsrC() as LightSensor;
	Odyseus.LightRead -> LightSensor;
	// partie les led 
	components LedsC;
	// led0 : rouge //- led1 : verte //- led2 : bleue
	Odyseus.Leds -> LedsC;


	// Partie Radio
	components ActiveMessageC as Radio;
	components new AMSenderC(AM_RADIO_MSG);
	components new AMReceiverC(AM_RADIO_MSG);
	Odyseus.RadioControl -> Radio;
	Odyseus.RadioAMSend -> AMSenderC;
	Odyseus.RadioReceive -> AMReceiverC;
	Odyseus.RadioPacket -> AMSenderC;
	Odyseus.RadioAMPacket -> Radio;

	// partie  Main et Timer
	components MainC;
	Odyseus.Boot -> MainC;
	components new TimerMilliC();
	Odyseus.Timer0 -> TimerMilliC;


} // fin implementation
