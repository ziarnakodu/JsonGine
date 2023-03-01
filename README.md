# JsonGine
Extended JSON (JavaScript Object Notation)

JsonGine extends JSON by using comments, variable names and constant expressions
The project aims to replace properties-, xml- and json-definition as configuration files

Example jsongine:

    jsongine(version = 1.0,
      // properties
      pi      = 3.14,
      baseUrl       = "https://mydomain.com/",
      org.szb.ziarnakodu.AA = 12,
      CRLF          = "\r\n",
      radix         = 100/*cm*/
    )
    // data for class MyClass
    {
      name   : "myvalue01", //comment
      id     : 1234,
      nrList : [12, 5, /*13,*/ 4, ]
      myMap: {
               "home"    : #baseUrl "home"    #CRLF,//result: "https://mydomain.com/home\r\n"
               "contact" : #baseurl "contact" #CRLF,//result: "https://mydomain.com/contact\r\n"
             }
      circumference : 2*#pi*#radix, //expression
      conn : {
               user  : org.szb.ziarnakodu.plugins.Crypto.decryptAES("MY_USER"),
               passw : org.szb.ziarnakodu.plugins.Crypto.decryptAES("MY_PASSW"),
             }
    }
    
java:

    public class MyClass{
        String name;
        int    id;
        int    list[];
        Map<String, String> myMap;
        int circumference;
        ConnectionData conn;
    }

    class ConnectionData{
        String user;
        String password;
    }

    
    
#Features
- Buildin crypto-plugins gives the possibility to maintain different passwords on local and target server in encripted form
- C/Java Style Single-Line and Multi-Lines Comments.


#Road Map:

- inclusion of other jsongine/json files from local storage or an http/https source.
- Better unit test coverage.
- Stabilize and resolve any bugs.

