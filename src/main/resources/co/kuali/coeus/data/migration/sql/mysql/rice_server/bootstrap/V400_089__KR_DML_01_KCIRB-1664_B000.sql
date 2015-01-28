DELIMITER /
INSERT INTO KRIM_ROLE_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND, LAST_UPDT_DT)
VALUES ((SELECT (MAX(ID)) FROM KRIM_ROLE_ID_S), UUID(), 1, 'Committee Administrator', 'KC-COMMITTEE', 'Committee Administrator', (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM='Unit'), 'Y', NOW())
/
INSERT INTO KRIM_ROLE_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND, LAST_UPDT_DT)
VALUES ((SELECT (MAX(ID)) FROM KRIM_ROLE_ID_S), UUID(), 1, 'Active Committee Member On Scheduled Date','KC-COMMITTEE','Role members are derived from the active committee members on a particular schedule date.',(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM='Derived Role: Active Committee Member on Scheduled Date'),'Y',NOW())
/
DELIMITER ;
