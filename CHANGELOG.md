

##CURRENT
* fix V1507_005 checksum
  * blackcathacker on Thu, 9 Jul 2015 15:11:50 -0700 [View Commit](../../commit/04b2c5230d1a27c695557dd69881f345f368e65c)

##kc-sql-1506.1
* Minimally support oracle for Flyway

  * This is so we can begin validating our Oracle sql automatically instead of right before release and also run integration tests against Oracle
  * blackcathacker on Fri, 19 Jun 2015 16:37:42 -0700 [View Commit](../../commit/843c95e216481b180712706eb947a6ec94791408)

##kc-sql-1505.2
* Move to Java 8
  * Travis Schneberger on Thu, 23 Apr 2015 16:18:31 -0400 [View Commit](../../commit/46b802d74c3dfd3e687ef1eaf1bf23682c047cbc)

##kc-sql-1505.1
* No Changes


##kc-sql-1504.7
* No Changes


##kc-sql-1504.6
* No Changes


##kc-sql-1504.5
* No Changes


##kc-sql-1504.4
* No Changes


##kc-sql-1504.3
* No Changes


##kc-sql-1504.2
* Add pre migration sql to flyway migrator
  * blackcathacker on Fri, 17 Apr 2015 20:22:52 -0700 [View Commit](../../commit/ac1acb8d93070b5f6dea6b2fc698cdf97ccc7084)

##kc-sql-1504.1
* No Changes


##kc-sql-6.0.1
* Refactor for rice 2.5.x
  * Lance Speelmon on Thu, 11 Dec 2014 13:52:06 -0700 [View Commit](../../commit/db5781cdfc8704df08247cf0bfecf79d8ae225d5)
* Switch from org.kuali.kra.logging.BufferedLogger to org.kuali.rice.core.util.BufferedLogger to accomodate rice 2.5 changes.
  * Lance Speelmon on Thu, 11 Dec 2014 14:04:34 -0700 [View Commit](../../commit/4b7a8647e998c01e70ecf6c37b895a48f67416f5)
* switch from Rice's BufferedLogger to log4j
  * Lance Speelmon on Fri, 12 Dec 2014 11:01:38 -0700 [View Commit](../../commit/5b99e79d3fc7e3d8065c11ac34f4c8378565fd90)
* Should fix: org.kuali.rice.kew.api.WorkflowRuntimeException: Could not load DocumentTypeAuthorizer: org.kuali.kra.proposaldevelopment.document.authorization.ProposalDevelopmentWorkflowDocumentAuthorizer at org.kuali.rice.kew.doctype.service.impl.DocumentTypePermissionServiceAuthorizerImpl.getDocumentTypeAuthorizer(DocumentTypePermissionServiceAuthorizerImpl.java:75) at org.kuali.rice.kew.doctype.service.impl.DocumentTypePermissionServiceAuthorizerImpl.canInitiate(DocumentTypePermissionServiceAuthorizerImpl.java:91)
  * Lance Speelmon on Wed, 17 Dec 2014 13:18:48 -0700 [View Commit](../../commit/33c9dae2cd95f1d998048eb681c613c05cde382d)
* Adding SQL from Kuali Coeus 6.0
  * See https://github.com/kuali/kc for source of these scripts
  * Adding flyway maven plugin and mysql for manually applying Flyway migrations.
  * blackcathacker on Mon, 12 Jan 2015 17:29:04 -0800 [View Commit](../../commit/79c5422bc11df792d5c7c6869cb37ed1d416a9b7)
* Kuali Coeus 6.0 Database script fixes and changes
  * Should be applied upstream as well
  * blackcathacker on Tue, 13 Jan 2015 16:28:49 -0800 [View Commit](../../commit/79090de4865c82b757efa73043a9920c303d881b)
* moving all KC scripts to flyway
  * blackcathacker on Tue, 27 Jan 2015 17:33:21 -0800 [View Commit](../../commit/ba541b5efa1ab42b377d1ee9b4afed43175d64ee)
* Adding demo scripts to 6.0 and updating 6.0 scripts to match release
  * blackcathacker on Mon, 2 Feb 2015 09:53:50 -0800 [View Commit](../../commit/1b4d8c60f9101d4ef666a957657be1855c224018)
* Updated and deleted duplicate rsmart files
  * blackcathacker on Mon, 2 Feb 2015 14:06:10 -0800 [View Commit](../../commit/f4b3270966aa9857eb97bd75c8d0a3e3769bc921)
* Further deleted from rsmart
  * blackcathacker on Mon, 2 Feb 2015 14:10:11 -0800 [View Commit](../../commit/5285ea636fc707ae6ca19389ad0a5895761c9dac)
* Further cleanup again
  * blackcathacker on Mon, 2 Feb 2015 17:46:40 -0800 [View Commit](../../commit/0e8354c981c85fb6844f4d502122f80f5d5e43f4)
* Renamed package to co.kuali and added custom migrations
  * blackcathacker on Mon, 2 Feb 2015 22:38:00 -0800 [View Commit](../../commit/f45b5ddd16dc2051dff89a9a8841f5dbe6a28403)
* Code review comments
  * blackcathacker on Wed, 4 Feb 2015 17:03:36 -0800 [View Commit](../../commit/357a4fcb4ced89a928d9211dcdb8f9d05e125d4a)
* KRACOUES-6639:db changes for person effort
  * Joe Williams on Fri, 6 Feb 2015 08:09:58 -0600 [View Commit](../../commit/45d48dc33c8d4ad75911354afe17eb43a7f5d815)
* KRACOEUS-8800 : Fixing classnames post repackaging
  * blackcathacker on Thu, 5 Feb 2015 22:59:29 -0800 [View Commit](../../commit/aee9396a77ef80fe3252d58d1b6201041531183b)
* KRACOEUS-8800 : code review changes
  * blackcathacker on Fri, 6 Feb 2015 09:59:19 -0800 [View Commit](../../commit/44f94b2c5d07b50ca8ffc4434f9e44040e32aded)
* KRACOEUS-8820 : Demo Scripts
  * blackcathacker on Fri, 6 Feb 2015 16:50:59 -0800 [View Commit](../../commit/6e0c1a347df158dccdb4f70b12dc27b421f73cf3)
* KRACOEUS-8820 : Staging data
  * blackcathacker on Fri, 6 Feb 2015 19:01:07 -0800 [View Commit](../../commit/a2412d1d297ec999747c875142820e4a1dc276ae)
* KRACOEUS-8820 : Improvements to Flyway migrator
  * blackcathacker on Sat, 7 Feb 2015 23:22:31 -0800 [View Commit](../../commit/d738437eb2f4f13f3e167051596f0eda5fc5b363)
* KRACOEUS-8820: adding documentation
  * Travis Schneberger on Sun, 8 Feb 2015 17:30:22 -0500 [View Commit](../../commit/549862fba66f77336dd22be96747ef04379c09ce)
* KRACOUES-6639 : Fix script naming
  * blackcathacker on Sun, 8 Feb 2015 16:55:03 -0800 [View Commit](../../commit/adfb7b8fbef2713cd0eb0f7f069c5c037ea34eae)
* Update README.md  * Travis Schneeberger on Sun, 8 Feb 2015 20:34:41 -0500 [View Commit](../../commit/5db387dd70f13b2f2c35bf0946f83f5c42ebe811)
* Update README.md  * Travis Schneeberger on Sun, 8 Feb 2015 20:35:48 -0500 [View Commit](../../commit/60772e1950ae31f64b104e6185bb13d547cdcb52)
* Update README.md  * Travis Schneeberger on Sun, 8 Feb 2015 20:36:09 -0500 [View Commit](../../commit/a9eaa43e372a891317f13bfd44359719f2aa2aa0)
* KRACOEUS-8788: fixing demo and stage data
  * Travis Schneberger on Mon, 9 Feb 2015 18:06:39 -0500 [View Commit](../../commit/3a7590ff0ab93b91e470d1ff025a6d51ea440e55)
* KRACOEUS-8814: support billing frequency
  * Travis Schneberger on Mon, 9 Feb 2015 18:28:17 -0500 [View Commit](../../commit/15dfbb09e599a8664a71de0830610cbebe5c45d1)
* Updating README
  * blackcathacker on Mon, 9 Feb 2015 21:34:59 -0700 [View Commit](../../commit/13c6b937d177889f3a9e25e734a295861a50a11a)
* KRACOEUS-8788 : fixing sql
  * blackcathacker on Mon, 9 Feb 2015 22:12:49 -0700 [View Commit](../../commit/ce2042bd52068d3b7dd4db00fd709bc6cccabccb)
* KRACOEUS-8824 : Table to support auto-ingestion
  * blackcathacker on Mon, 9 Feb 2015 22:15:50 -0700 [View Commit](../../commit/d5b296951eb8b9cb077a8f921060bda15693cbbf)
* KRACOEUS-8824 : Auto-ingestion code
  * blackcathacker on Thu, 12 Feb 2015 16:16:32 -0700 [View Commit](../../commit/5fdc40cc03bc5dc0bf55ac3ae973d297cb9189ab)
* KRACOEUS-8824 : Code review comment
  * blackcathacker on Thu, 12 Feb 2015 19:34:13 -0700 [View Commit](../../commit/ed2bf471ae256a49e227df5ba98467317799333e)
* KRACOEUS-8830: reorganize and refactor to split the migrator logic from the migrator files
  * Travis Schneberger on Fri, 13 Feb 2015 09:25:09 -0500 [View Commit](../../commit/991a301e3d606bd1587518ad92093c75afe015db)
* KRACOEUS-8830: deleting sql migrations
  * Travis Schneberger on Fri, 13 Feb 2015 16:30:17 -0500 [View Commit](../../commit/d26912bd702f86b2809d8b4c349c08d45222d89a)
* KRACOEUS-8830: code review comments
  * Travis Schneberger on Fri, 13 Feb 2015 17:10:55 -0500 [View Commit](../../commit/56dc37d91342bce7beb6d6cc50103add0a7a1020)
* KRACOEUS-8830: code review comments
  * Travis Schneberger on Fri, 13 Feb 2015 17:16:36 -0500 [View Commit](../../commit/87e0b29f0e18bd9618efe42cdd42b3d799444adf)
*  support grm data in our flyway process
  * Travis Schneberger on Mon, 9 Mar 2015 23:29:58 -0400 [View Commit](../../commit/7c1ea59d118859ca615e9f9aef48bcc157e9add2)
*  support for jenkins
  * Travis Schneberger on Fri, 20 Mar 2015 12:53:17 -0400 [View Commit](../../commit/5aabc932e9e00970405dcfd8b9aa6baa380f87f4)
* release
  * Travis Schneberger on Sat, 4 Apr 2015 09:18:03 -0400 [View Commit](../../commit/da12a164c2f30d8de0c1c4359d291e51b077fd05)

##cx_data_migration-5.2.1.11
* No Changes


