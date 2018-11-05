(ns pfchangsindex-server.schema
  "Contains custom resolvers and a function to provide the full schema."
  (:require
    [clojure.java.io :as io]
    [com.walmartlabs.lacinia.util :as util]
    [com.walmartlabs.lacinia.schema :as schema]
    [com.stuartsierra.component :as component]
    [clojure.edn :as edn]))

(defn resolve-region-by-id
  [region-map context args value]
  (let [{:keys [id]} args]
    (get region-map id)))

(defn resolve-restaurant-by-id
  [restaurant-map context args value]
  (let [{:keys [id]} args]
    (get restaurant-map id)))

(defn resolve-list-regions
  [region-map context args value]
  (vals region-map))

(defn resolve-list-restaurants
  [restaurant-map context args value]
  (vals restaurant-map))

(defn resolve-region-restaurants
  [region-map context args restaurant]
  (->> restaurant
       :regions
       (map region-map)))

(defn resolve-restaurant-regions
  [restaurant-map context args region]
  (let [{:keys [id]} region]
    (->> restaurant-map
         vals
         (filter #(-> % :regions (contains? id))))))

(defn entity-map
  [data k]
  (reduce #(assoc %1 (:id %2) %2)
          {}
          (get data k)))

(defn resolver-map
  [component]
  (let [cgg-data (-> (io/resource "all-pfcs-region-data.edn");; "cgg-data.edn")
                     slurp
                     edn/read-string)
        restaurant-map (entity-map (first cgg-data) :restaurants)
        region-map (entity-map (first cgg-data) :regions)]
    {:query/region_by_id (partial resolve-region-by-id region-map)
     :query/restaurant_by_id (partial resolve-restaurant-by-id restaurant-map)
     :query/regions_list (partial resolve-list-regions region-map)
     :query/restaurants_list (partial resolve-list-restaurants restaurant-map)
     :Restaurant/regions (partial resolve-region-restaurants region-map)
     :Region/restaurants (partial resolve-restaurant-regions restaurant-map)}))

(comment (let [cgg-data (-> (io/resource "all-pfcs-region-data.edn");; "cgg-data.edn")
                    slurp
                    edn/read-string)]
   (entity-map (first cgg-data) :regions)))

(defn load-schema
  [component]
  (-> (io/resource "all-pfcs-region-schema.edn") ;;"cgg-schema.edn")
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
