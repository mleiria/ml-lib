package pt.mleiria.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Optional;
import java.util.function.BiPredicate;

public class JsonValidator {


    /**
     * Retrieves a specific JSON node from a JSON data string.
     *
     * @param jsonData the JSON data string
     * @param nodeName the name of the node to retrieve
     * @return an Optional containing the requested JSON node, or an empty Optional if it cannot be found
     */
    public static Optional<JsonNode> getJsonNode(final String jsonData, final String nodeName) {
        try {
            return Optional.of(new ObjectMapper().readTree(jsonData).path(nodeName));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    /**
     * Retrieves the value of a specified node in the given JSON data.
     *
     * @param jsonData the JSON data to be searched
     * @param nodeName the name of the node to retrieve the value from
     * @return an Optional containing the value of the specified node, or empty if the node does not exist
     */
    public static Optional<String> getJsonValue(final String jsonData, final String nodeName) {
        return getJsonNode(jsonData, nodeName).map(JsonNode::textValue);
    }

    /**
     * Returns a BiPredicate that checks if a given node with a specified value is present in the provided JSON data.
     *
     * @param jsonData the JSON data to search in
     * @return the BiPredicate that checks if the node with the specified value is present
     */
    public static BiPredicate<String, String> isNodePresentWithValue(final String jsonData) {
        return
                (nodeName, nodeValue) ->
                        getJsonNode(jsonData, nodeName).map(JsonNode::textValue)
                                .filter(elem -> elem.equals(nodeValue))
                                .isPresent();

    }
}
