-- liquibase formatted sql

--
-- Pre-execution script of the module (plugin-liquibase >= 2.0.2), run before the version resolution of the main
-- liquibase update.
--
-- The SQL files of the 3.x releases carry no Liquibase header: on a Lutece 7 site, plugin-liquibase records no
-- version for extend-rating. Without one, the main run takes the module for a fresh install: init_core fails on the
-- existing rights and create_db drops extend_rating and extend_rating_config with every vote and configuration.
-- Each changeset records the version the existing schema proves, when the tables exist and no version does:
--   - extend_rating_vote_type present: a 1.1.5 to 2.x schema, the 2.0.0 to 3.0.0 upgrade still has to run;
--   - extend_rating_vote_type absent: the 3.x schema, unchanged since 3.0.0.
-- False on a fresh install and on a site already followed by Liquibase (MARK_RAN; the query itself may fail on an
-- empty database: onError:MARK_RAN). Each statement repeats its condition, for a run that ignores preconditions.
--

-- changeset extend-rating:prerun-version-of-a-v7-site-2.0.0
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_datastore WHERE entity_key LIKE '%core.plugins.status.extend-rating.version'
-- precondition-sql-check expectedResult:2 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = database() AND table_name IN ('extend_rating_config', 'extend_rating_vote_type')
INSERT INTO core_datastore (entity_key, entity_value)
SELECT 'core.plugins.status.extend-rating.version', '2.0.0' FROM information_schema.tables
WHERE table_schema = database() AND table_name = 'extend_rating_vote_type'
AND EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = database() AND table_name = 'extend_rating_config')
AND NOT EXISTS (SELECT 1 FROM core_datastore WHERE entity_key LIKE '%core.plugins.status.extend-rating.version');

-- changeset extend-rating:prerun-version-of-a-v7-site-3.0.0
-- preconditions onFail:MARK_RAN onError:MARK_RAN
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM core_datastore WHERE entity_key LIKE '%core.plugins.status.extend-rating.version'
-- precondition-sql-check expectedResult:1 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = database() AND table_name = 'extend_rating_config'
-- precondition-sql-check expectedResult:0 SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = database() AND table_name = 'extend_rating_vote_type'
INSERT INTO core_datastore (entity_key, entity_value)
SELECT 'core.plugins.status.extend-rating.version', '3.0.0' FROM information_schema.tables
WHERE table_schema = database() AND table_name = 'extend_rating_config'
AND NOT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = database() AND table_name = 'extend_rating_vote_type')
AND NOT EXISTS (SELECT 1 FROM core_datastore WHERE entity_key LIKE '%core.plugins.status.extend-rating.version');
