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
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feature_flag_values", uniqueConstraints = {
    @UniqueConstraint(columnNames = { "flag_id", "version" })
})
public class FeatureFlagValue {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "flag_id", nullable = false)
  private FeatureFlag flag;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "value", nullable = false, columnDefinition = "jsonb")
  private JsonNode value;

  @Column(nullable = false)
  @Builder.Default
  private Integer version = 1;

  @Column(name = "updated_by", nullable = false)
  private String updatedBy;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  // Getters e setters podem ser gerados conforme necessário
}