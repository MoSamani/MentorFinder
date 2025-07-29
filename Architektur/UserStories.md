# User Stories
_______________________________________________________________________________________________________________________________
<br><br>

# Anmeldung/Registrierung

<br><br>
## UC1.001: Registrieren Student

### Ablauf
- Geht auf Startseite des Systems
- Wechselt durch einen Klick auf "Registrieren als Student" zur Registrierungsseite für Studenten
- Gibt die angeforderten Daten (Vorname, Nachname, Email-Adresse, Passwort, Nachricht *optional*) ein
- Bei falscher Eingabe der Daten wird eine entsprechende Fehlermeldung angegeben
- Bei richtiger Eingabe der Daten wird der Student nun zur Startseite weitergeleitet und hat dort eine Verlinkung zum Login
- Falls eine Nachricht angegeben wird, wird diese an alle Organisatoren gesendet



<br><br>
## UC1.002: Registrieren Mentor

### Ablauf
- Geht auf Startseite des Systems
- Wechselt durch einen Klick auf "Registrieren als Mentor" zur Registrierungsseite für Mentoren
- Gibt die angeforderten Daten (Vorname, Nachname, Email-Adresse, Passwort) ein
- Bei falscher Eingabe der Daten wird eine entsprechende Fehlermeldung angegeben
- Bei richtiger Eingabe der Daten wird der Mentor nun zur Startseite weitergeleitet und hat dort eine Verlinkung zum Login 



<br><br>
## UC1.003: Registrieren Organisator

### Ablauf
- Geht auf Startseite des Systems
- Wechselt durch einen Klick auf "Registrieren als  Organisator" zur Registrierungsseite für Organisatoren
- Gibt die angeforderten Daten (Vorname, Nachname, Email-Adresse, Passwort) ein
- Bei falscher Eingabe der Daten wird eine entsprechende Fehlermeldung angegeben
- Bei richtiger Eingabe der Daten wird der Organisator nun zur Startseite weitergeleitet und hat dort eine Verlinkung zum Login 



<br><br>
## UC1.004: Anmeldung Student

### Ablauf
- Student geht auf Startseite
- Klickt auf "Zur Startseite"
- Gibt Email-Adresse und Passwort ein
- Bei falscher Eingabe der Daten wird eine entsprechende Fehlermeldung angegeben
- Bei richtiger Eingabe der Daten wird der Student auf das Studentendashboard weitergeleitet



<br><br>
## UC1.005: Anmeldung Mentor

### Ablauf
- Mentor geht auf Startseite
- Klickt auf "Zur Startseite"
- Gibt Email-Adresse und Passwort ein
- Bei falscher Eingabe der Daten wird eine entsprechende Fehlermeldung angegeben
- Bei richtiger Eingabe der Daten wird der Mentor auf das Mentorendashboard weitergeleitet



<br><br>
## UC1.006: Anmeldung Organisator

### Ablauf
- Organisator geht auf Startseite
- Klickt auf "Zur Startseite"
- Gibt Email-Adresse und Passwort ein
- Bei falscher Eingabe der Daten wird eine entsprechende Fehlermeldung angegeben
- Bei richtiger Eingabe der Daten wird der Organisator auf das Organisatorendashboard weitergeleitet

<br><br>
## UC1.007: Ausloggen Student/Mentor/Organisator

### Ablauf
- Wenn man eingeloggt ist kann man durch einen Klick auf den Button "Ausloggen" der Navbar, der sich auf jeder Seite befindet (außer auf der Startseite), wird der User ausgeloggt und zur Startseite weitergeleitet



<br><br>
# Nachrichten

<br><br>
## UC2.001: Nachricht senden Student

### Ablauf
- Student öffnet sein Dashboard
- Klickt auf den Button "Post" in der Navbar und wird zum Posteingang weitergeleitet
- Klickt dort auf Link für Nachricht senden und wird zur Seite für die Nachrichtenverfassung weitergeleitet
- Schreibt Nachricht an zugewiesenen Mentor (ohne Zuweisung ist keine Nachrichtenerstellung möglich)
- Schickt Nachricht ab und wird zum Postausgang weitergeleitet



<br><br>
## UC2.002: Nachricht senden Mentor

### Ablauf
- Mentor öffnet sein Dashboard
- Klickt auf den Button "Post" in der Navbar und wird zum Posteingang weitergeleitet
- Klickt dort auf Link für Nachricht senden und wird zur Seite für die Nachrichtenverfassung weitergeleitet
- Wählt als Empfänger einen ihm zugewiesenen Studenten (falls er welche hat) oder einen Organisator aus einer Liste aus
- Schreibt Nachricht
- Schickt Nachricht ab und wird zum Postausgang weitergeleitet




