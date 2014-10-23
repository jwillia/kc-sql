DELIMITER //
DROP PROCEDURE IF EXISTS RemoveTransactionalDataOnly //
CREATE PROCEDURE RemoveTransactionalDataOnly()
BEGIN

DECLARE exit handler for sqlexception
  BEGIN
    SHOW ERRORS;
    ROLLBACK;
END;

DECLARE exit handler for sqlwarning
  BEGIN
    SHOW WARNINGS;
    ROLLBACK;
END;

START TRANSACTION;

-- select * from iacuc_protocol;
delete from iacuc_principles;
delete from iacuc_protocol_actions;
delete from iacuc_protocol_attach_protocol;
delete from iacuc_protocol_funding_source;
delete from iacuc_protocol_location;
delete from iacuc_protocol_notepad;
delete from iacuc_protocol_persons;
delete from iacuc_protocol_research_areas;
delete from iacuc_proc_person_responsible;
delete from iacuc_protocol_study_groups;
delete from iacuc_protocol_species;
delete from iacuc_protocol_study_group_hdr;
delete from iacuc_proto_study_group_locs;
delete from comm_schedule_minutes;
delete from iacuc_protocol_alt_search;
delete from iacuc_protocol_submission;
delete from iacuc_proto_amend_renew_mod;
delete from iacuc_proto_amend_renewal;
delete from iacuc_protocol;

delete from KRNS_DOC_HDR_T where DOC_HDR_ID IN (select DOCUMENT_NUMBER from iacuc_protocol_document);
delete from krew_actn_itm_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from iacuc_protocol_document);
delete from krew_doc_hdr_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from iacuc_protocol_document);
delete from iacuc_protocol_document;

-- select * from protocol;
delete from comm_batch_corresp_detail;
delete from protocol_correspondence;
delete from protocol_actions;
delete from protocol_attachment_protocol;
delete from protocol_funding_source;
delete from protocol_location;
delete from protocol_notepad;
delete from protocol_onln_rvws;
delete from protocol_attachment_personnel;
delete from protocol_persons;
delete from protocol_research_areas;
delete from comm_schedule_minutes;
delete from protocol_reviewers;
delete from protocol_exempt_number;
delete from protocol_special_review;
delete from protocol_expidited_chklst;
delete from protocol_exempt_chklst;
delete from protocol_submission;
delete from protocol_vulnerable_sub;
delete from proto_amend_renew_modules;
delete from proto_amend_renewal;
delete from protocol_risk_levels;
delete from protocol_references;
delete from protocol_units;
delete from protocol;

delete from KRNS_DOC_HDR_T where DOC_HDR_ID IN (select DOCUMENT_NUMBER from protocol_document);
delete from krew_actn_itm_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from protocol_document);
delete from krew_doc_hdr_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from protocol_document);
delete from protocol_document;

-- select * from negotiation;
delete from negotiation_attachment;
delete from negotiation_activity;
delete from negotiation_unassoc_detail;
delete from negotiation;

delete from KRNS_DOC_HDR_T where DOC_HDR_ID IN (select DOCUMENT_NUMBER from negotiation_document);
delete from krew_actn_itm_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from negotiation_document);
delete from krew_doc_hdr_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from negotiation_document);
delete from negotiation_document;

-- select * from proposal;
delete from proposal_comments;
delete from proposal_ip_review_join;
delete from proposal_cost_sharing;
delete from proposal_admin_details;
delete from award_funding_proposals;
delete from proposal_special_review;
delete from proposal_custom_data;
delete from proposal_science_keyword;
delete from proposal_notepad;
delete from proposal;

delete from eps_prop_per_credit_split;
delete from eps_prop_unit_credit_split;
delete from eps_proposal_budget_status;
delete from eps_prop_sites;
delete from eps_prop_ynq;
delete from narrative_user_rights;
delete from narrative;
delete from s2s_opportunity;
delete from s2s_opp_forms;
delete from eps_prop_exempt_number;
delete from eps_prop_special_review;
delete from awd_bgt_det_cal_amts_ext;
delete from budget_details_cal_amts;
delete from budget_rate_and_base;
delete from budget_per_det_rate_and_base;
delete from awd_budget_per_cal_amts_ext;
delete from budget_personnel_cal_amts;
delete from awd_budget_per_det_ext;
delete from budget_personnel_details;
delete from award_budget_details_ext;
delete from budget_details;
delete from budget_persons;
delete from eps_prop_person;
delete from eps_prop_science_keyword;
delete from eps_prop_abstract;
delete from eps_prop_changed_data;
delete from eps_prop_cong_district;
delete from eps_prop_exempt_number;
delete from eps_prop_la_rates;
delete from eps_prop_location;
delete from eps_prop_pers_ynq;
delete from eps_prop_person_bio;
delete from eps_prop_person_bio_attachment;
delete from eps_prop_person_degree;
delete from eps_prop_person_ext;
delete from eps_prop_person_units;
delete from eps_prop_user_roles;
delete from eps_proposal where HIERARCHY_PROPOSAL_NUMBER is not NULL;
delete from eps_proposal where HIERARCHY_PROPOSAL_NUMBER is NULL;
delete from eps_proposal;

delete from KRNS_DOC_HDR_T where DOC_HDR_ID IN (select DOCUMENT_NUMBER from EPS_PROPOSAL_DOCUMENT);
delete from krew_actn_itm_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from EPS_PROPOSAL_DOCUMENT);
delete from krew_doc_hdr_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from EPS_PROPOSAL_DOCUMENT);
delete from EPS_PROPOSAL_DOCUMENT;

