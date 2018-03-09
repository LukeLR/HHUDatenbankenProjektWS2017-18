PRAGMA auto_vacuum = 1;
PRAGMA automatic_index = 1;
PRAGMA case_sensitive_like = 0;
PRAGMA defer_foreign_keys = 0;
PRAGMA encoding = "UTF-8";
PRAGMA foreign_keys = 1;
PRAGMA ignore_check_constraints = 0;
PRAGMA journal_mode = WAL;
PRAGMA query_only = 0;
PRAGMA recursive_triggers = 1;
PRAGMA reverse_unordered_selects = 0;
PRAGMA secure_delete = 0;
PRAGMA synchronous = NORMAL;
--.headers ON

/*==========================================
 *================ TABLES ==================
 *==========================================*/

SELECT '==========================================';
SELECT '================ TABLES ==================';
SELECT '==========================================';
SELECT '';

CREATE TABLE Adresse (
    Strasse VARCHAR(70) NOT NULL CONSTRAINT Strassenname CHECK (
        Strasse NOT GLOB ('*[0-9]*') AND
        LENGTH(CAST(Strasse AS VARCHAR)) > 0),
    Hausnummer VARCHAR(10) NOT NULL CONSTRAINT Hausnummer CHECK(
        CAST (substr(Hausnummer, 1, LENGTH(Hausnummer) - 1) AS INTEGER)
        == substr(Hausnummer, 1, LENGTH(Hausnummer) - 1) AND
        CAST (substr(Hausnummer, LENGTH(Hausnummer)) AS TEXT)
        == substr(Hausnummer, LENGTH(Hausnummer)) OR
        CAST (Hausnummer AS INTEGER) == Hausnummer),
    PLZ DECIMAL(5,0) NOT NULL CONSTRAINT PLZ CHECK(
        LENGTH(CAST(PLZ AS VARCHAR)) == 5),
    Ort VARCHAR(40) NOT NULL CONSTRAINT Ort CHECK (
        Ort NOT GLOB ('*[0-9]*') AND
        LENGTH(CAST(Ort AS VARCHAR)) > 0),
    Adressen_ID INTEGER PRIMARY KEY NOT NULL
    );

CREATE TABLE Kunde (
    E_Mail_Adresse VARCHAR(320) NOT NULL CONSTRAINT E_Mail_Adresse CHECK (
        E_Mail_Adresse LIKE '%_@_%.__%'),
    Vorname VARCHAR(50) NOT NULL CONSTRAINT Vorname CHECK (
        Vorname NOT GLOB ('*[0-9]*') AND
        LENGTH(CAST(Vorname AS VARCHAR)) > 0),
    Nachname VARCHAR(30) NOT NULL CONSTRAINT Nachname CHECK (
        Nachname NOT GLOB ('*[0-9]*') AND
        LENGTH(CAST(Nachname AS VARCHAR)) > 0),
    Passwort VARCHAR(64) NOT NULL CONSTRAINT Passwort CHECK (
        LENGTH(CAST(Passwort AS VARCHAR)) >= 6),
    Adressen_ID INTEGER NOT NULL,
    PRIMARY KEY(E_Mail_Adresse),
    FOREIGN KEY (Adressen_ID) REFERENCES Adresse (Adressen_ID)
    );

CREATE TABLE Premiumkunde (
    Ablaufdatum VARCHAR NOT NULL CONSTRAINT Ablaufdatum CHECK (
        DATE(Ablaufdatum) IS NOT NULL),
    Studierendenausweis BLOB,
    E_Mail_Adresse VARCHAR(320) NOT NULL,
    PRIMARY KEY (E_Mail_Adresse),
    FOREIGN KEY (E_Mail_Adresse) REFERENCES Kunde (E_Mail_Adresse)
    );

CREATE TABLE Angestellter (
    Jobbezeichnung VARCHAR(30) NOT NULL CONSTRAINT Jobbezeichnung CHECK (
        LENGTH(CAST(Jobbezeichnung AS VARCHAR)) > 0),
    Gehalt INTEGER NOT NULL CONSTRAINT Gehalt CHECK (
        Gehalt >= 0 AND (
            TYPEOF(Gehalt) == 'real' OR
            TYPEOF(Gehalt) == 'integer'
        )
    ),
    E_Mail_Adresse VARCHAR(320) NOT NULL,
    PRIMARY KEY (E_Mail_Adresse),
    FOREIGN KEY (E_Mail_Adresse) REFERENCES Kunde (E_Mail_Adresse)
    );

CREATE TABLE Lieferdienst(
    Lieferdienst_Bezeichnung VARCHAR(40) NOT NULL
        CONSTRAINT Lieferdienst_Bezeichnung CHECK (
            LENGTH(CAST(Lieferdienst_Bezeichnung AS VARCHAR)) > 0),
    Versandkosten DECIMAL(3,2) NOT NULL CONSTRAINT Versandkosten CHECK (
        Versandkosten >= 0 AND (
            TYPEOF(Versandkosten) == 'real' OR
            TYPEOF(Versandkosten) == 'integer'
        )
    ),
    PRIMARY KEY (Lieferdienst_Bezeichnung)
    );

CREATE TABLE Warenkorb (
    Bestelldatum VARCHAR
        CONSTRAINT Bestelldatum CHECK (
            (
                Bestelldatum IS NOT NULL AND
                DATE(Bestelldatum) IS NOT NULL
            ) OR (
                Bestelldatum IS NULL
            )
        ),
    Bestellstatus VARCHAR(20) NOT NULL CONSTRAINT Bestellstatus CHECK (
        Bestellstatus IN (
            'In Bearbeitung', 'Storniert', 'Bezahlt', 'Versandfertig',
            'Versendet', 'Abgeschlossen'
        )
    ),
    Warenkorb_ID INTEGER PRIMARY KEY NOT NULL,
    E_Mail_Adresse VARCHAR(320) NOT NULL,
    Lieferdienst_Bezeichnung VARCHAR(40) NOT NULL,
    Lieferdatum VARCHAR
        CONSTRAINT Lieferdatum CHECK (
            (
                Lieferdatum IS NOT NULL AND
                DATE(Lieferdatum) IS NOT NULL AND
                Lieferdatum > CURRENT_DATE
            ) OR (
                Lieferdatum IS NULL
            )
        ),
    FOREIGN KEY (E_Mail_Adresse) REFERENCES Kunde (E_Mail_Adresse),
    FOREIGN KEY (Lieferdienst_Bezeichnung)
        REFERENCES Lieferdienst (Lieferdienst_Bezeichnung)
    );

CREATE TABLE Lieferabo (
    Intervall INTEGER NOT NULL CONSTRAINT Intervall CHECK (
        TYPEOF(Intervall) == 'integer' AND
        Intervall > 0),
    Beginn VARCHAR NOT NULL CONSTRAINT Beginn_Datum CHECK (
        DATE(Beginn) IS NOT NULL),
    Ende VARCHAR NOT NULL CONSTRAINT Ende_Datum CHECK (
        DATE(Ende) IS NOT NULL),
    Warenkorb_ID INTEGER NOT NULL,
    PRIMARY KEY (Warenkorb_ID),
    FOREIGN KEY (Warenkorb_ID) REFERENCES Warenkorb (Warenkorb_ID),
    check (Beginn < Ende)
    );

