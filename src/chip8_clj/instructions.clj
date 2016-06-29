(ns chip8-clj.instructions
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.utils :as utils]
            [chip8-clj.machine-state :as machine-state]
            [chip8-clj.state :as state]
            [clojure.tools.logging :as log]))

(defn- trace
  [machine-state opcode trace-str & trace-args]
  (log/info (format "0x%04x 0x%04x %s" 
                    (machine-state/get-pc machine-state) opcode 
                    (apply format trace-str trace-args))))

(defn execute-0NNN
  "Calls RCA 1802 program at address NNN. Not necessary for most ROMs."
  [machine-state opcode]
  (trace machine-state opcode "0NNN Not yet implemented")
  (System/exit 1))

(defn execute-00E0
  "Clears the screen."
  [machine-state opcode]
  (trace machine-state opcode "00E0 cls")
  (-> (reduce ; For each column in the screen buffer.
        (fn [machine-state y] 
          (reduce ; For each row in the screen bufer.
            (fn [machine-state x]
              (state/add-screen-update {:x x :y y :bit 0})
              (machine-state/set-screen-buffer machine-state x y 0))
            machine-state (range 0 graphics/width-pixels)))
        machine-state (range 0 graphics/height-pixels))
      (machine-state/increment-pc)))

(defn execute-00EE
  "Returns from a subroutine."
  [machine-state opcode]
  (let [[machine-state return-addr] (machine-state/pop-stack machine-state)]
    (trace machine-state opcode "00EE ret 0x%04x" return-addr)
    (machine-state/set-pc machine-state return-addr)))

(defn execute-1NNN
  "Jumps to address NNN."
  [machine-state opcode]
  (let [imm (utils/get-nnn opcode)]
    (trace machine-state opcode "1NNN jmp 0x%04x" imm)
    (machine-state/set-pc machine-state imm)))

(defn execute-2NNN
  "Calls subroutine at NNN."
  [machine-state opcode]
  (let [target-addr (utils/get-nnn opcode)]
    (trace machine-state opcode "2NNN cll 0x%04x" target-addr)
    (-> machine-state
        (machine-state/push-stack (+ (machine-state/get-pc machine-state) 2))
        (machine-state/set-pc target-addr))))

(defn execute-3XNN
  "Skips the next instruction if VX equals NN."
  [machine-state opcode]
  (let [reg-num (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)
        reg-val (machine-state/get-register machine-state reg-num)]
    (trace machine-state opcode "3XNN skp V[%d](0x%02x) == 0x%02x" reg-num reg-val imm)
    (if (= reg-val imm)
        (machine-state/skip-next-pc machine-state)
        (machine-state/increment-pc machine-state))))

(defn execute-4XNN
  "Skips the next instruction if VX doesn't equal NN."
  [machine-state opcode]
  (let [reg-num (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)
        reg-val (machine-state/get-register machine-state reg-num)]
    (trace machine-state opcode "4XNN skp V[%d](0x%02x) != 0x%02x" reg-num reg-val imm)
    (if (not= reg-val imm)
        (machine-state/skip-next-pc machine-state)
        (machine-state/increment-pc machine-state))))

(defn execute-5XY0
  "Skips the next instruction if VX equals VY."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-y-num (utils/get-nibble2 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num) 
        reg-y-val (machine-state/get-register machine-state reg-y-num)]
    (trace machine-state opcode "5XY0 skp V[%d](0x%02x) == V[%d](0x%02x)" reg-x-num reg-x-val reg-y-val reg-y-val)
    (if (= reg-x-val reg-y-val)
        (machine-state/skip-next-pc machine-state)
        (machine-state/increment-pc machine-state))))

(defn execute-6XNN
  "Sets VX to NN."
  [machine-state opcode]
  (let [reg (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)]
    (trace machine-state opcode "6XNN set V[%d](0x%02x) = 0x%02x" reg imm imm)
    (-> machine-state
        (machine-state/set-register reg imm)
        (machine-state/increment-pc))))

