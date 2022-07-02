# Hero Company Spring RestApi With Security

This project is a simulation of an e-commerce project. By researching the basic needs of an e-commerce company, the necessary product transactions can be added to the basket and purchased together with the customer transactions.It is a rest Service project written in Spring and used as a MySql database. We provide mail for password change processes using Spring Email.With jwt encryption logic, we can receive jwt and perform operations in accordance with these jwt authorizations.Service project tested in Postman and documented in Swagger.

The project offers a RestApi application that takes orders from an institution's customers and products, and tracks the company's product additions and subsequent sales to its users. Spring Boot, Spring RestApi, Spring Security, JPA, MySql, Javax Validation and Spring Mail Framework APIs are used in the project software. In the project, service procurement was provided according to roles with the JWT method. In the project, the data that is regularly consumed in case of need has been worked with cache methods and the application cost has been brought to the most ergonomic level. PostMan and Swagger tools have been used in the documentation of this project and have been adapted to OpenApi standards.

## Languages, Technologies and Environments Used in this Project


| Java  | MySQL | Spring Boot | Spring RestApi | Spring Security |
| :------------: | :------------: | :------------: | :------------: | :------------: |
|  <img src ="https://cdn.iconscout.com/icon/free/png-256/java-60-1174953.png" width ="100px" height = "100px" style="float:left" > | <img src ="https://www.onurbabur.com/wp-content/uploads/2020/09/MySQL-Logo.wine_-2048x1365.png" width ="65px" height = "65px" style="float:left " >  |  <img src ="https://www.nisabalci.com/wp-content/uploads/2021/07/springboot.jpg" width ="65px" height = "65px" style="float:left " > | <img src ="https://fiverr-res.cloudinary.com/images/t_main1,q_auto,f_auto,q_auto,f_auto/gigs/146771316/original/36864586c582c9e048b515f736db5286428ecfec/develop-a-spring-rest-api.png" width ="65px" height = "65px" >  | <img src ="https://miro.medium.com/max/1838/1*3jPSO8IO9r0k0QTnvrTo_Q.jpeg" width ="65px" height = "65px" >  |

## Project Overview 

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/1-admin_register_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/1-admin_register_result_true.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/2-admin_auth_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/2-admin_auth_result_true.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/4-admin_settings_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/4-admin_settings_result_true.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/5-customer_register_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/5-customer_register_result_false.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/6-customer_auth_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/6-customer_auth_result_true.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/8-admin_list_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/8-admin_list_result_1.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/9_category_save_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/9_category_save_status_false.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/10_category_list_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/10_category_list_status.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/11-category_update_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/11-category_update_result.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/12-category_delete_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/12-category_delete_status.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/13-product_save_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/13-product_save_result.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/16-product_update_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/16-product_update_result.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/17-product_search_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/17-product_search_result.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/18-admin_customer_changeEnableFalse_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/18-admin_customer_changeEnableFalse_result.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/19-basket_save_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/19-basket_save_result.PNG" width="300">|

|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/20-order_save_send.PNG" width="300">|<img src="https://github.com/omerkircal/Spring-RESTAPI-With-Security-JWT-/blob/main/images/20-order_save_result.PNG" width="300">|
