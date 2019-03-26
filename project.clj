 (defproject mfs-rest-server "0.1.0-SNAPSHOT"
   :description "FIXME: write description"
   :dependencies [[org.clojure/clojure "1.8.0"]
                  [clj-http "3.9.1"]
                  [metosin/compojure-api "1.1.11"]
                  [org.apache.hadoop/hadoop-common "2.7.0-mapr-1808"]]
                  ;;[org.apache.hadoop/hadoop-common "2.7.0-mapr-1607"]]
   :ring {:handler rest-server.handler/app}
   :uberjar-name "mfs-rest-server.jar"
   ;;:repositories {"local" ~(str (.toURI (java.io.File. "/Users/incjjung/.m2/repository/")))}
   :repositories {"mapr-releases" "http://repository.mapr.com/maven/"}
   :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                   :plugins [[lein-ring "0.12.5"]]}})
