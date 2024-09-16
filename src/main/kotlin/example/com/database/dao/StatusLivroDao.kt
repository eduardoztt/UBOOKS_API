package example.com.database.dao

import example.com.dto.LivroResponse
import example.com.dto.StatusLivroResponse
import example.com.dto.toStatusLivroResponse
import example.com.model.*
import example.com.model.Livros.innerJoin
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

    suspend fun findByEmailAndIdLivro(email: String, idLivro: Int): StatusLivroResponse? = dbQuery {
        (StatusLivros innerJoin Livros innerJoin Users innerJoin Statuses)
        StatusLivros.selectAll().where { (StatusLivros.email eq email) and (StatusLivros.idLivro eq idLivro) }
            .map {
                StatusLivro(
                    idStatusLivro = it[StatusLivros.idStatusLivro],
                    idLivro = it[StatusLivros.idLivro],
                    idStatus = it[StatusLivros.idStatus],
                    email = it[StatusLivros.email],
                    paginaslidas = it[StatusLivros.paginaslidas]
                ).toStatusLivroResponse()
            }.firstOrNull()

//            .map {
//                Livro(
//                    id = it[Livros.id],
//                    ano = it[Livros.ano],
//                    autor = it[Livros.autor],
//                    descricao = it[Livros.descricao],
//                    genero = it[Livros.genero],
//                    imagem = it[Livros.imagem],
//                    paginas = it[Livros.paginas],
//                    titulo = it[Livros.titulo]
//                ).toLivroResponse()
//            }

    }


    suspend fun findByEmailAndStatusId(): StatusLivroResponse? = dbQuery {
        (StatusLivros innerJoin Livros innerJoin Users innerJoin Statuses)
        StatusLivros.selectAll()
            .map {
                StatusLivro(
                    idStatusLivro = it[StatusLivros.idStatusLivro],
                    idLivro = it[StatusLivros.idLivro],
                    idStatus = it[StatusLivros.idStatus],
                    email = it[StatusLivros.email],
                    paginaslidas = it[StatusLivros.paginaslidas]
                ).toStatusLivroResponse()
            }.lastOrNull()
    }


    suspend fun findByEmailAndStatus(email: String, status: Int): List<LivroResponse> = dbQuery {
        Livros
            .innerJoin(StatusLivros, { Livros.idLivro }, { StatusLivros.idLivro })
            .innerJoin(Users, { StatusLivros.email }, { Users.email })
            .innerJoin(Statuses, { StatusLivros.idStatus }, { Statuses.idStatus })
            .select {
                (StatusLivros.email eq email) and (StatusLivros.idStatus eq status)
            }.map {
                Livro(
                    idLivros = it[Livros.idLivro],
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
        Livros
            .innerJoin(StatusLivros, { Livros.idLivro }, { StatusLivros.idLivro })
            .innerJoin(Users, { StatusLivros.email }, { Users.email })
            .innerJoin(Statuses, { StatusLivros.idStatus }, { Statuses.idStatus })
            .select {
                StatusLivros.email eq email
            }.map {
                Livro(
                    idLivros = it[Livros.idLivro],
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

    suspend fun updateStatus(idStatusLivro: Int, status: Int): Boolean {
        return dbQuery {
            StatusLivros.update({ StatusLivros.idStatusLivro eq idStatusLivro }) {
                it[StatusLivros.idStatus] = status
            } > 0
        }
    }

    suspend fun delete(idStatusLivro: Int): Boolean = dbQuery {
        StatusLivros.deleteWhere { StatusLivros.idStatusLivro eq idStatusLivro } > 0
    }

    suspend fun deleteByEmail(email: String): Boolean = dbQuery {
        StatusLivros.deleteWhere { StatusLivros.email eq email } > 0
    }


    suspend fun updatePaginas(idStatusLivro: Int, newpaginas: Int): Boolean {
        return dbQuery {
            StatusLivros.update({ StatusLivros.idStatusLivro eq idStatusLivro }) {
                it[StatusLivros.paginaslidas] = newpaginas
            } > 0
        }
    }

}