CREATE DATABASE order_service_db WITH OWNER=postgres ENCODING='UTF8' CONNECTION LIMIT = -1;
COMMENT ON DATABASE order_service_db IS 'order-service database';

CREATE DATABASE keycloak_db WITH OWNER=postgres ENCODING='UTF8' CONNECTION LIMIT = -1;
COMMENT ON DATABASE keycloak_db IS 'keycloak database';

