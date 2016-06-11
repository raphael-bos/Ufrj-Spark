import org.apache.spark.{SparkConf, SparkContext}
import com.datastax.spark.connector._

import scala.collection.mutable.ListBuffer

object Main {

  case class TableSchema (id: Int,name: String, school: String)

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("ProjetoSpark").setMaster("local[*]").set("spark.cassandra.connection.host", "127.0.0.1")
    val sc = new SparkContext(conf)
    val cassData = sc.cassandraTable[TableSchema]("spark","test").collect()
    val data = sc.parallelize(generateTableData())
    data.saveToCassandra("spark","test")
    System.exit(0)
  }

  def generateTableData() : List[TableSchema] ={
    val list = new ListBuffer[TableSchema]
    list += new TableSchema(0, "Nome0", "Escola0")
    list += new TableSchema(1, "Nome1", "Escola1")
    list += new TableSchema(2, "Nome2", "Escola2")
    list.toList
  }
}