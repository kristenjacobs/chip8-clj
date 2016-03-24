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
  (let [machine-state (assoc machine-state :stack-ptr (dec (:stack-ptr machine-state)))
        return-addr (get (:stack machine-state) (:stack-ptr machine-state))]
    (log/debug (format "0x%04x 00EE ret 0x%04x" (:pc machine-state) return-addr))
    (assoc machine-state :pc return-addr)))

(defn execute-1NNN
  [machine-state opcode]
  (let [imm (utils/get-nnn opcode)]
    (log/debug (format "0x%04x 1NNN jmp 0x%04x" (:pc machine-state) imm))
    (machine-state/set-pc machine-state imm)))

(defn execute-2NNN
  [machine-state opcode]
  (let [target-addr (utils/get-nnn opcode)]
    (log/debug (format "0x%04x 2NNN cal 0x%04x" (:pc machine-state) target-addr))
    (aset-int (:stack machine-state) (:stack-ptr machine-state) (+ (:pc machine-state) 2))
    (-> machine-state
        (assoc :stack-ptr (inc (:stack-ptr machine-state)))
        (assoc :pc target-addr))))

(defn execute-3XNN
  [machine-state opcode]
  (let [reg-num (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)
        reg-val (machine-state/get-register machine-state reg-num)]
    (log/debug (format "0x%04x 3XNN skp V[%d](0x%02x) == 0x%02x" 
                       (:pc machine-state) reg-num reg-val imm))
    (if (= reg-val imm)
        (machine-state/skip-next-pc machine-state)
        (machine-state/increment-pc machine-state))))

(defn execute-4XNN
  [machine-state opcode]
  (let [reg-num (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)
        reg-val (machine-state/get-register machine-state reg-num)]
    (log/debug (format "0x%04x 4XNN skp V[%d](0x%02x) != 0x%02x" 
                       (:pc machine-state) reg-num reg-val imm))
    (if (not= reg-val imm)
        (machine-state/skip-next-pc machine-state)
        (machine-state/increment-pc machine-state))))

(defn execute-5XY0
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-y-num (utils/get-nibble2 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num) 
        reg-y-val (machine-state/get-register machine-state reg-y-num)]
    (log/debug (format "0x%04x 5XY0 skp V[%d](0x%02x) == V[%d](0x%02x)" 
                       (:pc machine-state) reg-x-num reg-x-val reg-y-val reg-y-val))
    (if (= reg-x-val reg-y-val)
        (machine-state/skip-next-pc machine-state)
        (machine-state/increment-pc machine-state))))

(defn execute-6XNN
  [machine-state opcode]
  (let [reg (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)]
    (log/debug (format "0x%04x 6XNN set V[%d](0x%02x) = 0x%02x" 
                       (:pc machine-state) reg imm imm))
    (-> machine-state
        (machine-state/set-register reg imm)
        (machine-state/increment-pc))))

(defn execute-7XNN
  [machine-state opcode]
  (let [reg-num (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)
        reg-val (machine-state/get-register machine-state reg-num)
        result (+ reg-val imm)]
    (log/debug (format "0x%04x 7XNN add V[%d](0x%02x) = V[%d](0x%02x) + 0x%02x" 
                       (:pc machine-state) reg-num result reg-num reg-val imm))
    (-> machine-state
        (machine-state/set-register reg-num result)
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
  (let [nnn (utils/get-nnn opcode)]
    (log/debug (format "0x%04x ANNN sti I = 0x%03x" 
                       (:pc machine-state) nnn))
    (-> machine-state
        (machine-state/set-addr-reg nnn)
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
  (Thread/sleep 10000)
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
