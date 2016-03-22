(ns chip8-clj.core
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.instructions :as instructions]
            [chip8-clj.machine-state :as machine-state]
            [chip8-clj.utils :as utils]
            [clojure.tools.logging :as log]))

(defn- fetch-opcode
  [machine-state]
  ;(log/debug (format "fetch-opcode, pc: 0x%x" (:pc machine-state)))
  (let [pc (:pc machine-state)
        memory (:memory machine-state)
        byte0 (get memory pc)
        byte1 (get memory (+ pc 1))]
    ;(log/debug (format "fetch-opcode byte0: 0x%02x" byte0))
    ;(log/debug (format "fetch-opcode byte1: 0x%02x" byte1))
    (bit-or (bit-shift-left byte0 8) (bit-and byte1 0xff)))) 

(defn unknown-opcode-error
  [opcode]
  (log/error (format "Unknown opcode: 0x%x" opcode))
  (System/exit 1))

(defn- decode-and-execute 
  [machine-state opcode]
  ;(log/debug (format "decode-and-execute: 0x%04x" opcode))
  (let [byte0   (utils/get-byte0 opcode)
        byte1   (utils/get-byte1 opcode) 
        nibble0 (utils/get-nibble0 opcode)
        nibble1 (utils/get-nibble1 opcode)
        nibble2 (utils/get-nibble2 opcode)
        nibble3 (utils/get-nibble3 opcode)]
    ;(log/debug (format "decode-and-execute: byte0: 0x%02x" byte0))
    ;(log/debug (format "decode-and-execute: byte1: 0x%02x" byte1))
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
        (cond
          (= nibble3 0x0)
            (instructions/execute-5XY0 machine-state opcode)
          :else
            (unknown-opcode-error opcode))
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
            (unknown-opcode-error opcode))
      (= nibble0 0x9)
        (cond 
          (= nibble3 0x0)
            (instructions/execute-9XY0 machine-state opcode)
          :else  
            (unknown-opcode-error opcode))
      (= nibble0 0xa)
        (instructions/execute-ANNN machine-state opcode)
      (= nibble0 0xb)
        (instructions/execute-BNNN machine-state opcode)
      (= nibble0 0xc)
        (instructions/execute-CXNN machine-state opcode)
      (= nibble0 0xd)
        (instructions/execute-DXYN machine-state opcode)
      (= nibble0 0xe)
        (cond 
          (= byte1 0x9E)
            (instructions/execute-EX9E machine-state opcode)
          (= byte1 0xA1)
            (instructions/execute-EXA1 machine-state opcode)
          :else  
            (unknown-opcode-error opcode))
      (= nibble0 0xf)
        (cond 
          (= byte1 0x07)
            (instructions/execute-FX07 machine-state opcode)
          (= byte1 0x0A)
            (instructions/execute-FX0A machine-state opcode)
          (= byte1 0x15)
            (instructions/execute-FX15 machine-state opcode)
          (= byte1 0x18)
            (instructions/execute-FX18 machine-state opcode)
          (= byte1 0x1E)
            (instructions/execute-FX1E machine-state opcode)
          (= byte1 0x29)
            (instructions/execute-FX29 machine-state opcode)
          (= byte1 0x33)
            (instructions/execute-FX33 machine-state opcode)
          (= byte1 0x55)
            (instructions/execute-FX55 machine-state opcode)
          (= byte1 0x65)
            (instructions/execute-FX65 machine-state opcode)
          :else  
            (unknown-opcode-error opcode))
      :else  
        (unknown-opcode-error opcode))))

(defn step
  [machine-state]
  (->> (fetch-opcode machine-state)
       (decode-and-execute machine-state)))

(defn start
  [machine-state]
  (log/debug "Starting core")
 
  ; TODO: Just testing the graphics here.
  (graphics/handle-graphics machine-state)

  ; TODO 
  ; Start execution loop here
  ; (Loop until the program exits, or is closed externally)
  (loop [ms machine-state 
         iteration 0]
    ;(log/debug iteration)
    (if (= iteration 11)
      0
      (recur (step ms) (inc iteration)))))

(defn test-func
  []
  (let [machine-state (chip8-clj.machine-state/initialise 
                        "/home/kris/ProgrammingHome/chip8-clj-resources/Games/TETRIS")]
      (start machine-state)))

