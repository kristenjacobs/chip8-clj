(ns chip8-clj.test-FX55
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest registers-to-memory-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0xF555)
                              (machine-state/set-addr-reg 0x200)
                              (machine-state/set-register 0 1)
                              (machine-state/set-register 1 2)
                              (machine-state/set-register 2 3)
                              (machine-state/set-register 3 4)
                              (machine-state/set-register 4 5)
                              (machine-state/set-register 5 6))
        post-machine-state (step pre-machine-state)]

    ; Checks that the memory I0 to I5 has been updated.
    (is (= (machine-state/get-memory post-machine-state 0x200) 1))
    (is (= (machine-state/get-memory post-machine-state 0x201) 2))
    (is (= (machine-state/get-memory post-machine-state 0x202) 3))
    (is (= (machine-state/get-memory post-machine-state 0x203) 4))
    (is (= (machine-state/get-memory post-machine-state 0x204) 5))
    (is (= (machine-state/get-memory post-machine-state 0x205) 6))

    ; Checks that the memory I6 to I15 has not been updated.
    (is (= (machine-state/get-memory post-machine-state 0x206) 0))
    (is (= (machine-state/get-memory post-machine-state 0x207) 0))
    (is (= (machine-state/get-memory post-machine-state 0x208) 0))
    (is (= (machine-state/get-memory post-machine-state 0x209) 0))
    (is (= (machine-state/get-memory post-machine-state 0x210) 0))
    (is (= (machine-state/get-memory post-machine-state 0x211) 0))
    (is (= (machine-state/get-memory post-machine-state 0x212) 0))
    (is (= (machine-state/get-memory post-machine-state 0x213) 0))
    (is (= (machine-state/get-memory post-machine-state 0x214) 0))
    (is (= (machine-state/get-memory post-machine-state 0x215) 0))

    (is (= (machine-state/get-pc post-machine-state) 2))))

