package property

import models.User
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalatest.{MustMatchers, WordSpecLike}

class UserSpec extends WordSpecLike with MustMatchers with GeneratorDrivenPropertyChecks {

  "User" should {
    "not be constructable with a empty name" in {
      forAll {
        (str: String) => {
          whenever(str.isEmpty) {
            intercept[IllegalArgumentException] {
              new User(str)
            }
          }

          whenever(str.nonEmpty) {
            new User(str)
          }
        }
      }
    }
  }
}
