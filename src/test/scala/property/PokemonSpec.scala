package property

import models.Pokemon
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._
import org.scalacheck.Gen
import org.scalatest.WordSpec
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import utils.GeneratorHelpers

class PokemonSpec extends WordSpec with GeneratorDrivenPropertyChecks with GeneratorHelpers {


  def sameOrDifferent[T](implicit ev: Arbitrary[T]) = for {
    item <- ev.arbitrary
    pair <- Gen.oneOf((item, item), (item, ev.arbitrary.sample.get))
  } yield pair

  lazy val sameOrDifferentPokemon = for {
    ids <- sameOrDifferent[Int]
    names <- sameOrDifferent[String]
    types <- sameOrDifferent[String]
  } yield {
    (new Pokemon(ids._1, names._1, types._1), new Pokemon(ids._2, names._2, types._2))
  }

  //NOTE this prop is tricky to write because with 3 params if some don't match then the probability is that 2 don't match
  //therefor the prop finds it difficult to construct the failing example
  "Pokemon" should {
    "compare equality properly" in {
      forAll(sameOrDifferentPokemon) {
        (params: (Pokemon, Pokemon)) => {
          val pokemon1 = params._1
          val pokemon2 = params._2
          if (pokemon1.getId == pokemon2.getId &&
            pokemon1.getName.equals(pokemon2.getName) &&
            pokemon1.getType.equals(pokemon2.getType)) {
            assert(pokemon1 == pokemon2)
          } else {
            assert(pokemon1 != pokemon2)
          }
        }
      }
    }
    "if they are equal then the hashCode should be the same" in {
      forAll(sameOrDifferentPokemon) {
        (params: (Pokemon, Pokemon)) => {
          val pokemon1 = params._1
          val pokemon2 = params._2
          if (pokemon1 == pokemon2) assert(pokemon1.hashCode() == pokemon2.hashCode())
        }
      }

    }
  }


}
