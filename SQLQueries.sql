--Show-Artikel_empfielt_Artikel-Query
SELECT
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
ON 'Artikel2-Artikel_ID' = Artikel.Artikel_ID

--simpler version: (above doesn't work)
SELECT
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
