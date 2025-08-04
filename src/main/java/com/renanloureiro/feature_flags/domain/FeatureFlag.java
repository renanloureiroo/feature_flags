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
}
