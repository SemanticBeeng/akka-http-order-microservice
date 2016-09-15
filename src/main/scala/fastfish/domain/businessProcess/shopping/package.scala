package fastfish.domain.businessProcess

import scala.concurrent.Future
//
import fastfish.domain.common.BusinessProcess
import fastfish.domain.common.BusinessException
//
import fastfish.domain.catalogMgmt._
import fastfish.domain.customerMgmt._
import fastfish.domain.inventoryMgmt._


/**
  * @arch Belongs to [[fastfish.architecture.Shopping_BoundedContext]]
  */
package object shopping {

  type NonEmptyList = List

  trait Address
  trait PaymentInstrument

  trait BillingTo {
    def address : Address
    def payWith: PaymentInstrument
  }

  trait ShippingTo {
    def address : Address
  }

  trait OrderItem extends ProductReservation
  trait Order {

    def items: List[OrderItem]
  }

  trait ShoppingProcess extends BusinessProcess {

    def startOrderFor(customer: RegisteredCustomer): Future[Either[OrderInProgressException, Order]]
    def shopProducts(order: Order, items: List[(Product, ProductQty)]) : Future[Order]
    def recommendRelatedFor(order: Order): Future[List[Product]]
    def checkoutOrder(order: Order, billTo: BillingTo, shipTo: ShippingTo) : Future[NonEmptyList[OrderCheckoutError] \/ Order]
    def expireOrder(order: Order) : Future[Order]
  }

  trait OrderInProgressException extends BusinessException
  trait OrderCheckoutError extends BusinessException
}
