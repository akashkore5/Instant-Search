package com.example.instantSearch;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SpringBootApplication
@Controller
public class SearchApplication {

    private static final int MIN_QUERY_LENGTH = 3;
    private static final String DATA_FILE = "src/main/resources/Names.csv";
    private final Trie trie = new Trie();
    private final Set<String> namesSet = new HashSet<>();
    private final Map<String, List<Map<String, Object>>> cache = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }

    /**
     * Initialize the application by loading names from the CSV file.
     * This method is executed after the application starts.
     */
    @PostConstruct
    public void init() {
        loadNamesFromFile();
    }

    /**
     * Loads names from a CSV file into both the Trie (for prefix search)
     * and a HashSet (for substring search).
     *
     * @throws RuntimeException if the file cannot be read or loaded.
     */
    private void loadNamesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String name = line.trim();
                trie.insert(name);  // Insert the name into Trie for prefix search
                namesSet.add(name);  // Add the name to the set for substring search
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load data file", e);
        }
    }

    /**
     * Displays the main search page.
     *
     * @return the name of the HTML template (index) to be rendered.
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * Handles the search request, performs the search based on the query,
     * checks if the results are cached, and returns the search results.
     *
     * @param query the search query entered by the user.
     * @param model the model used to pass data to the view (Thymeleaf template).
     * @return the name of the HTML template (index) to be rendered.
     */
    @GetMapping("/search")
    public String search(@RequestParam(name = "q", required = false) String query, Model model) {
        long startTime = System.nanoTime();

        if (query == null || query.length() < MIN_QUERY_LENGTH) {
            model.addAttribute("error", "Query term must be at least 3 characters");
            model.addAttribute("results", Collections.emptyList());
        } else {
            List<Map<String, Object>> matches;
            // Check if the query is cached
            if (cache.containsKey(query)) {
                matches = cache.get(query);
            } else {
                matches = searchNames(query); // Perform search if not cached
                cache.put(query, matches); // Cache the results for future use
            }

            model.addAttribute("results", matches);  // Add results to the model
            model.addAttribute("size", matches.size());  // Add the number of results
        }

        long endTime = System.nanoTime();
        long responseTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        model.addAttribute("responseTime", responseTime);  // Add response time to the model

        return "index";
    }

    /**
     * Performs the search based on the query. It searches for both
     * prefix matches using the Trie and substring matches using the HashSet.
     * Results are ranked first by prefix matches and then by substring matches.
     *
     * @param query the search query entered by the user.
     * @return a list of ranked results containing name and rank.
     */
    private List<Map<String, Object>> searchNames(String query) {
        List<Map<String, Object>> rankedResults = new ArrayList<>();

        // Prefix-based search using Trie
        List<String> startsWithQuery = trie.searchPrefix(query);
        int rank = 1;
        for (String name : startsWithQuery) {
            rankedResults.add(Map.of("name", name, "rank", rank++));  // Add ranked prefix results
        }

        // Substring-based search using HashSet
        List<String> containsQuery = namesSet.stream()
                .filter(name -> name.toLowerCase().contains(query.toLowerCase()) && !name.toLowerCase().startsWith(query.toLowerCase()))
                .sorted(Comparator.comparingInt(String::length).thenComparing(String::compareToIgnoreCase))
                .collect(Collectors.toList());

        for (String name : containsQuery) {
            rankedResults.add(Map.of("name", name, "rank", rank++));  // Add ranked substring results
        }

        return rankedResults;
    }

    /**
     * TrieNode represents a node in the Trie for prefix-based search.
     * Each node stores its children and a flag indicating if it's the end of a word.
     */
    class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEndOfWord = false;
    }

    /**
     * Trie is a data structure for efficient prefix-based search.
     * It allows insertion of words and searching for all words with a given prefix.
     */
    class Trie {
        private final TrieNode root = new TrieNode();

        /**
         * Inserts a word into the Trie.
         *
         * @param word the word to be inserted into the Trie.
         */
        public void insert(String word) {
            TrieNode node = root;
            for (char c : word.toLowerCase().toCharArray()) {
                node = node.children.computeIfAbsent(c, k -> new TrieNode());
            }
            node.isEndOfWord = true;
        }

        /**
         * Searches for all words in the Trie that start with a given prefix.
         *
         * @param prefix the prefix to search for.
         * @return a list of words starting with the given prefix.
         */
        public List<String> searchPrefix(String prefix) {
            List<String> result = new ArrayList<>();
            TrieNode node = root;
            for (char c : prefix.toLowerCase().toCharArray()) {
                node = node.children.get(c);
                if (node == null) {
                    return result; // No match for the prefix
                }
            }
            collectWords(node, prefix, result);  // Collect all words that match the prefix
            return result;
        }

        /**
         * Collects all words in the Trie that match the given prefix.
         *
         * @param node the current TrieNode.
         * @param prefix the current prefix formed from the root to this node.
         * @param result the list to collect matching words.
         */
        private void collectWords(TrieNode node, String prefix, List<String> result) {
            if (node.isEndOfWord) {
                result.add(prefix);  // Add complete word to the result
            }
            for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {
                collectWords(entry.getValue(), prefix + entry.getKey(), result);  // Recurse for children
            }
        }
    }
}
