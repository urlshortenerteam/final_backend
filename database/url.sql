drop table if exists editLog;
drop table if exists visitLog;
drop table if exists shortenLog;
drop table if exists users;
create table users
(
	id bigint auto_increment,
	name varchar(50) not null,
	password varchar(50) not null,
	email varchar(50) not null,
	role int not null,
	visitCount bigint not null,
	primary key (id),
	unique key (name)
);
create table shortenLog
(
	id bigint auto_increment,
	shortUrl varchar(6) not null,
	creatorId bigint not null,
	createTime datetime not null,
	visitCount bigint not null,
	primary key (id),
	unique key (shortUrl),
	foreign key (creatorId) references users(id) on delete cascade
);
create table visitLog
(
	id bigint auto_increment,
	shortenerId varchar(24) not null,
	visitTime datetime not null,
	ip varchar(15) not null,
	device tinyint not null,
	primary key (id)
);
create table editLog
(
	id bigint auto_increment,
	editorId bigint not null,
	editTime datetime not null,
	shortenerId varchar(24) not null,
	primary key (id),
	foreign key (editorId) references users(id) on delete cascade
);
