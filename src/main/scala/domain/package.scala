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

  object Services {

    object orderMgmr  {
      val name = "orderMgmr"

      object api {
        val productAdd = "productAdd"
        val orderPlace = "orderPlace"
        val ordersView = "ordersView"
      }
    }
  }

  trait OrderMgmtService extends Service {

    /**
      * @see [[domain.Services.orderMgmr.api.productAdd]]
      */
    def productAdd(request: ProductAddRequest)(implicit ec: ExecutionContext) : Future[ProductAddResponse]

    /**
      * @see [[domain.Services.orderMgmr.api.orderPlace]]
      */
    def orderPlace(/*@todo*/) /*@todo*/

    /**
      * @see [[domain.Services.orderMgmr.api.ordersView]]
      */
    def ordersView(customerId : domain.CustomerId) : Future[List[Order]]
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
}
