# Padrão de Erros da API

```json
{
  "timestamp": "2025-07-23T18:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "<mensagem detalhada>",
  "path": "/api/v1/flags"
}
```

- status: HTTP status code
- error: resumo
- message: detalhamento amigável
- details (opcional): lista de campos inválidos etc.

---

## 9. CHANGELOG

**Arquivo:** `CHANGELOG.md`

# Changelog

Todas as mudanças notáveis neste projeto serão documentadas aqui.

## [Unreleased]

### Added

- ...

### Changed

- ...

### Fixed

- ...

## [0.1.0] - 2025-07-23

### Added

- MVP inicial
