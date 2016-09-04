package property

import java.util.Date

import models.{CatchEntry, Player, PokeStore, Pokemon}
import org.scalacheck.Arbitrary._
import org.scalacheck.commands.Commands
import org.scalacheck.{Gen, Prop}
import org.scalatest.prop.{Checkers, GeneratorDrivenPropertyChecks}
import org.scalatest.{MustMatchers, WordSpecLike}
import utils.PokemonGenerators

import scala.collection.JavaConversions._
import scala.util.{Failure, Success, Try}

class PokeStoreSpec
  extends WordSpecLike
    with MustMatchers
    with Checkers
    with PokemonGenerators
    with GeneratorDrivenPropertyChecks {

  "PokeStore" should {
    "support store, list, transfer of pokemons for a player" in {
      check(new SinglePlayerPokeStoreSpec().property(threadCount = 2, maxParComb = 10000))
    }
  }

}

class SinglePlayerPokeStoreSpec()
  extends Commands
    with PokemonGenerators {

  override type State = List[Pokemon]

  override def destroySut(sut: Sut): Unit = ()

  override def initialPreCondition(state: State): Boolean = true

  override def canCreateNewSut(newState: State,
                               initSuts: Traversable[State],
                               runningSuts: Traversable[Sut]): Boolean = {
    initSuts.isEmpty && runningSuts.isEmpty
  }

  override def genInitialState: Gen[State] = {
    Gen.const(List.empty)
  }

  override def newSut(state: State): Sut = {
    new PokeStore()
  }

  def arbitraryStore = {
    for {
      poke <- pokemon
      time <- arbitrary[Date]
    } yield Store(poke, time)
  }

  def arbitraryTransfer(state: State): Gen[Transfer] = {
    Gen.oneOf(
      Gen.oneOf(state).map(e => Transfer(e)),
      pokemon.filter(p => !state.contains(p)).map(Transfer)
    )
  }

  override def genCommand(state: State): Gen[Command] = {
    Gen.oneOf(
      Gen.const(ListPokemon),
      arbitraryStore,
      arbitraryTransfer(state)
    )
  }

  override type Sut = PokeStore


  case object ListPokemon extends Command {

    override def preCondition(state: State): Boolean = true

    override def run(sut: PokeStore): Result = sut.listPokemon().toList

    override def nextState(state: State): State = state

    override type Result = List[Pokemon]

    override def postCondition(state: State, result: Try[Result]): Prop =
      result.isSuccess && (
        result.get.size == state.size &&
        result.get.forall(p => state.exists(_ == p))
        )
  }

  case class Store(pokemon: Pokemon, time: Date) extends Command {
    override type Result = Unit

    override def preCondition(state: State): Boolean = true

    override def postCondition(state: State, result: Try[Result]): Prop = {
      result.isSuccess && nextState(state).contains(pokemon)
    }

    override def run(sut: PokeStore): Result = sut.storePokemon(pokemon, time)

    override def nextState(state: State): State = pokemon :: state
  }

  case class Transfer(pokemon: Pokemon) extends Command {
    override type Result = Unit

    override def preCondition(state: State): Boolean = true

    override def postCondition(state:State, result: Try[Result]): Prop = result match {
      case Success(_) => state.contains(pokemon) && !nextState(state).contains(pokemon)
      case Failure(ex) => ex.isInstanceOf[IllegalArgumentException] && !state.contains(pokemon)
    }

    override def run(sut: PokeStore): Result = sut.transferPokemon(pokemon)

    override def nextState(state: State): State = state.filterNot(_ == pokemon)
  }

}
