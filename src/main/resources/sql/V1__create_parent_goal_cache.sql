-- pgvector extension 활성화
CREATE EXTENSION IF NOT EXISTS vector;

-- 부모 목표 embedding 캐시 테이블
CREATE TABLE IF NOT EXISTS parent_goal_cache (
    id                   BIGSERIAL    PRIMARY KEY,
    parent_goal          TEXT         NOT NULL,
    embedding            vector(1536) NOT NULL,
    child_goals          TEXT[]       NOT NULL,
    created_at           TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- 벡터 유사도 검색용 HNSW 인덱스 (cosine similarity)
CREATE INDEX IF NOT EXISTS idx_parent_goal_cache_embedding
    ON parent_goal_cache
    USING hnsw (embedding vector_cosine_ops);