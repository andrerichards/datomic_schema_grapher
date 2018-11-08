(ns datomic-schema-grapher.core
  (:require [datomic.api :as d]
            [datomic-schema-grapher.database :refer (schema references)]
            [datomic-schema-grapher.dot :refer (show to-dot)])
  (:import javax.swing.JFrame))

(defn graph-datomic
  "Render a nice graphviz of your Datomic schema"
  ([uri & options]
   (let [db (d/db (d/connect uri)) ]
     (graph-datomic db options)))
  ([db & {:keys [save-as no-display exit-on-close]}]
   (let [schema (schema db)
         references (references db)]
     (if save-as
       (spit save-as (to-dot schema references)))
     (when-not no-display
       (let [jframe (show schema references)]
         (when exit-on-close
           ;; mainly for use with the lein plugin
           (.setDefaultCloseOperation jframe JFrame/EXIT_ON_CLOSE)
           (while true (Thread/sleep 500))))))))
