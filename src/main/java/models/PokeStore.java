package models;

import java.util.ArrayList;
import java.util.List;

public class PokeStore {

    private List<Pokemon> storage = new ArrayList<>();

    public void clearAll() {
        storage.clear();
    }

    public void storePokemon(Player player, Pokemon pokemon) {
        storage.add(pokemon);
    }

    public List<Pokemon> listPokemon(Player player) {
        return storage;
    }
}
