package utils

import java.util.Date

import models.{CatchEntry, Pokemon}
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary._


trait GeneratorHelpers {

  def shortString: Gen[String] = arbitrary[String].map(_.take(6))

  implicit lazy val pokemon =
    for {
      id <- arbitrary[Int]
      name <- shortString
      pType <- shortString
    } yield new Pokemon(id, name, pType)


  val catches = for{
    time <- Gen.posNum[Long]
    pokemon <- pokemon
  } yield new CatchEntry(pokemon,new Date(time))

}
