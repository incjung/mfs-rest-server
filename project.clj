;; got message 'use insecure HTTP repository without TLS' 
;; never used like: 
;;(require 'cemerick.pomegranate.aether)
;;(cemerick.pomegranate.aether/register-wagon-factory!
;; "http" #(org.apache.maven.wagon.providers.http.HttpWagon.))
 
(defproject mfs-rest-server "0.1.0-SNAPSHOT"
   :description "FIXME: write description"
   :dependencies [[org.clojure/clojure "1.8.0"]
                  [clj-http "3.9.1"]
                  [metosin/compojure-api "1.1.11"]
                  [ring/ring-defaults "0.3.2"]
                  [org.apache.hadoop/hadoop-common "2.7.0-mapr-1808"]]
                  ;;[org.apache.hadoop/hadoop-common "2.7.0-mapr-1607"]]
   :ring {:handler rest-server.handler/app
          :port 8445
          :ssl? true
          :ssl-port 8446
          ;;:keystore "resources/ssl_keystore"
          :keystore "/opt/mapr/conf/ssl_keystore"
          :key-password "mapr123" }
   :uberjar-name "mfs-rest-server.jar"
   ;;:repositories {"local" ~(str (.toURI (java.io.File. "/Users/incjjung/.m2/repository/")))}
   :repositories {"mapr-releases" "http://repository.mapr.com/maven/"}
   :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                   :plugins [[lein-ring "0.12.5"]]}})
