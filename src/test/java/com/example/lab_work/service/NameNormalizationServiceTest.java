package com.example.lab_work.service;

import com.example.lab_work.dto.NormalizedName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("ФИО Нормализация - полный набор тестов")
class NameNormalizationServiceTest {

    @Autowired
    private NameNormalizationService normalizationService;

    // ============ 2. НОРМАЛЬНЫЕ ПОЛНЫЕ ФИО ============

    @Test
    @DisplayName("2.1 Полное ФИО: Иванов Иван Иванович")
    void testFullName_IvanovIvanIvanovich() {
        NormalizedName result = normalizationService.normalize("Иванов Иван Иванович");
        assertEquals("Иванов", result.surname());
        assertEquals("Иван", result.name());
        assertEquals("Иванович", result.patronymic());
        assertNull(result.nameInitial());
        assertNull(result.patronymicInitial());
    }

    @Test
    @DisplayName("2.2 ФИО без отчества: Иванов Иван")
    void testNameWithoutPatronymic_IvanovIvan() {
        NormalizedName result = normalizationService.normalize("Иванов Иван");
        assertEquals("Иванов", result.surname());
        assertEquals("Иван", result.name());
        assertNull(result.patronymic());
    }

    @Test
    @DisplayName("2.3 Только фамилия: Иванов")
    void testOnlySurname_Ivanov() {
        NormalizedName result = normalizationService.normalize("Иванов");
        assertEquals("Иванов", result.surname());
        assertNull(result.name());
        assertNull(result.patronymic());
    }

    // ============ 3. ФИО С ИНИЦИАЛАМИ ============

    @Test
    @DisplayName("3.1 Инициалы имени и отчества: Иванов И.И.")
    void testDoubleInitials_IvanovII() {
        NormalizedName result = normalizationService.normalize("Иванов И.И.");
        assertEquals("Иванов", result.surname());
        assertNull(result.name());
        assertNull(result.patronymic());
        assertEquals('И', result.nameInitial());
        assertEquals('И', result.patronymicInitial());
    }

    @Test
    @DisplayName("3.2 Только имя-инициал: Иванов И.")
    void testNameInitialOnly_IvanovI() {
        NormalizedName result = normalizationService.normalize("Иванов И.");
        assertEquals("Иванов", result.surname());
        assertNull(result.name());
        assertEquals('И', result.nameInitial());
        assertNull(result.patronymicInitial());
    }

    @Test
    @DisplayName("3.3 Разделённые инициалы: Иванов И И")
    void testSeparatedInitials_IvanovISpace() {
        NormalizedName result = normalizationService.normalize("Иванов И И");
        assertEquals("Иванов", result.surname());
        assertEquals('И', result.nameInitial());
        assertEquals('И', result.patronymicInitial());
    }

    // ============ 4. РЕАЛЬНЫЕ "ПАША / ДИМА / ЖЕНЯ" (СИНОНИМЫ) ============

    @Test
    @DisplayName("4.1 Уменьшительное имя Паша -> Павел")
    void testDiminutive_Pasha() {
        NormalizedName result = normalizationService.normalize("Иванов Паша");
        assertEquals("Иванов", result.surname());
        assertEquals("Павел", result.name());
    }

    @Test
    @DisplayName("4.2 Уменьшительное имя Дима -> Дмитрий")
    void testDiminutive_Dima() {
        NormalizedName result = normalizationService.normalize("Иванов Дима");
        assertEquals("Иванов", result.surname());
        assertEquals("Дмитрий", result.name());
    }

    @Test
    @DisplayName("4.3 Уменьшительное имя Гоша -> Георгий")
    void testDiminutive_Gosha() {
        NormalizedName result = normalizationService.normalize("Иванов Гоша");
        assertEquals("Иванов", result.surname());
        assertEquals("Георгий", result.name());
    }

    @Test
    @DisplayName("4.4 Уменьшительное имя Женя -> Евгений")
    void testDiminutive_Zhenya() {
        NormalizedName result = normalizationService.normalize("Иванов Женя");
        assertEquals("Иванов", result.surname());
        assertEquals("Евгений", result.name());
    }

    @Test
    @DisplayName("4.5 Уменьшительное имя Дима -> Дмитрий (uppercase)")
    void testDiminutiveUppercase_Dima() {
        NormalizedName result = normalizationService.normalize("Иванов ДИМА");
        assertEquals("Иванов", result.surname());
        assertEquals("Дмитрий", result.name());
    }

    @Test
    @DisplayName("4.6 Уменьшительное имя Коля -> Николай")
    void testDiminutive_Kolya() {
        NormalizedName result = normalizationService.normalize("Иванов Коля");
        assertEquals("Иванов", result.surname());
        assertEquals("Николай", result.name());
    }

    @Test
    @DisplayName("4.7 Уменьшительное имя Тёма -> Артём")
    void testDiminutive_Tema() {
        NormalizedName result = normalizationService.normalize("Иванов Тёма");
        assertEquals("Иванов", result.surname());
        assertEquals("Артем", result.name());
    }

