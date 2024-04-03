-- ------------------------------------------------------------
-- Dump of table incoming_events
-- ------------------------------------------------------------

drop table if exists incoming_events;
create table incoming_events
(
    id             uuid DEFAULT gen_random_uuid() not null,
    trace_id       uuid                           not null,
    correlation_id uuid                           not null,
    source         varchar(50)                    not null,
    type           varchar(50)                    not null,
    request        jsonb                          not null,
    created_at     timestamp                      not null
);

create index incoming_events_trace_id_index
    on incoming_events (trace_id);

alter table incoming_events
    add constraint incoming_event_pk primary key (id);

comment on column incoming_events.id is 'incoming_events id';
comment on column incoming_events.trace_id is 'UUID of whole operation';
comment on column incoming_events.correlation_id is 'UUID of one part of operation';
comment on column incoming_events.source is 'The service that sent the request';
comment on column incoming_events.type is 'Type of incoming event';
comment on column incoming_events.request is 'Json request';
comment on column incoming_events.created_at is 'Created at date';
