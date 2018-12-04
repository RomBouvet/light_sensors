// Pour le timer
#include "Timer.h"
// Pour les structures et les defines
#include "Odysseus.h"

module OdysseusC{
	// Inferfaces utilisees
	uses{
		// Base
		interface Boot;
		// Leds pour tests et indiquer l'envoi de paquets
		interface Leds;
		// Timer (milli)
		interface Timer<TMilli>;
		// Capteur de luminosite
		interface Read<u_int16_t> as LightRead;

		// Un controlleur par mode de comm
		interface SplitControl as RadioControl;
		interface SplitControl as SerialControl;

		// Un listener par mode de comm
		interface Receive as RadioReceive;
		interface Receive as SerialReceive;
		
		// Un poster par mode de comm
		interface AMSend as RadioSend;	
		interface AMSend as SerialSend;
		
		// Un packet par mode de comm
		interface Packet as RadioPacket;
		interface Packet as SerialPacket;
	}
}
implementation{
	message_t packet;

	// Verrous pour eviter les conflits
	bool radio_locked;
	bool serial_locked;

	// Fonction de boot
	event void Boot.booted(){
		// Boot du controlleur radio
		call RadioControl.start();
		// Si le capteur est le maitre..
		if(TOS_NODE_ID==MASTER_ID)
			// .. on demarre le controlleur serie
			call SerialControl.start();
		// Sinon.. 
		else
			// .. on demarre le timer
			call Timer.startPeriodic(SAMPLING_FREQUENCY);
	}

	// Fonction de fin du controlleur radio
	event void RadioControl.startDone(error_t err){
		// S'il ne s'est pas lance..
		if(err!=SUCCESS)
			// .. on le redemarre
			call RadioControl.start();
	}

	// Meme chose pour le controlleur serie
	event void SerialControl.startDone(error_t err){
		if(err!=SUCCESS)
			call SerialControl.start();
	}

	// Trigger sur chaque cycle du timer
	event void Timer.fired(){
		// Appelle la lecture du capteur de luminosite
		call LightRead.read();
	}

	/**
	* Trigger sur reception de message radio
	* On verifie si les donnees recues sont correctes 
	* et on appelle l'envoie en serie
	* ! WORK IN PROGRESS !
	*/
	event message_t* RadioReceive.receive(message_t* buf_ptr, void* payload, uint8_t len){
		if(TOS_NODE_ID==MASTER_ID){
			if(len != sizeof(msg_t)) {return buf_ptr;}
			else{
				msg_t* rcm = (msg_t*)payload;
				if(len != sizeof(msg_t)){return buf_ptr;}
				else{
					msg_t* rcmSend = (msg_t*)call SerialPacket.getPayload(&packet, sizeof(msg_t));
					if (rcmSend == NULL) {return buf_ptr;}
					rcmSend->id = rcm->id; 
					rcmSend->light = rcm->light;
					if (rcm == NULL) {return buf_ptr;}
					if (call RadioPacket.maxPayloadLength()<sizeof(msg_t)){return buf_ptr;}
					if (call SerialSend.send(AM_BROADCAST_ADDR, &packet, sizeof(msg_t)) == SUCCESS)
						serial_locked = TRUE;
				}
			}
		}
		else{
			if(len != sizeof(msg_t)) {return buf_ptr;}
			else{
				msg_t* rcm = (msg_t*)payload;
				if(len != sizeof(msg_t)){return buf_ptr;}
				else{
					if(rcm->id==MASTER_ID){
						msg_t* rcmSend = (msg_t*)call RadioPacket.getPayload(&packet, sizeof(msg_t));
						if (rcmSend == NULL) {return buf_ptr;}
						rcmSend->id = rcm->id; 
						rcmSend->light = rcm->light;
						if (rcm == NULL) {return buf_ptr;}
						if (call RadioPacket.maxPayloadLength()<sizeof(msg_t)){return buf_ptr;}
						if (call RadioSend.send(MASTER_ID, &packet, sizeof(msg_t)) == SUCCESS)
							serial_locked = TRUE;
					}
					
				}
			}
		}
		return buf_ptr;
	}

	/**
	* Trigger sur la reception de donnees en serie
	* Rien pour l'instant
	*/
	event message_t* SerialReceive.receive(message_t* buf_ptr, void* payload, uint8_t len){return buf_ptr;}

	/**
	* Fonction de fin de mesure de la luminosite
	* On construit le paquet en faisant directement la conversion pour la luminosite
	*/
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

	/**
	* Fonction de fin d'envoi serie
	* On debloque le verrou 
	*/
	event void SerialSend.sendDone(message_t* buf_ptr, error_t error){
		if (&packet == buf_ptr) {
		  serial_locked = FALSE;
		}
	}

	/**
	* Meme chose pour l'envoi radio
	*/
	event void RadioSend.sendDone(message_t* buf_ptr, error_t error){
		if (&packet == buf_ptr) {
		  radio_locked = FALSE;
		}
	}

	/**
	* Fonction de fin des controlleurs
	*/
	event void RadioControl.stopDone(error_t err){}
	event void SerialControl.stopDone(error_t err){}

	/**
	* Actualisation sur commande des capteurs
	*/
	task void refresh(){
		msg_t* rcmSend = (msg_t*)call RadioPacket.getPayload(&packet, sizeof(msg_t));
		if (rcmSend == NULL) {return;}
		rcmSend->id = TOS_NODE_ID; 
		if (call RadioSend.send(AM_BROADCAST_ADDR, &packet,sizeof(msg_t)) == SUCCESS){
			call Leds.led0Toggle();
			radio_locked = TRUE;
		}
	}
	
}
