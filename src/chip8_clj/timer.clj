(ns chip8-clj.timer
  (:require [clojure.tools.logging :as log]))

(defn- tick
  [timer timer-type]
  (log/debug "timer tick" timer-type @timer)
  (if (> @timer 0)
    (swap! timer dec)
    0))  

(defn start
  [timer timer-type]
  (log/debug "Starting timer" timer-type)
  (while true 
    (do 
      (Thread/sleep 16) 
      (tick timer timer-type))))
