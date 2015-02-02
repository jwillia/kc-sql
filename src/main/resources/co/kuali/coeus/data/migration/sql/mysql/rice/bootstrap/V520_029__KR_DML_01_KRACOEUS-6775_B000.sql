--
-- Kuali Coeus, a comprehensive research administration system for higher education.
-- 
-- Copyright 2005-2015 Kuali, Inc.
-- 
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Affero General Public License as
-- published by the Free Software Foundation, either version 3 of the
-- License, or (at your option) any later version.
-- 
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU Affero General Public License for more details.
-- 
-- You should have received a copy of the GNU Affero General Public License
-- along with this program.  If not, see <http://www.gnu.org/licenses/>.
--

DELIMITER /
INSERT INTO KRIM_GRP_ID_S VALUES(NULL)
/
INSERT INTO KRIM_GRP_T ( GRP_ID, OBJ_ID, VER_NBR, GRP_NM, NMSPC_CD, GRP_DESC, KIM_TYP_ID, ACTV_IND, LAST_UPDT_DT )
VALUES (CONCAT ('KC', (SELECT (MAX(ID)) FROM KRIM_GRP_ID_S)),UUID(),'1', 'AwardAdmin', 'KC-WKFLW', 'Award Administrator',(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM = 'Default' AND NMSPC_CD='KUALI'), 'Y', NOW())
/
INSERT INTO KRIM_ROLE_MBR_ID_S VALUES(NULL)
/

INSERT INTO KRIM_ROLE_MBR_T (ROLE_MBR_ID,VER_NBR,OBJ_ID,ROLE_ID, MBR_ID,MBR_TYP_CD, ACTV_FRM_DT, ACTV_TO_DT,LAST_UPDT_DT)
VALUES (CONCAT ('KC',(SELECT (MAX(ID)) FROM KRIM_ROLE_MBR_ID_S)), '1', UUID(), (SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM='Award Modifier' AND NMSPC_CD='KC-AWARD'),(SELECT GRP_ID from KRIM_GRP_T WHERE GRP_NM='AwardAdmin' AND NMSPC_CD='KC-WKFLW'), 'G', NULL,NULL, NOW())
/
INSERT INTO KRIM_ATTR_DATA_ID_S VALUES(NULL)
/

INSERT INTO KRIM_ROLE_MBR_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, ROLE_MBR_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ( CONCAT ('KC',(SELECT (MAX(ID)) FROM KRIM_ATTR_DATA_ID_S)), UUID(), 1,(SELECT ROLE_MBR_ID FROM KRIM_ROLE_MBR_T WHERE ROLE_ID =(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM='Award Modifier' AND NMSPC_CD='KC-AWARD') AND MBR_ID =(SELECT GRP_ID FROM KRIM_GRP_T WHERE GRP_NM='AwardAdmin' AND NMSPC_CD='KC-WKFLW')), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM='UnitHierarchy' AND NMSPC_CD='KC-SYS'), (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM='unitNumber' AND NMSPC_CD='KC-SYS'),'000001')
/
INSERT INTO KRIM_ATTR_DATA_ID_S VALUES(NULL)
/

INSERT INTO KRIM_ROLE_MBR_ATTR_DATA_T (ATTR_DATA_ID, OBJ_ID, VER_NBR, ROLE_MBR_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL) VALUES ( CONCAT ('KC',(SELECT (MAX(ID)) FROM KRIM_ATTR_DATA_ID_S)), UUID(), 1,(SELECT ROLE_MBR_ID FROM KRIM_ROLE_MBR_T WHERE ROLE_ID =(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM='Award Modifier' AND NMSPC_CD='KC-AWARD') AND MBR_ID =(SELECT GRP_ID FROM KRIM_GRP_T WHERE GRP_NM='AwardAdmin' AND NMSPC_CD='KC-WKFLW')), (SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM='UnitHierarchy' AND NMSPC_CD='KC-SYS'), (SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM='subunits' AND NMSPC_CD='KC-SYS'),'Y')
/
INSERT INTO KRIM_PERM_ID_S VALUES(NULL)
/

INSERT INTO KRIM_PERM_T (PERM_ID, OBJ_ID,VER_NBR, PERM_TMPL_ID, NMSPC_CD, NM, DESC_TXT, ACTV_IND)
VALUES (CONCAT ('KC',(SELECT (MAX(ID)) FROM KRIM_PERM_ID_S)), UUID(),'1', (select PERM_TMPL_ID from KRIM_PERM_TMPL_T where NM='Route Document' AND NMSPC_CD='KR-WKFLW'), 'KC-AWARD', 'Submit Award', 'Submit Award Document', 'Y')
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/

INSERT INTO KRIM_ROLE_PERM_T ( ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND)
VALUES (CONCAT ('KC', (SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S)), UUID(), 1,(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM='Award Modifier' AND NMSPC_CD='KC-AWARD'), (SELECT PERM_ID FROM KRIM_PERM_T WHERE NM='Submit Award' AND NMSPC_CD='KC-AWARD'), 'Y')
/
INSERT INTO KRIM_ATTR_DATA_ID_S VALUES(NULL)
/

INSERT INTO KRIM_PERM_ATTR_DATA_T ( ATTR_DATA_ID, OBJ_ID, VER_NBR, PERM_ID, KIM_TYP_ID, KIM_ATTR_DEFN_ID, ATTR_VAL)
VALUES (CONCAT ('KC',(SELECT (MAX(ID)) FROM KRIM_ATTR_DATA_ID_S)),UUID(),'1', (SELECT PERM_ID FROM KRIM_PERM_T WHERE NM='Submit Award' AND NMSPC_CD='KC-AWARD') ,(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NM='Document Type (Permission)' AND NMSPC_CD='KR-SYS'),(SELECT KIM_ATTR_DEFN_ID FROM KRIM_ATTR_DEFN_T WHERE NM='documentTypeName' AND NMSPC_CD='KR-WKFLW'), 'AwardDocument')
/

UPDATE KRIM_PERM_T SET ACTV_IND='Y' WHERE NM='Blanket Approve AwardDocument' AND NMSPC_CD='KC-AWARD'
/
INSERT INTO KRIM_ROLE_PERM_ID_S VALUES(NULL)
/

INSERT INTO KRIM_ROLE_PERM_T ( ROLE_PERM_ID, OBJ_ID, VER_NBR, ROLE_ID, PERM_ID, ACTV_IND)
VALUES (CONCAT ('KC', (SELECT (MAX(ID)) FROM KRIM_ROLE_PERM_ID_S)), UUID(), 1,(SELECT ROLE_ID FROM KRIM_ROLE_T WHERE ROLE_NM='Award Modifier' AND NMSPC_CD='KC-AWARD'), (SELECT PERM_ID FROM KRIM_PERM_T WHERE NM='Blanket Approve AwardDocument' AND NMSPC_CD='KC-AWARD'), 'Y')
/

DELIMITER ;
