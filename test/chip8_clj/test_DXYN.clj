(ns chip8-clj.test-DXYN
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest draw-single-sprite-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0xD121)
                              (machine-state/set-addr-reg 2)
                              (machine-state/set-memory 2 0xFF)
                              (machine-state/set-register 1 0)
                              (machine-state/set-register 2 0)
                              (machine-state/set-screen-buffer-byte 0 0 0x00))
        post-machine-state (step pre-machine-state)]

    ;(is (= (machine-state/get-addr-reg post-machine-state) 0x123))

    ; Checks the screen buffer has been updated.
    ; Checks that VF is not set.

    (is (= (machine-state/get-pc post-machine-state) 2))))

