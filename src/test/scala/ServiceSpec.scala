import akka.event.NoLogging

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.ContentTypes._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest._

class ServiceSpec extends FlatSpec with Matchers with ScalatestRouteTest with Service with Protocols {
  override def testConfigSource = "akka.loglevel = WARNING"

  override def config = testConfig

  override val logger = NoLogging

  import domain.Services._
  import scenarios._
  import ProductAddTest1._

  "OrderMgr" should "allow adding a product given any customer auth info" in {

    ProductAddTest1.ProductAddSrv1.execute(productAddRequest)
    Post(s"/$orderMgmr", productAddRequest) ~> routes ~> check {
      status shouldBe OK
      contentType shouldBe `application/json`
      val r = responseAs[ProductAddResponse]
      println(r)
      responseAs[ProductAddResponse] shouldBe productAddResponse
    }

  }
}