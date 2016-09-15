package fastfish

/**
  *
  */
package object architecture {

  trait UbiquitousLanguage
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
    * Location [[fastfish.domain.analytics]]
    */
  object Analytics_BoundedContext extends BoundedContext

  /**
    * Location [[fastfish.domain.businessProcess]]
    */
  object Shopping_BoundedContext extends BoundedContext



  object decisions {

    /**
      * Logic that composes multiple [[fastfish.domain.common.BusinessService]]s towards a large purpose.
      * This logic acts as a "Mediator" thus keeping individual services independent of the context and more reusable
      */
    val orchestrationLogic = ""

    val transformers = ""

    /**
      * Architecture and Design Rules
      */
    /**
      * Do not use the [[UbiquitousLanguage]] from a [[BoundedContext]] outside it's native context
      * See [[TransformDataAcrossContextBoundariesInATypeSafeWay]]
      */
    val DoNotUseLanguageAcrossBoundedContexts = ""

    /**
      * Encapsulate [[orchestrationLogic]] into [[fastfish.domain.common.BusinessProcess]]es.
      * [[fastfish.domain.common.BusinessService]]s cannot have [[orchestrationLogic]].
      */
    val BusinessServicesCanHaveNoOrchestrationLogic = ""

    /**
      * When implementing [[orchestrationLogic]] we often call onto different [[BoundedContext]]s which
      * speak their own [[UbiquitousLanguage]]. To avoid violating [[DoNotUseLanguageAcrossBoundedContexts]] we can use
      */
    val TransformDataAcrossContextBoundariesInATypeSafeWay = ""
  }
}
