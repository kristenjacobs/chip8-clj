(ns chip8-clj.utils)

(defn get-byte0 [opcode] (bit-and 0xFF (bit-shift-right opcode 8)))
(defn get-byte1 [opcode] (bit-and 0xFF (bit-shift-right opcode 0)))

(defn get-nibble0 [opcode] (bit-and 0xF (bit-shift-right opcode 12)))
(defn get-nibble1 [opcode] (bit-and 0xF (bit-shift-right opcode 8)))
(defn get-nibble2 [opcode] (bit-and 0xF (bit-shift-right opcode 4)))
(defn get-nibble3 [opcode] (bit-and 0xF (bit-shift-right opcode 0)))

