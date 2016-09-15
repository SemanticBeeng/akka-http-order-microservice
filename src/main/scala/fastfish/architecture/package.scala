package fastfish

/**
  *
  */
package object architecture {

  trait BoundedContext

  /**
    * Location [[fastfish.domain.catalogMgmt]]
    */
  object CatalogMgmt_BoundedContext extends BoundedContext

  /**
    * Location [[fastfish.domain.inventoryMgmt]]
    */
  object InventoryMgmt_BoundedContext extends BoundedContext

  /**
    * Location [[fastfish.domain.customerMgmt]]
    */
  object CustomerMgmt_BoundedContext extends BoundedContext

  /**
    * Location [[fastfish.domain.businessProcess.shopping]]
    */
  object Shopping_BoundedContext extends BoundedContext

  /**
    * Location [[fastfish.domain.analytics]]
    */
  object Analytics_BoundedContext extends BoundedContext

  object violations {

    val boundaryCrossed
  }
}
