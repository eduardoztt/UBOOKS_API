package example.com.database.dao

import example.com.dto.StatusResponse
import example.com.dto.toResponse
import example.com.model.Livros
import example.com.model.Status
import example.com.model.Statuses
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class StatusDao {
    suspend fun save(statu: Status) =
        dbQuery {
            val insertStatement = Statuses.insert {
                it[idStatus] = statu.idStatus
                it[status] = statu.status
            }
            // Return the user that was inserted
            insertStatement.resultedValues?.singleOrNull()?.let {
                Status(
                    idStatus = it[Statuses.idStatus],
                    status = it[Statuses.status]
                )
            }
        }

    suspend fun deleteAll(): Boolean = dbQuery {
        Statuses.deleteAll() > 0
    }


    suspend fun findAll(): List<StatusResponse> = dbQuery {
        Statuses.selectAll().map {
            Status(
                idStatus = it[Statuses.idStatus],
                status = it[Statuses.status]
            ).toResponse()
        }
    }
}
