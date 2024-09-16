package example.com.dto

import example.com.model.User
import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val name: String,
    val email: String,
    val password: String,
    val img: String,
) {
    fun toUser() = User(
        name = name,
        email = email,
        password = password,
        img = img,
    )
}