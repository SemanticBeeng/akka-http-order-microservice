package fastfish.domain

/**
  *
  */
package object common {

  type CustomerId = Long
  type ProductId = Long

  trait BusinessService

  /**
    * @arch Is allow to mediate across multiple [[BusinessService]]s
    */
  trait BusinessProcess
  trait BusinessException
}
