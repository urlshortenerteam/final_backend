drop table if exists edit_log;
drop table if exists visit_log;
drop table if exists shorten_log;
drop table if exists users;
create table users
(
	id int auto_increment,
	name varchar(50) not null,
	password varchar(50) not null,
	primary key (id),
	unique key (name)
);
create table shorten_log
(
	id int auto_increment,
	short varchar(30) not null,
	creator_id int not null,
	create_time datetime not null,
	primary key (id),
	foreign key (creator_id) references users(id) on delete cascade
);
create table visit_log
(
	id int auto_increment,
	shortener_id varchar(24) not null,
	visit_time datetime not null,
	ip varchar(15) not null,
	device tinyint not null,
	primary key (id)
);
create table edit_log
(
	id int auto_increment,
	editor_id varchar(50) not null,
	edit_time datetime not null,
	shorten_id int not null,
	primary key (id),
	foreign key (shorten_id) references shorten_log(id) on delete cascade
);