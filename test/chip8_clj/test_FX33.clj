(ns chip8-clj.test-FX33
  (:require [chip8-clj.graphics :as graphics]
            [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest binary-coded-decimal-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0xF133)
                              (machine-state/set-register 1 243)
                              (machine-state/set-addr-reg 2))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-memory post-machine-state 2) 2))
    (is (= (machine-state/get-memory post-machine-state 3) 4))
    (is (= (machine-state/get-memory post-machine-state 4) 3))
    (is (= (machine-state/get-pc post-machine-state) 2))))

