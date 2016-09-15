package fastfish.domain

import scala.concurrent.Future
//
import fastfish.domain.common.{CustomerId, BusinessService, BusinessException}

/**
  * @arch belongs to [[fastfish.architecture.CustomerMgmt_BoundedContext]]
  */
package object customerMgmt {

  type SafePassword = String

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
