package fastfish.domain

import scala.concurrent.Future

//
import fastfish.domain.common.{ProductId, BusinessService, BusinessException}


/**
  * @arch Belongs to [[fastfish.architecture.CatalogMgmt_BoundedContext]]
  */
package object catalogMgmt {

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
