package utils

import models.Pokemon
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary._


trait GeneratorHelpers {

  implicit lazy val pokemon =
    for {
      id <- arbitrary[Int]
      name <- arbitrary[String]
      pType <- arbitrary[String]
    } yield new Pokemon(id, name, pType)

  implicit def arbPokemon = Arbitrary(
    pokemon
  )

}
