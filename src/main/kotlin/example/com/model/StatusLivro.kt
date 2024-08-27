package example.com.model

import org.jetbrains.exposed.sql.Table


data class StatusLivro(
    val idStatusLivro: Int,
    val idLivro: Int,
    val idStatus: Int,
    val email: String,
    val paginaslidas: Int
)

object StatusLivros : Table() {
    val idStatusLivro = integer("idStatusLivro").autoIncrement()
    val idLivro = integer("idLivro").references(Livros.idLivro)
    val idStatus = integer("idStatus").references(Statuses.idStatus)
    val email = text("email").references(Users.email)
    val paginaslidas = integer("paginaslidas")

    override val primaryKey = PrimaryKey(idStatusLivro)
}