<br><br>
## UC2.003: Nachrichten senden Organisator

### Ablauf
- Organisator öffnet sein Dashboard
- Klickt auf den Button "Post" in der Navbar und wird zum Posteingang weitergeleitet
- Klickt dort auf Link für Nachricht senden und wird zur Seite für die Nachrichtenverfassung weitergeleitet
- Wählt als Empfänger einen Mentor, Studenten oder anderen Organisator (alle falls vorhanden) aus einer Liste aus
- Schreibt Nachricht
- Schickt Nachricht ab und wird zum Postausgang weitergeleitet




<br><br>
## UC2.004: Empfangene Nachrichten einsehen Student / Mentor / Organisator

### Ablauf
- User öffnet sein Dashboard
- Klickt auf den Button "Post" und wird zum Posteingang weitergeleitet
- Sieht eine Liste aller empfangenen Nachrichten mit Informationen über Absender (Username, Rolle), Titel und Datum
- Klickt auf den Titel einer Nachricht und wird weitergeleitet zu einer Seite die die Nachricht anzeigt (Titel, Inhalt, Absender)
- Kann nach dem Lesen der Nachricht durch den Button "Zurück zum Posteingang" wieder die Liste der empfangenen Nachrichten sehen



<br><br>
## UC2.005: Gesendete Nachrichten einsehen Student / Mentor / Organisator

### Ablauf
- User öffnet sein Dashboard
- Klickt auf den Button "Post" und wird zum Posteingang weitergeleitet
- Klickt auf Button "Postausgang" und wird zum Postausgang weitergeleitet
- Sieht eine Liste aller gesendeten Nachrichten mit Informationen über Empfänger(Username, Rolle), Titel und Datum
- Klickt auf den Titel einer Nachricht und wird weitergeleitet zu einer Seite die die Nachricht anzeigt (Titel, Inhalt, Absender)
- Kann nach dem Lesen der Nachricht durch den Button "Zurück zum Postausgang" wieder die Liste der empfangenen Nachrichten sehen




<br><br>
# Zuweisung Student - Mentor

<br><br>
## UC3.001: Organisator Übersicht über nicht zugewiesene Studenten

### Ablauf
- Organisator klickt in der Navbar auf den Button "Studenten ohne Mentor"
- Er wird zu Seite mit Übersicht/Liste über alle registrierten Studenten ohne Mentor weitergeleitet


<br><br>
## UC3.002: Zuweisung eines Mentors

### Ablauf
- Organisator klickt in der Navbar auf den Button "Studenten ohne Mentor"
- Er wird zu Seite mit Übersicht/Liste über alle registrierten Studenten ohne Mentor weitergeleitet
- Er klickt auf den Link "Mentor hinzufügen"
- Er kann in einer Liste einen Mentor auswählen
- Durch Klicken auf "Absenden" wird die Zuweisung abgeschlossen
- Er wird zurück zu der Übersicht/Liste über alle registrierten Studenten ohne Mentor weitergeleitet
- Mentor und Student bekommen eine automatisch generierte Nachricht über diese Zuweisung

<br><br>
## UC3.003: Student beantragt Mentornwechsel

### Ablauf
- Student klickt in der Navbar auf den Button "Mentorwechsel"
- Wenn dem Studenten noch kein Mentor zugewiesen wurde, wird dies angezeigt und er kann keinen Mentorenwechsel beantragen
- Ansonten wird er auf eine Seite weitergeleitet auf der er den Grund angeben kann
- Durch Klicken auf "Antrag senden" wird der Antrag an die Organisatoren weitergeleitet
- Student und Mentor werden über das Einreichen des Antrages durch eine automatisch generierte Nachricht informiert



<br><br>
## UC3.004: Mentor beantragt Student abzugeben

### Ablauf
- Mentor klickt in der Navbar auf den Button "Fallabschluss"
- Mentor bekommt eine Liste mit den ihm zugewiesenen Studenten angezeigt und kann durch klicken einen auswählen
- (Wenn dem Mentor noch kein Student zugewiesen wurde wird ihm eine leere Liste angezeigt und er kann keinen Studenten auswählen)
- Wird auf eine Seite weitergeleitet auf der er den Grund angeben kann (Fall ist beendet oder Differenzen mit dem Studenten)
- Durch Klicken auf "Antrag senden" wird der Antrag an die Organisatoren weitergeleitet
- Student und Mentor werden über das Einreichen des Antrages durch eine automatisch generierte Nachricht informiert



