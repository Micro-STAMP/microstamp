INSERT INTO oauth2_registered_client
(id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings)
VALUES(uuid(),
       'fbefbe1f-933d-4012-b730-16db007cd13e',
	   now(),
	   '$2a$10$KTxMbNUqXonlI98nVxNvdO9ONGFZ8dCPBQJDSlWBMhGih8lfHsZGy',
	   NULL,
	   'microstamp',
	   'client_secret_basic',
	   'refresh_token,client_credentials,authorization_code',
	   'http://127.0.0.1:9000/login/oauth2/code/client-server-microstamp',
	   'http://127.0.0.1:9000/',
	   'openid,profile',
	   '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
	   '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');


INSERT INTO oauth2_registered_client
(id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings)
VALUES(uuid(),
       '309492a6-d293-4b90-9784-5831ebc50895',
	   now(),
	   '$2a$10$30Uaqu8ESX1kOfmb2t7vW.3Y1saWgEXknTpLolf9T9vGcIYXh4EkS',
	   NULL,
	   'step1',
	   'client_secret_basic',
	   'refresh_token,client_credentials,authorization_code',
	   'http://127.0.0.1:9001/login/oauth2/code/client-server-step1',
	   'http://127.0.0.1:9001/',
	   'openid,profile',
	   '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
	   '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');


INSERT INTO oauth2_registered_client
(id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings)
VALUES(uuid(),
       '0bade5db-0a74-4b3f-a6bd-ff04382f1efd',
	   now(),
	   '$2a$10$dTG0ZJPbSsqvzfXWAVS9UO4kkcJx3kY/dB3U/6mUurwCVlkjZhCSq',
	   NULL,
	   'step2',
	   'client_secret_basic',
	   'refresh_token,client_credentials,authorization_code',
	   'http://127.0.0.1:9002/login/oauth2/code/client-server-step2',
	   'http://127.0.0.1:9002/',
	   'openid,profile',
	   '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
	   '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');


INSERT INTO oauth2_registered_client
(id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings)
VALUES(uuid(),
       '826c5356-3a86-4902-ae4e-9a3f6afdf31c',
	   now(),
	   '$2a$10$arC9aM.qF4CK3v9DQiMrSeSPlzlF7nFVTE9lDU8OhRBIZhCzoV2pi',
	   NULL,
	   'step3',
	   'client_secret_basic',
	   'refresh_token,client_credentials,authorization_code',
	   'http://127.0.0.1:9003/login/oauth2/code/client-server-step3',
	   'http://127.0.0.1:9003/',
	   'openid,profile',
	   '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
	   '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');


INSERT INTO oauth2_registered_client
(id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings)
VALUES(uuid(),
	   '67a7ce07-2c50-4e24-9ce9-f339a69f64ee',
	   now(),
	   '$2a$10$zMNLybl4GxNAjOhYRNigC.PWRLOmsMbIOly19S5MwoFqhlh9EElqy',
	   NULL,
	   'step4',
	   'client_secret_basic',
	   'refresh_token,client_credentials,authorization_code',
	   'http://127.0.0.1:9004/login/oauth2/code/client-server-step4',
	   'http://127.0.0.1:9004/',
	   'openid,profile',
	   '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
	   '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');