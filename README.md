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


### **4.2 Steg-för-steg**
1. **Ladda ner projektet**  
   - Antingen via `git clone` eller som en `.zip`-fil.
2. **Importera i IntelliJ**  
   - Öppna IntelliJ och välj **"Open Project"**.
   - Navigera till projektmappen.
3. **Konfigurera API-nycklar**  
   - Öppna `application.properties` och fyll i API-nycklar på de tomma:
     ```
     FLIGHT_API_KEY= (denna ska va ifrån skyscanner)
     TOMOROW_API_KEY= (denna ska va från tomorow io med vädret)
     OPENTRIPMAP_API_KEY= (denna ska va från opentripmap med sevärdheter)
     ```
   - Backend startar på `http://localhost:7000` genom att köra App.java
   - Fungerar inte detta finns två scripts som startar programmet
     - Starta programmet med hjälp av scriptfiler:
       1. run.bat = Om du använder en Windows-dator. Antingen köra det direkt i IntelliJ eller navigera till projektmappen i Utforskar-applikationen på datorn och dubbelklicka på run.bat så skall en webbläsare komma upp automatiskt, samt ett meddelande kommer i terminalen att servern är igång. Öppnas inte webbläsarfönstret automatisk så gå in på en webbläsare och mata in `http://localhost:7000`i addressfältet på webbläsaren och navigera dit. (Föredragen/Testad webbläsare: Microsoft Edge/ Google Chrome)
       2. run.sh = Om du använder en Mac eller Linux dator. Antingen öppnas det i IntelliJ genom run, eller så navigerar du till projektmappen, hittar run.sh och kör. Först måste man välja körningsrättigheter och detta görs i terminalen. Först navigerar du till konsolen och där inne navigerar du fram till projektmappen (samma mapp där run.sh, pom.xml osv ligger). Skriv in detta kommandot: `chmod +x run.sh` och tryck `ENTER` därefter kan du i samma fönster skriva `./run.sh`. Då kommer applikationen att köras.
5. **Öppna frontend**  
   - Gå till `index.html`.
   - Fyll i sökfälten och klicka **"Search Flights"**.
   - Den kommer ladda, sedan kommer max 10st flygningar visas på skärmen.
   - Scrollar du förbi alla flygningar så kommer du att se vädret.
   - Under vädret finns en knapp till sevärdheter.
