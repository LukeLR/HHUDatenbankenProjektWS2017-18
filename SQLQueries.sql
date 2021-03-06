.headers ON
--Show-Artikel_empfielt_Artikel-Query
/*SELECT
    'Artikel1-Bezeichnung',
    'Artikel1-Beschreibung',
    'Artikel1-Bild',
    'Artikel1-Artikel_ID',
    'Artikel2-Artikel_ID',
    Bezeichnung AS 'Artikel2-Bezeichnung',
    Beschreibung AS 'Artikel2-Beschreibung',
    Bild AS 'Artikel2-Bild'
FROM (
    SELECT
        Bezeichnung AS 'Artikel1-Bezeichnung',
        Beschreibung AS 'Artikel1-Beschreibung',
        Bild AS 'Artikel1-Bild',
        Artikel_ID1 AS 'Artikel1-Artikel_ID',
        Artikel_ID2 AS 'Artikel2-Artikel_ID'
    FROM Artikel_empfiehlt_Artikel
    JOIN Artikel
    ON Artikel_empfiehlt_Artikel.Artikel_ID1 = Artikel.Artikel_ID
)
JOIN Artikel
ON 'Artikel2-Artikel_ID' = Artikel.Artikel_ID */

--simpler version: (above doesn't work)
/* SELECT
    a1.Bezeichnung AS 'Artikel1-Bezeichnung',
    a1.Beschreibung AS 'Artikel1-Beschreibung',
    a1.Bild AS 'Artikel1-Bild',
    a1.Artikel_ID AS 'Artikel1-Artikel_ID',
    a2.Artikel_ID AS 'Artikel2-Artikel_ID',
    a2.Bezeichnung AS 'Artikel2-Bezeichnung',
    a2.Beschreibung AS 'Artikel2-Beschreibung',
    a2.Bild AS 'Artikel2-Bild'
FROM Artikel_empfiehlt_Artikel a
JOIN Artikel a1
ON a.Artikel_ID1 = a1.Artikel_ID
JOIN Artikel a2
ON a.Artikel_ID2 = a2.Artikel_ID
WHERE a1.Bezeichnung LIKE '%Su%'*/

--Show-Anbieter-bietet-an-Query
/*SELECT
    Anbieter_bietet_an.Anbieterbezeichnung,
    Anbieter_bietet_an.Angebots_ID,
    Anbieter_bietet_an.Bestand,
    Angebot.Angebots_ID,
    Angebot.Artikel_ID,
    Angebot.Preis,
    Artikel.Bezeichnung,
    Artikel.Beschreibung,
    Artikel.Bild
FROM Anbieter_bietet_an
JOIN Angebot
ON Anbieter_bietet_an.Angebots_ID = Angebot.Angebots_ID
JOIN Artikel
ON Angebot.Artikel_ID = Artikel.Artikel_ID*/

--Check-Lagerbestand-Query für Trigger
SELECT Angebots_ID, SUM(Bestand)
FROM Anbieter_bietet_an
GROUP BY Angebots_ID
HAVING Angebots_ID = 5 AND
SUM(Bestand) > 400;

SELECT Angebots_ID, Anbieterbezeichnung, Bestand
FROM Anbieter_bietet_an
WHERE Angebots_ID = 1
AND Anbieterbezeichnung = 'Helge Schneider Fanshop'
AND 5 < Bestand;

SELECT (
    SELECT Bestand
    FROM Anbieter_bietet_an
    WHERE Angebots_ID = 1
    AND Anbieterbezeichnung = 'Helge Schneider Fanshop'
) -
(
    SELECT SUM(Anzahl)
    FROM Angebot_im_Warenkorb
    WHERE Angebots_ID = 1
    AND Anbieterbezeichnung = 'Helge Schneider Fanshop'
) AS Anzahl;
