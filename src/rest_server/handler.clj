(ns rest-server.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [clojure.java.shell :as sh]
            [clj-http.client :as client]
            [schema.core :as s])
  (:import  [org.apache.hadoop.conf Configuration]
            [org.apache.hadoop.fs.permission FsPermission]
            [org.apache.hadoop.fs FileSystem FileUtil FSDataInputStream FSDataOutputStream Path]))


(s/defschema ReturnOfSh
  {:exit s/Int
   :out s/Str
   :err s/Str})

;;(def glb (slurp "./project.clj"))


(defn ^FileSystem hdfsfilesystem
  "Returns the Hadoop filesystem from `path`."
  [path & [config]]
  (FileSystem/get (.toUri (Path. path)) (Configuration.)))

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "MFS-REST-API"
                    :description "MapR FileSystem REST API"}
             :tags [{:name "HDFS", :description "HDFS APIs copy/copyFromLocal/copyToLocal/mkdir/chmod/chown/delete"}
                    {:name "HTTPFS", :description "HTTPFS APIs"}
                    {:name "MAPRCLI", :description "MAPRCLI APIs"}]}}}

    (context "/httpfs" []
             :tags ["HTTPFS"]

             ;;HTTP GET SHELL COMMAND
             (GET "/hadoop-cmd" []
                  :return ReturnOfSh
                  :query-params [cmd :- String, opt :- String, path :- String]
                  :summary "In order to use 'hadoop fs <cmd> <option> <path>' in Shellb"
                  (ok (sh/sh "hadoop" "fs" cmd opt path)))

             ;;HTTP GET OPEN/GETFILESTATUS/LISTSTATUS/

             ;;"http://172.16.28.145:14000/webhdfs/v1/aaa?op=open&user.name=mapr"
             (GET "/GET" []
                  :return String
                  :query-params [FULLURL :- String, user :- String, password :- String]
                  :summary "For GET PROTOCOL"
                  (ok (:body (client/get FULLURL) {:basic-auth [user password]})))

             ;; (GET "/OPEN" []
             ;;      :return String
             ;;      :query-params [protocol :- String, host :- String, port :- String, filepath :- String, user :- String, password :- String]
             ;;      :summary "HTTPFS OPEN CALL"
             ;;      (print (format "%s://%s:%s/webhdfs/v1/%s?op=open&user.name=%s" protocol host port filepath user))
             ;;      (ok (:body (client/get (format "%s://%s:%s/webhdfs/v1/%s?op=open&user.name=%s" protocol host port filepath user) {:basic-auth [user password]}))))

             ;; To creat directory : http://172.16.28.145:14000/webhdfs/v1/user/mapr/DIR?op=MKDIRS&user.name=mapr

             (PUT "/PUT" []
                     :return String
                     :query-params [FULLURL :- String, user :- String, password :- String]
                     :summary "FOR PUT PROTOCOL"
                     (ok (:body (client/put FULLURL  {:basic-auth [user password]}))))

             ;;
             (DELETE "/DELETE" []
                     :return String
                     :query-params [FULLURL :- String, user :- String, password :- String]
                     :summary "FOR DELETE PROTOCOL"
                     (ok (:body (client/delete FULLURL  {:basic-auth [user password]})))))

    (context "/maprcli" []
             :tags ["MAPRCLI"]

             ;;baseurl='https://'+apiserver+':8443/rest/'
             (GET "/GET" []
                  :return String
                  :query-params [FULLURL :- String, user :- String, password :- String]
                  :summary "For GET Protocol (ex, http://172.16.28.145:14000/webhdfs/v1/aaa?op=open&user.name=mapr)"
                  (ok (:body (client/get FULLURL) {:basic-auth [user password]})))

             (GET "/sh" []
                  :return ReturnOfSh
                  :query-params [cmd :- String, opt :- String]
                  :summary "Shell script with only one option"
                  (ok (sh/sh cmd opt))))


    (context "/hdfs" []
             :tags ["HDFS"]

             (GET "/mkdir" []
                  :return String
                  :query-params [path :- String]
                  :summary "hadoop fs -mkdir <path>"
                  (ok (str (try
                    (.mkdirs (hdfsfilesystem "/") (Path. path)) 
                    (catch Exception e (str "caught exception: " e (.getMessage e)))))))

             (GET "/delete" []
                  :return String
                  :query-params [path :- String]
                  :summary "hadoop fs -rm -r <path>"
                  (ok (str (try
                             (.delete (hdfsfilesystem "/") (Path. path) true)
                             (catch Exception e (str "caught exception: " e (.getMessage e)))))))

             (GET "/copy" []
                  :return String
                  :query-params [src :- String, dst :- String]
                  :summary "hadoop fs -cp <src> <dst>"

                  (ok (str (try
                             (FileUtil/copy (hdfsfilesystem "/") (Path. src) (hdfsfilesystem "/") (Path. dst) false (Configuration.))
                             (catch Exception e (str "caught exception: " e (.getMessage e)))))))


             (GET "/move" []
                  :return String
                  :query-params [src :- String, dst :- String]
                  :summary "hadoop fs -mv <src> <dst>"
                  (ok (str (try
                             (FileUtil/copy (hdfsfilesystem "/") (Path. src) (hdfsfilesystem "/") (Path. dst) true (Configuration.))
                             (catch Exception e (str "caught exception: " e (.getMessage e)))))))

             (GET "/copyFromLocalFile" []
                  :return String
                  :query-params [src :- String, dst :- String]
                  :summary "hadoop fs -copyFromLocalFile <src_of_local> <dst_of_cluster>"
                  (ok (str (try
                             (nil? (.copyFromLocalFile (hdfsfilesystem "/") (Path. src) (Path. dst)))
                             (catch Exception e (str "caught exception: " e (.getMessage e)))))))


             (GET "/copyToLocalFile" []
                  :return String
                  :query-params [src :- String, dst :- String]
                  :summary "hadoop fs -copytoLocalFile <src_of_cluster> <dst_of_local>"
                  (ok (str (try
                             (nil? (.copyToLocalFile (hdfsfilesystem "/") (Path. src) (Path. dst)))
                             (catch Exception e (str "caught exception: " e (.getMessage e)))))))


             (GET "/chmod" []
                  :return String
                  :query-params [path :- String, mode :- String]
                  :summary "hadoop fs -chmod <mode> <path>"
                  (ok (str (try
                             (nil? (.setPermission (hdfsfilesystem "/") (Path. path) (FsPermission. mode)))
                             (catch Exception e (str "caught exception: " e (.getMessage e)))))))

             (GET "/chown" []
                  :return String
                  :query-params [path :- String, uid :- String, gid :- String]
                  :summary "hadoop fs -chown uid:gid <path>"
                  (ok (str (try
                             (nil? (.setOwner (hdfsfilesystem "/") (Path. path) uid gid))
                             (catch Exception e (str "caught exception: " e (.getMessage e)))))))

             (GET "/list" []
                  :return [String]
                  :query-params [path :- String]
                  :summary "hadoop fs -l <path>"
                  (ok
                   (try
                     (into [] (map #(str (.getPath %) " " (.getPermission %)) (.listStatus (hdfsfilesystem "/") (Path. path))))
                     (catch Exception e [(str "caught exception: " e (.getMessage e))]))))

             (GET "/echo" []
                   :return String
                   :query-params [msg :- String]
                   :summary "echoe your"
                   (ok msg)) )))