##cx_data_migration-5.2.1.10
* Updating maven release plugin to fix git issue
  * blackcathacker on Fri, 23 Jan 2015 10:13:46 -0800 [View Commit](../../commit/0a1ef42b175b7ed4d28525b5c88663791a55f63f)

##cx_data_migration-5.2.1.9
* SO-104, SO-102 : Add authorizer to PD KEW file.
  * blackcathacker on Tue, 9 Dec 2014 10:14:37 -0800 [View Commit](../../commit/a71b12c675054f5e53c07e65ccd43e0bd886548e)
* Refactor for new kuali_toolbox gem
  * Lance Speelmon on Thu, 11 Dec 2014 12:44:30 -0700 [View Commit](../../commit/eab698162fe9a3ddf461b55e7b96f14671ce523d)

##cx_data_migration-5.2.1.8
* Be way more careful about deleting from KEW tables when cleaning up transactional data.
  * CSUMB - unable to edit IRB Committee
  * https://github.com/rSmart/issues/issues/498
  * Lance Speelmon on Wed, 19 Nov 2014 09:58:57 -0700 [View Commit](../../commit/71fd78c6c281c2ff61abb971d97290782f45c9c7)
* SQL files have been moved to: https://github.com/rSmart/puppet/tree/master/modules/cx_data_migration/files
  * Lance Speelmon on Wed, 19 Nov 2014 10:41:59 -0700 [View Commit](../../commit/10a61646bbf4b48680b536ee4df9d6ee128627e2)

##cx_data_migration-5.2.1.7
* bundle update rsmart_toolbox
  * Lance Speelmon on Mon, 27 Oct 2014 09:54:30 -0700 [View Commit](../../commit/3a6d4d7640e6fec5ce95d59e67e596af61429530)
* added some notes on finding max effective award
  * Lance Speelmon on Mon, 27 Oct 2014 15:25:53 -0700 [View Commit](../../commit/ab6a89dab14f819f8bbb67e3a390962629c9a37f)
* bundle update rsmart_toolbox
  * Lance Speelmon on Thu, 30 Oct 2014 14:52:42 -0700 [View Commit](../../commit/19316534be4bf74eb65f0a1fc57f6e2ad58fe88a)
* Added load_organization.rb tool
  * Lance Speelmon on Thu, 30 Oct 2014 14:58:15 -0700 [View Commit](../../commit/2deecd5660d7ce6ae9bffb5db576df8c6e9db994)

##cx_data_migration-5.2.1.6.1
* ERROR 1062 (23000): Duplicate entry '6' for key 'PRIMARY'
  * Lance Speelmon on Fri, 24 Oct 2014 10:04:15 -0700 [View Commit](../../commit/d47cd07c4e867158eb1eee052acd52055a20f20b)
* re-release 5.2.1.6 version
  * Lance Speelmon on Fri, 24 Oct 2014 10:04:56 -0700 [View Commit](../../commit/67fde526eab182d76af2d843838cc0608da1ce98)

##cx_data_migration-5.2.1.6
* STE when trying to access an IRB committee: unable to locate document with documentHeaderId 'xxx'
  * https://github.com/rSmart/issues/issues/458
  * Lance Speelmon on Mon, 13 Oct 2014 15:03:48 -0700 [View Commit](../../commit/1b664f687e5158eb73b4f0ed29e72c8b3ac1f1f7)
* Deactivate DLC as a Negotiation Location.
  * https://github.com/rSmart/issues/issues/472
  * Lance Speelmon on Mon, 20 Oct 2014 16:34:19 -0700 [View Commit](../../commit/f511320ab726212c00f202d44055306f6ec7839b)
* Add Pending as a dropdown choice for Subaward Status.
  * https://github.com/rSmart/issues/issues/471
  * Lance Speelmon on Mon, 20 Oct 2014 16:49:07 -0700 [View Commit](../../commit/a1deba88101ed1dbe24b3c2bdfd9c99513ffd41d)
* Cannot delete or update a parent row: a foreign key constraint fails (`test`.`iacuc_proto_amend_renewal`, CONSTRAINT `FK_IACUC_PROTO_AMEND_RENEWAL` FOREIGN KEY (`PROTOCOL_ID`) REFERENCES `iacuc_protocol` (`PROTOCOL_ID`))
  * http://jira.s2.rsmart.com:8080/browse/SO-73
  * Lance Speelmon on Thu, 23 Oct 2014 10:45:36 -0700 [View Commit](../../commit/b9cde9f863a649028ce131c933d5c1b9b79b9f38)
* ERROR 1451 (23000): Cannot delete or update a parent row: a foreign key constraint fails (`coeus`.`proposal_notepad`, CONSTRAINT `FK_PROPOSAL_NOTEPAD` FOREIGN KEY (`PROPOSAL_ID`) REFERENCES `proposal` (`PROPOSAL_ID`))
  * http://jira.s2.rsmart.com:8080/browse/SO-73
  * Lance Speelmon on Thu, 23 Oct 2014 12:23:41 -0700 [View Commit](../../commit/e24c98d0e62cff5fd37116b6621d3a2d2e9f0e9f)
* Coastal IRB STE Error when trying to view an IRB Committee
  * https://github.com/rSmart/issues/issues/478
  * Lance Speelmon on Thu, 23 Oct 2014 13:40:14 -0700 [View Commit](../../commit/4375503a34c4b11aa9cfd875943dcb4b8eedde49)
* Increase maximum file size for attachments from 5M to 25M.
  * https://github.com/rSmart/issues/issues/481
  * Lance Speelmon on Thu, 23 Oct 2014 13:55:29 -0700 [View Commit](../../commit/b15b94b7b2bed81f69550ef125d315d1d1555a47)
* Cannot delete or update a parent row: a foreign key constraint fails (`test`.`eps_prop_exempt_number`, CONSTRAINT `FK_EPS_PROP_SPECIAL_REVIEW` FOREIGN KEY (`PROPOSAL_SPECIAL_REVIEW_ID`) REFERENCES `eps_prop_special_review` (`PROPOSAL_SPECIAL_REVIEW_ID`))
  * http://jira.s2.rsmart.com:8080/browse/SO-73
  * Lance Speelmon on Thu, 23 Oct 2014 16:32:46 -0700 [View Commit](../../commit/41c77124f3bafc56edb0ad0f8b4f7fb64b152ea3)
* Cannot delete or update a parent row: a foreign key constraint fails (`coeus`.`eps_proposal`, CONSTRAINT `EPS_PROPOSAL_FK1` FOREIGN KEY (`HIERARCHY_PROPOSAL_NUMBER`) REFERENCES `eps_proposal` (`PROPOSAL_NUMBER`))
  * http://jira.s2.rsmart.com:8080/browse/SO-73
  * Lance Speelmon on Thu, 23 Oct 2014 16:49:13 -0700 [View Commit](../../commit/d3a96a88ff3d8747b0e5cb17e9311cc1415542c8)
* Cannot delete or update a parent row: a foreign key constraint fails (`coeus`.`budget_project_income`, CONSTRAINT `FK_BUDGET_PROJ_INC_BP_KRA` FOREIGN KEY (`BUDGET_PERIOD_NUMBER`) REFERENCES `budget_periods` (`BUDGET_PERIOD_NUMBER`))
  * http://jira.s2.rsmart.com:8080/browse/SO-73
  * Lance Speelmon on Thu, 23 Oct 2014 16:54:36 -0700 [View Commit](../../commit/263120a746f9ff5a41343ccf51176f3dbbcb7e0d)

##cx_data_migration-5.2.1.5
* Cleaning up orphaned protocol submissions for IACUC

  * fixes rSmart/issues#413
  * r351574nc3 on Thu, 2 Oct 2014 13:31:10 -0700 [View Commit](../../commit/c4813c15139c607cd617cc6031f870ef97de1bba)
* Adding migration script that will clean up orphaned protocol submissions present in rSmart/issues#413

  * fixes rSmart/issues#413
  * Leo Przybylski on Sat, 4 Oct 2014 18:32:04 -0700 [View Commit](../../commit/8fb168c0f94608828b1e1090d7e62b7d8c740d3e)
* ADD CONSTRAINT FK_IACUC_PROTOCOL_NUMBER
  * fixes rSmart/issues#413
  * Lance Speelmon on Mon, 6 Oct 2014 12:10:54 -0700 [View Commit](../../commit/8b4137da03b369da8286e7b007e40f4ea912445e)
* add LOWER() function to ensure email match
  * Lance Speelmon on Tue, 7 Oct 2014 10:43:03 -0700 [View Commit](../../commit/3b90e49d21043f0b6e3af9d3b34815fbfabb3828)
* added retroactively_deactivate_demo_users.sql
  * Lance Speelmon on Tue, 7 Oct 2014 10:43:33 -0700 [View Commit](../../commit/ef02be55cba624c26070665cdf070930ed602a54)
* Updates all AwardDocuments to the most recent AwardDocument doc type.

  * fixes rSmart/issues#444
  * Leo Przybylski on Wed, 8 Oct 2014 00:15:00 -0700 [View Commit](../../commit/75ba1f82b31773489ffa43758bd0c50b88e59216)

##cx_data_migration-5.2.1.4
* added some SQL around granting roles to a principal
  * Lance Speelmon on Tue, 23 Sep 2014 15:22:02 -0700 [View Commit](../../commit/274f8dcb4e032df1f388b5b0fa23050ca0028959)
* added two new SQL scripts: activate_principals.sql and grant_group_to_principal.sql
  * Lance Speelmon on Thu, 25 Sep 2014 13:27:40 -0700 [View Commit](../../commit/44fa707c5d4a99a7edf8047002cee479df1e6e74)
* added a new load_sponsor_term tool
  * Lance Speelmon on Mon, 29 Sep 2014 10:11:51 -0700 [View Commit](../../commit/d8e358590a8a4b4e86b8259dc2ae313a51781535)
* removed incomplete tool tools/SQL/delete_sponsor_term.sql
  * Lance Speelmon on Mon, 29 Sep 2014 10:15:13 -0700 [View Commit](../../commit/fad2a70d90ec1bcd453b57177072f00fc70e2fe7)
* KcPerson search results not producing accurate results - Inactive People do not show when Both is selected
  * https://github.com/rSmart/issues/issues/440
  * Lance Speelmon on Wed, 1 Oct 2014 15:15:05 -0700 [View Commit](../../commit/165253319f30a891a5db899f7520812d8b4c4c62)
* KcPerson search results not producing accurate results - Inactive People do not show when Both is selected.
  * https://github.com/rSmart/issues/issues/440
  * Lance Speelmon on Thu, 2 Oct 2014 13:24:39 -0700 [View Commit](../../commit/da99a13f5e1a47b53fae362754408759e2393b33)

##cx_data_migration-5.2.1.3
* update rsmart_toolbox gem to 0.8
  * Lance Speelmon on Mon, 8 Sep 2014 09:39:40 -0700 [View Commit](../../commit/1deec3bd21cb9fef13a18dcdc279d6feed2715e9)
