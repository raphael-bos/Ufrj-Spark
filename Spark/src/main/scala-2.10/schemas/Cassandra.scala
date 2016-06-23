package schemas

/**
  * Created by raphael on 22/06/16.
  */
object Cassandra {

  case class Escola(
                      designacao: String,
                      nome: String,
                      logradouro: String,
                      bairro: String,
                      numero: String,
                      complemento: String,
                      cep: String,
                      ideb_1: String,
                      ideb_2: String,
                      latitude: String,
                      longitude: String,
                      ginasio_c: String,
                      acessibilidade: String,
                      series: String,
                      turnos: String,
                      ginasio_o: String,
                      diretor: String,
                      telefone: String,
                      inep: String,
                      coordenador: String
                    )
}
