import akka.actor.ActorSystem
import akka.event.{LoggingAdapter, Logging}
import akka.http.scaladsl.Http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._ //required
import akka.http.scaladsl.marshalling.ToResponseMarshallable //required
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
  implicit val ProductAddRequestFormat = jsonFormat3(ProductAddRequest.apply)
  implicit val ProductAddResponseFormat = jsonFormat2(ProductAddResponse.apply)
}

trait Service extends Protocols {
  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  def config: Config
  val logger: LoggingAdapter

  val routes = {
    import domain.Services._
    import scenarios._

    logRequestResult("akka-http-microservice") {
      pathPrefix(orderMgmr.name) {
          (post & path(orderMgmr.api.productAdd) & entity(as[ProductAddRequest])) { productAddRequest =>
            complete {

              //@todo ToResponseMarshallable.apply(ProductAddTest.OrderMgmtService_productAdd.productAdd(productAddRequest))
              ToResponseMarshallable.apply(ProductAddTest.OrderMgmtService_productAdd.response)
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
