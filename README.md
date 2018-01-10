# edw

### TODO
* builtin scripts and their cmd line script
    * redis wrapper (export of key-vals by pattern)
    * populate built-in rows into db
* parameter scripts
    * each script allow a binding of params from string to string.
        * how string values are converted depends on script type
    * each execution record both the script and params
    * builtin script provide script and default params binding
    * script and params binding are stored together as value in json format.
``` json
{"script": "echo abc",
 "params":
   {
     "param1": "value1",
     "param2": "[1, 2, 3]"
   }
}
```

* aws scripts in clojure: creating/deleting/checking signatures scripts
* caching aws function calls
* application dashboard: monitoring health of system
* Kafka Stream play
* this webapp deploy to aws instance and secure

### Summary
* store, search, execute bash, aws, python scripts at server

### Goal
To improve development efficiency.
* Certain things are easier to be done in a html page.
* AWS API playground
* Ideas playground

### Setup
#### AWS Setup
* AWS region and credentials needs be setup using command

``` bash
aws configure
```
This will generate .aws directory in your home directory. Two files
are generated under .aws.
#### Prerequisite Programs
* JDK 8 or later
* download lein and ensure it's executable and in your path:
 https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein

#### Redis
#### JDK
#### Python
#### Run Locally
``` bash
# from your project home
lein run
# in another terminal, from your project home
lein figwheel
# from browser
http://localhost:3123
```

#### Build
``` bash
lein clean
lein cljsbuild once
lein uberjar
```

### Sample .aws config files content
File config sample content:
```
[default]
region = us-west-1
output = json
```
File credentials sample content:
```
[default]
aws_access_key_id = XXXXXX
aws_secret_access_key = YYYYYYYYYY

```

