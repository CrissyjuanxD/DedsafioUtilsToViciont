# 🎬 Guía para Crear Nuevas Animaciones

## 📦 **Método 1: Resource Pack + ModelEngine**

### **Paso 1: Crear Animación en Blockbench**
1. Abrir **Blockbench**
2. Cargar modelo `casinod3.bbmodel`
3. Ir a **Animation** tab
4. Crear nueva animación:
   - `jackpot_explosion` - Efectos de explosión
   - `mystery_spin` - Giro misterioso
   - `lightning_effect` - Efectos de rayo
   - `rainbow_particles` - Partículas arcoíris

### **Paso 2: Exportar a Resource Pack**
```
assets/minecraft/models/item/
├── casinod3.json
└── animations/
    ├── jackpot_explosion.json    # Nueva animación
    ├── mystery_spin.json         # Nueva animación
    ├── lightning_effect.json     # Nueva animación
    └── rainbow_particles.json    # Nueva animación
```

### **Paso 3: Configurar en ModelEngine**
```yaml
# En ModelEngine config
casinod3:
  animations:
    jackpot_explosion:
      duration: 300  # 15 segundos
      loop: false
    mystery_spin:
      duration: 240  # 12 segundos
      loop: false
```

## 🎯 **Método 2: Usar Animaciones Existentes**

### **Reutilizar con Variaciones:**
```yaml
# Usar misma animación con diferentes efectos
slot_variant_1:
  animation: eon          # Reutilizar animación eon
  wait-time: 5.0         # Tiempo diferente
  
slot_variant_2:
  animation: eon          # Misma animación
  wait-time: 10.0        # Tiempo más largo
```

## ⚡ **Método 3: Efectos de Partículas (Sin Resource Pack)**

### **Añadir efectos visuales con código:**
```java
// En SlotMachineManager.java
private void playVisualEffects(Player player, String effectType) {
    Location loc = player.getLocation();
    
    switch(effectType) {
        case "jackpot":
            // Explosión de fuegos artificiales
            player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc, 50);
            player.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 1.0f);
            break;
            
        case "lightning":
            // Efecto de rayo
            player.getWorld().strikeLightningEffect(loc);
            player.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, loc, 30);
            break;
            
        case "rainbow":
            // Partículas de colores
            for(int i = 0; i < 20; i++) {
                player.getWorld().spawnParticle(Particle.REDSTONE, 
                    loc.add(Math.random()-0.5, Math.random(), Math.random()-0.5), 
                    1, new Particle.DustOptions(Color.fromRGB(
                        (int)(Math.random()*255), 
                        (int)(Math.random()*255), 
                        (int)(Math.random()*255)), 1.0f));
            }
            break;
    }
}
```

## 🎨 **Método 4: Animaciones Procedurales**

### **Crear efectos dinámicos:**
```java
// Animación de rotación
private void playSpinAnimation(Entity entity, int duration) {
    new BukkitRunnable() {
        int ticks = 0;
        float rotation = 0;
        
        @Override
        public void run() {
            if(ticks >= duration) {
                cancel();
                return;
            }
            
            rotation += 10; // Grados por tick
            Location loc = entity.getLocation();
            loc.setYaw(rotation);
            entity.teleport(loc);
            
            ticks++;
        }
    }.runTaskTimer(plugin, 0, 1);
}
```

## 📋 **Recomendaciones:**

### **✅ Fácil (Sin Resource Pack):**
- Reutilizar animaciones existentes
- Añadir efectos de partículas
- Cambiar tiempos de espera

### **⚙️ Intermedio (Con Resource Pack):**
- Modificar animaciones existentes
- Crear variaciones simples

### **🔥 Avanzado (Blockbench + ModelEngine):**
- Crear animaciones completamente nuevas
- Efectos complejos con keyframes
- Sincronización perfecta

## 🎯 **Limitaciones Actuales:**
- **Resource Pack** debe ser instalado por jugadores
- **ModelEngine** debe soportar las animaciones
- **Rendimiento** puede verse afectado con muchas animaciones

¿Qué método prefieres usar?