* Cleanup all remaining workflow cruft when cleaning transactional data
  * https://github.com/rSmart/issues/issues/426
  * Lance Speelmon on Mon, 8 Sep 2014 09:55:39 -0700 [View Commit](../../commit/ec74881e073a8d34275f28ba18973e71de6f4be1)
* bind to gem rsmart_toolbox (0.10)
  * Lance Speelmon on Wed, 10 Sep 2014 14:58:55 -0700 [View Commit](../../commit/34eb1573456830288ce89ad83c98a43dfc9734ce)
* also delete from krew_out_box_itm_t when scrubbing data
  * Lance Speelmon on Wed, 10 Sep 2014 17:05:26 -0700 [View Commit](../../commit/54a6b594080ee37201f1a0835f8a0ee404c6499a)
* STE after adding documents to IRB Committee meeting
  * Caused by: org.apache.ojb.broker.metadata.MetadataException: Can't find member 'scheduleIdFk' in class class org.kuali.kra.meeting.CommitteeScheduleAttachments
  * java.lang.RuntimeException: Unable to execute for sequence name: COMM_SCHEDULE_ATTACHMENT_ID
  * https://github.com/rSmart/issues/issues/357
  * https://jira.kuali.org/browse/KRAFDBCK-11191
  * https://jira.kuali.org/browse/KRAFDBCK-11230
  * Lance Speelmon on Sat, 13 Sep 2014 08:18:01 -0700 [View Commit](../../commit/c2fd26d0eb4f4431b53ec495ebca2f293b239825)
* rev version number to match KC target release
  * Lance Speelmon on Sat, 13 Sep 2014 08:21:41 -0700 [View Commit](../../commit/9da789bde272644022de85b48f2fd20370b5ecf3)
* upgrade rsmart_toolbox (0.11)
  * Lance Speelmon on Wed, 17 Sep 2014 16:59:02 -0700 [View Commit](../../commit/de75f3e6ad888f4cf60ee8d6291df2b8a2f58283)

##cx_data_migration-5.2.1.1
* Do not clean up Committee when cleaning only transactional data
  * Fixes https://github.com/rSmart/issues/issues/365
  * Lance Speelmon on Mon, 11 Aug 2014 10:02:00 -0700 [View Commit](../../commit/213390b6fc22919ef049b3f5f968d633ced85fa1)
* Do **not** `delete from eps_prop_person_role` when cleaning up transactional data - we need those values!
  * https://github.com/rSmart/issues/issues/388
  * Lance Speelmon on Thu, 14 Aug 2014 13:32:46 -0700 [View Commit](../../commit/01cfdee3292c57d6edf6d025bb43dac08d93b32a)
* bundle update latest gems
  * Lance Speelmon on Thu, 14 Aug 2014 15:00:24 -0700 [View Commit](../../commit/9d687d36552d6126ec1a236e81c7b3e554333d62)
* refactor step #1: remove exiting CX lib
  * Lance Speelmon on Thu, 14 Aug 2014 15:04:12 -0700 [View Commit](../../commit/59aeb1d8b90e55e4f25bb2d65732b3b36222ae6d)
* update data loading scripts for latest version of gem
  * Lance Speelmon on Thu, 14 Aug 2014 15:49:47 -0700 [View Commit](../../commit/0611075367c0193f1a77394dc7fd047755c83f98)
* Added 'graph' dependency to Gemfile
  * Lance Speelmon on Thu, 14 Aug 2014 15:50:46 -0700 [View Commit](../../commit/6d18eae12257c2d92a9790bc48505b6cbc98299b)
* updated data loading scripts README
  * Lance Speelmon on Thu, 14 Aug 2014 15:53:32 -0700 [View Commit](../../commit/e0241538a6f8bb1cf709eba08973e72ac799ea32)
* include +delete from subaward_closeout; when cleaning subaward
  * Lance Speelmon on Mon, 18 Aug 2014 11:33:14 -0700 [View Commit](../../commit/88e2b2eecd9d9799f90dac10117721313737a924)
* upgrade to latest rsmart_toolbox 0.6 (was 0.4)
  * Lance Speelmon on Mon, 18 Aug 2014 16:53:49 -0700 [View Commit](../../commit/4b77fd15dfa66e288030f6d9771b7ebdfbbc6145)
* bind to gem rsmart_toolbox (0.7)
  * Lance Speelmon on Tue, 19 Aug 2014 20:34:21 -0700 [View Commit](../../commit/3467b84ee5ea8820bea36640324e0f7949fb383b)
* added a couple of SQL notes for move_parent_unit.sql and move_primary_department.sql
  * Lance Speelmon on Wed, 20 Aug 2014 16:18:27 -0700 [View Commit](../../commit/15fea216332e4becff45e4363817f50996dec323)
* admin's roles and group memberships get deactivated during a data scrub
  * Fixes https://github.com/rSmart/issues/issues/405
  * Lance Speelmon on Thu, 21 Aug 2014 10:12:43 -0700 [View Commit](../../commit/6651d174f4722baeb958555caa03a3ab75874b5b)
* Ineffective Mapping of Required Subaward GSU Purchase Order Number to FDP Subaward Agreement Form.
  * GSU - Budget Print Forms Issue
  * Fixes https://github.com/rSmart/issues/issues/295
  * Fixes https://github.com/rSmart/issues/issues/375
  * Lance Speelmon on Wed, 27 Aug 2014 08:37:35 -0700 [View Commit](../../commit/af76ae48b34f5f8fa5f2ee14c5efc47e1e70a7d3)
* Do not scrub notifications when cleaning transactional data.
  * https://github.com/rSmart/issues/issues/365
  * Lance Speelmon on Wed, 27 Aug 2014 14:02:08 -0700 [View Commit](../../commit/6d1bdf35564336687807fd29a4ee0103c7d3e67b)

##cx_data_migration-5.2.1.0
* CSV to XML mostly working - now need to address XSD failures
  * Lance Speelmon on Tue, 1 Apr 2014 15:54:17 -0700 [View Commit](../../commit/5299892200ca232dcf949d7d07cb68120fe0ba03)
* Added citizenType="1" and started affiliation_type mapping
  * Lance Speelmon on Wed, 2 Apr 2014 08:18:07 -0700 [View Commit](../../commit/b0c04de91e92b0242ad4143f0d6e06cf9112c716)
* Revise the comment for affiliation_type
  * Lance Speelmon on Thu, 3 Apr 2014 14:45:52 -0700 [View Commit](../../commit/04794a81f063a245ab82e8aae7709b7a6c4ca915)
* Fixed malformed email addresses on the fly. Switched to large source CSV file.
  * Lance Speelmon on Tue, 8 Apr 2014 11:21:28 -0700 [View Commit](../../commit/d109c157022bedd22a9b783a9e67272443dcc7ca)
* removed bogus empty row
  * Lance Speelmon on Fri, 11 Apr 2014 10:20:02 -0700 [View Commit](../../commit/c5b872226775499765164a00ed73330d5dec2f16)
* Added KC 5.2.1 upgrade SQL; note a few files were missing from upstream MySQL scripts:
  * https://jira.kuali.org/browse/KRACOEUS-7306
  * ./../../current_mysql/5.2.1/sequences/KC_SEQ_S2S_USER_ATTACHED_FORM_ATT_ID.sql
  * ./../../current_mysql/5.2.1/sequences/KC_SEQ_S2S_USER_ATTACHED_FORM_ID.sql
  * ./../../current_mysql/5.2.1/dml/KR_DML_01_KRACOEUS-6640_B000.sql
  * Lance Speelmon on Thu, 29 May 2014 15:52:45 -0700 [View Commit](../../commit/599512023f2bea566b3c6011ae0dad7150292d09)
* Fixes: Caused by: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near '/' at line 16
  * Caused by: com.googlecode.flyway.core.command.FlywaySqlScriptException: Error executing statement at line 6: create procedure roleChange ()
  * Lance Speelmon on Fri, 30 May 2014 14:21:02 -0700 [View Commit](../../commit/6fa0a6fdd146d209db59d04324d53ae84e7e0b31)
* Added missing role to KRIM_ROLE_T from KC 5.1.0 SQL migrations
  * Lance Speelmon on Fri, 30 May 2014 14:50:39 -0700 [View Commit](../../commit/502e4e7b71a2cfe8baad64e85035d46154666537)
* Fixes: Caused by: java.sql.SQLException: Can't create table `coeus`.`#sql-c8e0_78` (errno: 150 "Foreign key constraint is incorrectly formed")
  * Caused by: com.googlecode.flyway.core.command.FlywaySqlScriptException: Error executing statement at line 3: ALTER TABLE CONTACT_USAGE
  * Lance Speelmon on Fri, 30 May 2014 15:15:24 -0700 [View Commit](../../commit/92399467a03c32a9a96058496ccc9bdeb02e271c)
* Create a little gap in integer sequence to allow for missed migrations (that need to run before cleanup)
  * Lance Speelmon on Mon, 2 Jun 2014 18:06:20 -0700 [View Commit](../../commit/d7a75ab58b7f0a1b2026ff98909febfd8bd0c920)
* Added missing SQL files from svn (i.e. missing from git branch)
  * https://jira.kuali.org/browse/KRACOEUS-7306
  * Lance Speelmon on Mon, 2 Jun 2014 19:20:26 -0700 [View Commit](../../commit/087864c1d472434dad4a5ff7df12ce403d450825)
* Get the code more ready for real prod usage by CMU
  * Lance Speelmon on Wed, 4 Jun 2014 14:00:08 -0700 [View Commit](../../commit/b4e6f408723168eee4e1065827bc7ee78707c705)
* Move CMU tools to subdirectory
  * Lance Speelmon on Thu, 5 Jun 2014 14:14:29 -0700 [View Commit](../../commit/63c3aaa9a1ae2140a2c1bd40200733451776de19)
* Add command line parsing to the CMU CSV->XML conversion script
  * Lance Speelmon on Thu, 5 Jun 2014 15:08:29 -0700 [View Commit](../../commit/9dde00d257bbac197856c007149812f629ce1ef3)
* Adding preprocessing method to handle cleanup of the CSV to a tempfile prior to parsing

  * Conflicts:
	tools/CMU/convert_CSV_XML.rb
  * Przybylski 중광 on Thu, 5 Jun 2014 17:38:52 -0700 [View Commit](../../commit/f4b5ac59e5259005c6e4eb7d44f6b35df43a71c2)
* Renamed CE --> CX
  * Lance Speelmon on Mon, 9 Jun 2014 15:00:54 -0700 [View Commit](../../commit/4c2583321a54f577a281b5b114caec179f450185)
* Also delete from KC Extended Attributes when disabling users
  * Lance Speelmon on Mon, 9 Jun 2014 15:29:06 -0700 [View Commit](../../commit/97955516aa2ef1d619488c9e369baf6c0b56e1df)
