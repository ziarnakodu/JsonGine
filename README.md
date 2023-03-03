# JsonGine
Extended JSON (JavaScript Object Notation)

JSON is a popular choice for configuration files due to its simplicity, human-readable format, and interoperability with various programming languages. However it does not have a built-in support for comments, which can make it difficult to document and explain certain aspects of the configuration. This can be a disadvantage in situations where a configuration file needs to be shared and understood by multiple developers or system administrators.

In some cases, developers work around this limitation by including comments in their JSON files as string values, but this can make the files harder to read and modify. Another option is to use a separate file or tool to document the configuration settings.

JsonGine syntax enhances JSON by incorporating Java-like comments and enables the use of encrypted passwords. With this feature, it becomes feasible to use a single configuration file for both development and production environments. The master key for password decryption is stored securely as an environment variable in each environment.

JsonGine also facilitates the use of property variables in addition to its other features.

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

    
    
# Features
- Buildin crypto-plugins gives the possibility to maintain different passwords on local and target server in encripted form
- C/Java Style Single-Line and Multi-Lines Comments.


# Road Map:

- inclusion of other jsongine/json files from local storage or an http/https source.
- Better unit test coverage.
- Stabilize and resolve any bugs.


