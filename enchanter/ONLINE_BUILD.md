# Online Build Instructions / Internetinių Build Sistemų Instrukcijos

## 🌐 Kaip gauti JAR failą be lokalaus kompiliavimo

### **1. GitHub Actions (Rekomenduojama)**

#### Žingsniai:
1. **Sukurkite GitHub repository**
   - Eikite į https://github.com
   - Spustelėkite "New Repository"
   - Įveskite pavadinimą: `enchanter-plugin`

2. **Įkelkite kodą**
   ```bash
   git init
   git add .
   git commit -m "Initial plugin commit"
   git remote add origin https://github.com/JŪSŲ_USERNAME/enchanter-plugin.git
   git push -u origin main
   ```

3. **Automatinis build**
   - GitHub Actions automatiškai paleis build procesą
   - Po 2-5 minučių eikite į "Actions" tab
   - Spustelėkite ant žaliojo ✅ build'o
   - Slinkite žemyn iki "Artifacts" sekcijos
   - Spustelėkite "Enchanter-Plugin" - atsisiųs ZIP failą
   - ZIP faile rasite `Enchanter-1.0.0.jar` failą!

### **2. Replit (Online IDE)**

#### Žingsniai:
1. **Eikite į https://replit.com**
2. **Sukurkite naują projektą**
   - Pasirinkite "Java" arba "Maven"
3. **Įkelkite failus**
   - Upload visus projekto failus
4. **Paleiskite build**
   ```bash
   mvn clean package
   ```
5. **Atsisiųskite JAR**
   - Eikite į Files panelę (kairėje)
   - Atidarykite `target/` folderį
   - Dešinis peles mygtukas ant `Enchanter-1.0.0.jar`
   - Pasirinkite "Download"

### **3. GitLab CI/CD**

#### Žingsniai:
1. **Sukurkite GitLab projektą**
   - Eikite į https://gitlab.com
2. **Įkelkite kodą**
3. **CI/CD automatiškai sukurs JAR**
   - Eikite į "CI/CD" → "Pipelines"
   - Spustelėkite ant sėkmingo ✅ pipeline
   - Dešinėje pusėje rasite "Job artifacts"
   - Spustelėkite "Download" - gausite ZIP su JAR failu

### **4. CodeSandbox**

#### Žingsniai:
1. **Eikite į https://codesandbox.io**
2. **Sukurkite "Server" projektą**
3. **Įkelkite failus**
4. **Naudokite terminalą**:
   ```bash
   mvn package
   ```

### **5. Gitpod (Cloud IDE)**

#### Žingsniai:
1. **Įkelkite į GitHub**
2. **Eikite į https://gitpod.io**
3. **Atidarykite projektą**: `https://gitpod.io/#https://github.com/JŪSŲ_USERNAME/enchanter-plugin`
4. **Kompiliuokite**:
   ```bash
   mvn clean package
   ```

---

## 🚀 **Greičiausias būdas:**

### **GitHub Actions (Zero Setup)**
1. Upload į GitHub
2. Palaukite 5 min
3. Atsisiųskite JAR iš Actions

### **Replit (Instant)**
1. Copy-paste kodą į Replit
2. Run `mvn package`
3. Download JAR

---

## 📁 **Failų struktūra upload'ui:**

Įkelkite visus šiuos failus:
```
enchanter/
├── pom.xml
├── src/
│   └── main/
│       ├── java/
│       │   └── lt/
│       │       └── enchanter/
│       │           ├── EnchanterPlugin.java
│       │           ├── enchants/
│       │           ├── commands/
│       │           └── listeners/
│       └── resources/
│           ├── plugin.yml
│           └── config.yml
├── .github/
│   └── workflows/
│       └── build.yml
└── README.md
```

**Rezultatas**: Gausite `Enchanter-1.0.0.jar` failą, kurį galėsite dėti į serverio `plugins/` direktoriją!

---

## 🔍 **KUR TIKSLIAI RASTI JAR FAILĄ:**

### **GitHub Actions:**
1. Eikite į savo repository
2. Spustelėkite "Actions" tab (viršuje)
3. Pasirinkite paskutinį žalią ✅ build
4. Slinkite žemyn iki "Artifacts" sekcijos
5. Spustelėkite "Enchanter-Plugin" 
6. Atsisiųs `Enchanter-Plugin.zip`
7. **JAR failas bus ZIP viduje!**

### **Replit:**
1. Po `mvn package` komandos
2. Kairėje Files panelėje atidarykite `target/` folderį
3. Rasite `Enchanter-1.0.0.jar`
4. Dešinis pelės mygtukas → "Download"

### **GitLab:**
1. Repository → "CI/CD" → "Pipelines"  
2. Spustelėkite žalią ✅ pipeline
3. Dešinėje "Job artifacts" → "Download"
4. JAR failas bus ZIP faile

### **Lokali kompiliacija:**
Jei vis tik naudojate `build.bat`:
- JAR failas: `target\Enchanter-1.0.0.jar`
- Automatiškai nukopijuojamas į `plugins\` folderį

**⚠️ SVARBU:** Visada ieškokite failo `Enchanter-1.0.0.jar` - tai yra jūsų pluginas!