(ns chip8-clj.test-FX65
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest memory-to-registers-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0xF565)
                              (machine-state/set-addr-reg 2)
                              (machine-state/set-memory 2 1)
                              (machine-state/set-memory 3 2)
                              (machine-state/set-memory 4 3)
                              (machine-state/set-memory 5 4)
                              (machine-state/set-memory 6 5)
                              (machine-state/set-memory 7 6))
        post-machine-state (step pre-machine-state)]

    ; Checks that these registers have been updated.
    (is (= (machine-state/get-register post-machine-state 0) 1))
    (is (= (machine-state/get-register post-machine-state 1) 2))
    (is (= (machine-state/get-register post-machine-state 2) 3))
    (is (= (machine-state/get-register post-machine-state 3) 4))
    (is (= (machine-state/get-register post-machine-state 4) 5))
    (is (= (machine-state/get-register post-machine-state 5) 6))

    ; Checks that these registers have not been updated.
    (is (= (machine-state/get-register post-machine-state 6) 0))
    (is (= (machine-state/get-register post-machine-state 7) 0))
    (is (= (machine-state/get-register post-machine-state 8) 0))
    (is (= (machine-state/get-register post-machine-state 9) 0))
    (is (= (machine-state/get-register post-machine-state 10) 0))
    (is (= (machine-state/get-register post-machine-state 11) 0))
    (is (= (machine-state/get-register post-machine-state 12) 0))
    (is (= (machine-state/get-register post-machine-state 13) 0))
    (is (= (machine-state/get-register post-machine-state 14) 0))
    (is (= (machine-state/get-register post-machine-state 15) 0))

    (is (= (machine-state/get-pc post-machine-state) 2))))

