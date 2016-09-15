package fastfish.domain

import scala.concurrent.Future

//
import fastfish.domain.common.{Address, ProductId, BusinessService, BusinessException}


/**
  * @arch Belongs to [[fastfish.architecture.CatalogMgmt_BoundedContext]]
  */
package object catalogMgmt {

  trait ProductCategory {

  }

  trait Product {

    def id : ProductId
    def description: String
    def category : ProductCategory
  }

  trait ProductCatalog extends BusinessService {

    def catalogProduct(p: Product) : Future[Either[DuplicateProductException, Unit]]

    def deactivateProduct(pid: ProductId) : Unit

    def uncatalogProduct(pid: ProductId) : Future[Either[ProductInUseException, Unit]]
  }

  trait DuplicateProductException extends BusinessException
  trait ProductInUseException extends BusinessException
  trait ProductNotAllowedException extends BusinessException

  /**
    * @todo Not sure that this is the best place for this logic
    */
  object Validator {

    /**
      * @todo implement [[DomainRules.Inv_HuntingGearIsNotAllowedUnderAge]]
      */
    def checkAgeRestrictions(age: Int, product: Product) : List[ProductNotAllowedException]  = ???

    /**
      * @todo implement [[DomainRules.Inv_PerisableCannotBeShippedBeyondMaxDistance]]
      */
    def checkShippingRestrictions(destination: Address, product: Product) : List[ProductNotAllowedException]  = ???
  }

  object DomainRules {

    /**
      * [[Product]]s in the "Hunting Gear" [[ProductCategory]] is not allowed under age of 18
      */
    val Inv_HuntingGearIsNotAllowedUnderAge = ""

    /**
      * [[Product]]s that are perisable cannot be shipped more that 100K
      */
    val Inv_PerisableCannotBeShippedBeyondMaxDistance = ""
  }
}
