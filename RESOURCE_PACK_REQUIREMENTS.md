# 📦 Resource Pack Requirements para SlotMachine

## 🎯 Archivos Necesarios:

### **1. Modelos 3D**
```
assets/minecraft/models/item/
├── casinod3.json                    # Modelo principal de la slot machine
└── custom/
    ├── slot_machine_idle.json       # Animación idle
    ├── slot_machine_eon.json        # Animación eon
    ├── slot_machine_dedita.json     # Animación dedita
    ├── slot_machine_nutria.json     # Animación nutria
    └── slot_machine_corazon.json    # Animación corazón
```

### **2. Texturas**
```
assets/minecraft/textures/item/
├── slot_machine_base.png           # Textura base
├── slot_machine_spinning.png       # Textura girando
└── slot_machine_symbols/
    ├── eon.png
    ├── dedita.png
    ├── nutria.png
    └── corazon.png
```

### **3. Sonidos**
```
assets/dtools3/sounds/tools/casino/
├── bet.ogg                         # Sonido al apostar
├── win.ogg                         # Sonido de victoria
├── lose.ogg                        # Sonido de pérdida
└── spin.ogg                        # Sonido de giro
```

### **4. Definición de Sonidos**
```json
// assets/dtools3/sounds.json
{
  "tools.casino.bet": {
    "sounds": ["dtools3:tools/casino/bet"]
  },
  "tools.casino.win": {
    "sounds": ["dtools3:tools/casino/win"]
  },
  "tools.casino.lose": {
    "sounds": ["dtools3:tools/casino/lose"]
  }
}
```

### **5. Custom Model Data**
```json
// En assets/minecraft/models/item/orange_glazed_terracotta.json
{
  "parent": "block/orange_glazed_terracotta",
  "overrides": [
    {
      "predicate": {
        "custom_model_data": 1000
      },
      "model": "item/casinod3"
    }
  ]
}
```

## 🎮 **Cómo Funciona:**

1. **Plugin** → Coloca bloque con `custom_model_data: 1000`
2. **Resource Pack** → Reemplaza el bloque con el modelo 3D
3. **Plugin** → Reproduce sonidos `dtools3:tools.casino.*`
4. **Resource Pack** → Proporciona los archivos de sonido
5. **Plugin** → Cambia animaciones via NBT/metadata
6. **Resource Pack** → Muestra diferentes modelos según el estado

## ⚠️ **Importante:**

- Los jugadores DEBEN tener el resource pack instalado
- Sin el resource pack, verán bloques normales y sonidos vanilla
- El plugin funciona igual, solo cambia la presentación visual/auditiva