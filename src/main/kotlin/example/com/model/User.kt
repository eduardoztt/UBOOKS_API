package example.com.model

import org.jetbrains.exposed.sql.Table

data class User(
    val email: String,
    val name: String,
    val password: String,
    val img: String,
)

object Users : Table() {
    val email = text("email")
    val name = text("name")
    val password = text("password")
    val img = text("img")

    override val primaryKey = PrimaryKey(email)
}