import domain.Services.orderMgmr.api

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

/**
  *
  */
package object scenarios {

  case class OrbitalCoordinates(x : domain.Coordinate, y: domain.Coordinate, z: domain.Coordinate) extends domain.OrbitalCoordinates

  case class AuthenticationInfo(fName : String, lName : String, orbitalCoordinates: OrbitalCoordinates) extends domain.AuthenticationInfo

  case class OrderSummary(customerId : domain.CustomerId, order : Order) extends domain.OrderSummary

  case class OrderItem(productId : domain.ProductId, qty : domain.ProductQty) extends domain.OrderItem

  case class Order(customerId : domain.CustomerId, date: String, details: List[OrderItem]) extends domain.Order

  trait TestScenario

  trait BasicData {

    val customer = ("Nick", "V", 230404)
    val coord = OrbitalCoordinates(x = 12.3, y = 34.5, z = 45.6)
    val auth = AuthenticationInfo(customer._1, customer._2, coord)

    val xspaceIceCream = 1020203
    val xspaceBloodSugarPills = 1020205

    val qty = 7
  }

//  case class ProductAddRequest(auth: AuthenticationInfo, productId: domain.ProductId, qty: domain.ProductQty) extends domain.ProductAddRequest
//
//  case class ProductAddResponse(result : domain.ResultCode, orderSummary : OrderSummary) extends domain.ProductAddResponse

  /**
    *
    */
  object ProductAddTest extends TestScenario with BasicData {

    val order = Order(customer._3, date = "2016/08/27", details = List(OrderItem(xspaceIceCream, qty)))
    val orderSummary : OrderSummary = OrderSummary(customer._3, order)
    val result: domain.ResultCode = true

    val orderMgmtService = new domain.OrderMgmtService[AuthenticationInfo, OrderSummary, Order] {

      val request = /*ProductAddRequest*/(auth, xspaceIceCream, qty)
      val response = /*ProductAddResponse*/(result, orderSummary)

      /**
        * @see [[api.productAdd]]
        */
      def productAdd(auth: AuthenticationInfo, productId: domain.ProductId, qty: domain.ProductQty)
      : Future[(domain.ResultCode, OrderSummary)] = {
        Future{ response }
      }

      /**
        * @see [[api.orderPlace]]
        */
      override def orderPlace(): Unit = ???

      /**
        * @see [[api.ordersView]]
        */
      override def ordersView(customerId : domain.CustomerId): Future[List[Order]] = ???
    }
  }

  object OrdersViewTest extends TestScenario with BasicData {

    val order1 = Order(customer._3, date = "2016/08/27", details = List(OrderItem(xspaceIceCream, qty)))
    val order2 = Order(customer._3, date = "2016/08/28", details = List(OrderItem(xspaceBloodSugarPills, qty * 2)))
    val someOtherCustomerId = 10202
    val order3 = Order(10202, date = "2017/08/27", details = List(OrderItem(xspaceIceCream, qty)))

    case class DbEntry(customerId: domain.CustomerId, order: Order)

    val orders : List[DbEntry] = List(
      DbEntry(order1.customerId, order1),
      DbEntry(order2.customerId, order2),
      DbEntry(order3.customerId, order3)
    )

    val orderMgmtService = new domain.OrderMgmtService[AuthenticationInfo, OrderSummary, Order] {

      /**
        * @see [[api.productAdd]]
        */
      def productAdd(auth: AuthenticationInfo, productId: domain.ProductId, qty: domain.ProductQty)
      : Future[(domain.ResultCode, OrderSummary)]  = ???

      /**
        * @see [[api.orderPlace]]
        */
      override def orderPlace(): Unit = ???

      def ordersFor(customerId: domain.CustomerId) : List[Order] = {

        orders.filter(e => e.customerId == customerId).map(e => e.order)
      }

      val request = customer._3
      val response = ordersFor(customer._3)

      /**
        * @see [[api.ordersView]]
        */
      override def ordersView(customerId : domain.CustomerId): Future[List[Order]] = {
        Future { response }
      }
    }
  }
}
