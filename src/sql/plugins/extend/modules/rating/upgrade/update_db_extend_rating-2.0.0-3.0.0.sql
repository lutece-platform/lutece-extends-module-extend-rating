ALTER TABLE extend_rating MODIFY COLUMN id_rating int AUTO_INCREMENT;

ALTER TABLE extend_rating_config MODIFY COLUMN id_vote_type VARCHAR(100) DEFAULT '' NOT NULL;
update extend_rating_config set id_vote_type='fr.paris.lutece.plugins.extend.modules.rating.business.StarRating' where id_vote_type='1';
update extend_rating_config set id_vote_type='fr.paris.lutece.plugins.extend.modules.rating.business.ThumbRating' where id_vote_type='2';
update extend_rating_config set id_vote_type='fr.paris.lutece.plugins.extend.modules.rating.business.SimpleRating' where id_vote_type='3';

DROP TABLE IF EXISTS extend_rating_vote_type;
DROP TABLE IF EXISTS extend_rating_vote_history;

ALTER TABLE extend_rating_config MODIFY COLUMN id_vote_type VARCHAR(100) DEFAULT '' NOT NULL;