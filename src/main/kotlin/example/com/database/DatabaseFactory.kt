package example.com.database

import example.com.model.Livros
import example.com.model.StatusLivros
import example.com.model.Statuses
import example.com.model.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(driverClassName: String = "org.h2.Driver", jdbcURL: String = "jdbc:h2:file:./build/db") {
        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            SchemaUtils.create(Users)
            SchemaUtils.create(StatusLivros)
            SchemaUtils.create(Livros)
            SchemaUtils.create(Statuses)
        }
    }
}
