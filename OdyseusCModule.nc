#include <stdio.h>
#include <string.h>
module OdyseusAppC
{
	uses{   

		// Radio
		interface SplitControl as RadioControl;
		interface Receive as RadioReceive;  // interface Receive;
		interface AMSend as RadioAMSend; // interface AMSend;
		interface Packet as RadioPacket;
		interface AMPacket as RadioAMPacket;

		// LEDs
		interface Leds;

		// Luminosité sensor

		interface Read<u_int16_t> as LightRead;
		interface Boot;
		interface Timer<TMilli> as Timer0;

	} // fin de uses
}
implementation{// début implimentation
	// partie Radio Count to leds Modifier //////////////////////////:::
	message_t packet;

	bool locked;

	event void Boot.booted()
	{
	call RadioControl.start();
	}
	// fin event boot

	event void RadioControl.startDone(error_t err) {
	if (err == SUCCESS) {
	call MilliTimer.startPeriodic(250);
	}
	else {
	call RadioControl.start();
	} // fin else
	} // fin void RadioControl

	event void RadioControl.stopDone(error_t err) {
	// do nothing
	} 

	task void readSensor()
	{
		if(call LightRead.read() != SUCCESS){
			post readSensor();
		}
	} // fin readSensor()

	task void sendPacket() 	{
		if(call RadioAMSend.send(AM_BROADCAST_ADDR, &packet, sizeof(msg_t)) != SUCCESS)
			post sendPacket();
	} //fin sendpacket

	// lecture de luminosité
	event void LightRead.readDone(error_t err, uint16_t val){
		if(err != SUCCESS){
			post readSensor();
		}
		else{
			msg_t * payload = (msg_t *)call Packet.getPayload(&packet, sizeof(msg_t));
			payload->monid= TOS_NODE_ID;
			payload->light = val;
			post sendPacket();
		}
	} //fin void LightRead
	event void MilliTimer.fired() {
		if (locked) {
			return;
		} // la radio est bloquée donc non disponible
		else { // Maintenant la radio est disponible
			msg_t* rcm = (msg_t*)call Packet.getPayload(&packet, sizeof(msg_t));
			if (rcm == NULL)
				return;
			post readSensor();
			if (call RadioAMSend.send(AM_BROADCAST_ADDR, &packet, sizeof(msg_t)) == SUCCESS){			  
			  locked = TRUE;
			}
			////// Mesurer la luminosité ///////////////////

		} //fin else Radio disponible

	} // fin fired

	event message_t* RadioReceive.receive(message_t* bufPtr, void* payload, uint8_t len)
	{
	dbg("RadioCountToLedsC", "Received packet of length %hhu.\n", len);
	if (len != sizeof(msg_t)) {return bufPtr;}
	else {
	msg_t* rcm = (msg_t*)payload;

	else {
	call Leds.led2Off();
	}
	return bufPtr;
	} // fin else
	} // fin void message receive


	event void RadioAMSend.sendDone(message_t * msg, error_t err)
	{
	if(err != SUCCESS)
	post sendPacket();
	}

	event message_t * RadioReceive.receive(message_t * msg, void * payload, uint8_t len)
	{
	msg_t * payload2 = (msg_t *)payload;
	call Leds.set(payload2->reading / 200);
	return msg;
	}
} // fin de implimentation 

//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::		

