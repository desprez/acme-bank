create table bank_account (id bigint not null, account_number varchar(34), balance varchar(255), created_date date, lower_limit varchar(255), status varchar(1), history_id bigint, primary key (id))
create table hibernate_sequences (sequence_name varchar(255) not null, next_val bigint, primary key (sequence_name))
create table history (id bigint not null, balance varchar(255), date binary(255), previous_id bigint, primary key (id))
alter table bank_account add constraint FKomarfo061l46dmnbu8cuawn4i foreign key (history_id) references history
alter table history add constraint FK5jgpb5xklbxbvpese7ja1ljw7 foreign key (previous_id) references history
