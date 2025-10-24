# Spring E-Commerce API Documentation

## Overview

This document provides comprehensive information about the Spring E-Commerce REST API endpoints, request/response formats, and usage examples.

**Base URL:** `http://localhost:8080`

**API Version:** 1.0

**Content-Type:** `application/json` (except for multipart endpoints)

---

## Table of Contents

1. [Authentication](#authentication)
2. [Product Endpoints](#product-endpoints)
3. [Order Endpoints](#order-endpoints)
4. [General Endpoints](#general-endpoints)
5. [Error Handling](#error-handling)
6. [Data Models](#data-models)

---

## Authentication

Currently, the API does not require authentication. All endpoints are publicly accessible.

---

## Product Endpoints

### 1. Get All Products

Retrieves a list of all products in the catalog.

**Endpoint:** `GET /api/products`

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "brand": "Dell",
    "price": 999.99,
    "category": "Electronics",
    "releaseDate": "2024-01-15",
    "productAvailable": true,
    "stockQuantity": 50,
    "imageName": "laptop.jpg",
    "imageType": "image/jpeg"
  }
]
```

**Example Request:**

```bash
curl -X GET http://localhost:8080/api/products
```

---

### 2. Get Product by ID

Retrieves detailed information about a specific product.

**Endpoint:** `GET /api/product/{id}`

**Path Parameters:**
- `id` (integer, required): Product ID

**Response:** `200 OK` | `404 NOT FOUND`

```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "brand": "Dell",
  "price": 999.99,
  "category": "Electronics",
  "releaseDate": "2024-01-15",
  "productAvailable": true,
  "stockQuantity": 50,
  "imageName": "laptop.jpg",
  "imageType": "image/jpeg"
}
```

**Example Request:**

```bash
curl -X GET http://localhost:8080/api/product/1
```

---

### 3. Get Product Image

Retrieves the image data for a specific product.

**Endpoint:** `GET /api/product/{productId}/image`

**Path Parameters:**
- `productId` (integer, required): Product ID

**Response:** `200 OK` | `404 NOT FOUND`

**Content-Type:** `image/jpeg`, `image/png`, etc.

**Response Body:** Binary image data

**Example Request:**

```bash
curl -X GET http://localhost:8080/api/product/1/image --output product-image.jpg
```

**Usage in HTML:**

```html
<img src="http://localhost:8080/api/product/1/image" alt="Product Image">
```

---

### 4. Add New Product

Creates a new product in the catalog.

**Endpoint:** `POST /api/product`

**Content-Type:** `multipart/form-data`

**Request Parts:**
- `product` (JSON, required): Product details
- `image` (File, required): Product image file

**Product JSON Structure:**

```json
{
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "brand": "Logitech",
  "price": 29.99,
  "category": "Accessories",
  "releaseDate": "2024-10-01",
  "productAvailable": true,
  "stockQuantity": 100
}
```

**Response:** `201 CREATED` | `500 INTERNAL SERVER ERROR`

```json
{
  "id": 2,
  "name": "Wireless Mouse",
  "description": "Ergonomic wireless mouse",
  "brand": "Logitech",
  "price": 29.99,
  "category": "Accessories",
  "releaseDate": "2024-10-01",
  "productAvailable": true,
  "stockQuantity": 100,
  "imageName": "mouse.jpg",
  "imageType": "image/jpeg"
}
```

**Example Request (cURL):**

```bash
curl -X POST http://localhost:8080/api/product \
  -F 'product={"name":"Wireless Mouse","description":"Ergonomic wireless mouse","brand":"Logitech","price":29.99,"category":"Accessories","releaseDate":"2024-10-01","productAvailable":true,"stockQuantity":100};type=application/json' \
  -F 'image=@/path/to/mouse.jpg'
```

**Example Request (JavaScript):**

```javascript
const formData = new FormData();
const productData = {
  name: "Wireless Mouse",
  description: "Ergonomic wireless mouse",
  brand: "Logitech",
  price: 29.99,
  category: "Accessories",
  releaseDate: "2024-10-01",
  productAvailable: true,
  stockQuantity: 100
};

formData.append('product', new Blob([JSON.stringify(productData)], {
  type: 'application/json'
}));
formData.append('image', fileInput.files[0]);

fetch('http://localhost:8080/api/product', {
  method: 'POST',
  body: formData
})
.then(response => response.json())
.then(data => console.log(data));
```

---

### 5. Update Product

Updates an existing product.

**Endpoint:** `PUT /api/product/{id}`

**Path Parameters:**
- `id` (integer, required): Product ID to update

**Content-Type:** `multipart/form-data`

**Request Parts:**
- `product` (JSON, required): Updated product details
- `image` (File, required): Updated product image

**Response:** `200 OK` | `400 BAD REQUEST`

```json
"Updated"
```

**Example Request:**

```bash
curl -X PUT http://localhost:8080/api/product/1 \
  -F 'product={"id":1,"name":"Updated Laptop","description":"Latest model","brand":"Dell","price":1099.99,"category":"Electronics","releaseDate":"2024-10-01","productAvailable":true,"stockQuantity":30};type=application/json' \
  -F 'image=@/path/to/new-laptop.jpg'
```

---

### 6. Delete Product

Deletes a product from the catalog.

**Endpoint:** `DELETE /api/product/{id}`

**Path Parameters:**
- `id` (integer, required): Product ID to delete

**Response:** `200 OK` | `404 NOT FOUND`

```json
"Deleted"
```

**Example Request:**

```bash
curl -X DELETE http://localhost:8080/api/product/1
```

---

### 7. Search Products

Searches for products by keyword in name, description, brand, or category.

**Endpoint:** `GET /api/products/search`

**Query Parameters:**
- `keyword` (string, required): Search term

**Response:** `200 OK`

```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "brand": "Dell",
    "price": 999.99,
    "category": "Electronics",
    "releaseDate": "2024-01-15",
    "productAvailable": true,
    "stockQuantity": 50,
    "imageName": "laptop.jpg",
    "imageType": "image/jpeg"
  }
]
```

**Example Request:**

```bash
curl -X GET "http://localhost:8080/api/products/search?keyword=laptop"
```

---

## Order Endpoints

### 1. Place Order

Creates a new order with specified items.

**Endpoint:** `POST /api/orders/place`

**Content-Type:** `application/json`

**Request Body:**

```json
{
  "customerName": "John Doe",
  "email": "john.doe@example.com",
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 3,
      "quantity": 1
    }
  ]
}
```

**Response:** `201 CREATED`

```json
{
  "orderId": "ORD-12345678",
  "customerName": "John Doe",
  "email": "john.doe@example.com",
  "status": "PLACED",
  "orderDate": "2024-10-24",
  "items": [
    {
      "productName": "Laptop",
      "quantity": 2,
      "totalPrice": 1999.98
    },
    {
      "productName": "Wireless Mouse",
      "quantity": 1,
      "totalPrice": 29.99
    }
  ]
}
```

**Example Request:**

```bash
curl -X POST http://localhost:8080/api/orders/place \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "John Doe",
    "email": "john.doe@example.com",
    "items": [
      {"productId": 1, "quantity": 2},
      {"productId": 3, "quantity": 1}
    ]
  }'
