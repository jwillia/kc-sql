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
DELETE FROM KREW_ACTN_RQST_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS Fellowship Supplemental Form V1-0 & 1-1')
/
DELETE FROM KREW_ACTN_TKN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS Fellowship Supplemental Form V1-0 & 1-1')
/
DELETE FROM KREW_DOC_HDR_CNTNT_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS Fellowship Supplemental Form V1-0 & 1-1')
/
DELETE FROM KREW_RTE_BRCH_T WHERE RTE_BRCH_ID = (SELECT BRCH_ID FROM KREW_RTE_NODE_INSTN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS Fellowship Supplemental Form V1-0 & 1-1'))
/
DELETE FROM KREW_RTE_NODE_INSTN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS Fellowship Supplemental Form V1-0 & 1-1')
/
DELETE FROM KREW_DOC_HDR_T WHERE TTL = 'PHS Fellowship Supplemental Form V1-0 & 1-1'
/

DELETE FROM KREW_ACTN_RQST_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'NSF s2s form supporting questions')
/
DELETE FROM KREW_ACTN_TKN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'NSF s2s form supporting questions')
/
DELETE FROM KREW_DOC_HDR_CNTNT_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'NSF s2s form supporting questions')
/
DELETE FROM KREW_RTE_BRCH_T WHERE RTE_BRCH_ID = (SELECT BRCH_ID FROM KREW_RTE_NODE_INSTN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'NSF s2s form supporting questions'))
/
DELETE FROM KREW_RTE_NODE_INSTN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'NSF s2s form supporting questions')
/
DELETE FROM KREW_DOC_HDR_T WHERE TTL = 'NSF s2s form supporting questions'
/

DELETE FROM KREW_ACTN_RQST_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS398 Training Budget Form version 1-0')
/
DELETE FROM KREW_ACTN_TKN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS398 Training Budget Form version 1-0')
/
DELETE FROM KREW_DOC_HDR_CNTNT_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS398 Training Budget Form version 1-0')
/
DELETE FROM KREW_RTE_BRCH_T WHERE RTE_BRCH_ID = (SELECT BRCH_ID FROM KREW_RTE_NODE_INSTN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS398 Training Budget Form version 1-0'))
/
DELETE FROM KREW_RTE_NODE_INSTN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS398 Training Budget Form version 1-0')
/
DELETE FROM KREW_DOC_HDR_T WHERE TTL = 'PHS398 Training Budget Form version 1-0'
/

DELETE FROM KREW_ACTN_RQST_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS Fellowship Supplemental Form V1-2')
/
DELETE FROM KREW_ACTN_TKN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS Fellowship Supplemental Form V1-2')
/
DELETE FROM KREW_DOC_HDR_CNTNT_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS Fellowship Supplemental Form V1-2')
/
DELETE FROM KREW_RTE_BRCH_T WHERE RTE_BRCH_ID = (SELECT BRCH_ID FROM KREW_RTE_NODE_INSTN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS Fellowship Supplemental Form V1-2'))
/
DELETE FROM KREW_RTE_NODE_INSTN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'PHS Fellowship Supplemental Form V1-2')
/
DELETE FROM KREW_DOC_HDR_T WHERE TTL = 'PHS Fellowship Supplemental Form V1-2'
/

DELETE FROM KREW_ACTN_RQST_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'Proposal Person Certification')
/
DELETE FROM KREW_ACTN_TKN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'Proposal Person Certification')
/
DELETE FROM KREW_DOC_HDR_CNTNT_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'Proposal Person Certification')
/
DELETE FROM KREW_RTE_BRCH_T WHERE RTE_BRCH_ID = (SELECT BRCH_ID FROM KREW_RTE_NODE_INSTN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'Proposal Person Certification'))
/
DELETE FROM KREW_RTE_NODE_INSTN_T WHERE DOC_HDR_ID = (SELECT DOC_HDR_ID FROM KREW_DOC_HDR_T WHERE TTL = 'Proposal Person Certification')
/
DELETE FROM KREW_DOC_HDR_T WHERE TTL = 'Proposal Person Certification'
/
DELIMITER ;
