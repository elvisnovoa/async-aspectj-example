# async-aspectj-example
Example of an aspect advising a controller method by calling a REST API and extracting additional data from the request. A custom annotation instructs the advise to perform the request 
asynchronously or to wait for the response before continuing execution. 

## Usage:
### /user/notadvised 
This method is not matched by our pointcut, so it's not advised.

**Request:**
```
curl -X POST \
  'http://localhost:8080/user/notadvised?param=Hello' \
  -H 'Content-Type: application/json' \
  -H 'Custom-Header: Custom Header Value' \
  -d '{
	"userId": "CloudFoundry"
}'
```

**Response:**
```
{
    "success": true,
    "data": {}
}
```

## Usage:
### /user/synchronous
This method is advised by calling a service and waiting for the response before continuing execution.

**Request:**
```
curl -X POST \
  'http://localhost:8080/user/synchronous?param=Hello' \
  -H 'Content-Type: application/json' \
  -H 'Custom-Header: Custom Header Value' \
  -d '{
	"userId": "CloudFoundry"
}'
```

**Response:**
```
{
    "success": true,
    "data": {
        "Custom-Header": "Custom Header Value",
        "param": "Hello",
        "isAsync": false,
        "user": {
            "name": "Cloud Foundry",
            "blog": "https://www.cloudfoundry.org/"
        }
    }
}
```

## Usage:
### /user/asynchronous
This method is advised by calling a service asynchronously and continuing execution.

**Request:**
```
curl -X POST \
  'http://localhost:8080/user/asynchronous?param=Hello' \
  -H 'Content-Type: application/json' \
  -H 'Custom-Header: Custom Header Value' \
  -d '{
	"userId": "CloudFoundry"
}'
```

**Response:**
```
{
    "success": true,
    "data": {
        "Custom-Header": "Custom Header Value",
        "param": "Hello",
        "isAsync": true,
        "user": null
    }
}
```