```

**Example Request (JavaScript):**

```javascript
fetch('http://localhost:8080/api/orders/place', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    customerName: 'John Doe',
    email: 'john.doe@example.com',
    items: [
      { productId: 1, quantity: 2 },
      { productId: 3, quantity: 1 }
    ]
  })
})
.then(response => response.json())
.then(data => console.log(data));
```

---

### 2. Get All Orders

Retrieves all orders from the system.

**Endpoint:** `GET /api/orders`

**Response:** `200 OK`

```json
[
  {
    "orderId": "ORD-12345678",
    "customerName": "John Doe",
    "email": "john.doe@example.com",
    "status": "PLACED",
    "orderDate": "2024-10-24",
    "items": [
      {
        "productName": "Laptop",
        "quantity": 2,
        "totalPrice": 1999.98
      },
      {
        "productName": "Wireless Mouse",
        "quantity": 1,
        "totalPrice": 29.99
      }
    ]
  }
]
```

**Example Request:**

```bash
curl -X GET http://localhost:8080/api/orders
```

---

## General Endpoints

### 1. Health Check

Simple endpoint to verify API is running.

**Endpoint:** `GET /hello`

**Response:** `200 OK`

```json
"Hello"
```

**Example Request:**

```bash
curl -X GET http://localhost:8080/hello
```

---

## Error Handling

The API uses standard HTTP status codes to indicate success or failure:

### Success Codes

- `200 OK`: Request succeeded
- `201 CREATED`: Resource created successfully

### Client Error Codes

- `400 BAD REQUEST`: Invalid request format or parameters
- `404 NOT FOUND`: Resource not found

### Server Error Codes

- `500 INTERNAL SERVER ERROR`: Server-side error occurred

### Common Error Response Format

```json
{
  "timestamp": "2024-10-24T12:30:45.123+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Product not found",
  "path": "/api/product/999"
}
```

### Specific Errors

#### Missing Request Body

**Status:** `400 BAD REQUEST`

```json
{
  "timestamp": "2024-10-24T12:30:45.123+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Required request body is missing",
  "path": "/api/orders/place"
}
```

**Solution:** Ensure you include a valid JSON body and set `Content-Type: application/json` header.

#### Missing Multipart Request Part

**Status:** `400 BAD REQUEST`

```json
{
  "timestamp": "2024-10-24T12:30:45.123+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Required part 'product' is not present",
  "path": "/api/product"
}
```

**Solution:** Ensure you're sending a `multipart/form-data` request with both `product` and `image` parts.

#### Unsupported Media Type

**Status:** `415 UNSUPPORTED MEDIA TYPE`

```json
{
  "timestamp": "2024-10-24T12:30:45.123+00:00",
  "status": 415,
  "error": "Unsupported Media Type",
  "message": "Content-Type 'application/octet-stream' is not supported",
  "path": "/api/product"
}
```

**Solution:** Use the correct `Content-Type` header for your request (e.g., `application/json` or `multipart/form-data`).

---

## Data Models

### Product

```json
{
  "id": "integer (auto-generated)",
  "name": "string (required)",
  "description": "string (optional)",
  "brand": "string (optional)",
  "price": "decimal (required)",
  "category": "string (optional)",
  "releaseDate": "date (YYYY-MM-DD, optional)",
  "productAvailable": "boolean (default: true)",
  "stockQuantity": "integer (required)",
  "imageName": "string (auto-generated)",
  "imageType": "string (auto-generated)",
  "imageData": "byte[] (not included in JSON responses)"
}
```

### OrderRequest

```json
{
  "customerName": "string (required)",
  "email": "string (required, valid email format)",
  "items": [
    {
      "productId": "integer (required)",
      "quantity": "integer (required, > 0)"
    }
  ]
}
```

### OrderResponse

```json
{
  "orderId": "string (auto-generated, format: ORD-XXXXXXXX)",
  "customerName": "string",
  "email": "string",
  "status": "string (PLACED, PROCESSING, SHIPPED, DELIVERED, CANCELLED)",
  "orderDate": "date (YYYY-MM-DD)",
  "items": [
    {
      "productName": "string",
      "quantity": "integer",
      "totalPrice": "decimal"
    }
  ]
}
```

### OrderItemRequest

```json
{
  "productId": "integer (required)",
  "quantity": "integer (required, > 0)"
}
```

### OrderItemResponse

```json
{
  "productName": "string",
  "quantity": "integer",
  "totalPrice": "decimal"
}
```

---

## Best Practices

### 1. Content-Type Headers

Always set the appropriate `Content-Type` header:

- For JSON requests: `Content-Type: application/json`
- For multipart requests: Let the client library set it automatically (includes boundary parameter)

### 2. Error Handling

Always check the HTTP status code and handle errors appropriately in your client application.

### 3. Image Uploads

When uploading images:
- Supported formats: JPEG, PNG, GIF, etc.
- Recommended maximum size: 5MB
- Send as multipart form data with the `image` field

### 4. Product Stock

When placing orders, the system automatically decrements the stock quantity. Ensure products have sufficient stock before ordering.

### 5. Order ID Format

Order IDs are auto-generated with the format `ORD-XXXXXXXX` where X represents random digits.

---

## Testing with Postman

### Setting up Postman for Product Creation

1. **Create a new POST request**
2. **URL:** `http://localhost:8080/api/product`
3. **Body tab:**
   - Select "form-data"
   - Add key `product` (type: Text)
   - Value: `{"name":"Test Product","price":99.99,"stockQuantity":10,"productAvailable":true}`
   - Add key `image` (type: File)
   - Select an image file
4. **Headers:** Remove any manually added `Content-Type` headers
5. **Click Send**

### Setting up Postman for Order Creation

1. **Create a new POST request**
2. **URL:** `http://localhost:8080/api/orders/place`
3. **Headers tab:**
   - Add `Content-Type: application/json`
4. **Body tab:**
   - Select "raw"
   - Select "JSON" from dropdown
   - Enter JSON body:
   ```json
   {
     "customerName": "Test User",
     "email": "test@example.com",
     "items": [
       {"productId": 1, "quantity": 2}
     ]
   }
   ```
5. **Click Send**

---

## Changelog

### Version 1.0 (October 24, 2024)

- Initial API release
- Product CRUD operations
- Order placement and retrieval
- Product search functionality
- Image upload and retrieval

---

## Support

For issues, questions, or contributions, please contact the development team.

**Developer:** Sahin  
**Project:** Spring E-Commerce Application  
**Framework:** Spring Boot 3.x  
**Database:** JPA/Hibernate