delete from KRNS_DOC_HDR_T where DOC_HDR_ID IN (select DOCUMENT_NUMBER from institute_proposal_document);
delete from krew_actn_itm_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from institute_proposal_document);
delete from krew_doc_hdr_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from institute_proposal_document);
delete from institute_proposal_document;

-- select * from award;
delete from award_amount_info;
delete from award_amount_transaction;
delete from award_amt_fna_distribution;
delete from award_approved_equipment;
delete from award_approved_foreign_travel;
delete from award_approved_subawards;
delete from award_attachment;
delete from award_budget_details_ext;
delete from award_budget_limit;
delete from award_closeout;
delete from award_comment;
delete from award_cost_share;
delete from award_custom_data;
delete from award_exempt_number;
delete from award_funding_proposals;
delete from award_hierarchy;
delete from award_idc_rate;
delete from award_notepad;
delete from award_payment_schedule;
delete from award_pers_unit_cred_splits;
delete from award_person_credit_splits;
delete from award_person_units;
delete from award_persons;
delete from award_rep_terms_recnt;
delete from award_report_notification_sent;
delete from award_report_terms;
delete from award_report_tracking;
delete from award_science_keyword;
delete from award_special_review;
delete from award_sponsor_contacts;
delete from award_sponsor_term;
delete from award_subcontract_goals_exp;
delete from award_sync_change;
delete from award_sync_log;
delete from award_sync_status;
delete from award_template_contact;
delete from award_template_report_terms;
delete from award_template_terms;
delete from award_template;
delete from award_transferring_sponsor;
delete from award_unit_contacts;
delete from award;

delete from KRNS_DOC_HDR_T where DOC_HDR_ID IN (select DOCUMENT_NUMBER from award_document);
delete from krew_actn_itm_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from award_document);
delete from krew_doc_hdr_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from award_document);
delete from award_document;

-- select * from notification;
-- delete from notification;

-- select * from proposal_log;
delete from proposal_log;

-- select * from subaward
delete from subaward_amount_info;
delete from subaward_contact;
delete from subaward_funding_source;
delete from subaward_amt_released;
delete from subaward_template_info;
delete from subaward_closeout;
delete from subaward;

delete from KRNS_DOC_HDR_T where DOC_HDR_ID IN (select DOCUMENT_NUMBER from subaward_document);
delete from krew_actn_itm_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from subaward_document);
delete from krew_doc_hdr_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from subaward_document);
delete from subaward_document;

-- select * from coi_disclosure;
delete from coi_discl_projects;
delete from coi_disclosure_history;
delete from coi_disclosure_notepad;
delete from coi_user_roles;
delete from coi_disc_details;
delete from coi_disclosure;

delete from KRNS_DOC_HDR_T where DOC_HDR_ID IN (select DOCUMENT_NUMBER from coi_disclosure_document);
delete from krew_actn_itm_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from coi_disclosure_document);
delete from krew_doc_hdr_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from coi_disclosure_document);
delete from coi_disclosure_document;

-- select * from budget;
delete from budget_modular_idc;
delete from budget_modular;
delete from award_budget_period_ext;
delete from awd_bgt_per_sum_calc_amt;
delete from budget_periods;
delete from eps_prop_rates;
delete from eps_proposal_budget_ext;
delete from eps_prop_cost_sharing;
delete from budget_sub_award_files;
delete from budget_sub_award_period_detail;
delete from budget_sub_awards;
delete from award_budget_ext;
delete from eps_prop_idc_rate;
delete from bud_formulated_cost_detail;
delete from budget_changed_data;
delete from budget_person_salary_details;
delete from budget_project_income;
delete from budget_sub_award_att;
delete from budget;

delete from KRNS_DOC_HDR_T where DOC_HDR_ID IN (select DOCUMENT_NUMBER from budget_document);
delete from krew_actn_itm_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from budget_document);
delete from krew_doc_hdr_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from budget_document);
delete from budget_document;

-- select * from krns_pessimistic_lock_t;
delete from krns_pessimistic_lock_t;

-- Clean up Rice's workflow docs
delete from krew_actn_itm_t;
delete from krew_actn_rqst_t;
delete from krew_actn_tkn_t;
delete from krew_doc_hdr_cntnt_t;
delete from krew_doc_hdr_ext_dt_t;
delete from krew_doc_hdr_ext_flt_t;
delete from krew_doc_hdr_ext_long_t;
delete from krew_doc_hdr_ext_t;
delete from krew_doc_hdr_t;
delete from krew_doc_nte_t;
delete from krns_maint_doc_t;
delete from krns_doc_hdr_t;
delete from krns_maint_doc_att_lst_t;
delete from krns_maint_doc_att_t;
delete from krns_maint_lock_t;

-- clean up any remaining workflow cruft
delete from KRNS_DOC_HDR_T;
delete from krew_actn_itm_t;
delete from krew_doc_hdr_t;
delete from krew_out_box_itm_t;
delete from COMMITTEE_DOCUMENT where COMMITTEE_ID NOT IN (select COMMITTEE_ID from COMMITTEE);

COMMIT;

END //
DELIMITER ;

call RemoveTransactionalDataOnly();

DROP PROCEDURE IF EXISTS RemoveTransactionalDataOnly;
