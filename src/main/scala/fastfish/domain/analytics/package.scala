package fastfish.domain

import scala.concurrent.Future
//
import fastfish.domain.common.{ProductId, CustomerId, BusinessService}


/**
  * @arch Belongs to [[fastfish.architecture.Analytics_BoundedContext]]
  */
package object analytics {

  type Preference = String

  /**
    * The [[analytics]] view of a [[fastfish.domain.customerMgmt.Customer]]
    * @arch : do not reference [[fastfish.domain.customerMgmt.Customer]] across the context boundary
    */
  trait CustomerPreferences {

    def customerId : CustomerId
    def preferences : List[Preference]
  }

  /**
    * Accrues over the life cycle of the [[fastfish.domain.customerMgmt.Customer]] through the interaction s/he
    * has with the system [[BusinessService]]s and [[fastfish.domain.common.BusinessProcess]]es
    */
  trait CustomerActivityHistory

  trait ProductRecommendations extends BusinessService {

    /**
      * Recommends [[Product]]s to [[fastfish.domain.customerMgmt.Customer]]s based on their [[CustomerActivityHistory]].
      */
    def recommendFor(customerId: CustomerId, history: CustomerActivityHistory) : Future[List[Product]]
  }

  type Timestamp = Double

  trait CustomerActivityMonitor extends BusinessService {

    /**
      * @todo: handle [[fastfish.architecture.violations.boundaryCrossed]]
      */
    import fastfish.domain.inventoryMgmt.ProductReservation

    def shoppedProduct(customerId: CustomerId, productId: ProductId, time: Timestamp): Unit
    def reservationExpired(customerId: CustomerId, res: ProductReservation, time: Timestamp): Unit
    def customerActivity(customerId: CustomerId): Future[CustomerActivityHistory]
  }

}
