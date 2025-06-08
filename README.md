# **SkyExplorer - Flyg och Väder Mashup**

## **1. Om Projektet**
SkyExplorer är en mashup-tjänst som hjälper resenärer att planera sina resor genom att kombinera **flygdata, väderprognoser och populära sevärdheter** för en given destination. Tjänsten hämtar information från flera externa API:er och presenterar den i ett webbgränssnitt.

## **2. Funktionalitet**
### **2.1 Huvudfunktioner**
- ✈️ **Sök flyg** – Hitta flygresor mellan två ställen baserat på datum då du vill resa.
- ☀️ **Väderprognos** – Visa väder vid destinationen.
- 🏛️ **Sevärdheter** – Skriv ut populära sevärdheter på resmålet.

### **2.2 Användarflöde**
1. Användaren fyller i **avreseort, destination och avresedatum**.
2. Systemet hämtar **flyginformation** från **SkyScanner API**.
3. Systemet hämtar **väderprognoser** från **Tomorrow.io API** baserat på flygets ankomsttid.
4. Systemet hämtar **populära sevärdheter** från **OpenTripMap API**.
5. Information visas på hemsidan.

## **3. Teknisk Specifikation**
### **3.1 Backend**
- **Språk:** Java
- **Ramverk:** Javalin
- **Byggverktyg:** Maven
- **Externa API:er:**
  - **SkyScanner API** *(för flygdata)*
  - **Tomorrow.io API** *(för väderprognoser)*
  - **OpenTripMap API** *(för sevärdheter)*

### **3.2 Frontend**
- **HTML, CSS, JavaScript**
- **Bibliotek:** Bootstrap, jQuery
- **Datepicker** för att välja avresedatum



## **4 Steg-för-steg**
> ⚠️ **VIKTIGT:**  
> Du måste ha internetuppkoppling för att denna applikation skall fungera korrekt! (då externa API:er måste anropas samt Maven behöver internet).  
> Det är även viktigt att du har **JDK 17** installerat på din dator.

---

## Systemkrav
- Windows 10/11
- IntelliJ IDEA Community Edition (eller högre)
- JDK 17
- Internetuppkoppling

---

## Steg-för-steg

### **Steg 1: Öppna projektet i IntelliJ**
1. Ladda ner `.zip`-filen med namnet `SkyExplorer` (detta är projektmappen inuti zip-filen).
2. Extrahera zip-filen.
3. Starta IntelliJ IDEA.
4. Välj **File → Open...** och navigera till mappen `SkyExplorer` (som du precis extraherade).
5. Öppna mappen i IntelliJ.

---

### **Steg 2: Ladda in Maven-dependencies**
- Kontrollera att Maven automatiskt laddar in alla dependencies.
- Om inte, gör något av följande:
  - Tryck `Ctrl` två gånger, skriv `mvn clean install` och tryck Enter.
  - Eller högerklicka på `pom.xml` i projektets rotmapp → välj **"Add as Maven Project"**.
- Tryck på **Maven-ikonen** (höger sida av IntelliJ) och klicka:  
  **"Generate Sources and Update Folders For All Projects"**.

---

### **Steg 3: Installera plugin för Lombok**
1. Gå till **File → Settings → Plugins**.
2. Sök efter **Lombok** under fliken **Marketplace**.
3. Klicka på **Install**.
4. Starta om IntelliJ om det krävs.

---

### **Steg 4: Kontrollera att SDK är inställd på 17**
1. Gå till **File → Project Structure (Ctrl+Alt+Shift+S)**.
2. Under fliken **Project**, kontrollera att:
- **Project SDK:** 17 (t.ex. Oracle OpenJDK 17.0.8)
- **Language level:** 17 – Sealed types, pattern matching for switch (preview)

---

### **Steg 5: Kontrollera att API-nycklar finns**
- Navigera till `src/main/resources/application.properties`
- Filen ska innehålla tre rader:
```properties
FLIGHT_API_KEY=din_google_flights_nyckel
TOMOROW_API_KEY=din_tomorrowio_nyckel
ATTRACTION_PLACES=din_opentripmap_nyckel
```
- Om dessa inte finns:
  1. Här behöver du först hämta API nyckel från respektive tjänst:
  - **Google Flights API**
  - **Tomorrow.io API**:
  - **OpenTripMap API**:
  2. Klistra in nycklarna i `src/main/resources/application.properties` på respektive plats.
  3. Spara filen (Ctrl + S).

---

### **Steg 6: Starta backend**
- Navigera till `src/main/java/controller/App.java`.
- Kör filen genom att:
  - Trycka på **play-knappen** bredvid `public class App {`
  - Eller högerklicka och välj **Run 'App.main()'**

---

### **Steg 7: Öppna frontend**
1. Gå till `src/main/resources/public/index.html`.
2. Högerklicka på filen och välj **Run 'index.html (2)'**.
3. Alternativt:
- Öppna valfri webbläsare.
- I adressfältet sök efter: `http://localhost:7000/index.html`.

---

## 🌐 Hur hemsidan fungerar

När du öppnar `index.html` (via IntelliJ eller direkt i webbläsaren):

1. **Skriv in en stad** i sökfältet.
2. Klicka på **"Search Flights"**.
3. Applikationen:
- Hämtar upp till 10 flygningar via Google Flights API.
- Visar väder för destinationen via Tomorrow.io.
- Längst ner finns två knappar:
  - **"Utforska {namn på staden}"** – Visar populära sevärdheter via OpenTripMap API.
  - **"Om {namn på staden}"** – Visar information om staden.

---

När detta är klart är du redo att använda SkyExplorer! ✈️🌍

