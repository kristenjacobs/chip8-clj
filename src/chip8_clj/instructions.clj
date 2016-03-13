(ns chip8-clj.instructions
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.utils :as utils]
            [chip8-clj.machine-state :as machine-state]
            [clojure.tools.logging :as log]))

(defn execute-6XNN
  [machine-state opcode]
  (log/debug "execute-6XNN")
  (let [reg (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)]
    (machine-state/set-register machine-state reg imm)))

(defn execute-7XNN
  [machine-state opcode]
  (log/debug "execute-7XNN")
  (let [reg (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)
        oper (machine-state/get-register machine-state reg)]
    (machine-state/set-register machine-state reg (+ oper imm))))

