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
INSERT INTO SEQ_COMMITTEE_ID VALUES(NULL)
/
INSERT INTO COMM_MEMBER_EXPERTISE (COMM_MEMBER_EXPERTISE_ID,COMM_MEMBERSHIP_ID_FK,RESEARCH_AREA_CODE,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
VALUES ((SELECT (MAX(ID)) FROM SEQ_COMMITTEE_ID),(SELECT COMM_MEMBERSHIP_ID FROM COMM_MEMBERSHIPS WHERE PERSON_ID = '10000000005'),'000001','quickstart',NOW(),UUID(),0)
/
INSERT INTO SEQ_COMMITTEE_ID VALUES(NULL)
/
INSERT INTO COMM_MEMBER_EXPERTISE (COMM_MEMBER_EXPERTISE_ID,COMM_MEMBERSHIP_ID_FK,RESEARCH_AREA_CODE,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
VALUES ((SELECT (MAX(ID)) FROM SEQ_COMMITTEE_ID),(SELECT COMM_MEMBERSHIP_ID FROM COMM_MEMBERSHIPS WHERE PERSON_ID = '10000000006'),'000001','quickstart',NOW(),UUID(),0)
/
INSERT INTO SEQ_COMMITTEE_ID VALUES(NULL)
/
INSERT INTO COMM_MEMBER_EXPERTISE (COMM_MEMBER_EXPERTISE_ID,COMM_MEMBERSHIP_ID_FK,RESEARCH_AREA_CODE,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
VALUES ((SELECT (MAX(ID)) FROM SEQ_COMMITTEE_ID),(SELECT COMM_MEMBERSHIP_ID FROM COMM_MEMBERSHIPS WHERE PERSON_ID = '10000000007'),'000001','quickstart',NOW(),UUID(),0)
/
INSERT INTO SEQ_COMMITTEE_ID VALUES(NULL)
/
INSERT INTO COMM_MEMBER_EXPERTISE (COMM_MEMBER_EXPERTISE_ID,COMM_MEMBERSHIP_ID_FK,RESEARCH_AREA_CODE,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
VALUES ((SELECT (MAX(ID)) FROM SEQ_COMMITTEE_ID),(SELECT COMM_MEMBERSHIP_ID FROM COMM_MEMBERSHIPS WHERE PERSON_ID = '10000000008'),'000001','quickstart',NOW(),UUID(),0)
/
INSERT INTO SEQ_COMMITTEE_ID VALUES(NULL)
/
INSERT INTO COMM_MEMBER_EXPERTISE (COMM_MEMBER_EXPERTISE_ID,COMM_MEMBERSHIP_ID_FK,RESEARCH_AREA_CODE,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
VALUES ((SELECT (MAX(ID)) FROM SEQ_COMMITTEE_ID),(SELECT COMM_MEMBERSHIP_ID FROM COMM_MEMBERSHIPS WHERE PERSON_ID = '10000000059'),'000001','quickstart',NOW(),UUID(),0)
/
INSERT INTO SEQ_COMMITTEE_ID VALUES(NULL)
/
INSERT INTO COMM_MEMBER_EXPERTISE (COMM_MEMBER_EXPERTISE_ID,COMM_MEMBERSHIP_ID_FK,RESEARCH_AREA_CODE,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
VALUES ((SELECT (MAX(ID)) FROM SEQ_COMMITTEE_ID),(SELECT COMM_MEMBERSHIP_ID FROM COMM_MEMBERSHIPS WHERE PERSON_ID = '10000000004'),'05.0126','quickstart',NOW(),UUID(),0)
/
INSERT INTO SEQ_COMMITTEE_ID VALUES(NULL)
/
INSERT INTO COMM_MEMBER_EXPERTISE (COMM_MEMBER_EXPERTISE_ID,COMM_MEMBERSHIP_ID_FK,RESEARCH_AREA_CODE,UPDATE_USER,UPDATE_TIMESTAMP,OBJ_ID,VER_NBR) 
VALUES ((SELECT (MAX(ID)) FROM SEQ_COMMITTEE_ID),(SELECT COMM_MEMBERSHIP_ID FROM COMM_MEMBERSHIPS WHERE PERSON_ID = '10000000002'),'000001','quickstart',NOW(),UUID(),0)
/
DELIMITER ;