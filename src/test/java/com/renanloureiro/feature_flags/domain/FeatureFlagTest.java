package com.renanloureiro.feature_flags.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

class FeatureFlagTest {

  @Test
  @DisplayName("Deve criar slug a partir de nome simples")
  void shouldCreateSlugFromSimpleName() {
    String result = FeatureFlag.createSlugByName("Feature Flag");
    assertEquals("feature-flag", result);
  }

  @Test
  @DisplayName("Deve criar slug a partir de nome com caracteres especiais")
  void shouldCreateSlugFromNameWithSpecialCharacters() {
    String result = FeatureFlag.createSlugByName("New Feature!@#$%");
    assertEquals("new-feature", result);
  }

  @Test
  @DisplayName("Deve criar slug a partir de nome com múltiplos espaços")
  void shouldCreateSlugFromNameWithMultipleSpaces() {
    String result = FeatureFlag.createSlugByName("  Multiple   Spaces  ");
    assertEquals("multiple-spaces", result);
  }

  @Test
  @DisplayName("Deve criar slug a partir de nome com hífens e underscores")
  void shouldCreateSlugFromNameWithHyphensAndUnderscores() {
    String result = FeatureFlag.createSlugByName("feature-flag_test");
    assertEquals("feature-flag-test", result);
  }

  @Test
  @DisplayName("Deve criar slug a partir de nome com números")
  void shouldCreateSlugFromNameWithNumbers() {
    String result = FeatureFlag.createSlugByName("Feature Flag 123");
    assertEquals("feature-flag-123", result);
  }

  @Test
  @DisplayName("Deve criar slug a partir de nome em maiúsculas")
  void shouldCreateSlugFromUpperCaseName() {
    String result = FeatureFlag.createSlugByName("FEATURE FLAG");
    assertEquals("feature-flag", result);
  }

  @Test
  @DisplayName("Deve retornar string vazia para nome null")
  void shouldReturnEmptyStringForNullName() {
    String result = FeatureFlag.createSlugByName(null);
    assertEquals("", result);
  }

  @Test
  @DisplayName("Deve retornar string vazia para nome vazio")
  void shouldReturnEmptyStringForEmptyName() {
    String result = FeatureFlag.createSlugByName("");
    assertEquals("", result);
  }

  @Test
  @DisplayName("Deve retornar string vazia para nome com apenas espaços")
  void shouldReturnEmptyStringForWhitespaceOnlyName() {
    String result = FeatureFlag.createSlugByName("   ");
    assertEquals("", result);
  }

  @Test
  @DisplayName("Deve criar slug a partir de nome com apenas caracteres especiais")
  void shouldCreateSlugFromNameWithOnlySpecialCharacters() {
    String result = FeatureFlag.createSlugByName("!@#$%^&*()");
    assertEquals("", result);
  }

  @Test
  @DisplayName("Deve criar slug a partir de nome com acentos")
  void shouldCreateSlugFromNameWithAccents() {
    String result = FeatureFlag.createSlugByName("Funcionalidade Especial");
    assertEquals("funcionalidade-especial", result);
  }
}