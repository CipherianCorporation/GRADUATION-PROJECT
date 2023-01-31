![shoeshy-logo](https://user-images.githubusercontent.com/52403567/202708244-38f7033e-ec32-4124-b807-4fa6b034823f.png)
## ShoeShy - Shoe store website

A final graduation project at FPT Polytechnic, a shoe store website made with Spring Boot, payment integration with Paypal Sandbox, there are 4 roles: USER, ADMIN, SHIPPER and STAFF. USER can only visit homepage, SHIPPER can visit both homepage and shipper page while STAFF and ADMIN can visit both homepage and admin page. User can sign in and sign up with normal account or with OAuth2 account (GG,FB), edit profile, change password, add favorite products, print order receipt, see orders history, reset password, and checkout with or without voucher, search & sort products. ADMIN and STAFF both can visit admin page, view Dashboard, CRUD User, Product, Order, Category & SubCategory, Voucher, only ADMIN have authorizing privilege, export data to excel, pdf, csv, upload excel, pdf, csv to db (to be added). Testing with TestNG that can read Excel data then export results to Excel.

Stack used:
  *	Stack:
    -	Frontend: AngularJS, Thymeleaf, MDBootstrap 5, JQuery
    -	Backend: Spring Boot, Spring Data JPA, Spring Security, OAuth2
    -	Database: SQL Server 2019 (Somee.com)
    - Testing: TestNG
  *	Software:
    -	Visual Studio Code + Spring Boot extension
    -	[Adoptium Temurin JDK 17](https://adoptium.net/)

## Installation
1. Clone this repo, create database by running the shoeshy_db.sql script inside db folder
2. Config application.properties according to your credentials
  ```
    # for SQLServer Database connection
    spring.datasource.url=jdbc:sqlserver://localhost:1433;database=<your database>
    spring.datasource.username=<your username>
    spring.datasource.password=<your password>
    spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
    spring.jpa.hibernate.dialect=org.hibernate.dialect.SQLServer2012Dialect
    spring.jpa.show-sql=false
    spring.jpa.hibernate.ddl-auto = none
  ```
  - Create a app password for your gmail in order to send email in this webapp
  ```
    # for send mail contact
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=<your gmail username>
    spring.mail.password=<your gmail app password>
    spring.mail.properties.mail.smpt.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true
  ```
  - Create Google, Facebook OAuth app and put your client-id, secret key to here, if you don't know how pls check this [link](https://www.codejava.net/frameworks/spring-boot/social-login-with-google-and-facebook-examples)
  ```
    # Google
    spring.security.oauth2.client.registration.google.client-id=<your google client id>
    spring.security.oauth2.client.registration.google.client-secret=<your google client secret>
    spring.security.oauth2.client.registration.google.scope=email,profile

    # Facebook
    spring.security.oauth2.client.registration.facebook.client-id=<your facebook client id>
    spring.security.oauth2.client.registration.facebook.client-secret=<your facebook client secret>
    spring.security.oauth2.client.registration.facebook.scope=email,public_profile
  ```
  - Create a Paypal Sandbox account and fill your business account id and secret here (tips: when checkout use your sandbox personal account email and password)
  ```
    paypal.mode = sandbox
    paypal.client.id=<your sandbox business account id>
    paypal.client.secret=<your sandbox business account secret>
  ```
  - Subscribe to [apilayer.com's Exchange Rate Data API](https://apilayer.com/marketplace/exchangerates_data-api) and fill your api key here
  ```
    apilayer.apikey=<your api key>
  ```

## Visuals - Demo

- User homepage

![image](https://user-images.githubusercontent.com/52403567/202708402-f50f8bf1-bf2d-4b16-8762-df16fc2a2b3b.png)

![image](https://user-images.githubusercontent.com/52403567/202708577-84cc2c61-8214-4631-98ee-090ea1c850ed.png)

![image](https://user-images.githubusercontent.com/52403567/202708667-83f63c39-775c-438e-ac83-27e31b9d9612.png)

![image](https://user-images.githubusercontent.com/52403567/202708736-ada31387-be62-46a4-a562-7c44b62d5154.png)

![image](https://user-images.githubusercontent.com/52403567/202708931-a139465b-07c6-4ed5-8f6b-4fcfdcdfde49.png)

![image](https://user-images.githubusercontent.com/52403567/202709055-49371876-5548-4c5b-8a6b-5ad40ef7d4ae.png)

- Admin homepage

![image](https://user-images.githubusercontent.com/52403567/202709150-e97513ef-a1c5-4f83-a84d-2213e4360ed7.png)

![image](https://user-images.githubusercontent.com/52403567/202709319-800e0d1e-9780-4fd5-a67c-84382a14645e.png)

![image](https://user-images.githubusercontent.com/52403567/202709399-81edd408-5b44-4f82-9d1d-5b1425767b7f.png)

![image](https://user-images.githubusercontent.com/52403567/202709452-6787ddbd-dc74-42b5-b0e7-6398b5534718.png)

![image](https://user-images.githubusercontent.com/52403567/202709532-f9acd654-c2ab-4abe-90ca-47a25d4a7c46.png)

![image](https://user-images.githubusercontent.com/52403567/202709644-388d6e21-ec2d-42db-8bab-5ecf31d8a16b.png)


## Support
If things are unclear, PM me at [balisongian@gmail.com](mailto:balisongian@gmail.com)


## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.


## Authors and acknowledgment
Authors of this project: 

- [jackytank](https://github.com/jackytank)
- [HiepNguyen0402](https://github.com/HiepNguyen0402)
- [supercum](https://github.com/supercuem)
- [tamntps15500](https://github.com/tamntps15500)
- [tranvohoaian0801](https://github.com/tranvohoaian0801)

## License
[MIT](https://choosealicense.com/licenses/mit/)
