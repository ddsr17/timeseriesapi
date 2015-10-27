package com.crackjam.api

/**
 * Created by deepanshu on 31/8/15.
 */

import com.sksamuel.elastic4s.{ElasticClient, HitAs, RichSearchHit}
import com.sksamuel.elastic4s.ElasticDsl._
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.jackson.JsonMethods._

object readgraph {

  case class Plot(timestamp: String, AverageofclientSRTT: String,AverageofclientSRTT_global_anomaly: String,Tag_hour_anomalyof_AverageofclientSRTT: String,anomalyof_AverageofclientSRTT_over_windowtimestamp: String)

    implicit object PlotHitAs extends HitAs[Plot] {
      override def as(hit: RichSearchHit):Plot ={
        Plot(hit.sourceAsMap("timestamp").toString,hit.sourceAsMap("AverageofclientSRTT").toString,hit.sourceAsMap("AverageofclientSRTT_global_anomaly").toString,hit.sourceAsMap("Tag_hour_anomalyof_AverageofclientSRTT").toString,hit.sourceAsMap("anomalyof_AverageofclientSRTT_over_windowtimestamp").toString)
      }
    }


    def makegraph = {

      val client = ElasticClient.remote("10.82.171.25",9300)

      //var drama = client.execute(delete index "appcito7")

      val response = client.execute( search in "vedappcito3" -> "appcito3" limit 50).await

      val data: Seq[Plot] = response.as[Plot]


      println(data);

      val group = data.map(x => {
        (("timestamp" -> x.timestamp)~
          ("AverageofclientSRTT" -> x.AverageofclientSRTT)~
          ("AverageofclientSRTT_global_anomaly" -> x.AverageofclientSRTT_global_anomaly)~
          ("Tag_hour_anomalyof_AverageofclientSRTT" -> x.Tag_hour_anomalyof_AverageofclientSRTT)~
          ("anomalyof_AverageofclientSRTT_over_windowtimestamp" -> x.anomalyof_AverageofclientSRTT_over_windowtimestamp))
      })


      val send = compact(render(group))

      send

    }

}


