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

(defn unknown-opcode-error
  [opcode]
  (log/error "Unknown opcode:" opcode)
  (System/exit 1))

(defn- decode-and-execute 
  [machine-state opcode]
  (log/debug "decode-and-execute:" opcode)
  (let [byte0   (utils/get-byte0 opcode)
        byte1   (utils/get-byte1 opcode) 
        nibble0 (utils/get-nibble0 opcode)
        nibble1 (utils/get-nibble1 opcode)
        nibble2 (utils/get-nibble2 opcode)
        nibble3 (utils/get-nibble3 opcode)]
    (cond 
      (= nibble0 0x0)
        (cond 
          (and (= nibble1 0x0) (= byte1 0xE0))
            (instructions/execute-00E0 machine-state opcode)
          (and (= nibble1 0x0) (= byte1 0xEE))
            (instructions/execute-00EE machine-state opcode)
          :else  
            (instructions/execute-0NNN machine-state opcode))
      (= nibble0 0x1) 
        (instructions/execute-1NNN machine-state opcode)
      (= nibble0 0x2)
        (instructions/execute-2NNN machine-state opcode)
      (= nibble0 0x3)
        (instructions/execute-3XNN machine-state opcode)
      (= nibble0 0x4)
        (instructions/execute-4XNN machine-state opcode)
      (= nibble0 0x5)
        (instructions/execute-5XY0 machine-state opcode)
      (= nibble0 0x6)
        (instructions/execute-6XNN machine-state opcode)
      (= nibble0 0x7) 
        (instructions/execute-7XNN machine-state opcode)
      (= nibble0 0x8)
        (cond 
          (= nibble3 0x0)
            (instructions/execute-8XY0 machine-state opcode)
          (= nibble3 0x1)
            (instructions/execute-8XY1 machine-state opcode)
          (= nibble3 0x2)
            (instructions/execute-8XY2 machine-state opcode)
          (= nibble3 0x3)
            (instructions/execute-8XY3 machine-state opcode)
          (= nibble3 0x4)
            (instructions/execute-8XY4 machine-state opcode)
          (= nibble3 0x5)
            (instructions/execute-8XY5 machine-state opcode)
          (= nibble3 0x6)
            (instructions/execute-8XY6 machine-state opcode)
          (= nibble3 0x7)
            (instructions/execute-8XY7 machine-state opcode)
          (= nibble3 0xE)
            (instructions/execute-8XYE machine-state opcode)
          :else
            (unknown-opcode-error))
      (= nibble0 0x9) 0x9
        ; TODO
      (= nibble0 0xa) 0xa
        ; TODO
      (= nibble0 0xb) 0xb
        ; TODO
      (= nibble0 0xc) 0xc
        ; TODO
      (= nibble0 0xd) 0xd
        ; TODO
      (= nibble0 0xe) 0xe
        ; TODO
      (= nibble0 0xf) 0xf)))
        ; TODO

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


