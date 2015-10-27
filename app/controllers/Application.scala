package controllers

import com.crackjam.api.readdata
import javax.inject.Inject
import com.sksamuel.elastic4s.{RichSearchHit, HitAs}
import play.api._
import play.api.libs.json._
import play.api.libs.ws.{WS, WSRequest, WSClient}
import play.api.mvc._
import play.api.Play.current
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import play.api.libs.ws.WS
import play.api.libs.functional.syntax._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
case class Any1(indexname: String,name: String)

class Application @Inject() (ws: WSClient) extends Controller {

  /*  implicit val myQuery: Reads[Any1] = (
      (JsPath \"index").read[String] and
        (JsPath\"name").read[String])(Any1.apply _)*/

  implicit val Any1Query: Reads[Any1] = (
    (JsPath \ "indexname").read[String] and
      (JsPath \ "name").read[String])(Any1.apply _)

  def allKeys(json: JsValue): collection.Set[String] = json match {
    case o: JsObject => o.keys ++ o.values.flatMap(allKeys)
    case JsArray(as) => as.flatMap(allKeys).toSet
    case _ => Set()
  }

  def index = Action {
    Ok(views.html.index(""))
  }

  def options(path:String) = Action { Ok("")}

  def getallindex = Action {

    import play.api.libs.json.Json
    val a = WS.url("http://104.199.139.233:9200/_stats/indexes").get().map { responce =>
      var da = responce.json \ "indices"

      var data = allKeys(da.get)

      data.toSeq
    }
    var data: Seq[String] = Seq()
    a.onComplete {
      case Success(value) => {
        data = value
      }
      case Failure(e) => e.printStackTrace
    }

    Await.result(a, 1500 millis)

    val d = Json.toJson(data).toString();

    Ok(Json.toJson(data).toString());
  }

  def getallData = Action(BodyParsers.parse.json) { request =>

    val requestdata = request.body.validate[Any1]
    requestdata.fold(
      error => {
        Ok("at error block !!")
      },
      requestparam => {
        val id = requestparam.indexname
        println("My Id", id)
        val name = requestparam.name
        var result = readdata.getData(id);
        Ok(Json.toJson(result))
      }
    )

  }



  def getplot = Action { request => {

    val body: AnyContent = request.body
    val textBody: Option[String] = body.asText

    var newdata = com.crackjam.api.readgraph.makegraph
    Ok(Json.toJson(newdata))
  }
  }

}

