DROP PROCEDURE IF EXISTS `DeleteSponsorTerm`;
DELIMITER //
CREATE PROCEDURE `DeleteSponsorTerm` (IN st_id INT)
BEGIN

delete FROM award_template_terms WHERE `SPONSOR_TERM_ID` = st_id;
delete FROM sponsor_term         WHERE `SPONSOR_TERM_ID` = st_id;

END //
DELIMITER ;
