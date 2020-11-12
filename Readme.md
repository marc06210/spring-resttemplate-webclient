run the auth-server, the resource-server and then the resttemplate-webclient

## webclient

The WebClient must be called from a servlet context only.

### working configuration

curl http://localhost:8080/testW
returns a list of messages

### error getting token
curl http://localhost:8080/testW?client=client-ko
returns so far a 200 return code but with message 'auth error detected'

### 500 resource server example
curl http://localhost:8080/testW?url=empty500
resource server returns 500 without message
or
curl http://localhost:8080/testW?url=error500
resource server returns 500 with message

## resttemplate

The RestTemplate object can be invoked without any servlet context.

### working configuration
curl http://localhost:8080/testR
returns a list of messages

### error getting token
curl http://localhost:8080/testR?client=client-ko
returns the generic IllegalStateException about 'An OAuth 2 access token must be obtained or an exception thrown'
