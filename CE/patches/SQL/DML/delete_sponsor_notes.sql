-- delete related award templates
delete from award_transferring_sponsor;
delete from award_template_contact;
delete from award_template_report_terms;
delete from award_template_terms;
delete from award_template;

-- delete related proposals
delete from proposal_comments;
delete from proposal_ip_review_join;
delete from proposal_cost_sharing;
delete from proposal_admin_details;
delete from award_funding_proposals;
delete from proposal_special_review;
delete from proposal_custom_data;
delete from proposal_science_keyword;
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
delete from eps_proposal;

delete from KRNS_DOC_HDR_T where DOC_HDR_ID IN (select DOCUMENT_NUMBER from EPS_PROPOSAL_DOCUMENT);
delete from krew_actn_itm_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from EPS_PROPOSAL_DOCUMENT);
delete from krew_doc_hdr_t where DOC_HDR_ID IN (select DOCUMENT_NUMBER from EPS_PROPOSAL_DOCUMENT);

delete from EPS_PROPOSAL_DOCUMENT;

-- finally delete all sponsors
delete from sponsor_forms;
delete from sponsor;
