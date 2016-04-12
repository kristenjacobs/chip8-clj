(ns chip8-clj.machine-state
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.utils :as utils]
            [clojure.tools.logging :as log]))

(def memory-size 0x1000)
(def program-load-addr 0x200)
(def num-reigsters 16)
(def stack-size 16)

(defn ->byte
  [value]
  (bit-and value 0xFF))

(defn- initialise-screen
  [machine-state]
  (assoc machine-state :screen (graphics/create-screen)))

(defn- initialise-screen-buffer
  [machine-state]
  (assoc machine-state 
         :screen-buffer (graphics/create-screen-buffer)))

(defn- initialise-timers
  [machine-state]
  (assoc machine-state :delay-timer 0)
  (assoc machine-state :sound-timer 0))

(defn- initialise-registers
  [machine-state]
  (-> machine-state
    (assoc :registers (short-array num-reigsters))
    (assoc :addr-reg 0)))

(defn- initialise-memory
  [machine-state]
  (assoc machine-state :memory (short-array memory-size)))

(defn- initialise-stack
  [machine-state]
  (-> machine-state
    (assoc :stack (int-array stack-size))
    (assoc :stack-ptr 0)))

(defn get-memory
  [machine-state addr]
  (assert (< addr memory-size)) 
  (->byte (get (:memory machine-state) addr)))

(defn set-memory
  [machine-state addr value]
  (assert (< addr memory-size)) 
  (aset-short (:memory machine-state) addr (->byte value))
  machine-state)

(defn set-instr
  [machine-state addr value]
  (-> machine-state
    (set-memory addr       (utils/get-byte0 value))
    (set-memory (+ addr 1) (utils/get-byte1 value))))

(defn- initialise-font-data
  [machine-state]
  (let [font-data [{:char "0" :addr 0  :bytes [0xF0 0x90 0x90 0x90 0xF0]}
                   {:char "1" :addr 5  :bytes [0x20 0x60 0x20 0x20 0x70]}
                   {:char "2" :addr 10 :bytes [0xF0 0x10 0xF0 0x80 0xF0]}
                   {:char "3" :addr 15 :bytes [0xF0 0x10 0xF0 0x10 0xF0]}
                   {:char "4" :addr 20 :bytes [0x90 0x90 0xF0 0x10 0x10]}
                   {:char "5" :addr 25 :bytes [0xF0 0x80 0xF0 0x10 0xF0]}
                   {:char "6" :addr 30 :bytes [0xF0 0x80 0xF0 0x90 0xF0]}
                   {:char "7" :addr 35 :bytes [0xF0 0x10 0x20 0x40 0x40]}
                   {:char "8" :addr 40 :bytes [0xF0 0x90 0xF0 0x90 0xF0]}
                   {:char "9" :addr 45 :bytes [0xF0 0x90 0xF0 0x10 0xF0]}
                   {:char "A" :addr 50 :bytes [0xF0 0x90 0xF0 0x90 0x90]}
                   {:char "B" :addr 55 :bytes [0xE0 0x90 0xE0 0x90 0xE0]}
                   {:char "C" :addr 60 :bytes [0xF0 0x80 0x80 0x80 0xF0]}
                   {:char "D" :addr 65 :bytes [0xE0 0x90 0x90 0x90 0xE0]}
                   {:char "E" :addr 70 :bytes [0xF0 0x80 0xF0 0x80 0xF0]}
                   {:char "F" :addr 75 :bytes [0xF0 0x80 0xF0 0x80 0x80]}]
        m (:memory machine-state)]
    (reduce (fn [machine-state font]
              (reduce-kv (fn [machine-state byte-index byte-data]
                           (set-memory 
                             machine-state 
                             (+ (:addr font) byte-index) byte-data))
                         machine-state (:bytes font)))
            machine-state font-data)))

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
               (aset-short memory (+ program-load-addr index) rom-byte))
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

(defn get-register 
  [machine-state reg]
  (assert (< reg num-reigsters)) 
  (->byte (get (:registers machine-state) reg)))

(defn set-register
  [machine-state reg value]
  (assert (< reg num-reigsters)) 
  (aset-short (:registers machine-state) reg (->byte value))
  machine-state)

(defn set-carry-flag
  [machine-state]
  ;(log/debug (format "set-carry-flag")) 
  (aset-short (:registers machine-state) 0xf 1)
  machine-state)

(defn clear-carry-flag
  [machine-state]
  ;(log/debug (format "clear-carry-flag")) 
  (aset-short (:registers machine-state) 0xf 0)
  machine-state)

(defn get-addr-reg 
  [machine-state]
  (:addr-reg machine-state))

(defn set-addr-reg
  [machine-state value]
  (assoc machine-state :addr-reg value))

(defn get-screen-buffer
  [machine-state x y]
  (let [value (get (:screen-buffer machine-state) (graphics/get-index x y))]
    ;(log/debug (format "get-screen-buffer (%d, %d) %d" x y value)) 
    (assert (or (= value 0) (= value 1)))
    value))

(defn set-screen-buffer
  [machine-state x y value]
  ;(log/debug (format "set-screen-buffer (%d, %d) %d" x y value)) 
  (assert (or (= value 0) (= value 1)))
  (assoc-in machine-state [:screen-buffer (graphics/get-index x y)] value))

(defn set-delay-timer
  [machine-state value]
  (assoc machine-state :delay-timer value))

(defn get-delay-timer
  [machine-state]
  (:delay-timer machine-state))

(defn set-sound-timer
  [machine-state value]
  (assoc machine-state :sound-timer value))

(defn get-sound-timer
  [machine-state]
  (:sound-timer machine-state))

(defn initialise
  ([rom-file]
   (-> (initialise)
       (load-rom-into-memory rom-file)))
  ([]
   (-> {}
       (initialise-screen)
       (initialise-screen-buffer)
       (initialise-timers)
       (initialise-registers)
       (initialise-memory)
       (initialise-stack)
       (initialise-font-data)
       (initialise-pc))))