(defn execute-7XNN
  "Adds NN to VX."
  [machine-state opcode]
  (let [reg-num (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)
        reg-val (machine-state/get-register machine-state reg-num)
        result (+ reg-val imm)]
    (trace machine-state opcode "7XNN add V[%d](0x%02x) = V[%d](0x%02x) + 0x%02x" reg-num result reg-num reg-val imm)
    (-> machine-state
        (machine-state/set-register reg-num result)
        (machine-state/increment-pc))))

(defn execute-8XY0
  "Sets VX to the value of VY."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-y-num (utils/get-nibble2 opcode)
        reg-y-val (machine-state/get-register machine-state reg-y-num)]
    (trace machine-state opcode "8XY0 set V[%d](0x%02x) = V[%d](0x%02x)" reg-x-num reg-y-val reg-y-num reg-y-val)
    (-> machine-state
        (machine-state/set-register reg-x-num reg-y-val)
        (machine-state/increment-pc))))

(defn execute-8XY1
  "Sets VX to VX or VY."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)
        reg-y-num (utils/get-nibble2 opcode)
        reg-y-val (machine-state/get-register machine-state reg-y-num)
        result (bit-or reg-x-val reg-y-val)]
    (trace machine-state opcode "8XY1 bor V[%d](0x%02x) = V[%d](0x%02x) & V[%d](0x%02x)" reg-x-num result reg-x-num reg-x-val reg-y-num reg-y-val)
    (-> machine-state
        (machine-state/set-register reg-x-num result)
        (machine-state/increment-pc))))

(defn execute-8XY2
  "Sets VX to VX and VY."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)
        reg-y-num (utils/get-nibble2 opcode)
        reg-y-val (machine-state/get-register machine-state reg-y-num)
        result (bit-and reg-x-val reg-y-val)]
    (trace machine-state opcode "8XY2 bnd V[%d](0x%02x) = V[%d](0x%02x) & V[%d](0x%02x)" reg-x-num result reg-x-num reg-x-val reg-y-num reg-y-val)
    (-> machine-state
        (machine-state/set-register reg-x-num result)
        (machine-state/increment-pc))))

(defn execute-8XY3
  "Sets VX to VX xor VY."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)
        reg-y-num (utils/get-nibble2 opcode)
        reg-y-val (machine-state/get-register machine-state reg-y-num)
        result (bit-xor reg-x-val reg-y-val)]
    (trace machine-state opcode "8XY3 xor V[%d](0x%02x) = V[%d](0x%02x) & V[%d](0x%02x)" reg-x-num result reg-x-num reg-x-val reg-y-num reg-y-val)
    (-> machine-state
        (machine-state/set-register reg-x-num result)
        (machine-state/increment-pc))))

(defn execute-8XY4
  "Adds VY to VX. VF is set to 1 when there's a carry, 
  and to 0 when there isn't."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)
        reg-y-num (utils/get-nibble2 opcode)
        reg-y-val (machine-state/get-register machine-state reg-y-num)
        result (+ reg-x-val reg-y-val)
        is-carry? (> result 255)]
    (trace machine-state opcode "8XY4 add V[%d](0x%02x) = V[%d](0x%02x) + V[%d](0x%02x)" reg-x-num result reg-x-num reg-x-val reg-y-num reg-y-val)
    (-> (if is-carry?
          (machine-state/set-register machine-state 0xF 1)
          (machine-state/set-register machine-state 0xF 0))
        (machine-state/set-register reg-x-num result)
        (machine-state/increment-pc))))

(defn execute-8XY5
  "VY is subtracted from VX. VF is set to 0 when there's a 
  borrow, and 1 when there isn't."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)
        reg-y-num (utils/get-nibble2 opcode)
        reg-y-val (machine-state/get-register machine-state reg-y-num)
        is-borrow? (> reg-y-val reg-x-val)
        result (if is-borrow?
                 (+ (- reg-x-val reg-y-val) 256)
                 (- reg-x-val reg-y-val))]
    (trace machine-state opcode "8XY5 sb5 V[%d](0x%02x) = V[%d](0x%02x) - V[%d](0x%02x)" reg-x-num result reg-x-num reg-x-val reg-y-num reg-y-val)
    (-> (if is-borrow?
          (machine-state/set-register machine-state 0xF 0)
          (machine-state/set-register machine-state 0xF 1))
        (machine-state/set-register reg-x-num result)
        (machine-state/increment-pc))))

