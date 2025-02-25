package sml;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Immutable Symbol Table for Key-Value Mappings.
 *
 * A generic, immutable key-value store ensuring safe and efficient lookups with {@link Optional}-based retrieval.
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Immutable: Prevents modification after creation.</li>
 *   <li>Safe Retrieval: Uses {@link Optional} for missing values.</li>
 *   <li>Factory Method: Ensures controlled instantiation.</li>
 * </ul>
 *
 * @param <K> the type of keys
 * @param <V> the type of values
 * @author Ricki Angel
 */
public class SymbolTable<K, V> {
    private final Map<K, V> map;

    /**
     * Constructs a new SymbolTable instance with the provided map.
     *
     * <p>Initializes the symbol table with a copy of the provided map.</p>
     *
     * @param map the map to be copied into the symbol table
     */
    private SymbolTable(Map<K, V> map) {
        this.map = Map.copyOf(map);
    }

    /**
     * Factory method to create a new SymbolTable instance from a map.
     *
     * <p>Creates a new SymbolTable using the provided map of key-value pairs.</p>
     *
     * @param <K> the type of keys
     * @param <V> the type of values
     * @param map the map containing the key-value pairs
     * @return a new SymbolTable instance
     */
    public static <K, V> SymbolTable<K, V> of(Map<K, V> map) {
        return new SymbolTable<>(map);
    }

    /**
     * Retrieves the value associated with the given key from the symbol table.
     *
     * <p>Returns an {@link Optional} containing the value if found, or an empty {@link Optional} if the key is not present.</p>
     *
     * @param key the key to look up
     * @return an {@link Optional} containing the value if found, or an empty {@link Optional} if not
     */
    public Optional<V> get(K key) {
        return Optional.ofNullable(map.get(key));
    }

    /**
     * Returns a collection of all values stored in the symbol table.
     *
     * <p>This method returns all values present in the symbol table.</p>
     *
     * @return a collection of values
     */
    public Collection<V> values() {
        return map.values();
    }

    /**
     * Checks whether the symbol table is empty.
     *
     * <p>Returns {@code true} if the symbol table has no entries, otherwise {@code false}.</p>
     *
     * @return {@code true} if the symbol table is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Returns a string representation of the symbol table.
     *
     * <p>Provides a formatted string representation of all key-value pairs in the table.</p>
     *
     * @return a string representation of the symbol table
     */
    @Override
    public String toString() {
        return map.entrySet().stream()
                .map(entry -> entry.getKey() + " -> " + entry.getValue())
                .collect(Collectors.joining(", ", "[", "]"));
    }
}