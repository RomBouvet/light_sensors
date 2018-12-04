# Odysseus

Odysseus est une application permettant de mesurer la luminosité à partir d'un réseaux de capteurs telosb

## Installation

TinyOS est nécessaire.
```
git clone https://github.com/RomBouvet/light_sensors
make telosb
```

Installer l'application sur chaque capteur en spécifiant l'id
```
make telosb reinstall,X
```

Si plusieurs capteurs sont branchés en même temps
```
make telosb reinstall,X bsl,/dev/ttyUSBY
```



## Usage

Work in progress...
