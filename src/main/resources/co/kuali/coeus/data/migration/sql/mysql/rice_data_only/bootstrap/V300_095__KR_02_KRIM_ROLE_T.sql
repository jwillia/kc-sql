delimiter /
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KUALI' AND NM = 'Default'),'KC-SYS','Manager','This role represents a collection of all the KC module manager roles and has permission to initiate simple maintenance documents.','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KUALI' AND NM = 'Default'),'KC-WKFLW','IRBApprover','IRB Approver','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KUALI' AND NM = 'Default'),'KC-WKFLW','OSPApprover','Office of Sponsored Projects Approver','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'Unit'),'KC-AB','Award Budget Viewer','Award Budget Viewer - the role grants permissions to view award budget at departmental level','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'Unit'),'KC-AB','Award Budget Modifier','Award Budget Modifier - the role grants permissions to modify or view award budget at departmental level','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'Unit'),'KC-PD','approver','approver','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'Unit'),'KC-PROTOCOL','Protocol Viewer','Protocol Viewer','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'Unit'),'KC-PROTOCOL','Protocol Aggregator','Protocol Aggregator','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'Unit'),'KC-PD','unassigned','Unassigned role - no permissions','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'Unit'),'KC-PD','Viewer','Proposal Viewer','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'Unit'),'KC-PD','Budget Creator','Proposal Budget Creator','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'Unit'),'KC-PD','Narrative Writer','Proposal Narrative Writer','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'Unit'),'KC-PD','Aggregator','Proposal Aggregator','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'Unit'),'KC-UNT','Proposal Creator','Proposal Creator','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-AB','Award Budget Approver','Award Budget Approver - the role grants permissions to edit and approve award budget','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-AB','Award Budget Aggregator','Award Budget Aggregator - the role grants permissions to create and maintain award budget at department level','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-AB','Award Budget Maintainer','Maintain Award Budget - the role grants permissions to modify and submit award budget at departmental level','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-AB','Award Budget Administrator','Award Budget Administrator - the role grants permissions to manage any award budget at OSP level','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-IP','Intellectual Property Review Maintainer','Maintain Intellectual Property Review','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-IP','Institutional Proposal Maintainer','Maintain Institutional Proposals','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-IP','Institutional Proposal Viewer','View Institutional Proposals','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-AWARD','Award Unassigned','Award Unassigned','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-AWARD','Template Viewer','Template Viewer','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-AWARD','Departments Awards Viewer','Departments Awards Viewer','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-AWARD','Award Documents Viewer','Award Documents Viewer','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-AWARD','Award Viewer','Award Viewer','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-AWARD','Award Documents Maintainer','Award Documents Maintainer','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-AWARD','Application Administrator','Application Administrator','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-AWARD','Award Modifier','Award Modifier','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-UNT','IRB Reviewer','IRB Reviewer','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-UNT','IRB Administrator','IRB Administrator','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-UNT','Protocol Creator','Protocol Creator','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-ADM','Proposal Submission','Proposal Submission','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-ADM','OSP Administrator','OSP Administrator','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-WKFLW' AND NM = 'Derived Role - PI'),'KC-WKFLW','PI','Proposal Primary Investigator','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-WKFLW' AND NM = 'Derived Role - COI'),'KC-WKFLW','COI','Proposal Co-Investigator','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-WKFLW' AND NM = 'Derived Role - KeyPerson'),'KC-WKFLW','DepartmentReviewer','Proposal Departmental Reviewer','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-WKFLW' AND NM = 'Derived Role - KeyPerson'),'KC-WKFLW','CustomReviewer','Proposal Custom Reviewer','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-WKFLW' AND NM = 'Derived Role - KeyPerson'),'KC-WKFLW','KP','Proposal Key Persons','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-IP' AND NM = 'Derived Role - Proposal Log PI'),'KC-IP','Proposal Log PI','Derived role from PI on Proposal Log','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-WKFLW' AND NM = 'Derived Role - Unit Administrator'),'KC-WKFLW','Unit Administrator','Derived role based on Unit','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-IP','Create Temporary Proposal Log','Create Temporary Proposal Log','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-IP','Create Proposal Log','Create Proposal Log','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-IP','Modify Proposal Log','Modify Proposal Log','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'UnitHierarchy'),'KC-IP','View Proposal Log','View Proposal Log','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-WKFLW' AND NM = 'Derived Role: IRB Online Reviewer'),'KC-PROTOCOL','IRB Online Reviewer','Online Reviewers','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-SYS' AND NM = 'Unit'),'KC-PROTOCOL','Protocol Unassigned','Protocol Unassigned - no permissions','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC_SYS' AND NM = 'IRBApprover-Nested'),'KC-PROTOCOL','ProtocolApprover','This role exists primarily to grant implicit Cancel permission to Protocol Aggregators and Admins','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-WKFLW' AND NM = 'Derived Role: Active Committee Member'),'KC-PROTOCOL','Active Committee Member','Role members are derived from active committee members on the systems current date.','Y',NOW(),UUID(),1)
/
INSERT INTO KRIM_ROLE_ID_S VALUES (null)
/
INSERT INTO KRIM_ROLE_T (ROLE_ID,KIM_TYP_ID,NMSPC_CD,ROLE_NM,DESC_TXT,ACTV_IND,LAST_UPDT_DT,OBJ_ID,VER_NBR) 
    VALUES ((SELECT MAX(ID) FROM KRIM_ROLE_ID_S),(SELECT KIM_TYP_ID FROM KRIM_TYP_T WHERE NMSPC_CD = 'KC-WKFLW' AND NM = 'Derived Role: Active Committee Member on Scheduled Date'),'KC-PROTOCOL','Active Committee Member On Scheduled Date','Role members are derived from the active committee members on a particular schedule date.','Y',NOW(),UUID(),1)
/
delimiter ;
