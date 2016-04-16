(ns chip8-clj.instructions
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.utils :as utils]
            [chip8-clj.machine-state :as machine-state]
            [clojure.tools.logging :as log]))

(defn execute-0NNN
  [machine-state opcode]
  (log/info "execute-0NNN: Error: Not yet implemented")
  (System/exit 1))

(defn execute-00E0
  [machine-state opcode]
  (log/info "execute-00E0: Error: Not yet implemented")
  (System/exit 1))

(defn execute-00EE
  [machine-state opcode]
  (let [machine-state (assoc machine-state :stack-ptr (dec (:stack-ptr machine-state)))
        return-addr (get (:stack machine-state) (:stack-ptr machine-state))]
    (log/info (format "0x%04x 00EE 0x%04x ret 0x%04x" (:pc machine-state) opcode return-addr))
    (assoc machine-state :pc return-addr)))

(defn execute-1NNN
  [machine-state opcode]
  (let [imm (utils/get-nnn opcode)]
    (log/info (format "0x%04x 1NNN 0x%04x jmp 0x%04x" (:pc machine-state) opcode imm))
    (machine-state/set-pc machine-state imm)))

(defn execute-2NNN
  [machine-state opcode]
  (let [target-addr (utils/get-nnn opcode)]
    (log/info (format "0x%04x 2NNN 0x%04x cll 0x%04x" (:pc machine-state) opcode target-addr))
    (aset-int (:stack machine-state) (:stack-ptr machine-state) (+ (:pc machine-state) 2))
    (-> machine-state
        (assoc :stack-ptr (inc (:stack-ptr machine-state)))
        (assoc :pc target-addr))))

(defn execute-3XNN
  [machine-state opcode]
  (let [reg-num (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)
        reg-val (machine-state/get-register machine-state reg-num)]
    (log/info (format "0x%04x 3XNN 0x%04x skp V[%d](0x%02x) == 0x%02x" 
                       (:pc machine-state) opcode reg-num reg-val imm))
    (if (= reg-val imm)
        (machine-state/skip-next-pc machine-state)
        (machine-state/increment-pc machine-state))))

(defn execute-4XNN
  [machine-state opcode]
  (let [reg-num (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)
        reg-val (machine-state/get-register machine-state reg-num)]
    (log/info (format "0x%04x 4XNN 0x%04x skp V[%d](0x%02x) != 0x%02x" 
                       (:pc machine-state) opcode reg-num reg-val imm))
    (if (not= reg-val imm)
        (machine-state/skip-next-pc machine-state)
        (machine-state/increment-pc machine-state))))

(defn execute-5XY0
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-y-num (utils/get-nibble2 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num) 
        reg-y-val (machine-state/get-register machine-state reg-y-num)]
    (log/info (format "0x%04x 5XY0 0x%04x skp V[%d](0x%02x) == V[%d](0x%02x)" 
                       (:pc machine-state) opcode reg-x-num reg-x-val reg-y-val reg-y-val))
    (if (= reg-x-val reg-y-val)
        (machine-state/skip-next-pc machine-state)
        (machine-state/increment-pc machine-state))))

(defn execute-6XNN
  [machine-state opcode]
  (let [reg (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)]
    (log/info (format "0x%04x 6XNN 0x%04x set V[%d](0x%02x) = 0x%02x" 
                       (:pc machine-state) opcode reg imm imm))
    (-> machine-state
        (machine-state/set-register reg imm)
        (machine-state/increment-pc))))

(defn execute-7XNN
  [machine-state opcode]
  (let [reg-num (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)
        reg-val (machine-state/get-register machine-state reg-num)
        result (+ reg-val imm)]
    (log/info (format "0x%04x 7XNN 0x%04x add V[%d](0x%02x) = V[%d](0x%02x) + 0x%02x" 
                       (:pc machine-state) opcode reg-num result reg-num reg-val imm))
    (-> machine-state
        (machine-state/set-register reg-num result)
        (machine-state/increment-pc))))

