package fastfish.domain

import scala.concurrent.Future

//
import fastfish.domain.common.BusinessService
import fastfish.domain.common.BusinessException


/**
  *
  */
package object catalogMgmt {

  type ProductId = Long

  trait Product {
    def id : ProductId
    def description: String
  }

  trait ProductCatalog extends BusinessService {

    def catalogProduct(p: Product) : Future[Either[DuplicateProductException, Unit]]

    def deactivateProduct(pid: ProductId) : Unit

    def uncatalogProduct(pid: ProductId) : Future[Either[ProductInUseException, Unit]]
  }

  trait DuplicateProductException extends BusinessException
  trait ProductInUseException extends BusinessException
}
