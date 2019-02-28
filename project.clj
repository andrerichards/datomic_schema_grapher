(defproject defng/datomic-schema-grapher "0.2.1"
  :description "Adds ability to use datomic-schema-grapher with Datomic Cloud."
  :url "https://github.com/andrerichards/datomic_schema_grapher"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[dorothy "0.0.5"]
                 [hiccup "1.0.5"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.5.1"]
                                  ;; user must provide their own datomic
                                  ;; for on-prem: [com.datomic/client-pro "0.8.20"]
                                  ;; for cloud: [com.datomic/client-cloud "0.8.63"]
                                  ;; for mem-db: [datomic-client-memdb "0.2.0"]
                                  ]}}
  :jvm-opts ["-Xmx1g"]
  :eval-in-leiningen true)
