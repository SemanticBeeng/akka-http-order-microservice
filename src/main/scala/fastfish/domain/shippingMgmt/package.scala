package fastfish.domain

/**
  * @arch violation of [[fastfish.architecture.decisions.DoNotUseLanguageAcrossBoundedContexts]]
  */

import fastfish.domain.inventoryMgmt.ProductReservation

import scala.concurrent.Future

//
import fastfish.domain.common._

/**
  * @arch Belongs to [[fastfish.architecture.Shipping_BoundedContext]]
  */
package object shippingMgmt {

  trait Status: String

  trait ShippingSchedule {
    def items: List[ProductReservation]
    def destination: Address
    def status : Status
  }

  trait Shipping extends BusinessProcess {

    /**
      * Attempts to schedule shipping and creates a [[ShippingSchedule]] and may "park" for human intervention if
      * shipping restrictions are identified.
      *
      * For example it will delegate to [[fastfish.domain.catalogMgmt.Validator.checkShippingRestrictions()]], through the proper
      * mechanisms, of course.
      *
      */
    def scheduleShipping(res: List[ProductReservation]) : Future[Either[ShippingParkedException, ShippingSchedule]]
  }

  trait ShippingParkedException extends BusinessException
}