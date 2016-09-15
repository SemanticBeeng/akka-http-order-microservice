package fastfish.domain

import scala.concurrent.Future
//
import fastfish.domain.common.{ProductId, CustomerId, BusinessService}


/**
  * @arch Belongs to [[fastfish.architecture.Analytics_BoundedContext]]
  */
package object analytics {

  trait Preference

  /**
    * The [[analytics]] view of a [[fastfish.domain.customerMgmt.Customer]]
    * @archRule [[fastfish.architecture.decisions.DoNotUseLanguageAcrossBoundedContexts]]
    *          Do not reference [[fastfish.domain.customerMgmt.Customer]] but transform to [[CustomerPreferences]]
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

  /**
    * Monitors and logs various [[fastfish.domain.common.DomainEvent]]s relevant to analytics that occur as the
    * [[fastfish.domain.customerMgmt.Customer]]s interact with the system.
    */
  trait CustomerActivityMonitor extends BusinessService {

    /**
      * @todo violated [[fastfish.architecture.decisions.DoNotUseLanguageAcrossBoundedContexts]]
      */
    import fastfish.domain.inventoryMgmt.ProductReservation

    def hadImpressionOnProduct(customerId: CustomerId, productId: ProductId, time: Timestamp): Unit

    def browsedProduct(customerId: CustomerId, productId: ProductId, time: Timestamp): Unit

    def reviewedProduct(customerId: CustomerId, productId: ProductId, time: Timestamp): Unit

    def shoppedProduct(customerId: CustomerId, productId: ProductId, time: Timestamp): Unit

    def madeReservation(customerId: CustomerId, res: ProductReservation, time: Timestamp): Unit

    def reservationExpired(customerId: CustomerId, res: ProductReservation, time: Timestamp): Unit

    def provideCustomerActivity(customerId: CustomerId): Future[CustomerActivityHistory]
  }

}
