(ns chip8-clj.timer
  (:require [clojure.tools.logging :as log]))

(defn play-sound
  []
  (case (System/getProperty "os.name") 
    "Linux"    (. (Runtime/getRuntime) exec "paplay resources/sounds/Click1.wav")
    "Mac OS X" (. (Runtime/getRuntime) exec "afplay resources/sounds/Click1.wav")
    0))

(defn- tick
  [timer timer-type]
  (if (> @timer 0)
    (do
      (swap! timer dec)
      (if (= timer-type "sound")
        (play-sound)
        0))
    0))  

(defn start
  [timer timer-type]
  (log/debug "Starting timer" timer-type)
  (try
    (while true 
      (do 
        (Thread/sleep 16)
        (tick timer timer-type)))

    (catch Exception e
      (log/debug "Exception detected in timer thread:" e))))
