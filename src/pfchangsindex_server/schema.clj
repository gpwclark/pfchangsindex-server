(ns pfchangsindex-server.schema
  "Contains custom resolvers and a function to provide the full schema."
  (:require
    [clojure.java.io :as io]
    [com.walmartlabs.lacinia.util :as util]
    [com.walmartlabs.lacinia.schema :as schema]
    [com.stuartsierra.component :as component]
    [clojure.edn :as edn]))

(defn resolve-pfchang-by-id
  [pfchangs-map context args value]
  (let [{:keys [id]} args]
    (get pfchangs-map id)))

(defn resolve-list-pfchangs
  [pfchangs-map context args value]
  (vals pfchangs-map))

(defn resolve-board-pfchang-designers
  [designers-map context args board-pfchang]
  (->> board-pfchang
       :designers
       (map designers-map)))

(defn resolve-designer-pfchangs
  [pfchangs-map context args designer]
  (let [{:keys [id]} designer]
    (->> pfchangs-map
         vals
         (filter #(-> % :designers (contains? id))))))

(defn entity-map
  [data k]
  (reduce #(assoc %1 (:id %2) %2)
          {}
          (get data k)))

(defn resolver-map
  [component]
  (let [cgg-data (-> (io/resource "cgg-data.edn")
                     slurp
                     edn/read-string)
        pfchangs-map (entity-map cgg-data :pfchangs)
        designers-map (entity-map cgg-data :designers)]
    {:query/pfchang-by-id (partial resolve-pfchang-by-id pfchangs-map)
     :query/list-pfchangs (partial resolve-list-pfchangs pfchangs-map)
     :PfChangs/designers (partial resolve-board-pfchang-designers designers-map)
     :Designer/pfchangs (partial resolve-designer-pfchangs pfchangs-map)}))

(defn load-schema
  [component]
  (-> (io/resource "cgg-schema.edn")
      slurp
      edn/read-string
      (util/attach-resolvers (resolver-map component))
      schema/compile))

(defrecord SchemaProvider [schema]

  component/Lifecycle

  (start [this]
    (assoc this :schema (load-schema this)))

  (stop [this]
    (assoc this :schema nil)))

(defn new-schema-provider
  []
  {:schema-provider (map->SchemaProvider {})})
