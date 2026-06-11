package com.example.lab_work.service;

import com.example.lab_work.dto.NormalizedName;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
@Slf4j
public class NameNormalizationService {

    private final JaroWinklerSimilarity similarity = new JaroWinklerSimilarity();
    private final Map<String, String> aliases = new ConcurrentHashMap<>();
    private final Set<String> knownSurnames = new HashSet<>();
    private final Set<String> knownNames = new HashSet<>();

    private static final Pattern PATRONYMIC_PATTERN = Pattern.compile(".*(ович|евич|овна|евна|ич)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern SURNAME_PATTERN = Pattern.compile(".*(ов|ев|ин|ын|ский|цкий|ова|ева|ина|ына|ская|цкая)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern INITIAL_PATTERN = Pattern.compile("^[а-яА-Яa-zA-Z]\\.?$");
    private static final Pattern DOUBLE_INITIAL_PATTERN = Pattern.compile("^[а-яА-Яa-zA-Z]\\.[а-яА-Яa-zA-Z]\\.?$");

    private static final double FUZZY_MATCH_THRESHOLD = 0.85;
    private static final double INITIAL_MATCH_SCORE = 0.95;

    @PostConstruct
    public void init() {
        loadAliases();
        initializeNameSets();
        log.info("NameNormalizationService initialized with {} aliases, {} surnames, {} names",
            aliases.size(), knownSurnames.size(), knownNames.size());
    }

    private void loadAliases() {

        loadDefaultAliases();

        try (InputStream is = getClass().getResourceAsStream("/names/aliases.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

          String line;
          boolean isFirstLine = true;
          while ((line = reader.readLine()) != null) {
              if (isFirstLine) {
                  isFirstLine = false;
                  continue;
              }
              String[] arr = line.split(",");
              if (arr.length >= 2) {
                  String canonical = normalizeString(arr[0].trim());
                  String alias = normalizeString(arr[1].trim());
                  aliases.put(alias, canonical);
                  aliases.put(canonical, canonical);
              }
          }
          log.info("Loaded additional aliases from file");
        } catch (Exception e) {
            log.warn("Could not load aliases.csv, using defaults only: {}", e.getMessage());
        }
    }

    private void loadDefaultAliases() {
        addAlias("александр", "Александр");
        addAlias("саша", "Александр");
        addAlias("саня", "Александр");
        addAlias("шур", "Александр");

        addAlias("павел", "Павел");
        addAlias("паша", "Павел");

        addAlias("дмитрий", "Дмитрий");
        addAlias("дима", "Дмитрий");
        addAlias("диму", "Дмитрий");

        addAlias("евгений", "Евгений");
        addAlias("женя", "Евгений");

        addAlias("георгий", "Георгий");
        addAlias("гоша", "Георгий");
        addAlias("жора", "Георгий");

        addAlias("артём", "Артём");
        addAlias("артем", "Артём");
        addAlias("тёма", "Артём");
        addAlias("тема", "Артём");

        addAlias("владимир", "Владимир");
        addAlias("вова", "Владимир");
        addAlias("володя", "Владимир");

        addAlias("михаил", "Михаил");
        addAlias("миша", "Михаил");

        addAlias("николай", "Николай");
        addAlias("коля", "Николай");

        addAlias("сергей", "Сергей");
        addAlias("серёжа", "Сергей");
        addAlias("серёга", "Сергей");

        addAlias("андрей", "Андрей");
        addAlias("андрюха", "Андрей");

        addAlias("алексей", "Алексей");
        addAlias("алёша", "Алексей");
        addAlias("лёша", "Алексей");

        addAlias("иван", "Иван");
        addAlias("ваня", "Иван");

        addAlias("пётр", "Пётр");
        addAlias("петр", "Пётр");
        addAlias("петя", "Пётр");

        addAlias("олег", "Олег");
        addAlias("игорь", "Игорь");
        addAlias("юрий", "Юрий");
        addAlias("юра", "Юрий");
        addAlias("леонид", "Леонид");
        addAlias("лёня", "Леонид");

        addAlias("анна", "Анна");
        addAlias("аня", "Анна");
        addAlias("анюта", "Анна");

        addAlias("елена", "Елена");
        addAlias("лена", "Елена");
        addAlias("леночка", "Елена");

        addAlias("ольга", "Ольга");
        addAlias("оля", "Ольга");

        addAlias("мария", "Мария");
        addAlias("маша", "Мария");
        addAlias("машенька", "Мария");

        addAlias("наталья", "Наталья");
        addAlias("наташа", "Наталья");
        addAlias("натали", "Наталья");

        addAlias("екатерина", "Екатерина");
        addAlias("катя", "Екатерина");
        addAlias("катюша", "Екатерина");

        addAlias("татьяна", "Татьяна");
        addAlias("таня", "Татьяна");

        addAlias("светлана", "Светлана");
        addAlias("света", "Светлана");

        addAlias("ирина", "Ирина");
        addAlias("ира", "Ирина");

        log.info("Loaded {} default aliases", aliases.size());
    }

    private void addAlias(String alias, String canonical) {
        String normalized = normalizeString(alias);
        aliases.put(normalized, canonical);
        aliases.put(normalizeString(canonical), canonical);
    }

    private void initializeNameSets() {
        knownSurnames.addAll(Arrays.asList(
            "иванов", "петров", "сидоров", "козлов", "соколов", "морозов", "волков",
            "новиков", "кузнецов", "попов", "лебедев", "соловьев", "медведев", "егоров",
            "смирнов", "михайлов", "андреев", "владимиров", "ёлкин", "елкин"
        ));

        knownNames.addAll(Arrays.asList(
            "александр", "павел", "дмитрий", "евгений", "георгий", "артём", "артем",
            "владимир", "михаил", "николай", "сергей", "андрей", "алексей", "иван",
            "пётр", "петр", "олег", "игорь", "юрий", "леонид",
            "анна", "елена", "ольга", "мария", "наталья", "екатерина", "татьяна", "светлана", "ирина"
        ));
    }

    private String normalizeString(String str) {
        if (str == null) return null;
        return str.toLowerCase().replace("ё", "е").trim();
    }

    private String capitalize(String value) {
        if (value == null || value.isBlank()) return value;
        if (value.length() == 1) return value.toUpperCase();
        return Character.toUpperCase(value.charAt(0)) + value.substring(1).toLowerCase();
    }

    private boolean isInitial(String str) {
        if (str == null || str.isEmpty()) return false;
        return INITIAL_PATTERN.matcher(str.trim()).matches();
    }

    private boolean isDoubleInitial(String str) {
        if (str == null || str.isEmpty()) return false;
        return DOUBLE_INITIAL_PATTERN.matcher(str.trim()).matches();
    }

    private boolean isPatronymic(String str) {
        if (str == null || str.length() < 3) return false;
        return PATRONYMIC_PATTERN.matcher(str).matches();
    }

    private boolean isSurname(String str) {
        if (str == null || str.length() < 2) return false;
        String lower = str.toLowerCase();
        return SURNAME_PATTERN.matcher(lower).matches() || knownSurnames.contains(lower);
    }

    private boolean isName(String str) {
        if (str == null) return false;
        String normalized = normalizeString(str);
        return knownNames.contains(normalized) || aliases.containsKey(normalized);
    }

    private String normalizeAlias(String value) {
        if (value == null || value.isBlank()) return null;
        String normalized = normalizeString(value);
        String canonical = aliases.get(normalized);
        if (canonical != null) {
            return capitalize(canonical);
        }
        return capitalize(value);
    }

    private String extractInitial(String str) {
        if (str == null || str.isEmpty()) return null;
        String trimmed = str.trim();
        if (trimmed.length() == 1) {
            return trimmed.toUpperCase();
        }
        if (trimmed.length() >= 2 && trimmed.charAt(1) == '.') {
            return trimmed.substring(0, 1).toUpperCase();
        }
        return null;
    }

    /**
     * Основной метод определения порядка слов (Фамилия Имя или Имя Фамилия)
     */
    private NameOrder detectOrder(String[] parts) {
        if (parts.length < 2) return NameOrder.SURNAME_FIRST;

        String first = parts[0].toLowerCase();
        String second = parts[1].toLowerCase();

        if (isName(first) && isSurname(second)) {
            log.debug("Detected NAME_FIRST: '{}' is name, '{}' is surname", first, second);
            return NameOrder.NAME_FIRST;
        }

        if (isSurname(first) && isName(second)) {
            log.debug("Detected SURNAME_FIRST: '{}' is surname, '{}' is name", first, second);
            return NameOrder.SURNAME_FIRST;
        }

        if (aliases.containsKey(first) && isSurname(second)) {
            log.debug("Detected NAME_FIRST via alias: '{}' -> name, '{}' -> surname", first, second);
            return NameOrder.NAME_FIRST;
        }

        if (isInitial(first) && isSurname(second)) {
            return NameOrder.NAME_FIRST;
        }

        if (isSurname(first) && isInitial(second)) {
            return NameOrder.SURNAME_FIRST;
        }

        return NameOrder.SURNAME_FIRST;
    }

    public NormalizedName normalize(String fullName) {
        if (fullName == null || fullName.isBlank()) {
            return new NormalizedName(null, null, null, null, null, "");
        }

        String prepared = fullName
            .toLowerCase()
            .replace("ё", "е")
            .replaceAll("\\s+", " ")
            .trim();

        String[] parts = prepared.split(" ");

        if (parts.length == 0) {
            return new NormalizedName(null, null, null, null, null, fullName);
        }

        if (parts.length == 1) {
            return new NormalizedName(capitalize(parts[0]), null, null, null, null, fullName);
        }

        NameOrder order = detectOrder(parts);

        String surname = null;
        String name = null;
        String patronymic = null;
        Character nameInitial = null;
        Character patronymicInitial = null;

        if (order == NameOrder.NAME_FIRST) {
            name = parts[0];
            surname = parts[1];
            if (parts.length >= 3) {
                String third = parts[2];
                if (isPatronymic(third)) {
                    patronymic = third;
                } else if (isInitial(third)) {
                    patronymicInitial = extractInitial(third) != null ? extractInitial(third).charAt(0) : null;
                }
            }
        } else {
            surname = parts[0];

            if (parts.length == 2) {
                String second = parts[1];
                if (isInitial(second)) {
                    nameInitial = extractInitial(second) != null ? extractInitial(second).charAt(0) : null;
                } else if (isPatronymic(second)) {
                    patronymic = second;
                } else {
                    name = second;
                }
            } else if (parts.length == 3) {
                String second = parts[1];
                String third = parts[2];

                if (isDoubleInitial(second)) {
                    nameInitial = second.charAt(0);
                    patronymicInitial = second.length() > 2 ? second.charAt(2) : null;
                } else if (isInitial(second) && isInitial(third)) {
                    nameInitial = extractInitial(second).charAt(0);
                    patronymicInitial = extractInitial(third).charAt(0);
                } else if (isInitial(second)) {
                    nameInitial = extractInitial(second).charAt(0);
                    patronymic = third;
                } else if (isInitial(third)) {
                    name = second;
                    patronymicInitial = extractInitial(third).charAt(0);
                } else {
                    name = second;
                    patronymic = third;
                }
            } else {
                surname = parts[0];
                name = parts[1];
                patronymic = parts.length > 2 ? parts[2] : null;
            }
        }

        String normalizedName = name != null ? normalizeAlias(name) : null;
        String normalizedPatronymic = patronymic != null ? capitalize(patronymic) : null;


        return new NormalizedName(
            surname != null ? capitalize(surname) : null,
            normalizedName,
            normalizedPatronymic,
            nameInitial,
            patronymicInitial,
            fullName
        );
    }

    public double calculateFullSimilarity(NormalizedName left, NormalizedName right) {
        double surnameScore = compareSurname(left, right);
        double nameScore = compareName(left, right);
        double patronymicScore = comparePatronymic(left, right);

        return surnameScore * 0.65 + nameScore * 0.25 + patronymicScore * 0.10;
    }

    private double compareSurname(NormalizedName left, NormalizedName right) {
        if (left.surname() == null || right.surname() == null) {
            return 0.0;
        }
        return similarity.apply(
            left.surname().toLowerCase(),
            right.surname().toLowerCase()
        );
    }

    private double compareName(NormalizedName left, NormalizedName right) {
        if (left.name() != null && right.name() != null) {
            return similarity.apply(
                left.name().toLowerCase(),
                right.name().toLowerCase()
            );
        }

        if (left.name() != null && right.nameInitial() != null) {
            char leftInitial = Character.toUpperCase(left.name().charAt(0));
            char rightInitial = Character.toUpperCase(right.nameInitial());
            return leftInitial == rightInitial ? INITIAL_MATCH_SCORE : 0.0;
        }

        if (right.name() != null && left.nameInitial() != null) {
            char rightInitial = Character.toUpperCase(right.name().charAt(0));
            char leftInitial = Character.toUpperCase(left.nameInitial());
            return rightInitial == leftInitial ? INITIAL_MATCH_SCORE : 0.0;
        }

        if (left.nameInitial() != null && right.nameInitial() != null) {
            return left.nameInitial() == right.nameInitial() ? 1.0 : 0.0;
        }

        return 0.0;
    }

    private double comparePatronymic(NormalizedName left, NormalizedName right) {
        if (left.patronymic() != null && right.patronymic() != null) {
            return similarity.apply(
                left.patronymic().toLowerCase(),
                right.patronymic().toLowerCase()
            );
        }

        if (left.patronymic() != null && right.patronymicInitial() != null) {
            char leftInitial = Character.toUpperCase(left.patronymic().charAt(0));
            char rightInitial = Character.toUpperCase(right.patronymicInitial());
            return leftInitial == rightInitial ? INITIAL_MATCH_SCORE : 0.0;
        }

        if (right.patronymic() != null && left.patronymicInitial() != null) {
            char rightInitial = Character.toUpperCase(right.patronymic().charAt(0));
            char leftInitial = Character.toUpperCase(left.patronymicInitial());
            return rightInitial == leftInitial ? INITIAL_MATCH_SCORE : 0.0;
        }

        if (left.patronymicInitial() != null && right.patronymicInitial() != null) {
            return left.patronymicInitial() == right.patronymicInitial() ? 1.0 : 0.0;
        }

        return 0.5;
    }

    public boolean isSamePerson(NormalizedName left, NormalizedName right) {
        return calculateFullSimilarity(left, right) >= 0.85;
    }

    public void clearCache() {
        log.info("Cache cleared");
    }

    private enum NameOrder {
        SURNAME_FIRST,
        NAME_FIRST
    }
}