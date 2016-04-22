(ns chip8-clj.test-8XY4
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest add-register-register-test-no-carry
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x8124)
                              (machine-state/set-register 1 6)
                              (machine-state/set-register 2 4)
                              (machine-state/set-register 0xF 2))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 1) 10))
    (is (= (machine-state/get-register post-machine-state 0xF) 0))
    (is (= (machine-state/get-pc post-machine-state) 2))))

(deftest add-register-register-test-carry
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x8124)
                              (machine-state/set-register 1 200)
                              (machine-state/set-register 2 200)
                              (machine-state/set-register 0xF 2))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 1) 144))
    (is (= (machine-state/get-register post-machine-state 0xF) 1))
    (is (= (machine-state/get-pc post-machine-state) 2))))


