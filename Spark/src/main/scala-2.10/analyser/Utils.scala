package analyser

/**
  * Created by raphael on 30/06/16.
  */
object Utils {

  def mergeByMost(arr1:Array[(String,Double)], arr2: Array[(String,Double)], size: Int): Array[(String,Double)] ={

    val merged= (arr1 ++ arr2).filter(x => x != null)
    val sorted = merged.sortWith((y,x) => y._2 > x._2 ).take(size)
    sorted
  }

}
