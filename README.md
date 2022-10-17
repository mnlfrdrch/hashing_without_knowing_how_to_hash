Introduction
============
'Hashing without knowing how to hash' is the implementation of a protocol designed in my bachelor thesis.
The protocol is meant to be used in the encoding step of Privacy Preserving Record Linkage.
The goal is to compute hash values for a hash based encoding distributed, such that no party knows the exact hash function. This should make frequency attacks more difficult.
To fulfill the requirements, the protocol uses a combination of the XOR Secret Sharing protocol and Yao's Garbled Circuit protocol.

Build
=====
Build the project using Maven.
~~~
$ mvn clean compile assembly:single
~~~
It will build the project to a single JAR file containing all dependencies.
The file name can be edited and renamed as required.

Usage
=====
The console application requires the parameters:
- Own server port
- Secret key of the hashing algorithm
- CSV file containing the records to be encoded
- The other hosts, specified by host name and port

In the example the JAR file name of the application is renamed to the shorter file name *hwkhth.jar*.
~~~
$ java -jar hwkhth.jar 8081 67556b58703273357638792f423f4528472b4b6250655368566d597133743677 ~/records.csv
192.168.1.2:8082
~~~

The HTTP server runs on port *8081*.
The secret key of the own hashing algorithm is *67556b58703273357638792f423f4528472b4b6250655368566d597133743677*.
The file containing the records is *~/records.csv*.
The other hosts are specified by host name or IP address and their server port. Host name and port are separated by a colon.

Host *192.168.1.2* has to type the counterpart to participate in the protocol.
~~~
$ java -jar hwkhth.jar 8082 4e635266556a586e3272357538782f413f4428472b4b6250655367566b597033 ~/records.csv
192.168.1.1:8081
~~~

Any number of hosts is supported. The protocol is a multi-party protocol. Another host can be set by appending the other host name and port like this.
~~~
$ java -jar hwkhth.jar 8081 67556b58703273357638792f423f4528472b4b6250655368566d597133743677 ~/records.csv
192.168.1.2:8082 192.168.1.3:8083
~~~

Further configuration
=====================
Configuration concerning the schema of the records file, the kind of hashing algorithm or the q-grams have to be set before the build.
It can be done by editing the file *src/main/java/localhost/hashing_without_knowing_how_to_hash/config/ModelMetaData.java*.
The most important parameters are:
- RECORD_SCHEMA_CONFIGURATION: determines the underlying schema of the CSV record file
- Q: determines the number q for the q-grams. Let e.g. Q=2, so bigrams will be hashed.
- WITH_PADDING: determines whether the q-grams are padded q-grams or unpadded q-grams.
- CHARACTERS: determines the alphabet of characters of which the q-grams consist
- ALGORITHM_TYPE: determines the actual kind of cryptographic hash function to be used
- OPTIMISATION_STRATEGY: determines which optimisation should be applied to improve the formulas for the garbled circuits

License
=======
The source code is published under a MIT license:

Copyright © 2022 Manuel Friedrich

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.