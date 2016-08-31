package utils

import java.util.Date

import models.{CatchEntry, Pokemon}
import org.scalacheck.{Arbitrary, Gen}
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

  val catches = for{
    time <- Gen.posNum[Long]
    pokemon <- pokemon
  } yield new CatchEntry(pokemon,new Date(time))

}
