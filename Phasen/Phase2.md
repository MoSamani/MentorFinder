## Phase 2
**Start**: 22.02.2018

**Ziel**: Nachrichten implementieren

#### ER-Modell
- Attribut "Titel" der Entity Nachrichten hinzufügen

#### Oberfläche:
- Posteingang (Liste aller empfangenen Nachrichten, zeigt nur Titel an)
  &rarr; Seite für Nachricht Detaildarstellung
- Postausgang (Liste aller gesendeten Nachrichten, zeigt nur Titel an)
  &rarr; Seite für Nachricht Detaildarstellung
- Nachricht senden


#### Datenbank
- Funktion für UserRepository: `findOneByMailAddress(String mailAddress)`
- Nachrichten Entity implementieren


#### Messenger Schnittstelle
- `send(User sender, User receiver)`
- `getAllRecievedMessages(User user)`
- `getAllSentMessages(User user)`
  
  &rarr; Messenger implementieren (benutze MessageRepository)


#### Tests
- Tests für Nachrichten
- Tests für MessageServices
- Aktualisierung der Tests für die Controller
