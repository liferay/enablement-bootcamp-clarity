# Upgrading the Enablement Course Bundle

There are special steps necessary to support upgrading the course bundle since we are using the HSQL database.

Fortunately everything has been set up:

* In the `configs/local/tools/portal-tools-db-upgrade-client` directory, the properties files are pre-created and populated with necessary changes for a successful upgrade.
* In the `configs/local/data/hypersonic` directory, there's a `Shutdown.java` file and the `hsql.jar` file needed to start up Hypersonic as a standalone database.

If you are setting up a new course bundle, I recommend copying the `tools` directory and the two files from `data/hypersonic` to the `configs/local` directory in the new repo so they, too, are ready to upgrade.

## Steps to Complete an Upgrade

1. Open a command line in the `bundles/data/hypersonic` directory.
2. Issue the command `javac Shutdown.java` to compile the Shutdown command.
3. Issue the command `java -cp hsql.jar org.hsqldb.Server -database.0 file:lportal -dbname.0 lportal` to start up a standalone DB server.
4. Open a new command line in the `bundles/tools/portal-tools-db-upgrade-client` directory.
5. Issue the command `./db_upgrade_client.sh` to run the upgrade script.
6. You'll frequently end up in the gogo shell after the script completes. I always issue the commands `upgrade:executeAll` and `verify:executeAll` to ensure all upgrades are processed and completed.
7. Use the `disconnect` command to close the gogo shell and return to the command prompt.
8. Change to the `../../data/hypersonic` directory.
9. Issue the command `java -cp .:hsql.jar Shutdown` to shut down the HSQL server.

At this point the database has been upgraded.

The `bundles/data/hypersonic/lportal.script` and `lportal.lobs` files are ready to copy over to the `configs/local/data/hypersonic` directory.

Don't forget to verify that the `bundles/data/document_library` folder isn't now out of sync with `configs/local/data/document_library` and if it is, sync it up.

