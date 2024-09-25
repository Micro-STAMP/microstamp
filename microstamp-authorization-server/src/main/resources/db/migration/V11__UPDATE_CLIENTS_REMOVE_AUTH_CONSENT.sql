UPDATE oauth2_registered_client
	SET client_settings='{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}'
	WHERE client_name='microstamp';

UPDATE oauth2_registered_client
	SET client_settings='{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}'
	WHERE client_name='step1';

UPDATE oauth2_registered_client
	SET client_settings='{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}'
	WHERE client_name='step2';

UPDATE oauth2_registered_client
	SET client_settings='{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}'
	WHERE client_name='step3';

UPDATE oauth2_registered_client
	SET client_settings='{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}'
	WHERE client_name='step4';