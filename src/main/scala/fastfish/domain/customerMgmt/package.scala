package fastfish.domain

import scala.concurrent.Future
//
import fastfish.domain.common.BusinessService
import fastfish.domain.common.BusinessException

/**
  *
  */
package object customerMgmt {

  type CustomerId = Long
  type SafePassword = Stirng

  trait AuthCredentials {
    def id: String
    def credentials: SafePassword
  }

  trait Customer {

    def id: CustomerId
    def firstName: String
    def lastName: String
  }

  trait RegisteredCustomer {

    def customer: RegisteredCustomer
    def authenticatedWith: AuthCredentials
  }

  trait CustomerManagement extends BusinessService {

    def signupCustomer(customer: Customer, auth: AuthCredentials)
    : Future[Either[DuplicateAccountException, RegisteredCustomer]]
  }

  trait DuplicateAccountException extends BusinessException
}
