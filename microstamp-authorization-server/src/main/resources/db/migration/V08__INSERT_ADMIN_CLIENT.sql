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
        'admin',
        '2024-08-07 02:19:12',
        '$2a$10$FVRj8e83jjoGnZAY55Vol.JSG65F1rpsu0Sq.0RaX41LMnwld0PKa',
        NULL,
        'admin-client',
        'client_secret_post',
        'refresh_token,client_credentials,authorization_code',
        'http://127.0.0.1',
        'http://127.0.0.1',
        'openid,profile',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false}',
        '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",3000.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');