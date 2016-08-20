import akka.actor.ActorSystem
import akka.event.{LoggingAdapter, Logging}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import scenarios.ProductAddTest1._

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

import scala.concurrent.{ExecutionContextExecutor, Future}
import spray.json.DefaultJsonProtocol

trait Protocols extends DefaultJsonProtocol {
  import scenarios._

  implicit val OrderItemFormat = jsonFormat2(OrderItem.apply)
  implicit val OrderFormat = jsonFormat3(Order.apply)
  implicit val OrderSummaryFormat = jsonFormat2(OrderSummary.apply)
  implicit val OrbitalCoordinatesFormat = jsonFormat3(OrbitalCoordinates.apply)
  implicit val AuthenticationInfoFormat = jsonFormat3(AuthenticationInfo.apply)
  implicit val ProductAddRequestFormat = jsonFormat3(ProductAddRequest.apply)
  implicit val ProductAddResponseFormat = jsonFormat2(ProductAddResponse.apply)
}

trait Service extends Protocols {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  def config: Config
  val logger: LoggingAdapter

//  lazy val ipApiConnectionFlow: Flow[HttpRequest, HttpResponse, Any] =
//    Http().outgoingConnection(config.getString("services.ip-api.host"), config.getInt("services.ip-api.port"))
//
//  def ipApiRequest(request: HttpRequest): Future[HttpResponse] = Source.single(request).via(ipApiConnectionFlow).runWith(Sink.head)
//
//  def fetchIpInfo(ip: String): Future[Either[String, IpInfo]] = {
//    ipApiRequest(RequestBuilding.Get(s"/json/$ip")).flatMap { response =>
//      response.status match {
//        case OK => Unmarshal(response.entity).to[IpInfo].map(Right(_))
//        case BadRequest => Future.successful(Left(s"$ip: incorrect IP format"))
//        case _ => Unmarshal(response.entity).to[String].flatMap { entity =>
//          val error = s"FreeGeoIP request failed with status code ${response.status} and entity $entity"
//          logger.error(error)
//          Future.failed(new IOException(error))
//        }
//      }
//    }
//  }
//
//  val routes = {
//    logRequestResult("akka-http-microservice") {
//      pathPrefix("ip") {
//        (get & path(Segment)) { ip =>
//          complete {
//            fetchIpInfo(ip).map[ToResponseMarshallable] {
//              case Right(ipInfo) => ipInfo
//              case Left(errorMessage) => BadRequest -> errorMessage
//            }
//          }
//        } ~
//        (post & entity(as[IpPairSummaryRequest])) { ipPairSummaryRequest =>
//          complete {
//            val ip1InfoFuture = fetchIpInfo(ipPairSummaryRequest.ip1)
//            val ip2InfoFuture = fetchIpInfo(ipPairSummaryRequest.ip2)
//            ip1InfoFuture.zip(ip2InfoFuture).map[ToResponseMarshallable] {
//              case (Right(info1), Right(info2)) => IpPairSummary(info1, info2)
//              case (Left(errorMessage), _) => BadRequest -> errorMessage
//              case (_, Left(errorMessage)) => BadRequest -> errorMessage
//            }
//          }
//        }
//      }
//    }

  def routes = {
    import domain.Services._
    import scenarios._

    logRequestResult("akka-http-microservice") {
      pathPrefix(orderMgmr) {
        /*(get & path(Segment)) { ip =>
          complete {
            fetchIpInfo(ip).map[ToResponseMarshallable] {
              case Right(ipInfo) => ipInfo
              case Left(errorMessage) => BadRequest -> errorMessage
            }
          }
        } ~*/
          (post & entity(as[ProductAddRequest])) { productAddRequest =>
            complete {

              //ProductAddSrv1.execute(productAddRequest).map[ToResponseMarshallable]
              //Future{ProductAddTest1.productAddResponse}.map[ToResponseMarshallable]
              null
            }
          }
      }
    }
  }

}

object AkkaHttpMicroservice extends App with Service {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()

  override val config = ConfigFactory.load()
  override val logger = Logging(system, getClass)

  Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))
}
