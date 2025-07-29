# Das Vorgehensmodell


Das vogeschlagene Vorgehensmodell ist eine Kombination von (XP, Wasserfall-Modell & Evolutionäres Vorgehen)
- Projekt in vollständige und realisierbare Phasen aufteilen
- Eine Phase besteht aus einigen zusammengehörigen Anforderungen, die als User Stories geschrieben sind
- Eine nächste Phase beginnt erst dann, wenn die vorhergehende Phase abgeschlossen ist
- Eine Phase ist abgeschlossen, wenn alle Tests laufen

#### Planen &rarr; Programmieren &rarr; Prüfen &rarr; Wiederholen


<br><br>
## Planen
- Im Team zusammensetzen
- Funktionen diskutieren
- Anforderungen feststellen und festhalten
- Aufgaben verteilen


<br><br>
## Programmieren

- Die Aufgaben die auf die Teams verteilt wurden, werden programmiert
- Dabei wird statische Analyse benutzt
  - FindBugs (sollte öfter durchgeführt werden)
  - Code Coverage
  - SpringAnnotation (Für Validation von Inputs)


<br><br>
## Prüfen

Zur Hilfe siehe [TestPyramide](https://github.com/hhu-propra2-2017/abschlussprojekt-bits-please/blob/develop/Architektur/Qualität-Sicherung/TestPyramide/TestPyramide.pdf)
 
- Zuerst wird jede Unit in Isolation getestet
- Dann testet man die Integration zwischen Units
- Am Ende wird die ganze Phase manuell getestet

>*Ein Unit ist Eine Klasse! (Klassenisolation)*

>*Die Logik wird immer getestet*


<br><br>
## Vorgeschlagene Phasen

Phase 1 bis 6 siehe [Phasen](https://github.com/hhu-propra2-2017/abschlussprojekt-bits-please/tree/develop/Phasen)


<br><br>
## Änderungen während der Entwicklung

Die Tests der Phasen wurden immer um eine halbe bis ganze Phase nach hinten verschoben. Erst so konnte das Programmierte getestet werden.

Da wir 6 Leute in der Gruppe sind gab es immer 3 Teams. Diese waren größtenteils in die Aufgaben Services (Datenbank), Tests und Views unterteilt. Das Service Team hat die Unit Tests hauptsächlich direkt selbst geschrieben, auch um die Funktionalität des Programmierten direkt testen und verbessern zu können. Die Views wurden durch das jeweilige Team erst manuell getestet, dann wurden vom Test Team sowohl Tests für die Controller als auch Integrationtests geschrieben.

Die HTML Templates werden nicht direkt getestet, sondern nur manuell.