CREATE TABLE Newsletter (
    Betreff VARCHAR(40) NOT NULL CONSTRAINT Betreff CHECK (
        LENGTH(CAST(Betreff AS VARCHAR)) > 0),
    Text TEXT NOT NULL CONSTRAINT Text CHECK (
        LENGTH(CAST(Text AS TEXT)) > 0),
    Datum VARCHAR NOT NULL
        DEFAULT CURRENT_DATE
        --ON UPDATE CURRENT_DATE
        CONSTRAINT Newsletter_Datum CHECK (
            DATE(Datum) IS NOT NULL),
    Newsletter_ID INTEGER PRIMARY KEY NOT NULL,
    E_Mail_Adresse VARCHAR(320) NOT NULL,
    FOREIGN KEY (E_Mail_Adresse) REFERENCES Angestellter (E_Mail_Adresse)
    );

CREATE TABLE Newsletterabo (
    E_Mail_Adresse VARCHAR(320) NOT NULL,
    Newsletter_ID INTEGER NOT NULL,
    PRIMARY KEY (E_Mail_Adresse, Newsletter_ID),
    FOREIGN KEY (E_Mail_Adresse) REFERENCES Kunde (E_Mail_Adresse),
    FOREIGN KEY (Newsletter_ID) REFERENCES Newsletter (Newsletter_ID)
    );

CREATE TABLE Artikel (
    Bezeichnung VARCHAR(40) NOT NULL CONSTRAINT Bezeichnung CHECK (
        LENGTH(CAST(Bezeichnung AS VARCHAR)) > 0),
    Beschreibung TEXT NOT NULL CONSTRAINT Beschreibung CHECK (
        LENGTH(CAST(Beschreibung AS TEXT)) > 0),
    Bild BLOB DEFAULT NULL,
    Artikel_ID INTEGER PRIMARY KEY NOT NULL
    );

CREATE TABLE Angebot (
    Angebots_ID INTEGER PRIMARY KEY NOT NULL,
    Artikel_ID INTEGER NOT NULL,
    Preis DECIMAL(10,2) NOT NULL CONSTRAINT Preis CHECK (
        Preis >= 0 AND (
            TYPEOF(Preis) == 'real' OR
            TYPEOF(Preis) == 'integer'
        )
    ),
    FOREIGN KEY (Artikel_ID) REFERENCES Artikel (Artikel_ID)
    );

CREATE TABLE Anbieter (
    Anbieterbezeichnung VARCHAR(40) NOT NULL CONSTRAINT Anbieterbezeichnung CHECK (
        LENGTH(CAST(Anbieterbezeichnung AS VARCHAR)) > 0),
    PRIMARY KEY (Anbieterbezeichnung)
    );

CREATE TABLE Angebot_im_Warenkorb (
    Angebots_ID INTEGER NOT NULL,
    Anbieterbezeichnung VARCHAR(40) NOT NULL,
    Warenkorb_ID INTEGER NOT NULL,
    Anzahl INTEGER NOT NULL CONSTRAINT Anzahl CHECK (
        TYPEOF(Anzahl) == 'integer' AND
        Anzahl >= 1),
    PRIMARY KEY (Angebots_ID, Anbieterbezeichnung, Warenkorb_ID),
    FOREIGN KEY (Angebots_ID) REFERENCES Angebot (Angebots_ID),
    FOREIGN KEY (Anbieterbezeichnung) REFERENCES Anbieter (Anbieterbezeichnung),
    FOREIGN KEY (Warenkorb_ID) REFERENCES Warenkorb (Warenkorb_ID)
    );

CREATE TABLE Anbieter_bietet_an (
    Anbieterbezeichnung VARCHAR(40) NOT NULL,
    Angebots_ID INTEGER NOT NULL,
    Bestand INTEGER NOT NULL CONSTRAINT Bestand CHECK (
        TYPEOF(Bestand) == 'integer' AND
        Bestand >= 0),
    PRIMARY KEY (Anbieterbezeichnung, Angebots_ID),
    FOREIGN KEY (Anbieterbezeichnung) REFERENCES Anbieter (Anbieterbezeichnung),
    FOREIGN KEY (Angebots_ID) REFERENCES Angebot (Angebots_ID)
    );

CREATE TABLE Artikel_im_Newsletter (
    Newsletter_ID INTEGER NOT NULL,
    Artikel_ID INTEGER NOT NULL,
    PRIMARY KEY(Newsletter_ID, Artikel_ID),
    FOREIGN KEY(Newsletter_ID) REFERENCES Newsletter (Newsletter_ID),
    FOREIGN KEY(Artikel_ID) REFERENCES Artikel (Artikel_ID)
    );

CREATE TABLE Artikel_empfiehlt_Artikel (
    Artikel_ID1 INTEGER NOT NULL,
    Artikel_ID2 INTEGER NOT NULL,
    PRIMARY KEY (Artikel_ID1, Artikel_ID2),
    FOREIGN KEY (Artikel_ID1) REFERENCES Artikel (Artikel_ID),
    FOREIGN KEY (Artikel_ID2) REFERENCES Artikel (Artikel_ID)
    );

CREATE TABLE Schlagwort (
    Schlagwort VARCHAR(30) NOT NULL CONSTRAINT Schlagwort CHECK (
        LENGTH(CAST(Schlagwort AS VARCHAR)) > 0),
    PRIMARY KEY (Schlagwort),
    UNIQUE (Schlagwort COLLATE NOCASE)
    );

CREATE TABLE Artikel_gehoert_zu_Schlagwort (
    Artikel_ID INTEGER NOT NULL,
    Schlagwort VARCHAR(30) NOT NULL,
    PRIMARY KEY (Artikel_ID, Schlagwort),
    FOREIGN KEY (Artikel_ID) REFERENCES Artikel(Artikel_ID),
    FOREIGN KEY (Schlagwort) REFERENCES Schlagwort(Schlagwort)
    );

/*==========================================
 *================ TRIGGER =================
 *==========================================*/

SELECT '==========================================';
SELECT '================ TRIGGER =================';
SELECT '==========================================';
SELECT '';

CREATE TRIGGER update_newsletter_datum
AFTER UPDATE ON Newsletter
WHEN NEW.Datum < CURRENT_DATE
BEGIN
    UPDATE Newsletter SET Datum = CURRENT_DATE
        WHERE E_Mail_Adresse = NEW.E_Mail_Adresse;
END;

CREATE TRIGGER newsletter_zu_viele_artikel
BEFORE INSERT ON Artikel_im_Newsletter
WHEN EXISTS (
    SELECT COUNT(*) FROM Artikel_im_Newsletter
    GROUP BY Artikel_im_Newsletter.Newsletter_ID
    HAVING COUNT(*) > 9
)
BEGIN
    SELECT RAISE (ABORT, 'Maximal 10 Artikel im Newsletter!');
END;

/* TODO: newsletter_zu_wenig_artikel? Aber wie lässt sich das realisieren,
 *       wenn die Artikel dem Newsletter nacheinander hinzugefügt werden,
 *       außer mit COMMIT? Und das würde bedeuten, dass man die UI um-
 *       programmieren müsste. -- Aussage des Korrektors: Muss nicht
 *       implementiert werden.
 */

/* Wenn ein Angebot in den Warenkorb gelegt werden soll, überprüft dieser
 * Trigger, ob der gewählte Anbieter dieses Angebot noch in der
 * gewünschten Anzahl anbietet.
 */

/* Wenn ein Angebot eines Anbieters in einen Warenkorb gelegt werden soll,
 * der schon das Angebot dieses Anbieters enthält, dann soll die Anzahl
 * dieses Angebotes in diesem Warenkorb erhöht werden, statt das Angebot
 * ein zweites Mal in den Warenkorb zu legen, da der Warenkorb nicht
 * zweimal dasselbe Angebot desselben Anbieters enthalten kann.
 */

