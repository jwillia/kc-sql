# Import DDL into database
**------------------------**

*SQL is now imported via Flyway so don't worry be happy.*

# Directory Structure

* config - contains grm specific workflow files. Internal to KualiCo
* java - custom Flyway migrator code
* resources - flyway versioned sql files

**Note:** this project does not yet support workflow ingester files

Within the resources package structure there are a series of folders for various configurations of the Kuali Coeus database.  There are three primary definitions to understand for this data: bootstrap, demo, stage.  

Bootstrap data contains things like required parameters, roles, kc code tables that are expected to be present in order for the application to function

Demo data contains things like example Kim principals, Role memberships, Units, Sponsors.  This represents a fully populated KC system without any transactional or document data.

Stage data contains things like document workflow data, proposals, awards, and other complex transactional data.  This represents a fully populated KC system that has been used for some period of time.

In most cases stage data will be separate from demo data which will be separate from bootstrap data.  In rare cases we may want to modify bootstrap data in either a demo or stage script or we might want to modify demo data in a stage script.

grm - contains custom grm specific sql files.  Internal to KualiCo

* kc
** bootstrap - bootstrap sql files. This will contain only files related to KC tables and data.
** demo - this contains a set of demo data.  This will contain only files related to KC tables and data.
** stage - this contains a set of staging data.  This will contain only files related to KC tables and data.
** embedded_client_scripts - rice client side sql files when running rice in embedded mode.  This may contain rice or kc data but only for rice client side tables.
* rice
** bootstrap - bootstrap sql files for the rice tables.  This may contain rice or kc data but only for rice tables.  Includes client side tables as well.
** demo - this contains a set of demo data.  This may contain rice or kc data but only for rice tables.
** stage - this contains a set of staging data.  This may contain rice or kc data but only for rice tables.

* rice_data_only
** bootstrap - this contains the rice bootstrap sql files but only the DML files related to KC.  This is used for when you are managing the rice server separates in a standalone configuration where the rice server has already been upgraded.

* rice_server
** bootstrap - bootstrap sql files for the rice tables.  This may contain rice or kc data but only for rice tables.  Does not includes client side tables.  This is used for when you are managing the rice server separates in a standalone configuration where the rice server has not been upgraded.


