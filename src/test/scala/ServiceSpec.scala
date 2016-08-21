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
  import scenarios.Order

  "OrderMgr" should "allow adding a product given any customer auth info" in {

    import scenarios.ProductAddTest._

    Post(s"/${orderMgmr.name}/${api.productAdd}", orderMgmtService.request) ~> routes(orderMgmtService) ~> check {
      status shouldBe OK
      contentType shouldBe `application/json`
      val response = responseAs[(domain.ResultCode, scenarios.OrderSummary)]
      println(response)
      response._1 shouldBe true
      response._2.customerId shouldBe customer._3
      response._2.order.details.size shouldBe 1
      response._2.order.details.head.productId shouldBe xspaceIceCream
    }
  }

  "OrderMgr" should "allow viewing order history given any customer id" in {

    import scenarios.OrdersViewTest._

    Get(s"/${orderMgmr.name}/${api.ordersView}/${orderMgmtService.request}") ~> routes(orderMgmtService) ~> check {
      status shouldBe OK
      contentType shouldBe `application/json`
      val response = responseAs[List[Order]]
      println(response)
      response.size shouldBe 2
      response.dropWhile(_.customerId == customer._3) shouldBe List.empty
    }
  }
}