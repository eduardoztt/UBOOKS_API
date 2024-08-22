package example.com.database.dao

import example.com.dto.LivroResponse
import example.com.model.Livro
import example.com.model.Livros
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class LivroDao {
    suspend fun findAll(): List<LivroResponse> = dbQuery {
        Livros.selectAll().map {
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

    suspend fun findById(idLivro: Int): LivroResponse? = dbQuery {
        Livros.selectAll().where { Livros.id eq idLivro }.map {
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
        }.singleOrNull()
    }

    suspend fun save(livro: Livro): LivroResponse? = dbQuery {
        val insertStatement = Livros.insert {
            it[ano] = livro.ano
            it[autor] = livro.autor
            it[descricao] = livro.descricao
            it[genero] = livro.genero
            it[imagem] = livro.imagem
            it[paginas] = livro.paginas
            it[titulo] = livro.titulo
        }
        insertStatement.resultedValues?.singleOrNull()?.let {
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

    suspend fun update(livro: Livro): Boolean = dbQuery {
        Livros.update({ Livros.id eq livro.id }) {
            it[ano] = livro.ano
            it[autor] = livro.autor
            it[descricao] = livro.descricao
            it[genero] = livro.genero
            it[imagem] = livro.imagem
            it[paginas] = livro.paginas
            it[titulo] = livro.titulo
        } > 0
    }

    suspend fun delete(idLivro: Int): Boolean = dbQuery {
        Livros.deleteWhere { Livros.id eq idLivro } > 0
    }

    suspend fun deleteAll(): Boolean = dbQuery {
        Livros.deleteAll() > 0
    }

    suspend fun saveAll(livros: List<Livro>) = dbQuery {
        Livros.batchInsert(livros) { livro ->
            this[Livros.ano] = livro.ano
            this[Livros.autor] = livro.autor
            this[Livros.descricao] = livro.descricao
            this[Livros.genero] = livro.genero
            this[Livros.imagem] = livro.imagem
            this[Livros.paginas] = livro.paginas
            this[Livros.titulo] = livro.titulo
        }
    }
}

