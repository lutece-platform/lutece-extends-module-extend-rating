ALTER TABLE extend_rating MODIFY COLUMN id_rating int AUTO_INCREMENT;
ALTER TABLE extend_rating_vote_history MODIFY COLUMN id_vote_history int AUTO_INCREMENT;

ALTER TABLE  extend_rating_config MODIFY COLUMN id_vote_type VARCHAR(100) DEFAULT '' NOT NULL;
update extend_rating_config set id_vote_type='StarRating' where id_vote_type='1';
update extend_rating_config set id_vote_type='ThumbRating' where id_vote_type='2';
update extend_rating_config set id_vote_type='SimpleRating' where id_vote_type='3';

DROP TABLE IF EXISTS extend_rating_vote_type;
DROP TABLE IF EXISTS extend_rating_vote_history;






alter table extend_rating_config modify column id_vote_type VARCHAR(100) DEFAULT '' NOT NULL;