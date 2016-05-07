(ns chip8-clj.main
  (:require [chip8-clj.core :as core]
            [chip8-clj.graphics :as graphics]
            [chip8-clj.machine-state :as machine-state]
            [chip8-clj.state :as state]
            [chip8-clj.timer :as timer])
  (:gen-class))

(defn -main 
  [& args]
  (if (not= (count args) 1)
    (do
      (println "Error: Usage: lein run <rom file>")
      (System/exit 1))
    (do
      (future (timer/start state/delay-timer "delay"))
      (future (timer/start state/sound-timer "sound"))
      (future (core/start (first args)))
      (graphics/start))))
