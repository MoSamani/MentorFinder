# Anmeldung/Registrierung


## UC1.001: Registrieren Student

### Ziel
Ein neuer Student registriert sich im System.

### Ablauf
- Geht auf Startseite des Systems
- Gibt die angeforderten Daten ein
- Fertig




## UC1.002: Registrieren Mentor

Nur mit Name




## UC1.003: Registrieren Organisator

Nur mit Name




## UC1.004: Anmeldung Student

### Ziel
Student meldet sich an

### Ablauf
- Student geht auf Startseite
- Klickt auf "Als Student anmelden"
- Gibt Matrikelnummer und Passwort ein -> angemeldet




## UC1.005: Anmeldung Mentor

### Ziel
Mentor meldet sich an

### Ablauf
- Geht auf Startseite
- Klickt auf "Als Mentor anmelden"
- Gibt Namen und Passwort ein -> angemeldet




## UC1.006: Anmeldung Organisator

### Ziel
Organisator meldet sich an

### Ablauf
- Geht auf Startseite
- Klickt auf "Als Organisator anmelden"
- Gibt Namen und Passwort ein -> angemeldet






# Nachrichten


## UC2.001: Nachricht senden Student -> Mentor

### Ziel
Student schickt dem Mentor eine Nachricht

### Ablauf
- Student öffnet sein persönliches Dashboard
- Klickt auf einen Button für Nachrichten
- Klickt auf "Neue Nachricht"
- Schreibt eine Nachricht und schickt diese ab
- Die Nachricht wird automatisch an seinen Mentor geschickt




## UC2.002: Nachricht senden Mentor -> Student

### Ziel
Mentor schickt einem seiner Studenten eine Nachricht

### Ablauf
- Mentor öffnet sein Dashboard (wählt keinen speziellen Fall aus)
- Klickt auf einen Button für Nachrichten
- Klickt auf "Neue Nachricht"
- Schreibt eine Nachricht
- Wählt als Empfänger einen seiner Studenten aus einer Liste aus
- Schickt Nachricht ab




## UC2.003: Nachrichten senden Organisator -> Mentor/Student/Organisator

### Ziel
Organisator schickt jemandem eine Nachricht

### Ablauf
- Organisator öffnet sein Dashboard
- Klickt auf einen Button für Nachrichten
- Klickt auf "Neue Nachricht"
- Schreibt Nachricht
- Wählt als Empfänger einen Mentor oder Studenten aus einer Liste aus
- Schickt Nachricht ab




## UC2.004: Nachrichten einsehen Student

### Ziel
Student sieht die Konversation zwischen ihm und seinem Mentor

### Ablauf
- Student öffnet sein Dashboard
- Klickt auf einen Button für Nachrichten
- Konversation wird angezeigt




## UC2.005: Nachrichten einsehen Mentor

### Ziel
Mentor sieht die Konversation zwischen ihm und einem seiner Studenten

### Ablauf
- Mentor öffnet sein Dashboard
- Klickt auf einen Button für Nachrichten
- Wählt einen seiner Studenten aus einer Liste aus
- Konversation wird angezeigt




## UC2.006: Nachrichten einsehen Organisator

### Ziel
Organisator sieht die Konversation zwischen ihm und einem Mentor oder Studenten

### Ablauf
- Organisator öffnet sein Dashboard
- Klickt auf einen Button für Nachrichten
- Wählt einen seiner Gesprächspartner aus einer Liste aus (Mentor oder Student)






# Zuweisung Student - Mentor


## UC3.001: Organisator Übersicht der neuen Studenten

### Ziel
Organisator hat eine Liste alle Studenten die einen Mentor brauchen




## UC3.002: Zuweisung eines Mentors

### Ziel
Ein Organisator weist einem (neu registrierten) Studenten einen Mentor zu

### Ablauf
- Organisator schaut sich die Übersicht der Studenten die einen Mentor brauchen an
- Organisator klickt einen Studenten an
- Ein Popup öffnet sich -> Organisator wählt einen Mentor aus
- Um Entscheidung zu erleichtern, soll dem Organisator angezeigt werden, wie viele Studenten der jeweilige Mentor schon betreut