<br><br>
## UC3.005: Organisator bearbeitet Mentorenwechselanträge

### Ablauf 
- Organistor klickt in der Navbar auf den Button "Mentorenwechselanträge"
- Dort wird ihm eine Liste Wechselanträgen, die zugehörigen Studenten und eine Vorschau des Grunds angezeigt
- Durch den Klick auf "Ansehen" bei einem bestimmten Fall kann man den gesamten Grund und die zugehörigen User einsehen
- Von dort aus kann der Antrag angenommen oder abgelehnt werden (dann wird der Organisator wieder auf die Liste der Mentorenwechselanträge weitergeleitet)
- Abgelehnt: Antrag wird aus der Liste gelöscht -> nichts weiteres passiert (Student und Mentor bekommen eine automatisch erstellte Nachricht)
- Bestätigt: Antrag wird aus der Liste gelöscht -> Student wird wieder in der Liste der Studenten angezeigt, die keinen Mentor haben und kann von da aus neu zugewiesen werden (Student und Mentor bekommen eine automatisch erstellte Nachricht)



<br><br>
## UC3.006: Organisator bearbeitet Fallabschlussanträge

### Ablauf
- Organistor klickt in der Navbar auf den Button "Fallabschlussanträge"
- Dort wird ihm eine Liste Liste Fallabschlussanträgen, die zugehörigen Mentoren und Studenten und eine Vorschau des Grunds angezeigt
- Durch den Klick auf "Ansehen" bei einem bestimmten Fall kann man den gesamten Grund und die zugehörigen User einsehen
- Von dort aus kann der Antrag angenommen oder abgelehnt werden (dann wird der Organisator wieder auf die Liste der Fallabschlussanträge weitergeleitet)
- Abgelehnt: Antrag wird aus der Liste gelöscht -> nichts weiteres passiert (Mentor und Student bekommen automatisch erstellte Nachricht)
- Bestätigt: Antrag wird aus der Liste gelöscht -> Student wird wieder in der Liste der Studenten angezeigt, die keinen Mentor haben und kann von da aus neu zugewiesen werden (Mentor und Student bekommen eine automatisch generierte Nachricht)



<br><br>
## UC3.007: Assignments einsehen und nachjustieren

### Ablauf
- Organisator klickt in der Navabr auf den Button "Assignments"
- Dort kann er alle laufenden Fälle und die Beteiligten sehen
- Er kann auf einen Button "Auflösen" klicken um das Assignment aufzuheben
- Es wird eine automatisch generierte Nachricht an Student und Mentor gesendet



<br><br>
# Termine
<br><br>
## UC4.001: Mentor schlägt Student Termine vor

### Ablauf:
- Mentor klickt in der Navbar auf den Button "Termine"
- Er wird auf eine Seite weitergeleitet, auf der alle bevorstehende Termine angezeigt werden
- Er kann von hier aus auf den Link "neuer Termin" klicken
- Er wir dauf eine Seite weitergeleitet auf der einen Studenten auswählen kann und 3 Terminvorschläge machen kann, indem er Datum und Uhrzeit für jeden Termin angibt
- Student bekommt automatisch generierte Nachricht darüber, dass neue Terminvorschläge für ihn erstellt wurden


<br><br>
## UC4.003: Terminvorschläge einsehen Mentor

### Ablauf
- Ein Mentor kann seine gemachten Terminvorschläge, die noch nicht bearbeitet wurden ansehen, indem er in der Navbar auf den Button "Termine" und dann auf "ausstehende Terminvorschläge" klickt
- Es wird ihm einer Liste aller seiner gemachten, nich vom Studenten bearbeiteten Terminvorschläge mit den Daten angezeigt


<br><br>
## UC4.002: Terminvorschlag wird vom Studenten bearbeitet

### Ablauf:
- Student klickt in der Navbar auf den Button "Termine"
- Dort befindet sich eine Liste mit den aktuellen Termine und der austehenden Terminvorschlägen
- Durch einen Klick auf "austehende Terminvotschläge" wird die Liste der vorgeschlagenen Termine ausgefahren
- Klickt der Student auf einen dieser Terminvorschläge wird er auf eine Seite weitergeleitet auf der er entweder einen der 3 Termine, oder ein Feld auswählen kann um zu sagen, dass er alle Termine ablehnen möchte
- Mit einem Klick auf "Auswählen" wird die Auswahl bestätigt und bei Wahl eines Termins ein Termineintrag für Student und Mentor erstellt
- Der Mentor bekommt eine automatisch generierte Nachricht darüber





