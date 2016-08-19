package unit

import models.User
import org.scalatest.{MustMatchers, WordSpecLike}

class UserSpec extends WordSpecLike with MustMatchers {

  "User" should {
    "not be constructable with a null name" in {
      intercept[IllegalArgumentException](new User(null,"address"))
    }
    "should not be constructable with a null address" in {
      intercept[IllegalArgumentException](new User("name",null))

    }
  }
}
