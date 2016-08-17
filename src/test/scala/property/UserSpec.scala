package property

import models.User
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{Assertions, MustMatchers, WordSpecLike}
import org.scalatest.Assertions._
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen

class UserSpec extends WordSpecLike with MustMatchers with GeneratorDrivenPropertyChecks with Assertions {

  val stringOrNull =  Gen.oneOf(Gen.const(null),arbitrary[String])

  val users = for {
    name <- arbitrary[String]
    address <- arbitrary[String]
  } yield new User(name,address)


  "User" should {
    "not be constructable with null parameters" in {
      forAll(stringOrNull,stringOrNull) {
        (name: String, address : String) => {
          if(name == null || address == null) {
            intercept[IllegalArgumentException](new User(name,address))
          }else{
            new User(name,address)
          }
        }
      }
    }
    "compare to other users correctly" in {
      pending
      forAll(users,users) {
        (user1 : User, user2) => {
        }
      }
    }
  }
}
