# mfs-rest-server

# package
## LEININING
Leiningen is for automating tool for test, compiling, packaging.
Install Leiningen

- https://github.com/technomancy/leiningen
- 

## Dependencies
- REST Service : metosin/compojure-api "1.1.11"
- MapR FS : org.apache.hadoop/hadoop-common "2.7.0-mapr-1808"
- Integration lanugage : org.clojure/clojure "1.8.0"


## Compile and Package

`lein ring uberjar` 

Then, You can find two jar files in _/target_ directory.


# usage
Upload the final jars _mfs-rest-server.jar_ on the MapR Cluster.

`java -jar mfs-rest-server.jar <port: default 3000>`


# refer
 - https://hadoop.apache.org/docs/r2.7.1/api/org/apache/hadoop/fs/FileSystem.html#setOwner(org.apache.hadoop.fs.Path,%20java.lang.String,%20java.lang.String)
