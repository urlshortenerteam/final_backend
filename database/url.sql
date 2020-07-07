drop table if exists edit_log;
drop table if exists visit_log;
drop table if exists shorten_log;
drop table if exists users;
create table users
(
	id bigint auto_increment,
	name varchar(50) not null,
	password varchar(50) not null,
	primary key (id),
	unique key (name)
);
create table shorten_log
(
	id bigint auto_increment,
	creator_id bigint not null,
	create_time datetime not null,
	primary key (id),
	foreign key (creator_id) references users(id) on delete cascade
);
create table visit_log
(
	id bigint auto_increment,
	shortener_id varchar(24) not null,
	visit_time datetime not null,
	ip varchar(15) not null,
	device tinyint not null,
	primary key (id)
);
create table edit_log
(
	id bigint auto_increment,
	editor_id bigint not null,
	edit_time datetime not null,
	shortener_id varchar(24) not null,
	primary key (id),
	foreign key (editor_id) references users(id) on delete cascade
);
