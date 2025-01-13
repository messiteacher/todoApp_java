package app.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Json {
    public static Map<String, Object> readAsMap(String filePath) {

        String jsonStr = File.readAsString(filePath);
        if (jsonStr.isEmpty()) return new LinkedHashMap<>();

        return jsonToMap(jsonStr);
    }

    public static String mapToJson(Map<String, Object> map) {

        StringBuilder jsonBuilder = new StringBuilder();

        jsonBuilder.append("{\n");

        String str = map.keySet().stream()
                .map(k -> map.get(k) instanceof String
                        ? "    \"%s\" : \"%s\"".formatted(k, map.get(k))
                        : "    \"%s\" : %s".formatted(k, map.get(k))
                ).collect(Collectors.joining(",\n"));
        jsonBuilder.append(str);

        jsonBuilder.append("\n}");

        return jsonBuilder.toString();
    }

    public static void writeAsMap(String filePath, Map<String, Object> todoMap) {

        String jsonStr = mapToJson(todoMap);
        File.write(filePath, jsonStr);
    }

    static Map<String, Object> jsonToMap(String jsonStr) {

        Map<String, Object> resultMap = new LinkedHashMap<>();

        jsonStr = jsonStr.replaceAll("\\{", "")
                .replaceAll("}", "")
                .replaceAll("\n", "")
                .replaceAll(" : ", ":");

        Arrays.stream(jsonStr.split(","))
                .map(p -> p.trim().split(":"))
                .forEach(p -> {
                    String key = p[0].replaceAll("\"", "");
                    String value = p[1];

                    if (value.startsWith("\"")) {
                        resultMap.put(key, value.replaceAll("\"", ""));
                    } else if (value.contains(".")) {
                        resultMap.put(key, Double.parseDouble(value));
                    } else if (value.equals("true") || value.equals("false")) {
                        resultMap.put(key, Boolean.parseBoolean(value));
                    } else {
                        resultMap.put(key, Integer.parseInt(value));
                    }
                });

        return resultMap;
    }

    public static String listToJson(List<Map<String, Object>> mapList) {

        StringBuilder jsonBuilder = new StringBuilder();

        jsonBuilder.append("[\n");

        String str = mapList.stream() // map들이 들어있다
                .map(Json::mapToJson)
                .map(s -> "    " + s)
                .map(s -> s.replaceAll("\n", "\n    "))
                .collect(Collectors.joining(",\n"));

        jsonBuilder.append(str);
        jsonBuilder.append("\n]");

        return jsonBuilder.toString();
    }
}
