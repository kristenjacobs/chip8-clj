(ns chip8-clj.instructions
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.utils :as utils]
            [chip8-clj.machine-state :as machine-state]
            [clojure.tools.logging :as log]))

(defn execute-0NNN
  [machine-state opcode]
  (log/debug "execute-0NNN: Error: Not yet implemented")
  (System/exit 1))

(defn execute-00E0
  [machine-state opcode]
  (log/debug "execute-00E0: Error: Not yet implemented")
  (System/exit 1))

(defn execute-00EE
  [machine-state opcode]
  (log/debug (format "0x%04x - 00EE" (:pc machine-state)))
  (machine-state/return machine-state))

(defn execute-1NNN
  [machine-state opcode]
  (log/debug (format "0x%04x - 1NNN" (:pc machine-state)))
  (let [imm (utils/get-nnn opcode)]
    (machine-state/set-pc machine-state imm)))

(defn execute-2NNN
  [machine-state opcode]
  (log/debug (format "0x%04x - 2NNN" (:pc machine-state)))
  (machine-state/call machine-state (utils/get-nnn opcode)))

(defn execute-3XNN
  [machine-state opcode]
  (log/debug (format "0x%04x - 3XNN" (:pc machine-state)))
  (let [reg (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)]
    (if (= (machine-state/get-register machine-state reg) imm)
        (machine-state/skip-next-pc machine-state)
        (machine-state/increment-pc machine-state))))

(defn execute-4XNN
  [machine-state opcode]
  (log/debug (format "0x%04x - 4XNN" (:pc machine-state)))
  (let [reg (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)]
    (if (not= (machine-state/get-register machine-state reg) imm)
        (machine-state/skip-next-pc machine-state)
        (machine-state/increment-pc machine-state))))

(defn execute-5XY0
  [machine-state opcode]
  (log/debug (format "0x%04x - 5XY0" (:pc machine-state)))
  (let [reg-x (utils/get-nibble1 opcode)
        reg-y (utils/get-nibble2 opcode)]
    (if (= (machine-state/get-register machine-state reg-x) 
           (machine-state/get-register machine-state reg-y))
        (machine-state/skip-next-pc machine-state)
        (machine-state/increment-pc machine-state))))

(defn execute-6XNN
  [machine-state opcode]
  (log/debug (format "0x%04x - 6XNN" (:pc machine-state)))
  (let [reg (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)]
    (-> machine-state
        (machine-state/set-register reg imm)
        (machine-state/increment-pc))))

(defn execute-7XNN
  [machine-state opcode]
  (log/debug (format "0x%04x - 7XNN" (:pc machine-state)))
  (let [reg (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)
        oper (machine-state/get-register machine-state reg)]
    (-> machine-state
        (machine-state/set-register reg (+ oper imm))
        (machine-state/increment-pc))))

(defn execute-8XY0
  [machine-state opcode]
  (log/debug "execute-8XY0: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XY1
  [machine-state opcode]
  (log/debug "execute-8XY1: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XY2
  [machine-state opcode]
  (log/debug "execute-8XY2: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XY3
  [machine-state opcode]
  (log/debug "execute-8XY3: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XY4
  [machine-state opcode]
  (log/debug "execute-8XY4: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XY5
  [machine-state opcode]
  (log/debug "execute-8XY5: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XY6
  [machine-state opcode]
  (log/debug "execute-8XY6: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XY7
  [machine-state opcode]
  (log/debug "execute-8XY7: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XYE
  [machine-state opcode]
  (log/debug "execute-8XYE: Error: Not yet implemented")
  (System/exit 1))

(defn execute-9XY0
  [machine-state opcode]
  (log/debug "execute-9XY0: Error: Not yet implemented")
  (System/exit 1))

(defn execute-ANNN
  [machine-state opcode]
  (log/debug (format "0x%04x - ANNN" (:pc machine-state)))
  (let [value (utils/get-nnn opcode)]
    (-> machine-state
        (machine-state/set-addr-reg value)
        (machine-state/increment-pc))))

(defn execute-BNNN
  [machine-state opcode]
  (log/debug "execute-BNNN: Error: Not yet implemented")
  (System/exit 1))

(defn execute-CXNN
  [machine-state opcode]
  (log/debug "execute-CXNN: Error: Not yet implemented")
  (System/exit 1))

(defn execute-DXYN
  [machine-state opcode]
  (log/debug "execute-DXYN: Error: Not yet implemented")
  (System/exit 1))

(defn execute-EX9E
  [machine-state opcode]
  (log/debug "execute-EX9E: Error: Not yet implemented")
  (System/exit 1))

(defn execute-EXA1
  [machine-state opcode]
  (log/debug "execute-EXA1: Error: Not yet implemented")
  (System/exit 1))

(defn execute-FX07
  [machine-state opcode]
  (log/debug "execute-FX07: Error: Not yet implemented")
  (System/exit 1))

(defn execute-FX0A
  [machine-state opcode]
  (log/debug "execute-FX0A: Error: Not yet implemented")
  (System/exit 1))

(defn execute-FX15
  [machine-state opcode]
  (log/debug "execute-FX15: Error: Not yet implemented")
  (System/exit 1))

(defn execute-FX18
  [machine-state opcode]
  (log/debug "execute-FX18: Error: Not yet implemented")
  (System/exit 1))

(defn execute-FX1E
  [machine-state opcode]
  (log/debug "execute-FX1E: Error: Not yet implemented")
  (System/exit 1))

(defn execute-FX29
  [machine-state opcode]
  (log/debug "execute-FX29: Error: Not yet implemented")
  (System/exit 1))

(defn execute-FX33
  [machine-state opcode]
  (log/debug "execute-FX33: Error: Not yet implemented")
  (System/exit 1))

(defn execute-FX55
  [machine-state opcode]
  (log/debug "execute-FX55: Error: Not yet implemented")
  (System/exit 1))

(defn execute-FX65
  [machine-state opcode]
  (log/debug "execute-FX65: Error: Not yet implemented")
  (System/exit 1))
