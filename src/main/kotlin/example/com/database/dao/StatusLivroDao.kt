package example.com.database.dao

import example.com.dto.LivroResponse
import example.com.dto.StatusLivroResponse
import example.com.dto.toStatusLivroResponse
import example.com.model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq


class StatusLivroDao {
    suspend fun findAll(): List<StatusLivroResponse> = dbQuery {
        StatusLivros.selectAll().map {
            StatusLivro(
                idStatusLivro = it[StatusLivros.idStatusLivro],
                idLivro = it[StatusLivros.idLivro],
                idStatus = it[StatusLivros.idStatus],
                email = it[StatusLivros.email],
                paginaslidas = it[StatusLivros.paginaslidas]
            ).toStatusLivroResponse()
        }
    }

    suspend fun findByEmailAndStatus(email: String, status: Int): List<LivroResponse> = dbQuery {
        (StatusLivros innerJoin Livros innerJoin Users innerJoin Statuses)
            .selectAll().where { (StatusLivros.email eq email) and (StatusLivros.idStatus eq status) }
            .map {
                Livro(
                    id = it[Livros.id],
                    ano = it[Livros.ano],
                    autor = it[Livros.autor],
                    descricao = it[Livros.descricao],
                    genero = it[Livros.genero],
                    imagem = it[Livros.imagem],
                    paginas = it[Livros.paginas],
                    titulo = it[Livros.titulo]
                ).toLivroResponse()
            }
    }

    suspend fun findByEmail(email: String): List<LivroResponse> = dbQuery {
        (StatusLivros innerJoin Livros innerJoin Users innerJoin Statuses)
            .selectAll().where { StatusLivros.email eq email }
            .map {
                Livro(
                    id = it[Livros.id],
                    ano = it[Livros.ano],
                    autor = it[Livros.autor],
                    descricao = it[Livros.descricao],
                    genero = it[Livros.genero],
                    imagem = it[Livros.imagem],
                    paginas = it[Livros.paginas],
                    titulo = it[Livros.titulo]
                ).toLivroResponse()
            }
    }

    suspend fun save(statusLivro: StatusLivro): StatusLivro = dbQuery {
        val insertStatement = StatusLivros.insert {
            it[idStatusLivro] = statusLivro.idStatusLivro
            it[idLivro] = statusLivro.idLivro
            it[idStatus] = statusLivro.idStatus
            it[email] = statusLivro.email
            it[paginaslidas] = statusLivro.paginaslidas
        }

        insertStatement.resultedValues?.singleOrNull()?.let {
            StatusLivro(
                idStatusLivro = it[StatusLivros.idStatusLivro],
                idLivro = it[StatusLivros.idLivro],
                idStatus = it[StatusLivros.idStatus],
                email = it[StatusLivros.email],
                paginaslidas = it[StatusLivros.paginaslidas]
            )
        } ?: statusLivro
    }

    suspend fun update(statusLivro: StatusLivro): Boolean = dbQuery {
        StatusLivros.update({ StatusLivros.idStatusLivro eq statusLivro.idStatusLivro }) {
            it[idLivro] = statusLivro.idLivro
            it[idStatus] = statusLivro.idStatus
            it[email] = statusLivro.email
            it[paginaslidas] = statusLivro.paginaslidas
        } > 0
    }

    suspend fun delete(idStatusLivro: Int): Boolean = dbQuery {
        StatusLivros.deleteWhere { StatusLivros.idStatusLivro eq idStatusLivro } > 0
    }
}