(defn execute-8XY6
  "Shifts VX right by one. VF is set to the value of the 
  least significant bit of VX before the shift.[2]"
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)
        result-x (bit-shift-right reg-x-val 1)
        result-f (bit-and reg-x-val 0x1)]
    (trace machine-state opcode "8XY6 shr V[%d](0x%02x) = V[%d](0x%02x) >> 1" reg-x-num result-x reg-x-num reg-x-val)
    (-> (machine-state/set-register machine-state 0xF result-f)
        (machine-state/set-register reg-x-num result-x)
        (machine-state/increment-pc))))

(defn execute-8XY7
  "Sets VX to VY minus VX. VF is set to 0 when there's a 
  borrow, and 1 when there isn't."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)
        reg-y-num (utils/get-nibble2 opcode)
        reg-y-val (machine-state/get-register machine-state reg-y-num)
        is-borrow? (> reg-x-val reg-y-val)
        result (if is-borrow?
                 (+ (- reg-y-val reg-x-val) 256)
                 (- reg-y-val reg-x-val))]
    (trace machine-state opcode "8XY7 sb7 V[%d](0x%02x) = V[%d](0x%02x) - V[%d](0x%02x)" 
           reg-x-num result reg-y-num reg-y-val reg-x-num reg-x-val)
    (-> (if is-borrow?
          (machine-state/set-register machine-state 0xF 0)
          (machine-state/set-register machine-state 0xF 1))
        (machine-state/set-register reg-x-num result)
        (machine-state/increment-pc))))

(defn execute-8XYE
  "Shifts VX left by one. VF is set to the value of the most 
  significant bit of VX before the shift.[2]"
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)
        result-x (bit-and (bit-shift-left reg-x-val 1) 0xFF)
        result-f (bit-and (bit-shift-right reg-x-val 7) 0x1)]
    (trace machine-state opcode "8XYE shl V[%d](0x%02x) = V[%d](0x%02x) << 1" 
           reg-x-num result-x reg-x-num reg-x-val)
    (-> (machine-state/set-register machine-state 0xF result-f)
        (machine-state/set-register reg-x-num result-x)
        (machine-state/increment-pc))))

(defn execute-9XY0
  "Skips the next instruction if VX doesn't equal VY."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)
        reg-y-num (utils/get-nibble2 opcode)
        reg-y-val (machine-state/get-register machine-state reg-y-num)]
    (trace machine-state opcode "9XY0 seq V[%d](0x%02x) == V[%d](0x%02x)" 
           reg-x-num reg-x-val reg-y-num reg-y-val)
    (if (not= reg-x-val reg-y-val)
      (machine-state/skip-next-pc machine-state)
      (machine-state/increment-pc machine-state))))

(defn execute-ANNN
  "Sets I to the address NNN."
  [machine-state opcode]
  (let [nnn (utils/get-nnn opcode)]
    (trace machine-state opcode "ANNN sti I = 0x%03x" nnn)
    (-> machine-state
        (machine-state/set-addr-reg nnn)
        (machine-state/increment-pc))))

(defn execute-BNNN
  "Jumps to the address NNN plus V0."
  [machine-state opcode]
  (let [reg-0-val (machine-state/get-register machine-state 0)
        imm (utils/get-nnn opcode)
        dest (+ imm reg-0-val)]
    (trace machine-state opcode "BNNN jpa 0x%04x + V0[0x%02x] = 0x%04x" imm reg-0-val dest)
    (machine-state/set-pc machine-state dest)))

(defn execute-CXNN
  "Sets VX to the result of a bitwise and operation on a random number and NN."
  [machine-state opcode]
  (let [reg-num (utils/get-nibble1 opcode)
        imm (utils/get-byte1 opcode)
        rnd (rand-int 0xFF)
        result (bit-and imm rnd)]
    (trace machine-state opcode "CXNN rnd V[%d](0x%02x) <- rand(0x%02x) & NN(0x%02x)" reg-num result rnd imm)
    (-> machine-state
        (machine-state/set-register reg-num result)
        (machine-state/increment-pc))))

