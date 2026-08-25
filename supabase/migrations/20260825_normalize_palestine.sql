-- Run once in the Supabase SQL editor before shipping this app version.
-- It merges legacy Israel/Palestine aliases into the canonical Palestine code: PS.
BEGIN;

WITH moved_rows AS (
    DELETE FROM country_message_stats
    WHERE upper(country_code) IN ('IL', 'ISR', 'ISRAEL', 'PS', 'PSE', 'PALESTINE')
       OR country_code ILIKE '%israel%'
    RETURNING state_counts, total_count
),
state_totals AS (
    SELECT entry.key AS state, SUM(entry.value::BIGINT) AS count
    FROM moved_rows,
         jsonb_each_text(moved_rows.state_counts) AS entry(key, value)
    GROUP BY entry.key
)
INSERT INTO country_message_stats (country_code, state_counts, total_count)
SELECT
    'PS',
    COALESCE((SELECT jsonb_object_agg(state, count) FROM state_totals), '{}'::JSONB),
    COALESCE((SELECT SUM(total_count) FROM moved_rows), 0)
WHERE EXISTS (SELECT 1 FROM moved_rows);

COMMIT;
