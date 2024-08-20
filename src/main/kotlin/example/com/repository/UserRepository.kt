package example.com.repository

import example.com.database.dao.UserDao
import example.com.model.User

class UserRepository(
    private val dao: UserDao = UserDao()
) {
    suspend fun getUserEmail(email: String): User? = dao.getUserEmail(email)

    suspend fun delete(email: String): Boolean {
        return dao.delete(email)
    }

    suspend fun updateEmail(email: String, newName: String): Boolean {
        return dao.updateEmail(email, newName)
    }

    suspend fun updatePassword(email: String, newPassword: String): Boolean {
        return dao.updatePassword(email, newPassword)
    }

    suspend fun save(user: User) = dao.save(user)
}