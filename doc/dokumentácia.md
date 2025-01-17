﻿## **Zadanie - VehicleList**

Témou nášho projektu je vytvorenie softvéru, ktorý bude zaznamenávať informácie o vypožičaných autách v požičovni áut. Systém bude zhromažďovať väčšinu informácií o vozidle, ako sú typ vozidla, rok výroby, výkon a objem motora atď. Taktiež bude systém zaznamenávať informácie o zmluvách vyhotovených pri výpožičke áut a informácie o vypožičiavateľovi daného auta. Systém ponúkne široké možnosti vyhľadávania podľa kľúčových atribútov vozidiel a pod.

## **Špecifikácia scenárov**

***1. Scenár - Vytváranie nových záznamov***

 V prvom scenári sme do našej aplikácie implementovali nasledujúce funkcionality:
	 
 - validácia prihlásenia zamestnanca do systému
 - vytvorenie konta pre nového zamestnanca/admina už existujúcim adminom
 - zaevidovanie vypožičiavateľa do databázy - zahŕňa pridanie ako fyzickej, tak aj právnickej osoby
 - pridanie nového záznamu o vozidle do databázy
 - vyhotovenie zmluvy ohľadom výpožičky auta, ktorá spája zamestnanca, auto a vypožičiavateľa
 

***2. Scenár - Aktualizovanie existujúcich záznamov***

Druhý scenár umožňuje zamestnancovi aktualizovať rôzne údaje, napríklad:
 - údaje o vozidle či vypožičiavateľovi
 - pridanie poškodenia k existujúcej zmluve (aktualizácia zmluvy)
 - aktualizácia existujúceho poškodenia na základe vykonanej opravy servisným strediskom

Aktualizácia záznamu je možná až po vyhľadaní a zobrazení detailu daného záznamu.

***3. Scenár - Vyhľadávanie existujúcich záznamov + detail***

Tretí scenár ponúka adminovi v aplikácii vyhľadávať zamestnancov napríklad na základe mena, priezviska, loginu či čísla. 

Zamestnanec má možnosť v aplikácii vyhľadať konkrétne vozidlo, výpožičiavateľa či zmluvu. Po vyhľadaní konkrétneho záznamu aplikácia zobrazuje po kliknutí pravým tlačidlom na záznam možnosť zobrazenia jeho detailnejších informácií. Pri hľadaní zmlúv aplikácia umožňuje zamestnancovi zobraziť ním vyhotovené zmluvy.

**Slovný opis štvrtého a piateho scenára**

***4. Scenár - Vymazanie existujúcich záznamov***

Štvrtý scenár pridá aplikácii funkcionalitu vymazávania záznamov o aute, zamestnancovi, zmluve , či vypožičiavateľovi.

***5. Scenár - Zobrazenie záznamov podľa vybranej doménovej štatistiky***

Piaty scenár ponúkne zobrazenie záznamov spĺňajúce rôzne štatistické kritéria, napríklad:
	

 - vozidlá, ktoré mali haváriu 
 - a iné...

