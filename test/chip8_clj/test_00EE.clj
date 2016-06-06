(ns chip8-clj.test-00EE
  (:require [chip8-clj.machine-state :as machine-state])
  (:require [clojure.test :refer :all]
            [chip8-clj.core :refer :all]))

(deftest return-from-subroutine-test
  (let [pre-machine-state (-> (machine-state/initialise)
                              (machine-state/set-pc 0)
                              (machine-state/push-stack 0x1234)
                              (machine-state/set-instr 0 0x00EE))
        post-machine-state (step pre-machine-state)]
    (is (= (machine-state/get-stack-ptr post-machine-state) 0))
    (is (= (machine-state/get-pc post-machine-state) 0x1234))))

