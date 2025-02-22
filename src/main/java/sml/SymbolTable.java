package sml;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ====================================================================================================================
 * Immutable Symbol Table for Key-Value Mappings.
 * --------------------------------------------------------------------------------------------------------------------
 * A generic, immutable key-value store ensuring safe and efficient lookups with {@link Optional}-based retrieval.
 * ====================================================================================================================
 * <p>
 * Key Features:
 * - **Immutable**: Prevents modification after creation.
 * - **Safe Retrieval**: Uses {@link Optional} for missing values.
 * - **Factory Method**: Ensures controlled instantiation.
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 * @author Ricki Angel
 */
public class SymbolTable<K, V> {
    private final Map<K, V> map;

    private SymbolTable(Map<K, V> map) {
        this.map = Map.copyOf(map);
    }

    public static <K, V> SymbolTable<K, V> of(Map<K, V> map) {
        return new SymbolTable<>(map);
    }

    public Optional<V> get(K key) {
        return Optional.ofNullable(map.get(key));
    }

    /**
     * Returns a collection of all values in the symbol table.
     *
     * @return a collection of values
     */
    public Collection<V> values() {
        return map.values();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public String toString() {
        return map.entrySet().stream()
                .map(entry -> entry.getKey() + " -> " + entry.getValue())
                .collect(Collectors.joining(", ", "[", "]"));
    }
}