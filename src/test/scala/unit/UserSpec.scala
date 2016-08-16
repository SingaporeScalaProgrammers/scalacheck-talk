package unit

import models.User
import org.scalatest.{MustMatchers, WordSpecLike}

class UserSpec extends WordSpecLike with MustMatchers {

  "User" should {
    "not be constructable with a empty name" in {
      intercept[IllegalArgumentException] {
        new User("")
      }
    }
  }
}
