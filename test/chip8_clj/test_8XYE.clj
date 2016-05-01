(ns chip8-clj.test-8XYE
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest shift-left-test-top-bit-set
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x812E)
                              (machine-state/set-register 1 0xFF)
                              (machine-state/set-register 0xF 0))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 1) 0xFE))
    (is (= (machine-state/get-register post-machine-state 0xF) 1))
    (is (= (machine-state/get-pc post-machine-state) 2))))

(deftest shift-left-test-top-bit-not-set
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x812E)
                              (machine-state/set-register 1 0x7F)
                              (machine-state/set-register 0xF 0))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 1) 0xFE))
    (is (= (machine-state/get-register post-machine-state 0xF) 0))
    (is (= (machine-state/get-pc post-machine-state) 2))))