/* Dieser Trigger kombiniert die Aufgaben der beiden oben beschriebenen
 * Trigger in einem, da für die Ausführung des zweiten Triggers das
 * RAISE(IGNORE)-Statement benötigt wird, um zu verhindern, dass der
 * ursprüngliche, fehlschlagende INSERT, der ja durch den Trigger ersetzt
 * wird, dennoch zu einem Fehler führt. Dies führt aber auch dazu, dass
 * andere Trigger ignoriert werden, wenn dieser Trigger ausgelöst wurde.
 * Konkret: Wurde also ein zweites Mal dasselbe Angebot desselben
 * Anbieters in denselben Warenkorb gelegt, wurde zwar die Anzahl dieses
 * Angebots dieses Anbieters in diesem Warenkorb erhöht, aber nicht mehr
 * überprüft, ob diese Anzahl dieses Angebotes bei diesem Anbieter
 * überhaupt vorhanden ist, da der dafür zuständige andere Trigger nicht
 * mehr ausgeführt wurde. Deshalb kombiniert dieser Trigger beide Trigger
 * in einem. Zusätzlich wurde noch ein dritter Trigger eingefügt, welcher
 * überprüft, ob das Angebot überhaupt von dem gewünschten Anbieter
 * angeboten wird.
 */
CREATE TRIGGER angebot_im_warenkorb
BEFORE INSERT ON Angebot_im_Warenkorb
/* Diese erste WHEN-Klausel überprüft, ob das Angebot noch in der ge-
 * wünschten Anzahl bei diesem Anbieter vorhanden ist.
 */
WHEN(
    SELECT (
        SELECT Bestand
        FROM Anbieter_bietet_an
        WHERE Angebots_ID = NEW.Angebots_ID
        AND Anbieterbezeichnung = NEW.Anbieterbezeichnung
    ) -
    (
        SELECT SUM(Anzahl)
        FROM Angebot_im_Warenkorb
        WHERE Angebots_ID = NEW.Angebots_ID
        AND Anbieterbezeichnung = NEW.Anbieterbezeichnung
    )
) <= NEW.Anzahl
/* Diese zweite WHEN-Klausel überprüft, ob der Warenkorb bereits eine
 * bestimmte Anzahl dieses Angebotes dieses Anbieters enthält.
 */
OR EXISTS (
    SELECT Anzahl
    FROM Angebot_im_Warenkorb
    WHERE Angebots_ID = NEW.Angebots_ID
    AND Anbieterbezeichnung = NEW.Anbieterbezeichnung
    AND Warenkorb_ID = NEW.Warenkorb_ID
)
/* Diese dritte WHEN-Klausel überprüft, ob das Angebot überhaupt von dem
 * gewünschten Anbieter angeboten wird.
 */
OR NOT EXISTS (
    SELECT Bestand
    FROM Anbieter_bietet_an
    WHERE Angebots_ID = NEW.Angebots_ID
    AND Anbieterbezeichnung = NEW.Anbieterbezeichnung
)
BEGIN
    /* Dieses erste SELECT-Statement bricht die Transaktion ab, wenn
     * das Angebot nicht mehr in der gewünschten Anzahl beim Anbieter
     * vorhanden ist. Dazu muss erneut die erste WHEN-Bedingung als
     * WHERE-Klausel dieses SELECT-Statements überprüft werden, damit
     * dieses SELECT-Statement nicht ausgeführt wird, wenn der Trigger
     * aufgrund der zweiten WHEN-Klausel ausgelöst wurde.
     */
    SELECT RAISE (ABORT, 'Gewünschte Anzahl dieses Angebots bei diesem Anbieter nicht verfügbar!')
    WHERE(
        SELECT (
            SELECT Bestand
            FROM Anbieter_bietet_an
            WHERE Angebots_ID = NEW.Angebots_ID
            AND Anbieterbezeichnung = NEW.Anbieterbezeichnung
        ) -
        (
            SELECT SUM(Anzahl)
            FROM Angebot_im_Warenkorb
            WHERE Angebots_ID = NEW.Angebots_ID
            AND Anbieterbezeichnung = NEW.Anbieterbezeichnung
        )
    ) <= NEW.Anzahl;
    /* Dieses zweite UPDATE-Statement erhöht die Anzahl des Angebotes
     * des Anbieters im Warenkorb um die gewünschte Anzahl, wenn dieses
     * bereits im Warenkorb enthalten wird. Auch hier muss erneut die
     * entsprechende WHEN-Bedingung als WHERE-Klausel geprüft werden,
     * damit dies nicht geschieht, wenn der Trigger oben aufgrund der
     * ersten WHEN-Klausel ausgelöst wurde.
     */
    UPDATE Angebot_im_Warenkorb
    SET Anzahl = Anzahl + NEW.Anzahl
    WHERE Angebots_ID = NEW.Angebots_ID
    AND Anbieterbezeichnung = NEW.Anbieterbezeichnung
    AND Warenkorb_ID = NEW.Warenkorb_ID
    AND EXISTS (
        SELECT Anzahl
        FROM Angebot_im_Warenkorb
        WHERE Angebots_ID = NEW.Angebots_ID
        AND Anbieterbezeichnung = NEW.Anbieterbezeichnung
        AND Warenkorb_ID = NEW.Warenkorb_ID
    );
    /* Gleiches gilt für dieses RAISE-Statement, welches ebenfalls nur
     * ausgeführt werden soll, wenn die zweite WHEN-Klausel zur
     * Auslösung des Triggers geführt hat.
     */
    SELECT RAISE (IGNORE)
    WHERE EXISTS (
        SELECT Anzahl
        FROM Angebot_im_Warenkorb
        WHERE Angebots_ID = NEW.Angebots_ID
        AND Anbieterbezeichnung = NEW.Anbieterbezeichnung
        AND Warenkorb_ID = NEW.Warenkorb_ID
    );
    /* Dieses vierte RAISE-Statement hat die Aufgabe, eine Fehlermeldung
     * auszugeben und die Transaktion abzubrechen, wenn ein Angebot von
     * einem Anbieter in den Warenkorb gelegt werden soll, der dieses
     * Angebot nicht anbietet.
     */
    SELECT RAISE (ABORT, 'Anbieter bietet dieses Angebot nicht an!')
    WHERE NOT EXISTS (
        SELECT Bestand
        FROM Anbieter_bietet_an
        WHERE Angebots_ID = NEW.Angebots_ID
        AND Anbieterbezeichnung = NEW.Anbieterbezeichnung
    );
END;

/* Wenn ein Kunde beim Bearbeiten der Accountdaten seine E-Mail-Adresse
 * aktualisiert, muss zunächst jede Referenz auf diesen Kunden anhand
 * der alten E-Mail-Adresse auf die neue E-Mail-Adresse geändert werden
 * (in allen Tabellen, in denen der Kunde mittels seiner E-Mail-Adresse
 * referenziert wird). Sonst gibt es einen FOREIGN KEY CONSTRAINT-error.
 */
CREATE TRIGGER e_mail_adressen_wechsel
BEFORE UPDATE ON Kunde
WHEN OLD.E_Mail_Adresse != NEW.E_Mail_Adresse
BEGIN
    UPDATE Premiumkunde
    SET E_Mail_Adresse = NEW.E_Mail_Adresse
    WHERE E_Mail_Adresse = OLD.E_Mail_Adresse;
    
    UPDATE Angestellter
    SET E_Mail_Adresse = NEW.E_Mail_Adresse
    WHERE E_Mail_Adresse = OLD.E_Mail_Adresse;
    
    UPDATE Warenkorb
    SET E_Mail_Adresse = NEW.E_Mail_Adresse
    WHERE E_Mail_Adresse = OLD.E_Mail_Adresse;
    
    UPDATE Newsletter
    SET E_Mail_Adresse = NEW.E_Mail_Adresse
    WHERE E_Mail_Adresse = OLD.E_Mail_Adresse;
    
    UPDATE Newsletterabo
    SET E_Mail_Adresse = NEW.E_Mail_Adresse
    WHERE E_Mail_Adresse = OLD.E_Mail_Adresse;
