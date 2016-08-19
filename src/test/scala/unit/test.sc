import java.util.Date

case class Pokemon(id: Int, name: String, pokemonType: String)

case class Player(id: Int)

case class Candy

trait PokeStore {

  def storePokemon(player: Player, pokemon: Pokemon, timestamp: Date)

  def listPokemon(player: Player): List[Pokemon]

  def transferPokemon(player: Player, pokemon: Pokemon): List[Candy]

  def listCandies(player: Player): List[Candy]

  def catchRate(player: Player): Double
}
