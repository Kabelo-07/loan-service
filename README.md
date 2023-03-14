# Loan Microservice
This is a mini micro-service for a lending platform, that is used to offer loan products to customers. 
A customer is presented with multiple loan products and can choose to accept/decline a specific product.

The project uses OpenAPI specification where API contracts are defined first.

# Todo

* Include a component and sequence diagram would also be created
* The API endpoints would be documented
* More test coverage
* Detailed documentation

# How to run the application

To run the application please ensure you have docker installed and that it is up and running

Navigate into the root folder of the project where you will see two files

* Dockerfile => contains command to create a docker image
* docker-compose.yaml => used to define the loan service, used to start and stop the loan-service with a single command 

To run the application, simply run
```
docker-compose -f docker-compose.yaml up -d
```
The above command will start the loan-service docker container. Run the below to verify if the container is up and running

```
docker ps
```

We should be able to see results similar to the below
```
CONTAINER ID   IMAGE                 COMMAND                  CREATED         STATUS         PORTS                    NAMES
fb1ccb72a92e   loan-service:latest   "java -jar /app/loan…"   2 seconds ago   Up 2 seconds   0.0.0.0:8080->8080/tcp   loan-service
```

We can tail the application logs used the docker container ID, this will show us the application's logs

```
docker logs <CONTAINER_ID> -f
```

# Test the application

In the root directory of the project, a file, ```loan-service.postman_collection.json```, has been included. This file needs to be imported as a Postman collection. 
Have a look at this article: [How to Import Postman Collections](https://learning.postman.com/docs/getting-started/importing-and-exporting-data/)

Once imported into Postman, replace {{baseUrl}} with ```http://localhost:8080``` => this is the host and port where the loan-service is running

### APIS

### 1. Get the list of products

```GET localhost:8080/api/loan-offers``` => Returns a list of product offers, below is the sample response payload

```
[
    {
        "id": "35a33e46-e555-4949-85db-a6077debfcca",
        "name": "Product A",
        "amount": 1000.00,
        "interest_percentage": 10.00,
        "tenure": 15
    },
    {
        "id": "1e886cb1-5799-40c6-9d5a-96c308782c95",
        "name": "Product B",
        "amount": 2500.00,
        "interest_percentage": 12.50,
        "tenure": 30
    }
]
```

### 2. Get the list of customers

```GET localhost:8080/api/customers``` => Returns a list of customers, below is the sample response payload

```
[
    {
        "id": "956e0d73-3d1f-4cc9-86ff-2cd546b8e019",
        "first_name": "Jon",
        "last_name": "Snow",
        "email_address": "js@email.tech",
        "phone_number": "0891231234",
        "communication_method": "SMS",
        "monthly_salary": 150000.00,
        "virtual_account": {
            "id": "4a8db1d4-a660-4303-ba98-a0601ecc680e",
            "balance": 0.00
        }
    },
    {
        "id": "70c33348-c0bf-45ee-8c40-93c3965c959f",
        "first_name": "Steve",
        "last_name": "Smith",
        "email_address": "sm@email.tech",
        "phone_number": "8218218128",
        "communication_method": "EMAIL",
        "monthly_salary": 135000.00,
        "virtual_account": {
            "id": "66493359-0853-4b25-938e-bf8f312e102e",
            "balance": 0.00
        }
    }
]
```

### 3. Accept Offer

```POST localhost:8080/api/loan-offers/:product-id/accept``` => Used for accepting a product/loan offer, below is the JSON request payload. 

The ```:product-id``` path param must be replaced with the product Id (from the product list above)

```
  {
      "customer_id": "<replace with customer id>",
      "payment_method": <must be one of AUTO_DEDUCTION/ BANK_TRANSFER>
  }
```

The Accept offer API returns a created Loan Account, below is the sample response

```
{
    "id": "d6ddb620-f1ff-4ca7-a4ef-e92784b8d55a",
    "principal_amount": 1000.00,
    "repayment_amount": 1100.00,
    "customer_id": "956e0d73-3d1f-4cc9-86ff-2cd546b8e019",
    "product_id": "35a33e46-e555-4949-85db-a6077debfcca",
    "due_date": "2023-03-29T07:28:11.790447Z",
    "payment_method": "AUTO_DEDUCTION"
}
```

Once an offer has been accepted:
 
* The customer virtual account will be credited
* A Notification (Email/Sms) will be sent (currently logged)