## Diagram logického a fyzického  modelu
![Fyzický model](https://prnt.sc/n8scqs)
Každá tabuľka car obsahuje práve jeden FK z car_info. Car_info obsahuje atribúty enumerátorov. Tabuľka contract spája auto, zamestnanca a vypožičiavateľa a ďalšie špecifické atribúty. Nachádza sa tam taktiež väzobná tabuľka car_repair, ktorá slúži nato aby sme vedeli uložiť viacero opráv k jednému autu. Customer tabuľka sa vie odkazovať na 2 rôzne tabuľky podľa typu customera (právnická alebo fyzická osoba).

![Logický model](https://prnt.sc/n8sqo1)
Logický model je abstraktnejší, neobsahuje väzobné tabuľky, čiže môže sa vyskytovať vzťah n:n. Obsahuje aj vyjadruje takmer totožné veci ako vo fyzickom modeli.

## Stručný opis návrhu a implementácie

Ako vývojové prostredie sme si vybrali IntelliJ IDEA. Ako objektovo-relačný databázový systém používame PostgreSQL. Ako klienta používame DataGrip, kde sme zadávali rôzne skripty. Naša aplikácia je plne funkčná s JDBC 42.2.5 driverom . Java development kit je vo verzii jdk1.8.0_202. 

V našej aplikácii pristupujeme k databáze pomocou triedy AllTableManager, ktorá zaručuje pripojenie na databázu. Trieda sa nachádza v balíku persistancemanagers spolu s ostatnými triedami pre prístup a prácu s databázou. V prípade neúspešného pripojenia sa vykoná SQLException.

Aplikácia je navrhnutá softvérovou architektúrou MVC. Taktiež obsahuje OOP princípy ako agregácia, zapuzdrenie, dedenie, RTTI a pod.

Grafické rozhranie vytvárame pomocou SceneBuilder-a, teda vytvárame JavaFX aplikáciu.

Pre správne fungovanie aplikácie je nutné spustiť skript pre vytvorenie tabuliek v databáze. Taktiež je potrebné vykonať inserty do číselníkovej tabuľky pre správne fungovanie aplikácie v adresári 'schema'. Taktiež jeden admin a zamestnanec je vytvorený spolu s tabuľkami, aby mal možnosť, či už zamestnanec alebo admin prihlásiť sa do aplikácie. Adminove konto je login: admin heslo: admin, zamestnancove je login: zamestnanec heslo:zamestnanec. Pred spustením aplikácie je potrebné nastaviť správne údaje v properties súbore, teda údaje pre úspešné pripojenie k databáze.

Snažíme sa písať prehľadný kód s odpovedajúcimi názvami premenných a metód, ktoré sa snažia jednoznačne určiť načo daná trieda, metóda, či premenná slúži. 

**Opis implementácie prvého scenára**

Aplikačnú logiku s GUI prepájame controllerom, ktorý volá rôzne metódy z rôznych tried z balíka persistancemanagers pre prístup do databázy. Tieto metódy častokrát vracajú inštancie objektov, s ktorými následne controller vykonáva rôzne operácie, či zobrazuje ich dáta na obrazovku. Prvý scenár zahŕňa triedy v balíku controller, ktoré obsahujú v názve '*...add...*', čo už z názvu vyplýva, že tieto triedy sú využívané ak chceme pridať nejaký nový záznam do databázy. V balíku model sme si vytvorili triedy Car, Employee, LegalPerson, NaturalPerson, ktoré obsahujú potrebné atribúty pre uloženie záznamu z databázy pre ten ktorý prvok. Taktiež tieto triedy obsahujú gety/sety aby sme mohli meniť, či získavať tieto atribúty bezpečne.

GUI je vytvorené v SceneBuilder-i, ktorý vytvára fxml súbor, ku ktorému sme vytvárali triedu v controlleri, čiže každý súbor fxml má svoju triedu v balíku controller.

**Opis implementácie druhého a tretieho scenára**
 
 Podobne ako v implementácii prvého scenára sme zachovali rozdelenie tried do balíkov, kde pribudli nové triedy využívané v týchto dvoch scenároch. Podobne sme postupovali pri tvorbe GUI. Začali sme využívať knižnicu ControlFX a JFoenix. Naplnili sme taktiež tabuľky v databáze fake dátami. Na zobrazovanie záznamov z databázy používame TableView, kde zobrazujeme záznamy po 500 ak ich je viac. Na urýchlenie vyhľadávania sme použili indexy. V balíku persistancemanagers majú triedy metódy, ktoré vracajú dáta z databázy vo forme ObservableList. Tieto metódy majú dve verzie: 

 1. Nájdenie a vrátenie všetkých záznamov z databázy
 2. Nájdenie a vrátenie záznamov spĺňajúcich zadané kritéria

Ďalej v balíku persistancemanagers pribudli metódy, ktoré slúžia na aktualizáciu dát v databáze, teda vykonávajú query na update dát.
