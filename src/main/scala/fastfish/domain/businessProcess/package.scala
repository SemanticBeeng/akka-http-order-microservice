package fastfish.domain

import scala.concurrent.Future
//
import fastfish.domain.common.{PaymentInstrument, Address, BusinessProcess, BusinessException}
//
import fastfish.domain.catalogMgmt._
import fastfish.domain.customerMgmt._
import fastfish.domain.inventoryMgmt._

/**
  *
  */
package object businessProcess {

  type NonEmptyList[T] = List[T]

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

    def startOrderFor(customer: RegisteredCustomer)
      : Future[Either[OrderInProgressException, Order]]

    def shopProducts(order: Order, items: List[(Product, ProductQty)]) : Future[Order]

    def recommendRelatedFor(order: Order) : Future[List[Product]]

    def checkoutOrder(order: Order, billTo: BillingTo, shipTo: ShippingTo)
      : Future[Either[NonEmptyList[OrderCheckoutError], Order]]

    def expireOrder(order: Order) : Future[Order]
  }

  trait OrderInProgressException extends BusinessException
  trait OrderCheckoutError extends BusinessException
}
