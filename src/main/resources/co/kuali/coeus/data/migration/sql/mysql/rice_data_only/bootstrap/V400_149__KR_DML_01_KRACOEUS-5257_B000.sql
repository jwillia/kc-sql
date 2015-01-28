DELIMITER /
INSERT INTO KRIM_ROLE_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_T(ACTV_IND,DESC_TXT,KIM_TYP_ID,LAST_UPDT_DT,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR)
VALUES('Y','Add Address Book',(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'UnitHierarchy'),NOW(),'KC-UNT',UUID(),(SELECT (MAX(ID)) FROM KRIM_ROLE_ID_S),'Add Address Book',1)
/
INSERT INTO KRIM_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_PERM_T(PERM_ID,PERM_TMPL_ID,NMSPC_CD,NM,DESC_TXT,ACTV_IND,OBJ_ID)
VALUES((SELECT (MAX(ID)) FROM KRIM_PERM_ID_S),
(SELECT PERM_TMPL_ID FROM KRIM_PERM_TMPL_T WHERE NMSPC_CD ='KR-SYS' AND NM ='Initiate Document'),
'KC-UNT','Add Address Book','Add Address Book','Y',UUID())
/
INSERT INTO KRIM_ATTR_DATA_ID_S VALUES(NULL)
/
INSERT INTO KRIM_PERM_ATTR_DATA_T(ATTR_DATA_ID,PERM_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_VAL,OBJ_ID)
VALUES((SELECT (MAX(ID)) FROM KRIM_ATTR_DATA_ID_S),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Add Address Book' AND NMSPC_CD='KC-UNT'),
(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KR-SYS' AND NM = 'Document Type (Permission)'),
(SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NMSPC_CD = 'KR-WKFLW' AND NM = 'documentTypeName'),
'RolodexMaintenanceDocument', UUID())
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND) VALUES
((SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S),UUID(), 1,
(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM = 'Add Address Book' AND NMSPC_CD='KC-UNT'),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Add Address Book' AND NMSPC_CD='KC-UNT'), 'Y')
/
INSERT INTO KRIM_ROLE_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_T(ACTV_IND,DESC_TXT,KIM_TYP_ID,LAST_UPDT_DT,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR)
VALUES('Y','Address Book Maintainer',(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'UnitHierarchy'),NOW(),'KC-UNT',UUID(),(SELECT (MAX(ID)) FROM KRIM_ROLE_ID_S),'Address Book Maintainer',1)
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND) VALUES
((SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S),UUID(), 1,
(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM = 'Address Book Maintainer' AND NMSPC_CD='KC-UNT'),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Add Address Book' AND NMSPC_CD='KC-UNT'), 'Y')
/
INSERT INTO KRIM_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_PERM_T(PERM_ID,PERM_TMPL_ID,NMSPC_CD,NM,DESC_TXT,ACTV_IND,OBJ_ID)
VALUES((SELECT (MAX(ID)) FROM KRIM_PERM_ID_S),(SELECT PERM_TMPL_ID FROM KRIM_PERM_TMPL_T WHERE NMSPC_CD = 'KR-NS' AND NM = 'Edit Document'),
'KC-UNT','Modify Address Book','Modify Address Book','Y',UUID())
/
INSERT INTO KRIM_ATTR_DATA_ID_S VALUES(NULL)
/
INSERT INTO KRIM_PERM_ATTR_DATA_T(ATTR_DATA_ID,PERM_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_VAL,OBJ_ID)
VALUES((SELECT (MAX(ID)) FROM KRIM_ATTR_DATA_ID_S),(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Modify Address Book'
AND NMSPC_CD='KC-UNT'),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KR-SYS' AND NM = 'Document Type (Permission)'),
(SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NMSPC_CD = 'KR-WKFLW' AND NM = 'documentTypeName'),
'RolodexMaintenanceDocument', UUID())
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND)
VALUES ((SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S), UUID(), 1,
(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM = 'Address Book Maintainer' AND NMSPC_CD='KC-UNT'),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Modify Address Book' AND NMSPC_CD='KC-UNT'), 'Y')
/
INSERT INTO KRIM_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_PERM_T(PERM_ID,PERM_TMPL_ID,NMSPC_CD,NM,DESC_TXT,ACTV_IND,OBJ_ID)
VALUES((SELECT (MAX(ID)) FROM KRIM_PERM_ID_S),
(SELECT PERM_TMPL_ID FROM KRIM_PERM_TMPL_T WHERE NM ='Default'),
'KC-UNT','Delete Address Book','Delete Address Book','Y',UUID())
/
INSERT INTO KRIM_ATTR_DATA_ID_S VALUES(NULL)
/
INSERT INTO KRIM_PERM_ATTR_DATA_T(ATTR_DATA_ID,PERM_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_VAL,OBJ_ID)
VALUES((SELECT (MAX(ID)) FROM KRIM_ATTR_DATA_ID_S),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Delete Address Book' AND NMSPC_CD='KC-UNT'),
(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KR-SYS' AND NM = 'Document Type (Permission)'),
(SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NMSPC_CD = 'KR-WKFLW' AND NM = 'documentTypeName'),
'RolodexMaintenanceDocument', UUID())
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND) VALUES
((SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S),UUID(), 1,
(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM = 'Address Book Maintainer' AND NMSPC_CD='KC-UNT'),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Delete Address Book' AND NMSPC_CD='KC-UNT'), 'Y')
/
INSERT INTO KRIM_ROLE_ID_S VALUES(NULL)
/
-- for organization
INSERT INTO KRIM_ROLE_T(ACTV_IND,DESC_TXT,KIM_TYP_ID,LAST_UPDT_DT,NMSPC_CD,OBJ_ID,ROLE_ID,ROLE_NM,VER_NBR)
VALUES('Y','Modify Organizations',(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'UnitHierarchy'),NOW(),'KC-UNT',UUID(),(SELECT (MAX(ID)) FROM KRIM_ROLE_ID_S),'Modify Organizations',1)
/
INSERT INTO KRIM_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_PERM_T(PERM_ID,PERM_TMPL_ID,NMSPC_CD,NM,DESC_TXT,ACTV_IND,OBJ_ID)
VALUES((SELECT (MAX(ID)) FROM KRIM_PERM_ID_S),(SELECT PERM_TMPL_ID FROM KRIM_PERM_TMPL_T WHERE NMSPC_CD = 'KR-NS' AND NM = 'Edit Document'),
'KC-UNT','Modify Organization','Modify Organization','Y',UUID())
/
INSERT INTO KRIM_ATTR_DATA_ID_S VALUES(NULL)
/
INSERT INTO KRIM_PERM_ATTR_DATA_T(ATTR_DATA_ID,PERM_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_VAL,OBJ_ID)
VALUES((SELECT (MAX(ID)) FROM KRIM_ATTR_DATA_ID_S),(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Modify Organization'
AND NMSPC_CD='KC-UNT'),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KR-SYS' AND NM = 'Document Type (Permission)'),
(SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NMSPC_CD = 'KR-WKFLW' AND NM = 'documentTypeName'),
'OrganizationMaintenanceDocument', UUID())
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND)
VALUES ((SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S), UUID(), 1,
(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM = 'Modify Organizations' AND NMSPC_CD='KC-UNT'),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Modify Organization' AND NMSPC_CD='KC-UNT'), 'Y')
/
INSERT INTO KRIM_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_PERM_T(PERM_ID,PERM_TMPL_ID,NMSPC_CD,NM,DESC_TXT,ACTV_IND,OBJ_ID)
VALUES((SELECT (MAX(ID)) FROM KRIM_PERM_ID_S),
(SELECT PERM_TMPL_ID FROM KRIM_PERM_TMPL_T WHERE NMSPC_CD ='KR-SYS' AND NM ='Initiate Document'),
'KC-UNT','Add Organization','Add Organization','Y',UUID())
/
INSERT INTO KRIM_ATTR_DATA_ID_S VALUES(NULL)
/
INSERT INTO KRIM_PERM_ATTR_DATA_T(ATTR_DATA_ID,PERM_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_VAL,OBJ_ID)
VALUES((SELECT (MAX(ID)) FROM KRIM_ATTR_DATA_ID_S),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Add Organization' AND NMSPC_CD='KC-UNT'),
(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KR-SYS' AND NM = 'Document Type (Permission)'),
(SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NMSPC_CD = 'KR-WKFLW' AND NM = 'documentTypeName'),
'OrganizationMaintenanceDocument', UUID())
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND) VALUES
((SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S),UUID(), 1,
(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM = 'Modify Organizations' AND NMSPC_CD='KC-UNT'),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Add Organization' AND NMSPC_CD='KC-UNT'), 'Y')
/
INSERT INTO KRIM_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_PERM_T(PERM_ID,PERM_TMPL_ID,NMSPC_CD,NM,DESC_TXT,ACTV_IND,OBJ_ID)
VALUES((SELECT (MAX(ID)) FROM KRIM_PERM_ID_S),
(SELECT PERM_TMPL_ID FROM KRIM_PERM_TMPL_T WHERE NM ='Default'),
'KC-UNT','Delete Organization','Delete Organization','Y',UUID())
/
INSERT INTO KRIM_ATTR_DATA_ID_S VALUES(NULL)
/
INSERT INTO KRIM_PERM_ATTR_DATA_T(ATTR_DATA_ID,PERM_ID,KIM_TYP_ID,KIM_ATTR_DEFN_ID,ATTR_VAL,OBJ_ID)
VALUES((SELECT (MAX(ID)) FROM KRIM_ATTR_DATA_ID_S),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Delete Organization' AND NMSPC_CD='KC-UNT'),
(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KR-SYS' AND NM = 'Document Type (Permission)'),
(SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NMSPC_CD = 'KR-WKFLW' AND NM = 'documentTypeName'),
'OrganizationMaintenanceDocument', UUID())
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND) VALUES
((SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S),UUID(), 1,
(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM = 'Modify Organizations' AND NMSPC_CD='KC-UNT'),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Delete Organization' AND NMSPC_CD='KC-UNT'), 'Y')
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND) VALUES
((SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S),UUID(), 1,
(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM = 'Modify Organizations' AND NMSPC_CD='KC-UNT'),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Add Address Book' AND NMSPC_CD='KC-UNT'), 'Y')
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND) VALUES
((SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S),UUID(), 1,
(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM = 'Modify Organizations' AND NMSPC_CD='KC-UNT'),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Modify Address Book' AND NMSPC_CD='KC-UNT'), 'Y')
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/
INSERT INTO KRIM_ROLE_PERM_T (ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND) VALUES
((SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S),UUID(), 1,
(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM = 'Modify Organizations' AND NMSPC_CD='KC-UNT'),
(SELECT PERM_ID FROM KRIM_PERM_T WHERE NM = 'Delete Address Book' AND NMSPC_CD='KC-UNT'), 'Y')
/
DELIMITER ;