* Added a script to load units (without rates)
  * Lance Speelmon on Wed, 11 Jun 2014 13:16:08 -0700 [View Commit](../../commit/ecb173cd4791909eef32c3673c2ffa1122c75f12)
* Include protocol_units when removing protocols
  * Lance Speelmon on Wed, 11 Jun 2014 13:16:45 -0700 [View Commit](../../commit/a0c29b1db700fd062cca1d4827f76214b1bec04f)
* Included some more tables in data cleanup script
  * Lance Speelmon on Tue, 17 Jun 2014 11:04:51 -0700 [View Commit](../../commit/8f01de57e3141000044945e4e34ba7a1247e7620)
* chmod +x ruby scripts
  * Lance Speelmon on Tue, 17 Jun 2014 11:06:25 -0700 [View Commit](../../commit/f68170c6395a67411192a59a43cb732362d2692d)
* Added a ruby script to load iacuc_species table
  * Lance Speelmon on Tue, 17 Jun 2014 14:20:55 -0700 [View Commit](../../commit/922781b97bcc6fc4810f8efb6f178eb8c27a6494)
* Adding column to store file content

  * fixes rSmart/issues#244
  * Przybylski 중광 on Thu, 19 Jun 2014 13:42:04 -0700 [View Commit](../../commit/da1913269a237d8fda5e7d50ca152423f3ca5dec)
* Added delete_questionnaires.sql
  * Lance Speelmon on Sat, 21 Jun 2014 09:51:41 -0700 [View Commit](../../commit/3fdc2d7df5b4408bac0d3276ebf48359ae0facff)
* Renamed SQL file to have a better name
  * Lance Speelmon on Mon, 23 Jun 2014 15:00:27 -0700 [View Commit](../../commit/7ba0845618c4b1f94b1f9979ed1f8b7f53f8bbaf)
* Modified workflow routing to place OSPInitial as the first node in the approval routing.
  * Lance Speelmon on Mon, 30 Jun 2014 13:14:33 -0700 [View Commit](../../commit/53db660f97dbf470e0c5c9da2f7047015cf5f77c)
* Added some notes on how to deactivate demo users
  * Lance Speelmon on Mon, 30 Jun 2014 14:46:56 -0700 [View Commit](../../commit/af592508b25705a912229c52493df3cdb44487e0)
* Renaming table seq_proposal_notepad_idn to seq_proposal_notepad_id to fix conflicts with ORM.

  * fixes rSmart/issues-95
  * Przybylski 중광 on Wed, 2 Jul 2014 11:34:00 -0700 [View Commit](../../commit/26099e3f0c3ca9295458100d1e5d944ea9b2e19c)
* Fixes: FlywayException: Wrong migration name format: V201407021132_KC_TBL_SEQ_PROPOSAL_NOTEPAD_IDN.sql(It should look like this: V1_2__Description.sql)
  * Lance Speelmon on Wed, 2 Jul 2014 15:01:05 -0700 [View Commit](../../commit/ca1c8a82f7e1db651184d112978d234c682e550a)
* Do not disable system accounts: kr, admin, notsys, kc
  * Lance Speelmon on Mon, 14 Jul 2014 13:49:13 -0700 [View Commit](../../commit/481a125e63210dc4ce2a060cb2b55b2431b02bca)
* One time SQL migration to reactivate system accounts: kr, admin, notsys, kc
  * Lance Speelmon on Mon, 14 Jul 2014 13:52:18 -0700 [View Commit](../../commit/45c5792df6b68b0c3ba1e9938e6e29ea890f7bfb)
* Added a FIXME note to refactor Questionnaire "deletion"
  * Lance Speelmon on Wed, 16 Jul 2014 16:56:46 -0700 [View Commit](../../commit/adec8bfb492e20a24df75e0fb085b35c80b72771)
* Started work on a new generic CSV --> HR XML conversion.
  * Lance Speelmon on Wed, 16 Jul 2014 16:57:53 -0700 [View Commit](../../commit/a70a97de95f9095985b912e2ac12d9ad176a3bf0)
* only add an appointment if it exists
  * Lance Speelmon on Thu, 17 Jul 2014 09:40:34 -0700 [View Commit](../../commit/18acd45208c4902bb333edb4527b6ffd3c5aafdf)
* also delete from unit_administrator and make the text parsing a little more forgiving
  * Lance Speelmon on Thu, 17 Jul 2014 10:13:27 -0700 [View Commit](../../commit/04e1695230f655f7a238af9168d6e662da6a8ee1)
* updated remove_organizations.sql for 5.2.1 schema
  * Lance Speelmon on Mon, 21 Jul 2014 12:23:23 -0700 [View Commit](../../commit/bdd97198d258c9ce7fcda8387f9894a549627c67)
* Fixed single quote escaping for unit_number (strange but true)
  * Lance Speelmon on Tue, 22 Jul 2014 16:53:15 -0700 [View Commit](../../commit/c0691d738f5a98d579b741e0441615ec910f9749)
* load_unit now required to delete from award
  * Lance Speelmon on Tue, 22 Jul 2014 17:00:10 -0700 [View Commit](../../commit/0abce8beb10962d938e55e4b8c27355a2544844d)
* Also +delete from committee when loading units
  * Lance Speelmon on Tue, 22 Jul 2014 17:06:36 -0700 [View Commit](../../commit/f9126f487edfa0aabb9fc25e27a1dff843ece441)
* added a few *more* tables to empty in load_unit
  * Lance Speelmon on Tue, 22 Jul 2014 19:54:36 -0700 [View Commit](../../commit/4769eb885d542ced049f52071bc83217feee71db)
* Major refactoring of load unit and rolodex; extract common behaviors to a library
  * Lance Speelmon on Thu, 24 Jul 2014 13:20:48 -0700 [View Commit](../../commit/51c29022024feca9102dee221aeb71d5c6f4b23a)
* Added a parse_email_address! method to library
  * Lance Speelmon on Thu, 24 Jul 2014 13:37:41 -0700 [View Commit](../../commit/6a8d8552cabd4939dd49fbdcd7329d4c6137d4ca)
* Added a default option to parse string; some lightweight code style refactoring for readability; remove opt[:line] -> not needed
  * Lance Speelmon on Thu, 24 Jul 2014 14:45:58 -0700 [View Commit](../../commit/310671d92f5a9142426842f4bf9a69d84ce5d0fb)
* Fixed faulty logic in parse_flag method. Improved exception handling output.
  * Lance Speelmon on Thu, 24 Jul 2014 16:20:16 -0700 [View Commit](../../commit/636023ea02f968a7d9f1ede9990208e802dec812)
* Refactored load_units.rb to take advantage of new common library
  * Lance Speelmon on Thu, 24 Jul 2014 16:33:49 -0700 [View Commit](../../commit/12ddf60adb515e6e1eb5c0bc73a1d658e6ecc361)
* Refactored command line parsing into a common method
  * Lance Speelmon on Thu, 24 Jul 2014 18:03:10 -0700 [View Commit](../../commit/a9d6b2ff0bd98b4b00541e47174a5710e0e8fb16)
* move transform_CSV_to_XML.rb alongside other ruby scripts
  * Lance Speelmon on Thu, 24 Jul 2014 20:08:55 -0700 [View Commit](../../commit/cfc3780cff58b4e21aab9aec328ec207bb4139d9)
* a little house cleaning and directory sanity
  * Lance Speelmon on Thu, 24 Jul 2014 20:30:02 -0700 [View Commit](../../commit/fd60d86e252f62d33683479f273073394ed8a152)
* instead of duplicating validation rules, just validate resulting XML against the XSD schema! dur
  * Lance Speelmon on Thu, 24 Jul 2014 21:13:47 -0700 [View Commit](../../commit/25196d0d5ac3f0aab2b5f81df2eff25983e480b2)
* Introduced Gemfile and bundler to project
  * Lance Speelmon on Fri, 25 Jul 2014 15:41:57 -0700 [View Commit](../../commit/f5c93a1007359a2bc4f98408783f08f2039440d8)
* started introducing specs
  * Lance Speelmon on Sat, 26 Jul 2014 12:32:32 -0700 [View Commit](../../commit/8a6b1a9fea33db5378a377f4da9f633eea557a89)
* added parse_flag spec
  * Lance Speelmon on Sat, 26 Jul 2014 13:04:18 -0700 [View Commit](../../commit/a1883e8bdf35fb0b008e391a6ceea0a0c4c05a67)
* added parse_rolodex_id! spec
  * Lance Speelmon on Sat, 26 Jul 2014 14:12:47 -0700 [View Commit](../../commit/28854a16ade588c9c3d78162daeb824e4030d7f8)
* added parse_country_code! spec
  * Lance Speelmon on Sat, 26 Jul 2014 14:21:09 -0700 [View Commit](../../commit/075691eed8df31046ff4b296a05c11a94ce56f56)
* added parse_state! spec
  * Lance Speelmon on Sat, 26 Jul 2014 15:56:18 -0700 [View Commit](../../commit/ac0fe334fc662bb6328d6c249c2b1e21bd285877)
* added parse_sponsor_code! spec
  * Lance Speelmon on Sat, 26 Jul 2014 16:16:23 -0700 [View Commit](../../commit/8aee27139511d1a7951c63f3b0601e269679b99f)
* added parse_postal_code! spec
  * Lance Speelmon on Sat, 26 Jul 2014 16:21:06 -0700 [View Commit](../../commit/41e366dc356c575958ea32e31ae92884fbb68224)
* added parse_owned_by_unit! spec
  * Lance Speelmon on Sat, 26 Jul 2014 16:26:23 -0700 [View Commit](../../commit/0eaa3a5d265cf132f18237d93e4ed8803e38b5e0)
* added parse_actv_ind! spec
  * Lance Speelmon on Sat, 26 Jul 2014 16:40:32 -0700 [View Commit](../../commit/b93dccd6ce351d4a0680eca733072aad7a699d70)
* added parse_email_address! spec
  * Lance Speelmon on Sat, 26 Jul 2014 17:01:58 -0700 [View Commit](../../commit/fc9d3378c4088ce44e87dc7a584cbf0a5844cfad)
* added parse_principal_id spec
  * Lance Speelmon on Sat, 26 Jul 2014 17:42:52 -0700 [View Commit](../../commit/c9cf40d06cbe9418fdbddaae03fd4caeb4c1ac38)
* added parse_prncpl_nm spec
  * Lance Speelmon on Sat, 26 Jul 2014 17:46:26 -0700 [View Commit](../../commit/b73699a956e9d1c3d157ba0e1e486088de53293b)
* added parse_emp_stat_cd spec
  * Lance Speelmon on Sat, 26 Jul 2014 17:57:12 -0700 [View Commit](../../commit/a2184c76bc1061a04d8160f013c832b4cae6f2f4)
* improved the data validation for parse_prncpl_nm
  * Lance Speelmon on Sat, 26 Jul 2014 20:45:46 -0700 [View Commit](../../commit/d8bcddcdf3bc43bc2d9c254a24479c2387e6240d)
