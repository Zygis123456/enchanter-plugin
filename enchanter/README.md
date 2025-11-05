# Enchanter Plugin

🎮 **Custom enchantmentų pluginas Minecraft serveriams**

## Aprašymas

Enchanter yra Bukkit/Spigot pluginas, kuris prideda 8 unikalius custom enchantmentus į jūsų Minecraft serverį. Kiekvienas enchantmentas turi savo specialų poveikį ir vizualinius efektus.

## Enchantmentai

### 1. **Unbreakable** 🛡️
- **Aprašymas**: Šansas, kad daiktas nepraras tvarumo
- **Maksimalus lygis**: 5
- **Šansas**: 20% per lygį
- **Suderinamumas**: Visi daiktai

### 2. **Explosive** 💥
- **Aprašymas**: Sukelia sprogimą pažeidžiant priešą
- **Maksimalus lygis**: 3
- **Suderinamumas**: Kardai
- **Efektai**: Sprogimas, dalelės, garsai

### 3. **Lifesteal** ❤️
- **Aprašymas**: Atgauna gyvybę pažeidžiant priešus
- **Maksimalus lygis**: 5
- **Gydymas**: 10% žalos per lygį
- **Suderinamumas**: Kardai

### 4. **AutoRepair** 🔧
- **Aprašymas**: Automatiškai taiso daiktą laikui bėgant
- **Maksimalus lygis**: 3
- **Šansas**: 5% per lygį per naudojimą
- **Suderinamumas**: Visi daiktai

### 5. **Lightning** ⚡
- **Aprašymas**: Šansas iškviesti žaibą ant priešo
- **Maksimalus lygis**: 3
- **Šansas**: 10% per lygį
- **Papildoma žala**: 2.0 per lygį

### 6. **Teleport** 🌀
- **Aprašymas**: Šansas teleportuotis už priešo nugaros arba pabėgti
- **Maksimalus lygis**: 2
- **Šansas (užpuolimas)**: 15% per lygį
- **Šansas (gynimasis)**: 10% per lygį

### 7. **Freezing** ❄️
- **Aprašymas**: Užšaldo priešus lėtindamas juos
- **Maksimalus lygis**: 4
- **Šansas**: 25% per lygį
- **Efektai**: Lėtumas, šaldymas, ledo blokai

### 8. **Poison** ☠️
- **Aprašymas**: Apnuodija priešus lėtai pažeisdamas
- **Maksimalus lygis**: 4
- **Šansas**: 30% per lygį
- **Papildomi efektai**: Sąmyšis (3+ lygis), Alkis (4 lygis)

## Komandos

### `/enchant <enchantmentas> [lygis] [žaidėjas]`
- **Teisė**: `enchanter.enchant`
- **Aprašymas**: Pritaiko enchantmentą daiktui
- **Pavyzdžiai**:
  - `/enchant unbreakable 3` - Pritaiko Unbreakable 3 lygio
  - `/enchant explosive 2 Notch` - Pritaiko Explosive žaidėjui Notch

### `/listenchants [item]`
- **Teisė**: `enchanter.list`
- **Aprašymas**: Parodo visus galimus enchantmentus arba daikto enchantmentus
- **Pavyzdžiai**:
  - `/listenchants` - Visi enchantmentai
  - `/listenchants item` - Daikto enchantmentai

### `/enchanterreload`
- **Teisė**: `enchanter.reload`
- **Aprašymas**: Perkrauna plugino konfigūraciją

## Teisės

- `enchanter.*` - Visos teisės (OP pagal nutylėjimą)
- `enchanter.enchant` - Enchantmentų taikymas
- `enchanter.enchant.others` - Enchantmentų taikymas kitiems
- `enchanter.list` - Enchantmentų sąrašo peržiūra
- `enchanter.reload` - Konfigūracijos perkrovimas

## Instaliavimas

### Būtini reikalavimai
- **Java**: 17 arba naujesnė
- **Server**: Spigot/Paper 1.20.4+
- **Maven**: Kompiliavimui

### Kompiliavimas
1. Atsisiųskite arba klonuokite projektą
2. Paleiskite `build.bat` Windows arba naudokite Maven:
   ```bash
   mvn clean package
   ```
3. JAR failas bus sukurtas `target/Enchanter-1.0.0.jar`

### Diegimas serveryje
1. Nukopijuokite `Enchanter-1.0.0.jar` į serverio `plugins/` direktoriją
2. Paleiskite/perkraukite serverį
3. Redaguokite `plugins/Enchanter/config.yml` pagal poreikius
4. Naudokite `/enchanterreload` po konfigūracijos keitimo

## Konfigūracija

Pagrindiniai nustatymai `config.yml` faile:

```yaml
general:
  show-welcome-message: true  # Rodyti sveikinimo žinutes
  show-effects: true          # Rodyti vizualinius efektus
  debug-mode: false           # Debug režimas

enchantments:
  unbreakable:
    enabled: true
    max-level: 5
    chance-per-level: 20      # 20% šansas per lygį
```

## Plėtojimas

### Pridėti naują enchantmentą
1. Sukurkite naują klasę `src/main/java/lt/enchanter/enchants/types/`
2. Išplėskite `CustomEnchantment`
3. Implementuokite reikalingus metodus
4. Užregistruokite `EnchantmentManager` klasėje

### API naudojimas
```java
// Gauti enchantment manager
EnchantmentManager manager = EnchanterPlugin.getInstance().getEnchantmentManager();

// Pritaikyti enchantmentą
ItemStack enchantedItem = manager.applyEnchantment(item, "unbreakable", 3);

// Patikrinti enchantmentą
boolean hasEnchant = manager.hasEnchantment(item, "explosive");
int level = manager.getEnchantmentLevel(item, "lifesteal");
```

## Techninė informacija

- **Versija**: 1.0.0
- **API**: Spigot 1.20.4
- **Java**: 17+
- **Build sistema**: Maven
- **Licencija**: MIT

## Palaikymas

Jei radote klaidų ar turite pasiūlymų:
1. Patikrinkite serverio logus
2. Įjunkite debug režimą konfigūracijoje
3. Praneškite apie problemą su detaliais

## Changelog

### v1.0.0
- Pradinis leidimas
- 8 custom enchantmentai
- Komandų sistema
- Konfigūracijos palaikymas
- Event handling sistema

---

**Sukurta su ❤️ Minecraft bendruomenei**