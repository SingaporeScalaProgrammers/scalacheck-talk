package property

import java.util.Date

import models.{PokeStore, Pokemon, Player}
import org.scalacheck.{Prop, Gen}
import org.scalacheck.Arbitrary._
import org.scalacheck.commands.Commands
import org.scalatest.{WordSpecLike, MustMatchers}
import org.scalatest.prop.{Checkers, GeneratorDrivenPropertyChecks}
import utils.GeneratorHelpers

import collection.JavaConversions._
import scala.util.{Failure, Success, Try}

class PokeStoreSpec
  extends WordSpecLike
    with MustMatchers
    with Checkers
    with GeneratorHelpers
    with GeneratorDrivenPropertyChecks {

  "PokeStore" should {
    "support store, list, transfer of pokemons for a player" in {
      val testPlayer = new Player(1)
      check(new SinglePlayerPokeStoreSpec(testPlayer).property(threadCount = 2))
    }

  }

}

class SinglePlayerPokeStoreSpec(player: Player)
  extends Commands
    with GeneratorHelpers {

  override type State = List[(Pokemon, Date)]

  override def destroySut(sut: Sut): Unit = () //sut.clearAll()

  override def initialPreCondition(state: State): Boolean = true

  override def canCreateNewSut(newState: State,
                               initSuts: Traversable[State],
                               runningSuts: Traversable[Sut]): Boolean = {
    initSuts.isEmpty && runningSuts.isEmpty
  }

  override def genInitialState: Gen[State] = {
    Gen.const(List.empty)
    /*Gen.listOf(
      for {
        poke <- arbitrary[Pokemon]
        time <- arbitrary[Date]
      } yield (poke, time)
    )*/
  }

  override def newSut(state: State): Sut = {
    val sut = new PokeStore()

    //state.foreach(entry => sut.storePokemon(player, entry._1, entry._2))

    sut
  }

  def arbitraryStore = {
    for {
      poke <- arbitrary[Pokemon]
      time <- arbitrary[Date]
    } yield Store(poke, time)
  }

  def arbitraryTransfer(state: State): Gen[Transfer] = {
    Gen.oneOf(
      Gen.oneOf(state).map(e => Transfer(e._1)),
      arbitrary[Pokemon].filter(p => !state.contains(p)).map(Transfer)
    )
  }

  override def genCommand(state: State): Gen[Command] = {
    Gen.oneOf(
      Gen.const(ListPokemon),
      arbitraryStore,
      arbitraryTransfer(state),
      Gen.const(CatchRate)
    )
  }

  override type Sut = PokeStore


  case object ListPokemon extends Command {

    override def preCondition(state: State): Boolean = true

    override def run(sut: PokeStore): Result = sut.listPokemon(player).toList

    override def nextState(state: State): State = state

    override type Result = List[Pokemon]

    override def postCondition(state: State, result: Try[Result]): Prop =
      result.isSuccess && (
        result.get.size == state.size &&
        result.get.forall(p => state.exists(_._1 == p))
        )
  }

  case class Store(pokemon: Pokemon, time: Date) extends Command {
    override type Result = Unit

    override def preCondition(state: State): Boolean = true

    override def postCondition(state: State, result: Try[Result]): Prop = result.isSuccess

    override def run(sut: PokeStore): Result = sut.storePokemon(player, pokemon, time)

    override def nextState(state: State): State = (pokemon, time) :: state
  }

  case class Transfer(pokemon: Pokemon) extends Command {
    override type Result = Unit

    override def preCondition(state: State): Boolean = true

    override def postCondition(state:State, result: Try[Result]): Prop = result match {
      case Success(_) => state.exists(_._1 == pokemon)
      case Failure(ex) => ex.isInstanceOf[IllegalArgumentException] && !state.exists(_._1 == pokemon)
    }

    override def run(sut: PokeStore): Result = sut.transferPokemon(player, pokemon)

    override def nextState(state: State): State = state.filterNot(_._1 == pokemon)
  }

  case object CatchRate extends Command {
    override type Result = Double

    override def preCondition(state: State): Boolean = true

    override def postCondition(state: State, result: Try[Result]): Prop = result.isSuccess

    override def run(sut: PokeStore): Result = sut.catchRate(player)

    override def nextState(state: State): State = state
  }
}
