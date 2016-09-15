package fastfish.domain

import scala.concurrent.Future

//
import fastfish.domain.common.BusinessService
import fastfish.domain.common.BusinessException
import fastfish.domain.common.ProductId


/**
  * @arch Belongs to [[fastfish.architecture.InventoryMgmt_BoundedContext]]
  */
package object inventoryMgmt {

  type ProductQty = Double

  trait ProductAvailability {
    def productId : ProductId
    def qty: ProductQty
  }

  trait ProductReservation {
    def productId : ProductId
    def qty: ProductQty
  }

  trait ProductInventory extends BusinessService {

    /**
      * Accrue stock for a [[Product]]
      */
    def provisionProduct(pid: ProductId, qty: ProductQty) : Future[Either[InventoryException, ProductAvailability]]


    /**
      * Reserve stock for [[Product]] for some purpose, usually shopping but may be other reasons.
      * This temporarily reduces the available stock without affecting the actual inventory.
      */
    def reserveProduct(pid: ProductId, qty: ProductQty): Future[Either[InventoryException, ProductReservation]]

    /**
      * Cancel the [[ProductReservation]]s made through [[reserveProduct]], thus undoing it's effect
      * on the respective [[ProductAvailability]]
      */
    def cancelReservations(pres: List[ProductReservation]): Future[Either[InventoryException, List[ProductReservation]]]

    /**
      * Commit the [[ProductReservation]]s made through [[reserveProduct]].
      * This restores the actual [[ProductAvailability]] by the [[ProductQty]] indicated in [[ProductReservation]]s.
      */
    def commitReservations(pres: List[ProductReservation]): Future[Either[InventoryException, List[ProductReservation]]]
  }

  trait  InventoryException extends BusinessException
}
