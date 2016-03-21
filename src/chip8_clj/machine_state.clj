(ns chip8-clj.machine-state
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.utils :as utils]))

(def memory-size 0x1000)
(def program-load-addr 0x200)
(def num-reigsters 16)
(def stack-size 16)

(defn- initialise-screen
  [machine-state]
  (assoc machine-state :screen (graphics/create-screen)))

(defn- initialise-timers
  [machine-state]
  (assoc machine-state :delay-timer 0)
  (assoc machine-state :sound-timer 0))

(defn- initialise-registers
  [machine-state]
  (assoc machine-state :registers (byte-array num-reigsters)))

(defn- initialise-memory
  [machine-state]
  (assoc machine-state :memory (byte-array memory-size)))

(defn- initialise-stack
  [machine-state]
  (assoc machine-state :stack (int-array stack-size)))

(defn- initialise-font-data
  [machine-state]
  ; TODO
  (assoc machine-state :font-data nil))

(defn- initialise-pc
  [machine-state]
  (assoc machine-state :pc program-load-addr))

(defn- read-rom-into-byte-array
  [rom-file]
  (with-open [out (java.io.ByteArrayOutputStream.)]
    (clojure.java.io/copy (clojure.java.io/input-stream rom-file) out)
    (.toByteArray out)))

(defn- load-rom-into-memory
  [machine-state rom-file]
  (let [memory (:memory machine-state)
        rom-bytes (read-rom-into-byte-array rom-file)]
    (doall (map-indexed 
             (fn [index rom-byte]
               (aset-byte memory (+ program-load-addr index) rom-byte))
             (vec rom-bytes)))
    machine-state))

(defn get-pc
  [machine-state]
  (:pc machine-state))

(defn set-pc
  [machine-state pc]
  (assoc machine-state :pc pc))

(defn increment-pc
  [machine-state]
  (assoc machine-state :pc (+ (:pc machine-state) 2)))

(defn skip-next-pc
  [machine-state]
  (assoc machine-state :pc (+ (:pc machine-state) 4)))

(defn get-memory
  [machine-state addr]
  (get (:memory machine-state) addr))

(defn set-memory
  [machine-state addr value]
  (aset-byte (:memory machine-state) addr value)
  machine-state)

(defn set-instr
  [machine-state addr value]
  (-> machine-state
    (set-memory addr       (utils/get-byte0 value))
    (set-memory (+ addr 1) (utils/get-byte1 value))))

(defn get-register 
  [machine-state reg]
  (get (:registers machine-state) reg))

(defn set-register
  [machine-state reg value]
  (aset-byte (:registers machine-state) reg value)
  machine-state)

(defn initialise
  ([rom-file]
   (-> (initialise)
       (load-rom-into-memory rom-file)))
  ([]
   (-> {}
       (initialise-screen)
       (initialise-timers)
       (initialise-registers)
       (initialise-memory)
       (initialise-stack)
       (initialise-font-data)
       (initialise-pc))))
