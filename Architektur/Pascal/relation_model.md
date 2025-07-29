Relationenschema

User(**UserID**, Prename, Surname, mailAdress, Password, Role)

Assignment(**AsgID**, **_Student_**, **_Mentor_**)

Message(**MsgID**, **_Sender_**, **_Receiver_**, Datum, Title, Content)

Appointment(**AppointmentID**, **_AsgID_**, **_ConversationLogID_**, Date, isCanceled)

ConversationLog(**ConversationLogID**, **_Termin_**, Text)

Note(**NoteID**, **_FileID_**, Title, Content, Date)

Agreement(**AgreementID**, **_FileID_**, Goal, DateBeginning, DateEnd)

Document(**DocumentID**, **_FileID_**,Title, Data)

File(**FileID**,**_Student_**, **_Notes_**, **_Documents_**, **_Agreements_**)

AppointmentProposal(**AppointmentProposalID**,  **_AsgID_**, Date)

ChangeRequest(**ChangeRequestID**, **_AsgID_**, **_Student_**, Reason)

ClosingRequest(**ClosingRequestID**, **_AsgID_**, **_Mentor_**, Reason)

