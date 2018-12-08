# Odysseus

Odysseus est une application permettant de mesurer la luminosité à partir d'un réseaux de capteurs telosb

## Installation

TinyOS est nécessaire.
```
git clone https://github.com/RomBouvet/light_sensors
make telosb
```

Installer l'application sur chaque capteur en spécifiant l'id
Il faudra que les ports soient accessibles (sudo chmod 666 port)
```
make telosb reinstall,id
```

Si plusieurs capteurs sont branchés en même temps
```
make telosb reinstall,id bsl,port
```

## Usage

```
java OdysseusApp -comm serial@port:telosb
```

## Configuration

Il est possible de changer le fired() des timers ou l'id du maitre dans le fichier Odysseus.h

## TODO

* Create tasks for send