<br><br>
## UC4.003: Termine Ansehen Mentor/Student

### Ablauf
- User klickt in der Navbar auf den Button "Termine"
- Dort befindet sich eine Liste mit den aktuellen Terminen mit zugehörigen User, dem Datum, dem Status und ggf. dem Gesprächsprotokoll
- Status: hier steht ob der Termin bereits stattgefunden hat und ansonsten lässt sich dieser hier durch den Button "absagen" absagen
- Gesprächsprotokoll: 
  - Termin stattgefunden, Gesprächsprotokoll noch nicht erstellt, Ansicht Mentor: durch einen Klick auf den Button "Hinzufügen" lässt sich ein Gesprächsprotokoll hinzufügen
   - Termin stattgefunden, Gesprächsprotokoll noch nicht erstellt, Ansicht Student: es wird angezeigt, dass das Gesprächsprotokoll noch nicht vorhanden ist
   - Termin stattgefunden, Gesprächsprotokoll erstellt: durch einen Klick auf den Button "Ansehen" lässt sich das Gesprächsprotokoll ansehen


<br><br>
## UC4.004: Termin absagen

### Ziel
Student/Mentor sagt einen Termin ab, der zuvor schon bestätigt wurde

### Ablauf
- User klickt in der Navbar auf den Button "Termine"
- Dort befindet sich eine Liste mit den aktuellen Terminen
- In der Spalte "Status" kann bei noch nicht stattgefundenen Terminen "absagen" ausgewählt werden
- Der Terminpartner bekommt eine automatisch generierte Nachricht darüber



<br><br>
## UC4.005: Erinnerungsmail

### Ziel
Ein Tag vorher bekommen sowohl Student als auch Mentor eine Erinnerung für ihren Termin

### Ablauf
- Die automatisch generierte Nachricht befindet sich einen Tag vor dem Termin im Posteingang des Studenten und des Mentors 




<br><br>
## UC4.006: Gesprächsprotokoll wird vom Mentor hinzugefügt

### Ablauf
- Mentor klickt in der Navbar auf den Button "Termine"
- Dort befindet sich eine Liste mit seinen Terminen
- In der Spalte "Gesprächsprotokoll" befindet sich für die nicht abgesagten, stattgefundenen Termine ein "Hinzufügen" Button
- Der Mentor wird auf eine Seite weitergeleitet auf der er das Gesprächsprotokoll eingeben und speichern kann

<br><br>
## UC4.007: Gesprächsprotokoll einsehen Mentor/Student

### Ablauf
- Mentor klickt in der Navbar auf den Button "Termine"
- Dort befindet sich eine Liste mit den aktuellen Terminen
- Wenn bereits ein Gesprächsprotokoll zu einem Termin besteht wird dies in der Spalte "Gesprächsprotokoll" angezeigt indem dort ein "Ansehen" Button ist
- Klickt der User nun auf diesen Button wird er auf eine Seite weitergeleitet, die das Gesprächsprotokoll anzeigt

<br><br>
# Studentenakte
<br><br>
## UC5.002: Notitzen einsehen Mentor/Organisator

### Ablauf
- User klickt in der Navbar auf den Button "Studentenakte"
- User wählt in der Dropdown-Liste einen Studenten aus zu dem er eine Notiz hinzufügen möchte
- User klickt auf den Button "Student auswählen" und wird zur Notizenübersicht des Studenten weitergeleitet


<br><br>
## UC5.001: Notitzen hinzufügen Mentor

### Ablauf
- Mentor klickt in der Navbar auf den Button "Studentenakte"
- Mentor wählt in der Dropdown-Liste einen Studenten aus zu dem er eine Notiz hinzufügen möchte
- Mentor klickt auf den Button "Student auswählen" und wird zur Notizenübersicht des Studenten weitergeleitet
- Mentor klickt auf den Button "Notiz hinzufügen" und wird auf eine Seite weitergeleitet, bei der er die Notiz für den ausgewählten Student erstellen kann.
- Er gibt Titel und Inhalt an und klickt auf den Button "Notiz hinzufügen"
- Die Notiz wurde bei nicht-leeren Inhalt erstellt und der Mentor wird zur Notizenübersicht über diesen Studenten weitergeleitet. Bei leerem Inhalt wird der Mentor darum gebeten Titel und Inhalt auszufüllen


<br><br>
## UC5.002: Dokumente hinzufügen Mentor/Organisator

