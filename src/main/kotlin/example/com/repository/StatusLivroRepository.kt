package example.com.repository

import example.com.database.dao.StatusLivroDao
import example.com.model.StatusLivro

class StatusLivroRepository(
    private val dao: StatusLivroDao = StatusLivroDao()
) {
    suspend fun getAll() = dao.findAll()

    suspend fun getByEmailAndStatus(email: String,status: Int) = dao.findByEmailAndStatus(email,status)

    suspend fun getByEmail(email: String) = dao.findByEmail(email)

    suspend fun findByEmailAndStatusId() = dao.findByEmailAndStatusId()

    suspend fun getByEmailAndIdLivro(email: String, idStatus: Int) = dao.findByEmailAndIdLivro(email, idStatus)

    suspend fun save(statusLivro: StatusLivro) = dao.save(statusLivro)

    suspend fun update(statusLivro: StatusLivro) = dao.update(statusLivro)

    suspend fun delete(idStatusLivro: Int) = dao.delete(idStatusLivro)

    suspend fun deleteByEmail(email: String) = dao.deleteByEmail(email)

    suspend fun updateStatus(idStatusLivro: Int, status: Int): Boolean {
        return dao.updateStatus(idStatusLivro, status)
    }

    suspend fun updatePaginas(idStatusLivro: Int, newpaginas: Int): Boolean {
        return dao.updatePaginas(idStatusLivro, newpaginas)
    }

}