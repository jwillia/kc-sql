DROP PROCEDURE IF EXISTS `DeleteAwardTemplate`;
DELIMITER //
CREATE PROCEDURE `DeleteAwardTemplate` ()
BEGIN

delete from award_template_comments     where AWARD_TEMPLATE_CODE in (select AWARD_TEMPLATE_CODE from award_template);
delete from award_template_contact      where AWARD_TEMPLATE_CODE in (select AWARD_TEMPLATE_CODE from award_template);
delete from award_template_terms        where AWARD_TEMPLATE_CODE in (select AWARD_TEMPLATE_CODE from award_template);
delete from award_template_report_terms where AWARD_TEMPLATE_CODE in (select AWARD_TEMPLATE_CODE from award_template);
delete from award_template;

END //
DELIMITER ;
