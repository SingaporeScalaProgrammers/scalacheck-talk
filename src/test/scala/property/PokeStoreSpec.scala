package property

import models.{PokeStore, Pokemon, Player}
import org.scalacheck.{Prop, Gen}
import org.scalacheck.Arbitrary._
import org.scalacheck.commands.Commands
import org.scalatest.{WordSpecLike, MustMatchers}
import org.scalatest.prop.{Checkers, GeneratorDrivenPropertyChecks}
import utils.GeneratorHelpers

import collection.JavaConversions._
import scala.util.Try

class PokeStoreSpec
  extends WordSpecLike
    with MustMatchers
    with Checkers {

  "PokeStore" should {
    "support store, list, transfer of pokemons for a player" in {
      val testPlayer = new Player(1)
      check(new SinglePlayerPokeStoreSpec(testPlayer).property())
    }
  }

}

class SinglePlayerPokeStoreSpec(player: Player)
  extends Commands
    with GeneratorHelpers {

  override type State = List[Pokemon]

  override def destroySut(sut: Sut): Unit = sut.clearAll()

  override def initialPreCondition(state: State): Boolean = true

  override def canCreateNewSut(newState: State,
                               initSuts: Traversable[State],
                               runningSuts: Traversable[Sut]): Boolean = {
    initSuts.isEmpty && runningSuts.isEmpty
  }

  override def genInitialState: Gen[State] = {
    Gen.listOf(arbitrary[Pokemon])
  }

  override def newSut(state: State): Sut = {
    val sut = new PokeStore()

    state.foreach(sut.storePokemon(player, _))

    sut
  }

  override def genCommand(state: State): Gen[Command] = {
    Gen.oneOf(
      Gen.const(List),
      arbitrary[Pokemon].map(Store)
    )
  }

  override type Sut = PokeStore


  case object List extends Command {

    override def preCondition(state: List[Pokemon]): Boolean = true

    override def run(sut: PokeStore): Result = sut.listPokemon(player).toList

    override def nextState(state: State): State = state

    override type Result = List[Pokemon]

    override def postCondition(state: List[Pokemon], result: Try[Result]): Prop =
      result.isSuccess && (
        result.get.size == state.size &&
        result.get.forall(state.contains)
        )
  }

  case class Store(pokemon: Pokemon) extends Command {
    override type Result = Unit

    override def preCondition(state: List[Pokemon]): Boolean = true

    override def postCondition(state: List[Pokemon], result: Try[Result]): Prop = result.isSuccess

    override def run(sut: PokeStore): Result = sut.storePokemon(player, pokemon)

    override def nextState(state: List[Pokemon]): List[Pokemon] = pokemon :: state
  }
}