(defn execute-DXYN
  "Sprites stored in memory at location in index register (I), 8bits wide. 
  Wraps around the screen. If when drawn, clears a pixel, register VF is set 
  to 1 otherwise it is zero. All drawing is XOR drawing (i.e. it toggles the 
  screen pixels). Sprites are drawn starting at position VX, VY. N is the 
  number of 8bit rows that need to be drawn. If N is greater than 1, second 
  line continues at position VX, VY+1, and so on."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-y-num (utils/get-nibble2 opcode)
        imm (utils/get-nibble3 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num) 
        reg-y-val (machine-state/get-register machine-state reg-y-num)
        addr-reg (machine-state/get-addr-reg machine-state)]
    (trace machine-state opcode "DXVN drw V[%d](0x%02x), V[%d](0x%02x), %d, I(0x%04x)" 
           reg-x-num reg-x-val reg-y-num reg-y-val imm addr-reg)

    (as-> machine-state $ 
      (machine-state/clear-carry-flag $)

      (reduce ; For each row in the sprite
        (fn [machine-state row-index] 
          (let [memory-byte (machine-state/get-memory 
                             machine-state (+ addr-reg row-index))]
            (reduce ; For each pixel in the row
              (fn [machine-state pixel-index]
                (let [x (mod (+ reg-x-val pixel-index) graphics/width-pixels)
                      y (mod (+ reg-y-val row-index) graphics/height-pixels)
                      screen-buffer (machine-state/get-screen-buffer machine-state x y)
                      memory-bit (bit-and (bit-shift-right memory-byte (- 7 pixel-index)) 0x1)
                  
                      ; xors the current screen buffer bit with the relevent bit from the memory byte.
                      updated-bit (bit-xor screen-buffer memory-bit)

                      ; Sets the carry flag in the machine state if any bits are flipped.
                      machine-state (if (and (= screen-buffer 1) (= memory-bit 1))
                                      (machine-state/set-carry-flag machine-state)
                                      machine-state)]

                  ; Appends the coordinates/value of the change to the screen updates. 
                  (state/add-screen-update {:x x :y y :bit updated-bit})

                  ; Writes the updated bit back to the machine state.
                  (machine-state/set-screen-buffer
                    machine-state x y updated-bit)))  

              machine-state (range 0 8))))
         $ (range 0 imm))  
      (machine-state/increment-pc $))))

(defn execute-EX9E
  "Skips the next instruction if the key stored in VX is pressed."
  [machine-state opcode]
  (let [reg-num (utils/get-nibble1 opcode)
        reg-val (machine-state/get-register machine-state reg-num)]
    (trace machine-state opcode "EX9E kpt V[%d](0x%02x)" reg-num reg-val)
    (if (state/is-key-pressed reg-val)
        (machine-state/skip-next-pc machine-state)
        (machine-state/increment-pc machine-state))))

(defn execute-EXA1
  "Skips the next instruction if the key stored in VX isn't pressed."
  [machine-state opcode]
  (let [reg-num (utils/get-nibble1 opcode)
        reg-val (machine-state/get-register machine-state reg-num)]
    (trace machine-state opcode "EXA1 kpf V[%d](0x%02x)" reg-num reg-val)
    (if (not (state/is-key-pressed reg-val))
      (machine-state/skip-next-pc machine-state)
      (machine-state/increment-pc machine-state))))

(defn execute-FX07
  "Sets VX to the value of the delay timer."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        delay-timer-val (state/get-delay-timer)]
    (trace machine-state opcode "FX07 gdt V[%d](0x%02x)" reg-x-num delay-timer-val)
    (-> machine-state
        (machine-state/set-register reg-x-num delay-timer-val)
        (machine-state/increment-pc))))

(defn execute-FX0A
  "A key press is awaited, and then stored in VX."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)]
    (loop [key-pressed (state/get-key-pressed)] 
      (if (= key-pressed nil)
        (recur (state/get-key-pressed))
        (do
          (trace machine-state opcode "FX0A wkp V[%d](0x%02x)" reg-x-num key-pressed)
          (-> machine-state
              (machine-state/set-register reg-x-num key-pressed)
              (machine-state/increment-pc)))))))

