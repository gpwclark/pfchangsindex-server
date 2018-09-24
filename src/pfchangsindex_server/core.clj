(ns pfchangsindex-server.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(q "{ game_by_id(id: \"1236\") { id name summary }}")
