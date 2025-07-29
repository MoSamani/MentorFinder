## Phase 5
**Start**: 02.03.2018

**Ziel**: Studentenakte hinzufügen


##### Dokumenten Upload evtl. in anderer Phase 

#### Oberfläche:
- Seite für Mentor: Aktenübersicht (Notizen, Dokumente, Vereinbarungen) -> Links zu Seiten
  - Seite Notizen (für Mentor): Ansehen und Hinzufügen/Bearbeiten
  - Seite Dokumente (für Mentor): Ansehen und Hinzufügen/Bearbeiten
  - Seite Vereinbarungen (für Mentor): Ansehen und Hinzufügen/Bearbeiten

- Setie für Student: Aktenübersicht (Dokumente, Vereinbarungen)
  - Seite Dokumente (für Student): Ansehen
  - Seite Vereinbarungen (für Student): Ansehen

- Aktenübersicht für Organisator gleich wie Mentorenseite, kann aber jeden Studenten auswählen

- Terminübersichtsseite Mentor/Student: Gesprächsprotokoll ansehen (für Mentor: hinzufügen)


#### Datenbank:
- Akte, Dokumente, Notizen, Vereinbarungen, Gesprächsprotokolle
- ER-Modell überarbeiten:
  - Gesprächsprotokoll keine Verbindung zu Akte, sondern zu Termin 
  - Akte speichert den Studenten


#### FileService
- Notizen, Dokumente, Vereinbarungen: anzeigen + hinzufügen


#### AppointmentService
- Gesprächsprotokoll anzeigen + hinzufügen


#### Tests:
- ControllerStudent (Phase 4)
- ControllerOrganizer (Zuweisung)