END;

/* Wenn ein Kunde seinen Account löscht, müssen zunächst alle Referenzen
 * auf diesen Account aus allen anderen Tabellen entfernt werden. Auch
 * hier droht sonst ein FOREIGN KEY CONSTRAINT-error.
 */
CREATE TRIGGER delete_account
BEFORE DELETE ON Kunde
BEGIN
    DELETE FROM Premiumkunde
    WHERE E_Mail_Adresse = OLD.E_Mail_Adresse;
    
    DELETE FROM Angestellter
    WHERE E_Mail_Adresse = OLD.E_Mail_Adresse;
    
    DELETE FROM Warenkorb
    WHERE E_Mail_Adresse = OLD.E_Mail_Adresse;
    
    DELETE FROM Newsletter
    WHERE E_Mail_Adresse = OLD.E_Mail_Adresse;
    
    DELETE FROM Newsletterabo
    WHERE E_Mail_Adresse = OLD.E_Mail_Adresse;
    
    /*DELETE FROM Kunde
    WHERE E_Mail_Adresse = OLD.E_Mail_Adresse;*/
END;

/* Wenn ein Anbieter gelöscht werden muss, müssen auch alle Angebote,
 * die dieser Anbieter anbietet, und deren Vorkommnisse in Warenkörben
 * zunächst gelöscht werden, da sonst ein FOREIGN KEY CONSTRAINT-error
 * droht.
 */
CREATE TRIGGER delete_anbieter
BEFORE DELETE ON Anbieter
BEGIN
    DELETE FROM Angebot_im_Warenkorb
    WHERE Anbieterbezeichnung = OLD.Anbieterbezeichnung;
    
    DELETE FROM Anbieter_bietet_an
    WHERE Anbieterbezeichnung = OLD.Anbieterbezeichnung;
    
    /*DELETE FROM Anbieter
    WHERE Anbieterbezeichnung = OLD.Anbieterbezeichnung;*/
END;

CREATE TRIGGER delete_newsletter
BEFORE DELETE ON Newsletter
BEGIN
    DELETE FROM Newsletterabo
    WHERE Newsletter_ID = OLD.Newsletter_ID;
    
    DELETE FROM Artikel_im_Newsletter
    WHERE Newsletter_ID = OLD.Newsletter_ID;
    
    /*DELETE FROM Newsletter
    WHERE Newsletter_ID = OLD.Newsletter_ID;*/
END;

/*==========================================
 *================ INSERTS =================
 *==========================================*/

SELECT '==========================================';
SELECT '================ INSERTS =================';
SELECT '==========================================';
SELECT '';

---------------------------------------------
------------------ Adresse ------------------
---------------------------------------------

SELECT 'Testing: 3 funktionierende Tests: Adresse';
INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID)
    VALUES ('Musterstrasse', '1', 11111, 'Musterort', NULL);
INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID)
    VALUES ('Musterweg', '2A', 22222, 'Musterstadt', NULL);
INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID)
    VALUES ('Musterallee', '33B', 33333, 'Musterdorf', NULL);

/* Die folgenden acht Tests sollten fehlschlagen:
 * - Der erste Test enthält mehr als einen Buchstaben am Ende der Hausnummer
 * - Der zweite Test enthält eine Zahl im Straßennamen
 * - Der dritte Test enthält eine Zahl im Ortsnamen
 * - Der vierte und fünfte Test enthält eine nicht-fünfstellige Postleitzahl.
 * - Der sechste Test enthält einen leeren Straßennamen.
 * - Der siebte Test enthält einen leeren Ortsnamen.
 * - Der achte Test enthält eine leere Hausnummer.
 */

SELECT 'Testing: 8 fehlschlagende Tests: Adresse';
INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID)
    VALUES ('Musterallee', '33BC', 33333, 'Musterdorf', NULL);
INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID)
    VALUES ('Mustera4llee', '33B', 33333, 'Musterdorf', NULL);
INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID)
    VALUES ('Musterallee', '33B', 33333, 'Muster4dorf', NULL);
INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID)
    VALUES ('Musterallee', '33B', 3333, 'Musterdorf', NULL);
INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID)
    VALUES ('Musterallee', '33B', 333333, 'Musterdorf', NULL);
INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID)
    VALUES ('', '33B', 33333, 'Musterdorf', NULL);
INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID)
    VALUES ('Musterallee', '33B', 333333, '', NULL);
INSERT INTO Adresse (Strasse, Hausnummer, PLZ, Ort, Adressen_ID)
    VALUES ('Musterallee', '', 333333, 'Musterdorf', NULL);

---------------------------------------------
------------------- Kunde -------------------
---------------------------------------------

SELECT '';
SELECT 'Testing: 12 funktionierende Tests: Kunde';
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('helge-schneider@helge-schneider.de', 'Helge', 'Schneider', '123456', 1);     -- Kunde, Angestellter
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('peter.thoms@helge-schneider.de', 'Peter', 'Thoms', '12345678', 2);           -- Kunde
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('peter.thoms@mail.com', 'Peter', 'Thoms', '!§"$!)§$(!)§$U§!"$KASFFH', 2);     -- Kunde, Premiumkunde
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('peter.thoms@mail.org', 'Peter', 'Thoms', '12345678', 2);                     -- Kunde
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('pete-york@helge-schneider.de', 'Pete', 'York', '123456', 3);                 -- Kunde, Premiumkunde, Angestellter
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('sergej_gleithmann@helge-schneider.de', 'Sergej', 'Gleithmann', '123456', 3); -- Kunde
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('buddy.casino@helge-schneider.de', 'Buddy', 'Casino', '123456', 3);           -- Kunde, Angestellter
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('rainer-lipski@helge-schneider.de', 'Rainer', 'Lipski', '123456', 2);         -- Kunde
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('kaistruwe@helge-schneider.de', 'Kai', 'Struwe', '123456', 3);                -- Kunde, Premiumkunde
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)   
    VALUES ('willy_ketzer@helge-schneider.de', 'Willy', 'Ketzer', '123456', 2);           -- Kunde, Angestellter
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('sandro-jampedro@helge-schneider.de', 'Sandro', 'Jampedro', '123456', 2);     -- Kunde
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('karlosboes@gmx.de', 'Karlos', 'Boes', '123456', 3);                          -- Kunde

/* Die folgenden neun Tests sollten fehlschlagen:
 * - Der erste Test enthält eine Zahl im Vornamen
 * - Der zweite Test enthält eine Zahl im Nachnamen
 * - Der dritte Test enthält eine E-Mail-Adresse ohne Top-Level-Domain
 * - Der vierte Test enthält eine E-Mail-Adresse ohne Second-Level-Domain
 * - Der fünfte Test enthält eine E-Mail-Adresse ohne Benutzernamen
 * - Der sechste Test enthält ein zu kurzes Passwort.
 * - Der siebte Test enthält eine leere E-Mail-Adresse.
 * - Der achte Test enthält einen leeren Vornamen.
 * - Der neunte Test enthält einen leeren Nachnamen.
 */

