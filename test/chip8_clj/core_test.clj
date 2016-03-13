(ns chip8-clj.core-test
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest instruction-6XNN-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-memory 0 0x61)
                              (machine-state/set-memory 1 0x12))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 1) 0x12))
    (is (= (machine-state/get-pc post-machine-state) 2))))

(deftest instruction-7XNN-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-memory 0 0x71)
                              (machine-state/set-memory 1 0x12)
                              (machine-state/set-register 1 0x23))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 1) 0x35))
    (is (= (machine-state/get-pc post-machine-state) 2))))

