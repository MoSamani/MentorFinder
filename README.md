# MentorFinder – Mentoring-Plattform für Studierende

MentorFinder ist eine Webanwendung zur Vermittlung von Mentorinnen und Mentoren an Studierende. Sie wurde mit Spring, Gradle und Docker entwickelt.

---

## Setup und Verwendung

| Aufgabe                         | Befehl                                            | Arbeitsverzeichnis |
| ------------------------------- | ------------------------------------------------- | ------------------ |
| Datenbank und Dokumente löschen | `sudo rm -rf database`<br>`sudo rm -rf documents` | `./`               |
| Aktuelles .jar erstellen        | `gradle build -x test`                            | `/webapp/app`      |
| Docker-Image erstellen          | `docker build . -t webapp`                        | `/webapp`          |
| Anwendung starten               | `docker-compose up`                               | `./`               |

Die Datenbankverbindung wird über die Datei `.env` konfiguriert.

### Standardzugang

| Benutzername | Passwort |
| ------------ | -------- |
| admin        | admin    |

**Hinweis:** Wenn der Ordner `database` noch nicht vorhanden ist, muss `docker-compose up` zweimal ausgeführt werden. Der Grund ist, dass der Webapp-Service bereits startet, bevor die Datenbank vollständig initialisiert wurde. Erst beim zweiten Start ist der Zugriff auf die Datenbank erfolgreich. Die Anwendung ist danach unter `localhost` erreichbar.

---

## Wichtige Architekturentscheidungen

- Keine E-Mail-Kommunikation: stattdessen ein eigener Nachrichtendienst auf Datenbankbasis.
- Die Beziehung zwischen Studierenden und Mentor:innen wird durch die Entität `Assignment` abgebildet.
- Alle Benutzertypen (Studierende, Mentor:innen, Organisator:innen) werden in einer gemeinsamen Tabelle `user` gespeichert, differenziert über das Attribut `role`.
- Authentifizierung erfolgt über Spring Security.
- Die Benutzeroberfläche ist rollenbasiert und passt sich dem jeweiligen Benutzertyp an.
- Ein Systembenutzer dient zur Versendung von Erinnerungsnachrichten.
- Erinnerungen werden bei Mentorenwechsel, Fallabschluss und Terminvorschlägen generiert.
- Erinnerungsnachrichten werden nicht in der Datenbank gespeichert, sondern dynamisch in der Anwendung erzeugt.
- Mentor:innen können Fälle abgeben oder beenden. Die Begründung kann im Antrag angegeben werden.
- Die E-Mail-Adresse dient zur Identifikation von Nutzer:innen.
- Das Frontend verwendet Bootstrap für das Styling.
- Die maximale Dateigröße für Uploads beträgt 10 MB.
- Hochgeladene Dateien werden nicht in der Datenbank, sondern im Containerverzeichnis `documents` gespeichert.
- Termine, Nachrichten, Dokumente, Vereinbarungen und Notizen können von Nutzer:innen nicht gelöscht werden.
- Die Navigationsleiste dient gleichzeitig als Dashboard.

---

## User Stories

Die vollständigen User Stories sind unter folgendem Link verfügbar:  
[User Stories](https://github.com/hhu-propra2-2017/abschlussprojekt-bits-please/blob/develop/Architektur/UserStories.md)

---

## Qualitätssicherung

Informationen zum Qualitätssicherungsprozess befinden sich hier:  
[Qualitätssicherung](https://github.com/hhu-propra2-2017/abschlussprojekt-bits-please/blob/develop/Architektur/Qualit%C3%A4t-Sicherung/Qualit%C3%A4tssicherung.md)

---

## Domainmodell

Referenz:  
[Domainmodell (PDF)](https://github.com/hhu-propra2-2017/abschlussprojekt-bits-please/blob/develop/Architektur/Modelle/Domain%20Model/DomainModel.pdf)

### Domänenübersicht

- **Core Domain**
  - Vermittlung
- **Subdomains**
  - Authentifizierung
  - Akte
  - Mitteilung
  - Terminvereinbarung

Jede Domäne übernimmt einen klar abgegrenzten Aufgabenbereich. Die Verbindung und Kommunikation zwischen Domänen ist strukturiert beschrieben.

---

## Softwarearchitektur

Referenz:  
[Architekturdiagramm (PDF)](https://github.com/hhu-propra2-2017/abschlussprojekt-bits-please/blob/develop/Architektur/Modelle/Architektur/Architektur.pdf)

Die Anwendung folgt dem Model-View-Controller-Prinzip. Das Datenmodell ist schichtenbasiert aufgebaut:

- **Services**: Kapseln die Geschäftslogik und kommunizieren mit den Repositories.
- **Controller**: Zuständig für Routing und Verarbeitung von Anfragen.
- **Views**: HTML-Templates, strukturiert nach Rollen (Student, Mentor, Organisator).
- **Tests**: Unterteilt in Controller-Tests und Datenbanktests.

---

## ER-Modell

Referenz:  
[ER-Modell (PDF)](https://github.com/hhu-propra2-2017/abschlussprojekt-bits-please/blob/develop/Architektur/Modelle/ER-Model/ER-Model.pdf)

Die Datenbankstruktur ist in vier Abschnitte gegliedert:

1. **Benutzer und Beziehungen**  
   Speicherung aller Nutzer:innen und ihrer Zuweisungen über `Assignment`.

2. **Nachrichten**  
   Speicherung von Kommunikation (inkl. Erinnerungen) über die Entität `Message`.

3. **Akte**  
   Speicherung fallbezogener Daten: Vereinbarungen, Dokumente, Notizen.

4. **Termine**  
   Verwaltung von Vorschlägen, Bestätigungen, Absagen und Gesprächsprotokollen.

---

Für weitere Details siehe die Dokumentation in den jeweiligen Unterordnern im Verzeichnis `Architektur/`.
