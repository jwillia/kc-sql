delimiter /
TRUNCATE TABLE AWARD_TRANSACTION_TYPE
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (1,'Administrative Amendment','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (2,'Allotment (Increment)','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (3,'Continuation (Amendment)','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (4,'Correction','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (5,'Date Change','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (6,'Deobligation','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (7,'F&A Rate Change','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (8,'Investigator Change','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (9,'New','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (10,'No Cost Extension','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (11,'Restriction Change','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (12,'Subaward Change','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (13,'Supplement','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (14,'Suspension (Stop Work Order)','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (15,'Unit Change','Y','admin',NOW(),UUID(),1)
/
INSERT INTO AWARD_TRANSACTION_TYPE (AWARD_TRANSACTION_TYPE_CODE,DESCRIPTION,SHOW_IN_ACTION_SUMMARY,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
    VALUES (16,'Termination','Y','admin',NOW(),UUID(),1)
/
delimiter ;
