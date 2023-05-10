package com.github.momikensneg.life.gateway

import org.testcontainers.containers.PostgreSQLContainer

class PostgresTestContainer: PostgreSQLContainer<PostgresTestContainer>(IMAGE_VERSION) {

    companion object {
        @Volatile
        private var POSTGRES_CONTAINER: PostgresTestContainer? = null
        private const val IMAGE_VERSION = "postgres:12.5-alpine"
        private const val DATABASE_NAME = "gateway"

        fun getInstance(): PostgresTestContainer? {

            if (POSTGRES_CONTAINER == null) {
                synchronized(PostgresTestContainer::class) {
                    if (POSTGRES_CONTAINER == null) {
                        POSTGRES_CONTAINER = PostgresTestContainer().withDatabaseName(DATABASE_NAME)
                        POSTGRES_CONTAINER!!.start()
                    }
                }
            }
            return POSTGRES_CONTAINER
        }
    }

    override fun start() {
        super.start();
        System.setProperty("DB_URL", POSTGRES_CONTAINER!!.jdbcUrl);
        System.setProperty("DB_USERNAME", POSTGRES_CONTAINER!!.username);
        System.setProperty("DB_PASSWORD", POSTGRES_CONTAINER!!.password);
    }

}