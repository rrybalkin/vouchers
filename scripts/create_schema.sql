CREATE SCHEMA IF NOT EXISTS `talons_db` DEFAULT CHARACTER SET utf8;
CREATE USER 'talons_admin'@'%' identified BY 'talons_admin_pwd';
GRANT ALL PRIVILEGES ON talons_db.* TO 'talons_admin'@'%';