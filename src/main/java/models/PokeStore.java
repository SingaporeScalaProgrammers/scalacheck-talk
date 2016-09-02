package models;

import java.util.*;
import java.util.stream.Collectors;

public class PokeStore {



    private List<CatchEntry> storage = new ArrayList<>();

    public synchronized void clearAll() {
        storage.clear();
    }

    public synchronized void storePokemon(Player player, Pokemon pokemon, Date catchTime) {
        storage.add(new CatchEntry(pokemon, catchTime));
    }

    public synchronized List<Pokemon> listPokemon(Player player) {
        return storage.stream().map(f -> f.getPokemon()).collect(Collectors.toList());
    }

    public synchronized void transferPokemon(Player player, Pokemon pokemon) {
       if(storage.stream().noneMatch(entry -> entry.getPokemon().equals(pokemon))) {
           throw new IllegalArgumentException();
       } else {
           storage = storage.stream().filter(entry -> !entry.getPokemon().equals(pokemon)).collect(Collectors.toList());
       }
    }

}
