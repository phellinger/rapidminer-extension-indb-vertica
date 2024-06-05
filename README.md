In-Database Vertica extension
========================================

What does this extension do?
----------------------------

In-Database Vertica extension for Altair RapidMiner adds support for the Vertica database when used together with the official [In-Database Processing extension](https://marketplace.rapidminer.com/UpdateServer/faces/product_details.xhtml?productId=rmx_in_database_processing).

With this extension, Vertica database can be used similarly to the [officially supported](https://docs.rapidminer.com/latest/studio/connect/database/index.html) MySQL, PostgreSQL, MSSQL, Oracle, BigQuery, Snowflake or Databricks databases.

Aggregate functions and built-in functions have been added from the [Vertica documentation](https://docs.vertica.com/24.1.x/en/sql-reference/functions/).

How to test the extension?
--------------------------

This extension can be tested against the docker image [vertica/vertica-ce:24.1.0-0](https://hub.docker.com/layers/vertica/vertica-ce/24.1.0-0/images/sha256-9248dbc3ee429827f89a96758b88d7a286762d50918e2e4bbfcda2dd18cb1cd9?context=explore).

The JDBC driver to create the Vertica connection can be downloaded from the [mvn repository](https://repo1.maven.org/maven2/com/vertica/jdbc/vertica-jdbc/24.2.0-0/vertica-jdbc-24.2.0-0.jar).
