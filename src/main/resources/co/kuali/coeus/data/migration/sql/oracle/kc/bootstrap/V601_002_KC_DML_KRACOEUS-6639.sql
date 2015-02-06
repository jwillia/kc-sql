INSERT INTO PERSON_EDITABLE_FIELDS (PERSON_EDITABLE_FIELD_ID,MODULE_CODE,FIELD_NAME,ACTIVE_FLAG,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR)
VALUES (SEQ_PERSON_EDITABLE_FIELD.NEXTVAL,(SELECT MODULE_CODE FROM COEUS_MODULE WHERE DESCRIPTION = 'Development Proposal'),'calendarYearEffort','Y','admin',SYSDATE,SYS_GUID(),1)
/
INSERT INTO PERSON_EDITABLE_FIELDS (PERSON_EDITABLE_FIELD_ID,MODULE_CODE,FIELD_NAME,ACTIVE_FLAG,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR)
VALUES (SEQ_PERSON_EDITABLE_FIELD.NEXTVAL,(SELECT MODULE_CODE FROM COEUS_MODULE WHERE DESCRIPTION = 'Development Proposal'),'summerEffort','Y','admin',SYSDATE,SYS_GUID(),1)
/
INSERT INTO PERSON_EDITABLE_FIELDS (PERSON_EDITABLE_FIELD_ID,MODULE_CODE,FIELD_NAME,ACTIVE_FLAG,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR)
VALUES (SEQ_PERSON_EDITABLE_FIELD.NEXTVAL,(SELECT MODULE_CODE FROM COEUS_MODULE WHERE DESCRIPTION = 'Development Proposal'),'academicYearEffort','Y','admin',SYSDATE,SYS_GUID(),1)
/
