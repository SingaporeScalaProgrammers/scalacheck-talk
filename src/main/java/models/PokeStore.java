package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PokeStore {

    private class CatchEntry {
        public Pokemon getPokemon() {
            return pokemon;
        }

        public Date getCatchTime() {
            return catchTime;
        }

        private Pokemon pokemon;
        private Date catchTime;

        public CatchEntry(Pokemon pokemon, Date catchTime) {
            this.pokemon = pokemon;
            this.catchTime = catchTime;
        }
    }

    private List<CatchEntry> storage = new ArrayList<>();

    public void clearAll() {
        storage.clear();
    }

    public void storePokemon(Player player, Pokemon pokemon, Date catchTime) {
        storage.add(new CatchEntry(pokemon, catchTime));
    }

    public List<Pokemon> listPokemon(Player player) {
        return storage.stream().map(f -> f.pokemon).collect(Collectors.toList());
    }

    public void transferPokemon(Player player, Pokemon pokemon) {
       if(storage.stream().noneMatch(entry -> entry.pokemon.equals(pokemon))) {
           throw new IllegalArgumentException();
       } else {
           storage = storage.stream().filter(entry -> !entry.pokemon.equals(pokemon)).collect(Collectors.toList());
       }
    }

    public double catchRate(Player player) {
        //TODO: add implementation
        return 0.0;
    }
}
