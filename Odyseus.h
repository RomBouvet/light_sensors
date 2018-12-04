
#ifndef Odyseus_H
#define Odyseus_H

typedef nx_struct msg {
  nx_uint16_t monid; // compter le nombre de message Radio
  nx_uint16_t light;
} msg_t;

enum {
  AM_RADIO_MSG = 20,
};

#endif
