# datomic-schema-grapher

```
This fork changes https://github.com/felixflores/datomic_schema_grapher to use datomic.client.api instead of datomic.api

This makes it work with Datomic Cloud.
```

Visually see the datomic schema. Uses graphviz.

![alt tag](https://raw.github.com/felixflores/datomic_schema_grapher/master/example/sample.png)

## Installation

Add to your `:dependencies`

```clojure
[defng/datomic-schema-grapher "0.2.2"]
```

If you want to use the lein plugin you must also add the dependency to your project's :plugins vector.

```bash
brew install graphviz
lein deps
```

## REPL Usage

### Datomic Cloud

```clojure
(require '[datomic.client.api :as d])
(require '[datomic-schema-grapher.core :refer (graph-datomic)])

(def db (-> (d/client {:server-type :ion
                       :region      "your-region"
                       :system      "your-system"
                       :endpoint    "your-endpoint"
                       :proxy-port  8182 #_your-proxy-port})
            (d/connect {:db-name "your-db-name"})
            (d/db)))
(graph-datomic db)
;; pops up a swing UI
```

### Local memdb

Also add to your `:dependencies`

```clojure
[datomic-client-memdb "0.2.0"]
```

Then

```clojure
(require '[datomic.client.api :as d])
(require '[compute.datomic-client-memdb.core])
(require '[datomic-schema-grapher.core :refer (graph-datomic)])

(def client (compute.datomic-client-memdb.core/client {}))

(d/create-database client {:db-name "your-db-name"}) ; create db
(def conn (d/connect client {:db-name "your-db-name"}))
(d/transact conn {:tx-data [#_"add your schema and data here"]})  
(def db (d/db conn))

(graph-datomic db)
;; pops up a swing UI
```

```clojure
(graph-datomic db :save-as "the-schema.dot")
;; writes graphviz to file and pops up swing UI
```

```clojure
(graph-datomic db :save-as "the-schema.dot" :no-display true)
;; does not pop up a display
```

## Plugin

In order to use it as a lein plugin you must list it as a dependency *and* as a plugin.

```clojure
; project.clj
(defproject your-project "x.x.x"
  :dependencies [[datomic-schema-grapher "0.0.1"]]
  :plugins [[datomic-schema-grapher "0.0.1"]])
```

then

```bash
lein graph-datomic <datomic:protocol://example>
```

Note that the lein plugin will not work for in memory databases.
Consider using the repl with in memory databases.

This project is still in the early stages of development.
The API is likely to change.

## License

Copyright © 2014 MIT

