## **Cars Theft Task - API Endpoints**

## **Database Setup**

The TheftCars project uses MySQL as its database. 

1. **Create the Schema:**
    
    Create a new schema called **`theftcars`**. :
    
    ```sql
    CREATE SCHEMA theftcars;
    ```
    
2. **Load Data from CSV:**
    
   The repository includes a file named **`MySQL-LoadData`**. 
This file contains SQL commands to extract data from a CSV file and load it into the database.


### **Cars Endpoint**

The API provides access to information about cars relevant to the task of car thefts. 
Below are the available endpoints:

- **GET /api/cars/carBrand**
    - *Description*: Retrieves a list of cars filtered by card brand.
    - *Example*: **`/api/cars/fiat`**
    - *Response*:
        
        [
          {
            "id": 19,
            "carBrand": "Fiat",
            "countryOfOrigin": "Italy"
          }
        ]
        
        ```
        

### **Pagination**

The API supports pagination for retrieving car data relevant to the task of car thefts. By default, the API returns a page with a size of 5 items and starts at page 0. However, clients can specify the page number and page size using query parameters:

- **Page Number**: Use the **`page`** parameter to specify the page number (starting from 0).
- **Page Size**: Use the **`size`** parameter to adjust the number of items per page. The maximum allowed size for better performance is 5.

### Default Values

If no **`page`** and **`size`** parameters are provided, the API defaults to the following:

- **`page`**: 0
- **`size`**: 5

### Example Client Input for Pagination

To retrieve the second page of cars relevant to the car theft task with a page size of 5, the client can make a request as follows:

```nasm
GET /api/cars/fiat?page=1&size=5
```
