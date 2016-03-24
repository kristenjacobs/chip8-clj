(ns chip8-clj.test-ANNN
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest instruction-ANNN-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-instr 0 0xA123))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-addr-reg post-machine-state) 0x123))
    (is (= (machine-state/get-pc post-machine-state) 2))))

