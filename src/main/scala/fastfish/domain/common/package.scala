package fastfish.domain

/**
  *
  */
package object common {

  trait BusinessService

  /**
    * @arch Is allow to mediate across multiple [[BusinessService]]s
    */
  trait BusinessProcess
  trait BusinessException
}