* added parse_emp_typ_cd spec
  * Lance Speelmon on Sat, 26 Jul 2014 20:56:13 -0700 [View Commit](../../commit/59dd0c2de0dbd005d17b1dfff95845109d51e6b9)
* now allows for mixed case input for ACTV_IND
  * Lance Speelmon on Sat, 26 Jul 2014 21:45:43 -0700 [View Commit](../../commit/a03f9943fa222e312f561c666e94083d2bbd24f3)
* add support for case insensitive flag matching
  * Lance Speelmon on Sat, 26 Jul 2014 22:09:12 -0700 [View Commit](../../commit/cd06f262e8beaaf20e22b290a76e788a47698906)
* added support for Regexp in valid_value method
  * Lance Speelmon on Sat, 26 Jul 2014 23:02:54 -0700 [View Commit](../../commit/1c029be9fbab8e327eee5c191a79df9e17ff75e9)
* Refactored parse_emp_stat_cd and parse_emp_typ_cd to use Regexp
  * Lance Speelmon on Sat, 26 Jul 2014 23:13:40 -0700 [View Commit](../../commit/ecdd9798d90a4b0679a5cf7211597dcc033e419c)
* introduce new common parse_string! method
  * Lance Speelmon on Sun, 27 Jul 2014 00:34:43 -0700 [View Commit](../../commit/36216d254e339318985123f9267dffa2754f2764)
* enhanced parse_string to perform optional validation and removed parse_flag
  * Lance Speelmon on Sun, 27 Jul 2014 00:55:13 -0700 [View Commit](../../commit/b1df33d1d96f1ec02e5d96e0002326c9fb306938)
* parse_flag makes a more generic return; and implemented code coverage with simplecov
  * Lance Speelmon on Sun, 27 Jul 2014 09:46:17 -0700 [View Commit](../../commit/17392bb866c49479c5bbe8ffeb220ef10b708520)
* refactored parse_emp_stat_cd and parse_emp_typ_cd to use new parse_flag method
  * Lance Speelmon on Sun, 27 Jul 2014 09:53:49 -0700 [View Commit](../../commit/556bf0d22aa78c58550a2dc3a2c42f8c501d18eb)
* added specs and impls for parse_address_type_code, parse_name_code, parse_prefix, and parse_suffix
  * Lance Speelmon on Sun, 27 Jul 2014 10:32:01 -0700 [View Commit](../../commit/f6275e19a3893ca021ef92690e7d8384ee47d60f)
* added parse_phone_type
  * Lance Speelmon on Sun, 27 Jul 2014 10:38:20 -0700 [View Commit](../../commit/d580eede668d5af251bb1c1985a112dd2c9902d1)
* added spec and impl for parse_phone_number
  * Lance Speelmon on Sun, 27 Jul 2014 10:51:17 -0700 [View Commit](../../commit/a2531b6f9c06636804fbaae89f89e2e878f4a6f4)
* added spec and impl for parse_email_type
  * Lance Speelmon on Sun, 27 Jul 2014 10:53:56 -0700 [View Commit](../../commit/dbe023b03f692d64b3b3804cb48a72acd0c4e39f)
* introduced new more generic parse_email_address method
  * Lance Speelmon on Sun, 27 Jul 2014 11:05:03 -0700 [View Commit](../../commit/d99351f74ce19116a84faa674f2abc5f68432830)
* added spec and impl for parse_year
  * Lance Speelmon on Sun, 27 Jul 2014 11:16:12 -0700 [View Commit](../../commit/549431d310a9f9addedc800c475c9813bb98260e)
* added spec and impl for parse_citizenship_type
  * Lance Speelmon on Sun, 27 Jul 2014 11:35:38 -0700 [View Commit](../../commit/792c62b308484ee6dc43dce5ec71d62271295beb)
* added spec and impl for parse_degree_code
  * Lance Speelmon on Sun, 27 Jul 2014 11:49:22 -0700 [View Commit](../../commit/3115060a20f669c0d40debd5f809ceb15d73de2d)
* renamed method parse_prncpl_nm --> parse_principal_name
  * Lance Speelmon on Sun, 27 Jul 2014 11:52:54 -0700 [View Commit](../../commit/a91331b309b418543c069f8eeb1cbeb50837ac6b)
* rename to_bool --> parse_boolean; refactor specs to be more DRY
  * Lance Speelmon on Sun, 27 Jul 2014 12:41:23 -0700 [View Commit](../../commit/6f761383712cb40abae8f6eb28abe039fa4b5499)
* improved data truncation warning messages with before/after previews for convenience
  * Lance Speelmon on Sun, 27 Jul 2014 12:50:24 -0700 [View Commit](../../commit/fda453f7ce631c05eaf08df7d451480366a0ef13)
* redirect STDOUT and STDERR to /dev/null during rspec runs
  * Lance Speelmon on Sun, 27 Jul 2014 12:59:30 -0700 [View Commit](../../commit/e969fe7a0522aacaa4036bb77d698bbd1a87e43d)
* refactored existing scripts to use new CX class; add a sweet XSD schema validation to end of CSV transform
  * Lance Speelmon on Sun, 27 Jul 2014 13:04:42 -0700 [View Commit](../../commit/2790f53201bc7cfa969e1cdef9b721df53ce4d28)
* refactor the CSV transform script to use all available CX parsing methods
  * Lance Speelmon on Sun, 27 Jul 2014 13:37:25 -0700 [View Commit](../../commit/933b193303b5a5991fa2801024e289dfb0d10475)
* better defined parse_integer behavior and implemented parse_float
  * Lance Speelmon on Sun, 27 Jul 2014 14:14:22 -0700 [View Commit](../../commit/f20c653b54974d5f80b0d416bb248d5bb1a4a3a6)
* Started using new parse_float method for baseSalaryAmounte
  * Lance Speelmon on Sun, 27 Jul 2014 14:20:47 -0700 [View Commit](../../commit/e1e7c4d2d99a39bc044d57509f47a21d100444dd)
* add support for more fields to CSV->XML transformation
  * Lance Speelmon on Sun, 27 Jul 2014 14:57:46 -0700 [View Commit](../../commit/a0b901d3d63c46995435ea2bac7d255062ad4ac2)
* added parse_csv_command_line_options spec
  * Lance Speelmon on Sun, 27 Jul 2014 16:28:46 -0700 [View Commit](../../commit/7766326cf6269458d2e740b1e3ecfb1085889609)
* introduce TextParseError exception
  * Lance Speelmon on Sun, 27 Jul 2014 20:13:50 -0700 [View Commit](../../commit/c9e611e1c71d51741a20f8ad37e6b66d877cb726)
* switch to rescue TextParseError from ArgumentError
  * Lance Speelmon on Sun, 27 Jul 2014 20:58:45 -0700 [View Commit](../../commit/ceaea176cad3ada5eb2bc3f1e9d691de34aedd0a)
* added CX.error and CX.warning specs and impls
  * Lance Speelmon on Sun, 27 Jul 2014 21:27:49 -0700 [View Commit](../../commit/27fee7bf117cae88d174c1d3748f27febc55909e)
* moved rescue TextParseError to allow output of error for each line of the input file
  * Lance Speelmon on Mon, 28 Jul 2014 09:47:29 -0700 [View Commit](../../commit/d088616a31c7e48ace2903462c1ee3c4c4bd3db0)
* whitespace only change; reformat files
  * Lance Speelmon on Mon, 28 Jul 2014 09:48:57 -0700 [View Commit](../../commit/90b33cb355e8b874b6a894c5d9b9ec2badde7cf8)
* print the TextParseError message for each row
  * Lance Speelmon on Mon, 28 Jul 2014 09:55:39 -0700 [View Commit](../../commit/4c9ad0dc5ef8ecfc565f10b788e5a6a125f6d3e0)
* whitespace only change; reformat files
  * Lance Speelmon on Mon, 28 Jul 2014 10:25:30 -0700 [View Commit](../../commit/979f43601bef13c9f6caeea8a76106c1da269d64)
* switched to require_relative './lib/CX.rb'
  * Lance Speelmon on Mon, 28 Jul 2014 10:29:26 -0700 [View Commit](../../commit/dc84f44ed67326c931bfd5ae60cd0d3afdcf32ff)
* added missing opt[:name] for parse_principal_id
  * Lance Speelmon on Mon, 28 Jul 2014 10:33:50 -0700 [View Commit](../../commit/54ae3ad42b3cce063dd813240859ef9ee0827299)
* ensure parse_boolean supports use of the :default option
  * Lance Speelmon on Mon, 28 Jul 2014 12:00:27 -0700 [View Commit](../../commit/37032bdda0f8cabe5751e6696c87df89c72d9f28)
* move common dependencies to lib/CX.rb
  * Lance Speelmon on Mon, 28 Jul 2014 12:04:28 -0700 [View Commit](../../commit/297c0c8d2b093d07e8b654bf676167acb6c7e737)
* added parse_integer
  * Lance Speelmon on Mon, 28 Jul 2014 14:09:48 -0700 [View Commit](../../commit/e23121ce5a0f5ccaab3f670df48cfc30202abac7)
* added load_valid_class_report_freq.rb and load_valid_frequency_base.rb
  * Lance Speelmon on Mon, 28 Jul 2014 14:10:25 -0700 [View Commit](../../commit/7e23cb2850d738550db3676835b0eb0437bcc9cf)
* replaced column name placeholders with real names
  * Lance Speelmon on Mon, 28 Jul 2014 18:19:01 -0700 [View Commit](../../commit/10be9f94c25bd89087cf9d4d2725f5cb18d875de)
* fixed a small error reporting bug in valid_value
  * Lance Speelmon on Mon, 28 Jul 2014 18:24:00 -0700 [View Commit](../../commit/c6ee0ebaeb379b200065e8ce7f7703d8f18df598)
* parse_boolean now returns nil for an empty string
  * Lance Speelmon on Mon, 28 Jul 2014 18:42:49 -0700 [View Commit](../../commit/e5f4cbaf12fd246fab0c9f3b435db9c038fc37b4)
* complete refactor of transform_CSV_to_XML.rb
  * Lance Speelmon on Mon, 28 Jul 2014 18:44:04 -0700 [View Commit](../../commit/ab4ca153ef95751ea3e1f946b7ff69432da8971e)
* make :strict :length validation the default behavior
  * Lance Speelmon on Mon, 28 Jul 2014 20:05:57 -0700 [View Commit](../../commit/c24381cf4e365b5a3cdd54f6cec20fb94114b4b1)
* simplified error handling
  * Lance Speelmon on Mon, 28 Jul 2014 20:26:19 -0700 [View Commit](../../commit/6e48715db0794a14abf019be34ade8c73a619b25)
* refactored scripts to account for making :strict :length validation the default behavior
  * Lance Speelmon on Mon, 28 Jul 2014 20:40:07 -0700 [View Commit](../../commit/1e3838bbc0c9a61011d7f474bd0a068641816c82)
