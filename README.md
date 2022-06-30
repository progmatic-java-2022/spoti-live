# Spoti live
ENUMOK TÁROLÁSA ADATBÁZISBAN JPABAN? ELÖNYE HÁTRÁNYA

alapjáraton indexként tárolja az enum fájlban lévö indexként @enumerated
Stringes változat jobb mert nem csuszik el az indexeles


SZERVIZ RÉTEG:
Mindent itt hajtunk végre kivéve a HTML-t és az Adatbázist
Itt van az üzleti logika

SZERVIZEK TUDNAK SZERVIZEKET HIVNI?
Igen tudnak
@Service annotácio

CONTROLLER RÉTEG:
Ez köti össze a szervizt a html-el, getMappinges végpontokat tudunk létrehozni
Controller csak egy felületet irányit, nem tudnak még egy controllert hivni.

REPOSITORY:
Adatbázis kezelésére van, a szervizben a repository függvényeit tudjuk meghivni,
uj müveleteket tudunk végezni. Repository entitásokkkal tud visszatérni.

MI AZ A @TRANSACTIONAL
Az adatbázis nem tudjuk hogy menti el adolgokat, csak abban lehetünk biztosak hogy minden
modositas mentődik.

OSZTÁLYRA RÁTESZÜK A TRANSACTIONAL MIKOR FUT TRANZAKCIOBA?
Ha meghivjuk egy függvényét.
@Transactionalt lehet függvényre is tenni.

TRANZAKCIO KÖZBEN MIKRO MENT A JPA?
bármikor menthet, de a végén biztos.

DTO ES ENTITÁS KÖZÖTTI KÜLÖMBSÉG?

Entitás: táblának egy sora, @Entiry kiirása kötelező a többi ajánlott. @Id kötelező!
Cska tranzakcion belül használjuk.
Dto: Data transfer object -> adatokat képvisel mellyel tudunk dolgokat végrehajtani.
Tranzakcion kivül használjuk.

MI AZ A COMMAND:
Command az egy Dto!
UTasitást hajtunk végre vele

ROLLESALLOWED ANNOTÁCIO?:
Jogosultság kezelés.

GET ÉS POSTMAPPING ANNOTÁCIO:

Postmappingel tudunk modositani.
GetMappingel cask lekérni tudunk adatot. @Getmmaping (/"Elérési utvonal")
lehet modositani benne, de TILOS !!


MI AZ "INITIALIZINGBEAN{}"
Kezdőérték adásához használjuk.


MI AZ A @MODELATTRIBUTE?
Modelben lévő változók, amiket át tudunk adni a HTML-nek vagy megkapjuk a HTML-től.

MODEL MODEL HOGYAN TÖLTÖDIK BE?
Oldalbetöltésenként töltődik be.

MI AZ @AUTOWIRED?:
Repositroykat, szervizeket tuduk összekötni egymással,és tudjuk használni a bennük lévi függvényeket.







