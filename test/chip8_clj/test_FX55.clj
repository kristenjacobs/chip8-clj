(ns chip8-clj.test-FX55
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest registers-to-memory-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0xF555)
                              (machine-state/set-addr-reg 2)
                              (machine-state/set-register 0 1)
                              (machine-state/set-register 1 2)
                              (machine-state/set-register 2 3)
                              (machine-state/set-register 3 4)
                              (machine-state/set-register 4 5)
                              (machine-state/set-register 5 6))
        post-machine-state (step pre-machine-state)]

    ; Checks that the memory I0 to I5 has been updated.
    (is (= (machine-state/get-memory post-machine-state 2) 1))
    (is (= (machine-state/get-memory post-machine-state 3) 2))
    (is (= (machine-state/get-memory post-machine-state 4) 3))
    (is (= (machine-state/get-memory post-machine-state 5) 4))
    (is (= (machine-state/get-memory post-machine-state 6) 5))
    (is (= (machine-state/get-memory post-machine-state 7) 6))

    ; Checks that the memory I6 to I15 has not been updated.
    (is (= (machine-state/get-memory post-machine-state 8) 0))
    (is (= (machine-state/get-memory post-machine-state 9) 0))
    (is (= (machine-state/get-memory post-machine-state 10) 0))
    (is (= (machine-state/get-memory post-machine-state 11) 0))
    (is (= (machine-state/get-memory post-machine-state 12) 0))
    (is (= (machine-state/get-memory post-machine-state 13) 0))
    (is (= (machine-state/get-memory post-machine-state 14) 0))
    (is (= (machine-state/get-memory post-machine-state 15) 0))
    (is (= (machine-state/get-memory post-machine-state 16) 0))
    (is (= (machine-state/get-memory post-machine-state 17) 0))

    (is (= (machine-state/get-pc post-machine-state) 2))))

