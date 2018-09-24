(ns pfchangsindex-server.system
(:require
  [com.stuartsierra.component :as component]
  [pfchangsindex-server.schema :as schema]
  [pfchangsindex-server.server :as server]))

(defn new-system
  []
  (merge (component/system-map)
         (server/new-server)
         (schema/new-schema-provider)))
