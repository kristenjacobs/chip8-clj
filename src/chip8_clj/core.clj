(ns chip8-clj.core
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.instructions :as instructions]
            [chip8-clj.machine-state :as machine-state]
            [chip8-clj.utils :as utils]
            [clojure.tools.logging :as log]))

(defn- fetch-opcode
  [machine-state]
  (log/debug (format "fetch-opcode, pc: 0x%x" (:pc machine-state)))
  (let [pc (:pc machine-state)
        memory (:memory machine-state)
        byte0 (bit-and (get memory pc) 0xFF)
        byte1 (bit-and (get memory (+ pc 1)) 0xFF)]
    (bit-and (bit-or (bit-shift-left byte0 8) (bit-and byte1 0xFF)) 0xFFFF))) 

(defn unknown-opcode-error
  [opcode]
  (log/error (format "Unknown opcode: 0x%x" opcode))
  (System/exit 1))

(defn- decode-and-execute 
  [machine-state opcode]
  (let [byte0   (utils/get-byte0 opcode)
        byte1   (utils/get-byte1 opcode) 
        nibble0 (utils/get-nibble0 opcode)
        nibble1 (utils/get-nibble1 opcode)
        nibble2 (utils/get-nibble2 opcode)
        nibble3 (utils/get-nibble3 opcode)]
    (log/debug (format "decode-and-execute: opcode: 0x%04x, byte0: 0x%02x, byte1: 0x%02x" 
                       opcode byte0 byte1))
    (case nibble0 
      0x0 (case nibble1
            0x0 (case byte1
                  0xE0 (instructions/execute-00E0 machine-state opcode)
                  0xEE (instructions/execute-00EE machine-state opcode)
                  (unknown-opcode-error opcode))
            (instructions/execute-0NNN machine-state opcode))
      0x1 (instructions/execute-1NNN machine-state opcode)
      0x2 (instructions/execute-2NNN machine-state opcode)
      0x3 (instructions/execute-3XNN machine-state opcode)
      0x4 (instructions/execute-4XNN machine-state opcode)
      0x5 (case nibble3 
            0x0 (instructions/execute-5XY0 machine-state opcode)
            (unknown-opcode-error opcode))
      0x6 (instructions/execute-6XNN machine-state opcode)
      0x7 (instructions/execute-7XNN machine-state opcode)
      0x8 (case nibble3 
            0x0 (instructions/execute-8XY0 machine-state opcode)
            0x1 (instructions/execute-8XY1 machine-state opcode)
            0x2 (instructions/execute-8XY2 machine-state opcode)
            0x3 (instructions/execute-8XY3 machine-state opcode)
            0x4 (instructions/execute-8XY4 machine-state opcode)
            0x5 (instructions/execute-8XY5 machine-state opcode)
            0x6 (instructions/execute-8XY6 machine-state opcode)
            0x7 (instructions/execute-8XY7 machine-state opcode)
            0xE (instructions/execute-8XYE machine-state opcode)
            (unknown-opcode-error opcode))
      0x9 (case nibble3 
            0x0 (instructions/execute-9XY0 machine-state opcode)
            (unknown-opcode-error opcode))
      0xa (instructions/execute-ANNN machine-state opcode)
      0xb (instructions/execute-BNNN machine-state opcode)
      0xc (instructions/execute-CXNN machine-state opcode)
      0xd (instructions/execute-DXYN machine-state opcode)
      0xe (case byte1 
            0x9E (instructions/execute-EX9E machine-state opcode)
            0xA1 (instructions/execute-EXA1 machine-state opcode)
            (unknown-opcode-error opcode))
      0xf (case byte1 
            0x07 (instructions/execute-FX07 machine-state opcode)
            0x0A (instructions/execute-FX0A machine-state opcode)
            0x15 (instructions/execute-FX15 machine-state opcode)
            0x18 (instructions/execute-FX18 machine-state opcode)
            0x1E (instructions/execute-FX1E machine-state opcode)
            0x29 (instructions/execute-FX29 machine-state opcode)
            0x33 (instructions/execute-FX33 machine-state opcode)
            0x55 (instructions/execute-FX55 machine-state opcode)
            0x65 (instructions/execute-FX65 machine-state opcode)
            (unknown-opcode-error opcode))
      (unknown-opcode-error opcode))))

(defn step
  [machine-state]
  (Thread/sleep 2)
  (->> (fetch-opcode machine-state)
       (decode-and-execute machine-state)))

(defn start
  [rom-file]
  (log/debug "Starting core with rom" rom-file)
  (let [machine-state (machine-state/initialise rom-file)]
    (try
      (loop [ms machine-state]
        (recur (step ms)))
      (catch Exception e
        (log/debug "Exception detected in core thread:" e)))))

