(ns chip8-clj.test-DXYN
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

;(deftest draw-sprite-test
;  (let [pre-machine-state (-> (machine-state/initialise)
;                              (machine-state/set-pc 0)
;                              (machine-state/set-instr 0 0xD122)
;                              (machine-state/set-addr-reg 2)
;                              (machine-state/set-memory 2 0xaa)
;                              (machine-state/set-memory 3 0x55)
;                              (machine-state/set-register 1 0)
;                              (machine-state/set-register 2 0)
;                              (machine-state/set-screen-buffer-byte 0 0 0x55)
;                              (machine-state/set-screen-buffer-byte 0 1 0xaa))
;        post-machine-state (step pre-machine-state)]
;
;    ; Checks the screen buffer has been updated.
;    (is (= (machine-state/get-screen-buffer-byte post-machine-state 0 0) 0xFF))
;    (is (= (machine-state/get-screen-buffer-byte post-machine-state 0 1) 0xFF))
;    ; Checks that VF (the carry flag) is not set.
;    (is (= (machine-state/get-register post-machine-state 0xF) 0))
;    ; Checks that the pc has been updated correctly.
;    (is (= (machine-state/get-pc post-machine-state) 2))))

(deftest draw-sprite-with-carry-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0xD122)
                              (machine-state/set-addr-reg 2)
                              (machine-state/set-memory 2 0xaa)
                              (machine-state/set-memory 3 0x55)
                              (machine-state/set-register 1 0)
                              (machine-state/set-register 2 0)
                              (machine-state/set-screen-buffer-byte 0 0 0xaa)
                              (machine-state/set-screen-buffer-byte 0 1 0x55))
        post-machine-state (step pre-machine-state)]

    ; Checks the screen buffer has been updated.
    (is (= (machine-state/get-screen-buffer-byte post-machine-state 0 0) 0x00))
    (is (= (machine-state/get-screen-buffer-byte post-machine-state 0 1) 0x00))
    ; Checks that VF (the carry flag) is set.
    (is (= (machine-state/get-register post-machine-state 0xF) 1))
    ; Checks that the pc has been updated correctly.
    (is (= (machine-state/get-pc post-machine-state) 2))))



