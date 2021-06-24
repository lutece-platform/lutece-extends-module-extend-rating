--
-- Structure for table extend_rating
--
DROP TABLE IF EXISTS extend_rating;
CREATE TABLE extend_rating (
	id_rating INT DEFAULT 0 NOT NULL,
	id_resource VARCHAR(100) DEFAULT '' NOT NULL,
	resource_type VARCHAR(255) DEFAULT '' NOT NULL,
	rating_type VARCHAR(255) DEFAULT '' NOT NULL,
	vote_count INT default 0 NOT NULL,
	score_value DOUBLE default 0 NOT NULL,
	PRIMARY KEY (id_rating)
);

--
-- Structure for table extend_rating_config
--
DROP TABLE IF EXISTS extend_rating_config;
CREATE TABLE extend_rating_config (
	id_extender INT DEFAULT 0 NOT NULL,
	id_mailing_list INT DEFAULT -1 NOT NULL,
	id_vote_type INT DEFAULT 1 NOT NULL,
	is_unique_vote SMALLINT DEFAULT 0 NOT NULL,
	nb_days_to_vote INT DEFAULT 0 NOT NULL,
	is_connected INT DEFAULT 0 NOT NULL,
	is_active INT DEFAULT 1 NOT NULL,
	nb_vote_per_user INT DEFAULT 0 NOT NULL,
	delete_vote INT DEFAULT 0 NOT NULL,
	date_start TIMESTAMP DEFAULT NULL NULL,
	date_end TIMESTAMP DEFAULT NULL NULL,
	PRIMARY KEY (id_extender)
);

--
-- Structure for table extend_rating_vote_type
--
DROP TABLE IF EXISTS extend_rating_vote_type;
CREATE TABLE extend_rating_vote_type (
	id_vote_type INT DEFAULT 0 NOT NULL,
	title VARCHAR(255) DEFAULT '' NOT NULL,
	template_name VARCHAR(100) DEFAULT '' NOT NULL,
	PRIMARY KEY (id_vote_type)
);

--
-- Structure for table extend_rating_vote_history
--
DROP TABLE IF EXISTS extend_rating_vote_history;
CREATE TABLE extend_rating_vote_history (
	id_vote_history INT DEFAULT 0 NOT NULL,
	id_extender_history INT DEFAULT 0 NOT NULL,
	vote_value DOUBLE DEFAULT 0 NOT NULL,
	PRIMARY KEY (id_vote_history)
);
