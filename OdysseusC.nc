#include "Timer.h"
#include "Odysseus.h"

module OdysseusC{
	uses{
		interface SplitControl as RadioControl;
		interface SplitControl as SerialControl;

		interface Boot;
		interface Leds;
		interface Timer<TMilli>;
		interface Read<u_int16_t> as LightRead;

		interface Receive as RadioReceive;
		interface Receive as SerialReceive;
		
		interface AMSend as RadioSend;	
		interface AMSend as SerialSend;
		
		interface Packet as RadioPacket;
		interface Packet as SerialPacket;
	}
}
implementation{
	message_t packet;

	bool radio_locked;
	bool serial_locked;
	event void Boot.booted(){
		call RadioControl.start();
		if(TOS_NODE_ID==MASTER_ID)
			call SerialControl.start();
		else
			call Timer.startPeriodic(SAMPLING_FREQUENCY);
	}

	event void RadioControl.startDone(error_t err){
		if(err!=SUCCESS)
			call RadioControl.start();
	}

	event void SerialControl.startDone(error_t err){
		if(err!=SUCCESS)
			call SerialControl.start();
	}


	event void Timer.fired(){
		call LightRead.read();
	}

	event message_t* RadioReceive.receive(message_t* buf_ptr, void* payload, uint8_t len){
		if (len != sizeof(msg_t)) {return buf_ptr;}
		else {
			msg_t* rcm = (msg_t*)payload;
			if(len != sizeof(msg_t)){return buf_ptr;}
			else{
				msg_t* rcmSend = (msg_t*)call SerialPacket.getPayload(&packet, sizeof(msg_t));
				if (rcmSend == NULL) {return buf_ptr;}
				rcmSend->id = rcm->id; 
				rcmSend->light = rcm->light;
				if (rcm == NULL) {return buf_ptr;}
				if (call RadioPacket.maxPayloadLength() < sizeof(msg_t)) {
					return buf_ptr;
				}

				if (call SerialSend.send(AM_BROADCAST_ADDR, &packet, sizeof(msg_t)) == SUCCESS) {
					serial_locked = TRUE;
				}
				return buf_ptr;
			}
		}
	}

	event message_t* SerialReceive.receive(message_t* buf_ptr, void* payload, uint8_t len){return buf_ptr;}


	event void LightRead.readDone(error_t result, u_int16_t val){
		if (result == SUCCESS){
			if (radio_locked)
				return;
			else {
				msg_t* rcmSend = (msg_t*)call RadioPacket.getPayload(&packet, sizeof(msg_t));
				if (rcmSend == NULL) {return;}
				rcmSend->id = TOS_NODE_ID; 
				rcmSend->light = 3*(val/4096.0)*6250;
				if (call RadioSend.send(AM_BROADCAST_ADDR, &packet,sizeof(msg_t)) == SUCCESS){
					call Leds.led0Toggle();
					radio_locked = TRUE;
				}
			}

		}
		else{
			call LightRead.read();
		}
	}

	event void SerialSend.sendDone(message_t* buf_ptr, error_t error){
		if (&packet == buf_ptr) {
		  serial_locked = FALSE;
		}
	}

	event void RadioSend.sendDone(message_t* buf_ptr, error_t error){
		if (&packet == buf_ptr) {
		  radio_locked = FALSE;
		}
	}

	event void RadioControl.stopDone(error_t err){}
	event void SerialControl.stopDone(error_t err){}
	
}