(defn execute-8XY0
  [machine-state opcode]
  (log/info "execute-8XY0: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XY1
  [machine-state opcode]
  (log/info "execute-8XY1: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XY2
  [machine-state opcode]
  (log/info "execute-8XY2: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XY3
  [machine-state opcode]
  (log/info "execute-8XY3: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XY4
  [machine-state opcode]
  (log/info "execute-8XY4: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XY5
  [machine-state opcode]
  (log/info "execute-8XY5: Error: Not yet implemented")
  
  (System/exit 1))

(defn execute-8XY6
  [machine-state opcode]
  (log/info "execute-8XY6: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XY7
  [machine-state opcode]
  (log/info "execute-8XY7: Error: Not yet implemented")
  (System/exit 1))

(defn execute-8XYE
  [machine-state opcode]
  (log/info "execute-8XYE: Error: Not yet implemented")
  (System/exit 1))

(defn execute-9XY0
  [machine-state opcode]
  (log/info "execute-9XY0: Error: Not yet implemented")
  (System/exit 1))

(defn execute-ANNN
  [machine-state opcode]
  (let [nnn (utils/get-nnn opcode)]
    (log/info (format "0x%04x ANNN 0x%04x sti I = 0x%03x" 
                       (:pc machine-state) opcode nnn))
    (-> machine-state
        (machine-state/set-addr-reg nnn)
        (machine-state/increment-pc))))

(defn execute-BNNN
  [machine-state opcode]
  (log/info "execute-BNNN: Error: Not yet implemented")
  (System/exit 1))

(defn execute-CXNN
  [machine-state opcode]
  (log/info "execute-CXNN: Error: Not yet implemented")
  (System/exit 1))

(defn execute-DXYN
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-y-num (utils/get-nibble2 opcode)
        imm (utils/get-nibble3 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num) 
        reg-y-val (machine-state/get-register machine-state reg-y-num)
        addr-reg (machine-state/get-addr-reg machine-state)]
    (log/info (format "0x%04x DXVN 0x%04x drw V[%d](0x%02x), V[%d](0x%02x), %d, I(0x%04x)" 
                       (:pc machine-state) opcode reg-x-num reg-x-val reg-y-num reg-y-val imm addr-reg))

    (as-> machine-state $ 
      (machine-state/clear-carry-flag $)

      ; For each row in the sprite
      (reduce 
        (fn [machine-state row-index] 
          (let [memory-byte (machine-state/get-memory 
                             machine-state (+ addr-reg row-index))]
            ; For each pixel in the row
            (reduce 
              (fn [machine-state pixel-index]
                (let [x (mod (+ reg-x-val pixel-index) graphics/width-pixels)
                      y (mod (+ reg-y-val row-index) graphics/height-pixels)
                      screen-buffer (machine-state/get-screen-buffer machine-state x y)
                      memory-bit (bit-and (bit-shift-right memory-byte (- 7 pixel-index)) 0x1)

                      ; Sets the carry flag in the machine state if any bits are flipped.
                      machine-state (if (and (= screen-buffer 1) (= memory-bit 1))
                                      (machine-state/set-carry-flag machine-state)
                                      machine-state)]

                  ; xors the current screen buffer bit with the relevent bit 
                  ; from the memory byte, and writes it back to the machine state.
                  (machine-state/set-screen-buffer
                    machine-state x y (bit-xor screen-buffer memory-bit))))  

              machine-state (range 0 8))))
         $ (range 0 imm))  

      (graphics/render-screen-buffer $)
      (machine-state/increment-pc $))))

(defn execute-EX9E
  [machine-state opcode]
  (log/info "execute-EX9E: Error: Not yet implemented")
  (System/exit 1))

(defn execute-EXA1
  [machine-state opcode]
  (log/info "execute-EXA1: Error: Not yet implemented")
  (System/exit 1))

(defn execute-FX07
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        delay-timer-val (machine-state/get-delay-timer)]
    (log/info (format "0x%04x FX15 0x%04x gdt V[%d](0x%02x)" 
                       (:pc machine-state) opcode reg-x-num delay-timer-val))
    (-> machine-state
        (machine-state/set-register reg-x-num delay-timer-val)
        (machine-state/increment-pc))))

(defn execute-FX0A
  [machine-state opcode]
  (log/info "execute-FX0A: Error: Not yet implemented")
  (System/exit 1))

(defn execute-FX15
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)]
    (log/info (format "0x%04x FX15 0x%04x sdt V[%d](0x%02x)" 
                       (:pc machine-state) opcode reg-x-num reg-x-val))
    (machine-state/set-delay-timer reg-x-val)
    (machine-state/increment-pc machine-state)))

(defn execute-FX18
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)]
    (log/info (format "0x%04x FX18 0x%04x sst V[%d](0x%02x)" 
                       (:pc machine-state) opcode reg-x-num reg-x-val))
    (machine-state/set-sound-timer reg-x-val)
    (machine-state/increment-pc machine-state)))

(defn execute-FX1E
  [machine-state opcode]
  (log/info "execute-FX1E: Error: Not yet implemented")
  (System/exit 1))

(defn execute-FX29
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)]
    (log/info (format "0x%04x FX29 0x%04x gfa V[%d](0x%02x)" 
                       (:pc machine-state) opcode reg-x-num reg-x-val))
    (-> machine-state
        (machine-state/set-addr-reg (* reg-x-val 5))
        (machine-state/increment-pc))))

(defn execute-FX33
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num) 
        addr-reg (machine-state/get-addr-reg machine-state)
        ones (mod reg-x-val 10)
        tens (quot (mod reg-x-val 100) 10)
        hundreds (quot (mod reg-x-val 1000) 100)]
    (log/info (format "0x%04x FX33 0x%04x bcd V[%d](0x%02x), I(0x%04x), %d-%d-%d" 
                       (:pc machine-state) opcode reg-x-num reg-x-val addr-reg, hundreds, tens, ones))
    (-> machine-state
        (machine-state/set-memory addr-reg hundreds)
        (machine-state/set-memory (+ addr-reg 1) tens)
        (machine-state/set-memory (+ addr-reg 2) ones)
        (machine-state/increment-pc))))

(defn execute-FX55
  [machine-state opcode]
  (log/info "execute-FX55: Error: Not yet implemented")
  (System/exit 1))

(defn execute-FX65
  [machine-state opcode]
  (let [reg-x (utils/get-nibble1 opcode)
        addr-reg (machine-state/get-addr-reg machine-state)]
    (log/info (format "0x%04x FX65 0x%04x mtr V[%d], I(0x%04x)" 
                       (:pc machine-state) opcode reg-x addr-reg))
    (-> (reduce (fn [machine-state index]
                  (let [reg-val (machine-state/get-memory machine-state (+ addr-reg index))]
                    (machine-state/set-register machine-state index reg-val)))
                machine-state (range 0 (inc reg-x)))
        (machine-state/increment-pc))))

