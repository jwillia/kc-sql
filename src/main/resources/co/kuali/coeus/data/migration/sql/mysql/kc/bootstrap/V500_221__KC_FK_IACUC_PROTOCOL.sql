DELIMITER /
ALTER TABLE IACUC_PROTOCOL 
ADD CONSTRAINT FK_IACUC_PROTOCOL_STATUS 
FOREIGN KEY (PROTOCOL_STATUS_CODE) 
REFERENCES IACUC_PROTOCOL_STATUS (PROTOCOL_STATUS_CODE)
/

ALTER TABLE IACUC_PROTOCOL 
ADD CONSTRAINT FK_IACUC_PROTOCOL_PROJECT_TYPE 
FOREIGN KEY (PROJECT_TYPE_CODE) 
REFERENCES IACUC_PROTOCOL_PROJECT_TYPE (PROJECT_TYPE_CODE)
/

ALTER TABLE IACUC_PROTOCOL 
ADD CONSTRAINT FK_IACUC_PROTOCOL_TYPE 
FOREIGN KEY (PROTOCOL_TYPE_CODE) 
REFERENCES IACUC_PROTOCOL_TYPE (PROTOCOL_TYPE_CODE)
/

DELIMITER ;
