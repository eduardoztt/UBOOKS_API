package example.com.dto

import example.com.model.Livro
import kotlinx.serialization.Serializable


@Serializable
data class LivroRequest(
    val ano: Int,
    val autor: String,
    val descricao: String,
    val genero: String,
    val imagem: String,
    val paginas: Int,
    val titulo: String
) {
    fun toLivro() = Livro(
        idLivros = 0,
        ano = ano,
        autor = autor,
        descricao = descricao,
        genero = genero,
        imagem = imagem,
        paginas = paginas,
        titulo = titulo
    )
}