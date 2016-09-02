package unit

import java.util.Date

import models.{CatchEntry, PokeStore, PokeUtils, Pokemon}
import org.scalatest.WordSpec

import scala.collection.JavaConversions._

class PokeUtilsUnitSpec extends WordSpec{

  "PokeStore.getCatchRate" should {
    "return 0 with no pokemon" in {
      val store = new PokeStore()
      assert(PokeUtils.catchRate(List.empty[CatchEntry]) == 0.0)
    }
    "return 0 with a single pokemon" in {
      val store = new PokeStore()
      assert(PokeUtils.catchRate(List(new CatchEntry(new Pokemon(0,"abc","def"),new Date()))) == 0.0)
    }
  }

}
