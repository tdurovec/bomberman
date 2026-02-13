# Bomberman

2D akčná hra, v ktorej hráč ovláda postavičku pohybujúcu sa po mape, kladie dynamity, vyhýba alebo zabíja nepriateľov a snaží sa nájsť kľúč a otvoriť dvere do ďalšej úrovne.  

Hra obsahuje viacero typov nepriateľov s rôznym správaním:
- **Slime** – pohyb po trase  
- **Goblin** – náhodný pohyb  
- **Vampire** – prenasledovanie hráča  

Hra ponúka niekoľko úrovní s rastúcou obtiažnosťou. Každý level môže obsahovať špeciálne podmienky postupu, napríklad je potrebné najskôr poraziť všetkých nepriateľov, aby sa dali otvoriť dvere do ďalšieho levelu.

V úrovniach sú navyše ukryté predmety, ktoré hráč objaví až po zničení prasknutej steny dynamitom. Tieto predmety môžu:
- pridať život,
- pridať dynamit,
- odobrať život.

Hráč tieto predmety zoberie automaticky, nemá na výber, takže treba myslieť strategicky pri výbere cesty.

---

## Ovládanie

**Pohyb:**
- Hore: `šípka hore`  
- Dole: `šípka dole`  
- Vľavo: `šípka vľavo`  
- Vpravo: `šípka vpravo`  
- Položenie dynamitu: `Medzerník`  
- Pauza: `Esc` (počas hry)  
- Výber v menu: `šípky hore/dole`, potvrdenie `Enter`

---

## Herné obrazovky

### Hlavné menu
- **Play** – spustí hru na vybranej úrovni  
- **Select Level** – výber úrovne `šípky vľavo/vpravo`  
- **Quit** – ukončí hru

### Hra
- Po dokončení levelu sa zobrazí obrazovka **Complete Level**.  
- Po strate všetkých životov sa zobrazí obrazovka **Game Over**.

### Pause Menu
- **Resume Game** – pokračovať alebo stlačením klávesy `Esc`  
- **Repeat Level** – reštart úrovne  
- **Main Menu** – návrat do hlavného menu

### Game Over
- **Repeat Level** – skúsiť úroveň znova  
- **Main Menu**

### Complete Level
- **Next Level** – pokračovať na ďalšiu úroveň  
- **Repeat Level**  
- **Main Menu**


## Spustenie
``` mvn clean compile exec:java -Dexec.mainClass="main.Main"```
