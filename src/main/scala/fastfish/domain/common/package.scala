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
  type Currency = Int
  type Amount = (BigDecimal, Currency)

  trait Address
  trait PaymentInstrument
}
