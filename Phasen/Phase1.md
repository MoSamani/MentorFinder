## Phase 1
**Start**: 21.02.2018

**Ziel**: Registrierung und Anmeldung

#### Vorbereitung
- .odt Dateien aufräumen
- IDE einrichten (FindBugs, EclEmma, ...), Lombok installieren
- GitFlow, GitKraken
- Basic Projekt Einrichtung (übernehme starter von columbbus)

#### Oberfläche:
- Thymeleaf Template erstellen
- Controller schreiben
- Startseite: Links zu verschiedenen Anmeldungs und Registrierungsseiten
- Registrierung (jeweils eine Seite für jede Rolle), Anmeldung

#### Datenbank
- User in DB, Übersetzung zu Student, Mentor, Organisator

#### Tests
- Unit-Tests :  Die Logik bei der vorgesehen Funktionen wird in RegistryController getestet.
- Unit-Tests :  Die relevante Funktionen zu der Datenbak und das Objekt User, wie `save()`, `find()` , `delete()` werden in Controller getestet.
- Intgration-Test : Die Interaktion zwischen Templates und Controllres wird getestet.
