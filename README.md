# Setup
Aufgabe | Befehl | Ordner
--------|--------|--------
Alle Daten zurücksetzen | `sudo rm -rf database`<br>`sudo rm -rf documents`  | `./`
Aktuelles .jar erstellen | `gradle build -x test ` | `/webapp/app`
Dockerimage erstellen | `docker build . -t webapp` | `/webapp`
Hochfahren | `docker-compose up` | `./`

Datenbank Konfiguration in `.env` Datei.

Username | Passwort
---------|---------
admin | admin

Wenn der Ordner `database` inkl. Daten noch nicht existiert, so muss `docker-compose up` zweimal hintereinander aufgerufen werden. Grund: Der Service `webapp` wartet zwar bis der Service `database` gestartet wird, jedoch müssen beim ersten Start der mysql Datenbank noch einige Dinge konfigurieren, deshalb kann `webapp` noch nicht auf den Datenbank Container zugreifen und das Programm schlägt fehl. Daher muss man danach `docker-compose up` einfach nochmal aufrufen. Die App kann dann über `localhost` aufgerufen werden.



<br><br>
<br><br>
<br><br>

# Wichtige Entscheidungen

* Es werden keine Mails zur Kommunikation verwendet. Stattdessen wurde ein eigener Nachrichten-Dienst implementiert, welcher über die Datenbank läuft
* Die Beziehung zwischen Student und Mentor wird durch die Entität Assignment dargestellt
* Studenten, Mentoren und Oranisatoren werden alle in einer Tabelle `user` (und nicht in separaten Tabellen) in der Datenbank gespeichert, sie können durch das Attribut `role` unterschieden werden
* Benutzung von Spring Security für Authentifizierung
* Die Seiten sind rollen- und accountspezifisch
* Es gibt einen User der das Mentoring System darstellt und über den Erinnerungsnachrichten verschickt werden
* Es werden bei Mentorenwechsel/Fallabschluss sowie bei Terminvorschlägen Erinnerungen versendet
* Erinnerungsnachrichten für Termine werden nicht als Nachricht in der Datenbank gespeichert, sondern innerhalb der App generiert
* Mentoren können Fälle abgeben/beenden. Es wird nicht unterschieden ob sie dies aufgrund von Differenzen zwischen ihm und dem Studenten tun oder weil der Fall beendet ist. Dies kann im Grund genauer angegeben werden
* Die Mail Adresse wird zur Identifikation der User benutzt
* Für die Styles in den Templates wurde Bootstrap verwendet
* Die Dateigröße für den Dokumenten-Upload ist auf 10MB beschränkt
* Hochgeladene Dokumente werden nicht in der Datenbank gespeichert. In der Datenbank werden nur die Dateinamen gespeichert, die Dokumente liegen in einem Ordner ("documents") auf dem webapp Container
* Termine, Nachrichten, Dokumente, Vereinbarungen und Notizen können nicht vom User gelöscht werden
* Die Navbar bietet eine schnelle Übersicht über alle Funktionen, sie ist das Dashboard




<br><br>
<br><br>
<br><br>

