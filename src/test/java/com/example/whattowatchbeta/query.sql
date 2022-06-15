alter table if exists base_details_results drop constraint if exists FKo93iavdlipswx2iov1t94s5b7
alter table if exists base_details_results drop constraint if exists FKjg5g2tot170nqpa3dqwmjiaxg
alter table if exists movie_entity_genre drop constraint if exists FK9bywb8m2w6g1mptr6w570odkc
alter table if exists movie_entity_imageurl drop constraint if exists FKat9746rycxua3fa8dgtv3eyws
alter table if exists newottdetails_map drop constraint if exists FKims24ggohd6c3cung43pufni0
alter table if exists newottdetails_map drop constraint if exists FKb7yq0c17ucu0r6lp7sjljh5jr
alter table if exists streaming_platforms_map drop constraint if exists FK9hlfurrtm7whopom7fsf1nr0o
alter table if exists streaming_platforms_map drop constraint if exists FK2uknfwa3988v0jwwjta6myvpw
drop table if exists base_details cascade
drop table if exists base_details_results cascade
drop table if exists genres cascade
drop table if exists imdbmovie_entity cascade
drop table if exists job_details cascade
drop table if exists key_value_items cascade
drop table if exists movie_entity cascade
drop table if exists movie_entity_genre cascade
drop table if exists movie_entity_imageurl cascade
drop table if exists newottdetails cascade
drop table if exists newottdetails_map cascade
drop table if exists streaming_platforms cascade
drop table if exists streaming_platforms_map cascade
drop sequence if exists hibernate_sequence
create sequence hibernate_sequence start 1 increment 1create table base_details (id int8 not null, page int8, primary key (id))
create table base_details_results (base_details_id int8 not null, results_id int8 not null)
create table genres (id int8 not null, genres varchar(255), primary key (id))
create table imdbmovie_entity (gen_id int8 not null, description varchar(255), genres varchar(255), imdb_rating varchar(255), imdb_rating_votes varchar(255), id varchar(255), image varchar(255), received_date timestamp, runtime_str varchar(255), title varchar(255), primary key (gen_id))
create table job_details (job_id int8 not null, date timestamp, primary key (job_id))
create table key_value_items (id int8 not null, platform varchar(255), url varchar(255), primary key (id))
create table movie_entity (id int8 not null, date timestamp, imdbid varchar(255), imdbrating int8, isottavailable boolean, released int8, synopsis varchar(255), title varchar(255), type varchar(255), primary key (id))create table movie_entity_genre (movie_entity_id int8 not null, genre varchar(255))
create table movie_entity_imageurl (movie_entity_id int8 not null, imageurl varchar(255))
create table newottdetails (imdbid varchar(255) not null, primary key (imdbid))
create table newottdetails_map (newottdetails_imdbid varchar(255) not null, map_id int8 not null)
create table streaming_platforms (id int8 not null, imdbid varchar(255), primary key (id))
create table streaming_platforms_map (streaming_platforms_id int8 not null, map_id int8 not null)
alter table if exists imdbmovie_entity add constraint UK_c1ibwhbdbxsr515nmbxtfrgkg unique (id)
alter table if exists movie_entity add constraint UK_2h8rlow9jbvktga9n3a0mgtof unique (imdbid)
alter table if exists newottdetails_map add constraint UK_jq3hicjvvgs2sakafk0pb6gqr unique (map_id)
alter table if exists streaming_platforms add constraint UK_3v97yoo00uypxv4oywev1h12p unique (imdbid)
alter table if exists streaming_platforms_map add constraint UK_8j80cj4lb4pafhyhbtntp7f2g unique (map_id)
alter table if exists base_details_results add constraint FKo93iavdlipswx2iov1t94s5b7 foreign key (results_id) references movie_entity
alter table if exists base_details_results add constraint FKjg5g2tot170nqpa3dqwmjiaxg foreign key (base_details_id) references base_details
alter table if exists movie_entity_genre add constraint FK9bywb8m2w6g1mptr6w570odkc foreign key (movie_entity_id) references movie_entity
alter table if exists movie_entity_imageurl add constraint FKat9746rycxua3fa8dgtv3eyws foreign key (movie_entity_id) references movie_entity
alter table if exists newottdetails_map add constraint FKims24ggohd6c3cung43pufni0 foreign key (map_id) references key_value_items
alter table if exists newottdetails_map add constraint FKb7yq0c17ucu0r6lp7sjljh5jr foreign key (newottdetails_imdbid) references newottdetails
alter table if exists streaming_platforms_map add constraint FK9hlfurrtm7whopom7fsf1nr0o foreign key (map_id) references key_value_items
alter table if exists streaming_platforms_map add constraint FK2uknfwa3988v0jwwjta6myvpw foreign key (streaming_platforms_id) references streaming_platforms
