package fastfish.domain

import scala.concurrent.Future
//
import fastfish.domain.common.{CustomerId, BusinessService}
import fastfish.domain.catalogMgmt.Product


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
}
