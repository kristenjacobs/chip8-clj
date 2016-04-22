(ns chip8-clj.test-8XY5
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest sub-register-register-test-no-borrow
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x8125)
                              (machine-state/set-register 1 6)
                              (machine-state/set-register 2 4)
                              (machine-state/set-register 0xF 2))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 1) 2))
    (is (= (machine-state/get-register post-machine-state 0xF) 1))
    (is (= (machine-state/get-pc post-machine-state) 2))))

(deftest sub-register-register-test-borrow
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x8125)
                              (machine-state/set-register 1 4)
                              (machine-state/set-register 2 6)
                              (machine-state/set-register 0xF 2))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 1) 254))
    (is (= (machine-state/get-register post-machine-state 0xF) 0))
    (is (= (machine-state/get-pc post-machine-state) 2))))


