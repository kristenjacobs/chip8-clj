(ns chip8-clj.core-test
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

(deftest instruction-4XNN-skip-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x4112)
                              (machine-state/set-register 1 0x13))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-pc post-machine-state) 4))))

(deftest instruction-4XNN-noskip-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x4112)
                              (machine-state/set-register 1 0x12))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-pc post-machine-state) 2))))

(deftest instruction-5XY0-skip-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x5120)
                              (machine-state/set-register 1 0x13)
                              (machine-state/set-register 2 0x13))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-pc post-machine-state) 4))))

(deftest instruction-5XY0-noskip-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x5120)
                              (machine-state/set-register 1 0x12)
                              (machine-state/set-register 1 0x13))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-pc post-machine-state) 2))))

(deftest instruction-6XNN-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x6112))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 1) 0x12))
    (is (= (machine-state/get-pc post-machine-state) 2))))

(deftest instruction-7XNN-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x7112)
                              (machine-state/set-register 1 0x23))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 1) 0x35))
    (is (= (machine-state/get-pc post-machine-state) 2))))

