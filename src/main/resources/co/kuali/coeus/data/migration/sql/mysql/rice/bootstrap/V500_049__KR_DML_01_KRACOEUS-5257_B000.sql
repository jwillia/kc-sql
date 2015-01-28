DELIMITER /
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND)
VALUES ((SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S), UUID(), 1,
(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM = 'Add Address Book' AND NMSPC_CD='KC-UNT'),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Modify Address Book' AND NMSPC_CD='KC-UNT'), 'Y')
/
DELIMITER ;
