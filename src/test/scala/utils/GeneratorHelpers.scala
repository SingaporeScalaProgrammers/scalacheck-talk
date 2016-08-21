package utils

import models.Pokemon
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._


trait GeneratorHelpers {


  implicit val arbPokemon = Arbitrary(
    for {
      id <- arbitrary[Int]
      name <- arbitrary[String]
      pType <- arbitrary[String]
    } yield new Pokemon(id, name, pType)
  )
}