SELECT 'Testing: 9 fehlschlagende Tests: Kunde';
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('peter.thoms@mail.org', 'Pe3ter', 'Thoms', '123456', 3);
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('peter.thoms@mail.org', 'Peter', 'Tho4ms', '123456', 3);
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('peter.thoms@mail.', 'Pe3ter', 'Thoms', '123456', 3);
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('peter.thoms@.org', 'Pe3ter', 'Thoms', '123456', 3);
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('@mail.org', 'Pe3ter', 'Thoms', '123456', 3);
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('peter.thoms@mail.org', 'Peter', 'Thoms', '12345', 3);
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('', 'Peter', 'Thoms', '12345', 3);
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('peter.thoms@mail.org', '', 'Thoms', '12345', 3);
INSERT INTO Kunde (E_Mail_Adresse, Vorname, Nachname, Passwort, Adressen_ID)
    VALUES ('peter.thoms@mail.org', 'Peter', '', '12345', 3);

---------------------------------------------
---------------- Premiumkunde ---------------
---------------------------------------------

SELECT '';
SELECT 'Testing: 3 funktionierende Tests: Premiumkunde';
INSERT INTO Premiumkunde (Ablaufdatum, Studierendenausweis, E_Mail_Adresse)
    VALUES ('2300-11-02', NULL, 'peter.thoms@mail.com');
INSERT INTO Premiumkunde (Ablaufdatum, Studierendenausweis, E_Mail_Adresse)
    VALUES ('2300-11-02',
        readfile('201px-Helge_Schneider_auf_der_Frankfurter_Buchmesse_2015.JPG'),
        'kaistruwe@helge-schneider.de');
INSERT INTO Premiumkunde(Ablaufdatum, Studierendenausweis, E_Mail_Adresse)
    VALUES ('2300-11-02',
        readfile('201px-Helge_Schneider_auf_der_Frankfurter_Buchmesse_2015.JPG'),
        'pete-york@helge-schneider.de');

/* Die folgenden drei Tests sollten fehlschlagen:
 * - Der erste Test enthält ein Ablaufdatum, welches nicht in der Zukunft liegt
 * - Der zweite Test enthält ein Datum im falschen Format
 * - Der dritte Test referenziert einen Kunden, den es nicht gibt
 */

SELECT 'Testing: 3 fehlschlagende Tests: Premiumkunde';
INSERT INTO Premiumkunde (Ablaufdatum, Studierendenausweis, E_Mail_Adresse)
    VALUES ('2017-11-02', NULL, 'peter.thoms@mail.org');
INSERT INTO Premiumkunde (Ablaufdatum, Studierendenausweis, E_Mail_Adresse)
    VALUES ('02.11.2300', NULL, 'peter.thoms@mail.org');
INSERT INTO Premiumkunde (Ablaufdatum, Studierendenausweis, E_Mail_Adresse)
    VALUES ('2300-11-02', NULL, 'pete-york@mail.com');

---------------------------------------------
---------------- Angestellter ---------------
---------------------------------------------

SELECT '';
SELECT 'Testing: 4 funktionierende Tests: Angestellter';
INSERT INTO Angestellter (Jobbezeichnung, Gehalt, E_Mail_Adresse)
    VALUES ('Systemadministrator', 75001, 'pete-york@helge-schneider.de');
INSERT INTO Angestellter (Jobbezeichnung, Gehalt, E_Mail_Adresse)
    VALUES ('Systemadministrator', 90000, 'helge-schneider@helge-schneider.de');
INSERT INTO Angestellter (Jobbezeichnung, Gehalt, E_Mail_Adresse)
    VALUES ('Organist', 85000, 'buddy.casino@helge-schneider.de');
INSERT INTO Angestellter (Jobbezeichnung, Gehalt, E_Mail_Adresse)
    VALUES ('Systemadministrator', 69000, 'willy_ketzer@helge-schneider.de');

-- Der folgende Test sollte fehlschlagen, da die Jobbezeichnung leer ist.
SELECT 'Testing: 1 fehlschlagender Test: Angestellter';
INSERT INTO Angestellter (Jobbezeichnung, Gehalt, E_Mail_Adresse)
    VALUES ('', 69000, 'willy_ketzer@helge-schneider.de');

---------------------------------------------
---------------- Lieferdienst ---------------
---------------------------------------------

SELECT '';
SELECT 'Testing: 5 funktionierende Tests: Lieferdienst';
INSERT INTO Lieferdienst (Lieferdienst_Bezeichnung, Versandkosten)
    VALUES ('Deutsche Post', 2.50);
INSERT INTO Lieferdienst (Lieferdienst_Bezeichnung, Versandkosten)
    VALUES ('DHL', 3.50);
INSERT INTO Lieferdienst (Lieferdienst_Bezeichnung, Versandkosten)
    VALUES ('UPS', 4.00);
INSERT INTO Lieferdienst (Lieferdienst_Bezeichnung, Versandkosten)
    VALUES ('DHL mit Nachnahme', 5.50);
INSERT INTO Lieferdienst (Lieferdienst_Bezeichnung, Versandkosten)
    VALUES ('DHL Gefahrengutversand', 5.90);

/* Die folgenden drei Tests sollten fehlschlagen:
 * - Der erste Test enthält eine leere Lieferdienst-Bezeichnung.
 * - Der zweite Test enthält negative Versandkosten.
 * - Der dritte Test enthält nicht-numerische Versandkosten.
 */

SELECT 'Testing: 3 fehlschlagende Tests: Lieferdienst';
INSERT INTO Lieferdienst (Lieferdienst_Bezeichnung, Versandkosten)
    VALUES ('', 5.90);
INSERT INTO Lieferdienst (Lieferdienst_Bezeichnung, Versandkosten)
    VALUES ('DHL Gefahrengutversand', -5.90);
INSERT INTO Lieferdienst (Lieferdienst_Bezeichnung, Versandkosten)
    VALUES ('DHL Gefahrengutversand', 'aaa');

---------------------------------------------
----------------- Warenkorb -----------------
---------------------------------------------

SELECT '';
SELECT 'Testing: 9 funktionierende Tests: Warenkorb';
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES ('2017-12-11', 'Versendet', NULL, 'peter.thoms@mail.com',
        'DHL', '2027-12-15');
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES ('2017-12-13', 'Versandfertig', NULL, 'peter.thoms@mail.com',
        'UPS', NULL);
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES ('2017-12-09', 'Abgeschlossen', NULL, 'peter.thoms@mail.com',
        'DHL Gefahrengutversand', '2027-12-13');
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES ('2017-12-13', 'Versandfertig', NULL, 'kaistruwe@helge-schneider.de',
        'DHL mit Nachnahme', NULL);
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES ('2017-12-09', 'Abgeschlossen', NULL, 'kaistruwe@helge-schneider.de',
        'Deutsche Post', '2027-12-13');
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES ('2017-12-09', 'Abgeschlossen', NULL, 'pete-york@helge-schneider.de',
        'DHL', '2027-12-13');
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES ('2017-12-11', 'Versendet', NULL, 'rainer-lipski@helge-schneider.de',
        'Deutsche Post', '2027-12-15');
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES (NULL, 'Versandfertig', NULL, 'rainer-lipski@helge-schneider.de',
        'UPS', NULL);
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES (NULL, 'Abgeschlossen', NULL, 'sandro-jampedro@helge-schneider.de',
        'DHL Gefahrengutversand', '2027-12-13');

/* Die folgenden sechs Tests sollten fehlschlagen:
 * - Der erste Test enthält ein Bestelldatum im falschen Datumsformat,
 * - Der zweite Test enthält ein Lieferdatum im falschen Datumsformat,
 * - Der dritte Test enthält einen nicht bekannten Kunden,
 * - Der vierte Test enthält einen nicht bekannten Lieferdienst,
 * - Der fünfte Test enthält einen nicht bekannten Bestellstatus,
 * - Beim sechsten Test ist das Lieferdatum nicht größer gleich dem
 *   aktuellen Datum.
 */

