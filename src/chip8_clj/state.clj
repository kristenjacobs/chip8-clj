(ns chip8-clj.state
  (:require [clojure.tools.logging :as log]))

(def delay-timer (atom 0))
(def sound-timer (atom 0))
(def keys-pressed (atom #{}))
(def screen-updates (atom []))

(defn set-delay-timer
  [value]
  (reset! delay-timer value)) 

(defn get-delay-timer
  []
  @delay-timer)

(defn set-sound-timer
  [value]
  (reset! sound-timer value))

(defn get-sound-timer
  []
  @sound-timer)

(defn is-key-pressed
  [key-val]
  (log/debug "keys-pressed" @keys-pressed "key-val" key-val)
  (contains? @keys-pressed key-val))

(defn get-key-pressed
  []
  (log/debug "keys-pressed" @keys-pressed)
  (first @keys-pressed))

(defn add-screen-update
  "Atomically adds the passed screen update map to the current list
  of pending screen updates."
  [screen-update]
  (swap! screen-updates conj screen-update))

(defn fetch-screen-updates 
  "Atomically returns, then clears, the latest set of screen updates"
  []
  (loop []
    (let [old @screen-updates]
      (if (compare-and-set! screen-updates old [])
        old
        (recur)))))