    // ============ 6. НЕСТАНДАРТНЫЙ ПОРЯДОК ============

    @Test
    @DisplayName("6.1 Имя впереди: Иван Иванов Иванович")
    void testNameFirst_IvanIvanovIvanovich() {
        NormalizedName result = normalizationService.normalize("Иван Иванов Иванович");
        // Should reorder to standard format
        assertEquals("Иванов", result.surname());
        assertEquals("Иван", result.name());
        assertEquals("Иванович", result.patronymic());
    }

    // ============ 7. СМЕШАННЫЕ ФОРМЫ ============

    @Test
    @DisplayName("7.1 Инициал имени + отчество: Иванов И. Иванович")
    void testMixedForm_IvanovIPatronymic() {
        NormalizedName result = normalizationService.normalize("Иванов И. Иванович");
        assertEquals("Иванов", result.surname());
        assertNull(result.name());
        assertEquals("Иванович", result.patronymic());
        assertEquals('И', result.nameInitial());
    }

    @Test
    @DisplayName("7.2 Имя + инициал отчества: Иванов Иван И.")
    void testMixedForm_IvanovNameInitialPatronymic() {
        NormalizedName result = normalizationService.normalize("Иванов Иван И.");
        assertEquals("Иванов", result.surname());
        assertEquals("Иван", result.name());
        assertNull(result.patronymic());
        assertEquals('И', result.patronymicInitial());
    }

    // ============ МАТЧИНГ ============

    @Test
    @DisplayName("Матчинг: фамилии совпадают (высокий приоритет)")
    void testMatching_SurnameMatch() {
        NormalizedName name1 = normalizationService.normalize("Иванов Иван Иванович");
        NormalizedName name2 = normalizationService.normalize("Иванов Петр Петрович");

        double similarity = normalizationService.calculateFullSimilarity(name1, name2);
        assertTrue(similarity >= 0.60, "Surname match should result in score >= 0.60, got " + similarity);
    }

    @Test
    @DisplayName("Матчинг: полное имя vs инициал")
    void testMatching_FullNameVsInitial() {
        NormalizedName name1 = normalizationService.normalize("Иванов Иван Иванович");
        NormalizedName name2 = normalizationService.normalize("Иванов И. И.");

        double similarity = normalizationService.calculateFullSimilarity(name1, name2);
        assertTrue(similarity >= 0.70, "Name and initial match should score high, got " + similarity);
    }

    @Test
    @DisplayName("Матчинг: синоним имени")
    void testMatching_NameAlias() {
        NormalizedName name1 = normalizationService.normalize("Иванов Павел Иванович");
        NormalizedName name2 = normalizationService.normalize("Иванов Паша Иванович");

        double similarity = normalizationService.calculateFullSimilarity(name1, name2);
        // Both convert to "Павел", so should match well
        assertTrue(similarity >= 0.80, "Alias names should match, got " + similarity);
    }

    // ============ EDGE CASES ============

    @Test
    @DisplayName("Пустое ФИО")
    void testEmpty() {
        NormalizedName result = normalizationService.normalize("");
        assertNull(result.surname());
        assertNull(result.name());
        assertNull(result.patronymic());
    }

    @Test
    @DisplayName("Null ФИО")
    void testNull() {
        NormalizedName result = normalizationService.normalize(null);
        assertNull(result.surname());
        assertNull(result.name());
        assertNull(result.patronymic());
    }

    @Test
    @DisplayName("Разные размеры букв")
    void testCaseInsensitive() {
        NormalizedName result1 = normalizationService.normalize("иванов иван иванович");
        NormalizedName result2 = normalizationService.normalize("ИВАНОВ ИВАН ИВАНОВИЧ");

        assertEquals(result1.surname(), result2.surname());
        assertEquals(result1.name(), result2.name());
        assertEquals(result1.patronymic(), result2.patronymic());
    }

    @Test
    @DisplayName("Множественные пробелы")
    void testMultipleSpaces() {
        NormalizedName result1 = normalizationService.normalize("Иванов   Иван   Иванович");
        NormalizedName result2 = normalizationService.normalize("Иванов Иван Иванович");

        assertEquals(result1.surname(), result2.surname());
        assertEquals(result1.name(), result2.name());
        assertEquals(result1.patronymic(), result2.patronymic());
    }

    @Test
    @DisplayName("Замена ё на е")
    void testYoReplacement() {
        NormalizedName result1 = normalizationService.normalize("Иванов Алёша");
        NormalizedName result2 = normalizationService.normalize("Иванов Алеша");

        assertEquals(result1.name(), result2.name());
    }

    @Test
    @DisplayName("Стандартное представление")
    void testStandardRepresentation() {
        NormalizedName result = normalizationService.normalize("Иванов Иван Иванович");
        String standard = result.getStandardRepresentation();
        assertEquals("Иванов Иван Иванович", standard);
    }

    @Test
    @DisplayName("Отображаемое имя с инициалом")
    void testDisplayName() {
        NormalizedName result = normalizationService.normalize("Иванов Иван");
        String display = result.getDisplayName();
        assertEquals("Иванов И.", display);
    }
}

