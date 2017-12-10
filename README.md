# edw

### TODO
* d3 and cljjsjs d3
* scripts search results pagination
### Summary
Goal is to improve development efficiency.
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

#### Run Locally
``` bash
# from your project home
lein run
# in another terminal, from your project home
lein figwheel
# from browser
http://localhost:3000
```

#### Build
``` bash
lein uberjar
```

### Questions?
Contact xwang@evident.io

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