* improve test coverage a tiny bit
  * Lance Speelmon on Mon, 28 Jul 2014 20:41:56 -0700 [View Commit](../../commit/ae9baf7e7374686327bddfaba5327ddb4a1d03f2)
* removed some code that was not needed to meet specs
  * Lance Speelmon on Mon, 28 Jul 2014 20:43:58 -0700 [View Commit](../../commit/7cc99106f7d0f34507c226c2b99f375b8357ea2f)
* Rebase and add UnitNode Class with hierarchy validations

  * Fix some white spacing
  * amastov on Tue, 29 Jul 2014 10:32:26 -0700 [View Commit](../../commit/0df448c93e8aeaaef2968c3ec6c8853f07848b9e)
* Add back require 'graph'  * Adam Mastov on Tue, 29 Jul 2014 11:19:03 -0700 [View Commit](../../commit/306d9f14e44eeac5a5631e9a71906dc2f7e10e83)
* replaced all instances of '+=' with concat(); I experienced some weird assignment behavior that I did not expect, so this seems to work in \*all\* cases
  * Lance Speelmon on Tue, 29 Jul 2014 11:28:03 -0700 [View Commit](../../commit/8112392b717c96643e83d7f935f4650bc6aebbf0)
* deactivate demo users in KRIM_ROLE_MBR_T and KRIM_GRP_MBR_T
  * Lance Speelmon on Tue, 29 Jul 2014 12:06:44 -0700 [View Commit](../../commit/dfe6eeab8929840d19349fd8186b558df67670af)
* refactor multiple SQL statements into just two for 'deactivate demo users in KRIM_ROLE_MBR_T and KRIM_GRP_MBR_T'
  * Lance Speelmon on Tue, 29 Jul 2014 12:19:05 -0700 [View Commit](../../commit/13fcb51477fe9625446809d8e118810d38970f51)
* fixed a small typo in SQL statement
  * Lance Speelmon on Tue, 29 Jul 2014 12:57:26 -0700 [View Commit](../../commit/c3defcc98d94bed7d6a5fff5061f1348d53af138)
* whitespace only change; reformat SQL for better readability
  * Lance Speelmon on Tue, 29 Jul 2014 12:58:38 -0700 [View Commit](../../commit/18a32c0326a0b639ed2725b18c56afc9bb06afae)
* ROLODEX_ID should be parsed as an Integer rather than String
  * Lance Speelmon on Thu, 31 Jul 2014 15:20:24 -0700 [View Commit](../../commit/f266eb9972a253724d1394b56e1289e954cc13b0)
* added a unit_administrator loading script
  * Lance Speelmon on Thu, 31 Jul 2014 16:33:02 -0700 [View Commit](../../commit/2ae74dcae757855681b85b34c006a93e71fa461d)
* updated rolodex temp table logic
  * Lance Speelmon on Fri, 1 Aug 2014 13:20:22 -0700 [View Commit](../../commit/5e4c9d4a057c67894cda8897c3ad34759e86b3d5)
* also delete from award_template when loading sponsor
  * Lance Speelmon on Fri, 1 Aug 2014 15:25:01 -0700 [View Commit](../../commit/55fac32a2c010dbdee2150694f5944c8ff79fa74)
* Moved PeopleFlows to the 2nd to last node. Fixes https://github.com/rSmart/issues/issues/353
  * Lance Speelmon on Sat, 2 Aug 2014 09:00:10 -0700 [View Commit](../../commit/7a125e6bea414f341466df59a206fcaf16dacef5)
* start conversion to rsmart_toolbox gem
  * Lance Speelmon on Sun, 3 Aug 2014 11:34:09 -0700 [View Commit](../../commit/d6516ce995d16c9f7aa88c8e1de974543c6216c2)
* bind to a release rsmart_toolbox gem
  * Lance Speelmon on Sun, 3 Aug 2014 12:30:52 -0700 [View Commit](../../commit/0d350f175fe33ef186025d0eab5cec7555735999)
* renamed:    remove_all_transactional_data.sql -> remove_transactional_data_only.sql
  * Lance Speelmon on Mon, 4 Aug 2014 09:08:08 -0700 [View Commit](../../commit/16062f9baf5070a24f301dc610a598e95f93b842)
* started separating concerns and fixed some foreign key constraints
  * Lance Speelmon on Mon, 4 Aug 2014 10:01:17 -0700 [View Commit](../../commit/8ca95bfad41cde552416c7360ca1456ef45d9706)
* reconciled award table cleanup with scrub manifest
  * Lance Speelmon on Mon, 4 Aug 2014 10:44:31 -0700 [View Commit](../../commit/b3811f5e7450678337f8b3ffe255c75a8e74f95a)
* added cleanup of krns_pessimistic_lock_t to scrub scripts
  * Lance Speelmon on Mon, 4 Aug 2014 10:45:58 -0700 [View Commit](../../commit/4e4cf4d370804147b959365b91e47b52b6d592d0)
* reconciled budget tables with scrub script manifest
  * Lance Speelmon on Mon, 4 Aug 2014 10:52:27 -0700 [View Commit](../../commit/9c19e5a753d692ae6a09eea1d9712d3fea4fbada)
* reconciled eps_proposal tables with scrub script manifest
  * Lance Speelmon on Mon, 4 Aug 2014 12:04:48 -0700 [View Commit](../../commit/d8df4b370fe7b1089f7646f2bcf5a9606b639ce1)
* added workflow document cleanup to scrub scripts'
  * Lance Speelmon on Mon, 4 Aug 2014 12:19:04 -0700 [View Commit](../../commit/5cfa360dde21aa50080466cc50bc7f706fda1e42)

##cx_data_migration-5.2.0.0
* initial commit
  * dgillman on Tue, 30 Jul 2013 13:20:11 -0700 [View Commit](../../commit/922dc12ce3e100bf9159ae363d4e14ca543e1382)
* Configure rSmart maven deploy for artifact
  * Lance Speelmon on Tue, 6 Aug 2013 08:55:39 -0700 [View Commit](../../commit/bf916f70bd49b1b42f93a42dc936f1c776970307)
* Upgraded s3 wagon
  * Lance Speelmon on Tue, 6 Aug 2013 09:01:36 -0700 [View Commit](../../commit/c6943f54cd982eea89e7258721551997c8107535)
* Reformat code only
  * Lance Speelmon on Wed, 30 Oct 2013 14:45:19 -0700 [View Commit](../../commit/ff3856545b2fe8693eba4807961ff4b3c8ae32d4)
* Converted custom string data types into generic xs:string types (not tested)
  * Lance Speelmon on Wed, 30 Oct 2013 16:02:34 -0700 [View Commit](../../commit/f9fc4c035f595086c133a86a7b44e79d368c6ca1)
* Refactored schema and code to support versioning and namepsace
  * Lance Speelmon on Tue, 5 Nov 2013 09:21:11 -0700 [View Commit](../../commit/6376c02bbd95944e42a46a4af28235ed77dcdb8e)
* Just a little cleanup and renaming
  * Lance Speelmon on Tue, 5 Nov 2013 13:31:31 -0700 [View Commit](../../commit/799e8c93510456c1ceb212f8bdd7ee8e359c2d68)
* Fixes: The file cannot be validated as the XML definition "/Develop/k2/rsmart/kc_hr_rest/src/main/java/log4j.dtd (No such file or directory)" that is specified as describing the syntax of the file cannot be located.	log4j.xml	/kc_hr_rest/src/main/resources	line 2	XML Problem
  * Lance Speelmon on Tue, 5 Nov 2013 13:43:32 -0700 [View Commit](../../commit/e4120ba96dc058a103e5625e9eda5fe8443b3eae)
* Some light refactoring and some major addition of unit test coverage (although more needed)
  * Lance Speelmon on Fri, 6 Dec 2013 14:11:18 -0700 [View Commit](../../commit/920c19bad9a2f888f83377d0c318929c6dd13c89)
* Added initial support for addresses
  * Lance Speelmon on Mon, 9 Dec 2013 17:17:11 -0700 [View Commit](../../commit/11531bc931a8cbd62b87e38ab888e792de426917)
* Added support for KC extended attributes
  * Lance Speelmon on Wed, 11 Dec 2013 10:48:39 -0700 [View Commit](../../commit/32dfe47222331b9e9428844e1c2c676cc2338a3a)
* Added support for middleName
  * Lance Speelmon on Wed, 11 Dec 2013 11:15:04 -0700 [View Commit](../../commit/8870032e1f6c9b671447aa20aa07d73eaee61cfd)
* Added support for degrees
  * Lance Speelmon on Wed, 11 Dec 2013 14:59:49 -0700 [View Commit](../../commit/ccd2339ef7a415a0e6ae8e8864b401172bc3d113)
* Renamed attribute s/addressType/addressTypeCode and made some address fields required
  * Lance Speelmon on Wed, 11 Dec 2013 15:24:30 -0700 [View Commit](../../commit/e26e88eae3fc375ff3c8c965da9a80f4404e39bb)
* Some lightweight refactoring
  * Lance Speelmon on Wed, 8 Jan 2014 15:46:26 -0700 [View Commit](../../commit/68436f22d8a9374f6c265578139cc98138327f74)
* improved import error output; correctly returning 400 errors on bad input; added unit test for 400 errors
  * duffy@rsmart.com on Fri, 31 Jan 2014 14:31:42 -0700 [View Commit](../../commit/e43db3ef8bb9b935a82197879ff7838ee8f56769)
* refactored and completed import logic; implemented framework for merging imported BOs; expanded test coverage

  * made tests pass again after refactor

  * added file which had not been saved before commit

  * added cache flush to correct for lack of invalidation on businessObjectService.save()

  * added appointments to import

  * added tests, refactored for readability/maintainability, documented

  * added tests, refactored for readability/maintainability, documented

  * removed dependency on commons-logging

  * added hibernate bean validation framework and initial annotations around some model objects

  * renamed JAX-RS collection objects to *Collection

  * unified logic for merging dependent business objects on to EntityBo

  * corrected NPE based on missing contactinfo list; eliminated unit tests for logic now too deep to be mockable

  * corrected creation of ContactInfo business object

  * addressed smoke test errors in update; implemented logic to deal with updates to partial dependency Employment -> Affiliation

  * added files missed in last commit

  * added even more files missed in last commit
  * duffy@rsmart.com on Wed, 5 Feb 2014 08:36:54 -0700 [View Commit](../../commit/2a9ce244a6a7cdb1d36a36820d96435fc1cffc0d)
* updated Javadoc and comments
  * duffy@rsmart.com on Fri, 14 Feb 2014 14:33:27 -0700 [View Commit](../../commit/cee39a34a79a45e4255a887853bd05c5384e1860)
* created authentication filter

  * created authentication filter

  * added servlet-api to support authentication ServletFilter class
  * duffy@rsmart.com on Mon, 17 Feb 2014 16:17:41 -0700 [View Commit](../../commit/55d329c3df347594a8aa2faf5cc0f5c5b8051b8f)