(defn execute-FX15
  "Sets the delay timer to VX."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)]
    (trace machine-state opcode "FX15 sdt V[%d](0x%02x)" reg-x-num reg-x-val)
    (state/set-delay-timer reg-x-val)
    (machine-state/increment-pc machine-state)))

(defn execute-FX18
  "Sets the sound timer to VX."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)]
    (trace machine-state opcode "FX18 sst V[%d](0x%02x)" reg-x-num reg-x-val)
    (state/set-sound-timer reg-x-val)
    (machine-state/increment-pc machine-state)))

(defn execute-FX1E
  "Adds VX to I.[3]"
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)
        addr-reg (machine-state/get-addr-reg machine-state)]
    (trace machine-state opcode "FX1E adi V[%d](0x%02x) I(0x%02x)" reg-x-num reg-x-val addr-reg)
    (-> machine-state
        (machine-state/set-addr-reg (+ addr-reg reg-x-val))
        (machine-state/increment-pc))))

(defn execute-FX29
  "Sets I to the location of the sprite for the character in VX. 
  Characters 0-F (in hexadecimal) are represented by a 4x5 font."
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num)]
    (trace machine-state opcode "FX29 gfa V[%d](0x%02x)" reg-x-num reg-x-val)
    (-> machine-state
        (machine-state/set-addr-reg (* reg-x-val 5))
        (machine-state/increment-pc))))

(defn execute-FX33
  "Stores the binary-coded decimal representation of VX, with the most 
  significant of three digits at the address in I, the middle digit at 
  I plus 1, and the least significant digit at I plus 2. (In other words, 
  take the decimal representation of VX, place the hundreds digit in 
  memory at location in I, the tens digit at location I+1, and the ones 
  digit at location I+2.)"
  [machine-state opcode]
  (let [reg-x-num (utils/get-nibble1 opcode)
        reg-x-val (machine-state/get-register machine-state reg-x-num) 
        addr-reg (machine-state/get-addr-reg machine-state)
        ones (mod reg-x-val 10)
        tens (quot (mod reg-x-val 100) 10)
        hundreds (quot (mod reg-x-val 1000) 100)]
    (trace machine-state opcode "FX33 bcd V[%d](0x%02x), I(0x%04x), %d-%d-%d" 
           reg-x-num reg-x-val addr-reg, hundreds, tens, ones)
    (-> machine-state
        (machine-state/set-memory addr-reg hundreds)
        (machine-state/set-memory (+ addr-reg 1) tens)
        (machine-state/set-memory (+ addr-reg 2) ones)
        (machine-state/increment-pc))))

(defn execute-FX55
  "Stores V0 to VX (including VX) in memory starting at address I.[4]"
  [machine-state opcode]
  (let [reg-x (utils/get-nibble1 opcode)
        addr-reg (machine-state/get-addr-reg machine-state)]
    (trace machine-state opcode "FX55 rtm V[%d], I(0x%04x)" reg-x addr-reg)
    (-> (reduce (fn [machine-state index]
                  (let [reg-val (machine-state/get-register machine-state index)]
                    (machine-state/set-memory machine-state (+ addr-reg index) reg-val)))
                machine-state (range 0 (inc reg-x)))
        (machine-state/increment-pc))))

(defn execute-FX65
  "Fills V0 to VX (including VX) with values from memory starting at address I.[4]"
  [machine-state opcode]
  (let [reg-x (utils/get-nibble1 opcode)
        addr-reg (machine-state/get-addr-reg machine-state)]
    (trace machine-state opcode "FX65 mtr V[%d], I(0x%04x)" reg-x addr-reg)
    (-> (reduce (fn [machine-state index]
                  (let [reg-val (machine-state/get-memory machine-state (+ addr-reg index))]
                    (machine-state/set-register machine-state index reg-val)))
                machine-state (range 0 (inc reg-x)))
        (machine-state/increment-pc))))

