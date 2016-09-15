package fastfish.domain

/**
  *
  */
package object common {

  trait DomainEvent

  trait BusinessService

  /**
    * @arch Is allow to mediate across multiple [[BusinessService]]s
    */
  trait BusinessProcess
  trait BusinessException

  /**
    * Really part of the public domain language
    */
  type CustomerId = Long
  type ProductId = Long

  trait Address
  trait PaymentInstrument
}
