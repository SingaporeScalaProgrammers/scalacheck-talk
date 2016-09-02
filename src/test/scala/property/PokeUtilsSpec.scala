package property

import models.{CatchEntry, PokeUtils}
import org.scalacheck.Gen
import org.scalatest.WordSpec
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import utils.GeneratorHelpers
import scala.collection.JavaConversions._

class PokeUtilsSpec extends WordSpec with GeneratorDrivenPropertyChecks with GeneratorHelpers{
  "PokeStore" should {
    //1st edge case the empty list
    "catch rate should be > 0 if list is non empty" in {
      forAll(Gen.listOfN(2, catches)) {
        (catches: List[CatchEntry]) => {
          if (catches.isEmpty || catches.size == 1) {
            //fixes for bug #1 and bug#2
            assert(PokeUtils.catchRate(catches) == 0.0)
          }
          else assert(PokeUtils.catchRate(catches) > 0) //bug #3 is that a catch rate of 0.0 is returned when you catch 2 pokemon on the same day
        }
      }
    }
  }
}
