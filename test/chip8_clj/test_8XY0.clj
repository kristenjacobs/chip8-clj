(ns chip8-clj.test-8XY0
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest set-register-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x8120)
                              (machine-state/set-register 1 6)
                              (machine-state/set-register 2 4))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 1) 4))
    (is (= (machine-state/get-pc post-machine-state) 2))))

