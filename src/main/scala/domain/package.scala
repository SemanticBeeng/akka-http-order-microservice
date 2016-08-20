import scala.concurrent.{ExecutionContext, Future}

/**
  *
  */
package object domain {

  type Coordinate = Double
  type CustomerId = Long
  type ProductId = Long
  type ProductQty = Double
  type ResultCode = Boolean

  trait OrbitalCoordinates {
    def x : Coordinate
    def y: Coordinate
    def z: Coordinate
  }

  trait AuthenticationInfo {
    def fName : String

    def lName : String

    def orbitalCoordinates: OrbitalCoordinates
  }

  trait Product {
    def id : ProductId
    def description: String
  }

  trait OrderItem {
    def productId : ProductId
    def qty : ProductQty
  }

  trait Order {
    def customerId : CustomerId

    def date: String

    def details: List[OrderItem]
  }

  sealed trait Service
  sealed trait Request
  sealed trait Response

  trait OrderSummary {

    def customerId : CustomerId

    def order : Order
  }

  trait OrderMgmtService extends Service {

    //def productAdd(auth: AuthenticationInfo, productId: ProductId, qty: ProductQty) : Future[Either[ResultCode, OrderSummary]]
//    def request: ProductAddRequest
//    def response: ProductAddResponse
    def execute(request: ProductAddRequest)(implicit ec: ExecutionContext) : Future[ProductAddResponse]
  }

  trait ProductAddRequest extends Request {
    def auth: AuthenticationInfo
    def productId: ProductId
    def qty: ProductQty
  }

  trait ProductAddResponse extends Response {
    def result : ResultCode
    def orderSummary : OrderSummary
  }

//  trait PlaceOrder extends Command
//
//  trait ReviewOrderHistory extends Command

  object Services {
    val orderMgmr  = "orderMgmr"

  }

}
