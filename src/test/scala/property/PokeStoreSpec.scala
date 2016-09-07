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
      check(new SinglePlayerPokeStoreSpec().property(threadCount = 1, maxParComb = 10000))
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

  def arbitraryDelete(state: State): Gen[Delete] = {
    Gen.oneOf(
      Gen.oneOf(state).map(e => Delete(e)),
      pokemon.filter(p => !state.contains(p)).map(Delete)
    )
  }

  override def genCommand(state: State): Gen[Command] = {
    Gen.oneOf(
      Gen.const(ListPokemon),
      arbitraryStore,
      arbitraryDelete(state)
    )
  }

  override type Sut = PokeStore


  case object ListPokemon extends Command {
    override type Result = List[Pokemon]

    override def preCondition(state: State): Boolean = true

    override def run(pokeStore: PokeStore): Result = pokeStore.listPokemon.toList

    override def nextState(state: State): State = state

    override def postCondition(state: State, result: Try[Result]): Prop =
      result.isSuccess && (
        result.get.size == state.size &&
        result.get.forall(p => state.contains(p))
        )
  }

  case class Store(pokemon: Pokemon, time: Date) extends Command {
    override type Result = Unit

    override def preCondition(state: State): Boolean = true

    override def postCondition(state: State, result: Try[Result]): Prop = {
      result.isSuccess
    }

    override def run(pokeStore: PokeStore): Result = pokeStore.storePokemon(pokemon, time)

    override def nextState(state: State): State = pokemon :: state
  }

  case class Delete(pokemon: Pokemon) extends Command {
    override type Result = Unit

    override def preCondition(state: State): Boolean = true

    override def postCondition(state:State, result: Try[Result]): Prop = result match {
      case Success(_) => state.contains(pokemon)
      case Failure(ex) => ex.isInstanceOf[IllegalArgumentException] && !state.contains(pokemon)
    }

    override def run(sut: PokeStore): Result = sut.deletePokemon(pokemon)

    override def nextState(state: State): State = state.filterNot(_ == pokemon)
  }

}
