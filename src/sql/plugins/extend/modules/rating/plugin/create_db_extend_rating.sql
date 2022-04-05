--
-- Structure for table extend_rating
--
DROP TABLE IF EXISTS extend_rating;
CREATE TABLE extend_rating (
	id_rating int AUTO_INCREMENT,
	id_extender_history INT DEFAULT 0 NOT NULL,
	id_resource VARCHAR(100) DEFAULT '' NOT NULL,
	resource_type VARCHAR(255) DEFAULT '' NOT NULL,
	user_guid VARCHAR(255) DEFAULT '' NOT NULL,
	rating_value float NOT NULL,
	rating_type VARCHAR(100) NOT NULL,
	
	PRIMARY KEY (id_rating)
);

--
-- Structure for table extend_rating_config
--
DROP TABLE IF EXISTS extend_rating_config;
CREATE TABLE extend_rating_config (
	id_extender INT DEFAULT 0 NOT NULL,
	id_mailing_list INT DEFAULT -1 NOT NULL,
	id_vote_type VARCHAR(100) DEFAULT '' NOT NULL,
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