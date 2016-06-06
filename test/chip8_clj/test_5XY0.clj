(ns chip8-clj.test-5XY0
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest skip-taken-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x5120)
                              (machine-state/set-register 1 0x13)
                              (machine-state/set-register 2 0x13))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-pc post-machine-state) 4))))

(deftest skip-not-taken-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x5120)
                              (machine-state/set-register 1 0x12)
                              (machine-state/set-register 1 0x13))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-pc post-machine-state) 2))))
