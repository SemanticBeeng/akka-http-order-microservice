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
  import orderMgmr._
  import scenarios.{ProductAddResponse, Order}

  "OrderMgr" should "allow adding a product given any customer auth info" in {

    import scenarios.ProductAddTest._

    Post(s"/${orderMgmr.name}/${api.productAdd}", orderMgmtService.request) ~> routes ~> check {
      status shouldBe OK
      contentType shouldBe `application/json`
      val response = responseAs[ProductAddResponse]
      println(response)
      response.result shouldBe true
      response.orderSummary.customerId shouldBe customer._3
      response.orderSummary.order.details.size shouldBe 1
      response.orderSummary.order.details.head.productId shouldBe xspaceIceCream
    }
  }

  "OrderMgr" should "allow viewing order history given any customer id" in {

    import scenarios.OrdersViewTest._

    Get(s"/${orderMgmr.name}/${api.ordersView}/${orderMgmtService.request}") ~> routes ~> check {
      status shouldBe OK
      contentType shouldBe `application/json`
      val r = responseAs[List[Order]]
      println(r)
      r shouldBe orderMgmtService.response
    }
  }
}