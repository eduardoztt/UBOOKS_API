package example.com.repository

import example.com.database.dao.LivroDao
import example.com.dto.LivroResponse
import example.com.model.Livro

class LivroRepository(
    private val dao: LivroDao = LivroDao()
) {
    suspend fun getAllLivros(): List<LivroResponse> = dao.findAll()

    suspend fun getLivroById(idLivro: Int): LivroResponse? = dao.findById(idLivro)

    suspend fun saveLivro(livro: Livro): LivroResponse? = dao.save(livro)

    suspend fun updateLivro(livro: Livro): Boolean = dao.update(livro)

    suspend fun deleteLivro(idLivro: Int): Boolean = dao.delete(idLivro)

    suspend fun deleteAllLivros() = dao.deleteAll()

    suspend fun saveAllLivros(livros: List<Livro>) = dao.saveAll(livros)
}
