package fastfish.domain

import scala.concurrent.Future

//
import fastfish.domain.common.BusinessService
import fastfish.domain.common.BusinessException
import fastfish.domain.catalogMgmt.ProductId



/**
  *
  */
package object inventoryMgmt {

  type ProductQty = Double

  trait ProductInventory {
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
    def provisionProduct(pid: ProductId, qty: ProductQty) : Future[Either[InventoryException, ProductInventory]]


    /**
      * Reserve stock for [[Product]] for some purpose, usually shopping but may be other reasons.
      * This temporarily reduces the available stock without affecting the actual [[ProductInventory]]
      */
    def reserveProduct(pid: ProductId, qty: ProductQty): Future[Either[InventoryException, ProductReservation]]

    /**
      * Cancel the [[ProductReservation]]s made through [[reserveProduct]], thus undoing it's effect
      * on the respective [[ProductInventory]]
      */
    def cancelReservations(pres: List[ProductReservation]): Future[Either[InventoryException, List[ProductReservation]]]

    /**
      * Commit the [[ProductReservation]]s made through [[reserveProduct]].
      * This restores the actual [[ProductInventory]] by the [[ProductQty]] indicated in [[ProductReservation]]s.
      */
    def commitReservations(pres: List[ProductReservation]): Future[Either[InventoryException, List[ProductReservation]]]
  }

  trait  InventoryException extends BusinessException
}
