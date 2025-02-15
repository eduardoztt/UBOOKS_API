package example.com.plugins


import example.com.dto.*
import example.com.model.Livro
import example.com.model.StatusLivro
import example.com.repository.LivroRepository
import example.com.repository.StatusLivroRepository
import example.com.repository.StatusRepository
import example.com.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    val userRepository = UserRepository()
    val livroRepository = LivroRepository()
    val statusRepository = StatusRepository()
    val statusLivroRepository = StatusLivroRepository()

    // user
    routing {
        get("/") {
            call.respondText("Api User funcionando")
        }


        get("/users") {
            val response = userRepository.getAll().map {
                it.toUserResponse()
            }
            call.respond(response)
        }

        // getUserEmail
        get("/users/{email}") {
            try {
                val email = call.request.queryParameters["email"]
                if (email == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val response = userRepository.getUserEmail(email)
                val userResponse = response?.toUserResponse()
                if (userResponse == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(userResponse)
            } catch (e: Exception) {
                call.respondText("Erro ao buscar usuario $e", status = HttpStatusCode.BadRequest)
            }
        }

        // verifica usuario com email e senha
        get("/users/login") {
            try {
                val email = call.request.queryParameters["email"]
                val password = call.request.queryParameters["password"]
                if (email == null || password == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val response = userRepository.getUserByEmailAndPassword(email, password)
                call.respond(response.toUserResponse())
            } catch (e: Exception) {
                call.respondText("Erro ao buscar usuario $e", status = HttpStatusCode.BadRequest)
            }
        }

        //deleteUser
        delete("/users/{email}") {
            try {
                val email = call.request.queryParameters["email"]
                if (email == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }

                statusLivroRepository.deleteByEmail(email)
                if (userRepository.delete(email)) {
                    call.respondText("Usuário deletado", status = HttpStatusCode.OK)
                } else {

                    call.respondText("Erro ao deletar usuario", status = HttpStatusCode.BadRequest)
                }
            } catch (e: Exception) {
                call.respondText("Erro ao deletar usuario $e", status = HttpStatusCode.BadRequest)
            }
        }

        // updateEmail
        patch("/users/{email}") {
            try {
                val email = call.request.queryParameters["email"]
                val newName = call.request.queryParameters["newName"]
                if (email == null || newName == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@patch
                }
                if (userRepository.updateEmail(email, newName)) {
                    call.respondText("Usuário atualizado", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Erro ao atualizar usuario", status = HttpStatusCode.BadRequest)
                }
            } catch (e: Exception) {
                call.respondText("Erro ao atualizar usuario $e", status = HttpStatusCode.BadRequest)
            }
        }

        patch("/users/imagem/{img}") {
            try {
                val email = call.request.queryParameters["email"]
                val newImg = call.request.queryParameters["img"]
                if (email == null || newImg == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@patch
                }
                if (userRepository.updateImg(email, newImg)) {
                    call.respondText("Usuário atualizado", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Erro ao atualizar usuario", status = HttpStatusCode.BadRequest)
                }
            } catch (e: Exception) {
                call.respondText("Erro ao atualizar usuario $e", status = HttpStatusCode.BadRequest)
            }
        }



        // updatePassword
        patch("/users/{email}/password") {
            try {
                val email = call.request.queryParameters["email"]
                val newPassword = call.request.queryParameters["newPassword"]
                if (email == null || newPassword == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@patch
                }
                if (userRepository.updatePassword(email, newPassword)) {
                    call.respondText("Usuário atualizado", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Erro ao atualizar usuario", status = HttpStatusCode.BadRequest)
                }
            } catch (e: Exception) {
                call.respondText("Erro ao atualizar usuario $e", status = HttpStatusCode.BadRequest)
            }
        }

        // insertUSer
        post("/users") {
            try {
                val request = call.receive<UserRequest>()
                val user = request.toUser()
                userRepository.save(user)?.let {
                    call.respondText("Usuário gravado", status = HttpStatusCode.Created)
                } ?: call.respondText("Erro ao gravar usuario", status = HttpStatusCode.BadRequest)
            } catch (e: Exception) {
                call.respondText("Erro ao gravar usuario $e", status = HttpStatusCode.BadRequest)
            }
        }
    }

    // livro
    routing {
        get("/") {
            call.respondText("Api Livro funcionando")
        }

        // getAllLivros
        get("/livros") {
            try {
                val livros = livroRepository.getAllLivros()
                call.respond(livros)
            } catch (e: Exception) {
                call.respondText("Erro ao buscar livros: $e", status = HttpStatusCode.BadRequest)
            }
        }

        // getLivroById
        get("/livros/{idLivro}") {
            try {
                val idLivro = call.parameters["idLivro"]?.toIntOrNull()
                if (idLivro == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@get
                }
                val livro = livroRepository.getLivroById(idLivro)
                if (livro == null) {
                    call.respond(HttpStatusCode.NotFound, "Livro não encontrado")
                    return@get
                }
                call.respond(livro)
            } catch (e: Exception) {
                call.respondText("Erro ao buscar livro: $e", status = HttpStatusCode.BadRequest)
            }
        }




        // insertLivro
        post("/livros") {
            try {
                val livroRequest = call.receive<Livro>()
                val livro = livroRepository.saveLivro(livroRequest)
                call.respondText("Livro gravado com sucesso", status = HttpStatusCode.Created)
            } catch (e: Exception) {
                call.respondText("Erro ao gravar livro: $e", status = HttpStatusCode.BadRequest)
            }
        }

        // updateLivro
        put("/livros/{idLivro}") {
            try {
                val idLivro = call.parameters["idLivro"]?.toIntOrNull()
                if (idLivro == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@put
                }
                val livroRequest = call.receive<Livro>()
                val livroAtualizado = livroRequest.copy(idLivros = idLivro)
                if (livroRepository.updateLivro(livroAtualizado)) {
                    call.respondText("Livro atualizado com sucesso", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Erro ao atualizar livro", status = HttpStatusCode.BadRequest)
                }
            } catch (e: Exception) {
                call.respondText("Erro ao atualizar livro: $e", status = HttpStatusCode.BadRequest)
            }
        }

        // deleteLivro
        delete("/livros/{idLivro}") {
            try {
                val idLivro = call.parameters["idLivro"]?.toIntOrNull()
                if (idLivro == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@delete
                }
                if (livroRepository.deleteLivro(idLivro)) {
                    call.respondText("Livro deletado com sucesso", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Erro ao deletar livro", status = HttpStatusCode.BadRequest)
                }
            } catch (e: Exception) {
                call.respondText("Erro ao deletar livro: $e", status = HttpStatusCode.BadRequest)
            }
        }

        // saveAllLivros
        post("/livros/batch") {
            try {
//                val livrosRequest = call.receive<List<Livro>>()
//                livroRepository.saveAllLivros(livrosRequest)

                val livroRequest = call.receive<List<LivroRequest>>()
                val livros = livroRequest.map { it.toLivro() }
                livroRepository.saveAllLivros(livros)

//                val statusRequest = call.receive<StatusRequest>()
//                val status = statusRequest.toStatus()
//                statusRepository.save(status)
                call.respondText("Livros gravados com sucesso", status = HttpStatusCode.Created)
            } catch (e: Exception) {
                call.respondText("Erro ao gravar livros: $e", status = HttpStatusCode.BadRequest)
            }
        }

        delete("/livros") {
            try {

                if (livroRepository.deleteAllLivros()) {
                    call.respondText("Livro deletado com sucesso", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Erro ao deletar livro", status = HttpStatusCode.BadRequest)
                }
            } catch (e: Exception) {
                call.respondText("Erro ao deletar livro: $e", status = HttpStatusCode.BadRequest)
            }
        }

    }


    // status
    routing {
        get("/") {
            call.respondText("Api Status funcionando")
        }

        get("/status") {
            try {
                val status = statusRepository.getAll()
                call.respond(status)
            } catch (e: Exception) {
                call.respondText("Erro ao buscar status: $e", status = HttpStatusCode.BadRequest)
            }
        }


        delete("/status") {
            try {

                if (statusRepository.deleteStatus()) {
                    call.respondText("Status deletado com sucesso", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Erro ao deletar Status", status = HttpStatusCode.BadRequest)
                }
            } catch (e: Exception) {
                call.respondText("Erro ao deletar Status: $e", status = HttpStatusCode.BadRequest)
            }
        }


        // save user
        post("/status") {
            try {
                val statusRequest = call.receive<StatusRequest>()
                val status = statusRequest.toStatus()
                statusRepository.save(status)
//                val statusRequest = call.receive<Status>()
//                val status = statusRepository.save(statusRequest)
                call.respondText("Status gravado com sucesso", status = HttpStatusCode.Created)
            } catch (e: Exception) {
                call.respondText("Erro ao gravar status: $e", status = HttpStatusCode.BadRequest)
            }
        }
    }

    // StatusLivro
    routing {
        get("/") {
            call.respondText("Api StatusLivro funcionando")
        }
        get("/statuslivros") {
            try {
                val statusLivros = statusLivroRepository.getAll()
                call.respond(statusLivros)
            } catch (e: Exception) {
                call.respondText("Erro ao buscar statuslivros: $e", status = HttpStatusCode.BadRequest)
            }
        }

        get("/statuslivros/{email}/{idStatus}") {
            try {
                val email = call.request.queryParameters["email"]
                val status = call.request.queryParameters["idStatus"]?.toIntOrNull()
                if (email == null || status == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val statusLivros = statusLivroRepository.getByEmailAndStatus(email,status)
                call.respond(statusLivros)
            } catch (e: Exception) {
                call.respondText("Erro ao buscar statuslivros: $e", status = HttpStatusCode.BadRequest)
            }
        }


        get("/statuslivros/user/{email}") {
            try {
                val email = call.request.queryParameters["email"]
                if (email == null ) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val statusLivros = statusLivroRepository.getByEmail(email)
                call.respond(statusLivros)
            } catch (e: Exception) {
                call.respondText("Erro ao buscar statuslivros: $e", status = HttpStatusCode.BadRequest)
            }
        }

        get("/statuslivros/verific/{email}/{idLivro}") {
            try {
                val email = call.request.queryParameters["email"]
                val idLivro = call.request.queryParameters["idLivro"]?.toIntOrNull()
                if (email == null || idLivro == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val response = statusLivroRepository.getByEmailAndIdLivro(email, idLivro)
                if (response == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(response)
            } catch (e: Exception) {
                call.respondText("Erro ao buscar statuslivros: $e", status = HttpStatusCode.BadRequest)
            }
        }


        post("/statuslivros") {
            try {
//
//                val body = call.receiveText()
//                print(body)


                val statusRequest = call.receive<StatusLivroRequest>()
                val status = statusRequest.toStatusLivro()
                val statusId = statusLivroRepository.findByEmailAndStatusId()

                val list = StatusLivro(
                    idStatusLivro = (statusId?.idStatusLivro?:0) +1,
                    idLivro = status.idLivro,
                    idStatus = status.idStatus,
                    email = status.email,
                    paginaslidas = status.paginaslidas
                )

                statusLivroRepository.save(list)
//                val statusRequest = call.receive<Status>()
//                val status = statusRepository.save(statusRequest)
                call.respondText("Status do Livro gravado com sucesso", status = HttpStatusCode.Created)
            } catch (e: Exception) {
                print("Erro ao gravar status do Livro: $e")
                call.respondText("Erro ao gravar status do Livro: $e", status = HttpStatusCode.BadRequest)

            }
        }

        put("/statuslivros/{idStatusLivro}") {
            try {
                val idStatusLivro = call.parameters["idStatusLivro"]?.toIntOrNull()
                if (idStatusLivro == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@put
                }
                val statusLivroRequest = call.receive<StatusLivro>()
                val statusLivroAtualizado = statusLivroRequest.copy(idStatusLivro = idStatusLivro)
                if (statusLivroRepository.update(statusLivroAtualizado)) {
                    call.respondText("StatusLivro atualizado com sucesso", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Erro ao atualizar statusLivro", status = HttpStatusCode.BadRequest)
                }
            } catch (e: Exception) {
                call.respondText("Erro ao atualizar statusLivro: $e", status = HttpStatusCode.BadRequest)
            }
        }

        patch("/statuslivros/update/{idStatusLivro}/{idStatus}") {
            try {
                val idStatusLivro = call.parameters["idStatusLivro"]?.toIntOrNull()
                val idStatus = call.parameters["idStatus"]?.toIntOrNull()

                if (idStatus == null || idStatusLivro == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@patch
                }

                if (statusLivroRepository.updateStatus(idStatusLivro,idStatus)) {
                    call.respondText("StatusLivro atualizado com sucesso", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Erro ao atualizar statusLivro", status = HttpStatusCode.BadRequest)
                }
            } catch (e: Exception) {
                call.respondText("Erro ao atualizar statusLivro: $e", status = HttpStatusCode.BadRequest)
            }
        }


        patch("/statuslivros/update/paginas/{paginas}/{idStatusLivro}") {
            try {
                val newpaginas = call.request.queryParameters["paginas"]?.toIntOrNull()
                val idStatusLivro = call.request.queryParameters["idStatusLivro"]?.toIntOrNull()
                if (idStatusLivro == null || newpaginas == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@patch
                }
                if (statusLivroRepository.updatePaginas(idStatusLivro, newpaginas)) {
                    call.respondText("Usuário atualizado", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Erro ao atualizar usuario", status = HttpStatusCode.BadRequest)
                }
            } catch (e: Exception) {
                call.respondText("Erro ao atualizar usuario $e", status = HttpStatusCode.BadRequest)
            }
        }


        delete("/statuslivros/{idStatusLivro}") {
            try {
                val idStatusLivro = call.parameters["idStatusLivro"]?.toIntOrNull()
                if (idStatusLivro == null) {
                    call.respond(HttpStatusCode.BadRequest, "ID inválido")
                    return@delete
                }
                if (statusLivroRepository.delete(idStatusLivro)) {
                    call.respondText("StatusLivro deletado com sucesso", status = HttpStatusCode.OK)
                } else {
                    call.respondText("Erro ao deletar statusLivro", status = HttpStatusCode.BadRequest)
                }
            } catch (e: Exception) {
                call.respondText("Erro ao deletar statusLivro: $e", status = HttpStatusCode.BadRequest)
            }
        }
    }
}