* added validation of new records and merged logic of KC and Rice dependent entity imports
  * duffy@rsmart.com on Wed, 19 Feb 2014 11:58:57 -0700 [View Commit](../../commit/ee86b1adba404b1418bfc8d8d00ed53c40369a2d)
* converted import to an async operation; added JDBC-backed status tracking to support this; renaming/refactoring of XSD

  * renamed 'manifest' to 'import' universally

  * completed async processing - need more smoke test

  * Implemented JDBC-backed status servie

  * corrected errors in logic to abort import
  * duffy@rsmart.com on Fri, 21 Feb 2014 09:01:41 -0700 [View Commit](../../commit/21c1c6360062cb23a77c0954fd46c7b61670e54a)
* implemented email report at end of import
  * duffy@rsmart.com on Mon, 10 Mar 2014 10:39:45 -0700 [View Commit](../../commit/bb2a5af241339eb4327ca72bda422592cf935df1)
* added null checks and revised XSD to solve smoke-test problems from client import

  * null checks before attempting date conversion within extended attributes
  * duffy@rsmart.com on Tue, 11 Mar 2014 10:12:05 -0700 [View Commit](../../commit/e2e5fe254870f0991c5faa902cb36148468c264d)
* Implemented fixed user, pw, and runas user configurable via kc-config.xml; more null checks to fix smoke test errors
  * duffy@rsmart.com on Tue, 11 Mar 2014 14:26:43 -0700 [View Commit](../../commit/c175d2f17f0f64db023dd8645c35be5f640834f1)
* improved error message for missing Unit
  * duffy@rsmart.com on Fri, 14 Mar 2014 17:33:45 -0700 [View Commit](../../commit/87c4c88c2e743de7cc1ca502becdcc021a0a9471)
* changes to match PR feedback
  * duffy@rsmart.com on Sun, 16 Mar 2014 17:42:14 -0700 [View Commit](../../commit/58792d3c84b2389358bdb7808e48599b55005d87)
* corrected name of authentication header for HTTP Basic Auth
  * duffy@rsmart.com on Wed, 19 Mar 2014 14:33:56 -0700 [View Commit](../../commit/94ce2548b431116686b7f869c36bb494a4e47371)
* Switch import_status.detail data type to TEXT to allow for larger detail messages.
  * Lance Speelmon on Wed, 19 Mar 2014 15:19:47 -0700 [View Commit](../../commit/d94961a2d5d955dea731e467eeed15b19d558be7)
* Switch exception column data type to BLOB
  * Lance Speelmon on Wed, 19 Mar 2014 15:56:00 -0700 [View Commit](../../commit/480e667a18e2a2d3db9ab0486c1b3dd8093dbf34)
* corrected linkage between principal and entity for a newly added person
  * duffy@rsmart.com on Wed, 19 Mar 2014 16:20:33 -0700 [View Commit](../../commit/8ac97ce44faaff1a5097e4834942f2e66614ab64)
* entities now use native ID generation; error checks added to avoid principal ID collision
  * duffy@rsmart.com on Fri, 21 Mar 2014 14:04:21 -0700 [View Commit](../../commit/29706819aaaff3e15571bc4e8f50d8dfa444c97a)
* Included Duffy's README
  * Lance Speelmon on Mon, 24 Mar 2014 13:35:03 -0700 [View Commit](../../commit/54e5aaf264906f0a3bf66dcf36e7ba391513e555)
* Updated scm info for mvn release
  * Lance Speelmon on Mon, 24 Mar 2014 14:30:38 -0700 [View Commit](../../commit/af8a93c1b4429678d61069e7ef54a3a18904ecd7)
* mvn release peform
  * Lance Speelmon on Mon, 24 Mar 2014 14:33:34 -0700 [View Commit](../../commit/825c1cc1c2aaa53834cf61c2d888d370e20fb1ff)
* corrected JSON results for import status
  * duffy@rsmart.com on Tue, 25 Mar 2014 15:58:56 -0700 [View Commit](../../commit/a6a1a84bd133d1f883cf33d811ecd90b06f3f869)
* Create a new custom data type for affiliationType. Make some attributes not required. Make more booleans default to true to allow for easier usage with a single item in collection.
  * Lance Speelmon on Tue, 25 Mar 2014 19:38:56 -0700 [View Commit](../../commit/75dc7dec1477f99ec48aba485ea8177046e365e1)
* Added email address regex validation
  * Lance Speelmon on Tue, 25 Mar 2014 19:56:59 -0700 [View Commit](../../commit/eb026decdc55846bc4a1efe2a044c283febddb9a)
* Create a 'year' custom data type for better validation
  * Lance Speelmon on Tue, 25 Mar 2014 20:06:55 -0700 [View Commit](../../commit/2e30f5fc1615498b0d6cc141019b217d7f08d98f)
* Minor update for campus code documentation
  * Lance Speelmon on Tue, 25 Mar 2014 20:31:09 -0700 [View Commit](../../commit/a5120df9a8c9a4dd994e7404245c4fd94a1ed953)
* formatted HTML email; removed unused XSD definition

  * removed unneeded XSD define
  * duffy@rsmart.com on Wed, 26 Mar 2014 06:50:43 -0700 [View Commit](../../commit/0119a5a596cbf06fb7888c74f936202bb3e67ea9)
* Added degreeCode validation and refined year regex
  * Lance Speelmon on Wed, 26 Mar 2014 13:46:54 -0700 [View Commit](../../commit/ec6063a3d8e605931f32bc10ab76f9af46c075e9)
* Added a custom data type for principalName
  * Lance Speelmon on Wed, 26 Mar 2014 14:25:09 -0700 [View Commit](../../commit/bab88088ce903c38cb838309e9dfa82abbaab0f9)
* Updated comment about principalId equality; i.e. '0002' equals '2'
  * Lance Speelmon on Wed, 26 Mar 2014 14:43:26 -0700 [View Commit](../../commit/2ffd3388d52282a0d225fd41f62c85094abfa546)
* Updated valid values comment for APPOINTMENT_TYPE
  * Lance Speelmon on Wed, 26 Mar 2014 16:03:01 -0700 [View Commit](../../commit/ea3b6d2c339345c61b831f34966b068a9423c708)
* Eliminate errors from Eclipse project build
  * Lance Speelmon on Wed, 26 Mar 2014 17:56:16 -0700 [View Commit](../../commit/2af666ce4ca0cf518281bb2c62536b3f9923154d)
* Fixes NullPointerException at com.rsmart.kuali.coeus.hr.service.impl.HRImportServiceImpl.deactivatePeople(HRImportServiceImpl.java:154)
  * Lance Speelmon on Wed, 26 Mar 2014 21:52:02 -0700 [View Commit](../../commit/47cc445767d89fb688dab65539b4d8773ab4f292)
* Altered validation annotations to prevent conflicts with XSD changes
  * duffy@rsmart.com on Tue, 1 Apr 2014 16:47:06 -0700 [View Commit](../../commit/d9e602349def5ec6e07ffa08b471d54d6453f538)
* Allow for empty name prefix or suffix. Would be useful to remove a prefix or suffix from a name.
  * Lance Speelmon on Tue, 1 Apr 2014 17:49:42 -0700 [View Commit](../../commit/2358e23b6a4651a282351efd41d177f39cf66c0b)
* Allow for email address characters '@' and '.' in principalName
  * Lance Speelmon on Tue, 1 Apr 2014 18:28:39 -0700 [View Commit](../../commit/d718e02c2c6073026c24296011778d1d4346a997)
* Fixes #14 : ConstraintViolationException: Column 'CITIZENSHIP_TYPE_CODE' cannot be null
  * Lance Speelmon on Wed, 2 Apr 2014 07:35:16 -0700 [View Commit](../../commit/a4520fa3269b98986cb9ec6119b787ef06b01042)
* Update README.md  * Lance Speelmon on Wed, 2 Apr 2014 07:55:08 -0700 [View Commit](../../commit/a9752e539ffdff9ba16c6a5683a9836687df0a6b)
* Added validation for citizenshipType and documented valid values
  * Lance Speelmon on Wed, 2 Apr 2014 08:14:37 -0700 [View Commit](../../commit/d072a1bbb4172b0094cb053d0dbfce26f1c483d9)
* import now uses principal name as key. principalId and entityId are optional and generated when missing.
  * duffy@rsmart.com on Wed, 2 Apr 2014 11:45:05 -0700 [View Commit](../../commit/0f45f1338d05ad1c71108259e7e37b5c30fc75b5)
* Removed validation of affiliationType and added comments re: krim_afltn_typ_t
  * Lance Speelmon on Thu, 3 Apr 2014 14:49:44 -0700 [View Commit](../../commit/a5b09d1be059aa9d0ca03d3f6768032ab78ee371)
* revised import to treat entity and pricipal IDs as option
  * duffy@rsmart.com on Thu, 3 Apr 2014 23:39:04 -0700 [View Commit](../../commit/058df4f0f7b0a10561b2bec3bfe384fb6dd4d900)
* Corrected unit test expectation to match changes to validation
  * duffy@rsmart.com on Fri, 4 Apr 2014 10:13:26 -0700 [View Commit](../../commit/7132ebdb3a171bd5a1773e057d268fc98f5b7763)
* corrected copy/paste error in EntityEmploymentBoAdapter.compareBOProperties
  * duffy@rsmart.com on Fri, 4 Apr 2014 11:06:32 -0700 [View Commit](../../commit/967f3f6f0c472dc2f0d0be2afde8caabd098b124)
