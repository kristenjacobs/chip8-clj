(ns chip8-clj.test-8XY2
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest instruction-8XY2-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x8122)
                              (machine-state/set-register 1 0x6)
                              (machine-state/set-register 2 0x5))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 1) 0x4))
    (is (= (machine-state/get-pc post-machine-state) 2))))

