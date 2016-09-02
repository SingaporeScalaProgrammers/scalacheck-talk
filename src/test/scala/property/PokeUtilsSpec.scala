package property

import models.{CatchEntry, PokeUtils}
import org.scalacheck.Gen
import org.scalatest.WordSpec
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import utils.GeneratorHelpers
import scala.collection.JavaConversions._

class PokeUtilsSpec extends WordSpec with GeneratorDrivenPropertyChecks with GeneratorHelpers{
  "PokeUtils" should {
    //1st edge case the empty list
    "catch rate should be >= 0" in {
      forAll(Gen.listOf(catches)) {
        (catches: List[CatchEntry]) => assert(PokeUtils.catchRate(catches) >= 0) //bug #3 is that a catch rate of 0.0 is returned when you catch 2 pokemon on the same day
      }
    }
  }
}
