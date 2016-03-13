(ns chip8-clj.core
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.instructions :as instructions]
            [chip8-clj.machine-state :as machine-state]
            [chip8-clj.utils :as utils]
            [clojure.tools.logging :as log]))

(defn- fetch-opcode
  [machine-state]
  (let [pc (:pc machine-state)
        memory (:memory machine-state)
        byte0 (get memory pc)
        byte1 (get memory (+ pc 1))]
    (bit-or (bit-shift-left byte0 8) byte1))) 

(defn- decode-and-execute 
  [machine-state opcode]
  (log/debug "decode-and-execute:" opcode)
  (let [nibble0 (utils/get-nibble0 opcode)]
    (cond 
      (= nibble0 0x0) 0x0
      (= nibble0 0x1) 0x1
      (= nibble0 0x2) 0x2
      (= nibble0 0x3) 0x3
      (= nibble0 0x4) 0x4
      (= nibble0 0x5) 0x5
      (= nibble0 0x6)
        (instructions/execute-6XNN machine-state opcode)
      (= nibble0 0x7) 
        (instructions/execute-7XNN machine-state opcode)
      (= nibble0 0x8) 0x8
      (= nibble0 0x9) 0x9
      (= nibble0 0xa) 0xa
      (= nibble0 0xb) 0xb
      (= nibble0 0xc) 0xc
      (= nibble0 0xd) 0xd
      (= nibble0 0xe) 0xe
      (= nibble0 0xf) 0xf)))

(defn step
  [machine-state]
  (->> (fetch-opcode machine-state)
       (decode-and-execute machine-state)))

(defn start
  [machine-state]
  (log/debug "Starting core")
 
  ; TODO 
  ; Start execution loop here
  ; (Loop until the program exits, or is closed externally)
  (loop [iteration 0]
    (log/debug iteration)
    (step machine-state)
    (if (= iteration 10)
      0
      (recur (inc iteration)))))

;(graphics/handle-graphics machine-state))

;(defn test-func
;  []
;  (let [machine-state (chip8-clj.machine-state/initialise 
;                        "/home/kris/ProgrammingHome/chip8-clj/dev-resources/Games/TETRIS")]
;      (start machine-state)))


