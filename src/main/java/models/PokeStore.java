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

    public List<Pokemon> listPokemon(Player player) {
        return storage.stream().map(f -> f.getPokemon()).collect(Collectors.toList());
    }

    public void transferPokemon(Player player, Pokemon pokemon) {
       if(storage.stream().noneMatch(entry -> entry.getPokemon().equals(pokemon))) {
           throw new IllegalArgumentException();
       } else {
           storage = storage.stream().filter(entry -> !entry.getPokemon().equals(pokemon)).collect(Collectors.toList());
       }
    }

    public double catchRate(List<CatchEntry> catches) {
        Comparator<CatchEntry> comp = (c1, c2) -> {
            if(c1.getCatchTime().before(c2.getCatchTime())) return -1;
            if(c1.getCatchTime().after(c2.getCatchTime())) return 1;
            else return 0;
        };
        if(catches.isEmpty()){ // fix for bug #1
            return 0.0;
        }
        Date firstCatch = Collections.min(catches,comp).getCatchTime(); //bug #1 no such element exception on empty list
        Date lastCatch = Collections.max(catches,comp).getCatchTime();
        //bug #2 division by zero error when passed a single pokemon
        if(firstCatch == lastCatch) return 0.0; //fix for bug #2
        else return catches.size() / ((lastCatch.getTime() - firstCatch.getTime()) / 1000);
    }
}