SELECT 'Testing: 6 fehlschlagende Tests: Warenkorb';
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES ('2017-12.11', 'Versendet', NULL, 'rainer-lipski@helge-schneider.de',
        'Deutsche Post', '2027-12-15');
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES ('2017-12-11', 'Versendet', NULL, 'rainer-lipski@helge-schneider.de',
        'Deutsche Post', '2027-12.15');
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES ('2017-12-11', 'Versendet', NULL, 'rainer-lips2ki@helge-schneider.de',
        'Deutsche Post', '2027-12-15');
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES ('2017-12-11', 'Versendet', NULL, 'rainer-lipski@helge-schneider.de',
        'Deuts2che Post', '2027-12-15');
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES ('2017-12-11', 'Vers2endet', NULL, 'rainer-lipski@helge-schneider.de',
        'Deutsche Post', '2027-12-15');
INSERT INTO Warenkorb (Bestelldatum, Bestellstatus, Warenkorb_ID, E_Mail_Adresse,
    Lieferdienst_Bezeichnung, Lieferdatum)
    VALUES (NULL, 'Abgeschlossen', NULL, 'sandro-jampedro@helge-schneider.de',
        'DHL Gefahrengutversand', '2017-12-13');

---------------------------------------------
----------------- Lieferabo -----------------
---------------------------------------------

SELECT '';
SELECT 'Testing: 6 funktionierende Tests: Lieferabo';
INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID)
    VALUES (30, '2017-11-02', '2018-11-02', 1);
INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID)
    VALUES (30, '2017-11-02', '2018-11-02', 2);
INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID)
    VALUES (30, '2017-11-02', '2018-11-02', 3);
INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID)
    VALUES (30, '2017-11-02', '2018-11-02', 4);
INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID)
    VALUES (30, '2017-11-02', '2018-11-02', 5);
INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID)
    VALUES (30, '2017-11-02', '2018-11-02', 6);

/* Die folgenden sechs Tests sollten fehlschlagen:
 * - Der erste Test enthält eine nichtpositive Intervallangabe
 * - Der zweite Test enthält eine nichtganzzahlige Intervallangabe
 * - Der dritte Test enthält ein Beginn-Datum im falschen Datumsformat
 * - Der vierte Test enthält ein Ende-Datum im falschen Datumsformat
 * - Der fünfte Test enthält ein Ende-Datum vor dem Beginn-Datum
 * - Der sechste Test enthält eine ungültige Warenkorb_ID
 */

SELECT 'Testing: 6 fehlschlagende Tests: Lieferabo';
INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID)
    VALUES (-30, '2017-11-02', '2018-11-02', 6);
INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID)
    VALUES (3.0, '2017-11-02', '2018-11-02', 6);
INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID)
    VALUES (30, '2017.11-02', '2018-11-02', 6);
INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID)
    VALUES (30, '2017-11-02', '2018-11.02', 6);
INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID)
    VALUES (30, '2017-11-02', '2016-11-02', 6);
INSERT INTO Lieferabo (Intervall, Beginn, Ende, Warenkorb_ID)
    VALUES (30, '2017-11-02', '2018-11-02', 99999);

---------------------------------------------
----------------- Newsletter ----------------
---------------------------------------------

SELECT '';
SELECT 'Testing: 2 funktionierende Tests: Newsletter';
/* - Der erste Test testet, ob das Datum entsprechend gesetzt wird,
 *   wenn ein neuer Eintrag vorgenommen wurde.
 * - Der zweite Test testet, ob das Datum entsprechend aktualisiert wird,
 *   wenn ein Eintrag aktualisiert wurde.
 * WICHTIG: Auf der Kommandozeile sollte anschließend einmal das aktuelle
 *          Datum, dann ein altes Datum, und dann wieder das aktuelle
 *          Datum ausgegeben werden, um zu zeigen, dass der erste Eintrag
 *          mit dem aktuellen Datum erstellt wird, und der zweite Eintrag,
 *          der mit dem alten Datum erstellt wird, bei einem Update mit
 *          dem neuen Datum aktualisiert wird.
 */

INSERT INTO Newsletter (Betreff, Text, Newsletter_ID, E_Mail_Adresse)
    VALUES ('Supertolle Neuheiten', 'Das sind die supertollen Neuheiten',
        NULL, 'pete-york@helge-schneider.de');
SELECT Datum FROM Newsletter WHERE Newsletter_ID == 1;
INSERT INTO Newsletter (Betreff, Text, Datum, Newsletter_ID, E_Mail_Adresse)
    VALUES ('Supertolle Neuheiten', 'Das sind die supertollen Neuheiten',
        '2017-11-02', NULL, 'pete-york@helge-schneider.de');
SELECT Datum FROM Newsletter WHERE Newsletter_ID == 2;
UPDATE Newsletter SET Betreff = 'Noch bessere Neuheiten',
    Text = 'Das sind die noch besseren Neuheiten!'
    WHERE Newsletter_ID == 2;
SELECT Datum FROM Newsletter WHERE Newsletter_ID == 2;

/* Die folgenden 4 Tests sollten fehlschlagen:
 * - Der erste Test enthält ein fehlerhaftes Datumsformat.
 * - Der zweite Test enthält eine E-Mail-Adresse, die nicht zu einem
 *   Angestellten gehört.
 * - Der dritte Test enthält einen leeren Betreff.
 * - Der vierte Test enthält einen leeren Text.
 */

SELECT 'Testing: 4 fehlschlagende Tests: Newsletter';
INSERT INTO Newsletter (Betreff, Text, Datum, Newsletter_ID, E_Mail_Adresse)
    VALUES ('Supertolle Neuheiten', 'Das sind die supertollen Neuheiten',
        '2017-11.02', NULL, 'pete-york@helge-schneider.de');
INSERT INTO Newsletter (Betreff, Text, Datum, Newsletter_ID, E_Mail_Adresse)
    VALUES ('Supertolle Neuheiten', 'Das sind die supertollen Neuheiten',
        '2017-11-02', NULL, 'sergej_gleithmann@helge-schneider.de');
INSERT INTO Newsletter (Betreff, Text, Datum, Newsletter_ID, E_Mail_Adresse)
    VALUES ('', 'Das sind die supertollen Neuheiten',
        '2017-11.02', NULL, 'pete-york@helge-schneider.de');
INSERT INTO Newsletter (Betreff, Text, Datum, Newsletter_ID, E_Mail_Adresse)
    VALUES ('Supertolle Neuheiten', '',
        '2017-11.02', NULL, 'pete-york@helge-schneider.de');

---------------------------------------------
--------------- Newsletterabo ---------------
---------------------------------------------

SELECT '';
SELECT 'Testing: 2 funktionierende Tests: Newsletterabo';
INSERT INTO Newsletterabo (E_Mail_Adresse, Newsletter_ID)
    VALUES ('sergej_gleithmann@helge-schneider.de', 1);
INSERT INTO Newsletterabo (E_Mail_Adresse, Newsletter_ID)
    VALUES ('sergej_gleithmann@helge-schneider.de', 2);
INSERT INTO Newsletterabo (E_Mail_Adresse, Newsletter_ID)
    VALUES ('karlosboes@gmx.de', 2);

---------------------------------------------
------------------ Artikel ------------------
---------------------------------------------

SELECT '';
SELECT 'Testing: 11 funktionierende Tests: Artikel';
INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID)
    VALUES ('Artikel', 'Ein Artikel', NULL, NULL);
INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID)
    VALUES ('Super Artikel', 'Ein super Artikel',
    readfile('210px-HelgeSchneider_2009_2_amk.jpg'), NULL);
INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID)
    VALUES ('Toller Artikel', 'Ein toller Artikel',
    readfile('210px-HelgeSchneider_2009_2_amk.jpg'), NULL);
INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID)
    VALUES ('Großartiger Artikel', 'Ein großartiger Artikel',
    readfile('210px-HelgeSchneider_2009_2_amk.jpg'), NULL);
INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID)
    VALUES ('Mega Artikel', 'Ein mega-Artikel', NULL, NULL);
INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID)
    VALUES ('Hammer Artikel', 'Ein Hammer Artikel', NULL, NULL);
INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID)
    VALUES ('Krasser Artikel', 'Ein krasser Artikel', NULL, NULL);
INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID)
    VALUES ('Heftiger Artikel', 'Ein heftiger Artikel', NULL, NULL);
INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID)
    VALUES ('Langweiliger Artikel', 'Ein langweiliger Artikel', NULL, NULL);
INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID)
    VALUES ('Banane', 'Eine Banane.', NULL, NULL);
INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID)
    VALUES ('Ottifant', 'Ein Ottifant.', NULL, NULL);

/* Die folgenden zwei Tests sollten fehlschlagen:
 * - Der erste Test enthält eine leere Artikelbezeichnung.
 * - Der zweite Test enthält eine leere Artikelbeschreibung.
 */

SELECT 'Testing: 2 fehlschlagende Tests: Artikel';
INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID)
    VALUES ('', 'Ein Ottifant.', NULL, NULL);
INSERT INTO Artikel (Bezeichnung, Beschreibung, Bild, Artikel_ID)
    VALUES ('Ottifant', '', NULL, NULL);

---------------------------------------------
------------------ Angebot ------------------
---------------------------------------------

SELECT '';
SELECT 'Testing: 6 funktionierende Tests: Angebot';
INSERT INTO Angebot (Angebots_ID, Artikel_ID, Preis)
    VALUES (NULL, 1, 1.50);
INSERT INTO Angebot (Angebots_ID, Artikel_ID, Preis)
    VALUES (NULL, 1, 2.50);
INSERT INTO Angebot (Angebots_ID, Artikel_ID, Preis)
    VALUES (NULL, 2, 3.50);
INSERT INTO Angebot (Angebots_ID, Artikel_ID, Preis)
    VALUES (NULL, 3, 4000);
INSERT INTO Angebot (Angebots_ID, Artikel_ID, Preis)
    VALUES (NULL, 5, 16.50);
INSERT INTO Angebot (Angebots_ID, Artikel_ID, Preis)
    VALUES (NULL, 5, 50);

---------------------------------------------
------------------ Anbieter -----------------
---------------------------------------------

SELECT '';
SELECT 'Testing: 6 funktionierende Tests: Anbieter';
INSERT INTO Anbieter (Anbieterbezeichnung)
    VALUES ('Helge Schneider Fanshop');
INSERT INTO Anbieter (Anbieterbezeichnung)
    VALUES ('Petes tolle Sachen');
INSERT INTO Anbieter (Anbieterbezeichnung)
    VALUES ('Peters Müllhalde');
INSERT INTO Anbieter (Anbieterbezeichnung)
    VALUES ('Sergejs langsamer Versandhandel');
INSERT INTO Anbieter (Anbieterbezeichnung)
    VALUES ('Super-beschissen-Export, Inc.');
INSERT INTO Anbieter (Anbieterbezeichnung)
    VALUES ('Teurer-gehts-nicht-Express GmbH');

-- Der folgende Test sollte fehlschlagen, da der Anbieter leer ist.

SELECT 'Testing: 1 fehlschlagender Test: Anbieter';
INSERT INTO Anbieter (Anbieterbezeichnung)
    VALUES ('');

---------------------------------------------
------------- Anbieter_bietet_an ------------
---------------------------------------------

SELECT '';
SELECT 'Testing: 16 funktionierende Tests: Anbieter_bietet_an';
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Helge Schneider Fanshop', 1, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Helge Schneider Fanshop', 2, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Helge Schneider Fanshop', 5, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Helge Schneider Fanshop', 3, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Super-beschissen-Export, Inc.', 1, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Super-beschissen-Export, Inc.', 2, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Super-beschissen-Export, Inc.', 3, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Super-beschissen-Export, Inc.', 5, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Teurer-gehts-nicht-Express GmbH', 1, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Teurer-gehts-nicht-Express GmbH', 5, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Petes tolle Sachen', 1, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Petes tolle Sachen', 2, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Petes tolle Sachen', 3, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Petes tolle Sachen', 5, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Peters Müllhalde', 2, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Peters Müllhalde', 3, 100);

/* Die folgenden Tests sollten fehlschlagen:
 * - Der erste Test lässt einen Anbieter ein Angebot anbieten, welches
 *   er bereits anbietet.
 * - Der zweite Test enthält einen negativen Bestand.
 */

SELECT 'Testing: 2 fehlschlagende Tests: Anbieter_bietet_an';
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Peters Müllhalde', 3, 100);
INSERT INTO Anbieter_bietet_an (Anbieterbezeichnung, Angebots_ID, Bestand)
    VALUES ('Peters Müllhalde', 5, -100);

---------------------------------------------
------------ Angebot_im_Warenkorb -----------
---------------------------------------------

SELECT '';
SELECT 'Testing: 6 funktionierende Tests: Angebot_im_Warenkorb';
INSERT INTO Angebot_im_Warenkorb (Angebots_ID, Anbieterbezeichnung,
    Warenkorb_ID, Anzahl)
        VALUES (1, 'Helge Schneider Fanshop', 1, 5);
INSERT INTO Angebot_im_Warenkorb (Angebots_ID, Anbieterbezeichnung,
    Warenkorb_ID, Anzahl)
        VALUES (1, 'Super-beschissen-Export, Inc.', 1, 5);
INSERT INTO Angebot_im_Warenkorb (Angebots_ID, Anbieterbezeichnung,
    Warenkorb_ID, Anzahl)
        VALUES (1, 'Super-beschissen-Export, Inc.', 2, 5);
INSERT INTO Angebot_im_Warenkorb (Angebots_ID, Anbieterbezeichnung,
    Warenkorb_ID, Anzahl)
        VALUES (2, 'Helge Schneider Fanshop', 3, 5);
INSERT INTO Angebot_im_Warenkorb (Angebots_ID, Anbieterbezeichnung,
    Warenkorb_ID, Anzahl)
        VALUES (2, 'Super-beschissen-Export, Inc.', 3, 5);
INSERT INTO Angebot_im_Warenkorb (Angebots_ID, Anbieterbezeichnung,
    Warenkorb_ID, Anzahl)
        VALUES (3, 'Super-beschissen-Export, Inc.', 5, 5);

/* Die folgenden vier Tests sollten fehlschlagen:
 * - Der erste Test enthält eine negative Anzahl
 * - Der zweite Test versucht einem Warenkorb Angebote eines Anbieters
 *   hinzuzüfügen, der bereits dasselbe Angebot dieses Anbieters enthält.
 * - Der dritte Test versucht, ein Angebot in den Warenkorb zu legen,
 *   welches von keinem Anbieter angeboten wird.
 * - Der vierte Test versucht, ein Angebot in einer höheren Stückzahl
 *   in den Warenkorb zu legen, als der Anbieter es anbietet.
 */

SELECT 'Testing: 4 fehlschlagende Tests: Angebot_im_Warenkorb';
INSERT INTO Angebot_im_Warenkorb (Angebots_ID, Anbieterbezeichnung,
    Warenkorb_ID, Anzahl)
        VALUES (1, 'Super-beschissen-Export, Inc.', 5, -5);
