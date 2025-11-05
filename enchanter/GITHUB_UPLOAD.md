# 🚀 KAIP ĮKELTI Į GITHUB - ŽINGSNIS PO ŽINGSNIO

## 📋 **KĄ REIKIA PADARYTI:**

### **1. Sukurti GitHub repository**
1. Eikite į https://github.com
2. Spustelėkite žalią **"New"** mygtuką arba **"+"** viršuje dešinėje
3. **Repository name**: `enchanter-plugin`
4. **Description**: `Custom enchantments plugin for Minecraft servers`
5. Pažymėkite **"Public"** (kad kiti galėtų matyti)
6. **NEPRIDĖKITE** README, .gitignore, license (nes jau turime)
7. Spustelėkite **"Create repository"**

### **2. Įkelti failus (Lengvas būdas - per naršyklę)**
1. **GitHub repositoryje** spustelėkite **"uploading an existing file"**
2. **Nutempkite VISUS failus** iš `C:\Users\pc\Desktop\enchanter\` folderio
3. **Arba spustelėkite "choose your files"** ir pasirinkite visus

#### **Reikalingi failai:**
```
✅ pom.xml
✅ README.md  
✅ ONLINE_BUILD.md
✅ build.bat
✅ package.json
✅ Dockerfile
✅ .gitlab-ci.yml
✅ src/ (visas folderis su java failais)
✅ .github/ (su workflows/build.yml)
```

### **3. Commit ir laukimas**
1. **Commit message**: `Initial enchanter plugin with 8 custom enchantments`
2. Spustelėkite **"Commit changes"**
3. **PALAUKITE 2-5 MINUČIŲ** - GitHub Actions pradės darbą

### **4. Gauti JAR failą**
1. Eikite į **"Actions" tab**
2. Pamatysite running ⚡ arba completed ✅ build
3. **Spustelėkite ant build** (žalio varnelės)
4. **Slinkite žemyn** iki "Artifacts"
5. **Atsisiųskite "Enchanter-Plugin"**
6. **Atidarykite ZIP** - viduje `Enchanter-1.0.0.jar`

## 🎯 **ALTERNATYVUS BŪDAS - per Git komandas:**

Jei turite Git įdiegtą:

```bash
# 1. Atidarykite PowerShell enchanter folderyje
cd C:\Users\pc\Desktop\enchanter

# 2. Inicijuokite git
git init

# 3. Pridėkite failus  
git add .

# 4. Padarykite commit
git commit -m "Initial enchanter plugin with 8 custom enchantments"

# 5. Prijunkite prie GitHub (pakeiskite USERNAME)
git remote add origin https://github.com/JŪSŲ_USERNAME/enchanter-plugin.git

# 6. Įkelkite
git push -u origin main
```

## ⚠️ **SVARBU:**

- **Įkelkite VISUS failus** - net tuos, kurių nežinote
- **GitHub Actions** automatiškai sukurs JAR failą
- **JAR failas** bus "Actions" → "Artifacts" sekcijoje
- **Failo pavadinimas:** `Enchanter-1.0.0.jar`

## 🎮 **KAS ATSITIKS:**

1. **Įkelsite kodą** → GitHub
2. **GitHub Actions** automatiškai kompiliuos
3. **Gausite JAR** failą be jokių programų diegimo
4. **Dėsite JAR** į serverio `plugins/` folderį
5. **Veiks 8 enchantmentai:**
   - Unbreakable, Explosive, Lifesteal
   - AutoRepair, Lightning, Teleport  
   - Freezing, Poison

**Viskas! Pluginas bus paruoštas naudojimui!** 🚀