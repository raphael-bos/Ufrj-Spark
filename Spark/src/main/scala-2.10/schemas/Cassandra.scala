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

  case class Professores2(designacao: String, ordem_materia: Int, descricao_materia: String, quantidade: Int)

  case class Frequencia_e_Aprovacao(designacao: String, ano: String, codigo_serie: Int, descricao_serie: String, avaliados: Int, aprovados: Int, reprovados: Int)
  case class Media(designacao: String, ano: String, bimestre: Int, ordem_serie: Int, serie: Int, ordem_materia: Int, descricao_serie: String,
                   codigo_materia: String, descricao_materia: String, media: Double)

  val materiasMap = Map(
    1 -> "Ciencias",
    2 -> "Geografia",
    3 -> "Historia",
    4 -> "Matematica",
    5 -> "Portugues",
    6 -> "Artes Visuais",
    7 -> "Danca",
    8 -> "Musica",
    9 -> "Teatro",
    10 -> "Educacao Fisica",
    11 -> "Espanhol",
    12 -> "Frances",
    13 -> "Ingles",
    14 -> "Tecnicas Agricolas",
    15 -> "Artes Cenicas",
    16 -> "Artes Industriais",
    17 -> "Educacao para o Lar",
    18 -> "Artes Plasticas",
    19 -> "Tecnicas Comerciais",
    20 -> "Educacao Infantil",
    21 -> "Nenhuma Crianca a Menos-Portugues",
    22 -> "Nenhuma Crianca a Menos-Matematica",
    23 -> "Ensino Fundamental",
    24 -> "Ensino Religioso",
    25 -> "Professor 2"
  )

}
