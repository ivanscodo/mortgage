# mortgage

Mortgage application to be used during the hiring process of Ivan Sotelo Codo

# TO run the application

CLone it.
The project was built using Java 21 and Spring boot 3.4.7.
In the source directory of the application call mvn clean install if you have it
installed in your machine, otherwise run it from your IDE.
Access the MortgageApplication class and run the main method. No special
configuration is required.

# To manually test the application

Access the all-requests.http file. If your IDE is IntelliJ, you can run the
requests and check the results, otherwise you can use a tool like
Insomnia or Postman to fire the requests, but in this case you will need to copy
the URLs and the body content.

# Mortgage rates

The mortgage rates are being created from the application.yml file, so in this
way it is not necessary to rebuild the whole application just to add some data.

# Questions raised during the development phase

The task mentioned the calculation of the monthly costs of a mortgage, but there
are two ways to do it, linear, and annuity. I didn't know which one I should
use,
so I implemented the annuity method as it is the only one that would return a
single value.
For the linear the amount as it differs between the beginning and the end of the
mortgage so I created the second method, but it throws an exception,
as the implementation is not done yet, and it would require to change the specs
to return multiple values.

