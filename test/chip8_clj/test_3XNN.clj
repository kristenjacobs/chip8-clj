(ns chip8-clj.test-3XNN
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest instruction-3XNN-skip-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x3112)
                              (machine-state/set-register 1 0x12))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-pc post-machine-state) 4))))

(deftest instruction-3XNN-noskip-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x3112)
                              (machine-state/set-register 1 0x13))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-pc post-machine-state) 2))))

