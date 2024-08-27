package example.com.repository

import example.com.database.dao.StatusLivroDao
import example.com.model.StatusLivro

class StatusLivroRepository(
    private val dao: StatusLivroDao = StatusLivroDao()
) {
    suspend fun getAll() = dao.findAll()

    suspend fun getByEmail(email: String) = dao.findByEmail(email)


    suspend fun getByEmailAndStatus(email: String, idStatus: Int) = dao.findByEmailAndStatus(email, idStatus)

    suspend fun save(statusLivro: StatusLivro) = dao.save(statusLivro)

    suspend fun update(statusLivro: StatusLivro) = dao.update(statusLivro)

    suspend fun delete(idStatusLivro: Int) = dao.delete(idStatusLivro)
}