* Added principal name to error output to improve feedback to user (fixes #11)
  * duffy@rsmart.com on Fri, 4 Apr 2014 16:57:38 -0700 [View Commit](../../commit/fe495f9622d47b31572750e10a1342470bfc0285)
* Updated principalId conflict error message to reflect that principalName is now the primary key
  * duffy@rsmart.com on Mon, 7 Apr 2014 11:12:03 -0700 [View Commit](../../commit/8f497da12dcbdf61bd29b6db0b17cf3666b9a65e)
* added REST endpoint to retrieve unmanaged principal names; corrected NPE on empty affiliation list uncovered during test (fixes #22)
  * duffy@rsmart.com on Mon, 7 Apr 2014 11:56:23 -0700 [View Commit](../../commit/690d2ba234fa7d6ad1b1de946dfa35135b011abe)
* create StAX parser-based import
  * duffy@rsmart.com on Mon, 7 Apr 2014 16:51:28 -0700 [View Commit](../../commit/e41ea3486513dfa15ecb92c001a585f8a4271d3c)
* Added path for using a StAX parser for more efficient memory footprint (fixes #23)
  * duffy@rsmart.com on Tue, 8 Apr 2014 10:41:28 -0700 [View Commit](../../commit/fcd18d995550e73554026de5768084552cef5aef)
* protected calls to HRImportRecordCollection.iterator().next() to prevent error killing import; added special handling for errors where personName == null (fixes #28)
  * duffy@rsmart.com on Wed, 9 Apr 2014 08:35:08 -0700 [View Commit](../../commit/2df54581de774678415c05564141fe30b3b6f5cc)
* removed finalize method from StAX implementation objects
  * duffy@rsmart.com on Thu, 10 Apr 2014 12:12:06 -0700 [View Commit](../../commit/8762dbbe5ddd0a8161bad37cbe8c96f7c5e36cf0)
* get current with maven plugin updates
  * Lance Speelmon on Mon, 14 Apr 2014 10:24:22 -0700 [View Commit](../../commit/e60ae19741e9bc2b00b3a4509c1e4c0196006b58)
* Switched from GlobalResourceLoader to KraServiceLocator in the hopes of clearing up memory leak
  * Lance Speelmon on Tue, 15 Apr 2014 15:57:23 -0700 [View Commit](../../commit/999703743ec307824e239667efb86e26391ceff9)
* Removed unused imports
  * Lance Speelmon on Tue, 15 Apr 2014 15:58:02 -0700 [View Commit](../../commit/b3fc14c2ae1eb8a7dae740317a278055cdc20213)
* Refine the way JDK source and target versions are specified
  * Lance Speelmon on Thu, 17 Apr 2014 13:29:08 -0700 [View Commit](../../commit/681bd55b2e849f423e775144f266aa6808e49776)
* reverted to using principalId as primary identifier for principals
  * duffy@rsmart.com on Wed, 23 Apr 2014 07:39:40 -0700 [View Commit](../../commit/c06b1b637bdc5f5e659720456d336d1e1e8d1906)
* corrected example import file to conform to return of principalId as primary key
  * duffy@rsmart.com on Thu, 24 Apr 2014 07:20:27 -0700 [View Commit](../../commit/68265cf7f6173490a2b3e83fca16e89f98d0e4fd)
* Upgrade jersey to 1.18
  * Lance Speelmon on Fri, 25 Apr 2014 09:30:07 -0700 [View Commit](../../commit/a6ff8f38935b878caf6b1319a775ddba1b46e5a6)
* Added XSD validation for phone number format
  * Lance Speelmon on Tue, 29 Apr 2014 09:12:56 -0700 [View Commit](../../commit/b1c3af75c90f3594e2622a1a435394a939a4a887)
* provides business object merge adapter the index of the object it is working with. this allows employment records to have a critical field (EntityEmployeeRecordId) set [fixes #35]
  * duffy@rsmart.com on Wed, 30 Apr 2014 00:13:04 -0700 [View Commit](../../commit/6016fb02fd5d778e1d2ad65c6492b0a09b32f951)
* Initial support for Flyway DDL import magic at startup
  * Lance Speelmon on Wed, 7 May 2014 08:58:09 -0700 [View Commit](../../commit/cb9bc2bd0e686e50743898fa2c2ff0e8f54a9c14)
* Added 'IF NOT EXISTS' to SQL table creation logic
  * Lance Speelmon on Wed, 7 May 2014 09:02:54 -0700 [View Commit](../../commit/73c6b930de4f0de639f69b8540664665808efb76)
* git ignore bin/
  * Lance Speelmon on Wed, 7 May 2014 09:12:50 -0700 [View Commit](../../commit/30c1eaa9e3c8a20430f6a833ed93ac906d477d2c)
* Removed now defunct ./src/main/resources/importstatus.sql
  * Lance Speelmon on Wed, 7 May 2014 09:14:08 -0700 [View Commit](../../commit/56a746432790818f28dd795e1be50f4be23b2629)
* Rename database objects to cx_hrapi_* to avoid anuy possible future collisions
  * Lance Speelmon on Wed, 7 May 2014 09:28:42 -0700 [View Commit](../../commit/838488d67ecd38d7a85c0017a488cc6062401161)
* Improved debug messages
  * Lance Speelmon on Wed, 7 May 2014 09:44:34 -0700 [View Commit](../../commit/360de05f092e0738549668e24eb45872b05d4bfa)
* Upgrade s3 wagon to 1.2.1
  * Lance Speelmon on Wed, 7 May 2014 09:44:49 -0700 [View Commit](../../commit/4d040822b2f0581dbc1e8b46e13a4581255ed15c)
* Update README with new SQL flyway stuff
  * Lance Speelmon on Wed, 7 May 2014 11:08:04 -0700 [View Commit](../../commit/0d13fdabd55ae79a538991b77814541b521c7802)
* Added database migrations for KC 5.2.0 from community
  * Lance Speelmon on Wed, 7 May 2014 17:05:09 -0700 [View Commit](../../commit/c65a22b7bb7149fdf1b62297beccee8a337c8881)
* Fixes: com.googlecode.flyway.core.command.FlywaySqlScriptException: Error executing statement at line 106: insert into krim_role_t (ROLE_ID, OBJ_ID, VER_NBR, ROLE_NM, NMSPC_CD, DESC_TXT, KIM_TYP_ID, ACTV_IND, LAST_UPDT_DT)
  * values ('KR1000', uuid(), 1, 'GuestRole', 'KUALI', 'This role is used for no login guest users.', '1', 'Y', now())
  * Lance Speelmon on Wed, 7 May 2014 18:59:14 -0700 [View Commit](../../commit/607d31f142c2a4213ae34465fafe672a1404257f)
* Work around: com.googlecode.flyway.core.command.FlywaySqlScriptException: Error executing statement at line 8: INSERT INTO CUST_REPORT_DETAILS(REPORT_ID,REPORT_LABEL,REPORT_DESCRIPTION,REPORT_TYPE_CODE,RIGHT_REQUIRED,FILE_NAME,CONTENT_TYPE,UPDATE_TIMESTAMP,UPDATE_USER,VER_NBR,OBJ_ID,REPORT_DESIGN) values ((SELECT (MAX(ID)) FROM SEQ_REPORT_ID),'Proposal By College','Proposal By College',(SELECT REPORT_TYPE_CODE FROM CUST_REPORT_TYPE WHERE REPORT_TYPE_DESC = 'Global'),'RUN GLOBAL REPORTS','proposalbycollege.rptdesign','application/octet-stream',NOW(),'admin',1,UUID(),
  * '<?xml version="1.0" encoding="UTF-8"?>
  * <report xmlns="http://www.eclipse.org/birt/2005/design" version="3.2.20" id="1">
  * Lance Speelmon on Wed, 7 May 2014 19:10:09 -0700 [View Commit](../../commit/2d627cea4a093ecfb789b4a8a50153ad12c989cd)
* Fixes: com.googlecode.flyway.core.command.FlywaySqlScriptException: Error executing statement at line 7: ALTER TABLE IACUC_PROTOCOL_PERSONS
    ADD CONSTRAINT FK_IACUC_PROTOCOL_PERS_AFF FOREIGN KEY (AFFILIATION_TYPE_CODE)
    REFERENCES IACUC_AFFILIATION_TYPE (AFFILIATION_TYPE_CODE)
  * Lance Speelmon on Wed, 7 May 2014 19:32:59 -0700 [View Commit](../../commit/b0c1a3cc8e0ff46bed7ac358719a2b81b447bde0)
* Fixes: com.googlecode.flyway.core.command.FlywaySqlScriptException: Error executing statement at line 57: CALL update_irb_protocol_document_derived_pi_role
  * Caused by: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: SELECT command denied to user 'coeus'@'localhost' for table 'krim_role_perm_t'
  * Lance Speelmon on Wed, 7 May 2014 19:39:07 -0700 [View Commit](../../commit/78189c03edfc8a38c92570939055a79a4dba3920)
* Work around: FlywaySqlScriptException: Error executing statement at line 57: CALL update_irb_protocol_document_derived_pi_role ()
  * MySQLIntegrityConstraintViolationException: Duplicate entry 'KC1884' for key 'PRIMARY'
  * Lance Speelmon on Thu, 8 May 2014 08:07:30 -0700 [View Commit](../../commit/12b19d2ec36c3d522ce0dd9c4ce6cd36eb8a3122)
* Refactored project for cx_data_migration artifact only
  * Lance Speelmon on Thu, 8 May 2014 08:29:19 -0700 [View Commit](../../commit/737bd5b374a44875261569b5a3094f3d9eb45b69)
* Update CX dependency to a newer version
  * Lance Speelmon on Thu, 8 May 2014 10:27:07 -0700 [View Commit](../../commit/fee3e9cf043c7298fb9012b4720843cfbe8a15ad)
* AutomatedXmlIngester initial implementation
  * Lance Speelmon on Tue, 20 May 2014 17:42:57 -0700 [View Commit](../../commit/2cfefde8374bb9311ae703286d512ef6c62be97f)
* The SQL that goes with Fixes https://github.com/rSmart/issues/issues/148
  * Lance Speelmon on Tue, 20 May 2014 17:48:30 -0700 [View Commit](../../commit/45a9c09df53f85309d2c73d038b04af4f1ed5a1e)
* The SQL to go with Fixes https://github.com/rSmart/issues/issues/63
  * Lance Speelmon on Tue, 20 May 2014 17:59:48 -0700 [View Commit](../../commit/8abedbf11415df435d00207d0b26c931a69ae88c)
* Moved CE SQL and tools to this repo
  * Lance Speelmon on Fri, 23 May 2014 12:11:43 -0700 [View Commit](../../commit/f1c995a2e73b9cac5fddb41a4c86763062d8f883)
* Include +delete from protocol_references when deleting protocols
  * Lance Speelmon on Sat, 24 May 2014 10:32:43 -0700 [View Commit](../../commit/4bb8d1e551ff313cfd87c6a282c443ae3ee14986)
* Started script to remove Organizations but currently depends on clean_all was run before this script
  * Lance Speelmon on Wed, 28 May 2014 13:28:43 -0700 [View Commit](../../commit/d7559f1d144d6e0df23f90e85cdba016ecc36973)
* Have AutoXmlIngester implement the Spring SmartApplicationListener to try to get it to start last
  * Lance Speelmon on Wed, 28 May 2014 13:36:27 -0700 [View Commit](../../commit/21db7a6a514e69de0750a128e7d9fbaeb568041b)
* Added award_template to cleanup script
  * Lance Speelmon on Thu, 29 May 2014 13:06:47 -0700 [View Commit](../../commit/6d752be0d5ad901db5dda457dfcae239c52090a7)
* Updated load_sponsors.rb to reflect that it depends on running RemoveAllTransactionalData() beforehand.
  * Lance Speelmon on Thu, 29 May 2014 13:07:35 -0700 [View Commit](../../commit/9b2e1915a13033d3a7c8708b132d1deb7e2a1540)
* Downgrade rice-core-web dependency to 2.1.7.3-rsmart to be more forgiving
  * Lance Speelmon on Thu, 29 May 2014 13:42:12 -0700 [View Commit](../../commit/feb5aab9385f9da9231fd81f85341359979ee58e)