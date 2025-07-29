## Phase 3
**Start**: 26.02.2018

**Ziel**: Zuweisung Student-Mentor implementieren

#### Oberfläche:
- Seite für Nachricht senden (benutzt Assignment für Student und Mentor, sodass der Student nur seinem Mentor schreiben kann und der Mentor allen seinen Studenten (+Organisatoren))

- Seite für Mentor um Fallabschlussantrag für einen bestimmten, auswählbaren Studenten einzureichen (Textfeld Grund, Button absenden)
- Seite für Studenten um Mentorenwechselantrag einzureichen (Textfeld Grund, Button absenden)
- Seite für Organisator, die anzeigt welche Studenten keinen Mentor haben -> kann von dort aus einen Mentor zuweisen
- Seite für Organisator, die alle Assignments (Fälle anzeigt)
- Seite für Organisator mit Liste von Wechselanträgen (von Student) + Grund -> Antrag ablehnen oder bestätigen
- Seite für Organisator mit Liste von Fallabschlussanträgen (von Mentor) + Grund -> Antrag ablehnen oder bestätigen


#### Datenbank
- Repository für Assignment
- Wechselantrag und Fallabschlussantrag implementieren


#### Vermittlung Schnittstelle
- getAllAssignments()
- getAllStudentsWithoutMentor()
- assignMentorToStudent(User mentor, User student)
- *getAssignmentOfStudent(User student)* (braucht man evtl. nicht)
- *getAllStudentOfMentor(User mentor)* (braucht man evtl. nicht)

#### Antrag Schnittstelle
- Anträge ablehnen (für beide Typen von Anträgen)
- Anträge bestätigen (für beide Typen von Anträgen)


#### Tests
- Tests für AssignmentServices
- Tests Controller
