import scala.concurrent.{ExecutionContext, Future}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  *
  */
package object scenarios {

  case class OrbitalCoordinates(x : domain.Coordinate, y: domain.Coordinate, z: domain.Coordinate) extends domain.OrbitalCoordinates

  case class AuthenticationInfo(fName : String, lName : String, orbitalCoordinates: OrbitalCoordinates) extends domain.AuthenticationInfo

  case class ProductAddRequest(auth: AuthenticationInfo, productId: domain.ProductId, qty: domain.ProductQty) extends domain.ProductAddRequest

  case class ProductAddResponse(result : domain.ResultCode, orderSummary : OrderSummary) extends domain.ProductAddResponse

  case class OrderSummary(customerId : domain.CustomerId, order : Order) extends domain.OrderSummary

  case class OrderItem(productId : domain.ProductId, qty : domain.ProductQty) extends domain.OrderItem

  case class Order(customerId : domain.CustomerId, date: String, details: List[OrderItem]) extends domain.Order

  trait TestScenario

  /**
    *
    */
  object ProductAddTest1 extends TestScenario {

    val customer = ("Nick", "V", 230404)
    val coord = OrbitalCoordinates(x = 12.3, y = 34.5, z = 45.6)
    val auth = AuthenticationInfo(customer._1, customer._2, coord)

    val productId = 1020203
    val qty = 7
    val order = Order(customer._3, date = "", details = List(OrderItem(productId, qty)))
    val orderSummary : OrderSummary = OrderSummary(customer._3, order)
    val result: domain.ResultCode = true

    val productAddRequest = ProductAddRequest(auth, productId, qty)
    val productAddResponse = ProductAddResponse(result, orderSummary)

    val ProductAddSrv1 = new domain.OrderMgmtService {

      def execute(request: domain.ProductAddRequest)(implicit ec: ExecutionContext) : Future[domain.ProductAddResponse] = {
        Future{ productAddResponse }
      }

      //def productAdd(auth: AuthenticationInfo, productId: ProductId, qty: ProductQty) : Future[Either[ResultCode, OrderSummary]]
    }
  }

  object OrderView {

    case class DbEntry(customerId: domain.CustomerId, order: Order)

    val orders : List[DbEntry] = ???

    def ordersFor(customerId: domain.CustomerId) : List[Order] = orders.filter(e => e.customerId == customerId).map(e => e.order)
  }
}
