(ns chip8-clj.state
  (:require [clojure.tools.logging :as log]))

(def delay-timer (atom 0))
(def sound-timer (atom 0))
(def keys-pressed (atom #{}))

(defn is-key-pressed
  [key-val]
  (log/debug "keys-pressed" @keys-pressed "key-val" key-val)
  (contains? @keys-pressed key-val))

