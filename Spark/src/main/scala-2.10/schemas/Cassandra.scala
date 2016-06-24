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

  case class Professores(
                        designacao: String,
                        artes_cenicas: Int,
                        artes_industriais: Int,
                        artes_plasticas: Int,
                        ciencias: Int,
                        educacao_fisica: Int,
                        educacao_musical: Int,
                        educacao_lar: Int,
                        espanhol: Int,
                        frances: Int,
                        geografia: Int,
                        historia: Int,
                        ingles: Int,
                        portugues: Int,
                        matematica: Int,
                        tecnicas_agricolas: Int,
                        tecnicas_comerciais: Int,
                        educacao_infantil: Int,
                        ensino_fundamental: Int,
                        ensino_religioso: Int,
                        professor_2: Int
                        )

  case class Frequencia_e_Aprovacao(designacao: String, ano: String, codigo_serie: Int, descricao_serie: String, avaliados: Int, aprovados: Int, reprovados: Int)
}
