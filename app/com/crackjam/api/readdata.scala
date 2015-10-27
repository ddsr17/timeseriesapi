package com.crackjam.api

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.json4s.jackson.JsonMethods._
import org.json4s.jackson.Serialization
import org.json4s.JsonDSL._
import collection.JavaConversions._


/**
 * Created by deepanshu on 27/8/15.
 */
object readdata {

  implicit val formats = org.json4s.DefaultFormats



  def getData(abc: String) : String={

    val client = ElasticClient.remote("104.199.139.233",9300)

    val resp = client.execute(search in abc/"raw" start 1 limit 10).await

    /*val headers = client.execute(get mapping abc/"new").await

    val myMappins = headers.mappings();*/

    var ls:List[Map[String, String]] = List()

    var json:Map[String, String] = Map();

    var some2: List[String] = List()

    var finalheaders: List[String] =List();
    for(x <- resp.getHits.hits())
    {
      var headers: List[String] =List();
      headers = headers:+ "Index";
      headers = headers:+ "Type";
      headers = headers:+ "Id";
      headers = headers:+ "Score"
      json += ("Index" -> x.getIndex)
      json += ("Type" -> x.getType)
      json += ("Id" -> x.getId)
      json += ("Score" -> x.getScore.toString)

      val y = x.getSource
      val keys = y.keys.iterator
      while (keys.hasNext){
        val key = keys.next()
        //println("key " + key);
        headers = headers :+ key;
        val value = y(key).toString
        json += ( key -> value)

      }


      finalheaders = headers

      ls = ls :+ json;

    }

    var finalMap = Map("header" -> finalheaders,"data"->ls)

    println(finalMap)
    val result = compact(Serialization.write(finalMap))

    result;
  }

  def main (args: Array[String]) {
  readdata.getData("appclientnew2")
  }
}
