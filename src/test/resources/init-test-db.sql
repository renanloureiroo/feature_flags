-- Script de inicialização do banco de testes
-- Este script é executado antes dos testes de integração

-- Criar schema específico para testes
CREATE SCHEMA IF NOT EXISTS feature_flags_test;

-- Configurar search_path para o schema de testes
SET search_path TO feature_flags_test, public;

-- Garantir que o usuário tem permissões
GRANT ALL PRIVILEGES ON SCHEMA feature_flags_test TO test_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA feature_flags_test TO test_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA feature_flags_test TO test_user; 