UPDATE microstamp.oauth2_registered_client
SET redirect_uris = REPLACE(redirect_uris, '5173', '3000'),
    post_logout_redirect_uris = REPLACE(post_logout_redirect_uris, '5173', '3000')
WHERE redirect_uris LIKE '%5173%' AND post_logout_redirect_uris LIKE '%5173%';