INSERT INTO Angebot_im_Warenkorb (Angebots_ID, Anbieterbezeichnung,
    Warenkorb_ID, Anzahl)
        VALUES (3, 'Super-beschissen-Export, Inc.', 5, 5);
INSERT INTO Angebot_im_Warenkorb (Angebots_ID, Anbieterbezeichnung,
    Warenkorb_ID, Anzahl)
        VALUES (4, 'Helge Schneider Fanshop', 5, 5);
INSERT INTO Angebot_im_Warenkorb (Angebots_ID, Anbieterbezeichnung,
    Warenkorb_ID, Anzahl)
        VALUES (1, 'Helge Schneider Fanshop', 5, 900);

---------------------------------------------
----------- Artikel_im_Newsletter -----------
---------------------------------------------

SELECT '';
SELECT 'Testing: 8 funktionierende Tests: Artikel_im_Newsletter';
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (1, 1);
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (1, 2);
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (2, 1);
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (2, 2);
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (3, 1);
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (3, 2);
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (4, 1);
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (5, 2);

/* Der folgende Test sollte fehlschlagen, da mehr als 10 Artikel im
 * selben Newsletter referenziert werden sollen.*/
SELECT 'Testing: 1 fehlschlagender Test: Artikel_im_Newsletter';
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (5, 1);
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (6, 1);
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (7, 1);
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (8, 1);
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (9, 1);
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (10, 1);
INSERT INTO Artikel_im_Newsletter (Artikel_ID, Newsletter_ID) VALUES (11, 1);

---------------------------------------------
--------- Artikel_empfiehlt_Artikel ---------
---------------------------------------------

SELECT '';
SELECT 'Testing: 5 funktionierende Tests: Artikel_empfiehlt_Artikel';
INSERT INTO Artikel_empfiehlt_Artikel (Artikel_ID1, Artikel_ID2) VALUES (1, 2);
INSERT INTO Artikel_empfiehlt_Artikel (Artikel_ID1, Artikel_ID2) VALUES (2, 1);
INSERT INTO Artikel_empfiehlt_Artikel (Artikel_ID1, Artikel_ID2) VALUES (3, 2);
INSERT INTO Artikel_empfiehlt_Artikel (Artikel_ID1, Artikel_ID2) VALUES (4, 2);
INSERT INTO Artikel_empfiehlt_Artikel (Artikel_ID1, Artikel_ID2) VALUES (5, 1);

---------------------------------------------
---------------- Schlagwort -----------------
---------------------------------------------

SELECT '';
SELECT 'Testing: 5 funktionierende Tests: Schlagwort';
INSERT INTO Schlagwort (Schlagwort) VALUES ('Werkzeug');
INSERT INTO Schlagwort (Schlagwort) VALUES ('Malerzubehör');
INSERT INTO Schlagwort (Schlagwort) VALUES ('Unnützes Zeug');
INSERT INTO Schlagwort (Schlagwort) VALUES ('Müll');
INSERT INTO Schlagwort (Schlagwort) VALUES ('Obst');

/* Die folgenden zwei Tests sollten fehlschlagen:
 * - Der erste Test versucht, ein neues Schlagwort anzulegen, obwohl es
 *   bereits dasselbe Schlagwort mit einer anderen Groß- und Kleinschreibung
 *   gibt.
 * - Der zweite Test hat ein leeres Schlagwort.
 */

SELECT '';
SELECT 'Testing: 2 fehlschlagender Test: Schlagwort';
INSERT INTO Schlagwort (Schlagwort) VALUES ('unnützes Zeug');
INSERT INTO Schlagwort (Schlagwort) VALUES ('');

---------------------------------------------
------- Artikel_gehoert_zu_Schlagwort -------
---------------------------------------------

SELECT '';
SELECT 'Testing: 12 funktionierende Tests: Artikel_gehoert_zu_Schlagwort';
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (1, 'Werkzeug');
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (1, 'Malerzubehör');
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (1, 'Unnützes Zeug');
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (2, 'Werkzeug');
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (2, 'Obst');
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (3, 'Werkzeug');
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (3, 'Malerzubehör');
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (4, 'Werkzeug');
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (4, 'Malerzubehör');
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (4, 'Müll');
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (4, 'Obst');
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (5, 'Werkzeug');
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (5, 'Malerzubehör');

/* Der folgende Test sollte fehlschlagen, da er versucht, einen Artikel
 * zu einem Schlagwort hinzuzufügen, das dieser Artikel bereits hat.
 */

SELECT 'Testing: 1 fehlschlagender Test: Artikel_gehoert_zu_Schlagwort';
INSERT INTO Artikel_gehoert_zu_Schlagwort (Artikel_ID, Schlagwort)
    VALUES (5, 'Malerzubehör');

/*==========================================
 *============= TEST-ANFRAGEN ==============
 *==========================================*/

SELECT '==========================================';
SELECT '============= TEST-ANFRAGEN ==============';
SELECT '==========================================';
SELECT '';

SELECT '';
SELECT 'Lieferdienste, deren Versandkosten zwischen 5 und 6 Euro liegen:';
SELECT * FROM Lieferdienst
WHERE Versandkosten > 5 AND Versandkosten < 6;

SELECT '';
SELECT 'Angestellte mit Jobbezeichnung "Sytemadministrator" und Jahres-';
SELECT '  gehalt über 75000 Euro:';
SELECT * FROM Angestellter
WHERE Jobbezeichnung = 'Systemadministrator' AND Gehalt > 75000;

SELECT '';
SELECT 'Premiumkunden mit der höchsten Anzahl an Lieferabos:';
SELECT MAX(Anzahl), E_Mail_Adresse FROM (
    SELECT COUNT(*) AS Anzahl, Premiumkunde.E_Mail_Adresse FROM Lieferabo
    JOIN Warenkorb on Lieferabo.Warenkorb_ID == Warenkorb.Warenkorb_ID
    JOIN Premiumkunde on Premiumkunde.E_Mail_Adresse == Warenkorb.E_Mail_Adresse
    GROUP BY Premiumkunde.E_Mail_Adresse
);

SELECT '';
SELECT 'Artikel aus mindestens zwei Newslettern mit Kategorie "Werkzeug"';
SELECT '  und Kategorie "Malerzubehör":';

SELECT AnzahlInNewslettern, NewsletterArtikelID, Schlagwort1, Schlagwort2
FROM (
    SELECT * FROM (
        SELECT COUNT(*) AS AnzahlInNewslettern,
        Artikel_im_Newsletter.Artikel_ID AS NewsletterArtikelID
        FROM Artikel_im_Newsletter
        GROUP BY Artikel_im_Newsletter.Artikel_ID
        HAVING COUNT(*) >= 2
    ) JOIN (
        SELECT Artikel_ID AS SchlagwortArtikelID1, Schlagwort AS Schlagwort1
        FROM Artikel_gehoert_zu_Schlagwort
        WHERE Schlagwort = 'Malerzubehör'
    ) ON SchlagwortArtikelID1 == NewsletterArtikelID
    JOIN (
        SELECT Artikel_ID AS SchlagwortArtikelID2, Schlagwort AS Schlagwort2
        FROM Artikel_gehoert_zu_Schlagwort
        WHERE Schlagwort = 'Werkzeug'
    )
    ON SchlagwortArtikelID2 == NewsletterArtikelID
)
;--ON SchlagwortArtiklelID;

SELECT '';
SELECT 'Artikel, zu denen ein Bild vorliegt, aber kein Angebot:';
SELECT * FROM Artikel
LEFT JOIN Angebot on Angebot.Artikel_ID == Artikel.Artikel_ID
WHERE Artikel.Bild IS NOT NULL AND Angebot.Angebots_ID IS NULL;
