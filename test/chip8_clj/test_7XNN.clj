(ns chip8-clj.test-7XNN
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest add-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0x7112)
                              (machine-state/set-register 1 0x23))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-register post-machine-state 1) 0x35))
    (is (= (machine-state/get-pc post-machine-state) 2))))

