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

  "OrderMgr" should "allow adding a product given any customer auth info" in {

    import ProductAddTest._
    import orderMgmr._

    Post(s"/${orderMgmr.name}/${api.productAdd}", orderMgmtService.request) ~> routes ~> check {
      status shouldBe OK
      contentType shouldBe `application/json`
      val r = responseAs[ProductAddResponse]
      println(r)
      r shouldBe orderMgmtService.response
    }
  }

  "OrderMgr" should "allow viewing order history given any customer id" in {

    import OrdersViewTest._
    import orderMgmr._

    Get(s"/${orderMgmr.name}/${api.ordersView}/${orderMgmtService.request}") ~> routes ~> check {
      status shouldBe OK
      contentType shouldBe `application/json`
      val r = responseAs[List[Order]]
      println(r)
      r shouldBe orderMgmtService.response
    }
  }
}