## UC3.003: Student beantragt Wechsel

### Ziel
Student beantragt Wechsel

### Ablauf
- Geht zu Dashboard
- Beantragt Wechsel über irgendeinen Button
- Gibt Grund an




## UC3.004: Mentor beantragt Student abzugeben

### Ziel
Mentor beantragt Wechsel

### Ablauf
- Geht zu Dashboard
- Wählt Fall Akte aus
- Beantragt Wechsel über irgendeinen Button
- Gibt Grund an




## UC3.005: Organisator bearbeitet Wechsel

### Ziel
Dem Student wird ein neuer Mentor zugewiesen

### Ablauf 
- Organistor geht auf Dashboard
- Dort wird ihm eine Liste mit Studenten angezeigt die einen Wechsel beantragt haben
- Organisator klickt auf einen Fall
- Kann Wechsel bestätigen oder ablehnen
- Abgelehnt: Student aus der Liste -> nichts weiteres passiert
- Bestätigt: Student aus Liste -> in die Liste der Studenten die keinen Mentor haben -> neue Zuweisung




## UC3.006: Organisator bearbeitet Absage durch Mentor

### Ziel
Mentor den Student "entziehen"

### Ablauf
- Organisator geht auf Dashboard
- Dort ist Liste mit Mentoren die einen Antrag eingereicht haben
- Wählt einen Antrag aus -> bestätigen oder ablehnen
- Abgelehnt: Mentor aus Liste -> nichts weiteres passiert
- Bestätigt: Mentor aus Liste -> Student in Liste die keinen Mentor mehr haben -> neue Zuweisung






# Dashboard

## UC4.001: Organisator Übersicht über Mentoren und deren Fälle (Anzahl)




## UC4.002: Auf neue Nachrichten wird im Dashboard hingewiesen




?????## UC4.003: Übersicht über die von AUAS gegebenen Kandidaten?????




 

# Termine

## UC5.001: Student schlägt Mentor Termin vor

### Ablauf:
- Student guckt auf Dashboard
- Terminübersicht auswählen
- Neuen Vorschlag einreichen (Datum, Uhrzeit, Betreff)




## UC5.002: Mentor schlägt Student Termin vor

### Ablauf:
- Mentor guckt auf Dashboard
- Terminübersicht auswählen
- Neuen Vorschlag einreichen (Student, Datum, Uhrzeit, Betreff)




## UC5.003: Terminvorschlag bearbeiten

### Ablauf
- Geht in Terminübersicht
- Akzeptiert Termin oder lehnt ihn ab (mit Begründung)

### Zusatz
- Auf Dashboard wird angezeigt wenn ein neuer Vorschlag vorliegt
- Auf Dashboard wird angezeigt wenn ein Vorschlag bestätigt abgelehnt wurde




## UC5.004: Termin absagen

### Ziel
Student/Mentor sagt einen Termin ab, der zuvor schon bestätigt wurde

### Ablauf
- Geht in Terminübersicht
- Klickt auf irgendeinen Button "Absagen"
- Der andere bekommt eine automatisierte Nachricht über die Absage




## UC5.005: Erinnerungsmail

### Ziel
Ein Tag vorher bekommen sowohl Student als auch Mentor eine Erinnerung für ihren Termin






# Studentenakte

## UC6.001: Notitzen hinzufügen Mentor




## UC6.002: Gesprächsprotokoll hinzufügen Mentor




## UC6.003: Vereinbarung hinzufügen Mentor






## UC?.???: AUAS Schnittstelle

### Ziel
Einem Studenten, welcher noch nicht am Mentoring Programm teilnimmt, wird eine Mail gesendet um ihn einzuladen

### Ablauf
- AUAS benutzt Mentoring System Schnittstelle, schickt die Mail Adresse eines Studenten mit (Vorschlag fürs Mentoring System)
- Mentoring System schickt eine automatisierte Mail an diesen Studenten, in welchem er zum Mentoring System eingeladen wird

### Zusatz
- Der Text einer solchen Mail soll einfach zu verändern sein
