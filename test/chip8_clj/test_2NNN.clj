(ns chip8-clj.test-2NNN
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest call-subroutine-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/set-stack 0x0)
                              (machine-state/set-instr 0 0x2246))
        post-machine-state (step pre-machine-state)]

    (is (= (machine-state/get-stack post-machine-state 0) 0x2))
    (is (= (machine-state/get-stack-ptr post-machine-state) 1))
    (is (= (machine-state/get-pc post-machine-state) 0x246))))

