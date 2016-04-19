(ns chip8-clj.state)

(def delay-timer (atom 0))
(def sound-timer (atom 0))
(def keys-pressed (atom #{}))

(defn is-key-pressed
  [key-val]
  (contains? @keys-pressed key-val))
