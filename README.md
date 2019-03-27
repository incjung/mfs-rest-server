# mfs-rest-server


# usage
Compile/Package with _mfs-rest-server.jar_, and uploaed it on one of cluster nodes.
This service uses default 3000 port. You can change the port like : 


`$ java -jar mfs-rest-server.jar`


# package
## Dependencies
- REST Service : metosin/compojure-api "1.1.11"
- MapR FS : org.apache.hadoop/hadoop-common "2.7.0-mapr-1808"
- Integration lanugage : org.clojure/clojure "1.8.0"

## Build using Leiningen
Leiningen is for automating tool for test, compiling, packaging.
- https://github.com/technomancy/leiningen

`$ lein ring uberjar` 

Then, You can find two jar files in _/target_ directory.


# usage
## Start service
Upload the final jars _mfs-rest-server.jar_ on the MapR Cluster.

`$ java -jar mfs-rest-server.jar`

Default http port is 3000, if you want to change it, 

`$ PORT=8000 java -jar mfs-rest-server.jar`

![front page](../img/mfs-swaager.png "swagger page")
![mkdir](../img/swaager-mkdir.png "mkdir api")
![mkdir failed](../img/swagger_fail_mkdir.png "mkdir api failed")
![list up](../img/swagger_list.png "list api")
![mv](../img/swaager_mv.png "mv api")


## Access service
open `http:/<node>:8000/index.html`



# refer
 - https://hadoop.apache.org/docs/r2.7.1/api/org/apache/hadoop/fs/FileSystem.html#setOwner(org.apache.hadoop.fs.Path,%20java.lang.String,%20java.lang.String)
