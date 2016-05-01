(ns chip8-clj.test-9XY0
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest skip-test-equal
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x9120)
                              (machine-state/set-register 1 6)
                              (machine-state/set-register 2 6))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-pc post-machine-state) 2))))

(deftest skip-test-not-equal
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x9120)
                              (machine-state/set-register 1 6)
                              (machine-state/set-register 2 5))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-pc post-machine-state) 4))))

