select * from unit;
update unit set UNIT_NAME='Portland State University' where UNIT_NUMBER='000001';

delete from award_pers_unit_cred_splits where AWARD_PERSON_UNIT_ID in (select AWARD_PERSON_UNIT_ID from award_person_units where UNIT_NUMBER!='000001');
delete from award_person_units where UNIT_NUMBER!='000001';
delete from iacuc_protocol_units where UNIT_NUMBER!='000001';
delete from protocol_units where UNIT_NUMBER!='000001';
delete from award_report_tracking where LEAD_UNIT_NUMBER!='000001';
delete from award_budget_limit where AWARD_ID in (select AWARD_ID from award where LEAD_UNIT_NUMBER!='000001');
delete from award_amount_info where AWARD_ID in (select AWARD_ID from award where LEAD_UNIT_NUMBER!='000001');
delete from award_person_credit_splits where AWARD_PERSON_ID in (select AWARD_PERSON_ID from award_persons where AWARD_ID in (select AWARD_ID from award where LEAD_UNIT_NUMBER!='000001'));
delete from award_persons where AWARD_ID in (select AWARD_ID from award where LEAD_UNIT_NUMBER!='000001');
delete from award_closeout where AWARD_ID in (select AWARD_ID from award where LEAD_UNIT_NUMBER!='000001');
delete from award_comment where AWARD_ID in (select AWARD_ID from award where LEAD_UNIT_NUMBER!='000001');
delete from award_cost_share where AWARD_ID in (select AWARD_ID from award where LEAD_UNIT_NUMBER!='000001');
delete from award_rep_terms_recnt where AWARD_REPORT_TERMS_ID in (select AWARD_REPORT_TERMS_ID from award_report_terms where AWARD_ID in (select AWARD_ID from award where LEAD_UNIT_NUMBER!='000001'));
delete from award_report_terms where AWARD_ID in (select AWARD_ID from award where LEAD_UNIT_NUMBER!='000001');
delete from award_special_review where AWARD_ID in (select AWARD_ID from award where LEAD_UNIT_NUMBER!='000001');
delete from award_funding_proposals where AWARD_ID in (select AWARD_ID from award where LEAD_UNIT_NUMBER!='000001');
delete from award_attachment where AWARD_ID in (select AWARD_ID from award where LEAD_UNIT_NUMBER!='000001');
delete from award_payment_schedule where AWARD_ID in (select AWARD_ID from award where LEAD_UNIT_NUMBER!='000001');
delete from award where LEAD_UNIT_NUMBER!='000001';

delete from proposal_log where LEAD_UNIT in (select UNIT_NUMBER from unit where UNIT_NUMBER!='000001');
delete from unit_administrator where UNIT_NUMBER in (select UNIT_NUMBER from unit where UNIT_NUMBER!='000001');
delete from unit where UNIT_NUMBER!='000001';
