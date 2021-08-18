package fi.bytecraft.spock

import groovy.sql.Sql

import java.sql.Connection

class DbTestUtils {

    static clearDatabase(Connection connection) {
        def sql = new Sql(connection)

        sql.execute "SET REFERENTIAL_INTEGRITY FALSE"

        def rows = sql.rows "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES where TABLE_SCHEMA='PUBLIC'"
        rows.each {
            sql.execute "TRUNCATE TABLE $it.TABLE_NAME".toString()
        }

        def sequences = sql.rows "SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_SCHEMA='PUBLIC'"
        sequences.each {
            sql.execute "ALTER SEQUENCE $it.SEQUENCE_NAME RESTART WITH 1".toString()
        }

        sql.execute "SET REFERENTIAL_INTEGRITY TRUE"

        sql.close()
    }

}
