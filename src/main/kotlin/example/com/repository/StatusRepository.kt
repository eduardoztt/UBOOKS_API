package example.com.repository

import example.com.database.dao.StatusDao
import example.com.model.Status

class StatusRepository(
    private val dao: StatusDao = StatusDao()
) {
    suspend fun getAll() = dao.findAll()

    suspend fun save(status: Status) = dao.save(status)

    suspend fun  deleteStatus() = dao.deleteAll()
}