### Ablauf 
- Mentor/Organisator klickt in der Navbar auf den Button "Studentenakte"
- Mentor/Organisator klickt auf den Link "Dokumente" und wird zur Dokumentenübersicht weitergeleitet
- Mentor/Organisator wählt in der Dropdown-Liste einen Studenten aus zu dem er ein Dokument hochladen möchte
- Mentor/Organisator klickt auf den Button "Student auswählen" und wird zur Dokumentenübersicht über den Student weitergeleitet
- Dort kann er auf den Button "Dokument hinzufügen" drücken und wird zu einer Seite weitergeleitet, auf der er ein Dokument hochladen kann
- Mentor/Organisator schreibt Titel und wählt durch "Durchsuchen" eine Datei aus und klickt auf den Button "Dokument hinzufügen"
- Ist kein Titel gegeben, wird er aufgefordert einen anzugeben
- Ist kein Dokument ausgewählt, wird die Errorseite gezeigt
- Ansonsten wurde das Dokument gespeichert und der Mentor wird wieder zur Dokumentenübersicht über den Studenten weitergeleitet

<br><br>
## UC5.003: Dokumente einsehen Mentor/Organisator

### Ablauf
- Mentor/Organisator klickt in der Navbar auf den Button "Studentenakte"
- Mentor/Organisator klickt auf den Link "Dokumente" und wird zur Dokumentenübersicht weitergeleitet
- Mentor/Organisator wählt in der Dropdown-Liste einen Studenten aus und drückt den "Student auswählen" Button
- Es werden alle Dokumente die für den Studenten hochgeladen wurden mit Titel und Datum angezeigt
- Durch Klicken auf den Titel, kann man das Dokument herunterladen

<br><br>
## UC5.004: Dokumente einsehen Student

### Ablauf
- Student klickt in der Navbar auf den Button "Meine Akte"
- Student klickt auf den Link "Dokumente" und wird zur Dokumentenübersicht weitergeleitet
- Es werden alle Dokumente die für den Studenten hochgeladen wurden mit Titel und Datum angezeigt
- Durch Klicken auf den Titel, kann man das Dokument herunterladen

<br><br>
## UC5.005: Vereinbarung hinzufügen Mentor/Organisator

### Ablauf
- Mentor/Organisator klickt in der Navbar auf den Button "Studentenakte"
- Mentor/Organisator klickt auf den Link "Vereinbarungen" und wird zur Vereinbarungenübersicht weitergeleitet
- kann in der Dropdown-Liste einen Student auswählen und durch Klick auf den Button "Studenten auswählen" die Auswahl bestätigen
- Durch Klick auf den Button "Vereinbarung hinzufügen" wird er auf eine Seite weitergeleitet, wo er den Termin definieren kann
- Er gibt Ziel, Datum und Zeit der Deadline an und bestätigt die Vereinbarungserstellung durch Klick auf den Button "Verinbarung hinzufügen" und die Vereinbarung wird im System gespeichert
- Daraufhin wird der Mentor/Organisator wieder zur Vereinbarungenübersicht über den Studenten weitergeleitet

<br><br>
## UC5.006: Vereinbarungen einsehen Mentor/Organisator

### Ablauf
- Mentor/Organisator klickt in der Navbar auf den Button "Studentenakte"
- Mentor/Organisator klickt auf den Link "Vereinbarungen" und wird zur Vereinbarungeübersicht weitergeleitet
- Mentor/Organisator wählt in der Dropdown-Liste einen Studenten aus und drückt den "Student auswählen" Button
- Es werden alle Vereinbarungen des Studenten angezeigt

<br><br>
## UC5.007: Vereinbarungen einsehen Student

### Ablauf
- Student klickt in der Navbar auf den Button "Meine Akte"
- Student klickt auf den Link "Vereinbarungen" und wird zur Vereinbarungenübersicht weitergeleitet
- Es werden alle Vereinbarungen des Studenten angezeigt



<br><br>
# AUAS Schnittstelle

### Ziel
Das Auas System kann dem Mentoring System einen Studenten vorschlagen

### Ablauf
- AUAS benutzt Mentoring System Schnittstelle und übergibt so dem Mentoring System einen Studenten
- Benutzung der AUAS Schnittstelle: Auas ruft den Link /AuasSchnittstelle/<StudentVorname>/<StudentNachname> und übergibt dort den Namen des Studenten in dem es den Vor- und Nachnamen durch ein "/" getrennt in den Link einfügt (Bsp. /AuasSchnittstelle/Alex/Afrika)
- Dieser Name wird dann als Vorschlag mittels einer automatisch generierten Nachricht an alle Organisatoren geschickt
