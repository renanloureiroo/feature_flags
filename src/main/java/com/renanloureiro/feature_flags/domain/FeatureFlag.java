package com.renanloureiro.feature_flags.domain;

import java.time.LocalDateTime;

import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "feature_flags")
public class FeatureFlag {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "slug", nullable = false, unique = true)
  private String slug;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private FeatureFlagType type;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "schema", nullable = false, columnDefinition = "jsonb")
  private JsonNode schema;

  @Column(name = "description")
  private String description;

  @CreationTimestamp()
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp()
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public static String createSlugByName(String name) {
    if (name == null || name.trim().isEmpty()) {
      return "";
    }

    StringBuilder slug = new StringBuilder(name.length());
    boolean lastWasHyphen = false;

    for (int i = 0; i < name.length(); i++) {
      char c = name.charAt(i);

      if (Character.isLetterOrDigit(c)) {
        slug.append(Character.toLowerCase(c));
        lastWasHyphen = false;
      } else if (Character.isWhitespace(c) || c == '-' || c == '_') {
        if (!lastWasHyphen && slug.length() > 0) {
          slug.append('-');
          lastWasHyphen = true;
        }
      }
    }

    // Remove hífen do final se existir
    if (slug.length() > 0 && slug.charAt(slug.length() - 1) == '-') {
      slug.setLength(slug.length() - 1);
    }

    return slug.toString();
  }

}
