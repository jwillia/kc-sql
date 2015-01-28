DELIMITER /
UPDATE KRIM_PERM_ATTR_DATA_T SET ATTR_VAL = 'SubAwardInvoiceMaintenanceDocument'
WHERE PERM_ID = (SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Create Subaward Invoice' AND NMSPC_CD='KC-SUBAWARD')
AND KIM_TYP_ID = (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KR-SYS' AND NM = 'Document Type (Permission)')
AND KIM_ATTR_DEFN_ID = (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NMSPC_CD = 'KR-WKFLW' AND NM = 'documentTypeName')
/

UPDATE KRIM_PERM_ATTR_DATA_T SET ATTR_VAL = 'SubAwardInvoiceMaintenanceDocument'
WHERE PERM_ID = (SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Modify Subaward Invoice' AND NMSPC_CD='KC-SUBAWARD')
AND KIM_TYP_ID = (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KR-SYS' AND NM = 'Document Type (Permission)')
AND KIM_ATTR_DEFN_ID = (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NMSPC_CD = 'KR-WKFLW' AND NM = 'documentTypeName')
/

UPDATE KRIM_PERM_ATTR_DATA_T SET ATTR_VAL = 'SubAwardInvoiceMaintenanceDocument'
WHERE PERM_ID = (SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'View Subaward Invoice' AND NMSPC_CD='KC-SUBAWARD')
AND KIM_TYP_ID = (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KR-SYS' AND NM = 'Document Type (Permission)')
AND KIM_ATTR_DEFN_ID = (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NMSPC_CD = 'KR-WKFLW' AND NM = 'documentTypeName')
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND) VALUES
((SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S),UUID(), 1,
(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM = 'View Subaward' AND NMSPC_CD='KC-SUBAWARD'),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'View Subaward Invoice' AND NMSPC_CD='KC-SUBAWARD'), 'Y')
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/

INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND) VALUES
((SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S),UUID(), 1,
(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM = 'Requisitioner' AND NMSPC_CD='KC-SUBAWARD'),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'View Subaward Invoice' AND NMSPC_CD='KC-SUBAWARD'), 'Y')
/
DELIMITER ;
