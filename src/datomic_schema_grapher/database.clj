(ns datomic-schema-grapher.database
  (:require [datomic.client.api :as d]))

(defn datomic-attribute?
  [identifier]
  ((complement empty?) (re-matches #"(db|fressian).*" (namespace identifier))))

(defn schema
  "Returns all user defined datomic attribute as entities,
  grouped by their common namespace."
  [database]
  (->> (d/q '[:find ?attr ?name
              :where
              [_ :db.install/attribute ?attr]
              [?attr :db/ident ?name]]
            database)
       (remove #(datomic-attribute? (last %)))
       (map #(d/pull database '[:db/id
                                :db/ident
                                {:db/valueType [:db/ident]}
                                {:db/cardinality [:db/ident]}
                                {:db/unique [:db/ident]}
                                :schema/deprecated] (first %)))))

(defn ref-entities
  "Returns all entities the references a given datomic attribute."
  [attr-name database]
  (->>  (d/q '[:find ?ref-name
               :in $ ?attr-name
               :where
               [_ ?attr-name ?ref]
               [?ref ?ref-attr]
               [?ref-attr :db/ident ?ref-name]]
             database
             attr-name)
       (apply concat)
       (map namespace)
       (set)))

(defn references
  [database]
  (let [ref-attrs (->> (schema database)
                       (group-by #(-> % :db/valueType :db/ident))
                       :db.type/ref)]
    (->> (for [ref-attr ref-attrs]
           (interleave (repeat (:db/ident ref-attr))
                       (ref-entities (:db/ident ref-attr) database)
                       (repeat (name (-> ref-attr :db/cardinality :db/ident)))))
         flatten
         (partition 3))))