# User Stories
Alle User Stories zu unserem System befinden sich [hier](https://github.com/hhu-propra2-2017/abschlussprojekt-bits-please/blob/develop/Architektur/UserStories.md).


<br><br>
<br><br>
<br><br>

# Qualitätssicherung
Das Vorgehensmodell zur Qualitätssicherung befindet sich [hier](https://github.com/hhu-propra2-2017/abschlussprojekt-bits-please/blob/develop/Architektur/Qualit%C3%A4t-Sicherung/Qualit%C3%A4tssicherung.md).



<br><br>
<br><br>
<br><br>
 
# Domainmodel

Zur Hilfe siehe [Domainmodel-Skizze](https://github.com/hhu-propra2-2017/abschlussprojekt-bits-please/blob/develop/Architektur/Modelle/Domain%20Model/DomainModel.pdf). 

Es gibt 5 Domänen:
- Core Domain:
  - Vermittlung
- Subdomain:
  - Authentifizierung
  - Akte
  - Mitteilung
  - Terminvereinbarung
  


### Vermittlung
In der Vermittlung werden Mentoren ihren Studenten zugewiesen. Außerdem werden hier Mentorenwechsel durchgeführt und Mentoren können ihre Studenten abgeben.

**Verbindung - Authentifizierung**: Neu Registrierte Benutzer (vor allem Studenten) werden der Vermittlungsdomäne von der Authentifizierung übermittelt. 



### Authentifizierung
Für jede Rolle wird hier die Registrierung und Anmeldung organisiert.



### Akte
Hier werden die Daten eines Falles (Notizen, Vereinbarungen, Dokumente) gespeichert.

**Verbindung - Vermittlung**: Jeder Student besitzt seine persönliche Akte, sofern er denn einem Mentor zugewiesen wurde.



### Mitteilung
Diese Domäne kümmert sich um die Kommunikation zwischen Student und Mentor bzw. Organisator. Außerdem werden hier die Erinnerungen erstellt und an die betreffenden Personen abgeschickt. Weiterhin soll hier das Dashboard verwaltet werden.
Hier ist die Schnittstelle für AUAS implemntiert. AUAS soll anhand seiner Daten einen Studentent für das Mentoring vorschlagen. Die Mitteilungsdomäne soll daraufhin diesen Studenten über das Programm informieren. 

**Verbindung - Vermittlung**: Die Mittelungsdomäne muss wissen, welcher Student mit welchem Mentor kommunizieren soll, denn der Student sollte nur mit seinem Mentor Nachrichten austaschen können.

**Verbindung - Terminvereinbarung**: Die Mitteilungsdomäne muss von der Terminvereinbarung erfahren, für welche Termine sie Erinnerungen verschicken soll.



### Termine
Alle Termine werden hier verwaltet. Es können Terminvorchläge hinzugefügt, verarbeitet werden und bestehende Termine können abgesagt werden. Es können zusätzlich Gesprächsprotokolle hinzugefügt werden.

**Verbindung - Vermittlung**: Die Terminvereinbarung muss wissen, welche Benutzer für Termine zur Verfügung stehen.



<br><br>
<br><br>
<br><br>

# Architektur

Zur Hilfe siehe [Architektur Diagramm](https://github.com/hhu-propra2-2017/abschlussprojekt-bits-please/blob/develop/Architektur/Modelle/Architektur/Architektur.pdf).

Die Architektur ist nach dem *Model-View-Controller* Pattern erstellt worden. Die *Views* sind die HTML-Templates und die *Controller* sind die Spring Controller.

Das *Model* wiederrum ist als Schichtenarchitektur aufgebaut. Es besteht grob gesagt aus Services, welche auf die Datenbank zugreifen. Die Services bestehen aus Interfaces und deren Implementationen, welche mit der Datenbank über die Repositories kommunizieren. Die Idee ist also, dass die *Controller* nur auf die Services zugreifen und so total unabhängig von der Datenbank sind. Die *Controller* lassen sich von Spring die Implementation injezieren, indem sie eine Variable mit dem entsprechenden Interface erstellen und diese mit `@Autowired` annotiert wird.

### Services
Jeder Service ist in einem eigenen Package, welches wiederrum in dem `services` Package liegt. In den jeweiligen Packages befindet sich dann ein Interface und eine dazugehörige Implementation. Nur beim Messaging Service ist zusätzlich noch der `ReminderService` im Package, welcher nur auf die Methoden des `Messengers` zugreift um das versenden von vorgefertigten Errinerungsnachrichten zu vereinfachen. 

### Controller
Die Controller selber sind aufgeteilt in einen `MainController`, welcher für die Startseite und Weiterleitung von eingeloggten Usern zu ihrem jeweiligen Bereich (Student, Mentor oder Organizer) zuständig ist, einen `RegistryController` der für das Registrieren von neuen Usern behandelt und weitere Controller für jede Rolle. Die Controller für jede Rolle (Student, Mentor, Organizer) sind unterteilt in einen File Controller für die Aktenübersicht/hinzufügen der Studentenakten und einen Controller für alle anderen Seiten(Postfach, Termine, Wechsel-/Fallabschlussantrag).

### Views
Die Templates (View) sind genauso aufgeteilt wie die Controller und es gibt noch das Template error.html, welches bei auftretenden Fehlern je nach URL und HTTP Statuscode zu einer Fehlerseite weiterleitet.

### Tests
Unsere Tests sind unterteilt in Controllertests mit einer Testklasse für jeden Controller und Databasetests mit einer Testklasse für jeden Service.



<br><br>
<br><br>
<br><br>

# ER-Modell

Zur Hilfe siehe [ER-Modell-Diagramm](https://github.com/hhu-propra2-2017/abschlussprojekt-bits-please/blob/develop/Architektur/Modelle/ER-Model/ER-Model.pdf).

Die Datenbank hat die Aufgabe Benutzer, Nachrichten, Termine und die Studentenakte zu speichern. Zur Erkläuterung wird das ER-Modell in 4 Abschnitte unterteilt:
- Benutzer und Beziehungen
- Nachrichten
- Akte
- Termine


<br><br>

## Benutzer und Beziehungen 

In diesem Teil sollen Benutzer und Beziehung zwischen Student und Mentor gespeichert werden. Die Funktionen des Mentorenwechsels durch den Studenten bzw. der Fall-Abgabe durch den Mentor werden hier mit Hilfe von zwei Entitäten realisiert, mehr dazu in der Detailbeschreibung.

### User
Hier werden alle Benutzer des Systems gespeichert, die Attribute sind grundlegende Informationen über den Benutzer. Unterschieden wird zwischen den Benutzern über das Attribute "Rolle" welches Student, Mentor oder Organisator sein kann.

### Assignment
Ein Assignment wird benutzt um die Zuweisung zwischen einem Student und einem Mentor zu speichern. Dies dient unter anderem dazu, dass die Datenspeicherung in anderen Entitäten einfacher wird, denn so muss man nicht immer Student und Mentor einzeln abspeichern, sondern kann direkt diese Beziehung zwischen den beiden benutzen.

### Wechselantrag (ChangeRequest)
Wenn ein Student einen Antrag auf Mentorenwechsel stellt, so wird ein neuer Eintrag in dieser Tabelle eingefügt. Er referenziert auf das betroffene Assignment. Es gibt nun folgende Fälle:
1. Organisator gestattet den Antrag: Das Assignment und der dazugehörige Antrag wird aus den jeweiligen Tabellen gelöscht. Der Student ist also nun wieder ohne Mentor und ihm kann in einem späteren Schritt ein neuer zugewiesen werden.
2. Organisator lehnt den Antrag ab: Der Antrag wird einfach gelöscht.

Es muss außerdem immer ein Grund für den Antrag angegeben werden.

### Fallabschlussantrag (ClosingRequest)
Dies ist im Grunde genommen genauso wie die Entität Wechselantrag, nur dass ein Fallabschlussantrag ausschließlich von einem Mentor gestellt werden kann. Die Entitäten wurden bewusst getrennt, da sie zwar die selben Attribute haben, aber eine andere Aufgabe.


<br><br>

## Nachrichten

Nachrichten werden benutzt um die Kommukation zwischen Student und Mentor, zwischen Organisator und allen anderen Benutzern, sowie die Erinnerungsnachrichten zu realisieren.

### Nachricht (Message)
Ein Eintrag in dieser Entität stellt eine Nachricht dar. Sie beinhalten einen Titel, den Inhalt und das Versanddatum, sowie einen Absender und einen Empfänger.


<br><br>

## Akte (File)

Der Teil Akte besitzt mehrere Entitäten die dazu dienen, fallbezogene Informationen zu speichern.

### Vereinbarung (Agreement)
Eine Vereinbarung speichert ein vorgegebenes Ziel, welches zu einem gegebenen Zeitpunkt erreicht werden soll. Es wird außerdem der Startzeitpunkt gespeichert.

### Dokument (Document)
Ein Dokument enthält einen Titel und eine Datei. Diese Datei kann z.B. ein Aufgabenblatt sein, welches der Mentor dem Student zur Verfügung stellt. Außerdem wird das Upload Datum und ein Titel gespeichert.

### Notiz (Note)
Der Mentor kann eine Notiz erstellen, um sich wichtige Informationen zum Fall zu speichern. Die Notiz hat einen Titel, einen Inhalt und ein Erstelldatum.


<br><br>

## Termine

Termine werden benutzt um Treffen zwischen Student und Mentor zu organisieren.

### Terminvorschlag (AppointmentProposal)
Der Mentor kann einem Studenten einen Termin vorschlagen, wenn er dies tut wird in dieser Entität ein Eintrag erstellt. Ein Terminvorschlag speichert das vorgeschlagene Datum.
Wird der Vorschlag akzeptiert so wird ein richtiger Termin erstellt.

### Termin (Appointment)
Ein Termin repräsentiert die tatsächliche Vereinbarung auf ein Treffen. Hier wird das Datum gesichert und es wird gespeichert ob der Termin stattfindet oder noch nachträglich abgesagt wurde.

### Gesprächsprotokoll (ConversationLog)
Ein Gesprächsprotokoll kann erstellt werden, um einen Termin zwischen Student und Mentor zu dokumentieren. Ein Gesprächsprotokoll enthält einen Text (das Protokoll selbst) und eine Referenz auf den dazugehörigen Termin.
#   M e n t o r F i n d e r  
 #   M e n t o r F i n d e r  
 #   M e n t o r F i n d e r  
 