import akka.actor.ActorSystem
import akka.event.{LoggingAdapter, Logging}
import akka.http.scaladsl.Http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._ //required
import akka.http.scaladsl.marshalling._ //required
import akka.http.scaladsl.unmarshalling.Unmarshal //required
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
//  implicit val ProductAddRequestFormat = jsonFormat3(ProductAddRequest.apply)
//  implicit val ProductAddResponseFormat = jsonFormat2(ProductAddResponse.apply)
}

trait Service extends Protocols {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  def config: Config
  val logger: LoggingAdapter

  def routes(service : domain.OrderMgmtService[scenarios.AuthenticationInfo, scenarios.OrderSummary, scenarios.Order]) = {
    import domain.Services._
    import scenarios._
    import orderMgmr._

    logRequestResult("akka-http-microservice") {
      pathPrefix(orderMgmr.name) {

        /**
          *
          */
        (post & pathPrefix(api.productAdd) & entity(as[(AuthenticationInfo, domain.ProductId, domain.ProductQty)])) {
          request: (AuthenticationInfo, domain.ProductId, domain.ProductQty) =>

          complete {

            ToResponseMarshallable.apply(service.productAdd(request._1, request._2, request._3))
          }
        } ~
          /**
            *
            */
        (get & pathPrefix(api.ordersView) & path(Segment)) { customerId =>
          complete {

            ToResponseMarshallable.apply(service.ordersView(customerId.toLong))
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

  val service = null //@todo: create production implementation
  Http().bindAndHandle(routes(service), config.getString("http.interface"), config.getInt("http.port"))
}
