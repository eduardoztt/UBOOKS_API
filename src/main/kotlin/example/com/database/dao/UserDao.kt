package example.com.database.dao

import example.com.model.User
import example.com.model.Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class UserDao {


    suspend fun findAll(): List<User> = dbQuery {
        Users.selectAll().map {
            User(
                name = it[Users.name],
                email = it[Users.email],
                password = it[Users.password],
                img = it[Users.img],

            )
        }
    }

    suspend fun getUserEmail(email: String): User? = dbQuery {
        Users.selectAll().where { Users.email eq email }
            .map {
                User(
                    name = it[Users.name],
                    email = it[Users.email],
                    password = it[Users.password],
                    img = it[Users.img],
                )
            }.firstOrNull()
    }

    suspend fun findByEmailAndPassword(email: String, password: String): User {
        return dbQuery {
            Users.selectAll().where { Users.email.eq(email) and Users.password.eq(password) }
                .map {
                    User(
                        name = it[Users.name],
                        email = it[Users.email],
                        password = it[Users.password],
                        img = it[Users.img],

                    )
                }.first()
        }
    }

    suspend fun delete(email: String): Boolean {
        return dbQuery {
            Users.deleteWhere { Users.email eq email } > 0
        }
    }

    suspend fun updateEmail(email: String, newName: String): Boolean {
        return dbQuery {
            Users.update({ Users.email eq email }) {
                it[Users.name] = newName
            } > 0
        }
    }

    suspend fun updateImg(email: String, newImg: String): Boolean {
        return dbQuery {
            Users.update({ Users.email eq email }) {
                it[Users.img] = newImg
            } > 0
        }
    }

    suspend fun updatePassword(email: String, newPassword: String): Boolean {
        return dbQuery {
            Users.update({ Users.email eq email }) {
                it[Users.password] = newPassword
            } > 0
        }
    }

    suspend fun save(user: User) =
        dbQuery {
            val insertStatement = Users.insert {
                it[name] = user.name
                it[password] = user.password
                it[email] = user.email
                it[img] = user.img
            }
            // Return the user that was inserted
            insertStatement.resultedValues?.singleOrNull()?.let {
                User(
                    name = it[Users.name],
                    email = it[Users.email],
                    password = it[Users.password],
                    img = it[Users.img],
                )
            }
        }
}

suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }